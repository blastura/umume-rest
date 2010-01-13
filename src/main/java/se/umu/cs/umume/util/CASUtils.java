package se.umu.cs.umume.util;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.cas.client.validation.Saml11TicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CASUtils {
    private static Logger logger = LoggerFactory.getLogger(CASUtils.class);
    
    public static final boolean validateTicketSAML(String ticket) {
        Saml11TicketValidator sv = new Saml11TicketValidator("https://cas.umu.se/");
        String legacyServerServiceUrl = "http://localhost:8080/UmuMeREST/users/aonjon04";
        try {
            AttributePrincipal principal = null;
            Assertion a = sv.validate(ticket, legacyServerServiceUrl);
            principal = a.getPrincipal();
            System.out.println("user name:" + principal.getName());
        } catch (TicketValidationException e) {
            e.printStackTrace(); // bad style, but only for demonstration purpose.
        }
        return false;
    }

    public static final String validateTicket(String ticket, String service) {
        AttributePrincipal principal = null;
        String casServerUrl = "https://cas.umu.se/";
        Cas20ProxyTicketValidator sv = new Cas20ProxyTicketValidator(casServerUrl);
        sv.setAcceptAnyProxy(true);
        try {
            // there is no need, that the legacy application is accessible
            // through this URL. But for validation purpose, even a non-web-app
            // needs a valid looking URL as identifier.
            //String legacyServerServiceUrl = "http://localhost:8080/UmuMeREST/users/aonjon04";
            Assertion a = sv.validate(ticket, service);
            principal = a.getPrincipal();
            System.out.println("user name:" + principal.getName());
            return principal.getName();
        } catch (TicketValidationException e) {
            logger.info("Not valid ticket: {}", e.getMessage());
        }
        return null;
    }
}
