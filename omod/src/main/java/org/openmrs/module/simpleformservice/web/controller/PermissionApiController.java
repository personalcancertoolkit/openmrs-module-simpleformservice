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
import org.openmrs.api.PersonService;

import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.simpleformservice.DataAccessPermission;
import org.openmrs.module.simpleformservice.api.DataAccessPermissionService;
    
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.*;
import java.text.ParseException;
import org.json.*;


@Controller
public class PermissionApiController {
    
    //////////////////////////////////////////////////////////////////////////
    // Create dataAccessPermission
    //////////////////////////////////////////////////////////////////////////
    /*
        expects to receive a json w/ keys "granted_to_person_uuid" and "encounter_type" and "permission_type"
    */
    @RequestMapping(value = "/simpleformservice/api/give_data_access", method = RequestMethod.POST)
    @ResponseBody
    public Object createDataAccessPermission(@RequestParam String json)
    {
        // the current user is giving access to another user. Define the current user as the accessToPerson.
        //      Bug #10, To get person reliably, get patient first and then cast to person
        Person accessToPerson = Context.getAuthenticatedUser().getPerson();		
        
        
        
        //////////////////////
        // Parse the JSON 
        /////////////////////
        System.out.println("Parsing JSON...");
        String grantedToPersonUuid = null;
        String encounterType = null;
        String permissionType = null;
        try {
            // Convert data to json object
            JSONObject data = new JSONObject(json);

            // Get the id of the person to which to grant access
            grantedToPersonUuid = data.getString("granted_to_person_uuid");

            // get encounterType
            encounterType = data.getString("encounter_type");

            // get permissionType
            permissionType = data.getString("permission_type");
        } catch (JSONException e) {
            e.printStackTrace();
            String error = "ERROR: Error durring parsing json.";
            System.out.println(error);
            return error;
        }
        
        //////////////////////
        // Grab grantedToUser
        /////////////////////
        System.out.println("Grabbing user to grant...");
        Person grantedToPerson = null;
        try {
            grantedToPerson = Context.getPersonService().getPersonByUuid(grantedToPersonUuid);
        } catch (APIException e) {
            e.printStackTrace();
            String error = "ERROR: Api Error while finding person";  
            System.out.println(error);
            return error;
        }
        if(grantedToPerson == null){
            String error = "ERROR: Person could not be found";  
            System.out.println(error);
            return error;
        };
        //System.out.println("Granted to person found successfully.");
        //System.out.println(grantedToPerson);
        
        
        ///////////////////////
        // Ensure grantedToPerson and accessToPerson are different
        ///////////////////////
        if(grantedToPerson.getUuid().equals(accessToPerson.getUuid())){
            String error = "ERROR: GrantedToPerson and AccessToPerson (current user) are the same user";  
            System.out.println(error);
            return error;
        }
        
        
        //////////////////////
        // Create permission
        //////////////////////
        System.out.println("Starting to create permission...");
        DataAccessPermissionService permissionService = Context.getService(DataAccessPermissionService.class);
        
        // check to make sure the permission does not already exist
        System.out.println("Try to find an already existing permission...");
        DataAccessPermission thePermission = permissionService.getDataAccessPermission(accessToPerson, grantedToPerson, encounterType, permissionType);
        if(thePermission != null){
            System.out.println("Permission already exists");
            return "SCS||Permission already exists.";
        }
        System.out.println("Permission does not already exist...");
        
        // if here, then we need to create the new permission
        System.out.println("Attempting to create permision now...");
        DataAccessPermission newPermission = new DataAccessPermission(accessToPerson, grantedToPerson, encounterType, permissionType);
        System.out.println("Permission created successfully... :");
        System.out.println(newPermission);
            
        // and save it
        System.out.println("Saving new permission...");
        thePermission = permissionService.saveDataAccessPermission(newPermission);
        System.out.println("New permission has been successfully created:");
        System.out.println(thePermission);
        
        return "SCS||Permission created.";
    }
}
