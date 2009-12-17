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

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import se.umu.cs.umume.util.LDAPUtils;
import javax.naming.NamingException;
import java.net.URI;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/users/{uid}")
public class UsersResource {

    // The Java method will process HTTP GET requests
    @GET
    @Produces("text/plain;charset=UTF-8")
    public String getUser(@Context UriInfo uriInfo,
                          @PathParam("uid") String uid) {
        try {
            URI uri =  uriInfo.getAbsolutePath();
            String result = LDAPUtils.toString(LDAPUtils.searchForUid(uid));
            if (result.isEmpty()) {
                throw new WebApplicationException(404);
            }
            return "URI: " + uri + "\n" + result;
        } catch (NamingException e) {
            throw new WebApplicationException(e, 500);
        }
    }
}