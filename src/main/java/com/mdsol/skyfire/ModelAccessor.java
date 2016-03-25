/*******************************************************************************
 * Copyright 2015, Medidata Solutions, Inc., All Rights Reserved. The program
 * and the accompanying materials are made under the terms of MIT license.
 * Author: Nan Li, nli@mdsol.com
 ******************************************************************************/
package com.mdsol.skyfire;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.StateMachine;

/**
 * A class that provides functions to access UML models. Classes in Acceleo are used to be helpers
 * to access the models.
 *
 * @author Nan Li
 * @version 1.0 Nov 14, 2012
 */
public class ModelAccessor {

    private static Logger logger = LogManager.getLogger("ModelAccessor");

    /**
     * Gets the {@link org.eclipse.emf.ecore.EObject} object of a UML model specified by path
     *
     * @param path
     *            a String representation of the path of a UML model
     * @return an {@link org.eclipse.emf.ecore.EObject} object
     * @throws IOException
     *             when the path to the model is not found
     */
    public static EObject getModelObject(final String path) throws IOException {

        final URI modelURI = URI.createFileURI(path);

        final URIConverter uriConverter = createURIConverter();

        @SuppressWarnings("deprecation")
        final Map<URI, URI> uriMap = EcorePlugin.computePlatformURIMap();

        final ResourceSet modelResourceSet = new AcceleoResourceSetImpl();
        modelResourceSet.setPackageRegistry(AcceleoPackageRegistry.INSTANCE);

        if (uriConverter != null) {
            modelResourceSet.setURIConverter(uriConverter);
        }

        // make sure that metamodel projects in the workspace override those in
        // plugins
        modelResourceSet.getURIConverter().getURIMap().putAll(uriMap);

        registerResourceFactories(modelResourceSet);
        registerPackages(modelResourceSet);

        final URI newModelURI = URI.createURI(modelURI.toString(), true);
        final EObject model = ModelUtils.load(newModelURI, modelResourceSet);
        return model;
    }

    /**
     * Gets all objects of {@link org.eclipse.uml2.uml.StateMachine} in the model
     *
     * @param model
     *            a UML model to parse
     * @return a list of {@link org.eclipse.uml2.uml.StateMachine} in the model
     */
    public static List<StateMachine> getStateMachines(final EObject model) {

        final List<StateMachine> result = new ArrayList<>();
        final EList<Element> elements = ((Model) model).getOwnedElements();

        for (final Element elementObject : elements) {
            if (elementObject instanceof StateMachine) {
                result.add((StateMachine) elementObject);
            }
        }

        return result;
    }

    /**
     * The methods below are copied from Acceleo.
     */

    /**
     * Creates the URI Converter we'll use to load our modules. Take note that this should never be
     * used out of Eclipse.
     *
     * @return The created URI Converter.
     * @since 3.0
     */
    protected static URIConverter createURIConverter() {
        if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
            return null;
        }

        return new ExtensibleURIConverterImpl() {
            /**
             * {@inheritDoc}
             *
             */
            @Override
            public URI normalize(final URI uri) {
                URI normalized = getURIMap().get(uri);
                if (normalized == null) {
                    final BundleURLConverter conv = new BundleURLConverter(uri.toString());
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
    public static void registerPackages(final ResourceSet resourceSet) {
        resourceSet.getPackageRegistry().put(EcorePackage.eINSTANCE.getNsURI(),
                EcorePackage.eINSTANCE);

        resourceSet.getPackageRegistry().put(
                org.eclipse.ocl.ecore.EcorePackage.eINSTANCE.getNsURI(),
                org.eclipse.ocl.ecore.EcorePackage.eINSTANCE);
        resourceSet.getPackageRegistry().put(ExpressionsPackage.eINSTANCE.getNsURI(),
                ExpressionsPackage.eINSTANCE);

        resourceSet.getPackageRegistry().put(MtlPackage.eINSTANCE.getNsURI(), MtlPackage.eINSTANCE);

        resourceSet.getPackageRegistry().put("http://www.eclipse.org/ocl/1.1.0/oclstdlib.ecore",
                getOCLStdLibPackage());

        if (!isInWorkspace(org.eclipse.uml2.uml.UMLPackage.class)) {
            resourceSet.getPackageRegistry().put(
                    org.eclipse.uml2.uml.UMLPackage.eINSTANCE.getNsURI(),
                    org.eclipse.uml2.uml.UMLPackage.eINSTANCE);
        }
    }

    /**
     * This will update the resource set's resource factory registry with all usual factories.
     *
     * @param resourceSet
     *            The resource set which registry has to be updated.
     */
    public static void registerResourceFactories(final ResourceSet resourceSet) {
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore",
                new EcoreResourceFactoryImpl());
        resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap()
                .put(IAcceleoConstants.BINARY_CONTENT_TYPE, new EMtlBinaryResourceFactoryImpl());
        resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap()
                .put(IAcceleoConstants.XMI_CONTENT_TYPE, new EMtlResourceFactoryImpl());
    }

    /**
     * Returns the package containing the OCL standard library.
     *
     * @return The package containing the OCL standard library.
     */
    protected static EPackage getOCLStdLibPackage() {
        final EcoreEnvironmentFactory factory = new EcoreEnvironmentFactory();
        final EcoreEnvironment environment = (EcoreEnvironment) factory.createEnvironment();
        final EPackage oclStdLibPackage = (EPackage) EcoreUtil
                .getRootContainer(environment.getOCLStandardLibrary().getBag());
        environment.dispose();
        return oclStdLibPackage;
    }

    /**
     * Clients can override this if the default behavior doesn't properly find the emtl module URL.
     *
     * @param moduleName
     *            Name of the module we're searching for.
     * @return The template's URL. This will use Eclipse-specific behavior if possible, and use the
     *         class loader otherwise.
     */
    protected final URL findModuleURL(final String moduleName) {
        URL moduleURL = null;
        if (EMFPlugin.IS_ECLIPSE_RUNNING) {
            try {
                moduleURL = AcceleoWorkspaceUtil.getResourceURL(getClass(), moduleName);
            } catch (final IOException e) {
                // Swallow this, we'll try and locate the module through the
                // class loader
                logger.error(e);
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
     *            The path towards the template file. Could be a jar or file scheme URI, or we'll
     *            assume it represents a relative path.
     * @return The actual URI from which the template file can be resolved.
     */
    protected final URI createTemplateURI(final String entry) {
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
     * @return <code>true</code> if the given class has been loaded from a dynamically installed
     *         bundle, <code>false</code> otherwise.
     * @since 3.1
     */
    public static boolean isInWorkspace(final Class<? extends EPackage> ePackageClass) {
        return EMFPlugin.IS_ECLIPSE_RUNNING
                && AcceleoWorkspaceUtil.INSTANCE.isInDynamicBundle(ePackageClass);
    }
}
