package com.zeh.jungle.web;

/**
 * The type Sort bean.
 *
 * @author huzhongyuan
 * @version $Id : ${FILE_NAME}, v 0.1 16/4/17 18:25 huzhongyuan Exp $
 */
public class SortBean {

    /**
     * The Property.
     */
    private String property;
    /**
     * The Direction.
     */
    private String direction;

    /**
     * Gets property.
     *
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Sets property.
     *
     * @param property the property
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Gets direction.
     *
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Sets direction.
     *
     * @param direction the direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }
}