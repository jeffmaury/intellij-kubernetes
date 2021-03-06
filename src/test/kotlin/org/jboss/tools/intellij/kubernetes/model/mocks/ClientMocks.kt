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
package org.jboss.tools.intellij.kubernetes.model.mocks

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.fabric8.kubernetes.api.model.DoneablePod
import io.fabric8.kubernetes.api.model.Namespace
import io.fabric8.kubernetes.api.model.NamespaceList
import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.api.model.PodList
import io.fabric8.kubernetes.client.Config
import io.fabric8.kubernetes.client.NamespacedKubernetesClient
import io.fabric8.kubernetes.client.dsl.MixedOperation
import io.fabric8.kubernetes.client.dsl.Namespaceable
import io.fabric8.kubernetes.client.dsl.NonNamespaceOperation
import io.fabric8.kubernetes.client.dsl.PodResource
import org.jboss.tools.intellij.kubernetes.model.NamespaceListOperation
import org.jboss.tools.intellij.kubernetes.model.mocks.Mocks.resource
import org.mockito.ArgumentMatchers

object ClientMocks {

    val NAMESPACE1 = resource<Namespace>("namespace1")
    val NAMESPACE2 = resource<Namespace>("namespace2")
    val NAMESPACE3 = resource<Namespace>("namespace3")

    val POD1 = resource<Pod>("pod1")
    val POD2 = resource<Pod>("pod2")
    val POD3 = resource<Pod>("pod3")

    fun client(currentNamespace: String?, namespaces: Array<Namespace>): NamespacedKubernetesClient {
        val namespacesMock = namespaceListOperation(namespaces)
        val config = mock<Config>() {
            on { namespace } doReturn currentNamespace
        }

        return mock<NamespacedKubernetesClient> {
            on { namespaces() } doReturn namespacesMock
            on { namespace } doReturn currentNamespace
            on { configuration } doReturn config
        }
    }

    private fun namespaceListOperation(namespaces: Array<Namespace>): NamespaceListOperation {
        val namespaceList = mock<NamespaceList> {
            on { items } doReturn namespaces.asList()
        }
        return mock {
            on { list() } doReturn namespaceList
        }
    }

    fun inNamespace(client: NamespacedKubernetesClient): NamespacedKubernetesClient {
        val namespaceClient = mock<NamespacedKubernetesClient>()
        whenever(client.inNamespace(ArgumentMatchers.anyString()))
            .doReturn(namespaceClient)
        return namespaceClient
    }

    fun inNamespace(mixedOp: MixedOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>>)
            : NonNamespaceOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>> {
        val nonNamespaceOperation: NonNamespaceOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>>
                = mock()
        whenever(mixedOp.inNamespace(ArgumentMatchers.anyString()))
            .doReturn(nonNamespaceOperation)
        return nonNamespaceOperation
    }

    fun pods(client: NamespacedKubernetesClient)
            : MixedOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>> {
        val podsOp = mock<MixedOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>>>()
        whenever(client.pods())
            .doReturn(podsOp)
        return podsOp
    }

    fun list(nonNamespaceOperation: NonNamespaceOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>>)
            : PodList {
        val podList = mock<PodList>()
        whenever(nonNamespaceOperation.list())
            .doReturn(podList)
        return podList

    }

    fun list(mixedOp: MixedOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>>): PodList {
        val podList = mock<PodList>()
        whenever(mixedOp.list())
            .doReturn(podList)
        return podList
    }

    fun items(podList: PodList, vararg pods: Pod ) {
        val returnedPods = listOf(*pods)
        whenever(podList.items)
            .doReturn(returnedPods)
    }

    fun withName(mixedOp: MixedOperation<Pod, PodList, DoneablePod, PodResource<Pod, DoneablePod>>, pod: Pod) {
        val podResource = mock<PodResource<Pod, DoneablePod>>()
        whenever(podResource.get())
            .doReturn(pod)
        whenever(mixedOp.withName(pod.metadata.name))
            .doReturn(podResource)
    }
}