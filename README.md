# skyfire

[![Build Status](https://travis-ci.org/mdsol/skyfire.svg?branch=develop)](https://travis-ci.org/mdsol/skyfire)

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

## Deploy to a staging repository
mvn deploy

## Deploy to a public repository
mvn deploy -P release
