/*
 * Copyright © 2017 no and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ProviderContext;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.SayBuilder;

public class DemoNotificationProvider {

    private NotificationProviderService notificationProviderService;
    public void onSessionInitiated(ProviderContext session) {
        this.notificationProviderService=session.getSALService(NotificationProviderService.class);
        notificationProviderService.publish(new SayBuilder().build());
    }

}
