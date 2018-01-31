package com.zeh.jungle.fsm.graph;

import com.zeh.jungle.fsm.actor.ActorType;

import java.io.Serializable;

/**
 * 
 * @author allen
 * @version $Id: ActorMeta.java, v 0.1 2017年10月19日 下午5:30:04 allen Exp $
 */
public class ActorMeta implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** Actor type */
    private ActorType         type;

    /** Actor name */
    private String            actorName;

    /** Graph line */
    private GraphLine         line;

    /**
     * @param type actor type
     * @param actorName actor name
     * @param line graph line
     */
    public ActorMeta(ActorType type, String actorName, GraphLine line) {
        super();
        this.type = type;
        this.actorName = actorName;
        this.line = line;
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
     * Getter method for property <tt>actorName</tt>.
     * 
     * @return property value of actorName
     */
    public String getActorName() {
        return actorName;
    }

    /**
     * Getter method for property <tt>line</tt>.
     * 
     * @return property value of line
     */
    public GraphLine getLine() {
        return line;
    }

}
