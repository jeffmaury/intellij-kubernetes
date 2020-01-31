/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.intellij.kubernetes.model

import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.client.NamespacedKubernetesClient

class NamespacesProvider(private val client: NamespacedKubernetesClient)
    : AbstractResourcesProvider<Namespace>(client, null) {

    companion object {
        val KIND = Namespace::class.java;
    }

    override val kind = KIND

    override fun loadAllResources(): List<Namespace> {
        return client.namespaces().list().items
    }
}