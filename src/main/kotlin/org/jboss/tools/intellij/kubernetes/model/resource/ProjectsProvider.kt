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
package org.jboss.tools.intellij.kubernetes.model.resource

import io.fabric8.kubernetes.api.model.Pod
import io.fabric8.kubernetes.client.Watch
import io.fabric8.kubernetes.client.Watcher
import io.fabric8.kubernetes.client.dsl.Watchable
import io.fabric8.openshift.api.model.Project
import io.fabric8.openshift.client.NamespacedOpenShiftClient
import io.fabric8.openshift.client.OpenShiftClient

class ProjectsProvider(client: NamespacedOpenShiftClient)
    : NonNamespacedResourcesProvider<Project, OpenShiftClient>(client) {

    companion object {
        val KIND = Project::class.java
    }

    override val kind = KIND

    override fun loadAllResources(): List<Project> {
        return client.projects().list().items
    }

    override fun getWatchableResource(): () -> Watchable<Watch, Watcher<Project>>? {
        return { client.projects() as Watchable<Watch, Watcher<Project>>}
    }
}
