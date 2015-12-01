
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
     * Constructs a Test with the test name, test comment, and abstract test path from a state machine diagram
     */
    public Test(String testName, String testComment) {
	this.testName = testName;
	this.testComment = testComment;
    }

    /**
     * Gets the concrete test code
     * 
     * @return the concrete test code in a String format
     */
    public String getTestCode() {
	return testCode;
    }

    /**
     * Sets the test code
     * 
     * @param testCode
     *            the test code in a String format
     */
    public void setTestCode(String testCode) {
	this.testCode = testCode;
    }

    /**
     * Gets the comment for the test
     * 
     * @return the test comment in a String format
     */
    public String getTestComment() {
	return testComment;
    }

    /**
     * Sets the test comment
     * 
     * @param testComment
     *            the test comment in a String format
     */
    public void setTestComment(String testComment) {
	this.testComment = testComment;
    }

    /**
     * Gets the test name
     * 
     * @return the test name in a String format
     */
    public String getTestName() {
	return testName;
    }

    /**
     * Sets the test name
     * 
     * @param testName
     *            the test name in a String format
     */
    public void setTestName(String testName) {
	this.testName = testName;
    }

    /**
     * Gets the mappings for the abstract test path
     * 
     * @return a list of {@link edu.gmu.swe.taf.Mapping}s
     */
    public List<Mapping> getMappings() {
	return mappings;
    }

    /**
     * Sets the mappings
     * 
     * @param mappings
     *            a list of {@link edu.gmu.swe.taf.Mapping}s
     */
    public void setMappings(List<Mapping> mappings) {
	this.mappings = mappings;
    }

}
