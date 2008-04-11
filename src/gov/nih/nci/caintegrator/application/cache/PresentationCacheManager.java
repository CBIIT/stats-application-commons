package gov.nih.nci.caintegrator.application.cache;

import gov.nih.nci.caintegrator.service.findings.Finding;
import gov.nih.nci.caintegrator.service.task.Task;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.ObjectExistsException;

import org.apache.log4j.Logger;

/**
 * PresentationCacheManager was written to provide a cache written specifically
 * for the Presentation tier.  At the time of writing, methods have been removed
 * from the ConvenientCache interface and added to the PresentationTierCache 
 * interface.  
 *  
 * @author BauerD
 */


/**
* caIntegrator License
* 
* Copyright 2001-2005 Science Applications International Corporation ("SAIC"). 
* The software subject to this notice and license includes both human readable source code form and machine readable, 
* binary, object code form ("the caIntegrator Software"). The caIntegrator Software was developed in conjunction with 
* the National Cancer Institute ("NCI") by NCI employees and employees of SAIC. 
* To the extent government employees are authors, any rights in such works shall be subject to Title 17 of the United States
* Code, section 105. 
* This caIntegrator Software License (the "License") is between NCI and You. "You (or "Your") shall mean a person or an 
* entity, and all other entities that control, are controlled by, or are under common control with the entity. "Control" 
* for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity,
*  whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) 
* beneficial ownership of such entity. 
* This License is granted provided that You agree to the conditions described below. NCI grants You a non-exclusive, 
* worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights 
* in the caIntegrator Software to (i) use, install, access, operate, execute, copy, modify, translate, market, publicly 
* display, publicly perform, and prepare derivative works of the caIntegrator Software; (ii) distribute and have distributed 
* to and by third parties the caIntegrator Software and any modifications and derivative works thereof; 
* and (iii) sublicense the foregoing rights set out in (i) and (ii) to third parties, including the right to license such 
* rights to further third parties. For sake of clarity, and not by way of limitation, NCI shall have no right of accounting
* or right of payment from You or Your sublicensees for the rights granted under this License. This License is granted at no
* charge to You. 
* 1. Your redistributions of the source code for the Software must retain the above copyright notice, this list of conditions
*    and the disclaimer and limitation of liability of Article 6, below. Your redistributions in object code form must reproduce 
*    the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials
*    provided with the distribution, if any. 
* 2. Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: "This 
*    product includes software developed by SAIC and the National Cancer Institute." If You do not include such end-user 
*    documentation, You shall include this acknowledgment in the Software itself, wherever such third-party acknowledgments 
*    normally appear.
* 3. You may not use the names "The National Cancer Institute", "NCI" "Science Applications International Corporation" and 
*    "SAIC" to endorse or promote products derived from this Software. This License does not authorize You to use any 
*    trademarks, service marks, trade names, logos or product names of either NCI or SAIC, except as required to comply with
*    the terms of this License. 
* 4. For sake of clarity, and not by way of limitation, You may incorporate this Software into Your proprietary programs and 
*    into any third party proprietary programs. However, if You incorporate the Software into third party proprietary 
*    programs, You agree that You are solely responsible for obtaining any permission from such third parties required to 
*    incorporate the Software into such third party proprietary programs and for informing Your sublicensees, including 
*    without limitation Your end-users, of their obligation to secure any required permissions from such third parties 
*    before incorporating the Software into such third party proprietary software programs. In the event that You fail 
*    to obtain such permissions, You agree to indemnify NCI for any claims against NCI by such third parties, except to 
*    the extent prohibited by law, resulting from Your failure to obtain such permissions. 
* 5. For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and 
*    to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses 
*    of modifications of the Software, or any derivative works of the Software as a whole, provided Your use, reproduction, 
*    and distribution of the Work otherwise complies with the conditions stated in this License.
* 6. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, 
*    THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. 
*    IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE, SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
*    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE 
*    GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
*    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT 
*    OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
* 
*/

/**
 * @author bauerd, sahnih, landyr
 *
 */
public class PresentationCacheManager implements PresentationTierCache{
	private static final String PRESENTATION_CACHE = "PresentationTierCache";
	//DO NOT change PERSISTED_SESSIONS_CACHE value without modifying ehcache.xml
	private static final String PERSISTED_SESSIONS_CACHE = "persistedSessionsCache";
	protected final String PERSISTED_SUFFIX = "__p"; //flag persisted items with this internal suffix
	private static Logger logger = Logger.getLogger(PresentationCacheManager.class);
	private static PresentationTierCache myInstance;
	static protected CacheManager manager = null;
	static private Cache presentationCache = null;
	static {
	   	try {
     		myInstance = new PresentationCacheManager();
     	   //Create the cacheManager and the application cache
           //as specified in the configurationFile.xml 
     		logger.debug("Getting ehCache manager instance");
         	manager = CacheManager.getInstance();
        	logger.debug("CacheManger available");
        }catch(Throwable t) {
            logger.error("FATAL: Problem creating CacheManager!");
            logger.error(t);
            throw new ExceptionInInitializerError(t);
        }
 	}
	protected PresentationCacheManager() {}
	
	/**
	 * This method is the workhorse of the PresentationCacheManager, almost
	 * every other mehtod makes use of this method to retrieve and create all
	 * session caches for the cache manager.
	 * 
	 * @param sessionId
	 * @param createTempCounter 
	 * @return
	 */
    protected Cache getSessionCache(String sessionId) {
        Cache sessionCache = null;
        /*
         * Process the sessionId to make sure that we have a unique sessionName for
         * the presentation tier cache
         */
        String uniqueSession = processSessionId(sessionId);
        if( manager!=null && !manager.cacheExists(uniqueSession) ) {
        	/**
        	 * Here are the parameters that we are using for creating the presentation
        	 * tier session caches.  These caches are only stored in Memory and 
        	 * never persisted out to disk
        	 * 
        	 *  	CacheName = the sessionId;
        	 *  	Max Elements in Memory = 1000;
        	 *  	Overflow to disk = false;
        	 *  	Make the cache eternal = true;
        	 *  	Elements time to live in seconds = 12000 (200 minutes, this not eternal in case the data changes);
        	 *  	Elements time to idle in seconds = 0 (Special setting which means never check);
         	 */
            sessionCache = new Cache(uniqueSession, 10000, true, true, 0, 0);
            logger.debug("New Presentation SessionCache created: "+sessionId);
            try {
            	manager.addCache(sessionCache);
            	Element counter = new Element(CacheConstants.REPORT_COUNTER, new SessionTempReportCounter());
            	sessionCache.put(counter);
            }catch(ObjectExistsException oee) {
                logger.error("Attempted to create the same session cache twice.");
                logger.error(oee);
            }catch(CacheException ce) {
                logger.error("Attempt to create session cache failed.");
                logger.error(ce);
            }
        }else if(manager!=null){
        	logger.debug("Returning an existing session cache");
        	sessionCache = manager.getCache(uniqueSession);
        }
        return sessionCache;
    }


    /**
     * Will look in the session cache of the sessionId that was passed and 
     * look for a cache element stored under the key that was passed.
     * 
     * @param sessionId
     * @param key
     * @return
     */
    public Object getPersistableObjectFromSessionCache(String sessionId, String key) {
    	Cache sessionCache = getSessionCache(sessionId);
    	Object returnObject = null;
    	try {
			Element element = sessionCache.get(key+this.PERSISTED_SUFFIX);
			if(element != null){
				returnObject = element.getValue();
			}
		} catch (IllegalStateException e) {
			logger.error(e);
		} catch (CacheException e) {
			logger.error(e);
		} catch (NullPointerException e){
            logger.error(e);
        }
    	return returnObject;
    }
 

    /**
     * Will look in the session cache of the sessionId that was passed and 
     * look for a cache element stored under the key that was passed.
     * 
     * @param sessionId
     * @param key
     * @return
     */
    public Object getNonPersistableObjectFromSessionCache(String sessionId, String key) {
    	Cache sessionCache = getSessionCache(sessionId);
    	Object returnObject = null;
    	try {
			Element element = sessionCache.get(key);
			if(element != null){
				returnObject = element.getValue();
			}
		} catch (IllegalStateException e) {
			logger.error(e);
		} catch (CacheException e) {
			logger.error(e);
		} catch (NullPointerException e){
            logger.error(e);
        }
    	return returnObject;
    }
    
    /* (non-Javadoc)
     * @see gov.nih.nci.rembrandt.cache.BusinessTierCache#getAllSessionFindings(java.lang.String)
     */
    public Collection<Task> getAllSessionTasks(String sessionId){
        Collection<Task> tasks = new ArrayList<Task>();
        Cache sessionCache = getSessionCache(sessionId);
        try {
            List keys = sessionCache.getKeys();
            for(Iterator i = keys.iterator();i.hasNext();) {
                Element element = sessionCache.get((String)i.next());
                Object object = element.getValue();
                if(object instanceof Task) {
                    tasks.add((Task)object);
                }
            }
        }catch(CacheException ce) {
            logger.error(ce);
        }
        return tasks;
    }
	

	
	 /**
	  * Returns the singleton instance of the PresentationTierCache
	  * 
	  * @return The singleton instance of the PresentationTierCache
	  */
	 public static PresentationTierCache getInstance() {
		 return myInstance;
	 }
	 
	 /***
	  * Adds the Passed Serializable value object to the "Session Cache" for the
	  * sessionId passed (if it exists) under the lookup key of the passed 
	  * Serializable key
	  * 
	  *  @param The SessionId of the cache that you would like to drop this object in
	  *  @param The key that you would like to store the object under in the cache
	  *  @param The object that you would like to have stored in the cache.
	  *  
	  *  @return void
	  */
	 public void addPersistableToSessionCache(String sessionId, Serializable key, Serializable value) {
			Cache sessionCache = getSessionCache(sessionId);
			Element element = new Element(key+this.PERSISTED_SUFFIX, value);
			try {	
				sessionCache.put(element);
			}catch(IllegalStateException ise) {
				logger.error("Placing object in SessionCache threw IllegalStateException");
				logger.error(ise);
			}catch(IllegalArgumentException iae) {
				logger.error("Placing object in SessionCache threw IllegalArgumentException");
				logger.error(iae);
			}
	}

	 
	 public void addNonPersistableToSessionCache(String sessionId, Serializable key, Serializable value) {
			Cache sessionCache = getSessionCache(sessionId);
			Element element = new Element(key, value);

			try {	
				sessionCache.put(element);
			}catch(IllegalStateException ise) {
				logger.error("Placing object in SessionCache threw IllegalStateException");
				logger.error(ise);
			}catch(IllegalArgumentException iae) {
				logger.error("Placing object in SessionCache threw IllegalArgumentException");
				logger.error(iae);
			}
	}
	 
	/**
	 * The only purpose of this code is to make sure we never have collisions
	 * between the Presentation and BusinessTier session caches since often 
	 * times we are using the same HttpSession Id when identifying a particualar
	 * session.  When the CacheManagers can be seperated we will be able to remove
	 * this. 
	 * 
	 * @param givenSessionId
	 * @return
	 */
	protected String processSessionId(String givenSessionId) {
		String returnedSessionId = givenSessionId+"_presentation";
		return returnedSessionId;
	}
	
	

	/**
	 * Removes the session cache for the sessionId that was passed.  This should
	 * be called whenever a user session times out.  
	 * 
	 * @param  the session that wants to be logged out.
	 * @return
	 */
	public boolean removeSessionCache(String rawSessionId) {
		/*
		 * process the sessionId to make sure that we have a session id
		 * unique to the presentation tier cache
		 */
		String safeSessionId = processSessionId(rawSessionId);
    	if(manager!=null && manager.cacheExists(safeSessionId)) {
    		String[] beforeCaches = manager.getCacheNames();
    		logger.debug("Current Caches");
    		logger.debug("--------------------------------------");
    		for(String name:beforeCaches) {
    			logger.debug("cache: "+name);
    		}
    		logger.debug("--------------------------------------");
    		logger.debug("removing all temp files associated with session");
    		String sessionTempFolder = getSessionTempFolderPath(rawSessionId);
    		/*
    		 * remove the temp folder if the folder exist
    		 */
    		deleteAllFiles(sessionTempFolder);
    		logger.debug("--------------------------------------");
    		manager.removeCache(safeSessionId);
    		logger.debug("Removing Cache: "+safeSessionId);
    		String[] remainingCaches = manager.getCacheNames();
    		logger.debug("Remaining Caches");
    		logger.debug("--------------------------------------");
    		for(String name:remainingCaches) {
    			logger.debug("cache: "+name);
    		}
    		logger.debug("--------------------------------------");
            return true;
        }else {
        	//there was no sessionCache to remove
        	logger.debug("There was no sessionCache for : "+ rawSessionId+" to remove.");
        	return false;
        }
    }
		
	
	public void removeObjectFromPersistableSessionCache(String sessionId, String key)	{
		Cache sessionCache = getSessionCache(sessionId);
		try	{
			sessionCache.remove(key+this.PERSISTED_SUFFIX);
		}
		catch(Exception e){
			logger.debug("Could not remove " + key + " from persistableSessionCache");
		}
	}
	
	public void removeObjectFromNonPersistableSessionCache(String sessionId, String key)	{
		Cache sessionCache = getSessionCache(sessionId);
		try	{
			sessionCache.remove(key);
		}
		catch(Exception e){
			logger.debug("Could not remove " + key + " from nonPersistableSessionCache");
		}
	}
	
	//dont know where something is, so remove it from both..this will be slower
	public void removeObjectFromCache(String sessionId, String key)	{
		Cache sessionCache = getSessionCache(sessionId);
		try	{
			this.removeObjectFromPersistableSessionCache(sessionId, key);
			this.removeObjectFromNonPersistableSessionCache(sessionId, key);
		}
		catch(Exception e){
			logger.debug("Could not remove " + key + " from unknownSessionCache");
		}
	}
	
	//remove all that does not end with our internal flag suffix...aka all that arent marked as persistent
	public void removeNonPersistableSessionCache(String sessionId)	{
		Cache sessionCache = getSessionCache(sessionId);
		try	{
			List keys = sessionCache.getKeys();
			for(Object s : keys)	{
				if(!s.toString().endsWith(this.PERSISTED_SUFFIX))
					sessionCache.remove(s.toString());
			}
		}
		catch(Exception e){
			logger.debug("Could not remove from nonPersistableSessionCache");
		}
	}
	
	//remove all elements with a key that ends in our internal flag suffix
	public void removePersistableSessionCache(String sessionId)	{
		Cache sessionCache = getSessionCache(sessionId);
		try	{
			List keys = sessionCache.getKeys();
			for(Object s : keys)	{
				if(s.toString().endsWith(this.PERSISTED_SUFFIX))
					sessionCache.remove(s.toString());
			}
		}
		catch(Exception e){
			logger.debug("Could not remove from nonPersistableSessionCache");
		}
	}
	
	/* Stores the path name where temporary files such as image files are stored for this session
	 * @see gov.nih.nci.rembrandt.cache.PresentationTierCache#addSessionTempFolderPath(java.lang.String, java.lang.String)
	 */
	public void addSessionTempFolderPath(String sessionId, String sessionTempFolderPath) {
		myInstance.addNonPersistableToSessionCache(sessionId,CacheConstants.SESSION_TEMP_FOLDER_PATH,sessionTempFolderPath);
		
	}
	/* Returns the path name where temporary files such as image files are stored for this session
	 * @see gov.nih.nci.rembrandt.cache.PresentationTierCache#addSessionTempFolderPath(java.lang.String, java.lang.String)
	 */
	public String getSessionTempFolderPath(String sessionId) {
		return (String) myInstance.getNonPersistableObjectFromSessionCache(sessionId,CacheConstants.SESSION_TEMP_FOLDER_PATH);
	}
	/*
	 * remove the temp folder if the folder exist
	 */
	protected void deleteAllFiles(String filePath){
		if(filePath != null){
			File dir = new File(filePath);
			File[] list = dir.listFiles();
			for(File fileToDelete:list){
				fileToDelete.delete();
			}
			dir.delete();
		}
	}
	/**
	 * This method will simply add the key/value pair to the Presentation Tier's Application 
	 * cache making it accessable by anyone in the presentation tier.
	 * 
	 * @param key
	 * @param value
	 */
	public void addToApplicationCache(Serializable key, Serializable value) {
		Cache applicationCache = getApplicationCache();
		try {
			Element element = new Element(key, value);
			applicationCache.put(element);
		}catch(IllegalStateException ise) {
			logger.error("Checking applicationCache threw IllegalStateException");
			logger.error(ise);
		}catch(ClassCastException cce) {
			logger.error("CacheElement was not a Collection");
			logger.error(cce);
		}
	}
	/**
	 * Returns the Cache that is intended to be used to store application scoped
	 * variables 
	 * @return
	 */
	private Cache getApplicationCache() {
        Cache applicationCache = null;
    	if(manager!=null && !manager.cacheExists(PresentationCacheManager.PRESENTATION_CACHE)) {
    		/**
        	 * Here are the parameters that we are using for creating the presentation
        	 * tier Application Cache
        	 *  	CacheName = PRESENTATION_CACHE;
        	 *  	Max Elements in Memory = 100;
        	 *  	Overflow to disk = false;
        	 *  	Make the cache eternal = true;
        	 *  	Elements time to live in seconds = 12000 (200 minutes, this not eternal in case the data changes);
        	 *  	Elements time to idle in seconds = 0 (Special setting which means never check);
         	 */
    		presentationCache = new Cache(PresentationCacheManager.PRESENTATION_CACHE, 100, false, true, 0, 0);
            logger.debug("New ApplicationCache created");
            try {
            	manager.addCache(presentationCache);
            }catch(ObjectExistsException oee) {
                logger.error("ApplicationCache creation failed.");
                logger.error(oee);
            }catch(CacheException ce) {
                logger.error("ApplicationCache creation failed.");
                logger.error(ce);
            }
        }else if(manager!=null){
        	presentationCache = manager.getCache(PresentationCacheManager.PRESENTATION_CACHE);
        }
      
    	return presentationCache;
    }

	
	/**
	 * This should be the only place that the PresentationCache delegates the
	 * work to the _businessTier
	 * 
	 * @return
	 */
	public Collection checkApplicationCache(String lookupType) {
		Cache applicationCache = this.getApplicationCache();
		Collection lookpCollection = null;
		try {
			Element element = applicationCache.get(lookupType);
			if(element!=null) {
				if(element.getValue() instanceof Collection)
					lookpCollection = (Collection)element.getValue();
			}
		}catch(IllegalStateException ise) {
			logger.error("Getting the Lookup Collection  from cache threw IllegalStateException");
			logger.error(ise);
		}catch(CacheException ce) {
			logger.error("Getting the Lookup Collection  from cache threw a new CacheException");
			logger.error(ce);
		}catch(ClassCastException cce) {
			logger.error("CacheElement was not a Lookup Collection ");
			logger.error(cce);
		}
		return lookpCollection;
	}
	
}
