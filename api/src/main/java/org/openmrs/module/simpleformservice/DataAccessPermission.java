/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.simpleformservice;

import org.openmrs.Person;
import java.util.HashSet;
import java.util.Set;
import org.openmrs.BaseOpenmrsObject;


public class DataAccessPermission {
    private Integer id;
    private Person grantedToPerson;
    private Person accessToPerson;
    //private String accessToPersonName; just use `Person.getPersonName()`;
    private String encounterType;
    private String permissionType;

    // constructor
    public DataAccessPermission(){}
    public DataAccessPermission(Person accessToPerson, Person grantedToPerson, String encounterType, String permissionType){
        this.grantedToPerson = grantedToPerson;
        this.accessToPerson = accessToPerson;
        this.encounterType = encounterType;
        this.permissionType = permissionType;
    }
        
    // id
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    // grantedToPerson
    public Person getGrantedToPerson() {
        return grantedToPerson;
    }
    public void setGrantedToPerson(Person grantedToPerson) {
        this.grantedToPerson = grantedToPerson;
    }

    // accessToPerson
    public Person getAccessToPerson() {
        return grantedToPerson;
    }
    public void setAccessToPerson(Person accessToPerson) {
        this.accessToPerson = accessToPerson;
    }
    
    // encounterType
    public String getEncounterType() {
        return encounterType;
    }
    public void setEncounterType(String encounterType) {
        this.encounterType = encounterType;
    }

    // permissionType
    public String getPermissionType() {
        return permissionType;
    }
    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }

}
