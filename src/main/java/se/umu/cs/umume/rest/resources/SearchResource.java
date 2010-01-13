package se.umu.cs.umume.rest.resources;

import java.util.List;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.umu.cs.umume.PersonBean;
import se.umu.cs.umume.util.LDAPUtils;

import com.sun.jersey.api.json.JSONWithPadding;

@Path("/search/{searchString}")
public class SearchResource {
    private static final Logger logger = LoggerFactory.getLogger(SearchResource.class);
    private static @Context UriInfo uriInfo;

    @GET
    @Produces("application/javascript")
    public JSONWithPadding searchForUsers(@PathParam("searchString") String searchString,
            @QueryParam("callback") String callback) {
        logger.info("Search for '{}'", searchString);
        try {
            if (searchString.length() < 3) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            List<PersonBean> result = LDAPUtils.toPersonBeans(LDAPUtils.searchPerson(searchString));
            for (PersonBean pb : result) {
                pb.setResourceRef(UriBuilder.fromUri(uriInfo.getBaseUri()).path(UsersResource.class).build(pb.getUid()));
            }
            return new JSONWithPadding(new GenericEntity<List<PersonBean>>(result) {}, callback);
        } catch (NamingException e) {
            logger.warn("Search Exception: {} for search '{}'", e.getMessage(), searchString);
            Response r = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).type("text/plain").build();
            throw new WebApplicationException(r);
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<PersonBean> searchForUsers(@PathParam("searchString") String searchString) {
        logger.info("Search for '{}'", searchString);
        try {
            if (searchString.length() < 3) {
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
            List<PersonBean> result = LDAPUtils.toPersonBeans(LDAPUtils.searchPerson(searchString));
            for (PersonBean pb : result) {
                pb.setResourceRef(UriBuilder.fromUri(uriInfo.getBaseUri()).path(UsersResource.class).build(pb.getUid()));
            }
            return result;
        } catch (NamingException e) {
            logger.warn("Search Exception: {} for search '{}'", e.getMessage(), searchString);
            Response r = Response.status(Status.BAD_REQUEST).entity(e.getMessage()).type("text/plain").build();
            throw new WebApplicationException(r);
        }
    }
}