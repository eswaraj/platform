package com.next.eswaraj.admin.jsf.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.core.service.FileService;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.next.eswaraj.admin.jsf.convertor.CategoryConvertor;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class DepartmentBean extends BaseBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AwsUploadUtil awsImageUploadUtil;

    @Autowired
    private FileService fileService;

    private TreeNode root;

    private TreeNode selectedNode;

    private TreeNode selectedLocationNode;

    private MapModel draggableModel;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Double lat;
    private Double lng;
    private List<LocationBoundaryFile> locationBoundaryFiles;
    
    private LocationBoundaryFile selectedKml;
    
    //
    @Autowired
    private CategoryConvertor categoryConvertor;
    private Category selectedCategory;

    @PostConstruct
    public void init() {
        Location location;
        try {
            categoryConvertor.setCategories(adminService.getAllRootCategories());
            
            draggableModel = new DefaultMapModel();
            logger.info("Getting Location From DB");
            location = adminService.getRootLocationForSwarajIndia();
            logger.info("Got  Location From DB : " + location);
            root = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
            TreeNode topLocation = new CustomTreeNode(new Document(location.getName(), "-", "Folder", location), root);
            topLocation.setSelected(true);
            selectedLocationNode = topLocation;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleCategoryChange(AjaxBehaviorEvent event) {
        System.out.println("handleCategoryChange here " + event.getClass());
        if (event instanceof ItemSelectEvent) {
            System.out.println("handleCategoryChange getItemIndex " + ((ItemSelectEvent) event).getItemIndex());
            System.out.println("handleCategoryChange getSeriesIndex " + ((ItemSelectEvent) event).getSeriesIndex());
        }
    }

    public void onNodeExpand(NodeExpandEvent event) {
        TreeNode nodeSelected = event.getTreeNode();
        loadNodeChild(nodeSelected);
    }

    private void loadNodeChild(TreeNode nodeSelected) {
        if (nodeSelected != null && nodeSelected.getChildCount() == 0) {

            try {
                Object data = nodeSelected.getData();
                if (data instanceof Document) {
                    Document document = ((Document) data);
                    Location selectedLocation = document.getLocation();
                    List<LocationType> childLocationTypes = adminService.getChildLocationsTypeOfParent(selectedLocation.getLocationType().getId());
                    if (childLocationTypes.isEmpty() || childLocationTypes.size() == 1) {
                        List<Location> childLocations = adminService.getChildLocationsOfParent(((Document) nodeSelected.getData()).getLocation().getId());
                        for (Location oneLocation : childLocations) {
                            new CustomTreeNode(new Document(oneLocation.getName(), "-", "Folder", oneLocation), nodeSelected);
                        }
                    } else {
                        for (LocationType oneLocationType : childLocationTypes) {
                            new CustomTreeNode(new LocationTypeDocument(oneLocationType.getName() + "(s)", "-", "mp3", oneLocationType), nodeSelected);
                        }
                    }

                }
                if (data instanceof LocationTypeDocument) {
                    LocationTypeDocument locationTypeDocument = ((LocationTypeDocument) nodeSelected.getData());
                    LocationType selectedLocationType = locationTypeDocument.getLocationType();
                    TreeNode parentNode = nodeSelected.getParent();
                    Document document = ((Document) parentNode.getData());

                    Location selectedParentLocation = document.getLocation();
                    List<Location> childLocations = adminService.findLocationByParentLocationAndLocationType(selectedParentLocation.getId(), selectedLocationType.getId());
                    for (Location oneLocation : childLocations) {
                        new CustomTreeNode(new Document(oneLocation.getName(), "-", "Folder", oneLocation), nodeSelected);
                    }
                }

            } catch (ApplicationException e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", e.getMessage());
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Collapsed", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private LocationBoundaryFile getActiveLocationBoundaryFile(Location location) throws ApplicationException {
        LocationBoundaryFile activeLocationBoundaryFile = null;
        if (location != null) {
            locationBoundaryFiles = adminService.getLocationBoundaryFiles(location.getId());
            System.out.println("locationBoundaryFiles = " + locationBoundaryFiles);
            if (!locationBoundaryFiles.isEmpty()) {
                System.out.println("Select Active File = ");
                
                for (LocationBoundaryFile oneLocationBoundaryFile : locationBoundaryFiles) {
                    activeLocationBoundaryFile = oneLocationBoundaryFile;
                    if (oneLocationBoundaryFile.isActive()) {
                        break;
                    }
                }
            }
        }
        return activeLocationBoundaryFile;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        TreeNode nodeSelected = event.getTreeNode();
        Object data = nodeSelected.getData();
        if (data instanceof Document) {
            selectedLocationNode = nodeSelected;
            draggableModel.getMarkers().clear();
            draggableModel.getPolygons().clear();
            Document document = (Document) data;


            try {
                Location location = document.getLocation();
                refreshKmls(location);
                List<LocationType> locationTypes = adminService.getChildLocationsTypeOfParent(((Document) nodeSelected.getData()).getLocation().getLocationType().getId());
                createButtonsAndMenus(locationTypes, document.getLocation());
            } catch (ApplicationException e) {
                e.printStackTrace();
            } finally {
                disableSaveCancelButtons(false);
            }
        } else {
            selectedLocationNode = nodeSelected.getParent();
            System.out.println("selectedLocationNode = " + selectedLocationNode);
            LocationType selectedLocationType = ((LocationTypeDocument) nodeSelected.getData()).getLocationType();
            List<LocationType> locationTypes = new ArrayList<>(1);
            locationTypes.add(selectedLocationType);
            System.out.println("createButtonsAndMenus for = " + selectedLocationType);
            createButtonsAndMenus(locationTypes, ((Document) selectedLocationNode.getData()).getLocation());
            disableSaveCancelButtons(true);
            System.out.println("Done");
        }
    }

    private void refreshKmls(Location location) throws ApplicationException {
        selectedKml = getActiveLocationBoundaryFile(location);
        createMarkerAndKmlBoundary(location, selectedKml);
    }

    private void createMarkerAndKmlBoundary(Location location, LocationBoundaryFile locationBoundaryFile) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        logger.info("lat={}", lat);
        logger.info("lng={}", lng);
        if (lat == null || lat == 0.0) {
            lat = 28.871187;
            location.setLatitude(lat);
        }
        if (lng == null || lng == 0.0) {
            lng = 77.095337;
            location.setLongitude(lng);
        }
        logger.info("lat={}", lat);
        logger.info("lng={}", lng);

        LatLng coord1 = new LatLng(lat, lng);

        draggableModel.getMarkers().clear();
        draggableModel.getPolygons().clear();
        // Draggable
        draggableModel.addOverlay(new Marker(coord1, location.getName()));
        for (Marker premarker : draggableModel.getMarkers()) {
            premarker.setDraggable(true);
        }
        try {
            if (locationBoundaryFile != null) {
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    org.w3c.dom.Document doc = dBuilder.parse(locationBoundaryFile.getFileNameAndPath());
                    // org.w3c.dom.Document doc =
                    // dBuilder.parse("https://s3-us-west-2.amazonaws.com/eswaraj-dev/locations/72848/41b8ccc9-9b3b-435d-9d20-a1217703539b_201409111918.kml");
                    NodeList coordinates = doc.getElementsByTagName("coordinates");
                    for (int temp = 0; temp < coordinates.getLength(); temp++) {

                        Node nNode = coordinates.item(temp);

                        System.out.println("\nCurrent Element :" + nNode.getNodeName());

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;

                            String coordinatesStr = eElement.getTextContent();
                            System.out.println("Cooridinates : " + coordinatesStr);
                            Polygon polygon = new Polygon();
                            String[] latLngs = coordinatesStr.split(" ");
                            for (String oneLatLng : latLngs) {

                                String[] ll = oneLatLng.split(",");
                                polygon.getPaths().add(new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0])));
                            }

                            polygon.setStrokeColor("#FF9900");
                            polygon.setFillColor("#FF9900");
                            polygon.setStrokeOpacity(0.7);
                            polygon.setFillOpacity(0.7);

                            draggableModel.addOverlay(polygon);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            disableSaveCancelButtons(false);
        }
    }

    public void onStateChange(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();
        ((Document) selectedLocationNode.getData()).getLocation().setDepth(zoomLevel);
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        Marker marker = event.getMarker();
        logger.info("onMarkerDrag");
        if (selectedNode.getData() instanceof Document) {
            logger.info("Updating Lat Long {} {} ", marker.getLatlng().getLat(), marker.getLatlng().getLng());
            lat = marker.getLatlng().getLat();
            lng = marker.getLatlng().getLng();
            Document document = (Document) selectedNode.getData();
            document.getLocation().setLatitude(marker.getLatlng().getLat());
            document.getLocation().setLongitude(marker.getLatlng().getLng());
        }

    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void saveLocation() {
        System.out.println("saving Location " + selectedNode.getData());
        Document document = (Document) selectedNode.getData();
        document.setName(document.getLocation().getName());
        try {
            System.out.println("LocationType " + document.getLocation().getLocationType());
            Location location = adminService.saveLocation(document.getLocation());
            document.setLocation(location);
        } catch (Exception e) {
            sendErrorMessage("Error", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void refreshLocation() {
        System.out.println("Refresh Location " + selectedNode.getData());
        Document document = (Document) selectedNode.getData();
        try {
            System.out.println("LocationType " + document.getLocation().getLocationType());
            Location location = adminService.getLocationById(document.getLocation().getId());
            document.setLocation(location);
            refreshKmls(location);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }

    private void disableSaveCancelButtons(boolean disabled) {
        CommandButton saveLocationButton = (CommandButton) FacesContext.getCurrentInstance().getViewRoot().findComponent("location_form:saveLocation");
        CommandButton canceButton = (CommandButton) FacesContext.getCurrentInstance().getViewRoot().findComponent("location_form:cancel");
        saveLocationButton.setDisabled(disabled);
        canceButton.setDisabled(disabled);

    }

    private String getImageType(FileUploadEvent event) {
        String imageType = ".jpg";
        if ("image/png".equals(event.getFile().getContentType())) {
            imageType = ".png";
        }
        if ("image/jpeg".equals(event.getFile().getContentType())) {
            imageType = ".jpg";
        }
        return imageType;
    }

    public void handleHeaderFileUpload(FileUploadEvent event) {
        String imageType = getImageType(event);
        Document document = (Document) selectedNode.getData();
        Location location = document.getLocation();
        String remoteFileName = location.getId() + "_header" + imageType;
        try {
            String httpFilePath = awsImageUploadUtil.uploadLocationHeaderImage(remoteFileName, event.getFile().getInputstream(), imageType);
            location.setMobileHeaderImageUrl(httpFilePath);
            location = adminService.saveLocation(location);
            document.setLocation(location);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void handleKmlFileUpload(FileUploadEvent event) {
        Document document = (Document) selectedNode.getData();
        Location location = document.getLocation();
        try {
            adminService.createNewLocationBoundaryFile(location.getId(), event.getFile().getFileName(), event.getFile().getInputstream(), fileService);
            locationBoundaryFiles = adminService.getLocationBoundaryFiles(document.getLocation().getId());
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void reprocessKmlFile() {
        try {
            logger.info("Selected KML : {}", selectedKml);
            adminService.reprocessLocationFile(selectedKml);
            FacesMessage message = new FacesMessage("Succesful", "File sent for reprocessing");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", "Unable to reporcess file");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    private void createButtonsAndMenus(List<LocationType> locationTypes, Location selectedLocation) {

        UIComponent component = FacesContext.getCurrentInstance().getViewRoot().findComponent("location_form:buttonPanel");
        UIComponent contextMenu = FacesContext.getCurrentInstance().getViewRoot().findComponent("location_form:contextMenu");
        component.getChildren().clear();
        contextMenu.getChildren().clear();
        FacesContext facesCtx = FacesContext.getCurrentInstance();
        ELContext elContext = facesCtx.getELContext();
        Application app = facesCtx.getApplication();
        ExpressionFactory elFactory = app.getExpressionFactory();
        System.out.println("component=" + component);
        System.out.println("locationTypes=" + locationTypes);
        for (LocationType oneLocationType : locationTypes) {
            CommandButton submit = new CommandButton();
            submit.setValue("Create " + oneLocationType.getName());
            submit.setUpdate(":location_form");
            submit.setId("create" + oneLocationType.getId());
            MethodExpression methodExpression = elFactory.createMethodExpression(elContext, "#{locationBean.createChildLocation( " + oneLocationType.getId() + "," + selectedLocation.getId()
                    + ")}",
                    null, new Class[] {});
            submit.setActionExpression(methodExpression);
            // createButtons.getChildren().add(submit);
            if (component != null) {
                component.getChildren().add(submit);
            }

            UIMenuItem menuItem = new UIMenuItem();
            // menuItem.setParam("value","Create City");
            menuItem.setValue("Create " + oneLocationType.getName());
            menuItem.setUpdate("location_form");
            menuItem.setId("createMenu" + oneLocationType.getId());
            methodExpression = elFactory.createMethodExpression(elContext, "#{locationBean.createChildLocation(" + oneLocationType.getId() + "," + selectedLocation.getId() + ")}", null,
                    new Class[] {});
            menuItem.setActionExpression(methodExpression);
            if (contextMenu != null) {
                contextMenu.getChildren().add(menuItem);
            }
        }

    }
    public void cancel() {
        if (selectedLocationNode != null) {
            if (((Document) selectedLocationNode.getData()).getLocation().getId() == null) {
                TreeNode parentNode = selectedLocationNode.getParent();
                selectedLocationNode.getChildren().clear();
                selectedLocationNode.getParent().getChildren().remove(selectedLocationNode);
                selectedLocationNode.setParent(null);
                selectedLocationNode = null;
                if (parentNode.getData() instanceof LocationTypeDocument) {
                    selectedLocationNode = parentNode.getParent();
                } else {
                    selectedLocationNode = parentNode;
                }
                selectedLocationNode.setSelected(true);
            }
        }
    }
    public void createChildLocation(Long locationTypeId, Long locationId) {
        System.out.println("Creating SOmething : " + locationTypeId + ", " + locationId);
        if (selectedLocationNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Creating under ", selectedNode.getData().toString());
            FacesContext.getCurrentInstance().addMessage(null, message);
            loadNodeChild(selectedLocationNode);
            TreeNode parentNode = getParentNodeForNewChild(selectedLocationNode, locationTypeId);
            parentNode.setExpanded(true);
            Location location = new Location();
            LocationType locationType;
            try {
                System.out.println("Getting LocationType " + locationTypeId);
                Location parentLocation = adminService.getLocationById(locationId);
                locationType = adminService.getLocationTypeById(locationTypeId);
                location.setLocationType(locationType);
                location.setParentLocation(parentLocation);
                System.out.println("locationType = " + locationType);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            location.setName("New");
            location.setUrlIdentifier("");
            selectedNode.setExpanded(true);
            selectedNode.setSelected(false);
            selectedNode = new CustomTreeNode(new Document("NEW", "-", "Folder", location), parentNode);
            selectedNode.setSelected(true);
            selectedLocationNode = selectedNode;
            disableSaveCancelButtons(false);
        }
    }

    private TreeNode getParentNodeForNewChild(TreeNode selectedLocationNode, Long locationTypeId) {
        List<TreeNode> childrenNodes = selectedLocationNode.getChildren();
        if (childrenNodes.isEmpty()) {
            return selectedLocationNode;
        }
        if(childrenNodes.get(0).getData() instanceof Document){
            return selectedLocationNode;
        }
                
        for(TreeNode oneChildNode : childrenNodes){
            System.out.println(((LocationTypeDocument) oneChildNode.getData()).getLocationType().getId() + "== " + locationTypeId);
            if (((LocationTypeDocument) oneChildNode.getData()).getLocationType().getId().equals(locationTypeId)) {
                System.out.println("Found Parent Node");
                return oneChildNode;
            }
        }
        return selectedLocationNode;
    }

    public void displaySelectedSingle() {
        if (selectedNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getData().toString());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void deleteNode() {
        selectedNode.getChildren().clear();
        selectedNode.getParent().getChildren().remove(selectedNode);
        selectedNode.setParent(null);

        selectedNode = null;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public MapModel getDraggableModel() {
        return draggableModel;
    }

    public void setDraggableModel(MapModel draggableModel) {
        this.draggableModel = draggableModel;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public List<LocationBoundaryFile> getLocationBoundaryFiles() {
        return locationBoundaryFiles;
    }

    public void setLocationBoundaryFiles(List<LocationBoundaryFile> locationBoundaryFiles) {
        this.locationBoundaryFiles = locationBoundaryFiles;
    }

    public TreeNode getSelectedLocationNode() {
        return selectedLocationNode;
    }

    public void setSelectedLocationNode(TreeNode selectedLocationNode) {
        this.selectedLocationNode = selectedLocationNode;
    }

    public LocationBoundaryFile getSelectedKml() {
        return selectedKml;
    }

    public void setSelectedKml(LocationBoundaryFile selectedKml) {
        this.selectedKml = selectedKml;

    }

    public void onKmlRowSelect(SelectEvent event) {
        logger.info("onKmlRowSelect : updating Boundary layer");
        Object data = selectedNode.getData();
        if (data instanceof Document) {
            Document document = ((Document) data);
            Location selectedLocation = document.getLocation();
            createMarkerAndKmlBoundary(selectedLocation, selectedKml);
        }
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
    }
}
