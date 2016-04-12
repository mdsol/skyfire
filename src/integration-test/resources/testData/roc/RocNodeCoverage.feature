Feature: Roc feature file generated from a state machine diagram

Scenario: initializeWithInvalidKeys startEmrWithErrorStatus  
Given initializeWithInvalidKeys
When startEmrWithErrorStatus
Then invalidKeyErrorOccur


Scenario: initializeWithValidKeys  startEmr addValidSteps addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When addValidSteps
And addInvalidSteps
And checkUntilGettingErrors
Then invalidStepErrorOccur


Scenario: initializeWithValidKeys  startEmr startUntilRunning runUntilWaiting addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When startUntilRunning
Then emrIsRunning
When runUntilWaiting
Then stepsAreCompleteWaiting
When addInvalidSteps
And checkUntilGettingErrors
Then invalidStepErrorOccur


Scenario: initializeWithValidKeys  startEmr addValidSteps checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When addValidSteps
And checkInvalidStepId
Then invalidStepIdErrorOccur
When terminate
Then terminationIsSuccess


