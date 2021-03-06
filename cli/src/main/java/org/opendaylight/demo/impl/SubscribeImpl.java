/*
 * Copyright (c) 2018 UTStarcom, Inc. and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.DemoListener;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev180706.Say;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscribeImpl implements DemoListener {

    private static final Logger LOG = LoggerFactory.getLogger(SubscribeImpl.class);

    @Override
    public void onSay(final Say notification) {
        LOG.info("Get {}", notification.getMessage());
        LOG.info("Get {}", notification.getMessage());
    }

}
