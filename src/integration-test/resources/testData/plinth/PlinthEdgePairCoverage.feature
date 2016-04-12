Feature: Plinth feature file generated from a state machine diagram

Scenario: initialize initialize_client_divisions create studies create study_environments list self-prev-next self-prev-next fliter_by_study list fliter_by_study create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.fliter_by_study
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.fliter_by_study
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list create study_environments self-prev-next create self update study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list create deactivate self self study_environments self-prev-next create study_environment_sites self-prev-next create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.deactivate
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list create lock self self study_environments create self study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.lock
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list filter_by_parameters show study_environments self-prev-next list fliter_by_study create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.filter_by_parameters
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.fliter_by_study
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list filter_by_parameters create deactivate study_environments create self self study_environment_sites list filter_by_study_environment-filter_by_client_division_site create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.filter_by_parameters
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.deactivate
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.filter_by_study_environment-filter_by_client_division_site
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list filter_by_parameters self-prev-next show self unlock self-update deactivate active self-update lock unlock deactivate self study_environments list fliter_by_study create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.filter_by_parameters
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.unlock
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.self-update
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.deactivate
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.active
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.self-update
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.lock
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.unlock
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.deactivate
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.fliter_by_study
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list self-prev-next filter_by_parameters list filter_by_parameters self-prev-next show self active deactivate self active lock study_environments self-prev-next create update self self study_environment_sites self-prev-next create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.filter_by_parameters
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.filter_by_parameters
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.active
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.deactivate
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.active
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.lock
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities create reconcile sites create client_division_sites self-prev-next create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create create create self-update studies create study_environment_sites list self-prev-next self-prev-next filter_by_study_environment-filter_by_client_division_site list filter_by_study_environment-filter_by_client_division_site create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.create
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.self-update
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.filter_by_study_environment-filter_by_client_division_site
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.filter_by_study_environment-filter_by_client_division_site
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities filter_by_curated create sites list self-prev-next self-prev-next filter_by_medical_facility-filter_by_principal_investigator self-prev-next create client_division_sites create self-update study_environment_sites self-prev-next self-prev-next create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.filter_by_medical_facility-filter_by_principal_investigator
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.self-update
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities filter_by_curated self-prev-next create reconcile self-update unreconcile reconcile self-update unreconcile reconcile unreconcile sites self-prev-next self-prev-next list filter_by_medical_facility-filter_by_principal_investigator create self-update client_division_sites create study_environment_sites self-prev-next list filter_by_study_environment-filter_by_client_division_site self-prev-next list self-prev-next filter_by_study_environment-filter_by_client_division_site list filter_by_study_environment-filter_by_client_division_site create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.unreconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.unreconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.unreconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.filter_by_medical_facility-filter_by_principal_investigator
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.self-update
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.filter_by_study_environment-filter_by_client_division_site
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.filter_by_study_environment-filter_by_client_division_site
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.filter_by_study_environment-filter_by_client_division_site
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities filter_by_curated self-prev-next self-prev-next list self-prev-next self-prev-next create sites self-prev-next list filter_by_medical_facility-filter_by_principal_investigator list filter_by_medical_facility-filter_by_principal_investigator create client_division_sites create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.filter_by_medical_facility-filter_by_principal_investigator
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.filter_by_medical_facility-filter_by_principal_investigator
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities create self-update self-update sites create self-update client_division_sites create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.self-update
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities filter_by_curated list filter_by_curated create self-update reconcile self-update sites self-prev-next create self-update self-update client_division_sites create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.self-update
And Plinth::Plinth statemachine::Region1::Site::Region1::site.self-update
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions filter_by_name create studies self-prev-next list filter_by_client_division-filter_by_site self-prev-next self-prev-next create study_environment_sites self-prev-next create-cascaded_create self-update delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.filter_by_client_division-filter_by_site
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.self-update
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions filter_by_name create studies self-prev-next self-prev-next create lock self unlock study_environments self-prev-next self-prev-next create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.lock
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.unlock
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions filter_by_name list self-prev-next-first filter_by_name list filter_by_name self-prev-next create create self-prev-next-first create create create studies create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.self-prev-next-first
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.create
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.self-prev-next-first
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.create
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create self-update create filter_by_name list create studies list self-prev-next self-prev-next filter_by_parameters self-prev-next list self-prev-next create self-update self-update lock study_environments list fliter_by_study self-prev-next create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.self-update
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.create
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.filter_by_parameters
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.self-update
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.self-update
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.lock
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.fliter_by_study
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions self-prev-next-first self-prev-next-first filter_by_name self-prev-next list filter_by_name create create create studies list self-prev-next self-prev-next filter_by_client_division-filter_by_site self-prev-next list filter_by_client_division-filter_by_site self-prev-next list self-prev-next filter_by_client_division-filter_by_site list filter_by_client_division-filter_by_site create self-update self-update study_environment_sites create-cascaded_create self-update self-update delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.self-prev-next-first
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.self-prev-next-first
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.create
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.filter_by_client_division-filter_by_site
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.filter_by_client_division-filter_by_site
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.filter_by_client_division-filter_by_site
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.filter_by_client_division-filter_by_site
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.self-update
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.self-update
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.self-update
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.self-update
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies show unlock self-update study_environments create self update self update self study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.unlock
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.self-update
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.self
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions filter_by_name self-prev-next self-prev-next create self-update self-update studies create self-update lock unlock lock self unlock deactivate study_environments list self-prev-next fliter_by_study create update update update update study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.filter_by_name
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.self-update
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.self-update
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.self-update
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.lock
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.unlock
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.lock
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.self
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.unlock
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.deactivate
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.fliter_by_study
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_medical_facilities self-prev-next filter_by_curated self-prev-next list filter_by_curated list self-prev-next filter_by_curated list create reconcile self-update self-update unreconcile self-update reconcile unreconcile reconcile sites list self-prev-next filter_by_medical_facility-filter_by_principal_investigator list self-prev-next filter_by_medical_facility-filter_by_principal_investigator create client_division_sites list filter_by_client_division-filter_by_site create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Initial3.initialize_medical_facilities
When Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.self-prev-next
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.filter_by_curated
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::MedicalFacilities::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.unreconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.self-update
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.unreconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::uncurated.reconcile
And Plinth::Plinth statemachine::Region1::MedicalFacility::Region1::curated.sites
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.filter_by_medical_facility-filter_by_principal_investigator
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::Sites::Region1::Collection.filter_by_medical_facility-filter_by_principal_investigator
And Plinth::Plinth statemachine::Region1::Sites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::Site::Region1::site.client_division_sites
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Collection.filter_by_client_division-filter_by_site
And Plinth::Plinth statemachine::Region1::ClientDivisionSites::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::ClientDivisionSite::Region1::CDS.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies list filter_by_parameters show study_environments list self-prev-next fliter_by_study create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::Studies::Region1::Collection.filter_by_parameters
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.self-prev-next
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Collection.fliter_by_study
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies show study_environments create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies show active deactivate active study_environments create study_environment_sites create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.active
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.deactivate
And Plinth::Plinth statemachine::Region1::Study::Region1::Inactive.active
And Plinth::Plinth statemachine::Region1::Study::Region1::Active.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


Scenario: initialize initialize_client_divisions create studies show study_environments create update study_environment_sites list filter_by_study_environment-filter_by_client_division_site create-cascaded_create delete 

Given Plinth::Plinth statemachine::Region1::Initial1.initialize
Given Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Initial2.initialize_client_divisions
When Plinth::Plinth statemachine::Region1::ClientDivisions::Region1::Collection.create
And Plinth::Plinth statemachine::Region1::ClientDivision::Region1::CD.studies
And Plinth::Plinth statemachine::Region1::Studies::Region1::Navigation.show
And Plinth::Plinth statemachine::Region1::Study::Region1::Locked.study_environments
And Plinth::Plinth statemachine::Region1::StudyEnvironments::Region1::Navigation.create
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::active.update
And Plinth::Plinth statemachine::Region1::StudyEnvironment::Region1::inactive.study_environment_sites
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.list
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Collection.filter_by_study_environment-filter_by_client_division_site
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSites::Region1::Navigation.create-cascaded_create
And Plinth::Plinth statemachine::Region1::StudyEnvironmentSite::Region1::SES.delete


