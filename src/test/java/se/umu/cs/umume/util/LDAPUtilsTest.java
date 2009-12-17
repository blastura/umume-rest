package se.umu.cs.umume.util;

import static org.junit.Assert.*;

import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;

import org.junit.Test;

public class LDAPUtilsTest {

	@Test
	public void testUidQuery() {
		try {
			NamingEnumeration<SearchResult> result = LDAPUtils.searchForUid("aonjon04");
			System.out.println(LDAPUtils.toString(result));
			assertEquals("aonjon04", result.next().getAttributes().get("uid"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}