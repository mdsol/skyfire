/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import java.util.List;

/**
 * A class that represents an object of a test.
 *
 * @author Nan Li
 * @version 1.0 Feb 10, 2013
 *
 */
public class Test {

    private String testName;
    private String testComment;
    private List<Mapping> mappings;
    private String testCode;

    /**
     * Constructs a test with no parameters
     */
    public Test() {

    }

    /**
     * Constructs a Test with the test name, test comment, and abstract test path from a state
     * machine diagram
     *
     * @param testName
     *            the name of the test
     * @param testComment
     *            the comment for the test
     */
    public Test(final String testName, final String testComment) {
        this.testName = testName;
        this.testComment = testComment;
    }

    /**
     * Gets the concrete test code
     *
     * @return the concrete test code in a String format
     */
    public final String getTestCode() {
        return testCode;
    }

    /**
     * Sets the test code
     *
     * @param testCode
     *            the test code in a String format
     */
    public final void setTestCode(final String testCode) {
        this.testCode = testCode;
    }

    /**
     * Gets the comment for the test
     *
     * @return the test comment in a String format
     */
    public final String getTestComment() {
        return testComment;
    }

    /**
     * Sets the test comment
     *
     * @param testComment
     *            the test comment in a String format
     */
    public final void setTestComment(final String testComment) {
        this.testComment = testComment;
    }

    /**
     * Gets the test name
     *
     * @return the test name in a String format
     */
    public final String getTestName() {
        return testName;
    }

    /**
     * Sets the test name
     *
     * @param testName
     *            the test name in a String format
     */
    public final void setTestName(final String testName) {
        this.testName = testName;
    }

    /**
     * Gets the mappings for the abstract test path
     *
     * @return a list of {@link edu.gmu.swe.taf.Mapping}s
     */
    public final List<Mapping> getMappings() {
        return mappings;
    }

    /**
     * Sets the mappings
     *
     * @param mappings
     *            a list of {@link edu.gmu.swe.taf.Mapping}s
     */
    public final void setMappings(final List<Mapping> mappings) {
        this.mappings = mappings;
    }

}
