Feature: Roc feature file generated from a state machine diagram

Scenario: initializeWithValidKeys  startEmr addValidSteps addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
And addValidSteps
And addInvalidSteps
And checkUntilGettingErrors


Scenario: initializeWithValidKeys  startEmr startUntilRunning addValidSteps checkStepsUntilComplete addValidSteps checkUntilRunning runUntilWaiting addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
And startUntilRunning
And addValidSteps
And checkStepsUntilComplete
And addValidSteps
And checkUntilRunning
And runUntilWaiting
And addInvalidSteps
And checkUntilGettingErrors


Scenario: initializeWithValidKeys  startEmr startUntilRunning addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
And startUntilRunning
And addInvalidSteps
And checkUntilGettingErrors


Scenario: initializeWithValidKeys  startEmr checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
And checkInvalidStepId
And terminate


Scenario: initializeWithInvalidKeys startEmrWithErrorStatus  
Given initializeWithInvalidKeys
When startEmrWithErrorStatus


Scenario: initializeWithValidKeys  startEmr addInvalidSteps addValidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
And addInvalidSteps
And addValidSteps
And checkUntilGettingErrors


Scenario: initializeWithValidKeys  startEmr terminate  
Given initializeWithValidKeys
When startEmr
And terminate


Scenario: initializeWithValidKeys  startEmr startUntilRunning terminate  
Given initializeWithValidKeys
When startEmr
And startUntilRunning
And terminate


Scenario: initializeWithValidKeys  startEmr startUntilRunning runUntilWaiting terminate  
Given initializeWithValidKeys
When startEmr
And startUntilRunning
And runUntilWaiting
And terminate


Scenario: initializeWithValidKeys  startEmr addValidSteps terminate  
Given initializeWithValidKeys
When startEmr
And addValidSteps
And terminate


Scenario: initializeWithValidKeys  startEmr startUntilRunning checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
And startUntilRunning
And checkInvalidStepId
And terminate


Scenario: initializeWithValidKeys  startEmr startUntilRunning runUntilWaiting checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
And startUntilRunning
And runUntilWaiting
And checkInvalidStepId
And terminate


Scenario: initializeWithValidKeys  startEmr addValidSteps checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
And addValidSteps
And checkInvalidStepId
And terminate


