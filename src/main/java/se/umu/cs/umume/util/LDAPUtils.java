/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package se.umu.cs.umume.util;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.Attribute;

import se.umu.cs.umume.PersonBean;
import java.util.ArrayList;

/**
 *
 * @author anton
 */
public class LDAPUtils {
	private static final String URL = "ldap://ldap.umu.se";

	public static NamingEnumeration<SearchResult> doLDAPSearch(String searchBase, Attributes searchAttrs,
			String[] matchingAttributes) throws NamingException {
		// Setup connection
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
		"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, URL);

		LdapContext context = new InitialLdapContext(env, null);

		// Perform search
		NamingEnumeration<SearchResult> resultEnum = context.search(searchBase, searchAttrs, matchingAttributes);
		return resultEnum;
	}

	public static NamingEnumeration<SearchResult> searchForUid(final String uid) throws NamingException {
		String searchBase = "cn=person,dc=umu,dc=se";
		// Filter (&(objectClass=organizationalUnit)(umuSeAccountNumber=3300))
		// TODO: verify that departments are all of class organizationalUnit
		Attributes searchAttrs = new BasicAttributes("uid", uid);
		// Change to specified number/string
		String[] matchingAttributes = {"*"};//{searchAttrString};

		return doLDAPSearch(searchBase, searchAttrs, matchingAttributes);
	}

	public static List<PersonBean> toPersonBeans(final NamingEnumeration<SearchResult> resultEnum) {
		try {
			List<PersonBean> resultList = new ArrayList<PersonBean>();
			// Print result
			if (!resultEnum.hasMore()) {
				return resultList;
			}

			while (resultEnum.hasMore()) {
				Attributes attrs = resultEnum.next().getAttributes();
				PersonBean person = new PersonBean();

				// Get name
				String givenName = (String) attrs.get("givenName").get();
				// TODO: verify "sn"
				String familyName = (String) attrs.get("sn").get();
				person.setGivenName(givenName);
				person.setFamilyName(familyName);
				
//				private String givenName;
//				private String familyName;
//				private List<String> emails;
//				private String floor;
//				private String street;
//				private String postalCode;
//				private String institution;
//				private String roomNumber;
//				private String phoneNumber;
				
				// Get all mails
				NamingEnumeration<?> mailEnum = attrs.get("mail").getAll();
				if (mailEnum.hasMore()) {
					List<String> emails = new ArrayList<String>();
					while (mailEnum.hasMore()) {
						//sb.append("\t\t" + ea.next() + "\n");
						//System.out.println("\t\tmail: " + mailEnum.next());
						emails.add((String) mailEnum.next());
					}
					person.setEmails(emails);
				}
				resultList.add(person);
			}
			return resultList;
		} catch (NamingException e) {
			// TODO - fix error message
			throw new Error("TODO: fix");
		}
	}

	public static String toString(final NamingEnumeration<SearchResult> resultEnum) {
		try {
			StringBuffer sb = new StringBuffer();
			// Print result
			if (!resultEnum.hasMore()) {
				return "";
			}

			while (resultEnum.hasMore()) {
				Attributes resultAttributes = resultEnum.next().getAttributes();
				sb.append("Has result: " + resultAttributes + "\n");

				// get all attributes from result
				for (NamingEnumeration<? extends Attribute> e = resultAttributes.getAll(); e.hasMore();) {
					Attribute attr = e.next();
					sb.append("\t" + attr.getID() + "\n");

					// Get all duplicate attributes from current attribute
					for (NamingEnumeration<?> ea = attr.getAll(); ea.hasMore();) {
						sb.append("\t\t" + ea.next() + "\n");
					}
				}
			}
			return sb.toString();
		} catch (NamingException e) {
			// TODO - fix error message
			return e.getMessage();
		}
	}
}
