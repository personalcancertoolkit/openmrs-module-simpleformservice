/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.simpleformservice.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;


public class ManualPermissionInterfaceController {

    protected final Log log = LogFactory.getLog(getClass());
    protected final String token="REQUEST_PROFILE_PAGE";


    public void controller(PageModel model, PageRequest pageRequest) {

        model.addAttribute("contextUser", Context.getAuthenticatedUser()); // required for 

        model.addAttribute("securitylevel", 0);
        //log.info(PPTLogAppender.appendLog(token, pageRequest.getRequest(), Context.getAuthenticatedUser().getSystemId(), Context.getAuthenticatedUser().getUsername()));

    }
}
