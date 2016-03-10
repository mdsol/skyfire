/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * @version 2015.1.0
 */

public class StateMachineAccessor extends ModelAccessor {

    /**
     * the key is a state; the value is an integer number used in {@link coverage.web.Path}
     */
    private HashMap<Vertex, String> stateMappings = new HashMap<Vertex, String>();

    /**
     * the key is an integer number used in {@link coverage.web.Path}; the value is a state
     */
    private HashMap<String, Vertex> reversedStateMappings = new HashMap<String, Vertex>();

    /**
     * transitions in the region of this state machine
     */
    private List<Transition> transitions = new ArrayList<Transition>();

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
        int stateNumber = 1;
        final EList<Vertex> vertices = region.getSubvertices();

        // check if all states are simple
        boolean isSimple = true;
        for (final Vertex vertex : vertices) {
            if (vertex instanceof State) {
                if (((State) vertex).isComposite()) {
                    isSimple = false;
                }
            } else if (vertex instanceof FinalState) {
                if (((FinalState) vertex).isComposite()) {
                    isSimple = false;
                }
            }
        }
        // if all states are simple, execute the if block; otherwise, do the
        // else block
        if (isSimple) {
            for (final Vertex vertex : vertices) {
                if (vertex instanceof Pseudostate
                        && ((Pseudostate) vertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                    final EList<Transition> outgoings = vertex.getOutgoings();
                    if (outgoings.size() > 0) {
                        getStateMappings().put(vertex, new Integer(stateNumber).toString());
                        getReversedStateMappings().put(new Integer(stateNumber).toString(), vertex);
                        setInitialStates(getInitialStates() + new Integer(stateNumber).toString());
                    }
                } else if (vertex instanceof FinalState) {
                    getStateMappings().put(vertex, new Integer(stateNumber).toString());
                    getReversedStateMappings().put(new Integer(stateNumber).toString(), vertex);
                    setFinalStates(getFinalStates() + new Integer(stateNumber).toString());
                } else {
                    getStateMappings().put(vertex, new Integer(stateNumber).toString());
                    getReversedStateMappings().put(new Integer(stateNumber).toString(), vertex);
                }
                stateNumber++;
            }
        } else {
            final List<Vertex> compositeStates = new ArrayList<Vertex>();
            compositeStates.addAll(vertices);
            // System.out.println(compositeStates.size());
            // System.out.println(compositeStates);
            do {
                final Vertex aVertex = compositeStates.get(0);
                // System.out.println(aVertex.toString());
                if (aVertex instanceof Pseudostate
                        && ((Pseudostate) aVertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                    final EList<Transition> outgoings = aVertex.getOutgoings();
                    if (outgoings.size() > 0) {
                        getStateMappings().put(aVertex, new Integer(stateNumber).toString());
                        getReversedStateMappings().put(new Integer(stateNumber).toString(),
                                aVertex);
                        // set the initial if this initial is at the top level
                        // of the region
                        // initials states in composite states are not
                        // considered as initials in the whole graph
                        if (aVertex.getOwner() == region) {
                            setInitialStates(
                                    getInitialStates() + new Integer(stateNumber).toString());
                        }
                        compositeStates.remove(0);
                    }
                } else if (aVertex instanceof FinalState) {
                    getStateMappings().put(aVertex, new Integer(stateNumber).toString());
                    getReversedStateMappings().put(new Integer(stateNumber).toString(), aVertex);
                    // set the final if this final is at the top level of the
                    // region
                    // final states in composite states are not considered as
                    // finals in the whole graph
                    if (aVertex.getOwner() == region) {
                        setFinalStates(getFinalStates() + new Integer(stateNumber).toString());
                    }
                    compositeStates.remove(0);
                } else if (((State) aVertex).isSimple()) {
                    getStateMappings().put(aVertex, new Integer(stateNumber).toString());
                    getReversedStateMappings().put(new Integer(stateNumber).toString(), aVertex);
                    compositeStates.remove(0);
                } else if (((State) aVertex).isComposite()) {
                    final List<Vertex> localVertices = getStates((State) aVertex);
                    // this composite state does not have any sub-vertices when
                    // the size is 0
                    if (localVertices.size() == 0) {
                        getStateMappings().put(aVertex, new Integer(stateNumber).toString());
                        getReversedStateMappings().put(new Integer(stateNumber).toString(),
                                aVertex);
                    } else if (localVertices.size() > 0) {
                        compositeStates.addAll(localVertices);
                    }
                    compositeStates.remove(0);
                }
                stateNumber++;

            } while (compositeStates.size() > 0);
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
        final EList<Transition> firstLevelTransitions = region.getTransitions();
        final List<Transition> compoStateTrans = new ArrayList<Transition>();

        for (final Transition transition : firstLevelTransitions) {
            // transitions may not have a source or target because some of them
            // are isolated because model editors might leave them accidentally
            // they do not appear in the UML diagram but they do exist in the
            // UML model
            if (transition.getSource() != null && transition.getTarget() != null) {
                // System.out.println(
                // transition.getSource().getName() + "; " + transition.getTarget().getName());

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
                        // firstLevelTransitions.add(transition);
                        getTransitions().add(transition);
                    } else if ((transition.getSource() instanceof State
                            && ((State) transition.getSource()).isComposite())
                            || (transition.getTarget() instanceof State
                                    && ((State) transition.getTarget()).isComposite())) {
                        compoStateTrans.add(transition);
                    }
                }
            }
        }

        // mark composite states whose sub-states have been added to the list
        // transCompoState
        final HashMap<Vertex, Boolean> compositeStates = new HashMap<Vertex, Boolean>();
        final List<Transition> simpleTrans = new ArrayList<Transition>();
        if (compoStateTrans.size() > 0) {
            do {
                // handle the first transition in every iteration
                final Transition t = compoStateTrans.get(0);

                if (t.getSource() instanceof State && ((State) t.getSource()).isComposite()) {

                    final List<Vertex> sourceVertices = ((State) t.getSource()).getRegions().get(0)
                            .getSubvertices();
                    // initialize initial states, final states, and regular states in the source
                    final List<Pseudostate> sourceInitialStates = new ArrayList<Pseudostate>();
                    final List<FinalState> sourceFinalStates = new ArrayList<FinalState>();
                    final List<State> sourceStates = new ArrayList<State>();

                    for (final Vertex vertex : sourceVertices) {
                        if (vertex instanceof Pseudostate && ((Pseudostate) vertex)
                                .getKind() == PseudostateKind.INITIAL_LITERAL) {
                            sourceInitialStates.add((Pseudostate) vertex);
                        } else if (vertex instanceof FinalState) {
                            sourceFinalStates.add((FinalState) vertex);
                        } else if (vertex instanceof State) {
                            sourceStates.add((State) vertex);
                        }
                    }

                    if (sourceFinalStates.size() > 0) {
                        for (final FinalState finalState : sourceFinalStates) {
                            // the transition creation supposes to be updated
                            // for the cases that transitions have guards,
                            // effects or others
                            final Transition tempTran = region.createTransition(t.getName());

                            tempTran.setSource(finalState);
                            tempTran.setTarget(t.getTarget());

                            compoStateTrans.add(tempTran);
                        }
                    } else if (sourceStates.size() > 0) {
                        for (final State state : sourceStates) {

                            final Transition tempTran = region.createTransition(t.getName());
                            tempTran.setSource(state);
                            tempTran.setTarget(t.getTarget());

                            compoStateTrans.add(tempTran);
                        }

                        /*
                         * I decide not to create a transition from an initial state of a composite
                         * state to other states since the initial state is not a stable state
                         * for(Pseudostate initialState : sourceInitialStates){
                         *
                         * Transition tempTran = region.createTransition(t.getName());
                         * tempTran.setSource(initialState); tempTran.setTarget(t.getTarget());
                         *
                         * transCompoState.add(tempTran); }
                         */
                    }

                    // if the sub-states of this composite state have been added
                    // to the list, add them
                    if (!compositeStates.containsKey((t.getSource()))) {
                        for (final Transition transition : ((State) t.getSource()).getRegions()
                                .get(0).getTransitions()) {
                            compoStateTrans.add(transition);
                        }
                        compositeStates.put(t.getSource(), true);
                    }

                    compoStateTrans.remove(0);
                } else if (t.getTarget() instanceof State
                        && ((State) t.getTarget()).isComposite()) {
                    final List<Vertex> targetVertices = ((State) t.getTarget()).getRegions().get(0)
                            .getSubvertices();
                    // initialize initial states, final states, and regular states in the target
                    // composite state
                    final List<Pseudostate> targetInitialStates = new ArrayList<Pseudostate>();
                    final List<FinalState> targetFinalStates = new ArrayList<FinalState>();
                    final List<State> targetStates = new ArrayList<State>();

                    for (final Vertex vertex : targetVertices) {
                        if (vertex instanceof Pseudostate && ((Pseudostate) vertex)
                                .getKind() == PseudostateKind.INITIAL_LITERAL) {
                            targetInitialStates.add((Pseudostate) vertex);
                        } else if (vertex instanceof FinalState) {
                            targetFinalStates.add((FinalState) vertex);
                        } else if (vertex instanceof State) {
                            targetStates.add((State) vertex);
                        }
                    }

                    if (targetInitialStates.size() > 0) {
                        for (final Pseudostate initialState : targetInitialStates) {

                            final Transition tempTran = region.createTransition(t.getName());
                            tempTran.setSource(t.getSource());
                            tempTran.setTarget(initialState);

                            compoStateTrans.add(tempTran);
                        }
                    } else if (targetStates.size() > 0) {
                        for (final State state : targetStates) {

                            final Transition tempTran = region.createTransition(t.getName());
                            tempTran.setSource(t.getSource());
                            tempTran.setTarget(state);

                            compoStateTrans.add(tempTran);
                        }
                        /*
                         * final states of a composite state is not connected to the coming
                         * transitions. for(FinalState finalState : targetFinalStates){
                         *
                         * Transition tempTran = region.createTransition(t.getName());
                         * tempTran.setSource(t.getSource()); tempTran.setTarget(finalState);
                         *
                         * transCompoState.add(tempTran); }
                         */
                    }

                    if (!compositeStates.containsKey(t.getTarget())) {
                        for (final Transition transition : ((State) t.getTarget()).getRegions()
                                .get(0).getTransitions()) {
                            compoStateTrans.add(transition);
                        }
                        compositeStates.put(t.getTarget(), true);
                    }
                    compoStateTrans.remove(0);
                } else if ((t.getSource() instanceof State && ((State) t.getSource()).isSimple())
                        && (t.getTarget() instanceof State && ((State) t.getTarget()).isSimple())) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    compoStateTrans.remove(0);
                    edges = updateEdges(edges, t);

                    // add a potential composite state
                    // this address a case in which a transition directly connects from a composite
                    // state to a simple state
                    // in another composite state, which has not been considered.
                    if (((State) t.getTarget()).getContainer() != null
                            && ((State) t.getTarget()).getContainer().getState() != null
                            && !compositeStates.containsKey(
                                    ((State) t.getTarget()).getContainer().getState())) {

                        for (final Transition transition : ((State) t.getTarget()).getContainer()
                                .getTransitions()) {
                            compoStateTrans.add(transition);
                        }
                        compositeStates.put(((State) t.getTarget()).getContainer().getState(),
                                true);
                    }
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
                /*
                 * System.out.println(simpleTrans.size()); for(Transition t1 : simpleTrans){
                 * System.out.println(t1.getName() + " " + t1.getSource().getName() + " " +
                 * t1.getTarget().getName()); } System.out.println(transCompoState.size());
                 * for(Transition t2 : transCompoState){ //System.out.println(t2.getName() );
                 * System.out.println(t2.getName() + " " + t2.getSource().getName() + " " +
                 * t2.getTarget().getName() + (t2.getSource() instanceof State &&
                 * ((State)t2.getSource()).isSimple()) + (t2.getTarget() instanceof State &&
                 * ((State)t2.getTarget()).isSimple())); }
                 */
            } while (compoStateTrans.size() > 0);
        }
        /*
         * System.out.println(getTransitions().size()); for(Transition t3 : getTransitions()){
         * System.out.println(t3.getSource().getName() + " " + t3.getTarget().getName()); }
         */

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
        final List<Region> result = new ArrayList<Region>();
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
        final List<State> result = new ArrayList<State>();

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
        final List<Vertex> result = new ArrayList<Vertex>();

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
        final List<FinalState> result = new ArrayList<FinalState>();

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
        final List<Pseudostate> result = new ArrayList<Pseudostate>();

        final EList<Vertex> vertexes = region.getSubvertices();
        for (final Vertex vertex : vertexes) {
            if (vertex instanceof Pseudostate) {
                // check if the initial node is connected with other nodes
                if (((Pseudostate) vertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                    final EList<Transition> outgoings = vertex.getOutgoings();
                    if (outgoings.size() > 0) {
                        result.add((Pseudostate) vertex);
                    }
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
        final List<NamedElement> result = new ArrayList<NamedElement>();

        // add distinct transitions to the result
        // EList<Transition> transitions = region.getTransitions();
        final List<Transition> transitions = findAllTransitions(region);
        for (final Transition transition : transitions) {
            boolean hasSameName = false;
            for (final NamedElement element : result) {
                if (element.getName().equals(transition.getName())) {
                    hasSameName = true;
                }
            }
            if (!hasSameName && !transition.getName().trim().equals("")) {
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
        final List<Transition> result = new ArrayList<Transition>();
        for (final Transition t : transitions) {
            result.add(t);
        }

        final List<Vertex> compoStates = new ArrayList<Vertex>();

        for (final Vertex vertex : region.getSubvertices()) {
            if (vertex instanceof State && ((State) vertex).isComposite()) {
                compoStates.add(vertex);
            }
        }
        // add transitions in the composite states
        if (compoStates.size() > 0) {
            do {
                final Vertex compo = compoStates.get(0);
                result.addAll(((State) compo).getRegions().get(0).getTransitions());
                for (final Vertex vertex : ((State) compo).getRegions().get(0).getSubvertices()) {
                    if (vertex instanceof State && ((State) vertex).isComposite()) {
                        compoStates.add(vertex);
                    }
                }
                compoStates.remove(0);

            } while (compoStates.size() > 0);
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
    public final HashMap<Vertex, String> getStateMappings() {
        return stateMappings;
    }

    /**
     * Sets a new state-number mapping to the field of this class: stateMapping
     *
     * @param stateMappings
     *            a HashMap object whose key is a state and value is an integer number used in
     *            {@link coverage.web.Path}
     */
    public final void setStateMappings(final HashMap<Vertex, String> stateMappings) {
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
    public final HashMap<String, Vertex> getReversedStateMappings() {
        return reversedStateMappings;
    }

    /**
     * Sets a new number-state mapping to the field of this class: reversedStateMapping
     *
     * @param reversedStateMappings
     *            a HashMap object whose key is an integer number used in {@link coverage.web.Path
     *            and value is a state
     */
    public final void setReversedStateMappings(
            final HashMap<String, Vertex> reversedStateMappings) {
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
