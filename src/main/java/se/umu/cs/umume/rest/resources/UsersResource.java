/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License("CDDL") (the "License").  You may not use this file
 * except in compliance with the License.
 *
 * You can obtain a copy of the License at:
 *     https://jersey.dev.java.net/license.txt
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * When distributing the Covered Code, include this CDDL Header Notice in each
 * file and include the License file at:
 *     https://jersey.dev.java.net/license.txt
 * If applicable, add the following below this CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 *     "Portions Copyrighted [year] [name of copyright owner]"
 */

package se.umu.cs.umume.rest.resources;

import java.net.URI;
import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import se.umu.cs.umume.PersonBean;
import se.umu.cs.umume.persistance.PersistanceLayer;
import se.umu.cs.umume.util.LDAPUtils;
import se.umu.cs.umume.util.TwitterUtils;

@Path("/users/{uid}")
public class UsersResource {
    @Context UriInfo uriInfo;
    @PathParam("uid") String uid;
    
    // The Java method will process HTTP GET requests
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public PersonBean getUserXML() {
        try {
            URI uri =  uriInfo.getAbsolutePath();
            List<PersonBean> result = LDAPUtils.toPersonBeans(LDAPUtils.searchForUid(uid));
            PersistanceLayer.addDatabaseInfo(result);
            TwitterUtils.getTweets(result);
            if (result.isEmpty()) {
                throw new WebApplicationException(404);
            }
            result.get(0).setResourceRef(uri);
            return result.get(0);
        } catch (NamingException e) {
            throw new WebApplicationException(e, 500);
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void updateUser() {
        System.err.println("PUT PU TPU T");
        
    }
}