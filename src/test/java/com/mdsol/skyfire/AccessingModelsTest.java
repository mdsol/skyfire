package com.mdsol.skyfire;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.acceleo.common.IAcceleoConstants;
import org.eclipse.acceleo.common.internal.utils.AcceleoPackageRegistry;
import org.eclipse.acceleo.common.internal.utils.workspace.AcceleoWorkspaceUtil;
import org.eclipse.acceleo.common.internal.utils.workspace.BundleURLConverter;
import org.eclipse.acceleo.common.utils.ModelUtils;
import org.eclipse.acceleo.model.mtl.MtlPackage;
import org.eclipse.acceleo.model.mtl.resource.AcceleoResourceSetImpl;
import org.eclipse.acceleo.model.mtl.resource.EMtlBinaryResourceFactoryImpl;
import org.eclipse.acceleo.model.mtl.resource.EMtlResourceFactoryImpl;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.ocl.ecore.EcoreEnvironment;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.expressions.ExpressionsPackage;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.FinalState;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Region;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.Vertex;

import coverage.graph.Graph;
import coverage.graph.GraphUtil;
import coverage.graph.InvalidGraphException;
import coverage.graph.Path;
import coverage.web.InvalidInputException;

public class AccessingModelsTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void accessingModelstest() throws IOException, InvalidInputException, InvalidGraphException {
	// String path = "E:\\EclipseDataModeling\\VendingMachine";
	String path = System.getProperty("user.dir") + "/src/test/resources/testData/VendingMachine/model";
	String umlPath = path + "/VendingMachineFSM.uml";

	URI modelURI = URI.createFileURI(umlPath);
	// GenerateFSM generator = new GenerateFSM(modelURI, targetFolder, new ArrayList<Object>());

	URIConverter uriConverter = createURIConverter();

	Map<URI, URI> uriMap = EcorePlugin.computePlatformURIMap();

	ResourceSet modelResourceSet = new AcceleoResourceSetImpl();
	modelResourceSet.setPackageRegistry(AcceleoPackageRegistry.INSTANCE);

	if (uriConverter != null) {
	    modelResourceSet.setURIConverter(uriConverter);
	}

	// make sure that metamodel projects in the workspace override those in plugins
	modelResourceSet.getURIConverter().getURIMap().putAll(uriMap);

	registerResourceFactories(modelResourceSet);
	registerPackages(modelResourceSet);

	URI newModelURI = URI.createURI(modelURI.toString(), true);
	EObject model = ModelUtils.load(newModelURI, modelResourceSet);
	System.out.println(newModelURI + "....." + model);
	System.out.println((model instanceof Model) + ((Model) model).getName());

	EList<Package> packages = ((Model) model).getNestedPackages();
	for (Package packageObject : packages) {
	    System.out.println(packageObject.getName());
	}

	EList<Element> elements = ((Model) model).getOwnedElements();

	for (Element elementObject : elements) {
	    System.out.println(elementObject.toString());
	    if (elementObject instanceof StateMachine) {
		EList<Region> regions = ((StateMachine) elementObject).getRegions();
		for (Region region : regions) {
		    System.out.println(region.getName());

		    EList<Transition> transitions = region.getTransitions();
		    EList<Vertex> vertexes = region.getSubvertices();
		    String edges = "";
		    HashMap<String, String> map = new HashMap<String, String>();
		    map.put("State1", "1");
		    map.put("State2", "2");
		    map.put("State3", "3");
		    map.put("State4", "4");
		    map.put("State5", "5");
		    map.put("State6", "6");
		    map.put("State7", "7");
		    map.put("State8", "8");
		    map.put("State9", "9");
		    // map.put("FinalState1", "10");
		    for (Vertex vertex : vertexes) {
			System.out.println(vertex.getName());
			if (vertex instanceof FinalState)
			    map.put(vertex.getName(), "10");
			if (vertex instanceof Pseudostate) {
			    map.put(vertex.getName(), "0");
			    System.out.println(vertex.getOutgoings().size());
			    EList<Transition> outgoings = vertex.getOutgoings();
			    for (Transition t : outgoings) {
				System.out.println("transiiton: " + t.getName());
			    }
			}
		    }
		    String sourceNumber = "0";
		    String targetNumber = "10";
		    for (Transition transition : transitions) {
			// transitions may not have a source or target because some of them are leftover
			// they do not appear in the UML diagram but they do exist in the UML model
			if (transition.getSource() != null && transition.getTarget() != null) {
			    System.out.println(transition.getSource().getName() + "; " + transition.getTarget().getName());

			    if (transition.getSource().getName().indexOf("[0-10]") != -1) {
			    }
			    if (map.containsKey(transition.getSource().getName())) {
				edges = edges + map.get(transition.getSource().getName());
			    }
			    if (map.containsKey(transition.getTarget().getName())) {
				edges = edges + " " + map.get(transition.getTarget().getName());
			    }
			    edges = edges + "\n";
			}
		    }
		    System.out.println(map);
		    System.out.println(edges);
		    System.out.println(getTestPaths(edges, sourceNumber, targetNumber));
		}
	    }
	}
	// Resource resource = modelResourceSet.getResource(newModelURI, true);
	// resource.getContents();
	/*
	 * final TreeIterator<EObject> targetElements = model.eAllContents(); while (targetElements.hasNext()) { final EObject potentialTarget =
	 * targetElements.next(); System.out.println(potentialTarget.toString());
	 * 
	 * }
	 */

    }

    public List<Path> getTestPaths(String edges, String initialNodes, String finalNodes) throws InvalidInputException, InvalidGraphException {
	Graph g = GraphUtil.readGraph(edges, initialNodes, finalNodes);
	try {
	    g.validate();
	} catch (InvalidGraphException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return g.findEdgeCoverage();

    }

    /**
     * Creates the URI Converter we'll use to load our modules. Take note that this should never be used out of Eclipse.
     * 
     * @return The created URI Converter.
     * @since 3.0
     */
    protected URIConverter createURIConverter() {
	if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
	    return null;
	}

	return new ExtensibleURIConverterImpl() {
	    /**
	     * {@inheritDoc}
	     * 
	     * @see org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl#normalize(org.eclipse.emf.common.util.URI)
	     */
	    @Override
	    public URI normalize(URI uri) {
		URI normalized = getURIMap().get(uri);
		if (normalized == null) {
		    BundleURLConverter conv = new BundleURLConverter(uri.toString());
		    if (conv.resolveBundle() != null) {
			normalized = URI.createURI(conv.resolveAsPlatformPlugin());
			getURIMap().put(uri, normalized);
		    }
		}
		if (normalized != null) {
		    return normalized;
		}
		return super.normalize(uri);
	    }
	};
    }

    /**
     * This will update the resource set's package registry with all usual EPackages.
     * 
     * @param resourceSet
     *            The resource set which registry has to be updated.
     */
    public void registerPackages(ResourceSet resourceSet) {
	resourceSet.getPackageRegistry().put(EcorePackage.eINSTANCE.getNsURI(), EcorePackage.eINSTANCE);

	resourceSet.getPackageRegistry().put(org.eclipse.ocl.ecore.EcorePackage.eINSTANCE.getNsURI(), org.eclipse.ocl.ecore.EcorePackage.eINSTANCE);
	resourceSet.getPackageRegistry().put(ExpressionsPackage.eINSTANCE.getNsURI(), ExpressionsPackage.eINSTANCE);

	resourceSet.getPackageRegistry().put(MtlPackage.eINSTANCE.getNsURI(), MtlPackage.eINSTANCE);

	resourceSet.getPackageRegistry().put("http://www.eclipse.org/ocl/1.1.0/oclstdlib.ecore", //$NON-NLS-1$
		getOCLStdLibPackage());

	if (!isInWorkspace(org.eclipse.uml2.uml.UMLPackage.class)) {
	    resourceSet.getPackageRegistry().put(org.eclipse.uml2.uml.UMLPackage.eINSTANCE.getNsURI(), org.eclipse.uml2.uml.UMLPackage.eINSTANCE);
	}
    }

    /**
     * This will update the resource set's resource factory registry with all usual factories.
     * 
     * @param resourceSet
     *            The resource set which registry has to be updated.
     */
    public void registerResourceFactories(ResourceSet resourceSet) {
	resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", //$NON-NLS-1$
		new EcoreResourceFactoryImpl());
	resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap().put(IAcceleoConstants.BINARY_CONTENT_TYPE, new EMtlBinaryResourceFactoryImpl());
	resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap().put(IAcceleoConstants.XMI_CONTENT_TYPE, new EMtlResourceFactoryImpl());
    }

    /**
     * Returns the package containing the OCL standard library.
     * 
     * @return The package containing the OCL standard library.
     */
    protected EPackage getOCLStdLibPackage() {
	EcoreEnvironmentFactory factory = new EcoreEnvironmentFactory();
	EcoreEnvironment environment = (EcoreEnvironment) factory.createEnvironment();
	EPackage oclStdLibPackage = (EPackage) EcoreUtil.getRootContainer(environment.getOCLStandardLibrary().getBag());
	environment.dispose();
	return oclStdLibPackage;
    }

    /**
     * Clients can override this if the default behavior doesn't properly find the emtl module URL.
     * 
     * @param moduleName
     *            Name of the module we're searching for.
     * @return The template's URL. This will use Eclipse-specific behavior if possible, and use the class loader otherwise.
     */
    protected URL findModuleURL(String moduleName) {
	URL moduleURL = null;
	if (EMFPlugin.IS_ECLIPSE_RUNNING) {
	    try {
		moduleURL = AcceleoWorkspaceUtil.getResourceURL(getClass(), moduleName);
	    } catch (IOException e) {
		// Swallow this, we'll try and locate the module through the class loader
	    }
	}
	if (moduleURL == null) {
	    moduleURL = getClass().getResource(moduleName);
	}
	return moduleURL;
    }

    /**
     * Creates the URI that will be used to resolve the template that is to be launched.
     * 
     * @param entry
     *            The path towards the template file. Could be a jar or file scheme URI, or we'll assume it represents a relative path.
     * @return The actual URI from which the template file can be resolved.
     */
    protected URI createTemplateURI(String entry) {
	if (entry.startsWith("file:") || entry.startsWith("jar:")) { //$NON-NLS-1$ //$NON-NLS-2$
	    return URI.createURI(URI.decode(entry));
	}
	return URI.createFileURI(URI.decode(entry));
    }

    /**
     * Checks whether the given EPackage class is located in the workspace.
     * 
     * @param ePackageClass
     *            The EPackage class we need to take into account.
     * @return <code>true</code> if the given class has been loaded from a dynamically installed bundle, <code>false</code> otherwise.
     * @since 3.1
     */
    public boolean isInWorkspace(Class<? extends EPackage> ePackageClass) {
	return EMFPlugin.IS_ECLIPSE_RUNNING && AcceleoWorkspaceUtil.INSTANCE.isInDynamicBundle(ePackageClass);
    }

}
