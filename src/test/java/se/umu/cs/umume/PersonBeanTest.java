package se.umu.cs.umume;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.junit.Test;

public class PersonBeanTest {

	@Test
	public void testJaxb() throws JAXBException {
		PersonBean pb = new PersonBean();
		pb.setGivenName("Anton");
		pb.setFamilyName("Johansson");
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
	}
}
