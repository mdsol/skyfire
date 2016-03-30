/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import java.util.List;

/**
 * A {@link ConstraintMapping} class that maps test code to a constraint.
 *
 * @author Nan Li
 * @version 1.0 Feb 19, 2013
 * @version 2015.1.0
 */
public class ConstraintMapping extends Mapping {

    // names of the mappings that can be used to solve the constraint
    private List<String> constSolvingMappings;
    // names of the mappings that use this constraint as preconditions
    private List<String> preconditions;
    // names of the mappings that use this constraint as postconditions
    private List<String> postconditions;
    // names of the mappings that use this constraint as state invariants
    private List<String> stateinvariants;

    /**
     * Allocates a {@link ConstraintMapping} object and initialize it to represent the the mapping
     *
     * @param mappingName
     *            the name of the mapping
     * @param type
     *            the type of the identifiable element
     * @param identifiableElementName
     *            the name of the identifiable element
     * @param testCode
     *            the mapped test code
     * @param requiredMappings
     *            the required mappings for this mapping
     * @param parameters
     *            the names of object mappings that are used to be method parameters
     * @param callers
     *            the names of object mappings that are used to call methods
     * @param returnObjects
     *            the names of object mappings that are returned by methods
     * @param constSolvingMappings
     *            the mappings that are used to solve the constraint
     * @param preconditions
     *            the pre-conditions that use this constraint
     * @param postconditions
     *            the post-conditions that use this constraint
     * @param stateinvariants
     *            the state-invariants that use this constraint
     */
    public ConstraintMapping(final String mappingName, final IdentifiableElementType type,
            final String identifiableElementName, final String testCode,
            final List<String> requiredMappings, final List<String> parameters,
            final List<String> callers, final List<String> returnObjects,
            final List<String> constSolvingMappings, final List<String> preconditions,
            final List<String> postconditions, final List<String> stateinvariants) {
        super(mappingName, type, identifiableElementName, testCode, requiredMappings, parameters,
                callers, returnObjects);
        this.constSolvingMappings = constSolvingMappings;
        this.preconditions = preconditions;
        this.postconditions = postconditions;
        this.stateinvariants = stateinvariants;
    }

    /**
     * Gets the names of mappings that used to solve the constraints.
     *
     * @return a list of names of the mappings in a String format
     */
    public final List<String> getConstSolvingMappings() {
        return constSolvingMappings;
    }

    /**
     * Sets the mappings specified by the parameter.
     *
     * @param constSolvingMappings
     *            a list of names of the mappings that solve the constraint
     */
    public final void setConstSolvingMappings(final List<String> constSolvingMappings) {
        this.constSolvingMappings = constSolvingMappings;
    }

    /**
     * Gets the names of mappings that use this constraint as postconditions
     *
     * @return a list of names of the mappings in a String format
     */
    public final List<String> getPostconditions() {
        return postconditions;
    }

    /**
     * Sets the mappings specified by the parameter
     *
     * @param postconditions
     *            a list of names of the mappings that use this constraint as postconditions
     */
    public final void setPostconditions(final List<String> postconditions) {
        this.postconditions = postconditions;
    }

    /**
     * Gets the names of mappings that use this constraint as preconditions
     *
     * @return a list of names of the mappings in a String format
     */
    public final List<String> getPreconditions() {
        return preconditions;
    }

    /**
     * Sets the mappings specified by the parameter
     *
     * @param preconditions
     *            a list of names of the mappings that use this constraint as preconditions
     */
    public final void setPreconditions(final List<String> preconditions) {
        this.preconditions = preconditions;
    }

    /**
     * Gets the names of mappings that use this constraint as state invariants
     *
     * @return a list of names of the mappings in a String format
     */
    public final List<String> getStateinvariants() {
        return stateinvariants;
    }

    /**
     * Sets the mappings specified by the parameter
     *
     * @param stateinvariants
     *            a list of names of the mappings that use this constraint as state invariants
     */
    public final void setStateinvariants(final List<String> stateinvariants) {
        this.stateinvariants = stateinvariants;
    }

}
