/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.rest.action.search.template;

import org.elasticsearch.action.search.template.SearchTemplateAction;
import org.elasticsearch.action.search.template.SearchTemplateRequest;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.action.support.RestActions;
import org.elasticsearch.rest.action.support.RestToXContentListener;
import org.elasticsearch.script.ScriptService;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;

public class RestRenderSearchTemplateAction extends BaseRestHandler {

    @Inject
    public RestRenderSearchTemplateAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(GET, "/_render/template", this);
        controller.registerHandler(POST, "/_render/template", this);
        controller.registerHandler(GET, "/_render/template/{id}", this);
        controller.registerHandler(POST, "/_render/template/{id}", this);
    }

    @Override
    public void handleRequest(RestRequest request, RestChannel channel, NodeClient client) throws Exception {
        // Creates the render template request
        SearchTemplateRequest renderRequest = RestSearchTemplateAction.parse(RestActions.getRestContent(request));
        renderRequest.setSimulate(true);

        String id = request.param("id");
        if (id != null) {
            renderRequest.setScriptType(ScriptService.ScriptType.STORED);
            renderRequest.setScript(id);
        }

        client.execute(SearchTemplateAction.INSTANCE, renderRequest, new RestToXContentListener<>(channel));
    }
}
