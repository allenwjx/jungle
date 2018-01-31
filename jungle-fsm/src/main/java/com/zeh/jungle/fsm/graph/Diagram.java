package com.zeh.jungle.fsm.graph;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * @author allen
 * @version $Id: Diagram.java, v 0.1 2017年10月20日 下午2:54:40 allen Exp $
 */
public class Diagram implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** The node key */
    private String            nodeKeyProperty  = "id";

    /** The nodes of diagram */
    private List<GoJSNode>    nodeDataArray    = Lists.newArrayList();

    /** The links of diagram */
    private List<GoJSLink>    linkDataArray    = Lists.newArrayList();

    /**
     * 
     */
    public Diagram() {
    }

    /**
     * 构造方法
     * 
     * @param nodeDataArray node data array
     * @param linkDataArray link data array
     */
    public Diagram(List<GoJSNode> nodeDataArray, List<GoJSLink> linkDataArray) {
        this.nodeDataArray = nodeDataArray;
        this.linkDataArray = linkDataArray;
    }

    /**
     * Getter method for property <tt>nodeKeyProperty</tt>.
     * 
     * @return property value of nodeKeyProperty
     */
    public String getNodeKeyProperty() {
        return nodeKeyProperty;
    }

    /**
     * Setter method for property <tt>nodeKeyProperty</tt>.
     * 
     * @param nodeKeyProperty value to be assigned to property nodeKeyProperty
     */
    public void setNodeKeyProperty(String nodeKeyProperty) {
        this.nodeKeyProperty = nodeKeyProperty;
    }

    /**
     * Getter method for property <tt>nodeDataArray</tt>.
     * 
     * @return property value of nodeDataArray
     */
    public List<GoJSNode> getNodeDataArray() {
        return nodeDataArray;
    }

    /**
     * Setter method for property <tt>nodeDataArray</tt>.
     * 
     * @param nodeDataArray value to be assigned to property nodeDataArray
     */
    public void setNodeDataArray(List<GoJSNode> nodeDataArray) {
        this.nodeDataArray = nodeDataArray;
    }

    /**
     * Getter method for property <tt>linkDataArray</tt>.
     * 
     * @return property value of linkDataArray
     */
    public List<GoJSLink> getLinkDataArray() {
        return linkDataArray;
    }

    /**
     * Setter method for property <tt>linkDataArray</tt>.
     * 
     * @param linkDataArray value to be assigned to property linkDataArray
     */
    public void setLinkDataArray(List<GoJSLink> linkDataArray) {
        this.linkDataArray = linkDataArray;
    }

    /**
     * The node model of Go JS
     * 
     * @author allen
     * @version $Id: Diagram.java, v 0.1 2017年10月20日 下午2:59:56 allen Exp $
     */
    public static class GoJSNode implements Serializable {
        /**  */
        private static final long serialVersionUID = 1L;

        /** Node id */
        private String            id;

        /** Node name */
        private String            text;

        /**
         * Getter method for property <tt>id</tt>.
         * 
         * @return property value of id
         */
        public String getId() {
            return id;
        }

        /**
         * Setter method for property <tt>id</tt>.
         * 
         * @param id value to be assigned to property id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * Getter method for property <tt>text</tt>.
         * 
         * @return property value of text
         */
        public String getText() {
            return text;
        }

        /**
         * Setter method for property <tt>text</tt>.
         * 
         * @param text value to be assigned to property text
         */
        public void setText(String text) {
            this.text = text;
        }
    }

    /**
     * The link model of GO JS
     * 
     * @author allen
     * @version $Id: Diagram.java, v 0.1 2017年10月20日 下午3:06:11 allen Exp $
     */
    public static class GoJSLink implements Serializable {

        /**  */
        private static final long serialVersionUID = 1L;

        /** The source node of the link */
        private String            from;

        /** The target node of the link */
        private String            to;

        /** The text of the link */
        private String            text;

        /** The tooltip of the link */
        private String            tooltip;

        /**
         * Getter method for property <tt>from</tt>.
         * 
         * @return property value of from
         */
        public String getFrom() {
            return from;
        }

        /**
         * Setter method for property <tt>from</tt>.
         * 
         * @param from value to be assigned to property from
         */
        public void setFrom(String from) {
            this.from = from;
        }

        /**
         * Getter method for property <tt>to</tt>.
         * 
         * @return property value of to
         */
        public String getTo() {
            return to;
        }

        /**
         * Setter method for property <tt>to</tt>.
         * 
         * @param to value to be assigned to property to
         */
        public void setTo(String to) {
            this.to = to;
        }

        /**
         * Getter method for property <tt>text</tt>.
         * 
         * @return property value of text
         */
        public String getText() {
            return text;
        }

        /**
         * Setter method for property <tt>text</tt>.
         * 
         * @param text value to be assigned to property text
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * Getter method for property <tt>tooltip</tt>.
         * 
         * @return property value of tooltip
         */
        public String getTooltip() {
            return tooltip;
        }

        /**
         * Setter method for property <tt>tooltip</tt>.
         * 
         * @param tooltip value to be assigned to property tooltip
         */
        public void setTooltip(String tooltip) {
            this.tooltip = tooltip;
        }

    }
}
