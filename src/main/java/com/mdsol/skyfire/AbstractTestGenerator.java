/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

import coverage.graph.Graph;
import coverage.graph.GraphUtil;
import coverage.graph.InvalidGraphException;
import coverage.graph.Node;
import coverage.graph.Path;
import coverage.web.InvalidInputException;

/**
 * A class that generates abstract tests (that is, test paths) based on UML models
 *
 * @author Nan Li
 * @version 1.0 Nov 28, 2012
 *
 */
public class AbstractTestGenerator {
    private String globalDirectory = System.getProperty("user.dir");
    private static final String TEMPTESTDIR = "testData/test/temp/";
    private static final String TEMPTESTNAME = "tempTest";
    private static Logger logger = LogManager.getLogger("AbstractTestGenerator");

    // Maps a transition to the mappings
    private final HashMap<Transition, List<Mapping>> hashedTransitionMappings;

    /**
     * Constructs an AbstractTestGenerator with no detailed directories
     */
    public AbstractTestGenerator() {
        hashedTransitionMappings = new HashMap<>();
    }

    /**
     * Constructs an AbstractTestGenerator with the global directory
     *
     * @param globalDirectory
     *            the global directory
     */
    public AbstractTestGenerator(final String globalDirectory) {
        this.globalDirectory = globalDirectory;
        hashedTransitionMappings = new HashMap<>();
    }

    /**
     * Generates test paths to satisfy the edge coverage criterion.
     *
     * @param edges
     *            edges of a control flow graph in a String format "1 2 \n 2 3 \n"
     * @param initialNodes
     *            initial nodes of a control flow graph in a String format "1 2 ... etc."
     * @param finalNodes
     *            final nodes of a control flow graph in a String format "1 2 ... etc."
     * @param criterion
     *            an enumeration type of {@link TestCoverageCriteria}
     * @return a list of {@link coverage.graph.Path} objects that satisfy edge coverage
     * @throws InvalidInputException
     *             when any of edges, initialNodes, and finalNodes is in a wrong format
     * @throws InvalidGraphException
     *             when the graph cannot be correctly constructed from the specified inputs
     */
    public static List<Path> getTestPaths(final String edges, final String initialNodes,
            final String finalNodes, final TestCoverageCriteria criterion)
            throws InvalidInputException, InvalidGraphException {
        logger.info("Generate abstract test paths from a flat graph");

        final Graph g = GraphUtil.readGraph(edges, initialNodes, finalNodes);

        try {
            g.validate();
        } catch (final InvalidGraphException e) {
            logger.debug("The flattened generic graph is invalid");
            logger.error(e);
        }

        if (criterion == TestCoverageCriteria.NODECOVERAGE) {
            logger.info(
                    "Node coverage is used. The number of total nodes is " + g.findNodes().size());
            logger.info("The test requirements of nodes are: " + g.findNodes());
            List<Path> testPaths = g.findNodeCoverage();
            logger.info(testPaths.size()
                    + " test paths are generated to satisfy node coverage. The total nodes is "
                    + getTotalNodes(testPaths));
            return testPaths;
        } else if (criterion == TestCoverageCriteria.EDGECOVERAGE) {
            final List<Path> edgeTRs = g.findEdges();
            logger.info("Edge coverage is used. The number of total edges is " + edgeTRs.size());
            logger.info("The test requirements of edges are: " + edgeTRs);
            final Graph prefix = GraphUtil.getPrefixGraph(edgeTRs);
            final Graph bipartite = GraphUtil.getBipartiteGraph(prefix, initialNodes, finalNodes);
            final List<Path> testPaths = g.splittedPathsFromSuperString(
                    bipartite.findMinimumPrimePathCoverageViaPrefixGraphOptimized(edgeTRs).get(0),
                    g.findTestPath());
            logger.info(testPaths.size()
                    + " test paths are generated to satisfy edge coverage. The total nodes is "
                    + getTotalNodes(testPaths));
            return testPaths;
        } else if (criterion == TestCoverageCriteria.EDGEPAIRCOVERAGE) {
            final List<Path> edgePairs = g.findEdgePairs();
            logger.info("Edge-pair coverage is used. The number of total edge-pairs is "
                    + edgePairs.size());
            logger.info("The test requriements of edge-pairs are: " + edgePairs);
            final Graph prefix = GraphUtil.getPrefixGraph(edgePairs);
            final Graph bipartite = GraphUtil.getBipartiteGraph(prefix, initialNodes, finalNodes);
            final List<Path> testPaths = g.splittedPathsFromSuperString(
                    bipartite.findMinimumPrimePathCoverageViaPrefixGraphOptimized(edgePairs).get(0),
                    g.findTestPath());
            logger.info(testPaths.size()
                    + " test paths are generated to satisfy edge-pair coverage. The total nodes is "
                    + getTotalNodes(testPaths));
            return testPaths;
        } else {
            final List<Path> primePaths = g.findPrimePaths();
            final Graph prefix = GraphUtil.getPrefixGraph(primePaths);
            final Graph bipartite = GraphUtil.getBipartiteGraph(prefix, initialNodes, finalNodes);
            final List<Path> testPaths = g.splittedPathsFromSuperString(bipartite
                    .findMinimumPrimePathCoverageViaPrefixGraphOptimized(g.findPrimePaths()).get(0),
                    g.findTestPath());
            logger.info("Prime path coverage is used. The number of total prime paths is "
                    + primePaths.size());
            logger.info("The test requirements of prime paths are: " + primePaths);
            logger.info(testPaths.size()
                    + " test paths are generated to satisfy prime coverage. The total nodes is "
                    + getTotalNodes(testPaths));
            return testPaths;
        }
    }

    /**
     * Transforms a {@link coverage.graph.Path} to a list of {@link org.eclipse.uml2.uml.Vertex} in
     * a UML state machine
     *
     * @param path
     *            a path of a control flow graph in a String format "1, 2, 3, ... etc."
     * @param stateMachine
     *            a StateMachineAccessor object
     * @return a list of {@link org.eclipse.uml2.uml.Vertex}
     */
    public static List<Vertex> getPathByState(final Path path,
            final StateMachineAccessor stateMachine) {
        final List<Vertex> vertices = new ArrayList<>();
        final Iterator<Node> nodes = path.getNodeIterator();

        while (nodes.hasNext()) {
            final Node node = nodes.next();
            vertices.add(stateMachine.getReversedStateMappings().get(node.toString()));
        }
        return vertices;
    }

    /**
     * Transforms a list of {@link org.eclipse.uml2.uml.Vertex} to a list of
     * {@link org.eclipse.uml2.uml.Transition}s in a UML state machine
     *
     * @param vertices
     *            a list of {@link org.eclipse.uml2.uml.Vertex}
     * @return a list of {@link org.eclipse.uml2.uml.Transition}s
     */
    public static final List<Transition> convertVerticesToTransitions(final List<Vertex> vertices) {
        final List<Transition> transitions = new ArrayList<>();

        for (int i = 0; i < vertices.size(); i++) {
            int sourceIndex = i;
            final Vertex sourceVertex = vertices.get(sourceIndex);

            if (i == vertices.size() - 1) {
                break;
            }

            int targetIndex = sourceIndex + 1;

            final Vertex targetVertex = vertices.get(targetIndex);
            for (final Transition transition : sourceVertex.getOutgoings()) {
                if (transition.getTarget() == null) {
                    logger.error(transition.getName() + " has no target state");
                }
                // Now the first right transition is used
                // but there may be more than one transition between two vertices
                // this issue will be taken care of later
                if (transition.getTarget().getName().equals(targetVertex.getName())) {
                    transitions.add(transition);
                    // a bug is fixed; without break statement, extra transitions may be added
                    break;
                }
            }
        }
        return transitions;
    }

    /**
     * Generates tests from the paths of a graph
     *
     * @param paths
     *            a list of {@link coverage.graph.Path}
     * @param modelAccessor
     *            a {@link StateMachineAccessor} object
     * @return a list of {@link edu.gmu.swe.taf.Test} objects
     */
    public final List<Test> generateTests(final List<Path> paths,
            final ModelAccessor modelAccessor) {
        final List<Test> tests = new ArrayList<>();

        for (int i = 0; i < paths.size(); i++) {
            Test test = null;

            if (modelAccessor instanceof StateMachineAccessor) {
                convertVerticesToTransitions(
                        getPathByState(paths.get(i), (StateMachineAccessor) modelAccessor));
                final String testComment = "/** The test path is: " + paths.get(i).toString()
                        + "**/";
                test = new Test("test" + i, testComment);
            }
            tests.add(test);
        }
        return tests;
    }

    /**
     * Adds preconditions, state invariants, and postconditions of an element to the mappings.
     *
     * @param element
     *            a {@link Element} object that owns the constraints
     * @param finalMappings
     *            a list of {@link Mapping} objects that represents the path of the abstract test
     * @param constraints
     *            a list of {@link Mapping} objects that represents preconditions, state invariants,
     *            and postconditions
     * @return a list of {@link Element} objects
     */
    public final List<Mapping> addPreconditionStateInvariantMappings(final Element element,
            final List<Mapping> finalMappings, final List<Mapping> constraints) {
        List<Mapping> mappings = finalMappings;

        if (element instanceof Vertex) {
            for (final Mapping precondition : constraints) {
                if (precondition.getIdentifiableElementName().equals(((Vertex) element).getName())
                        && precondition.getType() == IdentifiableElementType.PRECONDITION) {
                    mappings.add(precondition);
                }
            }

            for (final Mapping stateinvariant : constraints) {
                if (stateinvariant.getIdentifiableElementName().equals(((Vertex) element).getName())
                        && stateinvariant.getType() == IdentifiableElementType.STATEINVARIANT) {
                    mappings.add(stateinvariant);
                }
            }
        }

        if (element instanceof Transition) {
            for (final Mapping precondition : constraints) {
                if (precondition.getIdentifiableElementName()
                        .equals(((Transition) element).getName())
                        && precondition.getType() == IdentifiableElementType.PRECONDITION) {
                    mappings.add(precondition);
                }
            }

            for (final Mapping stateinvariant : constraints) {
                if (stateinvariant.getIdentifiableElementName()
                        .equals(((Transition) element).getName())
                        && stateinvariant.getType() == IdentifiableElementType.STATEINVARIANT) {
                    mappings.add(stateinvariant);
                }
            }
        }
        return mappings;
    }

    /**
     * Adds postconditions of an element to the mappings.
     *
     * @param element
     *            a {@link Element} object that owns the constraints
     * @param finalMappings
     *            a list of {@link Mapping} objects that represents the path of the abstract test
     * @param constraints
     *            a list of {@link Mapping} objects that represents postconditions
     * @return a list of {@link Element} objects
     */
    public final List<Mapping> addPostconditionMappings(final Element element,
            final List<Mapping> finalMappings, final List<Mapping> constraints) {
        if (element instanceof Vertex) {
            for (final Mapping postcondition : constraints) {
                if (postcondition.getIdentifiableElementName().equals(((Vertex) element).getName())
                        && postcondition.getType() == IdentifiableElementType.POSTCONDITION) {
                    finalMappings.add(postcondition);
                }
            }
        }

        if (element instanceof Transition) {
            for (final Mapping postcondition : constraints) {
                if (postcondition.getIdentifiableElementName()
                        .equals(((Transition) element).getName())
                        && postcondition.getType() == IdentifiableElementType.POSTCONDITION) {
                    finalMappings.add(postcondition);
                }
            }
        }
        return finalMappings;
    }

    /**
     * Gets all constraints from the XML file and creates precondition, stateinvariant,
     * postcondition mappings.
     *
     * @param constraints
     *            a list of {@link ConstraintMapping} objects
     * @return a list of {@link Mapping} objects
     */
    public static final List<Mapping> getConstraints(final List<ConstraintMapping> constraints) {
        // a list of mappings to be returned: precondition, stateinvariant, postcondition mappings
        List<Mapping> mappings = new ArrayList<>();

        if (constraints == null)
            return mappings;

        for (final ConstraintMapping constraint : constraints) {
            // add precondition mappings
            if (constraint.getPreconditions() != null && constraint.getPreconditions().size() > 0) {
                mappings = addPreconditions(mappings, constraint);
            }
            // add state invariant mappings
            if (constraint.getStateinvariants() != null
                    && constraint.getStateinvariants().size() > 0) {
                mappings = addStateInvariants(mappings, constraint);
            }
            // add postcondition mappings
            if (constraint.getPostconditions() != null
                    && constraint.getPostconditions().size() > 0) {
                mappings = addPostconditions(mappings, constraint);
            }
        }

        return mappings;
    }

    /**
     * Adds pre-conditions to the mappings
     *
     * @param mappings
     *            the specified mappings that do not have pre-conditions
     * @param constraint
     *            the constraint mapping
     * @return a list of Mapping objects that have pre-conditions
     */
    private static List<Mapping> addPreconditions(final List<Mapping> mappings,
            final ConstraintMapping constraint) {
        List<Mapping> newMappings = mappings;

        for (final String precondition : constraint.getPreconditions()) {
            newMappings.add(new Mapping(constraint.getName(), IdentifiableElementType.PRECONDITION,
                    precondition, constraint.getTestCode(), constraint.getRequiredMappings(),
                    constraint.getParameters(), constraint.getCallers(),
                    constraint.getReturnObjects()));
        }

        return newMappings;
    }

    /**
     * Adds state invariants to the mappings
     *
     * @param mappings
     *            the specified mappings that do not have state invariants
     * @param constraint
     *            the constraint mapping
     * @return a list of Mapping objects that have state invariants
     */
    private static List<Mapping> addStateInvariants(final List<Mapping> mappings,
            final ConstraintMapping constraint) {
        List<Mapping> newMappings = mappings;

        for (final String stateinvariant : constraint.getStateinvariants()) {
            newMappings
                    .add(new Mapping(constraint.getName(), IdentifiableElementType.STATEINVARIANT,
                            stateinvariant, constraint.getTestCode(),
                            constraint.getRequiredMappings(), constraint.getParameters(),
                            constraint.getCallers(), constraint.getReturnObjects()));
        }

        return newMappings;
    }

    /**
     * Adds post-conditions to the mappings
     *
     * @param mappings
     *            the specified mappings that do not have post-conditions
     * @param constraint
     *            the constraint mapping
     * @return a list of Mapping objects that have post-conditions
     */
    private static List<Mapping> addPostconditions(final List<Mapping> mappings,
            final ConstraintMapping constraint) {
        List<Mapping> newMappings = mappings;

        for (final String postcondition : constraint.getPostconditions()) {
            newMappings.add(new Mapping(constraint.getName(), IdentifiableElementType.POSTCONDITION,
                    postcondition, constraint.getTestCode(), constraint.getRequiredMappings(),
                    constraint.getParameters(), constraint.getCallers(),
                    constraint.getReturnObjects()));
        }

        return newMappings;
    }

    /**
     * Calculates the total number of nodes in all the test paths
     *
     * @param testPaths
     *            a list of {@link coverage.graph.Path} objects
     * @return the total number of nodes in the specified test paths
     */
    public static int getTotalNodes(final List<Path> testPaths) {
        int totalNumber = 0;
        if (testPaths == null) {
            return totalNumber;
        } else {
            for (Path p : testPaths) {
                totalNumber += p.size();
            }
        }

        return totalNumber;
    }

    /**
     * @return the globalDirectory
     */
    public final String getGlobalDirectory() {
        return globalDirectory;
    }

    /**
     * Sets the globalDirectory
     *
     * @param globalDirectory
     *            the globalDirectory
     */
    public final void setGlobalDirectory(final String globalDirectory) {
        this.globalDirectory = globalDirectory;
    }

    /**
     * @return the tempTestDirectory
     */
    public final String getTempTestDirectory() {
        return TEMPTESTDIR;
    }

    /**
     * @return the tempTestName
     */
    public final String getTempTestName() {
        return TEMPTESTNAME;
    }

    /**
     * @return the hashedTransitionMappings
     */
    public final Map<Transition, List<Mapping>> getHashedTransitionMappings() {
        return hashedTransitionMappings;
    }

}
