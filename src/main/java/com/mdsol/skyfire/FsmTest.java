/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import java.util.List;

import org.eclipse.uml2.uml.Transition;

/**
 * A class that represents an object of test for state machine diagrams
 *
 * @author Nan Li
 * @version 1.0 Feb 11, 2013
 * @version 2015.1.0
 */
public class FsmTest extends Test {

    private List<Transition> path;

    /**
     * Default Constructor
     */
    public FsmTest() {
        // do nothing for a quick initialization
        super();
    }

    /**
     *
     * @param testName
     *            the name of the test
     * @param testComment
     *            the comment for the test
     * @param path
     *            the path that includes a list of transitions
     */
    public FsmTest(final String testName, final String testComment, final List<Transition> path) {
        super(testName, testComment);
        this.path = path;
    }

    /**
     * Gets the abstract test path of the test
     *
     * @return a list of {@link org.eclipse.uml2.uml.Transition}s
     */
    public final List<Transition> getPath() {
        return path;
    }

    /**
     * Sets the abstract test path of the test
     *
     * @param path
     *            a list of {@link org.eclipse.uml2.uml.Transition}s
     */
    public final void setPath(final List<Transition> path) {
        this.path = path;
    }

}
