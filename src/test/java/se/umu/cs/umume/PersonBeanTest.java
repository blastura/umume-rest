package se.umu.cs.umume;

import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

public class PersonBeanTest {

    @Test
    public void testJaxb() throws JAXBException {
        PersonBean pb = new PersonBean();
        pb.setGivenName("Anton");
        pb.setFamilyName("Johansson");
        pb.setInstitution("TFE");
        List<String> emails = new ArrayList<String>();
        emails.add("aonjon04@student.umu.se");
        emails.add("anton@student.umu.se");
        pb.setEmails(emails);

        JAXBContext jaxbContext = JAXBContext.newInstance(PersonBean.class);

        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter writer = new StringWriter();

        m.marshal(pb, writer);
        System.out.println("======= Marshalling =============");
        System.out.println(writer);

        System.out.println("======= Unmarshalling =============");
        Unmarshaller um = jaxbContext.createUnmarshaller();
        Reader reader = new StringReader(writer.toString());
        PersonBean umPb = (PersonBean) um.unmarshal(reader);
        assertEquals(pb.getEmails().get(0), umPb.getEmails().get(0));
        assertEquals(pb.getEmails().get(1), umPb.getEmails().get(1));
        assertEquals(pb, umPb);
    }
}
