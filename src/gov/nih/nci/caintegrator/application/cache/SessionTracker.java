/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.cache;

import gov.nih.nci.caintegrator.application.download.DownloadTask;
import gov.nih.nci.caintegrator.application.download.caarray.CaArrayFileDownloadManager;
import gov.nih.nci.caintegrator.application.zip.ZipItem;
import gov.nih.nci.caintegrator.security.PublicUserPool;
import gov.nih.nci.caintegrator.service.task.Task;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 *	SessionTracker is notified whenever an HttpSession is created or destroyed
 *	it stores this information in a Map for easy access by other interested
 *	classes.  It also is responsible for removing any cache that was established
 *	for a session when that session is destroyed.  It accomplished this by 
 *	calling the BusinessCacheManager.removeSessionCache(String sessionId) method.
 *
 *  At instantiation it creates the CacheTracker which watches the 
 *  BusinessCacheManager for any cache creations or removals.  A CacheCleaner
 *  is also created to use the CacheTracker in determining when a 
 *  sessionCache should be removed. 
 * 
 *  @author BauerD Feb 9, 2005
 * 
 */




public class SessionTracker implements HttpSessionListener {
	// Maintains a reference to all active sessions
	static private SessionMap activeSessions = new SessionMap();
	private static CacheCleaner theCacheCleaner;
	private static CacheTracker theCacheTracker;
	private static Logger logger = Logger.getLogger(SessionTracker.class);
	private static boolean appplicationRunning = false;
	
	public SessionTracker() {
		//Properties loggerProperties = PropertyLoader.loadProperties("log4j.properties");
	    //    PropertyConfigurator.configure(loggerProperties);
		theCacheTracker = new CacheTracker();	
		BusinessCacheManager.getInstance().addCacheListener(theCacheTracker);
		new CacheCleaner(theCacheTracker, this).start();
	}

	public void sessionCreated(HttpSessionEvent evt) {
		activeSessions.put(evt.getSession().getId(), evt.getSession());
		logger.debug("New session added: " + evt.getSession().getId());
		logger.debug("Storing reference to new session: " + evt.getSession().getId());
		logger.debug("Total Active Sessions: " + activeSessions.size());
	}

	public void sessionDestroyed(HttpSessionEvent evt) {
		//cancel incomplete jobs
		Map<String,Future<?>> futureTaskMap = (Map<String,Future<?>>) evt.getSession().getAttribute("FutureTaskMap");
        
	         if(futureTaskMap != null){
	        	 int i = 1;
	        	 for(String key: futureTaskMap.keySet()){
	        		 Future<?> future = futureTaskMap.get(key);
	        		 if(future != null && !future.isDone()){
	        			 System.out.println(i + " cancel: "+ key);
	        			 future.cancel(true);
	        			 i++;	        			 
	        		 }
	        	 }
	         }
    	//delete caArray dowloads
		String dir = (String)evt.getSession().getAttribute(CaArrayFileDownloadManager.ZIP_FILE_PATH);
		if(dir != null){
			Collection<DownloadTask> downloadTasks;
			try {
				downloadTasks = CaArrayFileDownloadManager.getAllSessionDownloads(BusinessCacheManager.getInstance(),evt.getSession().getId());
		    	for(DownloadTask downloadTask: downloadTasks){
		    		String taskId = downloadTask.getTaskId();
			    		String zipFileName = downloadTask.getZipFileName();
			    		if(taskId != null){
			    			String filePath = dir + "/"+ zipFileName;
			    			File file = new File(filePath);
			    			if( file.exists()){
			    				file.delete();
			    			}
					    	if(downloadTask.getListOfZipFileLists() != null){
					    		List<ZipItem> zipItems = downloadTask.getListOfZipFileLists();
								for(ZipItem zi : zipItems){
									zipFileName = zi.getFileName();
									filePath = dir + "/"+ zi.getFileName();
					    			file = new File(filePath);
					    			if( file.exists()){
					    				file.delete();
					    			}
								}
					    	}
		    		}
		    	}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		
		activeSessions.remove(evt.getSession().getId());
		logger.debug("Session Destroyed: " + evt.getSession().getId());
		logger.debug("Removing reference to session: " + evt.getSession().getId());
		logger.debug("Total Active Sessions: " + activeSessions.size());
		//remove the cache for the dead session
		BusinessCacheManager.getInstance().removeSessionCache(evt.getSession().getId());
		
        Collection<Task> allTasks = PresentationCacheManager.getInstance().getAllSessionTasks(evt.getSession().getId());
        BusinessCacheManager.getInstance().removeSessionCacheForTasks(allTasks);
        
        PresentationCacheManager.getInstance().removeSessionCache(evt.getSession().getId());
    	String gpUser = (String)evt.getSession().getAttribute(PublicUserPool.PUBLIC_USER_NAME);
    	PublicUserPool pool = (PublicUserPool)evt.getSession().getAttribute(PublicUserPool.PUBLIC_USER_POOL);
    	
    	if (gpUser != null && pool != null){
			pool.returnPublicUser(gpUser);
    	}
	}

	/**
	 * @return Returns the activeSessions.
	 */
	public static SessionMap getSessions() {
		return activeSessions;
	}

	/**
	 * @return Returns the appplicationRunning.
	 */
	public static boolean isAppplicationRunning() {
		return appplicationRunning;
	}
	/**
	 * @param appplicationRunning The appplicationRunning to set.
	 */
	public static void setAppplicationRunning(boolean appplicationRunning) {
		SessionTracker.appplicationRunning = appplicationRunning;
	}
	
}
