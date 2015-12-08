package com.mdsol.skyfire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
 * @version 2015.1.0
 *
 */
public class AbstractTestGenerator {
    // String globalDirectory = "Users/nli/Documents/workspace/github/TestAutomationFramework/";
    private String globalDirectory = System.getProperty("user.dir");
    private final String tempTestDirectory = "testData/test/temp/";
    private final String tempTestName = "tempTest";
    private static Logger logger = LogManager.getLogger("AbstractTestGenerator");

    // Maps a transition to the mappings
    private final HashMap<Transition, List<Mapping>> hashedTransitionMappings;

    /**
     * Constructs an AbstractTestGenerator with no detailed directories
     */
    public AbstractTestGenerator() {
        hashedTransitionMappings = new HashMap<Transition, List<Mapping>>();
    }

    /**
     * Constructs an AbstractTestGenerator with the global directory
     *
     * @param globalDirectory
     *            the global directory
     */
    public AbstractTestGenerator(final String globalDirectory) {
        this.globalDirectory = globalDirectory;
        hashedTransitionMappings = new HashMap<Transition, List<Mapping>>();
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
        // System.out.println(edges);
        // System.out.println(initialNodes);
        // System.out.println(finalNodes);
        try {
            g.validate();
        } catch (final InvalidGraphException e) {
            logger.debug("The flat graph is invalid");
            e.printStackTrace();
        }

        if (criterion == TestCoverageCriteria.NODECOVERAGE) {
            return g.findNodeCoverage();
        } else if (criterion == TestCoverageCriteria.EDGECOVERAGE) {
            final List<Path> edgeCoverage = g.findEdges();
            final Graph prefix = GraphUtil.getPrefixGraph(edgeCoverage);
            final Graph bipartite = GraphUtil.getBipartiteGraph(prefix, initialNodes, finalNodes);
            final List<Path> splittedPaths = g.splittedPathsFromSuperString(bipartite
                    .findMinimumPrimePathCoverageViaPrefixGraphOptimized(edgeCoverage).get(0),
                    g.findTestPath());

            return splittedPaths;
        } else if (criterion == TestCoverageCriteria.EDGEPAIRCOVERAGE) {
            final List<Path> edgePairs = g.findEdgePairs();
            final Graph prefix = GraphUtil.getPrefixGraph(edgePairs);
            final Graph bipartite = GraphUtil.getBipartiteGraph(prefix, initialNodes, finalNodes);
            final List<Path> splittedPaths = g.splittedPathsFromSuperString(
                    bipartite.findMinimumPrimePathCoverageViaPrefixGraphOptimized(edgePairs).get(0),
                    g.findTestPath());
            return splittedPaths;
        } else {
            final List<Path> primePaths = g.findPrimePaths();
            final Graph prefix = GraphUtil.getPrefixGraph(primePaths);
            final Graph bipartite = GraphUtil.getBipartiteGraph(prefix, initialNodes, finalNodes);
            final List<Path> splittedPaths = g.splittedPathsFromSuperString(bipartite
                    .findMinimumPrimePathCoverageViaPrefixGraphOptimized(g.findPrimePaths()).get(0),
                    g.findTestPath());
            // System.out.println(edges);
            // System.out.println("splitted paths: " + splittedPaths.size());
            return splittedPaths;
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
        final List<Vertex> vertices = new ArrayList<Vertex>();
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
     * @param stateMachine
     *            a {@link StateMachineAccessor} object
     * @return a list of {@link org.eclipse.uml2.uml.Transition}s
     */
    public static final List<Transition> convertVerticesToTransitions(final List<Vertex> vertices,
            final StateMachineAccessor stateMachine) {
        final List<Transition> transitions = new ArrayList<Transition>();

        for (int i = 0; i < vertices.size();) {
            final Vertex source = vertices.get(i);
            if (i == vertices.size() - 1) {
                break;
            } else {
                i++;
            }

            final Vertex destination = vertices.get(i);
            for (final Transition transition : source.getOutgoings()) {
                if (transition.getTarget() == null) {
                    try {
                        throw new Exception(transition.getName() + " has no target state");
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
                // Now the first right transition is used
                // but there may be more than one transition between two vertices
                // this issue will be taken care of later
                if (transition.getTarget().getName().equals(destination.getName())) {
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
        final List<Test> tests = new ArrayList<Test>();

        for (int i = 0; i < paths.size(); i++) {
            Test test = null;

            if (modelAccessor instanceof StateMachineAccessor) {
                convertVerticesToTransitions(
                        getPathByState(paths.get(i), (StateMachineAccessor) modelAccessor),
                        (StateMachineAccessor) modelAccessor);
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

        if (element instanceof Vertex) {
            for (final Mapping precondition : constraints) {
                if (precondition.getIdentifiableElementName().equals(((Vertex) element).getName())
                        && precondition.getType() == IdentifiableElementType.PRECONDITION) {
                    finalMappings.add(precondition);
                }
            }

            for (final Mapping stateinvariant : constraints) {
                // System.out.println(((Vertex) element).getName() + " " +
                // stateinvariant.getIdentifiableElementName());
                if (stateinvariant.getIdentifiableElementName().equals(((Vertex) element).getName())
                        && stateinvariant.getType() == IdentifiableElementType.STATEINVARIANT) {
                    finalMappings.add(stateinvariant);
                }
            }
        }

        if (element instanceof Transition) {
            for (final Mapping precondition : constraints) {
                if (precondition.getIdentifiableElementName()
                        .equals(((Transition) element).getName())
                        && precondition.getType() == IdentifiableElementType.PRECONDITION) {
                    finalMappings.add(precondition);
                }
            }

            for (final Mapping stateinvariant : constraints) {
                if (stateinvariant.getIdentifiableElementName()
                        .equals(((Transition) element).getName())
                        && stateinvariant.getType() == IdentifiableElementType.STATEINVARIANT) {
                    finalMappings.add(stateinvariant);
                }
            }
        }
        return finalMappings;
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
        final List<Mapping> mappings = new ArrayList<Mapping>();

        if (constraints != null) {
            for (final ConstraintMapping constraint : constraints) {
                // add precondition mappings
                if (constraint.getPreconditions() != null) {
                    if (constraint.getPreconditions().size() > 0) {
                        for (final String precondition : constraint.getPreconditions()) {
                            mappings.add(new Mapping(constraint.getName(),
                                    IdentifiableElementType.PRECONDITION, precondition,
                                    constraint.getTestCode(), constraint.getRequiredMappings(),
                                    constraint.getParameters(), constraint.getCallers(),
                                    constraint.getReturnObjects()));
                        }
                    }
                }
                // add state invariant mappings
                if (constraint.getStateinvariants() != null) {
                    if (constraint.getStateinvariants().size() > 0) {
                        for (final String stateinvariant : constraint.getStateinvariants()) {
                            mappings.add(new Mapping(constraint.getName(),
                                    IdentifiableElementType.STATEINVARIANT, stateinvariant,
                                    constraint.getTestCode(), constraint.getRequiredMappings(),
                                    constraint.getParameters(), constraint.getCallers(),
                                    constraint.getReturnObjects()));
                        }
                    }
                }
                // add postcondition mappings
                if (constraint.getPostconditions() != null) {
                    if (constraint.getPostconditions().size() > 0) {
                        for (final String postcondition : constraint.getPostconditions()) {
                            mappings.add(new Mapping(constraint.getName(),
                                    IdentifiableElementType.POSTCONDITION, postcondition,
                                    constraint.getTestCode(), constraint.getRequiredMappings(),
                                    constraint.getParameters(), constraint.getCallers(),
                                    constraint.getReturnObjects()));
                        }
                    }
                }
            }
        }

        return mappings;
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
        return tempTestDirectory;
    }

    /**
     * @return the tempTestName
     */
    public final String getTempTestName() {
        return tempTestName;
    }

    /**
     * @return the hashedTransitionMappings
     */
    public final HashMap<Transition, List<Mapping>> getHashedTransitionMappings() {
        return hashedTransitionMappings;
    }

}
