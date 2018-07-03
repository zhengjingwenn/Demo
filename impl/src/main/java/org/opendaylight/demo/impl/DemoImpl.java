/*
 * Copyright © 2017 no and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.controller.md.sal.binding.api.DataObjectModification;
import org.opendaylight.controller.md.sal.binding.api.DataTreeChangeListener;
import org.opendaylight.controller.md.sal.binding.api.DataTreeModification;
import org.opendaylight.controller.md.sal.binding.api.NotificationPublishService;
import org.opendaylight.controller.md.sal.binding.api.WriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.controller.md.sal.common.api.data.TransactionCommitFailedException;
import org.opendaylight.controller.sal.binding.api.NotificationProviderService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.Main;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.Say;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.SayBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.main.User;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.main.UserBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

public class DemoImpl implements DemoService,DataTreeChangeListener<User>,AutoCloseable  {

    private static final Logger LOG = LoggerFactory.getLogger(DemoImpl.class);
    //private final ExecutorService executor;
    //private final AtomicReference<Future<DemoOutput>> currentDemo = new AtomicReference<>();
    private DataBroker db;
    private NotificationProviderService notificationProvider;
    private NotificationPublishService publishService;
    private String name = "张三";
    public DemoImpl() {
        //executor = Executors.newFixedThreadPool(1);

    }
    @Override
    public Future<RpcResult<DemoOutput>> demo(DemoInput input) {
        DemoOutputBuilder demoBuilder = new DemoOutputBuilder();
        demoBuilder.setGreeting("demo " + input.getName());
        UserBuilder userBuilder=new UserBuilder();
        userBuilder.setName(input.getName());
        User user=userBuilder.build();

        WriteTransaction tx = db.newWriteOnlyTransaction();
        tx.put(LogicalDatastoreType.CONFIGURATION, InstanceIdentifier.<Main>create(Main.class).child(User.class), user);
        Futures.addCallback(tx.submit(), new FutureCallback<Void>() {

            @Override
            public void onSuccess(Void result) {
                // silent is OK.
            }

            @Override
            public void onFailure(Throwable t) {
                LOG.error("Failed to write Config/DS", t);
            }


        });

        System.out.println(user.getName());
        //Say sayNotification = new SayBuilder().setMessage(input.getName().toString());
        return RpcResultBuilder.success(demoBuilder.build()).buildFuture();
    }

    @Override
    public void onDataTreeChanged(Collection<DataTreeModification<User>> changes) {
        // TODO Auto-generated method stub
        for(DataTreeModification<User> change: changes) {
            DataObjectModification<User> rootNode = change.getRootNode();
            if(rootNode.getModificationType() == DataObjectModification.ModificationType.WRITE) {
                User oldUser = rootNode.getDataBefore();
                User newUser = rootNode.getDataAfter();

                LOG.info(
                    "onDataTreeChanged - User config with path {} was added or replaced: old User: {}, new User: {}",
                    change.getRootPath().getRootIdentifier(), oldUser, newUser);
            }else if(rootNode.getModificationType() == DataObjectModification.ModificationType.DELETE) {
                System.out.println("onDataTreeChanged - Toaster config with path {} was deleted: old Toaster: {}"+"分隔1"+
                    change.getRootPath().getRootIdentifier()+"分隔2"+  rootNode.getDataBefore());
            }
            Say sayNotification=new SayBuilder().setMessage(rootNode.getDataAfter().getName()).build();
           notificationProvider.publish(sayNotification);
           try {
            publishService.putNotification(new SayBuilder().setMessage(rootNode.getDataAfter().getName()).build());
           } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        }

    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub
        //executor.shutdown();
    }
}
