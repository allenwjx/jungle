package com.zeh.jungle.fsm.graph;

import com.zeh.jungle.fsm.actor.ActorType;

import java.io.Serializable;

/**
 * 
 * @author allen
 * @version $Id: GraphMeta.java, v 0.1 2017年10月19日 下午4:46:27 allen Exp $
 */
public class GraphMeta implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** The business type of begin transaction */
    private String            fromBiz;

    /** The business type of end transaction */
    private String            toBiz;

    /** The business state of begin transaction */
    private String            fromState;

    /** The business state of end transaction */
    private String            toState;

    /** The name of actor */
    private String            actorName;

    /** The type of actor */
    private ActorType         type;

    /**
     * Getter method for property <tt>fromBiz</tt>.
     * 
     * @return property value of fromBiz
     */
    public String getFromBiz() {
        return fromBiz;
    }

    /**
     * Setter method for property <tt>fromBiz</tt>.
     * 
     * @param fromBiz value to be assigned to property fromBiz
     */
    public void setFromBiz(String fromBiz) {
        this.fromBiz = fromBiz;
    }

    /**
     * Getter method for property <tt>toBiz</tt>.
     * 
     * @return property value of toBiz
     */
    public String getToBiz() {
        return toBiz;
    }

    /**
     * Setter method for property <tt>toBiz</tt>.
     * 
     * @param toBiz value to be assigned to property toBiz
     */
    public void setToBiz(String toBiz) {
        this.toBiz = toBiz;
    }

    /**
     * Getter method for property <tt>fromState</tt>.
     * 
     * @return property value of fromState
     */
    public String getFromState() {
        return fromState;
    }

    /**
     * Setter method for property <tt>fromState</tt>.
     * 
     * @param fromState value to be assigned to property fromState
     */
    public void setFromState(String fromState) {
        this.fromState = fromState;
    }

    /**
     * Getter method for property <tt>toState</tt>.
     * 
     * @return property value of toState
     */
    public String getToState() {
        return toState;
    }

    /**
     * Setter method for property <tt>toState</tt>.
     * 
     * @param toState value to be assigned to property toState
     */
    public void setToState(String toState) {
        this.toState = toState;
    }

    /**
     * Getter method for property <tt>actorName</tt>.
     * 
     * @return property value of actorName
     */
    public String getActorName() {
        return actorName;
    }

    /**
     * Setter method for property <tt>actorName</tt>.
     * 
     * @param actorName value to be assigned to property actorName
     */
    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    /**
     * Getter method for property <tt>type</tt>.
     * 
     * @return property value of type
     */
    public ActorType getType() {
        return type;
    }

    /**
     * Setter method for property <tt>type</tt>.
     * 
     * @param type value to be assigned to property type
     */
    public void setType(ActorType type) {
        this.type = type;
    }

}
