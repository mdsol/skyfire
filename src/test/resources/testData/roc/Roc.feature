Feature: Roc feature file generated from a state machine diagram

Scenario: initializeWithValidKeys  startEmr addValidSteps addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When addValidSteps
And addInvalidSteps
And checkUntilGettingErrors
Then invalidStepError


Scenario: initializeWithValidKeys  startEmr startUntilRunning addValidSteps checkStepsUntilComplete addValidSteps checkUntilRunning runUntilWaiting addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When startUntilRunning
Then runningStatus
When addValidSteps
And checkStepsUntilComplete
Then completeWaitingStatus
When addValidSteps
And checkUntilRunning
Then runningStatus
When runUntilWaiting
Then completeWaitingStatus
When addInvalidSteps
And checkUntilGettingErrors
Then invalidStepError


Scenario: initializeWithValidKeys  startEmr startUntilRunning addInvalidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When startUntilRunning
Then runningStatus
When addInvalidSteps
And checkUntilGettingErrors
Then invalidStepError


Scenario: initializeWithValidKeys  startEmr checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When checkInvalidStepId
Then invalidStepIdError
When terminate
Then terminateSuccess


Scenario: initializeWithInvalidKeys startEmrWithErrorStatus  
Given initializeWithInvalidKeys
When startEmrWithErrorStatus
Then invalidKeyError


Scenario: initializeWithValidKeys  startEmr addInvalidSteps addValidSteps checkUntilGettingErrors  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When addInvalidSteps
And addValidSteps
And checkUntilGettingErrors
Then invalidStepError


Scenario: initializeWithValidKeys  startEmr terminate  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When terminate
Then terminateSuccess


Scenario: initializeWithValidKeys  startEmr startUntilRunning terminate  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When startUntilRunning
Then runningStatus
When terminate
Then terminateSuccess


Scenario: initializeWithValidKeys  startEmr startUntilRunning runUntilWaiting terminate  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When startUntilRunning
Then runningStatus
When runUntilWaiting
Then completeWaitingStatus
When terminate
Then terminateSuccess


Scenario: initializeWithValidKeys  startEmr addValidSteps terminate  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When addValidSteps
And terminate
Then terminateSuccess


Scenario: initializeWithValidKeys  startEmr startUntilRunning checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When startUntilRunning
Then runningStatus
When checkInvalidStepId
Then invalidStepIdError
When terminate
Then terminateSuccess


Scenario: initializeWithValidKeys  startEmr startUntilRunning runUntilWaiting checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When startUntilRunning
Then runningStatus
When runUntilWaiting
Then completeWaitingStatus
When checkInvalidStepId
Then invalidStepIdError
When terminate
Then terminateSuccess


Scenario: initializeWithValidKeys  startEmr addValidSteps checkInvalidStepId terminate  
Given initializeWithValidKeys
When startEmr
Then createSuccess
When addValidSteps
And checkInvalidStepId
Then invalidStepIdError
When terminate
Then terminateSuccess


