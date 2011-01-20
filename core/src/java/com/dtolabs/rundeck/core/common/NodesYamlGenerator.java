/*
 * Copyright 2011 DTO Labs, Inc. (http://dtolabs.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/*
* NodesYamlGenerator.java
*
* User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
* Created: Jan 17, 2011 4:47:15 PM
*
*/
package com.dtolabs.rundeck.core.common;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.JavaBeanDumper;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.*;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.representer.Represent;

import java.util.*;
import java.io.*;
import java.beans.IntrospectionException;

/**
 * NodesYamlGenerator produces YAML formatted output from a set of {@link INodeEntry} data.  Nodes should be added
 * with the {@link #addNode(INodeEntry)} method, then {@link #generate()} called.
 *
 * @author Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 */
public class NodesYamlGenerator implements NodesFileGenerator {
    private File destfile;
    private OutputStream outputStream;
    private HashMap<String,INodeEntry> entities;

    /**
     * Serialize nodes data as yaml to a file.
     * @param destfile
     */
    public NodesYamlGenerator(final File destfile) {
        this.destfile = destfile;
        entities = new HashMap<String, INodeEntry>();
    }

    /**
     * Serialize nodes data as yaml to an outputstream.
     * @param destfile
     */
    public NodesYamlGenerator(final OutputStream outputStream) {
        this.outputStream = outputStream;
        entities = new HashMap<String, INodeEntry>();
    }

    public void addNode(final INodeEntry node) {
        entities.put(node.getNodename(), node);

    }

    public void generate() throws IOException, NodesGeneratorException {
        if(null==destfile && null== outputStream) {
            throw new NullPointerException("destfile or outputstream was not set");
        }
        if(null==entities || entities.size()<1) {
            throw new NodesGeneratorException("Node set is empty");
        }
        Writer writer;
        if(null!=destfile){
            writer = new FileWriter(destfile);
        }else {
            writer = new OutputStreamWriter(outputStream);
        }
        final DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        final Representer representer = new MyRepresenter();
        representer.addClassTag(NodeEntryImpl.class, Tag.MAP);
        Yaml yaml = new Yaml(representer, dumperOptions);
        yaml.dump(entities,writer);
    }

    /**
     * Properties of INodeEntry to skip when generating output
     */
    private static HashSet<String> skipProperties=new HashSet<String>();
    static{
        skipProperties.add("attributes");
        skipProperties.add("settings");
        skipProperties.add("type");
        skipProperties.add("frameworkProject");
    }
    /**
     * Properties of INodeEntry to set to empty string if they are null when generating output
     */
    private static HashSet<String> notNullProperties=new HashSet<String>();
    static{
        skipProperties.add("editUrl");
        skipProperties.add("remoteUrl");
    }

    private class MyRepresenter extends Representer {

        @Override
        protected NodeTuple representJavaBeanProperty(Object javaBean, Property property,
                                                      Object propertyValue, Tag customTag) {
            if(skipProperties.contains(property.getName())){
                return null;
            }else if (propertyValue == null) {
                if(notNullProperties.contains(property.getName())){
                    return super.representJavaBeanProperty(javaBean, property, "", Tag.STR);
                }
                return null;
            }else if ("tags".equals(property.getName())) {
                //convert Set of tag strings into comma-separated scalar
                String valueString="";
                if (propertyValue instanceof Set) {
                    Set values = (Set) propertyValue;
                    StringBuffer sb = new StringBuffer();
                    for (final Object tagValue : values) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(tagValue);
                    }
                    valueString = sb.toString();
                }
                return super.representJavaBeanProperty(javaBean, property, valueString , Tag.STR);
            } else {
                return super
                    .representJavaBeanProperty(javaBean, property, propertyValue, customTag);
            }
        }
    }

}
