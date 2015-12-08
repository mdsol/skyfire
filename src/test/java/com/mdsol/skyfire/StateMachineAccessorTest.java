/**
 * 
 */
package com.mdsol.skyfire;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Vertex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mdsol.skyfire.ModelAccessor;

/**
 * A JUnit test case for class {@link StateMachineAccessor}
 * 
 * @author Nan Li
 * @version 1.0 Nov 28, 2012
 *
 */
public class StateMachineAccessorTest {

    private String vendingMachineXmlPath = System.getProperty("user.dir")
            + "/src/test/resources/testData/VendingMachine/model/VendingMachineFSM.uml";

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
    public void testGetStateMachines() throws IOException {
        EObject object = ModelAccessor.getModelObject(vendingMachineXmlPath);
        List<StateMachine> statemachines = ModelAccessor.getStateMachines(object);
        assertEquals(1, statemachines.size());
    }

    @Test
    public void testGetRegions() throws IOException {
        EObject object = ModelAccessor.getModelObject(vendingMachineXmlPath);
        List<StateMachine> statemachines = ModelAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        assertEquals(1, regions.size());
    }

    @Test
    public void testGetInitialStates() throws IOException {
        EObject object = ModelAccessor.getModelObject(vendingMachineXmlPath);
        List<StateMachine> statemachines = ModelAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        List<Pseudostate> initialStates = StateMachineAccessor.getInitialStates(regions.get(0));
        assertEquals(1, initialStates.size());
    }

    @Test
    public void testGetFinalStates() throws IOException {
        EObject object = ModelAccessor.getModelObject(vendingMachineXmlPath);
        List<StateMachine> statemachines = ModelAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        List<FinalState> finalStates = StateMachineAccessor.getFinalStates(regions.get(0));
        assertEquals(1, finalStates.size());
    }

    @Test
    public void testGetStates() throws IOException {
        EObject object = ModelAccessor.getModelObject(vendingMachineXmlPath);
        List<StateMachine> statemachines = ModelAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        List<State> states = StateMachineAccessor.getStates(regions.get(0));
        assertEquals(9, states.size());
    }

    @Test
    public void testCreateStateMappings() throws IOException {
        EObject object = StateMachineAccessor.getModelObject(vendingMachineXmlPath);
        List<StateMachine> statemachines = StateMachineAccessor.getStateMachines(object);
        List<Region> regions = StateMachineAccessor.getRegions(statemachines.get(0));
        StateMachineAccessor stateMachine = new StateMachineAccessor(regions.get(0));
        HashMap<Vertex, String> stateMappings = stateMachine.getStateMappings();
        HashMap<String, Vertex> reversedStateMappings = stateMachine.getReversedStateMappings();
        assertNotNull(stateMappings);
        assertEquals(11, stateMappings.size());
        assertEquals(11, reversedStateMappings.size());
        // System.out.println(reversedStateMappings);
        assertNotNull(stateMachine.getInitialStates());
        assertNotNull(stateMachine.getFinalStates());
        assertNotNull(stateMachine.getEdges());
        System.out.println(stateMachine.getEdges());
    }
}
