package com.mdsol.skyfire;

/**
 * An enumeration that represents the types of identifiable elements in UML models and types used in mappings
 * for such as parameters.
 * 
 * @author Nan Li
 * @version 1.0 Nov 12, 2012
 * @update  June 13, 2013
 */
public enum IdentifiableElementType {
	CLASS, 
	OBJECT,
	//state machine
	TRANSITION, 
	STATE, 
	GUARD, 
	CONSTRAINT, 
	
	//arguments and variables checking for test oracle
	PARAMETER, 
	FIELD,
	PRECONDITION,
	POSTCONDITION,
	STATEINVARIANT,
	TESTORACLE,
	TESTORACLE1,
	TESTORACLE2,
	TESTORACLE3,
	TESTORACLE4,
	TESTORACLE5;
}
