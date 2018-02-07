package com.zeh.jungle.web.basic;

import java.io.Serializable;

/**
  * @author allen
  * @version $Id : TextValue.java, v 0.1 2017年02月17 hxy43938 Exp $
 */
public class TextValue implements Serializable {

    private static final long serialVersionUID = 8368201574264798938L;

    private Object            value;

    private String            text;

    public TextValue() {

    }

    public TextValue(Object value, String text) {
        this.value = value;
        this.text = text;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
