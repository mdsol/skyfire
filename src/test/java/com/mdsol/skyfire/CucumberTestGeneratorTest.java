/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import coverage.graph.InvalidGraphException;
import coverage.graph.Path;
import coverage.web.InvalidInputException;

/**
 * A JUnit test case for class {@link CucumberTestGenerator}
 *
 * @author Nan Li
 * @version 1.0 Nov 19, 2015
 *
 */
public class CucumberTestGeneratorTest {
    private CucumberTestGenerator generator;
    private List<com.mdsol.skyfire.Test> tests;

    private String testResourceDir;
    private String rocPath;
    private String rocDirectory;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        tests = new ArrayList<com.mdsol.skyfire.Test>();
        generator = new CucumberTestGenerator(tests);

        testResourceDir = System.getProperty("user.dir") + "/src/test/resources/";
        rocPath = testResourceDir + "testData/roc/model/rocBasicModel.uml";
        rocDirectory = testResourceDir + "testData/roc/";
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructorAndGettersAndSetters() {
        FsmTest test = new FsmTest();
        test.setTestName("first test");
        tests.add(test);

        assertEquals("first test", generator.getTests().get(0).getTestName());
    }

    @Test
    public void testGenerateScenarios() {
        FsmTest test = new FsmTest();
        test.setTestName("first test");
        tests.add(test);
        String featureDescription = "first feature";

        StringBuffer sb = generator.generateScenarios(featureDescription);
        assertNotNull(sb);
    }

    @Test
    public void testGenerateScenariosRoc() throws IOException, InvalidInputException {
        EObject object = StateMachineAccessor.getModelObject(rocPath);
        List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));
        List<Path> paths = null;
        try {
            paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                    stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                    TestCoverageCriteria.EDGECOVERAGE);
        } catch (InvalidGraphException e) {
            e.printStackTrace();
        }

        // get the vertices from a path and return a list of transitions based on the vertices
        // List<edu.gmu.swe.taf.Test> tests = new ArrayList<edu.gmu.swe.taf.Test>();
        for (int i = 0; i < paths.size(); i++) {
            System.out.println("path: " + paths.get(i));
            List<Transition> transitions = AbstractTestGenerator.convertVerticesToTransitions(
                    AbstractTestGenerator.getPathByState(paths.get(i), stateMachine), stateMachine);

            String pathName = "";
            for (Transition transition : transitions) {
                // System.out.println(transition);
                pathName += (transition.getName() != null ? transition.getName() : "") + " ";
            }
            com.mdsol.skyfire.Test test = new com.mdsol.skyfire.FsmTest(String.valueOf(i), pathName,
                    transitions);
            tests.add(test);
            System.out.println(test.getTestComment());
        }

        String featureDescription = "Roc feature file generated from a state machine diagram";

        StringBuffer sb = generator.generateScenarios(featureDescription);
        assertNotNull(sb);
        System.out.println(sb);

        CucumberTestGenerator.writeFeatureFile(sb, rocDirectory + "Roc.feature");
        File file = new File(rocDirectory + "Roc.feature");
        assertTrue(file.exists());
    }

    /**
     * This tests show how a user should use skyfire to generate Cucumber features
     *
     * @throws IOException
     *             when the model does not exist
     */
    @Test
    public void testGenerateScenariosRocUsingExternalAPI() throws IOException {
        String featureDescription = "Roc feature file generated from a state machine diagram";
        boolean generated = CucumberTestGenerator.generateCucumberScenario(Paths.get(rocPath),
                TestCoverageCriteria.EDGECOVERAGE, featureDescription,
                Paths.get(rocDirectory + "Roc.feature"));
        assertTrue(generated);
        File file = new File(rocDirectory + "Roc.feature");
        assertTrue(file.exists());
    }
}
