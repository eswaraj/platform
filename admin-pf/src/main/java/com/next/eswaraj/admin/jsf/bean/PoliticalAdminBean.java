package com.next.eswaraj.admin.jsf.bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.SelectEvent;
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
import com.eswaraj.domain.nodes.Party;
import com.eswaraj.domain.nodes.Person;
import com.eswaraj.domain.nodes.PoliticalBodyAdmin;
import com.eswaraj.domain.nodes.PoliticalBodyType;
import com.eswaraj.domain.nodes.extended.LocationSearchResult;
import com.eswaraj.domain.nodes.extended.PoliticalBodyAdminExtended;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.queue.service.aws.impl.AwsImageUploadUtil;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class PoliticalAdminBean extends BaseBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private AwsImageUploadUtil awsImageUploadUtil;

    private TreeNode root;

    private TreeNode selectedNode;

    private TreeNode selectedLocationNode;

    private LocationSearchResult selectedLocationSearchResult;

    private List<LocationSearchResult> locationSearchResults;

    private List<Person> personSearchResults;

    private List<PoliticalBodyType> politicalBodyTypes;

    private PoliticalBodyType selectedPoliticalBodyType;

    private DataTable dataTable;
    
    private List<PoliticalBodyAdminExtended> politicalBodyAdmins;

    private PoliticalBodyAdmin selectedPoliticalBodyAdmin = new PoliticalBodyAdmin();

    private boolean enableRightSidePanel = true;

    private boolean showListPanel = true;

    private Person selectedPerson = new Person();
    
    private Party selectedPoliticalParty;

    private String createAdminButtonTitle = "Create";

    private boolean updateMode = false;

    Logger logger = LoggerFactory.getLogger(this.getClass());
/*
 * 
    private String biodata;
    private Date dob;
    private String gender;
    private String profilePhoto;
    private String voterId;
    private Address address;
 */
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
            selectNode(selectedLocationNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createPerson() {
        logger.info("Creating New Person");
        selectedPerson = new Person();
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

    public List<Person> searchPerson(String query) {
        try {
            personSearchResults = adminService.searchPersonByName(query);
            return personSearchResults;
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
        selectNode(nodeSelected);

    }

    public void handleFileUpload(FileUploadEvent event) {

        String imageType = ".jpg";
        String remoteFileName = selectedPerson.getId() + imageType;
        try {
            String httpFilePath = awsImageUploadUtil.uploadProfileImage(remoteFileName, event.getFile().getInputstream());
            selectedPerson.setProfilePhoto(httpFilePath);
            selectedPerson = adminService.savePerson(selectedPerson);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    private void selectNode(TreeNode nodeSelected) {
        showListPanel = true;
        Object data = nodeSelected.getData();
        if (data instanceof Document) {
            enableRightSidePanel = true;
            selectedLocationNode = nodeSelected;
            selectedNode = nodeSelected;

            try {
                politicalBodyTypes = adminService.getAllPoliticalBodyTypeOfLocation(((Document) nodeSelected.getData()).getLocation().getId());
                if (politicalBodyTypes.size() == 1) {
                    selectedPoliticalBodyType = politicalBodyTypes.get(0);
                    onSelectPoliticalBodyType();
                }
            } catch (ApplicationException e) {
                e.printStackTrace();
            } finally {
                disableSaveCancelButtons(false);
            }
        } else {
            enableRightSidePanel = false;
            disableCreateAdminButton = true;
            selectedLocationNode = nodeSelected.getParent();
            LocationType selectedLocationType = ((LocationTypeDocument) nodeSelected.getData()).getLocationType();
            List<LocationType> locationTypes = new ArrayList<>(1);
            locationTypes.add(selectedLocationType);
            disableSaveCancelButtons(true);
            politicalBodyAdmins = new ArrayList<PoliticalBodyAdminExtended>();
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

    public void savePerson(ActionEvent event) {
        logger.info("saving Person " + selectedPerson);

        try {
            selectedPerson = adminService.savePerson(selectedPerson);
            logger.info("Saved Person " + selectedPerson);
            queueService.sendRefreshPerson(selectedPerson.getId(), "web");
        } catch (Exception e) {
            sendErrorMessage("Error", e.getMessage());
            e.printStackTrace();
            logger.error("Error", e);
        }
    }

    public void saveAdmin(ActionEvent event) {
        logger.info("saving Admin " + selectedPoliticalBodyAdmin);
        Location location = ((Document) selectedLocationNode.getData()).getLocation();
        logger.info("Location " + location);
        logger.info("Person " + selectedPerson);
        logger.info("selectedPoliticalBodyType " + selectedPoliticalBodyType);
        selectedPoliticalBodyAdmin.setLocation(location);
        selectedPoliticalBodyAdmin.setPerson(selectedPerson);
        selectedPoliticalBodyAdmin.setPoliticalBodyType(selectedPoliticalBodyType);
        selectedPoliticalBodyAdmin.setParty(selectedPoliticalParty);
        try {
            if (selectedPoliticalBodyAdmin.getParty() == null) {
                sendErrorMessage("political_admin_form:politicalParty", "Error", "Please Select a Party");
            }
            if (isValidInput()) {
                if (selectedPoliticalBodyAdmin.getId() == null) {
                    selectedPoliticalBodyAdmin.setActive(true);
                }
                selectedPoliticalBodyAdmin = adminService.savePoliticalBodyAdmin(selectedPoliticalBodyAdmin);
                System.out.println("queueService=" + queueService);
                logger.warn("queueService=" + queueService);
                queueService.sendPoliticalBodyAdminUpdateMessage(selectedPoliticalBodyAdmin.getLocation().getId(), selectedPoliticalBodyAdmin.getId());
                showListPanel = true;
                sendInfoMessage("Success", "Admin Saved Succesfully");
                refreshPoliticalBodyAdmins();
            }
        } catch (Exception e) {
            sendErrorMessage("Error", e.getMessage());
            e.printStackTrace();
            logger.error("Error", e);
        }
    }

    public void cancelAdmin() {
        showListPanel = true;
    }

    private void disableSaveCancelButtons(boolean disabled) {
        /*
        CommandButton saveLocationButton = (CommandButton) FacesContext.getCurrentInstance().getViewRoot().findComponent("political_admin_form:savePerson");
        CommandButton canceButton = (CommandButton) FacesContext.getCurrentInstance().getViewRoot().findComponent("political_admin_form:cancel");
        saveLocationButton.setDisabled(disabled);
        canceButton.setDisabled(disabled);
        */

    }

    public void createAdmin() {
        showListPanel = false;
        selectedPoliticalBodyAdmin = new PoliticalBodyAdmin();
        selectedPerson = new Person();
        updateMode = false;
    }

    private boolean disableCreateAdminButton = true;

    private void refreshPoliticalBodyAdmins() {
        try {
            Document document = (Document) selectedLocationNode.getData();
            politicalBodyAdmins = adminService.getPoliticalAdminOfLocationAndAdminType(document.getLocation().getId(), selectedPoliticalBodyType.getId());
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
    }
    public void onSelectPoliticalBodyType() {
        if (selectedPoliticalBodyType != null) {

            refreshPoliticalBodyAdmins();
            disableCreateAdminButton = false;
            createAdminButtonTitle = "Create " + selectedPoliticalBodyType.getName();
        } else {
            disableCreateAdminButton = true;
            createAdminButtonTitle = "Create";
            politicalBodyAdmins = new ArrayList<PoliticalBodyAdminExtended>();
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

    public void displaySelectedSingle() {
        if (selectedNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedNode.getData().toString());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void onItemSelect(SelectEvent event) {
        LocationSearchResult locationSearchResult = (LocationSearchResult) event.getObject();
        System.out.println("Item Selected : " + locationSearchResult.getLocation().toString());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Item Selected", locationSearchResult.getLocation().toString()));
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

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public List<PoliticalBodyType> getPoliticalBodyTypes() {
        return politicalBodyTypes;
    }

    public void setPoliticalBodyTypes(List<PoliticalBodyType> politicalBodyTypes) {
        this.politicalBodyTypes = politicalBodyTypes;
    }

    public PoliticalBodyType getSelectedPoliticalBodyType() {
        return selectedPoliticalBodyType;
    }

    public void setSelectedPoliticalBodyType(PoliticalBodyType selectedPoliticalBodyType) {
        this.selectedPoliticalBodyType = selectedPoliticalBodyType;
        if (selectedPoliticalBodyType == null) {
            disableCreateAdminButton = true;
        }
    }

    public List<PoliticalBodyAdminExtended> getPoliticalBodyAdmins() {
        return politicalBodyAdmins;
    }

    public void setPoliticalBodyAdmins(List<PoliticalBodyAdminExtended> politicalBodyAdmins) {
        this.politicalBodyAdmins = politicalBodyAdmins;
    }

    public PoliticalBodyAdmin getSelectedPoliticalBodyAdmin() {
        return selectedPoliticalBodyAdmin;
    }

    public boolean isEditPersonAllowed() {
        if (selectedPerson == null || selectedPerson.getId() == null) {
            return false;
        }
        return true;
    }

    public void setSelectedPoliticalBodyAdmin(PoliticalBodyAdmin selectedPoliticalBodyAdmin) {
        try {
            System.out.println("selectedPoliticalBodyAdmin=" + selectedPoliticalBodyAdmin);
            this.selectedPoliticalBodyAdmin = selectedPoliticalBodyAdmin;
            this.selectedPerson = adminService.getPersonById(selectedPoliticalBodyAdmin.getPerson().getId());
            this.selectedPoliticalBodyType = adminService.getPoliticalBodyTypeById(selectedPoliticalBodyAdmin.getPoliticalBodyType().getId());
            if (selectedPoliticalBodyAdmin.getParty() != null) {
                this.selectedPoliticalParty = adminService.getPartyById(selectedPoliticalBodyAdmin.getParty().getId());
            }
            System.out.println("selectedPerson=" + selectedPerson);
            System.out.println("selectedPoliticalBodyType=" + selectedPoliticalBodyType);
            showListPanel = false;
            updateMode = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isEnableRightSidePanel() {
        return enableRightSidePanel;
    }

    public void setEnableRightSidePanel(boolean enableRightSidePanel) {
        this.enableRightSidePanel = enableRightSidePanel;
    }

    public boolean isShowListPanel() {
        return showListPanel;
    }

    public void setShowListPanel(boolean showListPanel) {
        this.showListPanel = showListPanel;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public List<Person> getPersonSearchResults() {
        return personSearchResults;
    }

    public void setPersonSearchResults(List<Person> personSearchResults) {
        this.personSearchResults = personSearchResults;
    }

    public boolean isDisableCreateAdminButton() {
        return disableCreateAdminButton;
    }

    public void setDisableCreateAdminButton(boolean disableCreateAdminButton) {
        this.disableCreateAdminButton = disableCreateAdminButton;
    }

    public String getCreateAdminButtonTitle() {
        return createAdminButtonTitle;
    }

    public void setCreateAdminButtonTitle(String createAdminButtonTitle) {
        this.createAdminButtonTitle = createAdminButtonTitle;
    }

    public Party getSelectedPoliticalParty() {
        return selectedPoliticalParty;
    }

    public void setSelectedPoliticalParty(Party selectedPoliticalParty) {
        this.selectedPoliticalParty = selectedPoliticalParty;
    }

    public boolean isUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }
}
