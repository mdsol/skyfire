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
 */
public class CucumberTestGenerator {

    private List<? extends Test> tests;
    private static Logger logger = LogManager.getLogger("CucumberTestGenerator");
    private static final String GIVEN = "Given ";
    private static final String WHEN = "When ";
    private static final String AND = "And ";
    private static final String THEN = "Then ";
    private static final String INVALIDGRAPH_MESSAGE = "The flattened graph is not valid";

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
     * @return a StringBuilder object that represents the feature scenarios
     */
    final StringBuilder generateScenarios(final String featureDescription) {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature: ");
        sb.append(featureDescription + "\n");
        sb.append("\n");

        for (final Test test : tests) {
            if (test instanceof FsmTest) {
                sb.append("Scenario: " + test.getTestComment() + "\n");
                sb = addTest(test, sb);
                sb.append("\n\n");
            }
        }

        return sb;
    }

    /**
     * Analyze the constraints and convert them into Cucumber steps with Then
     *
     * @param sb
     *            a StringBuilder object
     * @param transition
     *            a transition from a path
     * @return a new StringBuilder object that has constraints
     */
    private static StringBuilder addConstriants(final StringBuilder sb,
            final Transition transition) {
        StringBuilder newStrBuf = sb;

        if (transition.getTarget() instanceof State) {
            Constraint invariant = ((State) transition.getTarget()).getStateInvariant();
            if (invariant != null) {
                sb.append(THEN);
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
     *            specify whether to use qualified name to distinguish transitions that have the
     *            same name
     * @return a {@code StringBuilder} object that includes the Cucumber scenarios
     */
    private static final StringBuilder generateScenarios(final String featureDescription,
            final List<? extends Test> tests, final boolean useQualifiedName) {
        StringBuilder sb = new StringBuilder();
        sb.append("Feature: ");
        sb.append(featureDescription + "\n");
        sb.append("\n");

        for (final Test test : tests) {
            if (test instanceof FsmTest) {
                sb.append("Scenario: " + test.getTestComment() + "\n\n");
                sb = addTest(test, sb, useQualifiedName);
                sb.append("\n\n");
            }
        }

        return sb;
    }

    /**
     * Convert a test into the Cucumber format and add it to the StringBuilder object
     *
     * @param test
     *            the test to add
     * @param sb
     *            a StringBuidler object to keep all tests
     * @return the StringBuilder object that has added tests
     */
    private static StringBuilder addTest(final Test test, final StringBuilder sb) {
        StringBuilder newSb = sb;

        if (((FsmTest) test).getPath() == null || ((FsmTest) test).getPath().isEmpty()) {
            logger.debug(test.getTestName() + " does not have paths");
            return newSb;
        }

        boolean firstWhen = true;
        for (final Transition transition : ((FsmTest) test).getPath()) {
            if (transition == null || transition.getName() == null)
                continue;

            if (transition.getName().indexOf("initialize") >= 0) {
                newSb.append(GIVEN + transition.getName() + "\n");
            } else {
                if (firstWhen) {
                    newSb.append(WHEN + transition.getName() + "\n");
                    newSb = addConstriants(newSb, transition);
                    firstWhen = setWhetherFirstWhenAfterFirstWhen(transition);
                } else {
                    newSb.append(AND + transition.getName() + "\n");
                    newSb = addConstriants(newSb, transition);
                    firstWhen = setWhetherFirstWhenAfterAndStep(transition);
                }
            }
        }

        return newSb;
    }

    /**
     * Convert a test into the Cucumber format and add it to the StringBuilder object
     *
     * @param test
     *            the test to add
     * @param sb
     *            a StringBuidler object to keep all tests
     * @param useQualifiedName
     *            specify whether to use qualified names
     * @return the StringBuilder object that has added tests
     */
    private static StringBuilder addTest(final Test test, final StringBuilder sb,
            final boolean useQualifiedName) {
        StringBuilder newSb = sb;

        if (((FsmTest) test).getPath() == null || ((FsmTest) test).getPath().isEmpty()) {
            logger.debug(test.getTestName() + " does not have paths");
            return newSb;
        }

        boolean firstWhen = true;
        for (final Transition transition : ((FsmTest) test).getPath()) {
            if (transition == null || transition.getName() == null)
                continue;

            if (transition.getName().indexOf("initialize") >= 0) {
                newSb = addressGivenStep(newSb, transition, useQualifiedName);
            } else {
                if (firstWhen) {
                    newSb = addressWhenStep(newSb, transition, useQualifiedName);
                    firstWhen = setWhetherFirstWhenAfterFirstWhen(transition);
                } else {
                    newSb = addressAndStep(newSb, transition, useQualifiedName);
                    firstWhen = setWhetherFirstWhenAfterAndStep(transition);
                }
            }
        }

        return sb;
    }

    /**
     * Sets the firstWhen flag after executing the first when step
     *
     * @param transition
     *            the transition that represents the first when step
     * @return true if there is no more when step; otherwise, return false
     */
    private static boolean setWhetherFirstWhenAfterFirstWhen(final Transition transition) {
        return transition.getTarget() instanceof State
                && ((State) transition.getTarget()).getStateInvariant() != null;
    }

    /**
     * Sets the firstWhen flag after executing an And step
     *
     * @param transition
     *            the transition that represents the and step
     * @return true if there is no more when step; otherwise, return false
     */
    private static boolean setWhetherFirstWhenAfterAndStep(final Transition transition) {
        if (transition.getTarget() instanceof State
                && ((State) transition.getTarget()).getStateInvariant() != null)
            return true;
        return false;
    }

    /**
     * Convert a transition into the Cucumber given step and add it to the StringBuilder object
     *
     * @param sb
     *            a StringBuilder object to keep steps
     * @param useQualifiedName
     *            specify whether to use qualified names
     * @return the StringBuilder object that has added steps
     */
    private static StringBuilder addressGivenStep(final StringBuilder sb,
            final Transition transition, final boolean useQualifiedName) {
        StringBuilder newSb = sb;
        if (!useQualifiedName)
            newSb.append(GIVEN + transition.getName() + "\n");
        else
            newSb.append(GIVEN + getQualifiedName(transition) + "\n");

        return newSb;
    }

    /**
     * Convert a transition into the Cucumber when step and add it to the StringBuilder object
     *
     * @param sb
     *            a StringBuilder object to keep steps
     * @param useQualifiedName
     *            specify whether to use qualified names
     * @return the StringBuilder object that has added steps
     */
    private static StringBuilder addressWhenStep(final StringBuilder sb,
            final Transition transition, final boolean useQualifiedName) {
        StringBuilder newSb = sb;
        if (!useQualifiedName)
            newSb.append(WHEN + transition.getName() + "\n");
        else
            newSb.append(WHEN + getQualifiedName(transition) + "\n");
        newSb = addConstriants(newSb, transition);

        return newSb;
    }

    /**
     * Convert a transition into the Cucumber and step and add it to the StringBuilder object
     *
     * @param sb
     *            a StringBuilder object to keep steps
     * @param useQualifiedName
     *            specify whether to use qualified names
     * @return the StringBuilder object that has added steps
     */
    private static StringBuilder addressAndStep(final StringBuilder sb, final Transition transition,
            final boolean useQualifiedName) {
        StringBuilder newSb = sb;
        if (!useQualifiedName)
            newSb.append(AND + transition.getName() + "\n");
        else
            newSb.append(AND + getQualifiedName(transition) + "\n");
        newSb = addConstriants(newSb, transition);

        return newSb;
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
    public static final void writeFeatureFile(final StringBuilder sb, final String path)
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
            logger.debug(INVALIDGRAPH_MESSAGE);
            throw new InvalidInputException(e);
        } catch (InvalidGraphException e) {
            logger.debug(INVALIDGRAPH_MESSAGE);
            throw new InvalidGraphException(e);
        }
        logger.info("Generate abstract test paths on the flattened graph");

        // find the matched transitions on the original UML model and construct
        // tests
        List<? extends Test> tests = generateTests(paths, stateMachine);

        // write the scenarios into the feature file
        final StringBuilder sb = generateScenarios(featureDescription, tests, false);

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
            logger.debug(INVALIDGRAPH_MESSAGE);
            throw new InvalidInputException(e);
        } catch (InvalidGraphException e) {
            logger.debug(INVALIDGRAPH_MESSAGE);
            throw new InvalidGraphException(e);
        }
        logger.info("Generate abstract test paths on the flattened graph");

        List<? extends Test> tests = generateTests(paths, stateMachine);
        // write the scenarios into the feature file
        final StringBuilder sb = generateScenarios(featureDescription, tests, true);

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
     * Generates abstract tests
     *
     * @param paths
     *            a list of {@code edu.gmu.coverage.graph.Path} objects
     * @param stateMachine
     *            the {@code com.mdsol.skyfire.StateMachineAccessor} object
     * @return a list of Tests
     */
    private static List<? extends Test> generateTests(final List<coverage.graph.Path> paths,
            final StateMachineAccessor stateMachine) {
        // find the matched transitions on the original UML model and construct
        // tests
        final List<com.mdsol.skyfire.FsmTest> tests = new ArrayList<>();

        if (paths != null && !paths.isEmpty()) {
            for (int i = 0; i < paths.size(); i++) {
                final List<Transition> transitions = AbstractTestGenerator
                        .convertVerticesToTransitions(
                                AbstractTestGenerator.getPathByState(paths.get(i), stateMachine));

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

        return tests;
    }
}
