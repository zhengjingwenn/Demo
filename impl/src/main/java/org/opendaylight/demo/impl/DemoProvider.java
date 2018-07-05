/*
 * Copyright © 2017 no and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataTreeIdentifier;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.RpcRegistration;
import org.opendaylight.controller.sal.binding.api.RpcProviderRegistry;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.Main;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.main.User;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DemoService.class);
    private final DataBroker dataBroker;
    private final RpcProviderRegistry rpcProviderRegistry;
    private RpcRegistration<DemoService> serviceRegistration;
    private NotificationPublishService notificationProvider;
    private ListenerRegistration <DemoListenerImpl> dataTreeChangeListenerRegistration;


    public DemoProvider(DataBroker dataBroker, RpcProviderRegistry rpcProviderRegistry) {
        this.dataBroker = dataBroker;
        this.rpcProviderRegistry = rpcProviderRegistry;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        DemoImpl demoImpl = new DemoImpl(dataBroker,notificationProvider);
        serviceRegistration = rpcProviderRegistry.addRpcImplementation(DemoService.class, demoImpl);
        dataTreeChangeListenerRegistration = dataBroker.registerDataTreeChangeListener(
            new DataTreeIdentifier<User>(LogicalDatastoreType.CONFIGURATION, InstanceIdentifier.<Main>create(Main.class).child(User.class)), new DemoListenerImpl());
        LOG.info("DemoProvider Session Initiated");

    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        serviceRegistration.close();
        LOG.info("DemoProvider Closed");
        if(dataTreeChangeListenerRegistration != null) {
            dataTreeChangeListenerRegistration.close();
        }
    }
}
