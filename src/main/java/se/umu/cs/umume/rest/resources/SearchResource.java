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

import se.umu.cs.umume.Person;
import se.umu.cs.umume.rest.ErrorMessage;
import se.umu.cs.umume.util.LDAPUtils;

import com.sun.jersey.api.json.JSONWithPadding;

@Path("/search/{searchString}")
public class SearchResource {
    private static final Logger logger = LoggerFactory.getLogger(SearchResource.class);
    private static @Context UriInfo uriInfo;
    private static final String TO_SHORT_SEARCH = "Search string < 3 characters";
    //JSONConfiguration.mapped().rootUnwrapping(false).build();
    
    @GET
    @Produces("application/x-javascript")
    public JSONWithPadding searchForUsers(@PathParam("searchString") String searchString,
            @QueryParam("callback") String callback) {
        logger.info("JAVASCRIPT: Search for '{}'", searchString);
        try {
            if (searchString.length() < 3) {
                ErrorMessage msg = new ErrorMessage(TO_SHORT_SEARCH);
                JSONWithPadding jwp = new JSONWithPadding(new GenericEntity<ErrorMessage>(msg) {}, callback);
                Response r = Response.status(Status.BAD_REQUEST).entity(jwp).type("application/javascript").build();
                throw new WebApplicationException(r);
            }
            List<Person> result = LDAPUtils.toPersonBeans(LDAPUtils.searchPerson(searchString));
            for (Person pb : result) {
                pb.setResourceRef(UriBuilder.fromUri(uriInfo.getBaseUri()).path(UsersResource.class).build(pb.getUid()));
            }
            //return new JSONWithPadding(new GenericEntity<List<PersonBean>>(result) {}, callback);
            return new JSONWithPadding(new GenericEntity<List<Person>>(result) {}, callback);
        } catch (NamingException e) {
            logger.warn("Search Exception: {} for search '{}'", e.getMessage(), searchString);
            ErrorMessage msg = new ErrorMessage(e.getMessage());
            JSONWithPadding jwp = new JSONWithPadding(new GenericEntity<ErrorMessage>(msg) {}, callback);
            Response r = Response.status(Status.BAD_REQUEST).entity(jwp).type("application/javascript").build();
            throw new WebApplicationException(r);
        }
    }
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Person> searchForUsers(@PathParam("searchString") String searchString) {
        logger.info("XML/JSONSearch for '{}'", searchString);
        try {
            if (searchString.length() < 3) {
                ErrorMessage msg = new ErrorMessage(TO_SHORT_SEARCH);
                Response r = Response.status(Status.BAD_REQUEST).entity(msg).build();
                throw new WebApplicationException(r);
            }
            List<Person> result = LDAPUtils.toPersonBeans(LDAPUtils.searchPerson(searchString));
            for (Person pb : result) {
                pb.setResourceRef(UriBuilder.fromUri(uriInfo.getBaseUri()).path(UsersResource.class).build(pb.getUid()));
            }
            return result;
        } catch (NamingException e) {
            logger.warn("Search Exception: {} for search '{}'", e.getMessage(), searchString);
            ErrorMessage msg = new ErrorMessage(e.getMessage());
            Response r = Response.status(Status.BAD_REQUEST).entity(msg).build();
            throw new WebApplicationException(r);
        }
    }
}