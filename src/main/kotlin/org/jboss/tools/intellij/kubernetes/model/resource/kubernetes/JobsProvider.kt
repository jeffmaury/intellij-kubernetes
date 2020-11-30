/*******************************************************************************
 * Copyright (c) 2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.intellij.kubernetes.model.resource.kubernetes

import io.fabric8.kubernetes.api.model.batch.Job
import io.fabric8.kubernetes.client.BatchAPIGroupClient
import org.jboss.tools.intellij.kubernetes.model.resource.NamespacedResourcesProvider
import org.jboss.tools.intellij.kubernetes.model.resource.ResourceKind
import org.jboss.tools.intellij.kubernetes.model.resource.WatchableAndListable
import java.util.function.Supplier

class JobsProvider(client: BatchAPIGroupClient)
    : NamespacedResourcesProvider<Job, BatchAPIGroupClient>(client) {

    companion object {
        val KIND = ResourceKind.create(Job::class.java)
    }

    override val kind = KIND

    override fun getOperation(namespace: String): Supplier<WatchableAndListable<Job>> {
        return Supplier { client.jobs().inNamespace(namespace) }
    }
}