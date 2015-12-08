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
 *
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
    public StateMachineAccessor(Region region) {
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
    private void createStateMappings(Region region) {
        int stateNumber = 1;
        EList<Vertex> vertices = region.getSubvertices();

        // check if all states are simple
        boolean isSimple = true;
        for (Vertex vertex : vertices) {
            if (vertex instanceof State) {
                if (((State) vertex).isComposite())
                    isSimple = false;
            } else if (vertex instanceof FinalState) {
                if (((FinalState) vertex).isComposite())
                    isSimple = false;
            }
        }
        // if all states are simple, execute the if block; otherwise, do the
        // else block
        if (isSimple) {
            for (Vertex vertex : vertices) {
                if (vertex instanceof Pseudostate
                        && ((Pseudostate) vertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                    EList<Transition> outgoings = vertex.getOutgoings();
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
            List<Vertex> compositeStates = new ArrayList<Vertex>();
            compositeStates.addAll(vertices);
            // System.out.println(compositeStates.size());
            // System.out.println(compositeStates);
            do {
                Vertex aVertex = compositeStates.get(0);
                // System.out.println(aVertex.toString());
                if (aVertex instanceof Pseudostate
                        && ((Pseudostate) aVertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                    EList<Transition> outgoings = aVertex.getOutgoings();
                    if (outgoings.size() > 0) {
                        getStateMappings().put(aVertex, new Integer(stateNumber).toString());
                        getReversedStateMappings().put(new Integer(stateNumber).toString(),
                                aVertex);
                        // set the initial if this initial is at the top level
                        // of the region
                        // initials states in composite states are not
                        // considered as initials in the whole graph
                        if (aVertex.getOwner() == region)
                            setInitialStates(
                                    getInitialStates() + new Integer(stateNumber).toString());
                        compositeStates.remove(0);
                    }
                } else if (aVertex instanceof FinalState) {
                    getStateMappings().put(aVertex, new Integer(stateNumber).toString());
                    getReversedStateMappings().put(new Integer(stateNumber).toString(), aVertex);
                    // set the final if this final is at the top level of the
                    // region
                    // final states in composite states are not considered as
                    // finals in the whole graph
                    if (aVertex.getOwner() == region)
                        setFinalStates(getFinalStates() + new Integer(stateNumber).toString());
                    compositeStates.remove(0);
                } else if (((State) aVertex).isSimple()) {
                    getStateMappings().put(aVertex, new Integer(stateNumber).toString());
                    getReversedStateMappings().put(new Integer(stateNumber).toString(), aVertex);
                    compositeStates.remove(0);
                } else if (((State) aVertex).isComposite()) {
                    List<Vertex> localVertices = getStates(((State) aVertex));
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
    private void createEdges(Region region) {
        EList<Transition> firstLevelTransitions = region.getTransitions();
        List<Transition> transCompoState = new ArrayList<Transition>();

        for (Transition transition : firstLevelTransitions) {
            // transitions may not have a source or target because some of them
            // are isolated because model editors might leave them accidentally
            // they do not appear in the UML diagram but they do exist in the
            // UML model
            if (transition.getSource() != null && transition.getTarget() != null) {
                // System.out.println(transition.getSource().getName() + "; " +
                // transition.getTarget().getName());

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
                        transCompoState.add(transition);
                    }
                }
            }
        }
        // System.out.println(stateMappings);
        // System.out.println(transCompoState.size());
        // for(Transition tran : transCompoState){
        // System.out.println(tran.getName()+ " " + tran.getSource().getName() +
        // " " + tran.getTarget().getName());
        // }

        // mark composite states whose sub-states have been added to the list
        // transCompoState
        List<Vertex> compositeStates = new ArrayList<Vertex>();
        List<Transition> simpleTrans = new ArrayList<Transition>();
        if (transCompoState.size() > 0) {
            do {
                Transition t = transCompoState.get(0);
                // System.out.println("t: " + t.getName() + " " +
                // t.getSource().getName() + " " + t.getTarget().getName());
                if (t.getSource() instanceof State && ((State) t.getSource()).isComposite()) {

                    List<Vertex> sourceVertices = ((State) t.getSource()).getRegions().get(0)
                            .getSubvertices();
                    // initial states, final states, and regular states
                    List<Pseudostate> sourceInitialStates = new ArrayList<Pseudostate>();
                    List<FinalState> sourceFinalStates = new ArrayList<FinalState>();
                    List<State> sourceStates = new ArrayList<State>();

                    for (Vertex vertex : sourceVertices) {
                        if (vertex instanceof Pseudostate && ((Pseudostate) vertex)
                                .getKind() == PseudostateKind.INITIAL_LITERAL) {
                            sourceInitialStates.add(((Pseudostate) vertex));
                        } else if (vertex instanceof FinalState) {
                            sourceFinalStates.add((FinalState) vertex);
                        } else if (vertex instanceof State) {
                            sourceStates.add((State) vertex);
                        }
                    }

                    if (sourceFinalStates.size() > 0) {
                        for (FinalState finalState : sourceFinalStates) {
                            // the transition creation supposes to be updated
                            // for the cases that transitions have guards,
                            // effects or others
                            Transition tempTran = region.createTransition(t.getName());
                            // System.out.println("t.getname: " + t.getName() +
                            // " : guard : " + t.getGuard());

                            // tempTran.setGuard(t.getGuard());
                            //
                            // System.out.println("temp tran: " +
                            // tempTran.getGuard());
                            //
                            // System.out.println(t.getGuard().getSpecification().stringValue());
                            //
                            // Constraint tempTranGuard =
                            // tempTran.createGuard("Test2ToTest1");
                            // ValueSpecification vs =
                            // tempTranGuard.createSpecification("testSpec",
                            // t.getGuard().getSpecification().getType(),
                            // t.getGuard().getSpecification().eClass());
                            // vs.setVisibility(VisibilityKind.PUBLIC_LITERAL);
                            // vs.setValue(arg0, arg1, arg2);

                            tempTran.setSource(finalState);
                            tempTran.setTarget(t.getTarget());
                            // System.out.println(tempTran.getGuard().getName());
                            // System.out.println(tempTran.getGuard().getSpecification());
                            // System.out.println(tempTran.getGuard().getSpecification().getVisibility());
                            // System.out.println(tempTran.getGuard().getSpecification().stringValue());

                            transCompoState.add(tempTran);
                        }
                    } else if (sourceStates.size() > 0) {
                        for (State state : sourceStates) {

                            Transition tempTran = region.createTransition(t.getName());
                            tempTran.setSource(state);
                            tempTran.setTarget(t.getTarget());

                            transCompoState.add(tempTran);
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
                    if (!compositeStates.contains(((State) t.getSource()))) {
                        for (Transition transition : ((State) t.getSource()).getRegions().get(0)
                                .getTransitions()) {
                            transCompoState.add(transition);
                        }
                        compositeStates.add(((State) t.getSource()));
                    }

                    transCompoState.remove(0);
                } else if (t.getTarget() instanceof State
                        && ((State) t.getTarget()).isComposite()) {
                    List<Vertex> targetVertices = ((State) t.getTarget()).getRegions().get(0)
                            .getSubvertices();
                    // initial states, final states, and regular states
                    List<Pseudostate> targetInitialStates = new ArrayList<Pseudostate>();
                    List<FinalState> targetFinalStates = new ArrayList<FinalState>();
                    List<State> targetStates = new ArrayList<State>();

                    for (Vertex vertex : targetVertices) {
                        if (vertex instanceof Pseudostate && ((Pseudostate) vertex)
                                .getKind() == PseudostateKind.INITIAL_LITERAL) {
                            targetInitialStates.add(((Pseudostate) vertex));
                        } else if (vertex instanceof FinalState) {
                            targetFinalStates.add((FinalState) vertex);
                        } else if (vertex instanceof State) {
                            targetStates.add((State) vertex);
                        }
                    }

                    if (targetInitialStates.size() > 0) {
                        for (Pseudostate initialState : targetInitialStates) {

                            Transition tempTran = region.createTransition(t.getName());
                            tempTran.setSource(t.getSource());
                            tempTran.setTarget(initialState);

                            transCompoState.add(tempTran);
                        }
                    } else if (targetStates.size() > 0) {
                        for (State state : targetStates) {

                            Transition tempTran = region.createTransition(t.getName());
                            tempTran.setSource(t.getSource());
                            tempTran.setTarget(state);

                            transCompoState.add(tempTran);
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

                    if (!compositeStates.contains(((State) t.getTarget()))) {
                        for (Transition transition : ((State) t.getTarget()).getRegions().get(0)
                                .getTransitions()) {
                            transCompoState.add(transition);
                        }
                        compositeStates.add(((State) t.getTarget()));
                    }
                    transCompoState.remove(0);
                } else if ((t.getSource() instanceof State && ((State) t.getSource()).isSimple())
                        && (t.getTarget() instanceof State && ((State) t.getTarget()).isSimple())) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    transCompoState.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getSource() instanceof Pseudostate && ((Pseudostate) t.getSource())
                        .getKind() == PseudostateKind.INITIAL_LITERAL)
                        && (t.getTarget() instanceof State && ((State) t.getTarget()).isSimple())) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    transCompoState.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getTarget() instanceof Pseudostate && ((Pseudostate) t.getTarget())
                        .getKind() == PseudostateKind.INITIAL_LITERAL)
                        && (t.getSource() instanceof State && ((State) t.getSource()).isSimple())) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    transCompoState.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getSource() instanceof State && ((State) t.getSource()).isSimple())
                        && (t.getTarget() instanceof FinalState)) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    transCompoState.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getTarget() instanceof State && ((State) t.getTarget()).isSimple())
                        && (t.getSource() instanceof FinalState)) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    transCompoState.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getSource() instanceof FinalState)
                        && (t.getTarget() instanceof FinalState)) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    transCompoState.remove(0);
                    edges = updateEdges(edges, t);
                } else if ((t.getSource() instanceof Pseudostate && ((Pseudostate) t.getSource())
                        .getKind() == PseudostateKind.INITIAL_LITERAL)
                        && (t.getTarget() instanceof Pseudostate && ((Pseudostate) t.getTarget())
                                .getKind() == PseudostateKind.INITIAL_LITERAL)) {
                    simpleTrans.add(t);
                    getTransitions().add(t);
                    transCompoState.remove(0);
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
            } while (transCompoState.size() > 0);
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
    private String updateEdges(String edges, Transition transition) {
        String edge = stateMappings.get(transition.getSource()) + " "
                + stateMappings.get(transition.getTarget());
        // redundant edges are not allowed
        if (edges.indexOf(edge) == -1) {
            if (stateMappings.containsKey(transition.getSource())) {
                edges = edges + stateMappings.get(transition.getSource());
            }
            if (stateMappings.containsKey(transition.getTarget())) {
                edges = edges + " " + stateMappings.get(transition.getTarget());
            }
            edges = edges + "\n";
        }
        return edges;
    }

    /**
     * Gets all objects of {@link org.eclipse.uml2.uml.Region} in the stateMachine
     * 
     * @param stateMachine
     *            a UML {@link org.eclipse.uml2.uml.StateMachine} object to parse
     * @return a list of {@link org.eclipse.uml2.uml.Region} in the stateMachine
     */
    public static List<Region> getRegions(StateMachine stateMachine) {
        List<Region> result = new ArrayList<Region>();
        EList<Region> regions = stateMachine.getRegions();

        for (Region region : regions) {
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
    public static List<State> getStates(Region region) {
        List<State> result = new ArrayList<State>();

        EList<Vertex> vertexes = region.getSubvertices();
        for (Vertex vertex : vertexes) {
            if (!(vertex instanceof FinalState) && !(vertex instanceof Pseudostate)) {
                result.add(((State) vertex));
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
    public static List<Vertex> getStates(State state) {
        List<Vertex> result = new ArrayList<Vertex>();

        if (state.isComposite()) {
            EList<Vertex> vertexes = state.getRegions().get(0).getSubvertices();
            for (Vertex vertex : vertexes) {
                if ((vertex instanceof FinalState) || vertex instanceof Pseudostate
                        || vertex instanceof State)
                    result.add(vertex);
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
    public static List<FinalState> getFinalStates(Region region) {
        List<FinalState> result = new ArrayList<FinalState>();

        EList<Vertex> vertexes = region.getSubvertices();
        for (Vertex vertex : vertexes) {
            if (vertex instanceof FinalState) {
                result.add(((FinalState) vertex));
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
    public static List<Pseudostate> getInitialStates(Region region) {
        List<Pseudostate> result = new ArrayList<Pseudostate>();

        EList<Vertex> vertexes = region.getSubvertices();
        for (Vertex vertex : vertexes) {
            if (vertex instanceof Pseudostate) {
                // check if the initial node is connected with other nodes
                if (((Pseudostate) vertex).getKind() == PseudostateKind.INITIAL_LITERAL) {
                    EList<Transition> outgoings = vertex.getOutgoings();
                    if (outgoings.size() > 0) {
                        result.add(((Pseudostate) vertex));
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
    public static List<NamedElement> getAllIdentifiableElements(Region region) {
        List<NamedElement> result = new ArrayList<NamedElement>();

        // add distinct transitions to the result
        // EList<Transition> transitions = region.getTransitions();
        List<Transition> transitions = findAllTransitions(region);
        for (Transition transition : transitions) {
            boolean hasSameName = false;
            for (NamedElement element : result) {
                if (element.getName().equals(transition.getName())) {
                    hasSameName = true;
                }
            }
            if (hasSameName == false && !transition.getName().trim().equals(""))
                result.add(transition);
        }

        // add distinct constraints to the result
        EList<Constraint> constraints = region.getOwnedRules();
        for (Constraint constraint : constraints) {
            boolean hasSameName = false;
            for (NamedElement element : result) {
                if (element.getName().equals(constraint.getName())) {
                    hasSameName = true;
                }
            }
            if (hasSameName == false)
                result.add(constraint);
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
    public static List<Transition> findAllTransitions(Region region) {
        EList<Transition> transitions = region.getTransitions();
        // add the transitions at the first level
        List<Transition> result = new ArrayList<Transition>();
        for (Transition t : transitions)
            result.add(t);

        List<Vertex> compoStates = new ArrayList<Vertex>();

        for (Vertex vertex : region.getSubvertices()) {
            if (vertex instanceof State && ((State) vertex).isComposite()) {
                compoStates.add(vertex);
            }
        }
        // add transitions in the composite states
        if (compoStates.size() > 0) {
            do {
                Vertex compo = compoStates.get(0);
                result.addAll(((State) compo).getRegions().get(0).getTransitions());
                for (Vertex vertex : ((State) compo).getRegions().get(0).getSubvertices()) {
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
    public static EList<Element> getConstraint(Region region, String constraintName) {
        // find the constraint in the model
        EList<Constraint> constraints = region.getOwnedRules();
        for (Constraint constraint : constraints) {
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
    public HashMap<Vertex, String> getStateMappings() {
        return stateMappings;
    }

    /**
     * Sets a new state-number mapping to the field of this class: stateMapping
     * @param stateMappings a HashMap object whose key is a state and value is an integer number used in {@link coverage.web.Path}
     */
    public void setStateMappings(HashMap<Vertex, String> stateMappings) {
        this.stateMappings = stateMappings;
    }

    /**
     * Gets the edges in a String format
     * 
     * @return edges
     */
    public String getEdges() {
        return edges;
    }

    /**
     * Sets new edges
     * 
     * @param edges a String representation of edges
     */
    public void setEdges(String edges) {
        this.edges = edges;
    }

    /**
     * Gets the final states in a String format
     * 
     * @return a String representation of final states
     */
    public String getFinalStates() {
        return finalStates;
    }

    /**
     * Sets the final states
     * @param finalStates the final states, separated by commas
     */
    public void setFinalStates(String finalStates) {
        this.finalStates = finalStates;
    }

    /**
     * Gets the initial states in a String format
     * 
     * @return a String representation of initial states
     */
    public String getInitialStates() {
        return initialStates;
    }

    /**
     * Sets the initial sets
     * @param initialStates the initial states, separated by commas
     */
    public void setInitialStates(String initialStates) {
        this.initialStates = initialStates;
    }

    /**
     * Gets the mappings from integers to vertexes
     * 
     * @return the number-state mappings
     */
    public HashMap<String, Vertex> getReversedStateMappings() {
        return reversedStateMappings;
    }

    /**
     * Sets a new number-state mapping to the field of this class: reversedStateMapping
     * @param reversedStateMappings a HashMap object whose key is an integer number used in {@link coverage.web.Path and value is a state
     */
    public void setReversedStateMappings(HashMap<String, Vertex> reversedStateMappings) {
        this.reversedStateMappings = reversedStateMappings;
    }

    /**
     * Gets the transitions.
     * 
     * @return a list of {@link org.eclipse.uml2.uml2.Transition} objects
     */
    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * Sets a list of {@link org.eclipse.uml2.uml2.Transition} objects to the field of this class:
     * transitions
     * 
     * @param transitions a list of {@link org.eclipse.uml2.uml2.Transition} objects
     */
    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }

}
