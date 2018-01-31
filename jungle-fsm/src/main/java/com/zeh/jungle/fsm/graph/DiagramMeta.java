package com.zeh.jungle.fsm.graph;

import java.io.Serializable;
import java.util.Collection;

/**
 * 
 * @author allen
 * @version $Id: DiagramMeta.java, v 0.1 2017年10月20日 上午9:57:45 allen Exp $
 */
public class DiagramMeta implements Serializable {

    /**  */
    private static final long     serialVersionUID = 1L;

    /** The lines of diagram */
    private Collection<GraphLine> lines;

    /** The nodes of diagram */
    private Collection<GraphNode> nodes;

    /**
     * 构造方法
     * 
     * @param lines Graph line collection
     * @param nodes Graph node collection
     */
    public DiagramMeta(Collection<GraphLine> lines, Collection<GraphNode> nodes) {
        super();
        this.lines = lines;
        this.nodes = nodes;
    }

    /**
     * Getter method for property <tt>lines</tt>.
     * 
     * @return property value of lines
     */
    public Collection<GraphLine> getLines() {
        return lines;
    }

    /**
     * Getter method for property <tt>nodes</tt>.
     * 
     * @return property value of nodes
     */
    public Collection<GraphNode> getNodes() {
        return nodes;
    }

}
