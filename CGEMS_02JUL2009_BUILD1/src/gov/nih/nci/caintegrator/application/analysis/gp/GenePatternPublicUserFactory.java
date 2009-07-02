package gov.nih.nci.caintegrator.application.analysis.gp;

import org.apache.commons.pool.BasePoolableObjectFactory;


/**
 * This class uses the loadLists method from list loader to read any text files
 * placed in the dat_files directory of the project. It also loads (as "userLists")
 * all the predefined clinical status groups found in the ISPY study(e.g ER+, ER-, etc)
 * @author rossok
 * @param 
 *
 */

public class GenePatternPublicUserFactory extends BasePoolableObjectFactory {
	private int count = 1;
	private String publicUserName = "";
	
	public GenePatternPublicUserFactory(){
		publicUserName = System.getProperty("gov.nih.nci.caintegrator.gp.publicuser.name");
	}
    public Object makeObject() throws Exception {
    	return publicUserName + count++;
    }
    
    /**
     * Uninitialize an instance to be returned to the pool.
     * @param obj the instance to be passivated
     */
    public void passivateObject(Object obj) throws Exception {
    }
    
    public void setPublicUserName(String user){
    	publicUserName = user;
    }
}
