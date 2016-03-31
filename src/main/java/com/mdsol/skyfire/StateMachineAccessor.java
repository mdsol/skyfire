/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

/**
 * A class that provides functions to access UML State Machine models. Classes in Acceleo are used
 * to be helpers to access the models.
 *
 * @author Nan Li
 * @version 1.0 Nov 28, 2012
 */

public class StateMachineAccessor extends ModelAccessor {

    /**
     * the key is a state; the value is an integer number used in {@link coverage.web.Path}
     */
    private Map<Vertex, String> stateMappings = new HashMap<>();

    /**
     * the key is an integer number used in {@link coverage.web.Path}; the value is a state
     */
    private Map<String, Vertex> reversedStateMappings = new HashMap<>();

    /**
     * transitions in the region of this state machine
     */
    private List<Transition> transitions = new ArrayList<>();

    /**
     * the region of the state machine; this region is different from the region0 of the state
     * machine if the region has composite states
     */
    private Region region;
    // the initial states
    private String initialStates = "";
    // the final states
    private String finalStates = "";

    // transitions in a String format
    // 1 2
    // 2 3
    // ... etc.
    private String edges = "";

    /**
     * Constructs an StateMachineAccessor object with a Region object
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} Region object
     */
    public StateMachineAccessor(final Region region) {
        createStateMappings(region);
        createEdges(region);
    }

    /**
     * Creates the one mapping from vertexes to integers and another mapping from integers to
     * vertexes. This method cannot be overridden.
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} Region object
     */
    private void createStateMappings(final Region region) {

        final EList<Vertex> vertices = region.getSubvertices();

        // check if all states are simple
        boolean isSimple = true;
        for (final Vertex vertex : vertices) {
            if ((vertex instanceof State && ((State) vertex).isComposite())
                    || (vertex instanceof FinalState && ((FinalState) vertex).isComposite())) {
                isSimple = false;
            }
        }

        // if all states are simple, execute the if block; otherwise, do the
        // else block
        if (isSimple) {
            int stateNumber = 1;
            for (final Vertex vertex : vertices) {
                processSimpleState(vertex, stateNumber);
                stateNumber++;
            }
        } else {
            processCompositeStates(vertices, region);
        }

    }

    /**
     * Processes a simple state by adding it to state mappings
     *
     * @param vertex
     *            a state vertex
     * @param stateNumber
     *            the specified number assigned to the state
     */
    private void processSimpleState(final Vertex vertex, final int stateNumber) {
        if (vertex instanceof Pseudostate
                && ((Pseudostate) vertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
            final EList<Transition> outgoings = vertex.getOutgoings();
            if (outgoings.size() > 0) {
                getStateMappings().put(vertex, Integer.toString(stateNumber));
                getReversedStateMappings().put(Integer.toString(stateNumber), vertex);
                setInitialStates(getInitialStates() + Integer.toString(stateNumber));
            }
        } else if (vertex instanceof FinalState) {
            getStateMappings().put(vertex, Integer.toString(stateNumber));
            getReversedStateMappings().put(Integer.toString(stateNumber), vertex);
            setFinalStates(getFinalStates() + Integer.toString(stateNumber));
        } else {
            getStateMappings().put(vertex, Integer.toString(stateNumber));
            getReversedStateMappings().put(Integer.toString(stateNumber), vertex);
        }
    }

    /**
     * Process all states if one of them is a composite state
     *
     * @param vertices
     *            all vertices that represent states
     * @param region
     *            the region that has all states
     */
    private void processCompositeStates(final EList<Vertex> vertices, final Region region) {
        List<Vertex> compositeStates = new ArrayList<>();
        compositeStates.addAll(vertices);
        int stateNumber = 1;

        do {
            final Vertex aVertex = compositeStates.get(0);
            boolean toRemoveFirstVertex = false;

            if (aVertex instanceof Pseudostate
                    && ((Pseudostate) aVertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                toRemoveFirstVertex = processInitialNode(aVertex, stateNumber, region);
            } else if (aVertex instanceof FinalState) {
                toRemoveFirstVertex = processFinalNode(aVertex, stateNumber, region);
            } else if (((State) aVertex).isSimple()) {
                getStateMappings().put(aVertex, Integer.toString(stateNumber));
                getReversedStateMappings().put(Integer.toString(stateNumber), aVertex);
                toRemoveFirstVertex = true;
            } else if (((State) aVertex).isComposite()) {
                compositeStates = processCompositeState(aVertex, stateNumber, compositeStates);
                toRemoveFirstVertex = true;
            }

            if (toRemoveFirstVertex) {
                compositeStates.remove(0);
            }
            stateNumber++;

        } while (!compositeStates.isEmpty());

    }

    /**
     * Processes an initial state if there is at least one composite state
     *
     * @param aVertex
     *            a State vertex
     * @param stateNumber
     *            the specified state number
     * @param region
     *            the region that has all vertices
     * @return true if this initial node is valid and properly processed; otherwise, return false
     */
    private boolean processInitialNode(final Vertex aVertex, final int stateNumber,
            final Region region) {
        final EList<Transition> outgoings = aVertex.getOutgoings();
        if (outgoings.size() > 0) {
            getStateMappings().put(aVertex, Integer.toString(stateNumber));
            getReversedStateMappings().put(Integer.toString(stateNumber), aVertex);
            // set the initial if this initial is at the top level
            // of the region
            // initials states in composite states are not
            // considered as initials in the whole graph
            if (aVertex.getOwner() == region) {
                addExtraInitialNodes(stateNumber);
            }
            return true;
        }
        return false;
    }

    /**
     * Processes a final state if there is at least one composite state
     *
     * @param aVertex
     *            a State vertex
     * @param stateNumber
     *            the specified state number
     * @param region
     *            the region that has all vertices
     * @return true if this final node is valid and properly processed; otherwise, return false
     */
    private boolean processFinalNode(final Vertex aVertex, final int stateNumber,
            final Region region) {
        getStateMappings().put(aVertex, Integer.toString(stateNumber));
        getReversedStateMappings().put(Integer.toString(stateNumber), aVertex);
        // set the final if this final is at the top level of the
        // region
        // final states in composite states are not considered as
        // finals in the whole graph
        if (aVertex.getOwner() == region) {
            addExtraFinalNodes(stateNumber);
        }
        return true;
    }

    /**
     * Processes a composite state
     *
     * @param aVertex
     *            a State vertex
     * @param stateNumber
     *            the specified state number
     * @return true if this composite node is valid and properly processed; otherwise, return false
     */
    private List<Vertex> processCompositeState(final Vertex aVertex, final int stateNumber,
            final List<Vertex> compositeStates) {
        List<Vertex> newCompositeStates = compositeStates;
        final List<Vertex> localVertices = getStates((State) aVertex);
        // this composite state does not have any sub-vertices when
        // the size is 0
        if (localVertices.isEmpty()) {
            getStateMappings().put(aVertex, Integer.toString(stateNumber));
            getReversedStateMappings().put(Integer.toString(stateNumber), aVertex);
        } else {
            newCompositeStates.addAll(localVertices);
        }
        return newCompositeStates;
    }

    /**
     * Adds extra initial nodes
     *
     * @param stateNumber
     *            the state number to assign
     */
    private void addExtraInitialNodes(final int stateNumber) {
        if ("".equals(getInitialStates().trim())) {
            setInitialStates(Integer.toString(stateNumber));
        } else {
            setInitialStates(getInitialStates() + ", " + Integer.toString(stateNumber));
        }
    }

    /**
     * Adds extra final nodes
     *
     * @param stateNumber
     *            the state number to assign
     */
    private void addExtraFinalNodes(final int stateNumber) {
        if ("".equals(getFinalStates().trim())) {
            setFinalStates(Integer.toString(stateNumber));
        } else {
            setFinalStates(getFinalStates() + ", " + Integer.toString(stateNumber));
        }
    }

    /**
     * Creates the edges based on the transitions edges are in a String format 1 2 2 3 ...
     *
     * This method cannot be overridden.
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} Region object
     */
    private void createEdges(final Region region) {
        List<Transition> compoStateTrans = genCompoStateTrans(region);

        // mark composite states whose sub-states have been added to the list
        // transCompoState
        Map<Vertex, Boolean> compositeStates = new HashMap<>();
        final List<Transition> simpleTrans = new ArrayList<>();
        if (!compoStateTrans.isEmpty()) {
            do {
                // handle the first transition in every iteration
                final Transition t = compoStateTrans.get(0);

                if (t.getSource() instanceof State && ((State) t.getSource()).isComposite()) {
                    compoStateTrans = processCompoStateSource(t, compoStateTrans, region);
                    compoStateTrans = addNewTransitionsFromSrc(t, compositeStates, compoStateTrans);
                    compositeStates = addNewStateFromSrc(t, compositeStates);

                    compoStateTrans.remove(0);
                } else if (t.getTarget() instanceof State
                        && ((State) t.getTarget()).isComposite()) {
                    compoStateTrans = processCompoStateTarget(t, compoStateTrans, region);
                    compoStateTrans = addNewTransitionsFromTarget(t, compositeStates,
                            compoStateTrans);
                    compositeStates = addNewStateFromTarget(t, compositeStates);
                    compoStateTrans.remove(0);
                } else if ((t.getSource() instanceof State && ((State) t.getSource()).isSimple())
                        && (t.getTarget() instanceof State && ((State) t.getTarget()).isSimple())) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    compoStateTrans.remove(0);
                    edges = updateEdges(edges, t);

                    compoStateTrans = addNewTransitionsFromSimpleTarget(t, compositeStates,
                            compoStateTrans);
                    compositeStates = addNewStateFromSimpleTarget(t, compositeStates);
                } else if ((t.getSource() instanceof Pseudostate && ((Pseudostate) t.getSource())
                        .getKind() == PseudostateKind.INITIAL_LITERAL)
                        && (t.getTarget() instanceof State && ((State) t.getTarget()).isSimple())) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    compoStateTrans.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getTarget() instanceof Pseudostate && ((Pseudostate) t.getTarget())
                        .getKind() == PseudostateKind.INITIAL_LITERAL)
                        && (t.getSource() instanceof State && ((State) t.getSource()).isSimple())) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    compoStateTrans.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getSource() instanceof State && ((State) t.getSource()).isSimple())
                        && (t.getTarget() instanceof FinalState)) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    compoStateTrans.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getTarget() instanceof State && ((State) t.getTarget()).isSimple())
                        && (t.getSource() instanceof FinalState)) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    compoStateTrans.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getSource() instanceof FinalState)
                        && (t.getTarget() instanceof FinalState)) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    compoStateTrans.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getSource() instanceof Pseudostate && ((Pseudostate) t.getSource())
                        .getKind() == PseudostateKind.INITIAL_LITERAL)
                        && (t.getTarget() instanceof Pseudostate && ((Pseudostate) t.getTarget())
                                .getKind() == PseudostateKind.INITIAL_LITERAL)) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    compoStateTrans.remove(0);
                    edges = updateEdges(edges, t);
                }

            } while (!compoStateTrans.isEmpty());
        }

    }

    /**
     * Processes the states in a composite state, which is the source state of the specified
     * transition, and creates associated transitions
     *
     * @param t
     *            a transition to process
     * @param compoStateTrans
     *            the transitions among the composite states
     * @param region
     *            the region that has all states and transitions
     * @return a list of Transition objects
     */
    private List<Transition> processCompoStateSource(final Transition t,
            final List<Transition> compoStateTrans, final Region region) {
        List<Transition> newCompoStateTrans = compoStateTrans;

        final List<Vertex> sourceVertices = ((State) t.getSource()).getRegions().get(0)
                .getSubvertices();
        // initialize initial states, final states, and regular states in the source
        final List<Pseudostate> sourceInitialStates = new ArrayList<>();
        final List<FinalState> sourceFinalStates = new ArrayList<>();
        final List<State> sourceStates = new ArrayList<>();

        for (final Vertex vertex : sourceVertices) {
            if (vertex instanceof Pseudostate
                    && ((Pseudostate) vertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                sourceInitialStates.add((Pseudostate) vertex);
            } else if (vertex instanceof FinalState) {
                sourceFinalStates.add((FinalState) vertex);
            } else if (vertex instanceof State) {
                sourceStates.add((State) vertex);
            }
        }

        if (!sourceFinalStates.isEmpty()) {
            for (final FinalState finalState : sourceFinalStates) {
                // the transition creation supposes to be updated
                // for the cases that transitions have guards,
                // effects or others
                final Transition tempTran = region.createTransition(t.getName());

                tempTran.setSource(finalState);
                tempTran.setTarget(t.getTarget());

                newCompoStateTrans.add(tempTran);
            }
        } else if (!sourceStates.isEmpty()) {
            for (final State state : sourceStates) {

                final Transition tempTran = region.createTransition(t.getName());
                tempTran.setSource(state);
                tempTran.setTarget(t.getTarget());

                newCompoStateTrans.add(tempTran);
            }

            /*
             * I decide not to create a transition from an initial state of a composite state to
             * other states since the initial state is not a stable state for(Pseudostate
             * initialState : sourceInitialStates){
             *
             * Transition tempTran = region.createTransition(t.getName());
             * tempTran.setSource(initialState); tempTran.setTarget(t.getTarget());
             *
             * transCompoState.add(tempTran); }
             */
        }
        return newCompoStateTrans;
    }

    /**
     * Adds transitions from the source of the specified transition
     *
     * @param t
     *            the specified transition
     * @param compositeStates
     *            a map that records composite states
     * @param compoStateTrans
     *            a list of Transition objects
     * @return a list of Transition objects with newly added transitions
     */
    private List<Transition> addNewTransitionsFromSrc(final Transition t,
            final Map<Vertex, Boolean> compositeStates, final List<Transition> compoStateTrans) {
        List<Transition> newCompoStateTrans = compoStateTrans;

        if (!compositeStates.containsKey(t.getSource())) {
            for (final Transition transition : ((State) t.getSource()).getRegions().get(0)
                    .getTransitions()) {
                newCompoStateTrans.add(transition);
            }
        }

        return newCompoStateTrans;
    }

    /**
     * Adds transitions from the target of the specified transition
     *
     * @param t
     *            the specified transition
     * @param compositeStates
     *            a map that records composite states
     * @param compoStateTrans
     *            a list of Transition objects
     * @return a list of Transition objects with newly added transitions
     */
    private List<Transition> addNewTransitionsFromTarget(final Transition t,
            final Map<Vertex, Boolean> compositeStates, final List<Transition> compoStateTrans) {
        List<Transition> newCompoStateTrans = compoStateTrans;

        if (!compositeStates.containsKey(t.getTarget())) {
            for (final Transition transition : ((State) t.getTarget()).getRegions().get(0)
                    .getTransitions()) {
                newCompoStateTrans.add(transition);
            }
        }

        return newCompoStateTrans;
    }

    /**
     * Adds transitions whose source and target are simple states
     *
     * @param t
     *            the specified transition
     * @param compositeStates
     *            a map that records composite states
     * @param compoStateTrans
     *            a list of Transition objects
     * @return a list of Transition objects with newly added transitions
     */
    private List<Transition> addNewTransitionsFromSimpleTarget(final Transition t,
            final Map<Vertex, Boolean> compositeStates, final List<Transition> compoStateTrans) {
        List<Transition> newCompoStateTrans = compoStateTrans;

        // add a potential composite state
        // this address a case in which a transition directly connects from a composite
        // state to a simple state
        // in another composite state, which has not been considered.
        if (((State) t.getTarget()).getContainer() != null
                && ((State) t.getTarget()).getContainer().getState() != null && !compositeStates
                        .containsKey(((State) t.getTarget()).getContainer().getState())) {

            for (final Transition transition : ((State) t.getTarget()).getContainer()
                    .getTransitions()) {
                newCompoStateTrans.add(transition);
            }
        }

        return newCompoStateTrans;
    }

    /**
     * Adds new state from the source of the specified transition
     *
     * @param t
     *            the specified transition
     * @param compositeStates
     *            a map that records the composite states
     * @return a map that includes the newly added state
     */
    private Map<Vertex, Boolean> addNewStateFromSrc(final Transition t,
            final Map<Vertex, Boolean> compositeStates) {
        Map<Vertex, Boolean> newCompositeStates = compositeStates;
        if (!compositeStates.containsKey(t.getSource())) {
            compositeStates.put(t.getSource(), true);
        }
        return newCompositeStates;
    }

    /**
     * Adds new state from the target of the specified transition
     *
     * @param t
     *            the specified transition
     * @param compositeStates
     *            a map that records the composite states
     * @return a map that includes the newly added state
     */
    private Map<Vertex, Boolean> addNewStateFromTarget(final Transition t,
            final Map<Vertex, Boolean> compositeStates) {
        Map<Vertex, Boolean> newCompositeStates = compositeStates;
        if (!compositeStates.containsKey(t.getTarget())) {
            compositeStates.put(t.getTarget(), true);
        }
        return newCompositeStates;
    }

    /**
     * Adds new simple state, which is the target of the specified transition
     *
     * @param t
     *            the specified transition
     * @param compositeStates
     *            a map that records the composite states
     * @return a map that includes the newly added state
     */
    private Map<Vertex, Boolean> addNewStateFromSimpleTarget(final Transition t,
            final Map<Vertex, Boolean> compositeStates) {
        Map<Vertex, Boolean> newCompositeStates = compositeStates;
        // add a potential composite state
        // this address a case in which a transition directly connects from a composite
        // state to a simple state
        // in another composite state, which has not been considered.
        if (((State) t.getTarget()).getContainer() != null
                && ((State) t.getTarget()).getContainer().getState() != null && !newCompositeStates
                        .containsKey(((State) t.getTarget()).getContainer().getState())) {
            newCompositeStates.put(((State) t.getTarget()).getContainer().getState(), true);
        }
        return newCompositeStates;
    }

    /**
     * Processes the states in a composite state, which is the target state of the specified
     * transition, and creates associated transitions
     *
     * @param t
     *            a transition to process
     * @param compoStateTrans
     *            the transitions among the composite states
     * @param region
     *            the region that has all states and transitions
     * @return a list of Transition objects
     */
    private List<Transition> processCompoStateTarget(final Transition t,
            final List<Transition> compoStateTrans, final Region region) {
        List<Transition> newCompoStateTrans = compoStateTrans;

        final List<Vertex> targetVertices = ((State) t.getTarget()).getRegions().get(0)
                .getSubvertices();
        // initialize initial states, final states, and regular states in the target
        // composite state
        final List<Pseudostate> targetInitialStates = new ArrayList<>();
        final List<FinalState> targetFinalStates = new ArrayList<>();
        final List<State> targetStates = new ArrayList<>();

        for (final Vertex vertex : targetVertices) {
            if (vertex instanceof Pseudostate
                    && ((Pseudostate) vertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                targetInitialStates.add((Pseudostate) vertex);
            } else if (vertex instanceof FinalState) {
                targetFinalStates.add((FinalState) vertex);
            } else if (vertex instanceof State) {
                targetStates.add((State) vertex);
            }
        }

        if (!targetInitialStates.isEmpty()) {
            for (final Pseudostate initialState : targetInitialStates) {

                final Transition tempTran = region.createTransition(t.getName());
                tempTran.setSource(t.getSource());
                tempTran.setTarget(initialState);

                newCompoStateTrans.add(tempTran);
            }
        } else if (!targetStates.isEmpty()) {
            for (final State state : targetStates) {

                final Transition tempTran = region.createTransition(t.getName());
                tempTran.setSource(t.getSource());
                tempTran.setTarget(state);

                newCompoStateTrans.add(tempTran);
            }

        }
        return newCompoStateTrans;
    }

    /**
     * Generates transitions among composite states in a region
     *
     * @param region
     *            the specified region
     * @return a list of Transition objects
     */
    private List<Transition> genCompoStateTrans(final Region region) {
        final EList<Transition> firstLevelTransitions = region.getTransitions();
        final List<Transition> compoStateTrans = new ArrayList<>();

        for (final Transition transition : firstLevelTransitions) {
            // transitions may not have a source or target because some of them
            // are isolated because model editors might leave them accidentally
            // they do not appear in the UML diagram but they do exist in the
            // UML model
            if (transition.getSource() == null || transition.getTarget() == null) {
                continue;
            }

            if (transition.getSource() instanceof Pseudostate
                    || transition.getSource() instanceof FinalState
                    || transition.getSource() instanceof State
                    || transition.getTarget() instanceof Pseudostate
                    || transition.getTarget() instanceof FinalState
                    || transition.getTarget() instanceof State) {
                if (!((transition.getSource() instanceof State
                        && ((State) transition.getSource()).isComposite())
                        || (transition.getTarget() instanceof State
                                && ((State) transition.getTarget()).isComposite()))) {
                    edges = updateEdges(edges, transition);
                    getTransitions().add(transition);
                } else if ((transition.getSource() instanceof State
                        && ((State) transition.getSource()).isComposite())
                        || (transition.getTarget() instanceof State
                                && ((State) transition.getTarget()).isComposite())) {
                    compoStateTrans.add(transition);
                }
            }

        }

        return compoStateTrans;
    }

    /**
     * Find the corresponding edge specified in a String format for a transition and update the
     * edges.
     *
     * @param edges
     *            the field edges
     * @param transition
     *            a {@link org.eclipse.uml2.uml.Transition} object
     * @return the field of this: edges
     */
    private String updateEdges(final String edges, final Transition transition) {
        final String edge = stateMappings.get(transition.getSource()) + " "
                + stateMappings.get(transition.getTarget());
        String newEdges = edges;
        // redundant edges are not allowed
        if (edges.indexOf(edge) == -1) {
            if (stateMappings.containsKey(transition.getSource())) {
                newEdges = newEdges + stateMappings.get(transition.getSource());
            }
            if (stateMappings.containsKey(transition.getTarget())) {
                newEdges = newEdges + " " + stateMappings.get(transition.getTarget());
            }
            newEdges = newEdges + "\n";
        }
        return newEdges;
    }

    /**
     * Gets all objects of {@link org.eclipse.uml2.uml.Region} in the stateMachine
     *
     * @param stateMachine
     *            a UML {@link org.eclipse.uml2.uml.StateMachine} object to parse
     * @return a list of {@link org.eclipse.uml2.uml.Region} in the stateMachine
     */
    public static List<Region> getRegions(final StateMachine stateMachine) {
        final List<Region> result = new ArrayList<>();
        final EList<Region> regions = stateMachine.getRegions();

        for (final Region region : regions) {
            result.add(region);
        }

        return result;
    }

    /**
     * Gets all objects of {@link org.eclipse.uml2.uml.State} except
     * {@link org.eclipse.uml2.uml.FinalState} and {@link org.eclipse.uml2.uml.Pseudostate} in the
     * region
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} of a UML behavioral diagram
     * @return a list of {@link org.eclipse.uml2.uml.State} in the region
     */
    public static List<State> getStates(final Region region) {
        final List<State> result = new ArrayList<>();

        final EList<Vertex> vertexes = region.getSubvertices();
        for (final Vertex vertex : vertexes) {
            if (!(vertex instanceof FinalState) && !(vertex instanceof Pseudostate)) {
                result.add((State) vertex);
            }
        }

        return result;
    }

    /**
     * Gets all {@link org.eclipse.uml2.uml.Vertex} objects in the region
     *
     * @param state
     *            {@link org.eclipse.uml2.uml.State} object
     * @return a list of {@link org.eclipse.uml2.uml.Vertex} objects in the region
     */
    public static List<Vertex> getStates(final State state) {
        final List<Vertex> result = new ArrayList<>();

        if (state.isComposite()) {
            final EList<Vertex> vertexes = state.getRegions().get(0).getSubvertices();
            for (final Vertex vertex : vertexes) {
                if ((vertex instanceof FinalState) || vertex instanceof Pseudostate
                        || vertex instanceof State) {
                    result.add(vertex);
                }
            }
        }
        return result;
    }

    /**
     * Gets all objects of {@link org.eclipse.uml2.uml.FinalState} in the region
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} of a UML behavioral diagram
     * @return a list of {@link org.eclipse.uml2.uml.FinalState} in the region
     */
    public static List<FinalState> getFinalStates(final Region region) {
        final List<FinalState> result = new ArrayList<>();

        final EList<Vertex> vertexes = region.getSubvertices();
        for (final Vertex vertex : vertexes) {
            if (vertex instanceof FinalState) {
                result.add((FinalState) vertex);
            }
        }

        return result;
    }

    /**
     * Gets all objects of {@link org.eclipse.uml2.uml.Pseudostate} in the region
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} of a UML behavioral diagram
     * @return a list of {@link org.eclipse.uml2.uml.Pseudostate} in the region
     *
     *         Initial states in state machine diagrams are of Pseudostate, in this method, each
     *         object of Pseudostate is examined. If an object of Pseudostate is connected with
     *         other states, it is treated to be an initial state.
     */
    public static List<Pseudostate> getInitialStates(final Region region) {
        final List<Pseudostate> result = new ArrayList<>();

        final EList<Vertex> vertexes = region.getSubvertices();
        for (final Vertex vertex : vertexes) {
            if (vertex instanceof Pseudostate
                    && ((Pseudostate) vertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                // check if the initial node is connected with other nodes
                final EList<Transition> outgoings = vertex.getOutgoings();
                if (!outgoings.isEmpty()) {
                    result.add((Pseudostate) vertex);
                }
            }
        }

        return result;
    }

    /**
     * Gets all identifiable elements that can be mapped from a region of a state machine. So far
     * only transitions, constraints, and states are to be returned.
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} object
     * @return a list of {@link org.eclipse.uml2.uml.NamedElement} objects
     */
    public static List<NamedElement> getAllIdentifiableElements(final Region region) {
        final List<NamedElement> result = new ArrayList<>();

        // add distinct transitions to the result
        final List<Transition> transitions = findAllTransitions(region);
        for (final Transition transition : transitions) {
            boolean hasSameName = false;
            for (final NamedElement element : result) {
                if (element.getName().equals(transition.getName())) {
                    hasSameName = true;
                }
            }
            if (!hasSameName && !"".equals(transition.getName().trim())) {
                result.add(transition);
            }
        }

        // add distinct constraints to the result
        final EList<Constraint> constraints = region.getOwnedRules();
        for (final Constraint constraint : constraints) {
            boolean hasSameName = false;
            for (final NamedElement element : result) {
                if (element.getName().equals(constraint.getName())) {
                    hasSameName = true;
                }
            }
            if (!hasSameName) {
                result.add(constraint);
            }
        }

        // add distinct vertices to the result
        // saved for later
        /*
         * EList<Vertex> vertices = region.getSubvertices(); for(Vertex vertex: vertices){ boolean
         * hasSameName = false; for(NamedElement element: result){
         * if(element.getName().equals(vertex.getName())){ hasSameName = true; } }
         *
         * if(hasSameName == false){ if(vertex instanceof Pseudostate){ EList<Transition> outgoings
         * = vertex.getOutgoings(); if(outgoings.size() > 0){ result.add(((Pseudostate)vertex)); } }
         * else if(vertex instanceof FinalState){ result.add(((FinalState)vertex)); }else{
         * result.add(vertex); } }
         *
         * }
         */
        return result;
    }

    /**
     * Finds all transitions in a state machine.
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} of a UML behavioral diagram
     * @return a list of {@link org.eclipse.uml2.uml.Transition} objects included in that region
     */
    public static List<Transition> findAllTransitions(final Region region) {
        final EList<Transition> transitions = region.getTransitions();
        // add the transitions at the first level
        final List<Transition> result = new ArrayList<>();
        for (final Transition t : transitions) {
            result.add(t);
        }

        final List<Vertex> compoStates = new ArrayList<>();

        for (final Vertex vertex : region.getSubvertices()) {
            if (vertex instanceof State && ((State) vertex).isComposite()) {
                compoStates.add(vertex);
            }
        }
        // add transitions in the composite states
        if (!compoStates.isEmpty()) {
            do {
                final Vertex compo = compoStates.get(0);
                result.addAll(((State) compo).getRegions().get(0).getTransitions());
                for (final Vertex vertex : ((State) compo).getRegions().get(0).getSubvertices()) {
                    if (vertex instanceof State && ((State) vertex).isComposite()) {
                        compoStates.add(vertex);
                    }
                }
                compoStates.remove(0);

            } while (!compoStates.isEmpty());
        }

        return result;
    }

    /**
     * Finds the constraint in the model specified by a constraint name and returns its constrained
     * elements
     *
     * @param region
     *            a {@link org.eclipse.uml2.uml.Region} object
     * @param constraintName
     *            the name of a constraint
     * @return a list of {@link org.eclipse.uml2.uml.Element} objects
     */
    public static EList<Element> getConstraint(final Region region, final String constraintName) {
        // find the constraint in the model
        final EList<Constraint> constraints = region.getOwnedRules();
        for (final Constraint constraint : constraints) {
            if (constraintName.equals(constraint.getName())) {
                return constraint.getConstrainedElements();
            }
        }
        return null;
    }

    /**
     * Gets the mappings of states to numbers
     *
     * @return the state-number mappings
     */
    public final Map<Vertex, String> getStateMappings() {
        return stateMappings;
    }

    /**
     * Sets a new state-number mapping to the field of this class: stateMapping
     *
     * @param stateMappings
     *            a HashMap object whose key is a state and value is an integer number used in
     *            {@link coverage.web.Path}
     */
    public final void setStateMappings(final Map<Vertex, String> stateMappings) {
        this.stateMappings = stateMappings;
    }

    /**
     * Gets the edges in a String format
     *
     * @return edges
     */
    public final String getEdges() {
        return edges;
    }

    /**
     * Sets new edges
     *
     * @param edges
     *            a String representation of edges
     */
    public final void setEdges(final String edges) {
        this.edges = edges;
    }

    /**
     * Gets the final states in a String format
     *
     * @return a String representation of final states
     */
    public final String getFinalStates() {
        return finalStates;
    }

    /**
     * Sets the final states
     *
     * @param finalStates
     *            the final states, separated by commas
     */
    public final void setFinalStates(final String finalStates) {
        this.finalStates = finalStates;
    }

    /**
     * Gets the initial states in a String format
     *
     * @return a String representation of initial states
     */
    public final String getInitialStates() {
        return initialStates;
    }

    /**
     * Sets the initial sets
     *
     * @param initialStates
     *            the initial states, separated by commas
     */
    public final void setInitialStates(final String initialStates) {
        this.initialStates = initialStates;
    }

    /**
     * Gets the mappings from integers to vertexes
     *
     * @return the number-state mappings
     */
    public final Map<String, Vertex> getReversedStateMappings() {
        return reversedStateMappings;
    }

    /**
     * Sets a new number-state mapping to the field of this class: reversedStateMapping
     *
     * @param reversedStateMappings
     *            a HashMap object whose key is an integer number used in {@link coverage.web.Path
     *            and value is a state
     */
    public final void setReversedStateMappings(final Map<String, Vertex> reversedStateMappings) {
        this.reversedStateMappings = reversedStateMappings;
    }

    /**
     * Gets the transitions.
     *
     * @return a list of {@link org.eclipse.uml2.uml2.Transition} objects
     */
    public final List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Sets a list of {@link org.eclipse.uml2.uml2.Transition} objects to the field of this class:
     * transitions
     *
     * @param transitions
     *            a list of {@link org.eclipse.uml2.uml2.Transition} objects
     */
    public final void setTransitions(final List<Transition> transitions) {
        this.transitions = transitions;
    }

    /**
     * @return the region
     */
    public final Region getRegion() {
        return region;
    }

    /**
     * @param region
     *            the region to set
     */
    public final void setRegion(final Region region) {
        this.region = region;
    }

}
