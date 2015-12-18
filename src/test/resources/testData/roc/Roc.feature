Feature: Roc feature file generated from a state machine diagram

Scenario: initializeWithValidKeys  startEmr addValidSteps addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When addValidSteps
And addInvalidSteps
And checkUntilGettingErrors
Then invalidStepErrorOccur


Scenario: initializeWithValidKeys  startEmr startUntilRunning addValidSteps checkStepsUntilComplete addValidSteps checkUntilRunning runUntilWaiting addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When startUntilRunning
Then emrIsRunning
When addValidSteps
And checkStepsUntilComplete
Then stepsAreCompleteWaiting
When addValidSteps
And checkUntilRunning
Then emrIsRunning
When runUntilWaiting
Then stepsAreCompleteWaiting
When addInvalidSteps
And checkUntilGettingErrors
Then invalidStepErrorOccur


Scenario: initializeWithValidKeys  startEmr startUntilRunning addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When startUntilRunning
Then emrIsRunning
When addInvalidSteps
And checkUntilGettingErrors
Then invalidStepErrorOccur


Scenario: initializeWithValidKeys  startEmr checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When checkInvalidStepId
Then invalidStepIdErrorOccur
When terminate
Then terminationIsSuccess


Scenario: initializeWithInvalidKeys startEmrWithErrorStatus  
Given initializeWithInvalidKeys
When startEmrWithErrorStatus
Then invalidKeyErrorOccur


Scenario: initializeWithValidKeys  startEmr addInvalidSteps addValidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When addInvalidSteps
And addValidSteps
And checkUntilGettingErrors
Then invalidStepErrorOccur


Scenario: initializeWithValidKeys  startEmr terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When terminate
Then terminationIsSuccess


Scenario: initializeWithValidKeys  startEmr startUntilRunning terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When startUntilRunning
Then emrIsRunning
When terminate
Then terminationIsSuccess


Scenario: initializeWithValidKeys  startEmr startUntilRunning runUntilWaiting terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When startUntilRunning
Then emrIsRunning
When runUntilWaiting
Then stepsAreCompleteWaiting
When terminate
Then terminationIsSuccess


Scenario: initializeWithValidKeys  startEmr addValidSteps terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When addValidSteps
And terminate
Then terminationIsSuccess


Scenario: initializeWithValidKeys  startEmr startUntilRunning checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When startUntilRunning
Then emrIsRunning
When checkInvalidStepId
Then invalidStepIdErrorOccur
When terminate
Then terminationIsSuccess


Scenario: initializeWithValidKeys  startEmr startUntilRunning runUntilWaiting checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When startUntilRunning
Then emrIsRunning
When runUntilWaiting
Then stepsAreCompleteWaiting
When checkInvalidStepId
Then invalidStepIdErrorOccur
When terminate
Then terminationIsSuccess


Scenario: initializeWithValidKeys  startEmr addValidSteps checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then emrCreationIsSuccess
When addValidSteps
And checkInvalidStepId
Then invalidStepIdErrorOccur
When terminate
Then terminationIsSuccess


