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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.StringUtils;

/**
 * This rule checks that lists of dependencies are not included.
 *
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @version $Id: BannedDependencies.java 1496229 2013-06-24 21:43:56Z rfscholte $
 */
public class BannedDependencies
    extends AbstractBanDependencies
{

    /**
     * Specify the banned dependencies. This can be a list of artifacts in the format <code>groupId[:artifactId][:version]</code>.
     * Any of the sections can be a wildcard by using '*' (ie group:*:1.0) <br>
     * The rule will fail if any dependency matches any exclude, unless it also matches an include rule.
     * 
     * @deprecated the visibility will be reduced to private with the next major version
     * @see {@link #setExcludes(List)}
     * @see {@link #getExcludes()}
     */
    public List<String> excludes = null;

    /**
     * Specify the allowed dependencies. This can be a list of artifacts in the format <code>groupId[:artifactId][:version]</code>.
     * Any of the sections can be a wildcard by using '*' (ie group:*:1.0) <br>
     * Includes override the exclude rules. It is meant to allow wide exclusion rules with wildcards and still allow a
     * smaller set of includes. <br>
     * For example, to ban all xerces except xerces-api -> exclude "xerces", include "xerces:xerces-api"
     * 
     * @deprecated the visibility will be reduced to private with the next major version
     * @see {@link #setIncludes(List)}
     * @see {@link #getIncludes()}
     */
    public List<String> includes = null;


    /**
     * {@inheritDoc}
     */
    protected Set<Artifact> checkDependencies( Set<Artifact> theDependencies, Log log )
        throws EnforcerRuleException
    {
        Set<Artifact> excluded = checkDependencies( theDependencies, excludes );

        // anything specifically included should be removed
        // from the ban list.
        if ( excluded != null )
        {
            Set<Artifact> included = checkDependencies( theDependencies, includes );
            if ( included != null )
            {
                excluded.removeAll( included );
            }
        }
        return excluded;

    }

    /**
     * Checks the set of dependencies against the list of patterns.
     *
     * @param thePatterns the patterns
     * @param dependencies the dependencies
     * @return a set containing artifacts matching one of the patterns or <code>null</code>
     * @throws EnforcerRuleException the enforcer rule exception
     */
    private Set<Artifact> checkDependencies( Set<Artifact> dependencies, List<String> thePatterns )
        throws EnforcerRuleException
    {
        Set<Artifact> foundMatches = null;

        if ( thePatterns != null && thePatterns.size() > 0 )
        {

            for ( String pattern : thePatterns )
            {

                String[] subStrings = pattern.split( ":" );
                subStrings = StringUtils.stripAll( subStrings );

                for ( Artifact artifact : dependencies )
                {
                    if ( compareDependency( subStrings, artifact ) )
                    {
                        // only create if needed
                        if ( foundMatches == null )
                        {
                            foundMatches = new HashSet<Artifact>();
                        }
                        foundMatches.add( artifact );
                    }
                }
            }
        }
        return foundMatches;
    }

    /**
     * Compares the parsed array of substrings against the artifact.
     * The pattern should follow the format "groupId:artifactId:version:type:scope"
     *
     * @param pattern the array of patterns
     * @param artifact the artifact
     * @return <code>true</code> if the artifact matches one of the patterns
     * @throws EnforcerRuleException the enforcer rule exception
     */
    protected boolean compareDependency( String[] pattern, Artifact artifact )
        throws EnforcerRuleException
    {

        boolean result = false;
        if ( pattern.length > 0 )
        {
            result = pattern[0].equals( "*" ) || artifact.getGroupId().equals( pattern[0] );
        }

        if ( result && pattern.length > 1 )
        {
            result = pattern[1].equals( "*" ) || artifact.getArtifactId().equals( pattern[1] );
        }

        if ( result && pattern.length > 2 )
        {
            // short circuit if the versions are exactly the same
            if ( pattern[2].equals( "*" ) || artifact.getVersion().equals( pattern[2] ) )
            {
                result = true;
            }
            else
            {
                try
                {
                    result =
                        AbstractVersionEnforcer.containsVersion( VersionRange.createFromVersionSpec( pattern[2] ),
                                                                 new DefaultArtifactVersion( artifact.getBaseVersion() ) );
                }
                catch ( InvalidVersionSpecificationException e )
                {
                    throw new EnforcerRuleException( "Invalid Version Range: ", e );
                }
            }
        }

        if ( result && pattern.length > 3 )
        {
            String type = artifact.getType();
            if ( type == null || type.equals( "" ) )
            {
                type = "jar";
            }
            result = pattern[3].equals( "*" ) || type.equals( pattern[3] );
        }

        if ( result && pattern.length > 4 )
        {
            String scope = artifact.getScope();
            if ( scope == null || scope.equals( "" ) )
            {
                scope = "compile";
            }
            result = pattern[4].equals( "*" ) || scope.equals( pattern[4] );
        }

        return result;
    }

    /**
     * Gets the excludes.
     *
     * @return the excludes
     */
    public List<String> getExcludes()
    {
        return this.excludes;
    }

    /**
     * Sets the excludes.
     *
     * @param theExcludes the excludes to set
     */
    public void setExcludes( List<String> theExcludes )
    {
        this.excludes = theExcludes;
    }

    /**
     * Gets the includes.
     *
     * @return the includes
     */
    public List<String> getIncludes()
    {
        return this.includes;
    }

    /**
     * Sets the includes.
     *
     * @param theIncludes the includes to set
     */
    public void setIncludes( List<String> theIncludes )
    {
        this.includes = theIncludes;
    }

}
