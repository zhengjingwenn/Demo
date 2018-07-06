/*
 * Copyright © 2017 no and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import java.util.Collection;

import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.SayBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.main.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DemoListenerImpl implements DataTreeChangeListener<User>{
    private static final Logger LOG = LoggerFactory.getLogger(DemoListenerImpl.class);
    private static NotificationPublishService publishService;
    
    public void setNotificationPublishService(NotificationPublishService publishService) {
    	this.publishService=publishService;
    }

    @Override
    public void onDataTreeChanged(Collection<DataTreeModification<User>> changes) {
        // TODO Auto-generated method stub
        for(DataTreeModification<User> change: changes) {
            DataObjectModification<User> rootNode = change.getRootNode();
           try {
               publishService.putNotification(new SayBuilder().setMessage(rootNode.getDataAfter().getName()).build());
               LOG.info("publish success");
           } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }

    }


}
