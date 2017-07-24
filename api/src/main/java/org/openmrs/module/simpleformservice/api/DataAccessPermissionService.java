/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.simpleformservice.api;

import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.simpleformservice.DataAccessPermission;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface DataAccessPermissionService extends OpenmrsService {

    @Transactional(readOnly = true)
    DataAccessPermission getDataAccessPermission(String uuid);
    
    @Transactional(readOnly = true)
    List<DataAccessPermission> getDataAccessPermissionByGrantedToPerson(Person grantedToPerson, Boolean appendAccessToPersonNames);

    @Transactional(readOnly = false)
    DataAccessPermission saveDataAccessPermission(DataAccessPermission dataAccessPermission);

    @Transactional(readOnly = false)
    void deleteDataAccessPermission(DataAccessPermission dataAccessPermission);
    
    @Transactional(readOnly = true)
    List<DataAccessPermission> getAllDataAccessPermission();

    
    @Transactional(readOnly = true)
    DataAccessPermission getDataAccessPermission(Person accessToPerson, Person grantedToPerson, String encounterType, String permissionType);
}
