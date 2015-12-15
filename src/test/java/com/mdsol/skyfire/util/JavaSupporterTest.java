/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire.util;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JavaSupporterTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRemoveSemiColon() {
        String string = "vm.getCredit() == 0;;;;";
        assertEquals("vm.getCredit() == 0", JavaSupporter.removeSemiColon(string));
    }

    @Test
    public void testReturnPakcages() {
        assertEquals("edu/gmu/", JavaSupporter.returnPackages("package    edu.gmu;"));
    }

    @Test
    public void testCleanUpPackageName() {
        assertEquals("edu.gmu", JavaSupporter.cleanUpPackageName("package    edu.gmu;;"));
    }
}
