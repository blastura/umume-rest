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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.umu.cs.umume.PersonBean;
import se.umu.cs.umume.persistance.PersistanceLayer;
import se.umu.cs.umume.util.CASUtils;
import se.umu.cs.umume.util.LDAPUtils;
import se.umu.cs.umume.util.TwitterUtils;

@Path("/users/{uid}")
public class UsersResource {
    private static final Logger logger = LoggerFactory.getLogger(SearchResource.class);
    private @Context UriInfo uriInfo;
    
    // The Java method will process HTTP GET requests
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public PersonBean getUserXML(@PathParam("uid") String uid) {
        try {
            URI uri =  uriInfo.getAbsolutePath();
            List<PersonBean> result = LDAPUtils.toPersonBeans(LDAPUtils.searchForUid(uid));
            if (result.isEmpty()) {
                throw new WebApplicationException(404);
            }
            PersistanceLayer.addDatabaseInfo(result);
            //PersistanceLayer.testURI();
            //result.get(0).setTwitterName("javve");
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
    public Response updateUser(@PathParam("uid") String uid,
            PersonBean pb,
            @QueryParam("ticket") String ticket,
            @QueryParam("service") String service) {
        logger.info("Got PUT with ticket {} and service {}", ticket, service);
        String validUserName = CASUtils.validateTicket(ticket, service);
        if (validUserName == null) {
            logger.warn("Unauthorized attemt to update uid '{}' at service '{}'", uid, service);
            throw new WebApplicationException(Status.UNAUTHORIZED);
        };
        logger.info("UPDATE: Valid user: " + validUserName);
        logger.info("UPDATE: TwitterName: " + pb.getTwitterName());
        logger.info("UPDATE: desc: " + pb.getDescription());
        logger.info("UPDATE: long: " + pb.getLongitude());
        logger.info("UPDATE: lat: " + pb.getLatitude());
        pb.setUid(validUserName);
        PersistanceLayer.updateInfo(pb);
        Response r = Response.status(Status.OK).build();
        return r;
    }
}