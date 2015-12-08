/**
 * 
 */
package com.mdsol.skyfire;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mdsol.skyfire.IdentifiableElementType;
import com.mdsol.skyfire.Mapping;

/**
 * A JUnit test case for class {@link Mapping}
 * 
 * @author Nan Li
 * @version 1.0 Nov 12, 2012
 *
 */
public class MappingTest {

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructorAndGettersAndSetters() {
        String mappingName = "vMachineInit";
        String identifiedElementName = "vm";
        IdentifiableElementType type = IdentifiableElementType.CLASS;
        String testCode = "vendingMachine vm = new vendingMachine();";
        List<String> mappings = new ArrayList<String>();
        List<String> parameters = new ArrayList<String>();

        Mapping mapping = new Mapping(mappingName, type, identifiedElementName, testCode, mappings,
                parameters, null, null);

        assertEquals(mapping.getName(), "vMachineInit");
        assertEquals(mapping.getIdentifiableElementName(), "vm");
        assertEquals(mapping.getType(), IdentifiableElementType.CLASS);
        assertEquals(mapping.getTestCode(), "vendingMachine vm = new vendingMachine();");
        assertEquals(mapping.getRequiredMappings().size(), 0);
        assertEquals(mapping.getParameters().size(), 0);
    }

}
