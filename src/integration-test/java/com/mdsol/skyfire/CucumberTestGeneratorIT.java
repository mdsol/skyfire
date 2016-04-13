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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class CucumberTestGeneratorIT {
    private CucumberTestGenerator generator;
    private List<com.mdsol.skyfire.Test> tests;

    private String testResourceDir;
    private String rocPath;
    private String rocDirectory;
    private static final String ROCFEATUREFILENAME = "roc.feature";
    private static final String FIRSTTESTNAME = "first test";
    private String plinthPath;
    private String plinthDirectory;

    private static Logger logger = LogManager.getLogger("CucumberTestGeneratorIT");

    /**
     * Initialize variables
     */
    @Before
    public void setUp() {
        tests = new ArrayList<>();
        generator = new CucumberTestGenerator(tests);

        testResourceDir = System.getProperty("user.dir") + "/src/integration-test/resources/";
        rocPath = testResourceDir + "testData/roc/model/rocBasicModel.uml";
        rocDirectory = testResourceDir + "testData/roc/";
        plinthPath = testResourceDir + "testData/plinth/model/plinth.uml";
        plinthDirectory = testResourceDir + "testData/plinth/";
    }

    /**
     * Release resources
     */
    @After
    public void tearDown() {
        tests = null;
        generator = null;
    }

    @Test
    public void testConstructorAndGettersAndSetters() {
        FsmTest test = new FsmTest();
        test.setTestName(FIRSTTESTNAME);
        tests.add(test);

        assertEquals(FIRSTTESTNAME, generator.getTests().get(0).getTestName());
    }

    @Test
    public void testGenerateScenarios() {
        FsmTest test = new FsmTest();
        test.setTestName(FIRSTTESTNAME);
        tests.add(test);
        String featureDescription = "first feature";

        StringBuilder sb = generator.generateScenarios(featureDescription);
        assertNotNull(sb);
    }

    @Test
    public void testGenerateScenariosRoc()
            throws IOException, InvalidInputException, InvalidGraphException {
        EObject object = StateMachineAccessor.getModelObject(rocPath);
        List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));
        List<Path> paths = null;
        try {
            paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                    stateMachine.getInitialStates(), stateMachine.getFinalStates(),
                    TestCoverageCriteria.PRIMEPATHCOVERAGE);
        } catch (InvalidGraphException e) {
            logger.error("An invalid graph");
            logger.error("The initial states are: " + stateMachine.getInitialStates());
            logger.error("The final states are: " + stateMachine.getFinalStates());
            logger.error("The edges are: " + stateMachine.getEdges());

            throw new InvalidGraphException(e);
        }

        // get the vertices from a path and return a list of transitions based on the vertices
        if (paths != null && !paths.isEmpty()) {
            for (int i = 0; i < paths.size(); i++) {
                logger.info("No. " + i + " path: " + paths.get(i));
                List<Transition> transitions = AbstractTestGenerator.convertVerticesToTransitions(
                        AbstractTestGenerator.getPathByState(paths.get(i), stateMachine));

                String pathName = "";
                for (Transition transition : transitions) {
                    pathName += (transition.getName() != null ? transition.getName() : "") + " ";
                }
                com.mdsol.skyfire.Test test = new com.mdsol.skyfire.FsmTest(String.valueOf(i),
                        pathName, transitions);
                tests.add(test);
                logger.info("Test comment: " + test.getTestComment());
            }
        } else {
            logger.error("No test paths generated");
        }

        String featureDescription = "Roc feature file generated from a state machine diagram";

        StringBuilder sb = generator.generateScenarios(featureDescription);
        assertNotNull(sb);

        CucumberTestGenerator.writeFeatureFile(sb, rocDirectory + ROCFEATUREFILENAME);
        File file = new File(rocDirectory + ROCFEATUREFILENAME);
        assertTrue(file.exists());
    }

    /**
     * This tests show how a user should use skyfire to generate Cucumber features
     *
     * @throws IOException
     *             when the model does not exist
     * @throws InvalidGraphException
     *             when the converted flatten graph is not valid
     * @throws InvalidInputException
     *             when the converted flatten graph is not valid
     */
    @Test
    public void testGenerateScenariosRocUsingExternalAPI()
            throws IOException, InvalidInputException, InvalidGraphException {
        String featureDescription = "Roc feature file generated from a state machine diagram";
        boolean generated = CucumberTestGenerator.generateCucumberScenario(Paths.get(rocPath),
                TestCoverageCriteria.EDGECOVERAGE, featureDescription,
                Paths.get(rocDirectory + ROCFEATUREFILENAME));
        assertTrue(generated);
        File file = new File(rocDirectory + ROCFEATUREFILENAME);
        assertTrue(file.exists());
    }

    /**
     * This tests show how a user should use skyfire to generate Cucumber features
     *
     * @throws IOException
     *             when the specified model or the feature file to write is not found
     * @throws InvalidGraphException
     *             when the converted flattened graph is valid
     * @throws InvalidInputException
     *             when the converted flattened graph is valid
     */
    @Test
    public void testGenerateScenariosPlinth()
            throws IOException, InvalidInputException, InvalidGraphException {
        String featureDescription = "Plinth feature file generated from a state machine diagram";
        boolean generated = CucumberTestGenerator.generateCucumberScenarioWithQualifiedName(
                Paths.get(plinthPath), TestCoverageCriteria.NODECOVERAGE, featureDescription,
                Paths.get(plinthDirectory + "PlinthNodeCoverage.feature"));
        assertTrue(generated);
        File file = new File(plinthDirectory + "PlinthNodeCoverage.feature");
        assertTrue(file.exists());

        generated = CucumberTestGenerator.generateCucumberScenarioWithQualifiedName(
                Paths.get(plinthPath), TestCoverageCriteria.EDGECOVERAGE, featureDescription,
                Paths.get(plinthDirectory + "PlinthEdgeCoverage.feature"));
        assertTrue(generated);
        file = new File(plinthDirectory + "PlinthEdgeCoverage.feature");
        assertTrue(file.exists());

        generated = CucumberTestGenerator.generateCucumberScenarioWithQualifiedName(
                Paths.get(plinthPath), TestCoverageCriteria.EDGEPAIRCOVERAGE, featureDescription,
                Paths.get(plinthDirectory + "PlinthEdgePairCoverage.feature"));
        assertTrue(generated);
        file = new File(plinthDirectory + "PlinthEdgePairCoverage.feature");
        assertTrue(file.exists());

        generated = CucumberTestGenerator.generateCucumberScenarioWithQualifiedName(
                Paths.get(plinthPath), TestCoverageCriteria.PRIMEPATHCOVERAGE, featureDescription,
                Paths.get(plinthDirectory + "PlinthPrimeCoverage.feature"));
        assertTrue(generated);
        file = new File(plinthDirectory + "PlinthPrimeCoverage.feature");
        assertTrue(file.exists());
    }
}
