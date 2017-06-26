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


import org.apache.commons.logging.Log; 
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.ModuleActivator;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class SimpleFormServiceModuleActivator extends BaseModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
		

	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	@Override
	public void contextRefreshed() {
		super.contextRefreshed();    //To change body of overridden methods use File | Settings | File Templates.
		ensureRolesAreCreated();
	}

	private void ensureRolesAreCreated() {
		UserService userService = Context.getUserService();
		Role modulebasicrole = userService.getRole(SimpleFormServiceConstants.APP_VIEW_PRIVILEGE_ROLE);
		if (modulebasicrole == null) {
			modulebasicrole = new Role();
			modulebasicrole.setRole(SimpleFormServiceConstants.APP_VIEW_PRIVILEGE_ROLE);
			modulebasicrole.setDescription(SimpleFormServiceConstants.APP_VIEW_PRIVILEGE_ROLE_DESCRIPTION);
			modulebasicrole.addPrivilege(userService.getPrivilege(SimpleFormServiceConstants.APP_VIEW_PRIVILEGE));
			userService.saveRole(modulebasicrole);
		}
		userService.saveRole(modulebasicrole);
	}
}
