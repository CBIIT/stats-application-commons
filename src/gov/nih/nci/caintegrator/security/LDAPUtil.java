/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.security;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;





public class LDAPUtil {

	static public String getEmailAddressForUser(String inUsername) {
        String theSearchFilter = "(" + "cn" + "=" + inUsername + "*)";

        String theEmailAddress = "";
        
        try {
                
            //String theInitialContextFactory = 
            Hashtable<String,String> environment = new Hashtable<String,String>();
            environment.clear();
            environment.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            environment.put(Context.PROVIDER_URL, PROVIDER_URL);
            environment.put(Context.SECURITY_AUTHENTICATION, SECURITY_AUTHENTICATION);
            environment.put(Context.SECURITY_PROTOCOL, SECURITY_PROTOCOL);
            DirContext dirContext = new InitialDirContext(environment);

            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(null);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration searchEnum = dirContext.search(CONTEXT, theSearchFilter, searchControls);
            dirContext.close();
            
            while (searchEnum.hasMore()) {
                
                SearchResult searchResult = (SearchResult) searchEnum.next();
              
                Attributes theAttributes = searchResult.getAttributes();

                Enumeration theEnum = theAttributes.getAll();

                while (theEnum.hasMoreElements()) {
                    Attribute theAttribute = (Attribute) theEnum.nextElement();
                    if (theAttribute.getID().equals("mail"))
                    {
                        theEmailAddress = theAttribute.get().toString();
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("User Name does not exist + " + inUsername);
        }
        
        return theEmailAddress;
    }
    
    public static void main(String[] args) {
        System.out.println("Email address: " + LDAPUtil.getEmailAddressForUser("sahnih"));
    }
    /**
     * These are hard coded here for now while we are testing the Security
     * implementation for Rembrandt/caIntegrator
     */
    private static final String INITIAL_CONTEXT_FACTORY ="com.sun.jndi.ldap.LdapCtxFactory";
    private static final String PROVIDER_URL = "ldaps://ncids2b.nci.nih.gov:636";
    private static final String SECURITY_AUTHENTICATION="simple";
    private static final String SECURITY_PROTOCOL="ssl";
    private static final String CONTEXT="ou=nci,o=nih";
    /**
     * These keys will later be used to retrieve the values hard coded above from
     * a currently undefined properties file.
     * @author BauerD
     *
     */




    public interface Constants {
        public static final String INITIAL_CONTEXT_FACTORY_KEY = "ldap.initial.context.factory";
        public static final String PROVIDER_URL_KEY = "ldap.provider.url";
        public static final String SECURITY_AUTHENTICATION_KEY = "ldap.security.authentication";
        public static final String SECURITY_PROTOCOL_KEY = "ldap.security.protocol";
        public static final String CONTEXT_KEY = "ldap.context";
    }
    
}
