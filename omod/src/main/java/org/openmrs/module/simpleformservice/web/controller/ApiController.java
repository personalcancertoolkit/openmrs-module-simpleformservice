/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.simpleformservice.web.controller;

import org.openmrs.Person;
import org.openmrs.Patient;

import org.openmrs.Obs;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.ConceptClass;
import org.openmrs.ConceptDatatype;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;

import org.openmrs.api.ObsService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;

import org.openmrs.api.APIException;

import org.openmrs.util.PrivilegeConstants;
    
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.*;
import java.text.ParseException;
import org.json.*;

import org.openmrs.parameter.EncounterSearchCriteria;
import org.openmrs.parameter.EncounterSearchCriteriaBuilder;


/**
 * Created by Maurya on 08/06/2015.
 */
@Controller
public class ApiController {
    
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    // Get Encounters
    ////////////////////////////////////////////////////////////////////////////
    @RequestMapping( value = "/simpleformservice/api/get_encounters/{encounter_type}")
    @ResponseBody
    public Object getAllRemindersforPatient(@PathVariable( "encounter_type" ) String encounter_type)
    {
        // define the patient as the current user
        Person person = Context.getAuthenticatedUser().getPerson();		
        Patient patient = Context.getPatientService().getPatient(person.getId());
        
        // get this encounter_type object from identifier
        EncounterType encounterType = findOrCreateEncounterTypeByIdentifier(encounter_type);
        
        // get encounters by encounter_type
        List<EncounterType> encounterTypes = Arrays.asList(encounterType);
        EncounterSearchCriteria encounterSearchCriteria = new EncounterSearchCriteriaBuilder()
				.setPatient(patient)
				.setEncounterTypes(encounterTypes)
				.createEncounterSearchCriteria();
        EncounterService encounterService = Context.getEncounterService();
        List<Encounter> encounters = encounterService.getEncounters(encounterSearchCriteria);
        
        // for each encounter, create a "map" containing encounter_id, encounter_datetime, and obs (conceptid, datatype, + value) of each observation
        List<Object> encounters_data = new ArrayList<Object>();
        for (Encounter this_encounter : encounters) {
            Map<String, Object> an_encounter_data = new HashMap<String, Object>();
            System.out.println("For another encounter : " + this_encounter);
            
            // get encounter id 
            an_encounter_data.put("id", this_encounter.getEncounterId());
            
            // get encounter datetime
            Date encounterDateTime = this_encounter.getEncounterDatetime();
            an_encounter_data.put("datetime", encounterDateTime);
            
            // get date in formated string 
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            String formattedEncounterDate = formatter.format(encounterDateTime);
            an_encounter_data.put("datetime_formatted", formattedEncounterDate);
            
            // get observations for encounter
            List<Obs> these_observations = new ArrayList<Obs>(this_encounter.getObs());;
            int listCount = these_observations.size();
            System.out.println("observations for this encounter = " + listCount);
            
            
            List<Object> thses_observations_data = new ArrayList<Object>();
            for(Obs this_observation : these_observations){
                Map<String, String> an_observation_data = new HashMap<String, String>();
                
                // get the concept for this observation
                Concept this_concept = this_observation.getConcept(); 
                
                // set identifier of concept of observation
                String concept_identifier = this_concept.getName().getName();// note, in current implementation we use the name as the identification string
                an_observation_data.put("concept", concept_identifier);
                
                // set HL7 abbreviation of datatype of observation
                String concept_datatype = this_concept.getDatatype().getHl7Abbreviation();
                an_observation_data.put("datatype", concept_datatype);
                
                // set value of observation
                //    allow convinience method to convert data from original datatype to string
                Locale locale = Context.getLocale();
                an_observation_data.put("value", this_observation.getValueAsString(locale));
                
                thses_observations_data.add(new HashMap<String,String>(an_observation_data));
            }
            an_encounter_data.put("observations", thses_observations_data);
            encounters_data.add(new HashMap<String,Object>(an_encounter_data));
        }        
        
        
        System.out.println(encounters_data);
        return encounters_data;
    }
    
    
    
    //////////////////////////////////////////////////////////////////////////
    // Save EncounterType + Observations
    //////////////////////////////////////////////////////////////////////////
    @RequestMapping(value = "/simpleformservice/api/save_encounter", method = RequestMethod.POST)
    @ResponseBody
    public Object saveEncounter(@RequestParam String json)
    {
        // define the patient as the current user
        Person person = Context.getAuthenticatedUser().getPerson();		
        Patient patient = Context.getPatientService().getPatient(person.getId());
        
        // Convert data to json object
        JSONObject data = new JSONObject(json);
        
        // Create a new encounter for the requested encounter_type
        String encounter_type = data.getString("encounter_type");
        System.out.println(encounter_type);
        EncounterType encounterType = findOrCreateEncounterTypeByIdentifier(encounter_type);
        Encounter encounter = new Encounter();
        encounter.setPatient(patient);
        Date date = new Date();
        encounter.setDateCreated(date);
        encounter.setEncounterDatetime(date);
        encounter.setEncounterType(encounterType);
        
        // Append all new observations to encounter
        JSONArray observations = data.getJSONArray("observations");
        for (int i = 0; i < observations.length(); i++) {
            // map json to data
            String concept = observations.getJSONObject(i).getString("concept");
            String value = observations.getJSONObject(i).getString("value");
            String datatype = observations.getJSONObject(i).getString("datatype");
            
            // begin new observation
            Obs o = new Obs();
            
            // set concept of observation
            System.out.println("  -  " + concept);
            Concept conceptObject = findOrCreateConceptForConceptIdentifier(concept, datatype);
            if(conceptObject == null){
                return "ERROR: Concept Datatype Not Defined"; // sends this data to client
            }
            o.setConcept(conceptObject);
            
            // set value of observation
            if(datatype.equals("DT")){
                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date parsedDate = new Date();
                try {
                    parsedDate = formatter.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return "ERROR: Date could not be parsed properly. Are you sure the format is `MM/dd/yyyy`?";
                }
                o.setValueDate(parsedDate);
            }
            if(datatype.equals("NM")){
                o.setValueNumeric(Double.valueOf(value));
            }
            if(datatype.equals("BIT")){
                Boolean booleanValue = null;
                if(value.equals("true")){
                    booleanValue = true;
                } else if (value.equals("false")){
                    booleanValue = false;
                }  else {
                    System.out.println("ERROR - Boolean value was invalid. Received " + value);
                    return "ERROR: boolean value was not properly defined. Not true or false, instead = " + value; // sends this data to client
                }
                o.setValueBoolean(booleanValue);
            }
            
            // add observation to encounter
            encounter.addObs(o); 
        }
        
        // Save new encounter
        //System.out.println("Beginning to save the encounter");
        EncounterService encounterService = Context.getEncounterService();
        encounter = encounterService.saveEncounter(encounter);
        System.out.println("Encounter saved successfully.");
        
        return "SCS";
    }
    
    
    public String mapHL7AbbreviationToConceptName(String datatype_abbreviation){
        // all supported datatypes are defined here
        //  - supported means that we know how to map the value to the observation
        switch (datatype_abbreviation){
            case "DT" : return "Date";
            case "NM" : return "Numeric";
            case "BIT" : return "Boolean";
        }
        return null; // return false if none found
    }
    
    public Concept findOrCreateConceptForConceptIdentifier(String concept_identifier, String datatype_abbreviation){
        // load required service
        ConceptService conceptService = Context.getConceptService();
        
        // Find or Create the encounter by encounter_type_identifier === EncounterType.Name
        Concept thisConcept = new Concept();
        thisConcept = conceptService.getConceptByName(concept_identifier);
        if(thisConcept == null) {
            //System.out.println("Concept does not exist, creating a concept where name  = " + concept_identifier + "."); 
            
            // Create concept name
            String name = concept_identifier;
		    Locale locale = Context.getLocale();
            ConceptName thisConceptName = new ConceptName(name, locale);
            
            // Get concept datatype
            String datatypeName = mapHL7AbbreviationToConceptName(datatype_abbreviation);
            if(datatypeName == null) return null; // datatype is not supported
            ConceptDatatype thisConceptDatatype = conceptService.getConceptDatatypeByName(datatypeName);
            
            // Get MISC concept class
            ConceptClass thisConceptClass = conceptService.getConceptClassByUuid(ConceptClass.MISC_UUID);
            
            // Create concept
            thisConcept = new Concept();
            thisConcept.addName(thisConceptName);
            thisConcept.setShortName(thisConceptName);
            thisConcept.setFullySpecifiedName(thisConceptName);
            thisConcept.setDatatype(thisConceptDatatype);
            thisConcept.setConceptClass(thisConceptClass);
            try {
                Context.addProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
                Context.addProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
                thisConcept = conceptService.saveConcept(thisConcept);
                System.out.println(thisConcept.getConceptId());
            } catch (APIException e) {
                    e.printStackTrace();
            } finally {
                Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_CONCEPTS);
                Context.removeProxyPrivilege(PrivilegeConstants.GET_CONCEPT_ATTRIBUTE_TYPES);
            }
        }
        
        //System.out.println("Successfully returned concept with the desired identifier ");
        return thisConcept;
    }
    
    public EncounterType findOrCreateEncounterTypeByIdentifier(String encounter_type_identifier){
        // load required service
        EncounterService encounterService = Context.getEncounterService();
        
        // Find or Create the encounter by encounter_type_identifier === EncounterType.Name
        EncounterType thisEncounterType = new EncounterType();
        thisEncounterType = encounterService.getEncounterType(encounter_type_identifier);
        if(thisEncounterType == null) {
            //System.out.println("EncounterType does not exist, creating an encounter type for `Name` = " + encounter_type_identifier + "."); 
            String name = encounter_type_identifier;
            String description = "An encountertype created automagically by simpleformservice.";
            thisEncounterType = new EncounterType(name, description);
            try {
                Context.addProxyPrivilege(PrivilegeConstants.MANAGE_ENCOUNTER_TYPES);
                thisEncounterType = encounterService.saveEncounterType(thisEncounterType);
            } catch (APIException e) {
                    e.printStackTrace();
            } finally {
                Context.addProxyPrivilege(PrivilegeConstants.MANAGE_ENCOUNTER_TYPES);
            }
        }
        
        //System.out.println("Successfully returned EncounterType with the desired encounter_type");
        return thisEncounterType;
    }
    
}
