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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

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
            if (result.isEmpty()) {
                throw new WebApplicationException(404);
            }
            //PersistanceLayer.addDatabaseInfo(result);
            result.get(0).setTwitterName("javve");
            TwitterUtils.getTweets(result);

            // Should only be one user here
            result.get(0).setResourceRef(uri);
            return result.get(0);
        } catch (NamingException e) {
            throw new WebApplicationException(e, 500);
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public Response updateUser(PersonBean pb) {
        System.err.println("Name: " + pb.getGivenName());
        System.err.println("TwitterName: " + pb.getTwitterName());
        Response r = Response.status(Status.OK).build();
        return r;
    }
}