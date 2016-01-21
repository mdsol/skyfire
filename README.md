# skyfire

[![Build Status](https://travis-ci.org/mdsol/skyfire.svg?branch=develop)](https://travis-ci.org/mdsol/skyfire)

## Introduction
Skyfire is a Model-Based Testing (MBT) tool that generates [Cucumber](https://cucumber.io/) test scenarios  from a UML diagram.
Currently, skyfire supports Eclipse Modeling Framework (EMF) - based UML state machine diagrams.
Users can draw EMF-based UML diagrams using [Papyrus](https://eclipse.org/papyrus/) or [UMLDesigner](http://www.umldesigner.org/). 

## Installation
Run jars-installation.sh to install the coverage-0.9 jar, org.eclipse.uml2.common_1.7.0.v20120913-1441.jar, and org.eclipse.uml2.types_1.0.0.v20120913-1441.jar locally because these libraries are not available in any public Maven repository

## Usage

### Compile the whole project
mvn clean compile

### Check Checkstyle Report
mvn site

## Run unit tests
mvn clean test

## Generate source and JavaDoc Jars
mvn package

##Stage for Deployment and Release (project owners only)
mvn clean deploy

##Perform a Release Deployment (project owners only)
mvn clean deploy -P release

## License
Code and documentation copyright 2015-2016 Medidata Solutions, Inc. Code released under the MIT license.
