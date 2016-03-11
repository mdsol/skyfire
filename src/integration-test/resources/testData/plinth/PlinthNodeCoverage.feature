Feature: Plinth feature file generated from a state machine diagram

Scenario: initialize initialize_client_divisions create create create studies create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.create
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions filter_by_name list create studies create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities filter_by_curated list create sites create client_division_sites create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities create reconcile unreconcile sites create client_division_sites create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.unreconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list filter_by_client_division-filter_by_site create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.filter_by_client_division-filter_by_site
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies create study_environment_sites list filter_by_study_environment-filter_by_client_division_site create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.filter_by_study_environment-filter_by_client_division_site
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies create lock study_environments create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.lock
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies show study_environments create update update study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities create sites list filter_by_medical_facility-filter_by_principal_investigator create client_division_sites create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.filter_by_medical_facility-filter_by_principal_investigator
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list filter_by_parameters show study_environments create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.filter_by_parameters
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies show unlock study_environments create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.unlock
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list create study_environments create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies show active study_environments create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.active
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies create deactivate study_environments create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.deactivate
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies show study_environments list fliter_by_study create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.fliter_by_study
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


