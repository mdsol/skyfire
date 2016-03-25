/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;

import coverage.graph.InvalidGraphException;
import coverage.web.InvalidInputException;

/**
 * A class that generates Cucumber tests
 *
 * @author Nan Li
 * @version 1.0 Nov 19, 2015
 * @version 2015.1.0
 */
public class CucumberTestGenerator {

    private List<? extends Test> tests;
    private static Logger logger = LogManager.getLogger("CucumberTestGenerator");

    /**
     * Constructs a test with no parameters
     *
     * @param tests
     *            a list of tests
     */
    public CucumberTestGenerator(final List<? extends Test> tests) {
        this.tests = tests;
    }

    /**
     * Gets the tests
     *
     * @return a list of tests
     */
    public final List<? extends Test> getTests() {
        return tests;
    }

    /**
     * Sets the tests
     *
     * @param tests
     *            a list of tests
     */
    public final void setTests(final List<? extends Test> tests) {
        this.tests = tests;
    }

    /**
     * Parses the test and extract identifiable elements from the model Each test is a scenario
     *
     * @param featureDescription
     *            the description of a feature
     * @return a StringBuffer object that represents the feature scenarios
     */
    final StringBuffer generateScenarios(final String featureDescription) {
        StringBuffer sb = new StringBuffer();
        sb.append("Feature: ");
        sb.append(featureDescription + "\n");
        sb.append("\n");

        for (final Test test : tests) {
            if (test instanceof FsmTest) {
                sb.append("Scenario: " + test.getTestComment() + "\n");
                boolean firstWhen = true;
                if (((FsmTest) test).getPath() != null) {
                    for (final Transition transition : ((FsmTest) test).getPath()) {
                        if (transition != null && transition.getName() != null
                                && transition.getName().indexOf("initialize") >= 0) {
                            sb.append("Given " + transition.getName() + "\n");
                        } else if (transition != null && transition.getName() != null
                                && firstWhen) {
                            sb.append("When " + transition.getName() + "\n");
                            sb = addConstriants(sb, transition);

                            if (transition.getTarget() instanceof State
                                    && ((State) transition.getTarget()).getStateInvariant() != null)
                                firstWhen = true;
                            else
                                firstWhen = false;

                        } else if (transition != null && transition.getName() != null
                                && !firstWhen) {
                            sb.append("And " + transition.getName() + "\n");
                            sb = addConstriants(sb, transition);

                            if (transition.getTarget() instanceof State
                                    && ((State) transition.getTarget()).getStateInvariant() != null)
                                firstWhen = true;
                        } else {
                            continue;
                        }
                    }
                } else {
                    logger.debug(test.getTestName() + " does not have paths");
                }
                sb.append("\n\n");
            }
        }

        return sb;
    }

    /**
     * Analyze the constraints and convert them into Cucumber steps with Then
     *
     * @param sb
     *            a StringBuffer object
     * @param transition
     *            a transition from a path
     * @return a new StringBuffer object that has constraints
     */
    private static StringBuffer addConstriants(final StringBuffer sb, final Transition transition) {
        StringBuffer newStrBuf = sb;

        if (transition.getTarget() instanceof State) {
            Constraint invariant = ((State) transition.getTarget()).getStateInvariant();
            if (invariant != null) {
                sb.append("Then ");
                sb.append(invariant.getName() + "\n");
            }
        }

        return newStrBuf;
    }

    /**
     * Parses the test and extract identifiable elements from the model Each test is a scenario
     *
     * @param featureDescription
     *            the description of the feature file
     * @param tests
     *            a list of tests
     * @param useQualifiedName
     *            specify whether to use qualified name
     * @return a {@code StringBuffer} object that includes the Cucumber scenarios
     */
    private static final StringBuffer generateScenarios(final String featureDescription,
            final List<? extends Test> tests, final boolean useQualifiedName) {
        StringBuffer sb = new StringBuffer();
        sb.append("Feature: ");
        sb.append(featureDescription + "\n");
        sb.append("\n");

        for (final Test test : tests) {
            if (test instanceof FsmTest) {
                sb.append("Scenario: " + test.getTestComment() + "\n\n");
                boolean firstWhen = true;
                if (((FsmTest) test).getPath() != null) {
                    for (final Transition transition : ((FsmTest) test).getPath()) {
                        if (transition != null && transition.getName() != null
                                && transition.getName().indexOf("initialize") >= 0) {
                            if (!useQualifiedName)
                                sb.append("Given " + transition.getName() + "\n");
                            else
                                sb.append("Given " + getQualifiedName(transition) + "\n");
                        } else if (transition != null && transition.getName() != null
                                && firstWhen) {
                            if (!useQualifiedName)
                                sb.append("When " + transition.getName() + "\n");
                            else
                                sb.append("When " + getQualifiedName(transition) + "\n");
                            sb = addConstriants(sb, transition);

                            if (transition.getTarget() instanceof State
                                    && ((State) transition.getTarget()).getStateInvariant() != null)
                                firstWhen = true;
                            else
                                firstWhen = false;
                        } else if (transition != null && transition.getName() != null
                                && !firstWhen) {
                            if (!useQualifiedName)
                                sb.append("And " + transition.getName() + "\n");
                            else
                                sb.append("And " + getQualifiedName(transition) + "\n");
                            sb = addConstriants(sb, transition);

                            if (transition.getTarget() instanceof State
                                    && ((State) transition.getTarget()).getStateInvariant() != null)
                                firstWhen = true;
                        } else {
                            continue;
                        }
                    }
                } else {
                    logger.debug(test.getTestName() + " does not have paths");
                }
                sb.append("\n\n");
            }
        }

        return sb;
    }

    /**
     * Gets the qualified name of a {@code org.eclipse.uml.uml2.Transition} object
     *
     * @param transition
     *            the specified {@code org.eclipse.uml.uml2.Transition} object
     * @return the qualified name of the specified transition
     */
    public static String getQualifiedName(final Transition transition) {
        String qualifiedName = null;
        if (transition != null) {
            qualifiedName = transition.getSource().getQualifiedName() + "." + transition.getName();
        }
        return qualifiedName;
    }

    /**
     * Creates a feature file
     *
     * @param sb
     *            the content of the feature file
     * @param path
     *            the path of the Feature file
     * @throws IOException
     *             throws an IOException when the specified path to the file is not found
     *
     */
    public static final void writeFeatureFile(final StringBuffer sb, final String path)
            throws IOException {
        final String result = sb.toString();

        final BufferedWriter bufferWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
        bufferWriter.write(result);
        bufferWriter.close();
    }

    /**
     * Generates the Cucumber feature file from a UML behavioral model
     *
     * @param umlPath
     *            the path of the UML model
     * @param coverage
     *            the graph coverage that the tests are going to satisfy
     * @param featureDescription
     *            the description of the feature file to be generated
     * @param featureFilePath
     *            the path of the feature file
     * @return true if the feature file is successfully generated; otherwise return false
     * @throws IOException
     *             when specified UML model is not found or the feature file is not found
     * @throws InvalidInputException
     *             when the flattened graph is not valid
     * @throws InvalidGraphException
     *             when the flattened graph is not valid
     */
    public static final boolean generateCucumberScenario(final Path umlPath,
            final TestCoverageCriteria coverage, final String featureDescription,
            final Path featureFilePath)
            throws IOException, InvalidInputException, InvalidGraphException {
        final boolean isGenerated = true;

        // read the UML model
        EObject object = null;
        try {
            object = StateMachineAccessor.getModelObject(umlPath.toString());
        } catch (final IOException e) {
            logger.debug("Cannot find the specified UML model");
            throw new IOException(e);
        }
        final List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        final List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        final StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));
        logger.info("Read the specified UML model from " + umlPath.toString());

        // generate the abstract test paths on the flattened graph
        List<coverage.graph.Path> paths = null;
        try {
            paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                    stateMachine.getInitialStates(), stateMachine.getFinalStates(), coverage);
        } catch (InvalidInputException e) {
            logger.debug("The flattened graph is not valid");
            throw new InvalidInputException(e);
        } catch (InvalidGraphException e) {
            logger.debug("The flattened graph is not valid");
            throw new InvalidGraphException(e);
        }
        logger.info("Generate abstract test paths on the flattened graph");

        // find the matched transitions on the original UML model and construct
        // tests
        final List<com.mdsol.skyfire.FsmTest> tests = new ArrayList<com.mdsol.skyfire.FsmTest>();

        if (paths != null && !paths.isEmpty()) {
            for (int i = 0; i < paths.size(); i++) {
                final List<Transition> transitions = AbstractTestGenerator
                        .convertVerticesToTransitions(
                                AbstractTestGenerator.getPathByState(paths.get(i), stateMachine),
                                stateMachine);

                String pathName = "";
                for (final Transition transition : transitions) {
                    pathName += (transition.getName() != null ? transition.getName() : "") + " ";
                }
                final com.mdsol.skyfire.FsmTest test = new com.mdsol.skyfire.FsmTest(
                        String.valueOf(i), pathName, transitions);
                tests.add(test);
            }
            logger.info("Generate abstract tests");
        } else {
            logger.error("No test paths generated");
        }

        // write the scenarios into the feature file
        final StringBuffer sb = generateScenarios(featureDescription, tests, false);

        try {
            writeFeatureFile(sb, featureFilePath.toString());
        } catch (final IOException e) {
            logger.debug("Cannot write scenarios into the feature file");
            throw new IOException(e);
        }
        logger.info(
                "Create Cucumber feature file which is located at " + featureFilePath.toString());

        return isGenerated;
    }

    /**
     * Generates the Cucumber feature file from a UML behavioral model with qualified names because
     * many transition names are the same in different states.
     *
     * @param umlPath
     *            the path of the UML model
     * @param coverage
     *            the graph coverage that the tests are going to satisfy
     * @param featureDescription
     *            the description of the feature file to be generated
     * @param featureFilePath
     *            the path of the feature file
     * @return true if the feature file is successfully generated; otherwise return false
     * @throws IOException
     *             when the specified model or the feature file to write is not found
     * @throws InvalidInputException
     *             when the flattened graph is not valid
     * @throws InvalidGraphException
     *             when the flattened graph is not valid
     */
    public static final boolean generateCucumberScenarioWithQualifiedName(final Path umlPath,
            final TestCoverageCriteria coverage, final String featureDescription,
            final Path featureFilePath)
            throws IOException, InvalidInputException, InvalidGraphException {
        final boolean isGenerated = true;

        // read the UML model
        EObject object = null;
        try {
            object = StateMachineAccessor.getModelObject(umlPath.toString());
        } catch (final IOException e) {
            logger.debug("Cannot find the specified UML model");
            throw new IOException(e);
        }
        final List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        final List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        final StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));
        logger.info("Read the specified UML model from " + umlPath.toString());

        // generate the abstract test paths on the flattened graph
        List<coverage.graph.Path> paths = null;
        try {
            paths = AbstractTestGenerator.getTestPaths(stateMachine.getEdges(),
                    stateMachine.getInitialStates(), stateMachine.getFinalStates(), coverage);
        } catch (InvalidInputException e) {
            logger.debug("The flattened graph is not valid");
            throw new InvalidInputException(e);
        } catch (InvalidGraphException e) {
            logger.debug("The flattened graph is not valid");
            throw new InvalidGraphException(e);
        }
        logger.info("Generate abstract test paths on the flattened graph");

        // find the matched transitions on the original UML model and construct
        // tests
        final List<com.mdsol.skyfire.FsmTest> tests = new ArrayList<com.mdsol.skyfire.FsmTest>();

        for (int i = 0; i < paths.size(); i++) {
            System.out.println("path: " + paths.get(i));
            final List<Transition> transitions = AbstractTestGenerator.convertVerticesToTransitions(
                    AbstractTestGenerator.getPathByState(paths.get(i), stateMachine), stateMachine);

            String pathName = "";
            for (final Transition transition : transitions) {
                pathName += (transition.getName() != null ? transition.getName() : "") + " ";
            }
            final com.mdsol.skyfire.FsmTest test = new com.mdsol.skyfire.FsmTest(String.valueOf(i),
                    pathName, transitions);
            tests.add(test);
            // System.out.println(test.getTestComment());
        }
        logger.info("Generate abstract tests");

        // write the scenarios into the feature file
        final StringBuffer sb = generateScenarios(featureDescription, tests, true);

        try {
            writeFeatureFile(sb, featureFilePath.toString());
        } catch (final IOException e) {
            logger.debug("Cannot write scenarios into the feature file");
            throw new IOException(e);
        }
        logger.info(
                "Create Cucumber feature file which is located at " + featureFilePath.toString());

        return isGenerated;
    }
}
