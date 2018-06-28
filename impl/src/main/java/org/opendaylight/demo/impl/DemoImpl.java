/*
 * Copyright © 2017 no and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import java.util.concurrent.Future;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.demo.rev150105.DemoService;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

public class DemoImpl implements DemoService {
    @Override
    public Future<RpcResult<DemoOutput>> demo(DemoInput input) {
        DemoOutputBuilder demoBuilder = new DemoOutputBuilder();
        demoBuilder.setGreeting("demo " + input.getName());
        return RpcResultBuilder.success(demoBuilder.build()).buildFuture();
    }
}
