/**
 * 
 */
package com.mdsol.skyfire;

import java.util.List;

import org.eclipse.uml2.uml.Transition;

/**
 * A class that represents an object of test for state machine diagrams
 * @author Nan Li
 * @version 1.0 Feb 11, 2013
 */
public class FsmTest extends Test{

	private List<Transition> path;

	/**
	 * 
	 */
	public FsmTest() {
		super();
	}
	
	/**
	 * 
	 */
	public FsmTest(String testName, String testComment, List<Transition> path) {
		super(testName, testComment);
		this.path = path;
	}

	/**
	 * Gets the abstract test path of the test
	 * @return a list of {@link org.eclipse.uml2.uml.Transition}s
	 */
	public List<Transition> getPath() {
		return path;
	}

	/**
	 * Sets the abstract test path of the test
	 * @param path	a list of {@link org.eclipse.uml2.uml.Transition}s 
	 */
	public void setPath(List<Transition> path) {
		this.path = path;
	}

}
