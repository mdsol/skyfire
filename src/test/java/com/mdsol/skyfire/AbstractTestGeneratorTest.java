/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
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
public class AbstractTestGeneratorTest {

    private String testResourceDir;
    String vendingMachinePath;
    String vendingMachineXmlPath;
    String rocPath;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        testResourceDir = System.getProperty("user.dir") + "/src/test/resources/testData/";
        vendingMachinePath = testResourceDir + "VendingMachine/model/VendingMachineFSM.uml";
        vendingMachineXmlPath = testResourceDir + "VendingMachine/xml/vendingMachineMappings.xml";
        rocPath = testResourceDir + "roc/model/rocBasicModel.uml";
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetTestPaths()
            throws IOException, InvalidInputException, InvalidGraphException {

        EObject object = StateMachineAccessor.getModelObject(vendingMachinePath);
        List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));

        List<Path> paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.PRIMEPATHCOVERAGE);

        /*
         * String[] edges = stateMachine.getEdges().split("\n"); for(String edge : edges){ String[]
         * vertices = edge.split(" "); //System.out.println(vertices[0]+ " :: " + vertices[1]);
         * System.out.println(stateMachine.getReversedStateMappings().get(vertices[0]).getName() +
         * " :: " + stateMachine.getReversedStateMappings().get(vertices[1]).getName());
         * //System.out.println(stateMachine.getReversedStateMappings().get(vertices[1]).getName());
         * }
         */
        assertNotNull(paths);
        System.out.println(paths);
    }

    @Test
    public void testGetTestPathsForRoc()
            throws IOException, InvalidInputException, InvalidGraphException {

        EObject object = StateMachineAccessor.getModelObject(rocPath);
        List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));

        System.out.println(statemachines.get(0));
        System.out.println(regions.get(0));
        System.out.println(stateMachine.getInitialStates());
        System.out.println(stateMachine.getFinalStates());
        System.out.println(stateMachine.getEdges());

        System.out.println(stateMachine.getStateMappings());

        String[] edges = stateMachine.getEdges().split("\n");
        for (String edge : edges) {
            String[] vertices = edge.split(" ");
            // System.out.println(vertices[0]+ " :: " + vertices[1]);
            System.out.println(stateMachine.getReversedStateMappings().get(vertices[0]).getName()
                    + " :: " + stateMachine.getReversedStateMappings().get(vertices[1]).getName());
            // System.out.println(stateMachine.getReversedStateMappings().get(vertices[1]).getName());
        }

        List<Path> paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                TestCoverageCriteria.NODECOVERAGE);

        assertNotNull(paths);
        System.out.println(paths);
        for (Path path : paths) {
            Iterator<Node> nodes = path.getNodeIterator();
            while (nodes.hasNext()) {
                String node = nodes.next().toString();
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
            Iterator<Node> nodes = path.getNodeIterator();
            while (nodes.hasNext()) {
                String node = nodes.next().toString();
                System.out.println(stateMachine.getReversedStateMappings().get(node).getName());
            }
            System.out.println("\n");
        }
    }
}
