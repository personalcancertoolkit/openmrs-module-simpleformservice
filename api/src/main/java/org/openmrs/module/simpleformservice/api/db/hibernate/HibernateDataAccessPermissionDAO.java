/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.simpleformservice.api.db.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.module.simpleformservice.DataAccessPermission;
import org.openmrs.module.simpleformservice.api.db.DataAccessPermissionDAO;

import java.util.Date;
import java.util.List;

public class HibernateDataAccessPermissionDAO implements DataAccessPermissionDAO {

    protected final Log log = LogFactory.getLog(getClass());

    private SessionFactory sessionFactory;
    
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public DataAccessPermission getDataAccessPermission(String uuid) {
        final Criteria crit = this.sessionFactory.getCurrentSession().createCriteria(DataAccessPermission.class);
        crit.add(Restrictions.eq("uuid", uuid));
        return (DataAccessPermission) crit.uniqueResult();
    }

    @Override
    public DataAccessPermission saveDataAccessPermission(DataAccessPermission dataAccessPermission) {
        sessionFactory.getCurrentSession().saveOrUpdate(dataAccessPermission);
        return dataAccessPermission;
    }

    @Override
    public void deleteDataAccessPermission(DataAccessPermission dataAccessPermission) {
        sessionFactory.getCurrentSession().delete(dataAccessPermission);
    }

    @Override
    public List<DataAccessPermission> getAllDataAccessPermission() {
        final Criteria crit = this.sessionFactory.getCurrentSession().createCriteria(DataAccessPermission.class);
        crit.addOrder(Order.asc("id"));
        this.log.debug("HibernateDataAccessPermissionDAO:getAllDataAccessPermission->" + " | token count=" + crit.list().size());
        return crit.list();
    }

    @Override
    public List<DataAccessPermission> getDataAccessPermissionByGrantedToPerson(Person grantedToPerson) {
        final Criteria crit = this.sessionFactory.getCurrentSession().createCriteria(DataAccessPermission.class);
        //System.out.println("Current person = " +grantedToPerson);
        crit.add(Restrictions.eq("grantedToPerson", grantedToPerson));
        crit.addOrder(Order.asc("id"));
        final List<DataAccessPermission> list = crit.list();
        this.log.debug("HibernateDataAccessPermissionDAO:getDataAccessPermissionByGrantedToPerson->" + grantedToPerson + " | token count=" + list.size());
        if (list.size() >= 1) {
            return list;
        } else {
            return null;
        }
    }

    @Override
    public DataAccessPermission getDataAccessPermission(Person accessToPerson, Person grantedToPerson, String encounterType, String permissionType){
        //System.out.println("Looking up object in db (now in hibernate object) w/ accessTo:"+ accessToPerson + ", grantedToPerson:"+grantedToPerson+", encounterType:"+encounterType+", and permissionType:"+permissionType);
        Criteria crit = this.sessionFactory.getCurrentSession().createCriteria(DataAccessPermission.class);
        crit.add(Restrictions.eq("accessToPerson", accessToPerson));
        crit.add(Restrictions.eq("grantedToPerson", grantedToPerson));
        crit.add(Restrictions.eq("encounterType", encounterType));
        crit.add(Restrictions.eq("permissionType", permissionType));
        crit.addOrder(Order.desc("id"));

        List<DataAccessPermission> list = crit.list();

        this.log.debug("HibernateDataAccessPermissionDAO:getDataAccessPermission->" + accessToPerson + "|" + grantedToPerson + "|" 
                       + encounterType + "|"  + permissionType + "|token count=" + list.size());

        if (list.size() >= 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

}
