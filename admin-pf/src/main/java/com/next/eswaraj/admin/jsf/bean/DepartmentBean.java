package com.next.eswaraj.admin.jsf.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.next.eswaraj.admin.jsf.convertor.CategoryConvertor;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class DepartmentBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AwsUploadUtil awsImageUploadUtil;

    @Autowired
    private FileService fileService;

    private TreeNode root;
    private TreeNode locationRoot;

    private TreeNode selectedNode;

    private TreeNode selectedDepartmentNode;
    private TreeNode[] selectedDepartmentNodes;

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
        try {
            categoryConvertor.setCategories(adminService.getAllRootCategories());
            draggableModel = new DefaultMapModel();
            defaultLatLong();
            createMarker();
            loadLocations();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadLocations() throws ApplicationException {
        locationRoot = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
        List<Location> allLocations = adminService.getAllLocations();
        Map<String, Object> locationMap = new HashMap<String, Object>();
        Location rootLocation = null;
        for (Location oneLocation : allLocations) {
            locationMap.put(oneLocation.getId().toString(), oneLocation);
            if(oneLocation.getParentLocation() == null){
                rootLocation = oneLocation;
            }else{
                String locationChildKey = oneLocation.getParentLocation().getId()+"_Children";
                Set<Location> childLocations = (Set<Location>) locationMap.get(locationChildKey);
                if (childLocations == null) {
                    childLocations = new TreeSet<Location>();
                    locationMap.put(locationChildKey, childLocations);
                }
                childLocations.add(oneLocation);
            }
        }
        TreeNode indiaRootNode = new CustomTreeNode(new Document(rootLocation.getName(), "-", "Folder", rootLocation), root);
        addChildLocations(rootLocation, locationMap, indiaRootNode);
    }

    private void addChildLocations(Location currentLocation, Map<String, Object> locationMap, TreeNode parentTreeNode) {
        String locationChildKey = currentLocation.getId() + "_Children";
        Set<Location> childLocations = (Set<Location>) locationMap.get(locationChildKey);
        if (childLocations == null) {
            return;
        }
        for (Location oneLocation : childLocations) {
            TreeNode newNode = new CustomTreeNode(new Document(oneLocation.getName(), "-", "Folder", oneLocation), parentTreeNode);
            addChildLocations(oneLocation, locationMap, newNode);
        }
    }

    public void handleCategoryChange(AjaxBehaviorEvent event) {
        System.out.println("handleCategoryChange here " + event.getClass());
        System.out.println("selected Category " + selectedCategory);
        try {
            loadCategoryData(selectedCategory);
        } catch (ApplicationException e) {
            sendErrorMessage("Error Loading Catgeory", e.getMessage(), e);
        }
    }

    private void loadCategoryData(Category category) throws ApplicationException {
        List<Department> categoryDeaprtments = adminService.getAllRootDepartmentsOfcategory(category);
        System.out.println();
        root = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
        for (Department oneDepartment : categoryDeaprtments) {
            new CustomTreeNode(new DepartmentDocument(oneDepartment.getName(), "-", "Folder", oneDepartment), root);
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
                if (data instanceof DepartmentDocument) {
                    DepartmentDocument document = ((DepartmentDocument) data);
                    Department selectedDepartment = document.getDepartment();
                    List<Department> childDepartments = adminService.getAllChildDepartments(selectedDepartment);
                    for (Department oneDepartment : childDepartments) {
                        new CustomTreeNode(new DepartmentDocument(oneDepartment.getName(), "-", "Folder", oneDepartment), nodeSelected);
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
        selectedDepartmentNode = nodeSelected;
        System.out.println("selectedDepartmentNode= " + selectedDepartmentNode);
    }

    private void refreshKmls(Location location) throws ApplicationException {
        selectedKml = getActiveLocationBoundaryFile(location);
        createMarkerAndKmlBoundary(location, selectedKml);
    }

    private void defaultLatLong() {
        logger.info("lat={}", lat);
        logger.info("lng={}", lng);
        if (lat == null || lat == 0.0) {
            lat = 28.871187;
        }
        if (lng == null || lng == 0.0) {
            lng = 77.095337;
        }
        logger.info("lat={}", lat);
        logger.info("lng={}", lng);
    }

    private void createMarker() {
        LatLng coord1 = new LatLng(lat, lng);

        draggableModel.getMarkers().clear();
        draggableModel.getPolygons().clear();
        // Draggable
        Marker marker = new Marker(coord1, "Department Location");
        draggableModel.addOverlay(marker);
        for (Marker premarker : draggableModel.getMarkers()) {
            premarker.setDraggable(true);
        }
    }

    private void createMarkerAndKmlBoundary(Location location, LocationBoundaryFile locationBoundaryFile) {
        lat = location.getLatitude();
        lng = location.getLongitude();

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
        // ((DepartmentDocument) selectedDepartmentNode.getData()).getLocation().setDepth(zoomLevel);
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        Marker marker = event.getMarker();
        logger.info("onMarkerDrag");
        if (selectedNode.getData() instanceof DepartmentDocument) {
            logger.info("Updating Lat Long {} {} ", marker.getLatlng().getLat(), marker.getLatlng().getLng());
            lat = marker.getLatlng().getLat();
            lng = marker.getLatlng().getLng();
            DepartmentDocument document = (DepartmentDocument) selectedNode.getData();
            document.getDepartment().getAddress().setLattitude(marker.getLatlng().getLat());
            document.getDepartment().getAddress().setLongitude(marker.getLatlng().getLng());
        }

    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void saveDepartment() {
        System.out.println("saving Department " + selectedNode.getData());
        DepartmentDocument document = (DepartmentDocument) selectedNode.getData();
        document.setName(document.getDepartment().getName());
        try {
            Department department = document.getDepartment();
            System.out.println("Department " + department);
            department = adminService.saveDepartment(document.getDepartment());
            document.setDepartment(department);
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
        if (selectedDepartmentNode != null) {
            if (((Document) selectedDepartmentNode.getData()).getLocation().getId() == null) {
                TreeNode parentNode = selectedDepartmentNode.getParent();
                selectedDepartmentNode.getChildren().clear();
                selectedDepartmentNode.getParent().getChildren().remove(selectedDepartmentNode);
                selectedDepartmentNode.setParent(null);
                selectedDepartmentNode = null;
                if (parentNode.getData() instanceof LocationTypeDocument) {
                    selectedDepartmentNode = parentNode.getParent();
                } else {
                    selectedDepartmentNode = parentNode;
                }
                selectedDepartmentNode.setSelected(true);
            }
        }
    }

    public void createDepartment() {
        System.out.println("Creating Department : " + selectedDepartmentNode);
        boolean isRoot = false;
        TreeNode parentNode = selectedDepartmentNode;
        if (selectedDepartmentNode == null) {
            parentNode = root;
            isRoot = true;
        }
        Department department = new Department();
        department.setName("New");
        department.setRoot(isRoot);
        department.setCategory(selectedCategory);
        department.setAddress(new Address());
        if (!isRoot) {
            department.setParentDepartment(((DepartmentDocument) selectedDepartmentNode.getData()).getDepartment());
        }
        TreeNode newNode = new CustomTreeNode(new DepartmentDocument(department.getName(), "-", "Folder", department), parentNode);
        selectedDepartmentNode.setExpanded(true);
        newNode.setSelected(true);

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

    public TreeNode getSelectedDepartmentNode() {
        return selectedDepartmentNode;
    }

    public void setSelectedDepartmentNode(TreeNode selectedDepartmentNode) {
        this.selectedDepartmentNode = selectedDepartmentNode;
    }

    public TreeNode getLocationRoot() {
        return locationRoot;
    }

    public void setLocationRoot(TreeNode locationRoot) {
        this.locationRoot = locationRoot;
    }

    public TreeNode[] getSelectedDepartmentNodes() {
        return selectedDepartmentNodes;
    }

    public void setSelectedDepartmentNodes(TreeNode[] selectedDepartmentNodes) {
        this.selectedDepartmentNodes = selectedDepartmentNodes;
    }
}
