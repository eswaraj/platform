package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
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
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.LocationType;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class LocationBean {

    @Autowired
    private AdminService adminService;

    private TreeNode root;

    private TreeNode selectedNode;

    private MapModel draggableModel;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Double lat;
    private Double lng;
    private List<LocationBoundaryFile> locationBoundaryFiles;

    @PostConstruct
    public void init() {
        Location location;
        try {
            draggableModel = new DefaultMapModel();
            logger.info("Getting Location From DB");
            location = adminService.getRootLocationForSwarajIndia();
            logger.info("Got  Location From DB : " + location);
            root = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
            TreeNode topLocation = new CustomTreeNode(new Document(location.getName(), "-", "Folder", location), root);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onNodeExpand(NodeExpandEvent event) {
        TreeNode nodeSelected = event.getTreeNode();
        if (nodeSelected != null && nodeSelected.getChildCount() == 0) {

            try {
                List<Location> childLocations = adminService.getChildLocationsOfParent(((Document) nodeSelected.getData()).getLocation().getId());
                for (Location oneLocation : childLocations) {
                    new CustomTreeNode(new Document(oneLocation.getName(), "-", "Folder", oneLocation), nodeSelected);
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

    public void onNodeSelect(NodeSelectEvent event) {
        TreeNode nodeSelected = event.getTreeNode();
        Object data = nodeSelected.getData();
        if (data instanceof Document) {
            draggableModel.getMarkers().clear();
            Document document = (Document) data;
            lat = document.getLocation().getLatitude();
            lng = document.getLocation().getLongitude();
            if (lat == null) {
                lat = 21.1289956;
            }
            if (lng == null) {
                lng = 82.7792201;
            }

            LatLng coord1 = new LatLng(lat, lng);

            // Draggable
            draggableModel.addOverlay(new Marker(coord1, document.getLocation().getName()));
            for (Marker premarker : draggableModel.getMarkers()) {
                premarker.setDraggable(true);
            }
            try {
                locationBoundaryFiles = adminService.getLocationBoundaryFiles(document.getLocation().getId());
                System.out.println("locationBoundaryFiles = " + locationBoundaryFiles);
                if (!locationBoundaryFiles.isEmpty())
                {
                    System.out.println("Select Active File = ");
                    LocationBoundaryFile activeLocationBoundaryFile = null;
                    for (LocationBoundaryFile oneLocationBoundaryFile : locationBoundaryFiles) {
                        activeLocationBoundaryFile = oneLocationBoundaryFile;
                        if (oneLocationBoundaryFile.isActive()) {
                            break;
                        }
                    }
                    System.out.println("activeLocationBoundaryFile = " + activeLocationBoundaryFile);
                    System.out.println("Creating Polygon");
                    if (activeLocationBoundaryFile != null)
                    {
                        try {
                            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                            org.w3c.dom.Document doc = dBuilder.parse(activeLocationBoundaryFile.getFileNameAndPath());
                            //org.w3c.dom.Document doc = dBuilder.parse("https://s3-us-west-2.amazonaws.com/eswaraj-dev/locations/72848/41b8ccc9-9b3b-435d-9d20-a1217703539b_201409111918.kml");
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
                                    int count = 0;
                                    for (String oneLatLng : latLngs) {

                                        String[] ll = oneLatLng.split(",");
                                        polygon.getPaths().add(new LatLng(Double.parseDouble(ll[1]), Double.parseDouble(ll[0])));
                                        count++;
                                        System.out.println(count + ". oneLatLng : " + oneLatLng);
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
                }
            } catch (ApplicationException e) {
                e.printStackTrace();
            }
            createButtons();
        }
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        Marker marker = event.getMarker();
        if (selectedNode.getData() instanceof Document) {
            Document document = (Document) selectedNode.getData();
            document.getLocation().setLatitude(marker.getLatlng().getLat());
            document.getLocation().setLongitude(marker.getLatlng().getLng());
        }

    }
    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void createButtons() {
        try {
            List<LocationType> locationTypes = adminService.getChildLocationsTypeOfParent(((Document) selectedNode.getData()).getLocation().getLocationType().getId());
            UIComponent component = FacesContext.getCurrentInstance().getViewRoot().findComponent("location_form:buttonPanel");
            component.getChildren().clear();
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ELContext elContext = facesCtx.getELContext();
            Application app = facesCtx.getApplication();
            ExpressionFactory elFactory = app.getExpressionFactory();
            System.out.println("component=" + component);
            System.out.println("locationTypes=" + locationTypes);
            for (LocationType oneLocationType : locationTypes) {
                CommandButton submit = new CommandButton();
                submit.setValue("Create " + oneLocationType.getName());
                submit.setUpdate("location_form");
                submit.setId("create" + oneLocationType.getId());
                MethodExpression methodExpression = elFactory.createMethodExpression(elContext, "#{locationBean.createSomething(" + oneLocationType.getId() + ")}", null, new Class[] {});
                submit.setActionExpression(methodExpression);
                // createButtons.getChildren().add(submit);
                if (component != null) {
                    component.getChildren().add(submit);
                }

            }
        } catch (ApplicationException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void createSomething(Long id) {
        System.out.println("Creating SOmething");
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
}
