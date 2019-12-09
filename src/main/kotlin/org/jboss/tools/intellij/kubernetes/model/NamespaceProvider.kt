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

import io.fabric8.kubernetes.api.model.HasMetadata
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.client.NamespacedKubernetesClient

class NamespaceProvider(private val client: NamespacedKubernetesClient, val namespace: Namespace) {

    private val kindProviders: MutableMap<Class<out HasMetadata>, ResourceKindProvider<out HasMetadata>> = mutableMapOf(
        Pair(PodsProvider.KIND, PodsProvider(client, namespace))
    )

    fun getPods(): List<Pod> {
        return getResources(PodsProvider.KIND)
    }

    fun hasResource(resource: HasMetadata): Boolean {
        if (namespace == resource) {
            return true
        }
        return kindProviders.values.stream()
            .anyMatch{ it.hasResource(resource) }
    }

    fun clear(resource: HasMetadata) {
        kindProviders.values.find { it.hasResource(resource) }
            ?.clear(resource)
    }

    fun clear(kind: Class<HasMetadata>) {
        kindProviders.values.find { kind == it.kind }
            ?.clear()
    }

    fun clear() {
        kindProviders.forEach{ it.value.clear() }
    }

    private fun <T: HasMetadata> getResources(kind: Class<T>): List<T> {
        val provider = kindProviders.values.find { kind == it.kind }
        if (provider?.allResources is List) {
            return provider.allResources as List<T>
        } else {
            return emptyList()
        }
    }
}
