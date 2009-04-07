package gov.nih.nci.caintegrator.security;

/**
 * This class uses the loadLists method from list loader to read any text files
 * placed in the dat_files directory of the project. It also loads (as "userLists")
 * all the predefined clinical status groups found in the ISPY study(e.g ER+, ER-, etc)
 * @author rossok
 * @param 
 *
 */

public interface PublicUserPool {
	public static final String PUBLIC_USER_NAME = "publicUserName";
	public static final String PUBLIC_USER_POOL = "publicUserPool";
    public String borrowPublicUser();
    public void returnPublicUser(String user);
}
