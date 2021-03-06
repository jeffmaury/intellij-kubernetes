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
package org.jboss.tools.intellij.kubernetes.model.cluster

import io.fabric8.kubernetes.api.model.HasMetadata
import io.fabric8.openshift.api.model.Project
import io.fabric8.openshift.client.NamespacedOpenShiftClient
import org.jboss.tools.intellij.kubernetes.model.IModelChangeObservable
import org.jboss.tools.intellij.kubernetes.model.resource.IResourcesProvider
import org.jboss.tools.intellij.kubernetes.model.resource.NamespacesProvider
import org.jboss.tools.intellij.kubernetes.model.resource.PodsProvider
import org.jboss.tools.intellij.kubernetes.model.resource.ProjectsProvider

open class OpenShiftCluster(
    modelChange: IModelChangeObservable,
    client: NamespacedOpenShiftClient
) : AbstractCluster<Project, NamespacedOpenShiftClient>(modelChange, client) {

    override val resourceProviders: Map<Class<out HasMetadata>, IResourcesProvider<out HasMetadata>> =
        mapOf(
            Pair(NamespacesProvider.KIND,
                NamespacesProvider(client)),
            Pair(PodsProvider.KIND,
                PodsProvider(client, namespace)),
            Pair(ProjectsProvider.KIND,
                ProjectsProvider(client))
        )

    override fun getNamespaces(): Collection<Project> {
        return getResources(ProjectsProvider.KIND)
    }
}
