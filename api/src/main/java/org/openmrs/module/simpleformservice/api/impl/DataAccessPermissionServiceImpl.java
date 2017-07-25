/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.simpleformservice.api.impl;

import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.simpleformservice.DataAccessPermission;
import org.openmrs.module.simpleformservice.api.DataAccessPermissionService;
import org.openmrs.module.simpleformservice.api.db.DataAccessPermissionDAO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataAccessPermissionServiceImpl extends BaseOpenmrsService implements DataAccessPermissionService {

    DataAccessPermissionDAO dao;

    public void setDao(DataAccessPermissionDAO dao) {
        this.dao = dao;
    }

    @Override
    public DataAccessPermission getDataAccessPermission(String uuid) {
        return dao.getDataAccessPermission(uuid);
    }

    @Override
    public List<DataAccessPermission> getDataAccessPermissionByGrantedToPerson(Person grantedToPerson) {
        return dao.getDataAccessPermissionByGrantedToPerson(grantedToPerson);
    }

    @Override
    public DataAccessPermission saveDataAccessPermission(DataAccessPermission dataAccessPermission) {
        return dao.saveDataAccessPermission(dataAccessPermission);
    }

    @Override
    public void deleteDataAccessPermission(DataAccessPermission dataAccessPermission) {
        dao.deleteDataAccessPermission(dataAccessPermission);
    }
    
    @Override
    public List<DataAccessPermission> getAllDataAccessPermission() {
        return dao.getAllDataAccessPermission();
    }
    
    @Override
    public DataAccessPermission getDataAccessPermission(Person accessToPerson, Person grantedToPerson, String encounterType, String permissionType){
        return dao.getDataAccessPermission(accessToPerson, grantedToPerson, encounterType, permissionType);
    }
  
}
