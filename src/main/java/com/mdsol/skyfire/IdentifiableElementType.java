/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

/**
 * An enumeration that represents the types of identifiable elements in UML models and types used in
 * mappings for such as parameters.
 *
 * @author Nan Li
 * @version 1.0 Nov 12, 2012
 * @update June 13, 2013
 * @version 2015.1.0
 */
public enum IdentifiableElementType {
    CLASS,
    OBJECT,
    // state machine
    TRANSITION,
    STATE,
    GUARD,
    CONSTRAINT,

    // arguments and variables checking for test oracle
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
