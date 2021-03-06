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

import org.apache.maven.enforcer.rule.api.EnforcerRule;

/**
 * The Class AbstractNonCacheableEnforcerRule. This is to be used by rules
 * that don't need caching... it saves implementing a bunch of methods.
 *
 * @author <a href="mailto:brianf@apache.org">Brian Fox</a>
 * @version $Id: AbstractNonCacheableEnforcerRule.java 805190 2009-08-17 22:30:49Z hboutemy $
 */
public abstract class AbstractNonCacheableEnforcerRule
    extends AbstractStandardEnforcerRule
{

    /*
     * (non-Javadoc)
     *
     * @see org.apache.maven.enforcer.rule.api.EnforcerRule#getCacheId()
     */
    public String getCacheId()
    {
        return "0";
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.maven.enforcer.rule.api.EnforcerRule#isCacheable()
     */
    public boolean isCacheable()
    {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.maven.enforcer.rule.api.EnforcerRule#isResultValid(org.apache.maven.enforcer.rule.api.EnforcerRule)
     */
    public boolean isResultValid( EnforcerRule cachedRule )
    {
        return false;
    }

}
