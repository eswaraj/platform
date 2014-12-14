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

import org.primefaces.component.commandbutton.CommandButton;
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
import com.eswaraj.core.service.LocationService;
import com.eswaraj.web.dto.LocationDto;
import com.eswaraj.web.dto.LocationTypeDto;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class LocationBean {

    @Autowired
    private LocationService locationService;

    private TreeNode root;

    private TreeNode selectedNode;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        LocationDto location;
        try {
            logger.info("Getting Location From DB");
            location = locationService.getRootLocationForSwarajIndia();
            logger.info("Got  Location From DB : " + location);
            root = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
            TreeNode topLocation = new CustomTreeNode(new Document(location.getName(), "-", "Folder", location), root);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onNodeExpand(NodeExpandEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Expanded", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
        TreeNode nodeSelected = event.getTreeNode();
        TreeNode documents = new CustomTreeNode(new Document("Child", "-", "Folder", null), nodeSelected);
    }

    public void onNodeCollapse(NodeCollapseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Collapsed", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onNodeSelect(NodeSelectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);

        createButtons();
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void createButtons() {
        try {
            List<LocationTypeDto> locationTypes = locationService.getChildLocationsTypeOfParent(((Document) selectedNode.getData()).getLocation().getLocationTypeId());
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
}