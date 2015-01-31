package com.next.eswaraj.admin.jsf.bean;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.eswaraj.core.exceptions.ApplicationException;
import com.eswaraj.domain.nodes.Category;
import com.eswaraj.queue.service.QueueService;
import com.eswaraj.queue.service.aws.impl.AwsUploadUtil;
import com.next.eswaraj.admin.service.AdminService;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class CategoryBean {

    @Autowired
    private AdminService adminService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private AwsUploadUtil awsImageUploadUtil;

    private TreeNode root;

    private TreeNode selectedNode;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        try {
            logger.info("Getting Location From DB");
            List<Category> rootCategories = adminService.getAllRootCategories();
            logger.info("Got  Categories From DB : " + rootCategories);
            root = new CustomTreeNode(new Document("Files", "-", "Folder", null), null);
            for(Category oneCategory : rootCategories){
                new CustomTreeNode(new CategoryDocument(oneCategory.getName(), "-", "Folder", oneCategory), root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onNodeExpand(NodeExpandEvent event) {
        TreeNode nodeSelected = event.getTreeNode();
        if (nodeSelected.getChildCount() == 0) {
            Category category = ((CategoryDocument) nodeSelected.getData()).getCategory();
            List<Category> childCategorys;
            try {
                childCategorys = adminService.getChildCategories(category.getId());

                for (Category oneChildCategory : childCategorys) {
                    new DefaultTreeNode(new CategoryDocument(oneChildCategory.getName(), "-", "Folder", oneChildCategory), nodeSelected);
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
        CommandButton createChildCategoryButton = (CommandButton) FacesContext.getCurrentInstance().getViewRoot().findComponent("category_form:createChildCategory");
        SelectOneMenu systemCategoryselectOne = (SelectOneMenu) FacesContext.getCurrentInstance().getViewRoot().findComponent("category_form:createChildCategory");

        System.out.println("event.getTreeNode().class = " + event.getTreeNode().getClass());
        if(event.getTreeNode() instanceof CustomTreeNode){
            //Enable Create Child Node Button
            createChildCategoryButton.setDisabled(false);
            systemCategoryselectOne.setDisabled(true);
        }else{
            // Disable
            createChildCategoryButton.setDisabled(true);
            systemCategoryselectOne.setDisabled(false);
        }
        // createButtons();
    }

    public void onNodeUnselect(NodeUnselectEvent event) {
    }
    

    public void saveCategory() {
        System.out.println("saving : " + ((CategoryDocument) selectedNode.getData()).getCategory());
        try {
            Category category = ((CategoryDocument) selectedNode.getData()).getCategory();
            ((CategoryDocument) selectedNode.getData()).setName(category.getName());
            if (!category.isRoot()) {
                TreeNode parentNode = selectedNode.getParent();
                category.setParentCategory(((CategoryDocument) parentNode.getData()).getCategory());
            }
            category = adminService.saveCategory(category);
            queueService.sendCategoryUpdateMessage(category.getId());
            ((CategoryDocument) selectedNode.getData()).setCategory(category);
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Category Saved Succesfully");
            FacesContext.getCurrentInstance().addMessage(null, message);

        } catch (ApplicationException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, message);

        }
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
        Category category = ((CategoryDocument) selectedNode.getData()).getCategory();
        String remoteFileName = category.getId() + "_header" + imageType;
        try {
            String httpFilePath = awsImageUploadUtil.uploadCategoryImage(remoteFileName, event.getFile().getInputstream(), imageType);
            category.setHeaderImageUrl(httpFilePath);
            category = adminService.saveCategory(category);
            ((CategoryDocument) selectedNode.getData()).setCategory(category);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void handleFileUpload(FileUploadEvent event) {
        String imageType = getImageType(event);
        Category category = ((CategoryDocument) selectedNode.getData()).getCategory();
        String remoteFileName = category.getId() + imageType;
        try {
            String httpFilePath = awsImageUploadUtil.uploadCategoryImage(remoteFileName, event.getFile().getInputstream(), imageType);
            category.setImageUrl(httpFilePath);
            category = adminService.saveCategory(category);
            ((CategoryDocument) selectedNode.getData()).setCategory(category);
            FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (Exception ex) {
            logger.error("Unable to upload File", ex);
            FacesMessage message = new FacesMessage("Failed", event.getFile().getFileName() + " is failed to uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

    public void cancel() {
        if (selectedNode != null) {
            if (((CategoryDocument) selectedNode.getData()).getCategory().getId() == null) {
                TreeNode parentNode = selectedNode.getParent();
                selectedNode.getChildren().clear();
                selectedNode.getParent().getChildren().remove(selectedNode);
                selectedNode.setParent(null);
                selectedNode = null;
                parentNode.setSelected(true);
            }
        }
    }

    public void createRootCategory() {
        if (selectedNode != null || root.getChildCount() == 0) {

            Category category = new Category();
            category.setRoot(true);
            category.setName("New");
            if (selectedNode != null) {
                selectedNode.setExpanded(true);
                selectedNode.setSelected(false);
            }
            selectedNode = new CustomTreeNode(new CategoryDocument("NEW", "-", "Folder", category), root);
            selectedNode.setSelected(true);
        }
    }
    public void createChildCategory() {
        if (selectedNode != null) {
            Category category = new Category();
            category.setRoot(false);
            category.setName("New");
            selectedNode.setExpanded(true);
            selectedNode.setSelected(false);
            selectedNode = new CustomTreeNode(new CategoryDocument("NEW", "-", "Folder", category), selectedNode);
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
