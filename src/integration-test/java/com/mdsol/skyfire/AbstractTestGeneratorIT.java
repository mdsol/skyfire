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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private String rocPath;
    private String plinthPath;
    private static Logger logger = LogManager.getLogger("AbstractTestGeneratorIT");

    @Before
    public final void setUp() {
        testResourceDir = System.getProperty("user.dir")
                + "/src/integration-test/resources/testData/";
        vendingMachinePath = testResourceDir + "VendingMachine/model/VendingMachineFSM.uml";
        rocPath = testResourceDir + "roc/model/rocBasicModel.uml";
        plinthPath = testResourceDir + "plinth/model/plinth.uml";
    }

    @After
    public void tearDown() {
        testResourceDir = null;
        vendingMachinePath = null;
        rocPath = null;
        plinthPath = null;
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

        logger.info(statemachines.get(0));
        logger.info(regions.get(0));
        logger.info(stateMachine.getInitialStates());
        logger.info(stateMachine.getFinalStates());
        logger.info(stateMachine.getEdges());

        logger.info(stateMachine.getStateMappings());

        final String[] edges = stateMachine.getEdges().split("\n");
        for (String edge : edges) {
            final String[] vertices = edge.split(" ");
            logger.info(stateMachine.getReversedStateMappings().get(vertices[0]).getName() + " :: "
                    + stateMachine.getReversedStateMappings().get(vertices[1]).getName());
        }

        List<Path> paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.NODECOVERAGE);

        assertNotNull(paths);
        logger.info(paths);

        for (Path path : paths) {
            final Iterator<Node> nodes = path.getNodeIterator();
            while (nodes.hasNext()) {
                final String node = nodes.next().toString();
                logger.info(stateMachine.getReversedStateMappings().get(node).getName());
            }
        }

        paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.EDGECOVERAGE);

        assertNotNull(paths);
        logger.info(paths);
        for (Path path : paths) {
            final Iterator<Node> nodes = path.getNodeIterator();
            while (nodes.hasNext()) {
                final String node = nodes.next().toString();
                logger.info(stateMachine.getReversedStateMappings().get(node).getName());
            }
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

        logger.info(statemachines.get(0));
        logger.info(regions.get(0));
        logger.info(stateMachine.getInitialStates());
        logger.info(stateMachine.getFinalStates());
        logger.info(stateMachine.getEdges());

        for (Transition t : stateMachine.getTransitions()) {
            logger.info(t.getQualifiedName());
            logger.info(stateMachine.getStateMappings().get(t.getSource()) + " "
                    + stateMachine.getStateMappings().get(t.getTarget()));
        }

        for (Vertex v : stateMachine.getStateMappings().keySet()) {
            if (v instanceof State) {
                logger.info(((State) v).getQualifiedName() + " "
                        + stateMachine.getStateMappings().get(v));
            } else if (v instanceof FinalState) {
                logger.info(((FinalState) v).getQualifiedName() + " "
                        + stateMachine.getStateMappings().get(v));
            } else if (((Pseudostate) v).getKind() == PseudostateKind.INITIAL_LITERAL) {
                logger.info(((Pseudostate) v).getQualifiedName() + " "
                        + stateMachine.getStateMappings().get(v));
            }
        }

        List<Path> paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.NODECOVERAGE);

        assertNotNull(paths);
        logger.info(paths);

        paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.EDGECOVERAGE);

        assertNotNull(paths);
        logger.info(paths);

        paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.EDGEPAIRCOVERAGE);

        assertNotNull(paths);
        logger.info(paths);

        paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.PRIMEPATHCOVERAGE);

        assertNotNull(paths);
        logger.info(paths);
    }
}
