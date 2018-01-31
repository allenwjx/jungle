package com.zeh.jungle.fsm.event;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.zeh.jungle.fsm.FSMConstants;
import com.zeh.jungle.fsm.actor.Actor;
import com.zeh.jungle.fsm.annotation.State;
import com.zeh.jungle.fsm.core.StateContext;
import com.zeh.jungle.fsm.core.StateToString;
import com.zeh.jungle.utils.common.AopTargetUtils;
import com.zeh.jungle.utils.common.LoggerUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 抽象状态事件队列
 * 
 * @author allen
 * @version $Id: AbstractStateEventQueue.java, v 0.1 2017年9月3日 下午8:58:55 allen Exp $
 */
public abstract class AbstractStateEventQueue<T, BizType, StateValue> implements StateEventQueue<T, BizType, StateValue> {
    /** logger */
    protected final Logger                    logger          = LoggerFactory.getLogger(getClass());

    /**锁*/
    private final ReadWriteLock               lock            = new ReentrantReadWriteLock();

    /** 状态监听器容器 */
    private final Map<String, ListenerKeeper> keepers         = Maps.newHashMap();

    /** Actor comparator */
    protected ActorComparator                 actorComparator = new ActorComparator();

    /**
     * 获取状态字符串转化器
     * 
     * @return 状态字符串转化器
     */
    protected abstract StateToString<T, BizType, StateValue> getStateToString();

    /**
     * 获取Actor注册校验器
     * 
     * @return Actor注册校验器
     */
    protected abstract ActorRegistryValidator getActorRegistryValidator();

    /** 
     * @see com.zeh.jungle.fsm.event.StateEventRegistration#register(Actor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void register(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor) {
        Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> proxy = (Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>) AopTargetUtils
            .getTarget(actor);
        State anno = proxy.getClass().getDeclaredAnnotation(State.class);
        if (anno == null) {
            throw new IllegalStateException("Cannot register Actor, please make sure place annotation @State on serivce: " + actor.getClass().getName());
        }

        boolean validAnnotation = getActorRegistryValidator().validate(anno);
        if (!validAnnotation) {
            throw new IllegalStateException("Cannot register Actor, please make sure annotation value is correct: " + anno.from() + "@" + anno.to());
        }

        try {
            lock.writeLock().lock();
            String transitId = getTransitId(anno);
            ListenerKeeper keeper = keepers.get(transitId);
            if (keeper == null) {
                keeper = new ListenerKeeper();
            }
            keeper.actors.add(actor);
            keepers.put(transitId, keeper);
            LoggerUtils.info(logger, "Register state actor {}-{}, container {} ", transitId, actor.getClass().getName(), this.getClass().getName());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** 
     * @see com.zeh.jungle.fsm.event.StateEventRegistration#unregister(Actor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void unregister(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor) {
        Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> proxy = (Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>) AopTargetUtils
            .getTarget(actor);
        State anno = proxy.getClass().getDeclaredAnnotation(State.class);
        if (anno == null) {
            throw new IllegalStateException("Cannot unregister Actor, please make sure place annotation @State on serivce: " + actor.getClass().getName());
        }

        try {
            lock.writeLock().lock();
            String transitId = getTransitId(anno);
            ListenerKeeper keeper = keepers.get(transitId);
            if (keeper == null) {
                logger.warn("Cannot find actor container by transit id {} ", transitId);
                return;
            }
            keeper.actors.remove(actor);
            logger.info("Unregister state actor {}-{} ", transitId, actor.getClass().getName());
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** 
     * @see com.zeh.jungle.fsm.event.StateEventRegistration#unregisterAll()
     */
    @Override
    public void unregisterAll() {
        try {
            lock.writeLock().lock();
            keepers.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** 
     * @see com.zeh.jungle.fsm.event.StateEventQueue#getAllActors()
     */
    @Override
    public Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> getAllActors() {
        Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> all = Lists.newArrayList();
        for (ListenerKeeper keeper : keepers.values()) {
            all.addAll(keeper.retrieveListeners());
        }
        return Collections.unmodifiableCollection(all);
    }

    /** 
     * @see com.zeh.jungle.fsm.event.StateEventQueue#getAllActors(com.zeh.jungle.fsm.event.StateEvent)
     */
    @Override
    public Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> getAllActors(StateEvent<T, BizType, StateValue> event) {
        LinkedList<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> allActors = new LinkedList<>();

        // 根据状态流转ID获取对应的监听器
        StateContext<T, BizType, StateValue> context = event.getContext();
        String transitId = getTransitId(context);
        ListenerKeeper keeper = keepers.get(transitId);
        if (keeper == null) {
            return allActors;
        }

        for (Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor : keeper.actors) {
            // Check whether the event is suppored by the actor
            if (actor.isSupported(event)) {
                allActors.add(actor);
            }
        }
        allActors.sort(actorComparator);
        return allActors;
    }

    /**
     * Actor comparator
     * 
     * @author allen
     * @version $Id: AbstractStateEventQueue.java, v 0.1 2017年9月4日 上午10:06:01 allen Exp $
     */
    private class ActorComparator implements Comparator<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> {

        /** 
         * @see Comparator#compare(Object, Object)
         */
        @Override
        public int compare(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> o1, Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> o2) {
            if (o1.getOrder() > o2.getOrder()) {
                return 1;
            }

            if (o1.getOrder() < o2.getOrder()) {
                return -1;
            }

            return 0;
        }

    }

    /**
     * Get transit id
     * <p> biz1^biz2|state1^state2
     * 
     * @param context state context
     * @return transit id
     */
    private String getTransitId(StateContext<T, BizType, StateValue> context) {
        String fromBiz = StringUtils.defaultIfBlank(getStateToString().bizToString(context.getFromBiz()), FSMConstants.PLACEHOLD);
        String toBiz = StringUtils.defaultIfBlank(getStateToString().bizToString(context.getToBiz()), FSMConstants.PLACEHOLD);
        String fromState = StringUtils.defaultIfBlank(getStateToString().stateToString(context.getFromState()), FSMConstants.PLACEHOLD);
        String toState = StringUtils.defaultIfBlank(getStateToString().stateToString(context.getToState()), FSMConstants.PLACEHOLD);
        return fromBiz + FSMConstants.SPLIT1 + toBiz + FSMConstants.SPLIT0 + fromState + FSMConstants.SPLIT1 + toState;
    }

    /**
     * Get transit id
     * <p> biz1^biz2|state1^state2
     * 
     * @param anno {@link State}标注
     * @return transit id
     */
    private String getTransitId(State anno) {
        String fromBiz = StringUtils.defaultIfBlank(anno.fromBiz(), FSMConstants.PLACEHOLD);
        String toBiz = StringUtils.defaultIfBlank(anno.toBiz(), FSMConstants.PLACEHOLD);
        String fromState = StringUtils.defaultIfBlank(anno.from(), FSMConstants.PLACEHOLD);
        String toState = StringUtils.defaultIfBlank(anno.to(), FSMConstants.PLACEHOLD);
        return fromBiz + FSMConstants.SPLIT1 + toBiz + FSMConstants.SPLIT0 + fromState + FSMConstants.SPLIT1 + toState;
    }

    /**
     * 事件监听器容器
     * 
     * @author allen
     * @version $Id: AbstractStateEventQueue.java, v 0.1 2017年9月3日 下午9:01:08 allen Exp $
     */
    private class ListenerKeeper {
        /**注册事件容器*/
        final Set<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> actors;

        /**
         * 构造方法
         */
        ListenerKeeper() {
            actors = new LinkedHashSet<>();
        }

        /**
         * 获取容器注册的监听器
         * 
         * @return Actor集合
         */
        Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> retrieveListeners() {
            LinkedList<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> allActors = new LinkedList<>();
            allActors.addAll(actors);
            return Collections.unmodifiableCollection(allActors);
        }
    }

}
