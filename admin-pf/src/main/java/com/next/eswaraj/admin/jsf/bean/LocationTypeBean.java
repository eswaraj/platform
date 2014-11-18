package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.LocationType;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class LocationTypeBean {

    @Autowired
    private AdminService adminService;

    private TreeNode root;

    private TreeNode selectedNode;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        LocationType locationType;
        try {
            logger.info("Getting Location From DB");
            locationType = adminService.getRootLocationType();
            logger.info("Got  Location Type From DB : " + locationType);
            root = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
            TreeNode topLocation = new CustomTreeNode(new LocationTypeDocument(locationType.getName(), "-", "Folder", locationType), root);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onNodeExpand(NodeExpandEvent event) {
        TreeNode nodeSelected = event.getTreeNode();
        if (nodeSelected.getChildCount() == 0) {
            LocationType locationType = ((LocationTypeDocument) nodeSelected.getData()).getLocationType();
            List<LocationType> childLocationTypes;
            try {
                childLocationTypes = adminService.getChildLocationsTypeOfParent(locationType.getId());

                for (LocationType oneChildLocationType : childLocationTypes) {
                    new CustomTreeNode(new LocationTypeDocument(oneChildLocationType.getName(), "-", "Folder", oneChildLocationType), nodeSelected);
                }
            } catch (ApplicationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Collapsed", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeSelect(NodeSelectEvent event) {

        // createButtons();
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
    }
    /*
    private void createButtons() {
        try {
            List<LocationTypeDto> locationTypes = adminService.getChildLocationsTypeOfParent(((Document) selectedNode.getData()).getLocation().getLocationTypeId());
            UIComponent component = FacesContext.getCurrentInstance().getViewRoot().findComponent("location_form:buttonPanel");
            FacesContext facesCtx = FacesContext.getCurrentInstance();
            ELContext elContext = facesCtx.getELContext();
            Application app = facesCtx.getApplication();
            ExpressionFactory elFactory = app.getExpressionFactory();
            System.out.println("component=" + component);
            System.out.println("locationTypes=" + locationTypes);
            for (LocationTypeDto oneLocationTypeDto : locationTypes) {
                CommandButton submit = new CommandButton();
                submit.setValue("Create " + oneLocationTypeDto.getName());
                submit.setUpdate("location_form");
                submit.setId("create" + oneLocationTypeDto.getName());
                MethodExpression methodExpression = elFactory.createMethodExpression(elContext, "#{locationBean.createSomething(" + oneLocationTypeDto.getId() + ")}", null, new Class[] {});
                submit.setActionExpression(methodExpression);
                // createButtons.getChildren().add(submit);
                if (component != null) {
                    component.getChildren().add(submit);
                }

            }
        } catch (ApplicationException e) {
            e.printStackTrace();
        }

    }
    */
    public void saveLocationType() {
        System.out.println("saving : " + ((LocationTypeDocument) selectedNode.getData()).getLocationType());
        try {
            LocationType locationType = ((LocationTypeDocument) selectedNode.getData()).getLocationType();
            ((LocationTypeDocument) selectedNode.getData()).setName(locationType.getName());
            if (!locationType.isRoot()) {
                TreeNode parentNode = selectedNode.getParent();
                locationType.setParentLocationType(((LocationTypeDocument) parentNode.getData()).getLocationType());
            }
            adminService.saveLocationType(locationType);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Location Type Saved Succesfully");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (ApplicationException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);

        }
    }

    public void cancel() {
        if (selectedNode != null) {
            if (((LocationTypeDocument) selectedNode.getData()).getLocationType().getId() == null) {
                TreeNode parentNode = selectedNode.getParent();
                selectedNode.getChildren().clear();
                selectedNode.getParent().getChildren().remove(selectedNode);
                selectedNode.setParent(null);
                selectedNode = null;
                parentNode.setSelected(true);
            }
        }
    }
    public void createNewLocationType() {
        if (selectedNode != null) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Creating under ", selectedNode.getData().toString());
            FacesContext.getCurrentInstance().addMessage(null, message);

            LocationType locationType = new LocationType();
            locationType.setRoot(false);
            locationType.setName("New");
            selectedNode.setExpanded(true);
            selectedNode.setSelected(false);
            selectedNode = new CustomTreeNode(new LocationTypeDocument("NEW", "-", "Folder", locationType), selectedNode);
            selectedNode.setSelected(true);
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
}
