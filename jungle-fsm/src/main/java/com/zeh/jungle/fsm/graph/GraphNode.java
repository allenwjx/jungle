package com.zeh.jungle.fsm.graph;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * @author allen
 * @version $Id: GraphNode.java, v 0.1 2017年10月19日 下午4:48:38 allen Exp $
 */
public class GraphNode implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** The business type of node */
    private String            biz;

    /** The business state of node */
    private String            state;

    /** The lines that the node related */
    private List<GraphLine>   lines            = Lists.newArrayList();

    /**
     * @param biz
     * @param state
     */
    public GraphNode(String biz, String state) {
        super();
        this.biz = biz;
        this.state = state;
    }

    /**
     * Get the id of node
     * 
     * @return
     */
    public String getId() {
        return biz + "@" + state;
    }

    /**
     * Get the name of node
     * 
     * @return
     */
    public String getName() {
        return state + "(" + biz + ")";
    }

    /**
     * Getter method for property <tt>biz</tt>.
     * 
     * @return property value of biz
     */
    public String getBiz() {
        return biz;
    }

    /**
     * Getter method for property <tt>state</tt>.
     * 
     * @return property value of state
     */
    public String getState() {
        return state;
    }

    /**
     * Getter method for property <tt>lines</tt>.
     * 
     * @return property value of lines
     */
    public List<GraphLine> getLines() {
        return lines;
    }

    /**
     * Setter method for property <tt>lines</tt>.
     * 
     * @param lines value to be assigned to property lines
     */
    public void setLines(List<GraphLine> lines) {
        this.lines = lines;
    }

    /** 
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((biz == null) ? 0 : biz.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
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
        GraphNode other = (GraphNode) obj;
        if (biz == null) {
            if (other.biz != null)
                return false;
        } else if (!biz.equals(other.biz))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        return true;
    }

}
