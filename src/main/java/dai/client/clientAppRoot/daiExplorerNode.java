
//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:
package dai.client.clientAppRoot;

import java.util.Vector;

import javax.swing.JInternalFrame;
import javax.swing.tree.DefaultMutableTreeNode;

class daiExplorerNode extends DefaultMutableTreeNode {

    private String _nodeId  = null;
    private String _nodeDescription = null;
    private String _nodeClassName = null;
    private String _nodeComponentIcon = null;
    private JInternalFrame _nodeActionFrame = null;

    //Stores the list of launchable components asociated with
    //this Node.
    private Vector _componentList = new Vector();

    public daiExplorerNode(String nodeId, String nodeDescription) {
        super(nodeDescription);

        _nodeId   = nodeId;
        _nodeDescription = nodeDescription;
        _nodeClassName = "";
        _nodeComponentIcon = "";
    }

    public daiExplorerNode(String nodeId, String nodeDescription, String className, String icon) {
        super(nodeDescription);

        _nodeId   = nodeId;
        _nodeDescription = nodeDescription;
        _nodeClassName = className;
        _nodeComponentIcon = icon;
    }

    public String getNodeId()
    {
        return _nodeId;
    }

    public String getNodeDescription()
    {
        return _nodeDescription;
    }
    public String getIcon()
    {
        return _nodeComponentIcon;
    }

    public String getClassName()
    {
        return _nodeClassName;
    }

    public void addNodeComponent(String id, String description, String className, String icon)
    {
        _componentList.addElement(new NodeComponent(id, description, className, icon));
    }

    public int getNodeComponentCount()
    {
        return _componentList.size();
    }

    public void setNodeActionFrame(JInternalFrame _jInternalFrame)  {
      _nodeActionFrame = _jInternalFrame;
    }

    public JInternalFrame getNodeActionFrame()  {
      return _nodeActionFrame;
    }

    public String getNodeComponentDescription(int index)
    {
        NodeComponent nc = (NodeComponent)_componentList.elementAt(index);
        return nc.getDescription();
    }

    public String getNodeComponentIcon(int index)
    {
        NodeComponent nc = (NodeComponent)_componentList.elementAt(index);
        return nc.getIcon();
    }

    public String getNodeComponentLoadString(int index)
    {
        NodeComponent nc = (NodeComponent)_componentList.elementAt(index);
        return nc.getClassName();
    }
}

class NodeComponent
{
    private String _componentId  = null;
    private String _componentDescription = null;
    private String _componentIcon = null;
    private String _className = null;

    public NodeComponent(String id, String description, String className, String icon) {
        _componentId = id;
        _componentDescription = description;
        _className = className;
        _componentIcon = icon;
    }

    public String getDescription()
    {
        return _componentDescription;
    }

    public String getIcon()
    {
        return _componentIcon;
    }

    public String getClassName()
    {
        return _className;
    }
}
