package com.next.eswaraj.admin.jsf.bean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
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
import com.eswaraj.domain.nodes.Address;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.domain.nodes.Department;
import com.eswaraj.domain.nodes.Location;
import com.eswaraj.domain.nodes.LocationBoundaryFile;
import com.eswaraj.domain.nodes.Person;
import com.next.eswaraj.admin.jsf.convertor.PersonConvertor;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "view")
public class DepartmentBean extends BaseBean {

    private static final long serialVersionUID = 1L;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PersonConvertor personConvertor;

    private TreeNode root;
    private TreeNode locationRoot;
    private TreeNode categoryRoot;

    private TreeNode selectedDepartmentNode;
    private TreeNode[] selectedLocationNodes;
    private TreeNode[] selectedCategoryNodes;

    private MapModel draggableModel;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private Double lat;
    private Double lng;

    private List<Person> staffMembers;
    private Person selectedStaffMember;
    private List<Person> personSearchResults;
    //
    @Autowired
    private ApplicationCacheBean applicationCacheBean;

    @PostConstruct
    public void init() {
        try {
            // Refresh all locations in cache
            // applicationCacheBean.refreshLocations();
            draggableModel = new DefaultMapModel();
            defaultLatLong();
            createMarker();

            loadLocations();
            loadCategories();
            loadDepartments();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadCategories() throws ApplicationException {
        categoryRoot = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
        List<Category> allCategories = adminService.getAllCategories();
        System.out.println("Found " + allCategories.size() + " locations");
        Map<String, Object> locationMap = new HashMap<String, Object>();
        List<Category> rootCategories = new ArrayList<Category>();
        for (Category oneCategory : allCategories) {
            locationMap.put(oneCategory.getId().toString(), oneCategory);
            if (oneCategory.isRoot()) {
                rootCategories.add(oneCategory);
            } else {
                String categoryChildKey = oneCategory.getParentCategory().getId() + "_Children";
                Set<Category> childCategories = (Set<Category>) locationMap.get(categoryChildKey);
                if (childCategories == null) {
                    childCategories = new TreeSet<Category>(new Comparator<Category>() {
                        @Override
                        public int compare(Category o1, Category o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    locationMap.put(categoryChildKey, childCategories);
                }
                childCategories.add(oneCategory);
            }
        }
        if (rootCategories.isEmpty()) {
            System.out.println("No Root Categories Found");
        } else {
            for (Category oneRootCategory : rootCategories) {
                TreeNode indiaRootNode = new CustomTreeNode(new CategoryDocument(oneRootCategory.getName(), "-", "Folder", oneRootCategory), categoryRoot);
                addChildCategories(oneRootCategory, locationMap, indiaRootNode);
            }

        }
    }
    private void loadLocations() throws ApplicationException {
        locationRoot = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
        List<Location> allLocations = applicationCacheBean.getAllLocations();
        System.out.println("Found " + allLocations.size() + " locations");
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
                    childLocations = new TreeSet<Location>(new Comparator<Location>() {
                        @Override
                        public int compare(Location o1, Location o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    locationMap.put(locationChildKey, childLocations);
                }
                childLocations.add(oneLocation);
            }
        }
        if (rootLocation == null) {
            System.out.println("No Root Location Found");
        } else {
            TreeNode indiaRootNode = new CustomTreeNode(new Document(rootLocation.getName(), "-", "Folder", rootLocation), locationRoot);
            addChildLocations(rootLocation, locationMap, indiaRootNode);
        }
    }

    private void addChildCategories(Category currentCategory, Map<String, Object> locationMap, TreeNode parentTreeNode) {
        String categoryChildKey = currentCategory.getId() + "_Children";
        Set<Category> childCategories = (Set<Category>) locationMap.get(categoryChildKey);
        if (childCategories == null) {
            return;
        }
        for (Category oneCategory : childCategories) {
            TreeNode newNode = new CustomTreeNode(new CategoryDocument(oneCategory.getName(), "-", "Folder", oneCategory), parentTreeNode);
            newNode.setSelectable(!oneCategory.isRoot());// Root Categories can not be selected
            addChildCategories(oneCategory, locationMap, newNode);
        }
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

    private void loadDepartments() throws ApplicationException {
        List<Department> categoryDeaprtments = adminService.getAllRootDepartments();
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
            List<LocationBoundaryFile> locationBoundaryFiles = adminService.getLocationBoundaryFiles(location.getId());
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
        try {
            TreeNode nodeSelected = event.getTreeNode();
            selectedDepartmentNode = nodeSelected;
            System.out.println("selectedDepartmentNode= " + selectedDepartmentNode);
            Department department = ((DepartmentDocument) selectedDepartmentNode.getData()).getDepartment();
            draggableModel.getPolygons().clear();
            staffMembers = null;
            if (department.getId() != null) {
                // Do this only if Department has been saved
                List<Location> locations = adminService.getAllLocationsOfDepartment(department);
                // Select Locations in the tree
                Set<Long> locationIds = new HashSet<Long>();
                for (Location oneLocation : locations) {
                    locationIds.add(oneLocation.getId());
                }
                System.out.println("Lets Select Unselect TreeNode");
                for (TreeNode oneChildTreeNode : locationRoot.getChildren()) {
                    selectUnSelectLocationNode(oneChildTreeNode, locationIds);
                }

                List<Category> categories = adminService.getAllCategoriesOfDepartment(department);
                // Select Categories in the tree
                Set<Long> categoryIds = new HashSet<Long>();
                for (Category oneCategory : categories) {
                    categoryIds.add(oneCategory.getId());
                }
                System.out.println("Lets Select Unselect Category TreeNode");
                for (TreeNode oneChildTreeNode : categoryRoot.getChildren()) {
                    selectUnSelectCategoryNode(oneChildTreeNode, categoryIds);
                }
                staffMembers = adminService.getDepartmentStaffMembers(department);
            }


        } catch (Exception ex) {
            sendErrorMessage("Error", "Unable to Select Locations", ex);
        }
    }

    public void deleteStaffMember(Person person) {
        System.out.println("Remove Person as Staff " + person);
        DepartmentDocument document = (DepartmentDocument) selectedDepartmentNode.getData();
        try {
            adminService.deleteDepartmentStaff(document.getDepartment(), selectedStaffMember);
            sendInfoMessage("Success", "Staff Member " + selectedStaffMember.getName() + " removed successfully");
        } catch (ApplicationException e) {
            sendErrorMessage("Error", "Unable to add a Staff Member", e);
        }
    }

    private void selectUnSelectLocationNode(TreeNode treeNode, Set<Long> locationIds) throws ApplicationException {
        Location location = ((Document) treeNode.getData()).getLocation();
        if (locationIds.contains(location.getId())) {
            System.out.println("treeNode= " + treeNode);
            treeNode.setSelected(true);
            openAllParents(treeNode);
            createKmlBoundary(location);

        } else {
            treeNode.setSelected(false);
        }
        for (TreeNode oneChildTreeNode : treeNode.getChildren()) {
            selectUnSelectLocationNode(oneChildTreeNode, locationIds);
        }
    }

    private void selectUnSelectCategoryNode(TreeNode treeNode, Set<Long> categoryIds) throws ApplicationException {
        Category category = ((CategoryDocument) treeNode.getData()).getCategory();
        if (categoryIds.contains(category.getId())) {
            System.out.println("treeNode= " + treeNode);
            treeNode.setSelected(true);
            openAllParents(treeNode);
        } else {
            treeNode.setSelected(false);
        }
        for (TreeNode oneChildTreeNode : treeNode.getChildren()) {
            selectUnSelectCategoryNode(oneChildTreeNode, categoryIds);
        }
    }

    private void openAllParents(TreeNode treeNode) {
        if (treeNode.getParent() == null) {
            return;
        }
        if (!(treeNode.getParent().getData() instanceof Document)) {
            return;
        }
        treeNode.getParent().setExpanded(true);
        openAllParents(treeNode.getParent());
    }

    public void onLocationNodeSelect(NodeSelectEvent event) {
        TreeNode nodeSelected = event.getTreeNode();
        System.out.println("selectedLocationNodes= " + selectedLocationNodes);
        try {
            createKmlBoundary(((Document) nodeSelected.getData()).getLocation());
        } catch (ApplicationException e) {
            sendErrorMessage("Error", "unable to create Boundary", e);
        }
    }

    private void createKmlBoundary(Location location) throws ApplicationException {
        LocationBoundaryFile locationBoundaryFile = getActiveLocationBoundaryFile(location);
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

    public List<Person> searchPerson(String query) {
        try {
            logger.info("Searching Person for {}", query);
            personSearchResults = adminService.searchPersonByName(query);
            logger.info("personSearchResults {}", personSearchResults.size());
            personConvertor.setPersons(personSearchResults);
            return personSearchResults;
        } catch (Exception e) {
            sendErrorMessage("Error", "Unable to search person", e);
        }
        return null;

    }

    public void addStaffMember() {
        DepartmentDocument document = (DepartmentDocument) selectedDepartmentNode.getData();
        try {
            adminService.addDepartmentStaff(document.getDepartment(), selectedStaffMember);
            sendInfoMessage("Success", "Staff Member " + selectedStaffMember.getName() + " added successfully");
            staffMembers = adminService.getDepartmentStaffMembers(document.getDepartment());
        } catch (ApplicationException e) {
            sendErrorMessage("Error", "Unable to add a Staff Member", e);
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
        if (selectedDepartmentNode.getData() instanceof DepartmentDocument) {
            logger.info("Updating Lat Long {} {} ", marker.getLatlng().getLat(), marker.getLatlng().getLng());
            lat = marker.getLatlng().getLat();
            lng = marker.getLatlng().getLng();
            DepartmentDocument document = (DepartmentDocument) selectedDepartmentNode.getData();
            document.getDepartment().getAddress().setLattitude(marker.getLatlng().getLat());
            document.getDepartment().getAddress().setLongitude(marker.getLatlng().getLng());
        }

    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void saveDepartment() {
        System.out.println("saving Department " + selectedDepartmentNode.getData());
        DepartmentDocument document = (DepartmentDocument) selectedDepartmentNode.getData();
        document.setName(document.getDepartment().getName());
        try {
            Department department = document.getDepartment();
            System.out.println("Department " + department);
            List<Location> locations = new ArrayList<Location>();
            for (TreeNode oneLocationTreeNode : selectedLocationNodes) {
                locations.add(((Document) oneLocationTreeNode.getData()).getLocation());
            }

            List<Category> categories = new ArrayList<Category>();
            for (TreeNode oneLocationTreeNode : selectedCategoryNodes) {
                categories.add(((CategoryDocument) oneLocationTreeNode.getData()).getCategory());
            }
            department = adminService.saveDepartment(department, locations, categories);
            document.setDepartment(department);
        } catch (Exception e) {
            sendErrorMessage("Error", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void refreshLocation() {
        System.out.println("Refresh Location " + selectedDepartmentNode.getData());
        Document document = (Document) selectedDepartmentNode.getData();
        try {
            System.out.println("LocationType " + document.getLocation().getLocationType());
            Location location = adminService.getLocationById(document.getLocation().getId());
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



    public void cancel() {
        if (selectedDepartmentNode != null) {
            if (((DepartmentDocument) selectedDepartmentNode.getData()).getDepartment().getId() == null) {
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
        try {
            boolean isRoot = false;
            TreeNode parentNode = selectedDepartmentNode;
            if (selectedDepartmentNode == null) {
                parentNode = root;
                isRoot = true;
            }
            // NOTE : currently we are not allowing to create any Department Hierarchy
            // So will always create Department as Root Department
            parentNode = root;
            Department department = new Department();
            department.setName("New");
            department.setRoot(isRoot);
            department.setAddress(new Address());
            if (!isRoot) {
                department.setParentDepartment(((DepartmentDocument) selectedDepartmentNode.getData()).getDepartment());
            }
            TreeNode newNode = new CustomTreeNode(new DepartmentDocument(department.getName(), "-", "Folder", department), parentNode);
            parentNode.setExpanded(true);
            newNode.setSelected(true);
            parentNode.setSelected(false);
            selectedDepartmentNode = newNode;
            // Clear all Location Selection
            clearAllLocationSelection();
            clearAllCategorySelection();
            staffMembers = null;
        } catch (Exception ex) {
            sendErrorMessage("Error", "Unable to create new Department", ex);
        }


    }

    private void clearAllLocationSelection() throws ApplicationException {
        Set<Long> locationIds = new HashSet<Long>();
        for (TreeNode oneChildTreeNode : locationRoot.getChildren()) {
            selectUnSelectLocationNode(oneChildTreeNode, locationIds);
        }
    }
    private void clearAllCategorySelection() throws ApplicationException {
        Set<Long> locationIds = new HashSet<Long>();
        for (TreeNode oneChildTreeNode : categoryRoot.getChildren()) {
            selectUnSelectCategoryNode(oneChildTreeNode, locationIds);
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
        if (selectedDepartmentNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", selectedDepartmentNode.getData().toString());
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public void deleteNode() {
        selectedDepartmentNode.getChildren().clear();
        selectedDepartmentNode.getParent().getChildren().remove(selectedDepartmentNode);
        selectedDepartmentNode.setParent(null);

        selectedDepartmentNode = null;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
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

    public TreeNode[] getSelectedLocationNodes() {
        return selectedLocationNodes;
    }

    public void setSelectedLocationNodes(TreeNode[] selectedLocationNodes) {
        System.out.println("setSelectedLocationNodes= " + selectedLocationNodes);
        this.selectedLocationNodes = selectedLocationNodes;
    }

    public List<Person> getStaffMembers() {
        return staffMembers;
    }

    public void setStaffMembers(List<Person> staffMembers) {
        this.staffMembers = staffMembers;
    }

    public Person getSelectedStaffMember() {
        return selectedStaffMember;
    }

    public void setSelectedStaffMember(Person selectedStaffMember) {
        this.selectedStaffMember = selectedStaffMember;
    }

    public TreeNode getCategoryRoot() {
        return categoryRoot;
    }

    public void setCategoryRoot(TreeNode categoryRoot) {
        this.categoryRoot = categoryRoot;
    }

    public TreeNode[] getSelectedCategoryNodes() {
        return selectedCategoryNodes;
    }

    public void setSelectedCategoryNodes(TreeNode[] selectedCategoryNodes) {
        this.selectedCategoryNodes = selectedCategoryNodes;
    }
}
