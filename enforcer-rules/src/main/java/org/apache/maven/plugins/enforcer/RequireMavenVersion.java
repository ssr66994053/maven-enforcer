package org.apache.maven.plugins.enforcer;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.execution.RuntimeInformation;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

/**
 * This rule checks that the Maven version is allowed.
 *
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @version $Id: RequireMavenVersion.java 1493575 2013-06-16 19:23:51Z rfscholte $
 */
public class RequireMavenVersion
    extends AbstractVersionEnforcer
{

    /*
     * (non-Javadoc)
     *
     * @see org.apache.maven.enforcer.rule.api.EnforcerRule#execute(org.apache.maven.enforcer.rule.api.EnforcerRuleHelper)
     */
    public void execute( EnforcerRuleHelper helper )
        throws EnforcerRuleException
    {
        try
        {
            RuntimeInformation rti = (RuntimeInformation) helper.getComponent( RuntimeInformation.class );
            ArtifactVersion detectedMavenVersion = rti.getApplicationVersion();
            helper.getLog().debug( "Detected Maven Version: " + detectedMavenVersion );
            enforceVersion( helper.getLog(), "Maven", getVersion(), detectedMavenVersion );
        }
        catch ( ComponentLookupException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
