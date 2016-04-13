# skyfire

[![Build Status](https://travis-ci.org/mdsol/skyfire.svg?branch=develop)](https://travis-ci.org/mdsol/skyfire)

## Introduction
Skyfire is a Model-Based Testing (MBT) tool that generates [Cucumber](https://cucumber.io/) test scenarios from a UML behavioral diagram.
Currently, skyfire supports Eclipse Modeling Framework (EMF)-based UML state machine diagrams.
Users can create EMF-based UML diagrams using [Papyrus](https://eclipse.org/papyrus/) or [UMLDesigner](http://www.umldesigner.org/). 

For the motivation, algorithms, and more information, please read the [paper](https://cs.gmu.edu/~nli1/2016-nli-MbtWithCucumber.pdf).

## Usage
 * Create a Maven-based Java project
 
 * Include the dependency in the POM file

```
<dependency>
	<groupId>com.mdsol</groupId>
	<artifactId>skyfire</artifactId>
	<version>1.0.1</version>
</dependency>
```
* Call the API and specify the path to the UML diagram, a graph coverage criterion, a feature description, and the path to the Cucumber feature file to generate.
Users can select node coverage, edge coverage, edge-pair coverage, or prime path coverage for the graph coverage criterion.
The definitinos of these four coverage criteria are included in the paper above.

```
CucumberTestGenerator.generateCucumberScenario (
	Paths.get (pathToModel),
	TestCoverageCriteria.SOMECOVERAGE,
	featureDescription,
	Paths.get (pathToFeatureFile));
);
```
* When a UML diagram uses the same name for different behaviors, call another API to use qualified names of transitions to consist of Cucumber test scenarios.
The parameters used are the same as the one above.
```
CucumberTestGenerator.generateCucumberScenarioWithQualifiedName (
	Paths.get (pathToModel),
	TestCoverageCriteria.SOMECOVERAGE,
	featureDescription,
	Paths.get (pathToFeatureFile));
);
```

# Development Instructions

## Installation 
Run jars-installation.sh to install the coverage-0.9 jar, org.eclipse.uml2.common_1.7.0.v20120913-1441.jar, and org.eclipse.uml2.types_1.0.0.v20120913-1441.jar locally because these libraries are not available in any public Maven repository

### Compile The Project
```
mvn clean compile
```

### Check Checkstyle Report
```
mvn site
```
## Run Unit Tests
```
mvn clean test
```

## Run Integration Tests
```
mvn clean integration-test
```

## Generate source and JavaDoc Jars
```
mvn package
```

##Stage for Deployment and Release (project owners only)
```
mvn clean deploy
```

##Perform a Release Deployment (project owners only)
```
mvn clean deploy -P release
```
## License
Code and documentation copyright 2015-2016 Medidata Solutions, Inc. Code released under the MIT license.
