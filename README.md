openmrs-module-simpleformservice
================================

## What is this project?

This is an [OpenMRS](http://openmrs.org/) module. This module requires [OpenMRS Reference Application](http://openmrs.org/download/) version 2.2 and above.

This module handles scraping the information from a UI that a developer has created, passing it to the server, and recording all of the information properly as encounter+observations inside of the OpenMRS database. 

You create the UI, this does the rest.


## Project Goals 
- Create a service that makes creating and submitting forms very simple and fast
- Cleanly seperate client side logic + views from server side logic
- Enable maximum freedom for the developer by imposing minimal html structure requirements
- Automagically take care of managing `concepts` and `encounter_type`s


## Introduction 

Creating and using `simpleform` is... simple - and highly customizable. Simple form takes care of validating your inputs, passing your inputs to the server, and saving your inputs as openmrs encounters+observations on the server. It even has a feature which enables the user to automatically generate all required concepts, which the user can define by any unique string they want.

## Usage
1. Developer creates html containing the form they wish to submit
    - The html has a few rules, all of which are there to help the `simple_form_submission_handler` object generate the `json` which will be sent to the server and saved on the database
    - Rules:
        - the form should be wrapped with a `<simpleform encounter_type = "">...</simpleform>` tag. 
        - all questions should be wrapped with `<simplequestion concept="unique_identifier_string">..</simplequestion>` tag.
            - note that this tag requires a "unique_identifier_string", which it will use when finding the openmrs conceptid related to this string
        - all answers should be wrapped with `<simplequestion> ... <simpleanswer>..</simpleanswer> ... </simplequestion>`
            - note that answers need to be wrapped inside of questions
        - there should be a button which calls the `simple_form_submission_handler.submit_encounter(encounter_type)` method when form is ready to be submitted
            - note that the `simple_form_submission_handler` needs to be loaded onto the page first
2. Developer is finished working. Everything now works.


## Conceptual Overview
### Simple Forms
1. Developer creates html
2. `simple_form_submission_handler` crawls the form, scrapes all of the user inputs w/ concept ids and creates a json that contains all nessesary data to record the encounter and observations
3. server side handler receives json through async request from `simple_form_submission_handler`. handler records all data into database

### Concept and Encounter Type Loading
An important aspect to note is "automagical" handling of the `concept`s and `encounter_type`s defined in `<simpleform>`s. This module creates any concepts or encounter_types that are not already defined in the database. Using the `unique_identifier_string` found in `concept=<unique_identifier_string>`, `encounter_type=<unique_identifier_string>`, this module returns an `encounter type` or `concept` where that `unique_identifier_string` matches the `Name` of the `concept` or `encounter_type`. if it can find one in the database, it will return it; If not, it will create a new `encounter_type`. 

The forgiving nature design needs to be taken into consideration.

## Examples


## Documentation

### Form Tags
#### `<simpleanswer>` attributes
- `type`
    - identifier corresponding to an `answer_handler`, e.g., "boolean"
- `required`
    - defaults to "false"
    - "true" results in `simple_form_submission_handler.submit_encounter` returning `false` if any are w/ this attribute are not filled out
    
    
### Submission Services

#### `simpleformservice.simple_submission`

The purpose of this object is to remove the requirement of creating a custom handler to deal with the `promise` response of the `submission_handler`.
This wrapper enables the user to call `simple_submission.submit_encounter(encounter_type, on_success_function, on_error_message)` and 
- trigger `on_success_function` if the promise resolves
    - or reload the page if  `on_success_function` is undefined
- alert(on_error_message)  
    - or alert("Please ensure all questions are answered and answered correctly.") if on_error_message is undefined

#### `simpleformservice.submission_handler` 
- method `submit_encounter(encounter_type)`
    - submission logic:
        - this method finds the `<simpleform>` with `.find("simpleform[encounter_type='encounter_type']")`
        - it then finds each `<simplequestion>` and `<simpleanswer>` and validates the answers
            - validation is handled by the `<simpleanswer>`'s `answer_handler[encounter_type]`
            - if `simpleanswer.attr(required) == true` and any `<simpleanswer>`s are not answered, then `answer_handler[encounter_type]` throws an error
            - if answer given is invalid, then `answer_handler[encounter_type]` throws an error 
    - when error is thrown by any of the `answer_handler[encounter_type]`s, `submit_encounter` returns a promise resolving with the error data
    - if everything occured successfuly, `submit_encounter` returns a promise which resolves with the server response  
    
            

## How to contribute?
 - Improve module's [documentation](https://github.com/personalcancertoolkit/openmrs-module-simpleformservice/wiki) 
 - Go to [issues](https://github.com/personalcancertoolkit/openmrs-module-simpleformservice/issues) or choose an issue from [projects](https://github.com/personalcancertoolkit/openmrs-module-simpleformservice/projects) and start developing.
