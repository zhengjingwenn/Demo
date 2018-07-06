/*
 * Copyright © 2017 no and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import java.util.concurrent.Future;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.Say;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SubscribeImpl implements DemoListener {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeImpl.class);
    @Override
    public void onSay(Say notification) {
        // TODO Auto-generated method stub
        LOG.info("Get"+notification.getMessage());
    }

}
