package se.umu.cs.umume.rest.resources;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.umu.cs.umume.Person;
import se.umu.cs.umume.rest.ErrorMessage;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;
import com.sun.jersey.api.json.JSONWithPadding;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

@Provider
public class JsonFormatJaxbContextResolver implements ContextResolver<JAXBContext> {

    private static final Logger logger = LoggerFactory.getLogger(JsonFormatJaxbContextResolver.class);
    private JAXBContext context;
    private Set<Class> types;
    private Class[] ctypes = { ErrorMessage.class, Person.class };

    public JsonFormatJaxbContextResolver() throws JAXBException {
        logger.info("created");
        types = new HashSet<Class>(Arrays.asList(ctypes));
        this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), ctypes);
        //JSONConfiguration.mapped().rootUnwrapping(false).build();    
    }

    @Override
    public JAXBContext getContext(Class<?> objectType) {
        logger.info("getContext {}", objectType);
        return (types.contains(objectType)) ? context : null;
    }
}
