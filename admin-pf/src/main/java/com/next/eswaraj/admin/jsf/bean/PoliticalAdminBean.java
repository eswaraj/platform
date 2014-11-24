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

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.menuitem.UIMenuItem;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.event.map.StateChangeEvent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.map.LatLngBounds;
import org.primefaces.model.map.Marker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationType;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class PoliticalAdminBean {

    @Autowired
    private AdminService adminService;

    private TreeNode root;

    private TreeNode selectedNode;

    private TreeNode selectedLocationNode;

    private LocationSearchResult selectedLocationSearchResult;

    private List<LocationSearchResult> locationSearchResults;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        Location location;
        try {
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

    public List<LocationSearchResult> searchLocation(String query) {
        try {
            locationSearchResults = adminService.searchLocationByName(query);
            return locationSearchResults;
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        return null;

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

    public void onNodeSelect(NodeSelectEvent event) {
        TreeNode nodeSelected = event.getTreeNode();
        Object data = nodeSelected.getData();
        if (data instanceof Document) {
            selectedLocationNode = nodeSelected;
            Document document = (Document) data;

            try {
                List<LocationType> locationTypes = adminService.getChildLocationsTypeOfParent(((Document) nodeSelected.getData()).getLocation().getLocationType().getId());
                createTabs(locationTypes, document.getLocation());
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
            createTabs(locationTypes, ((Document) selectedLocationNode.getData()).getLocation());
            disableSaveCancelButtons(true);
            System.out.println("Done");
        }
    }

    public void onStateChange(StateChangeEvent event) {
        LatLngBounds bounds = event.getBounds();
        int zoomLevel = event.getZoomLevel();
        ((Document) selectedLocationNode.getData()).getLocation().setDepth(zoomLevel);
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

    public void saveLocation() {
        System.out.println("saving Location " + selectedNode.getData());
        Document document = (Document)selectedNode.getData();
        document.setName(document.getLocation().getName());
        try {
            System.out.println("LocationType " + document.getLocation().getLocationType());
            Location location = adminService.saveLocation(document.getLocation());
            document.setLocation(location);
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

    private void createTabs(List<LocationType> locationTypes, Location selectedLocation) {

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

    public TreeNode getSelectedLocationNode() {
        return selectedLocationNode;
    }

    public void setSelectedLocationNode(TreeNode selectedLocationNode) {
        this.selectedLocationNode = selectedLocationNode;
    }

    public LocationSearchResult getSelectedLocationSearchResult() {
        return selectedLocationSearchResult;
    }

    public void setSelectedLocationSearchResult(LocationSearchResult selectedLocationSearchResult) {
        this.selectedLocationSearchResult = selectedLocationSearchResult;
    }

    public List<LocationSearchResult> getLocationSearchResults() {
        return locationSearchResults;
    }

    public void setLocationSearchResults(List<LocationSearchResult> locationSearchResults) {
        this.locationSearchResults = locationSearchResults;
    }
}
