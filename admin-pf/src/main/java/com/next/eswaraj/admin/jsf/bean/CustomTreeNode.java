package com.next.eswaraj.admin.jsf.bean;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class CustomTreeNode extends DefaultTreeNode {

    private static final long serialVersionUID = 1L;

    public CustomTreeNode() {
        super();
    }

    public CustomTreeNode(Object data, TreeNode parent) {
        super(data, parent);
    }

    public CustomTreeNode(Object data) {
        super(data);
    }

    public CustomTreeNode(String type, Object data, TreeNode parent) {
        super(type, data, parent);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
}
