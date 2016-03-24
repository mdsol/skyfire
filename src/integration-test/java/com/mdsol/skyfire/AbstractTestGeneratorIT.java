/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.PseudostateKind;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import coverage.graph.InvalidGraphException;
import coverage.graph.Node;
import coverage.graph.Path;
import coverage.web.InvalidInputException;

/**
 * A JUnit test case for class {@link AbstractTestGenerator}
 *
 * @author Nan Li
 * @version 1.0 Nov 28, 2012
 *
 */
public class AbstractTestGeneratorIT {

    private String testResourceDir;
    private String vendingMachinePath;
    private String vendingMachineXmlPath;
    private String rocPath;
    private String plinthPath;

    /**
     * @throws java.lang.Exception
     *             when exceptions happen
     */
    @Before
    public final void setUp() throws Exception {
        testResourceDir = System.getProperty("user.dir")
                + "/src/integration-test/resources/testData/";
        vendingMachinePath = testResourceDir + "VendingMachine/model/VendingMachineFSM.uml";
        vendingMachineXmlPath = testResourceDir + "VendingMachine/xml/vendingMachineMappings.xml";
        rocPath = testResourceDir + "roc/model/rocBasicModel.uml";
        plinthPath = testResourceDir + "plinth/model/plinth.uml";
    }

    /**
     * @throws java.lang.Exception
     *             when exceptions happen
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     *
     * @throws IOException
     *             when IO exceptions happen
     * @throws InvalidInputException
     *             when there are invalid inputs for a graph
     * @throws InvalidGraphException
     *             when the graph is invalid
     */
    @Test
    public final void testGetTestPaths()
            throws IOException, InvalidInputException, InvalidGraphException {

        final EObject object = StateMachineAccessor.getModelObject(vendingMachinePath);
        final List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        final List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        final StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));

        final List<Path> paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.PRIMEPATHCOVERAGE);

        assertNotNull(paths);
        System.out.println(paths);
    }

    /**
     *
     * @throws IOException
     *             when IO exceptions happen
     * @throws InvalidInputException
     *             when there are invalid inputs for a graph
     * @throws InvalidGraphException
     *             when the graph is invalid
     */
    @Test
    public final void testGetTestPathsForRoc()
            throws IOException, InvalidInputException, InvalidGraphException {

        final EObject object = StateMachineAccessor.getModelObject(rocPath);
        final List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        final List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        final StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));

        System.out.println(statemachines.get(0));
        System.out.println(regions.get(0));
        System.out.println(stateMachine.getInitialStates());
        System.out.println(stateMachine.getFinalStates());
        System.out.println(stateMachine.getEdges());

        System.out.println(stateMachine.getStateMappings());

        final String[] edges = stateMachine.getEdges().split("\n");
        for (String edge : edges) {
            final String[] vertices = edge.split(" ");
            // System.out.println(vertices[0]+ " :: " + vertices[1]);
            System.out.println(stateMachine.getReversedStateMappings().get(vertices[0]).getName()
                    + " :: " + stateMachine.getReversedStateMappings().get(vertices[1]).getName());
        }

        List<Path> paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.NODECOVERAGE);

        assertNotNull(paths);
        System.out.println(paths);
        for (Path path : paths) {
            final Iterator<Node> nodes = path.getNodeIterator();
            while (nodes.hasNext()) {
                final String node = nodes.next().toString();
                System.out.println(stateMachine.getReversedStateMappings().get(node).getName());
            }
            System.out.println("\n");
        }

        paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.EDGECOVERAGE);

        assertNotNull(paths);
        System.out.println(paths);
        for (Path path : paths) {
            final Iterator<Node> nodes = path.getNodeIterator();
            while (nodes.hasNext()) {
                final String node = nodes.next().toString();
                System.out.println(stateMachine.getReversedStateMappings().get(node).getName());
            }
            System.out.println("\n");
        }
    }

    /**
     *
     * @throws IOException
     *             when IO exceptions happen
     * @throws InvalidInputException
     *             when there are invalid inputs for a graph
     * @throws InvalidGraphException
     *             when the graph is invalid
     */
    @Test
    public final void testGetTestPathsForPlinth()
            throws IOException, InvalidInputException, InvalidGraphException {

        final EObject object = StateMachineAccessor.getModelObject(plinthPath);
        final List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        final List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        final StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));

        System.out.println(statemachines.get(0));
        System.out.println(regions.get(0));
        System.out.println(stateMachine.getInitialStates());
        System.out.println(stateMachine.getFinalStates());
        System.out.println(stateMachine.getEdges());

        for (Transition t : stateMachine.getTransitions()) {
            System.out.println(t.getQualifiedName());
            System.out.println(stateMachine.getStateMappings().get(t.getSource()) + " "
                    + stateMachine.getStateMappings().get(t.getTarget()));
        }

        for (Vertex v : stateMachine.getStateMappings().keySet()) {
            if (v instanceof State) {
                System.out.println(((State) v).getQualifiedName() + " "
                        + stateMachine.getStateMappings().get(v));
            } else if (v instanceof FinalState) {
                System.out.println(((FinalState) v).getQualifiedName() + " "
                        + stateMachine.getStateMappings().get(v));
            } else if (((Pseudostate) v).getKind() == PseudostateKind.INITIAL_LITERAL) {
                System.out.println(((Pseudostate) v).getQualifiedName() + " "
                        + stateMachine.getStateMappings().get(v));
            }
        }

        // final String[] edges = stateMachine.getEdges().split("\n");
        // for (String edge : edges) {
        // final String[] vertices = edge.split(" ");
        // // System.out.println(vertices[0]+ " :: " + vertices[1]);
        // System.out.println(stateMachine.getReversedStateMappings().get(vertices[0]).getName()
        // + " :: " + stateMachine.getReversedStateMappings().get(vertices[1]).getName());
        // }

        List<Path> paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.NODECOVERAGE);

        assertNotNull(paths);
        System.out.println("\n\n\n");
        System.out.println(paths);

        paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.EDGECOVERAGE);

        assertNotNull(paths);
        System.out.println("\n\n\n");
        System.out.println(paths);

        paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.EDGEPAIRCOVERAGE);

        System.out.println("\n\n\n");
        assertNotNull(paths);
        System.out.println(paths);

        paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.PRIMEPATHCOVERAGE);

        System.out.println("\n\n\n");
        assertNotNull(paths);
        System.out.println(paths);
    }
}