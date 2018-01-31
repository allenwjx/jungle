package com.zeh.jungle.fsm.graph;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * @author allen
 * @version $Id: GraphLine.java, v 0.1 2017年10月19日 下午4:50:04 allen Exp $
 */
public class GraphLine implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** The start graph node */
    private GraphNode         fromNode;

    /** The end graph node */
    private GraphNode         toNode;

    /** Actor metas */
    private List<ActorMeta>   actors           = Lists.newArrayList();

    /**
     * 构造方法
     * 
     * @param fromNode source node
     * @param toNode target node
     */
    public GraphLine(GraphNode fromNode, GraphNode toNode) {
        super();
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    /**
     * The graph line id
     * 
     * @return id
     */
    public String getId() {
        StringBuffer sb = new StringBuffer();
        sb.append(fromNode.getBiz()).append("-").append(fromNode.getState()).append("|").append(toNode.getBiz()).append("-").append(toNode.getState());
        return sb.toString();
    }

    /**
     * Getter method for property <tt>fromNode</tt>.
     * 
     * @return property value of fromNode
     */
    public GraphNode getFromNode() {
        return fromNode;
    }

    /**
     * Setter method for property <tt>fromNode</tt>.
     * 
     * @param fromNode value to be assigned to property fromNode
     */
    public void setFromNode(GraphNode fromNode) {
        this.fromNode = fromNode;
    }

    /**
     * Getter method for property <tt>toNode</tt>.
     * 
     * @return property value of toNode
     */
    public GraphNode getToNode() {
        return toNode;
    }

    /**
     * Setter method for property <tt>toNode</tt>.
     * 
     * @param toNode value to be assigned to property toNode
     */
    public void setToNode(GraphNode toNode) {
        this.toNode = toNode;
    }

    /**
     * Getter method for property <tt>actors</tt>.
     * 
     * @return property value of actors
     */
    public List<ActorMeta> getActors() {
        return actors;
    }

    /**
     * Setter method for property <tt>actors</tt>.
     * 
     * @param actors value to be assigned to property actors
     */
    public void setActors(List<ActorMeta> actors) {
        this.actors = actors;
    }

    /** 
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fromNode == null) ? 0 : fromNode.hashCode());
        result = prime * result + ((toNode == null) ? 0 : toNode.hashCode());
        return result;
    }

    /** 
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GraphLine other = (GraphLine) obj;
        if (fromNode == null) {
            if (other.fromNode != null)
                return false;
        } else if (!fromNode.equals(other.fromNode))
            return false;
        if (toNode == null) {
            if (other.toNode != null)
                return false;
        } else if (!toNode.equals(other.toNode))
            return false;
        return true;
    }

}
