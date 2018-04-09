%{--
  - Copyright 2018 Rundeck, Inc. (http://rundeck.com)
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%

<%--
  Created by IntelliJ IDEA.
  User: greg
  Date: 1/30/18
  Time: 11:18 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="tabpage" content="triggers"/>
    <meta name="layout" content="base"/>
    <title><g:appTitle/> - <g:message code="Task.page.create.title"/></title>
    <asset:stylesheet src="bootstrap-datetimepicker.min.css"/>
    <asset:javascript src="jquery.autocomplete.min.js"/>
    <asset:javascript src="leavePageConfirm.js"/>
    <asset:javascript src="util/yellowfade.js"/>

    <asset:javascript src="task/edit.js"/>
    <g:jsMessages
            code="Generic.label.indexed,form.validation.failure.description,button.action.Delete,button.action.Save,select.noselection.choose.label,page.unsaved.changes,button.title.add.key.value.pair,key.value.key.title,loading.text,job.not.found.with.id.0"/>
    <g:jsMessages id="jobi18n1"
                  code="Node,Node.plural,job.starting.execution,job.scheduling.execution,option.value.required,options.remote.dependency.missing.required,,option.default.button.title,option.default.button.text,option.select.choose.text"/>

    <!--[if (gt IE 8)|!(IE)]><!--> <g:javascript library="ace/ace"/><!--<![endif]-->
    <!--[if (gt IE 8)|!(IE)]><!--> <g:javascript library="ace/ext-language_tools"/><!--<![endif]-->
    <g:javascript>"use strict";

        function getFrameworkProject() {
            return "${project}";
        }
    </g:javascript>

</head>

<body id="trigger_create">

<g:render template="/common/errorFragment"/>
<g:form method="POST"
        useToken="true"
        controller="task"
        action="createPost"
        params="[project: params.project]"
        class="form-horizontal">
    <div class="panel panel-primary"  data-ko-controller="taskEditor">
        <div class="panel-heading">
            <div class="row">
                <div class="col-sm-10">
                    <span class="h4">
                        <g:message code="Task.page.create.title"/>
                    </span>
                </div>

                <div class="col-sm-2 ">
                    <g:link controller="scheduledExecution" action="upload"
                            params="[project: params.project ?: request.project]"
                            class="btn btn-default btn-sm pull-right">
                        <i class="glyphicon glyphicon-upload"></i>
                        <g:message code="upload.definition.button.label"/>
                    </g:link>
                </div>
            </div>
        </div>

        <tmpl:editForm/>

        <div class="panel-footer">
            <div id="panelButtons" data-bind="visible: !formSubmit.value()">
                <g:actionSubmit id="createFormCancelButton"
                                value="${g.message(code: 'cancel')}"
                                class="btn btn-default reset_page_confirm"/>
                <g:submitButton name="Create"
                                value="${g.message(code: 'button.action.Create')}"
                                data-bind="click: formSubmit.toggle"

                                class="cformAllowSave cformAllowSaveOnly btn btn-primary reset_page_confirm"/>

            </div>
            <busy-spinner params="busy: formSubmit.value, css: 'busy-spinner-info'">
                <span class="text-info"><g:message code="loading.creating.task.message"/></span>
            </busy-spinner>
        </div>

    </div>
</g:form>
</body>
</html>
