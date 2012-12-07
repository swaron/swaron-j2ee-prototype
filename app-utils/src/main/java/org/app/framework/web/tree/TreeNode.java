package org.app.framework.web.tree;

import java.util.List;

public class TreeNode {
    
    private boolean leaf = true;
    private boolean loaded = true;
    private boolean expanded;
    private String text;
    private String value;
    private Integer id;
    private List<TreeNode> children;
    
    public static TreeNode newLeaf(Integer id,String value,String text) {
        TreeNode node = new TreeNode();
        node.setLeaf(true);
        node.setLoaded(true);
        node.setExpanded(true);
        node.setId(id);
        node.setValue(value);
        node.setText(text);
        return node;
    }
    public static TreeNode newBranch(Integer id,String value,String text) {
        TreeNode node = new TreeNode();
        node.setLeaf(false);
        node.setLoaded(false);
        node.setExpanded(false);
        node.setId(id);
        node.setValue(value);
        node.setText(text);
        return node;
    }
    public boolean isLeaf() {
        return leaf;
    }
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
    public boolean isLoaded() {
        return loaded;
    }
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
    public boolean isExpanded() {
        return expanded;
    }
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public List<TreeNode> getChildren() {
        return children;
    }
    public void setChildren(List<TreeNode> children) {
        if(children!= null){
            this.setLoaded(true);
            this.setLeaf(false);
            this.setExpanded(true);
        }
        this.children = children;
    }
    
    
}
