package com.zeh.jungle.fsm.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.zeh.jungle.core.context.JungleContext;
import com.zeh.jungle.core.context.StartupCallback;
import com.zeh.jungle.fsm.actor.Actor;
import com.zeh.jungle.fsm.annotation.State;
import com.zeh.jungle.utils.common.AopTargetUtils;
import com.zeh.jungle.utils.common.LoggerUtils;
import com.zeh.jungle.utils.serializer.FastJsonUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 
 * @author allen
 * @version $Id: StateDescriptor.java, v 0.1 2017年10月19日 下午5:08:44 allen Exp $
 */
@Service
public class StateDescriptor implements ApplicationContextAware, StartupCallback {
    private static final Logger logger = LoggerFactory.getLogger(StateDescriptor.class);

    private ApplicationContext  context;

    /** 
     * @see ApplicationContextAware#setApplicationContext(ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    /** 
     * @see com.zeh.jungle.core.context.Order#getOrder()
     */
    @Override
    public int getOrder() {
        return 999;
    }

    @Override
    public void startup(JungleContext context) {
        Diagram diagram = build();
        LoggerUtils.info(logger, "[FSM-Graph] {}", FastJsonUtils.toJSONString(diagram));
    }

    /**
     * Build graph diagram
     * 
     * @return
     */
    public Diagram build() {
        DiagramMeta diagramMetadata = buildDiagramMeta();
        List<Diagram.GoJSNode> nodes = Lists.newArrayList();
        List<Diagram.GoJSLink> links = Lists.newArrayList();

        for (GraphNode graphNode : diagramMetadata.getNodes()) {
            Diagram.GoJSNode node = new Diagram.GoJSNode();
            node.setId(graphNode.getId());
            node.setText(graphNode.getName());
            nodes.add(node);
        }

        for (GraphLine graphLine : diagramMetadata.getLines()) {
            Diagram.GoJSLink link = new Diagram.GoJSLink();
            link.setFrom(graphLine.getFromNode().getId());
            link.setTo(graphLine.getToNode().getId());
            link.setText(formatTooltip(graphLine));
            link.setTooltip(formatTooltip(graphLine));
            links.add(link);
        }

        Diagram diagram = new Diagram(nodes, links);
        return diagram;
    }

    /**
     * Format tooltip
     * 
     * @param graphLine
     * @return
     */
    private String formatTooltip(GraphLine graphLine) {
        StringBuffer sb = new StringBuffer();
        List<ActorMeta> actorMetas = graphLine.getActors();
        for (ActorMeta meta : actorMetas) {
            sb.append(meta.getActorName()).append("(").append(meta.getType().name()).append(")").append("\n");
        }
        return sb.toString();
    }

    /**
     * Build up the graph
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DiagramMeta buildDiagramMeta() {
        // Collect the actors alive in system
        Map<String, Actor> actors = context.getBeansOfType(Actor.class, false, true);
        if (MapUtils.isEmpty(actors)) {
            return null;
        }

        // Collect the lines, nodes of actors
        Collection<GraphLine> lines = buildLines(actors);
        Collection<GraphNode> nodes = buildNodes(lines);
        DiagramMeta diagram = new DiagramMeta(lines, nodes);
        return diagram;
    }

    /**
     * Build up the nodes
     * 
     * @param lines
     * @return
     */
    private Collection<GraphNode> buildNodes(Collection<GraphLine> lines) {
        Collection<GraphNode> nodes = Sets.newHashSet();
        for (GraphLine line : lines) {
            nodes.add(line.getFromNode());
            nodes.add(line.getToNode());
        }
        return nodes;
    }

    /**
     * Build up the lines by the actors
     * 
     * @param actors The collection of actors
     * @return The lines of the actor 
     */
    @SuppressWarnings("rawtypes")
    private Collection<GraphLine> buildLines(Map<String, Actor> actors) {
        List<GraphMeta> metas = buildGraphMetas(actors);

        Map<String, GraphLine> lines = Maps.newHashMap();
        for (GraphMeta meta : metas) {
            GraphNode fromNode = new GraphNode(meta.getFromBiz(), meta.getFromState());
            GraphNode toNode = new GraphNode(meta.getToBiz(), meta.getToState());
            GraphLine line = new GraphLine(fromNode, toNode);

            if (lines.containsKey(line.getId())) {
                line = lines.get(line.getId());
            }
            ActorMeta actorMeta = new ActorMeta(meta.getType(), meta.getActorName(), line);
            line.getActors().add(actorMeta);

            lines.put(line.getId(), line);
        }

        return Collections.unmodifiableCollection(lines.values());
    }

    /**
     * Build up the graph metadatas by the actor collection
     *
     * @param actors the collection of actor
     * @return The graph metadatas of actors
     */
    @SuppressWarnings("rawtypes")
    private List<GraphMeta> buildGraphMetas(Map<String, Actor> actors) {
        List<GraphMeta> metas = Lists.newArrayList();
        for (Actor actor : actors.values()) {
            State anno = resolveStateAnno(actor);
            GraphMeta graphMeta = createGraphMeta(actor, anno);
            metas.add(graphMeta);
        }
        return metas;
    }

    /**
     * Create graph metadata by the actor
     * 
     * @param actor Actor
     * @param anno The annotation of the actor
     * @return Graph metadata
     */
    @SuppressWarnings("rawtypes")
    private GraphMeta createGraphMeta(Actor actor, State anno) {
        String fromBiz = anno.fromBiz();
        String toBiz = anno.toBiz();
        String fromState = anno.from();
        String toState = anno.to();

        GraphMeta meta = new GraphMeta();
        meta.setActorName(actor.getClass().getSimpleName());
        meta.setType(actor.getActorType());
        meta.setFromBiz(fromBiz);
        meta.setFromState(fromState);
        meta.setToBiz(toBiz);
        meta.setToState(toState);
        return meta;
    }

    /**
     * Resolve the annotation of the Actor
     * 
     * @param actor Actor
     * @return The defined annotation of the Actor
     */
    @SuppressWarnings("rawtypes")
    private State resolveStateAnno(Actor actor) {
        Actor proxy = (Actor) AopTargetUtils.getTarget(actor);
        State anno = proxy.getClass().getDeclaredAnnotation(State.class);
        if (anno == null) {
            throw new IllegalStateException("State annotation should be place on this class " + actor.getClass().getName());
        }
        return anno;
    }

}
