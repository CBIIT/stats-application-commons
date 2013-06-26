/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.download.carray23;
import gov.nih.nci.caarray.domain.file.FileType;
import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.services.ServerConnectionException;
import gov.nih.nci.caarray.services.external.v1_0.CaArrayServer;
import gov.nih.nci.caarray.services.external.v1_0.data.DataApiUtils;
import gov.nih.nci.caarray.services.external.v1_0.data.DataService;
import gov.nih.nci.caarray.services.external.v1_0.data.JavaDataApiUtils;
import gov.nih.nci.caarray.services.external.v1_0.search.JavaSearchApiUtils;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchApiUtils;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator.application.cache.BusinessTierCache;
import gov.nih.nci.caintegrator.application.download.DownloadStatus;
import gov.nih.nci.caintegrator.application.download.DownloadTask;
import gov.nih.nci.caintegrator.application.download.DownloadZipItemImpl;
import gov.nih.nci.caintegrator.application.download.caarray.CaArrayFileDownloadManagerInterface;
import gov.nih.nci.caintegrator.application.zip.ZipItem;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.security.auth.login.LoginException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

public abstract class CaArrayFileDownloadManager implements CaArrayFileDownloadManagerInterface{
	public static Logger logger = Logger
			.getLogger(CaArrayFileDownloadManager.class);
	protected static CaArrayFileDownloadManager instance = null;
	//public static String ZIP_FILE_PATH = "ZipFilePath";
    /**
     * Cache manager which the ApplicationFinding result will be put into
     */ 
    protected BusinessTierCache businessCacheManager;
	/**
	 * URL to caArray server.
	 */
	private URL caarrayUrl;
	/**
	 * username.
	 */
	private String username;
	/**
	 * password.
	 */
	private String password;
	/**
	 * caArray importer.
	 */
	protected CaArrayFileDownloader importer = null;
	/**
	 * CaArrayServer caArrayServer.
	 */
	protected CaArrayServer server = null;
    protected ThreadPoolExecutor taskExecutor;
	private String outputZipDirectory;

	protected CaArrayFileDownloadManager(String caarrayUrl,
			String username, String password,
			String inputDirectory, String outputZipDirectory, String directoryInZip) 
			throws MalformedURLException, LoginException, ServerConnectionException {
		try {
			this.caarrayUrl = new URL(caarrayUrl);
		} catch (MalformedURLException e) {
			reportError("URL was invalid: " + caarrayUrl, e);
			throw e;
		}
		if (username != null)
			this.username = username.trim();
		if (password != null)
			this.password = password.trim();
		if (inputDirectory != null)
			inputDirectory.trim();
		if (outputZipDirectory != null)
			this.outputZipDirectory = outputZipDirectory.trim();
		//try {
			try {
				connectToCaArrayServer();
			} catch (LoginException e) {
					reportError(e.getMessage(), e);
					e.printStackTrace(System.err);
					throw e;
			} catch (ServerConnectionException e) {
					reportError(e.getMessage(), e);
					e.printStackTrace(System.err);
					throw e;
			}
			importer = new CaArrayFileDownloader(inputDirectory,outputZipDirectory,directoryInZip);

	}

	private DownloadTask createTask(String session, String task,
			String zipFileName, List<String> specimenList, FileType type) {
		DownloadTask downloadTask = new DownloadTask(session, task, zipFileName, type, specimenList);
       	return downloadTask;
	}
	   /**
     * The executeStrategy method is what performs the heavy lifting in the
     * strategy.  It will get the results from the QueryHandler back, 
     * and place the "domain findings" into a taskResult.  
     * It then places the taskResult finding into the cache. Runs the
     * execute method in a separate thread for asynchronous operations.     *
     */
    public Future<?> executeDownloadStrategy(final String session, final String taskId,String zipFileName, List<String> specimenList, FileType type, final String experimentName) {   
    	DownloadTask downloadTask = createTask(session,  taskId, zipFileName,  specimenList,  type);
    	
        if(downloadTask == null ){
        	throw new IllegalStateException("Please call createDownloadTask first to init");
        }
        if(importer == null ){
        	throw new NullPointerException("CaArrayFileDownloader cannot be null");
        }
        //set start time
		long startTime = System.currentTimeMillis();
		downloadTask.setStartTime(startTime);
		updateDownloadTaskInCache(downloadTask.getCacheId(),
    			 downloadTask.getTaskId(), downloadTask);
         logger.info("Task has been set to 'InitiatDownload' and placed in cache");
        
         Callable task = new Callable() {
            public DownloadTask call() {
            	DownloadTask downloadTask = null;
            	try {
            		downloadTask = downloadFiles(session,  taskId, experimentName);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				return downloadTask;
            }
        };
        Future<?> future = taskExecutor.submit(task);
        return future;
    }
    
	/**
	 * Tries to find an experiment with a publicIdentifier
	 * equal to the experimentName.  If it doesn't find one with a matching public
	 * identifier, then it tries to find one with title equal to the experimentName.
	 * If it doesn't find one, throws exception.  Otherwise, tries to download
	 * data files from the experiment that it found.
	 * @throws Exception 
	 * @throws LoginException
	 */
	private DownloadTask downloadFiles(String sessionId, String taskId, String experimentName) throws Exception {
		long startTime = 0;
        long endTime = 0;
        double totalTime = 0;
        List<String> listOfZipFiles = null;
        DownloadTask downloadTask = getSessionDownload(sessionId, taskId);
        if(downloadTask == null){
        	throw new IllegalStateException();
        }
        try{
        // Connect to server.
        startTime = System.currentTimeMillis();
        long totalStartTime = startTime;
	
        SearchService searchService = server.getSearchService();
        DataService dataService = server.getDataService();
        SearchApiUtils searchServiceHelper = new JavaSearchApiUtils(searchService);
        DataApiUtils dataServiceHelper = new JavaDataApiUtils(dataService);
        
//		businessCacheManager.addToSessionCache(downloadTask.getCacheId(),
//   			 downloadTask.getTaskId(), downloadTask);
		logger.debug("searching for experiment");
        //find Experiment
        startTime = System.currentTimeMillis();
        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.SearchingForExperiment);
		CaArrayEntityReference expRef = (CaArrayEntityReference) importer.findExperiment(searchService, experimentName);
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime) / 1000.0;
        logger.debug("Search for experiment took " + totalTime + " second(s).");
        
         //get Data Files 
        startTime = System.currentTimeMillis();
		logger.debug("getting Data files");
        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.GettingDataFiles);
		List<gov.nih.nci.caarray.external.v1_0.data.File> files = importer.selectFilesFromSamples(searchServiceHelper, expRef, downloadTask.getSpecimenList(), downloadTask.getType());
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime) / 1000.0;
        logger.debug("getDataFile for all files took " + totalTime + " second(s).");
        //If no files are found
        if(files == null ){
        	DownloadStatus status = DownloadStatus.NoFilesFoundToDownload;
	    	status.setComment("No files found to Download");
	    	downloadTask.setDownloadStatus(status);
	        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),status);
        }
        /*
        {
        //get Data Files 
        startTime = System.currentTimeMillis();    
		logger.debug("downloading files");
        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.DownloadingFiles);
        listOfZipFiles= importer.downloadZipOfFiles(dataServiceHelper, files, downloadTask.getZipFileName());
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime) / 1000.0;
        logger.debug("downloadFiles for all files took " + totalTime + " second(s).");
        }
       */
        //get Data Files 
        startTime = System.currentTimeMillis();    
		logger.debug("downloading files");
        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.DownloadingFiles);
		Set<ZipItem> zipItems = importer.downloadFiles(searchService, dataServiceHelper, files, downloadTask.getZipFileName());
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime) / 1000.0;
        logger.debug("downloadFiles for all files took " + totalTime + " second(s).");
        //If no files are found
        if(zipItems != null && zipItems.size() == 0){
        	DownloadStatus status = DownloadStatus.NoFilesFoundToDownload;
	    	status.setComment("No files found to Download");
	    	downloadTask.setDownloadStatus(status);
	        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),status);
        }
        //Zip Data Files 
        if(zipItems != null && zipItems.size() > 0){
	        startTime = System.currentTimeMillis();    
			logger.debug("writing zip files");
	        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.ZippingFiles);
	        listOfZipFiles = importer.writeZipFile(zipItems, downloadTask.getZipFileName());
	        endTime = System.currentTimeMillis();
	        totalTime = (endTime - startTime) / 1000.0;
	        logger.debug("writeZipFile for all files took " + totalTime + " second(s).");
        }
        if(listOfZipFiles != null  && listOfZipFiles.size() == 1){
        	File zipfile= new File(outputZipDirectory+File.separator+downloadTask.getZipFileName());
	        if(zipfile.exists()  && zipfile.length()> 0){
	            downloadTask.setEndTime(endTime); 
	        	downloadTask.setZipFileSize(zipfile.length());
	        	downloadTask.setDownloadStatus(DownloadStatus.Completed);
	    		updateDownloadTaskInCache(downloadTask.getCacheId(),
	       			 downloadTask.getTaskId(), downloadTask);
	    
	            double totalProcessTime = (endTime - totalStartTime)/1000.0;
	            logger.debug("Total processing time for all files took " + totalProcessTime + " second(s) or "+ totalProcessTime/60+" minute(s).");
	    		logger.debug("zip file completed");
	        }
        }else if(listOfZipFiles != null  && listOfZipFiles.size() > 1){
        	for(String filename : listOfZipFiles){
        		File zipfile= new File(filename);
    	        if(zipfile.exists()  && zipfile.length()> 0){
    	        	ZipItem zipItem = new DownloadZipItemImpl();
    	        	String name = filename.substring(filename.lastIndexOf(File.separator)+1);
    		        zipItem.setFileName(name);
    		        zipItem.setFilePath(outputZipDirectory+File.separator);
    		        zipItem.setFileSize(zipfile.length());    	           	
    	           	downloadTask.addZipFileItem(zipItem);
    	        }
    	            double totalProcessTime = (endTime - totalStartTime)/1000.0;
    	            logger.debug("Total processing time for all files took " + totalProcessTime + " second(s) or "+ totalProcessTime/60+" minute(s).");
    	    		logger.debug("zip file completed");
    	        
        	}
        	downloadTask.setEndTime(endTime);         	
        	downloadTask.setDownloadStatus(DownloadStatus.Completed);
    		updateDownloadTaskInCache(downloadTask.getCacheId(),
          			 downloadTask.getTaskId(), downloadTask);
        	
        }
		} catch (Exception e) {
			logger.error(e.getMessage());
	    	DownloadStatus status = DownloadStatus.Error;
	    	status.setComment(e.getMessage());
	    	downloadTask.setDownloadStatus(status);
	        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),status);
	        throw e;
		}
		return downloadTask;
	}
	protected static void reportError(String message, Throwable e) {
		if (null == message)
			message = "";
		logger.error(message, e);
		String e_message = "";
		if (null != e)
			e_message = e.getMessage();
		System.err.println(message + ": " + e_message);
	}

	protected CaArrayServer connectToCaArrayServer()
			throws  LoginException, ServerConnectionException {

		logger.debug("connecting to CaArrayServer");
		try {
			server = new CaArrayServer(getCaarrayUrl().getHost(),
					getCaarrayUrl().getPort());
			if (isUserSpecified()) {
				logger.debug("Trying to connect with username and password");
				server.connect(username, password);
			} else {
				logger.debug("Trying to connect.");
				server.connect();
			}
			server.connect();
			logger.debug("connected");
		} catch (ServerConnectionException e) {
			reportError("Unable to connect to server: " + caarrayUrl, e);
			throw e;
		} catch (LoginException e) {
			reportError(
					"Unable to login to server.  Invalid username/password.", e);
			throw e;
		}
		return server;
	}
	/**
	 * Returns true if a username/password has been specified.
	 * @return
	 */
	protected boolean isUserSpecified() {
		if(username != null && username.length()>0 && password != null && password.length()>0){
			return true;
		}
		return false;
	}

	/**
	 * @return the caarrayUrl
	 */
	public URL getCaarrayUrl() {
		return caarrayUrl;
	}

	/**
	 * @param caarrayUrl the caarrayUrl to set
	 */
	public void setCaarrayUrl(URL caarrayUrl) {
		this.caarrayUrl = caarrayUrl;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/* 
	 * return all DownloadTasks for a sessionId 
	 */
	@SuppressWarnings("unchecked")
	public Collection<DownloadTask> getAllSessionDownloads(String sessionId){
		
		return getAllSessionDownloads(getBusinessCacheManager(), sessionId);
	}
	/* 
	 * return all DownloadTasks for a sessionId 
	 */
	@SuppressWarnings("unchecked")
	public static Collection<DownloadTask> getAllSessionDownloads(BusinessTierCache _businessCacheManager, String _sessionId){
		Collection<DownloadTask> beans = new ArrayList<DownloadTask>();
		if(_businessCacheManager != null  && _sessionId != null 
				&& _businessCacheManager.getSessionCache(_sessionId) != null){
			Cache sessionCache = _businessCacheManager.getSessionCache(_sessionId);
			try {
				List keys = sessionCache.getKeys();
				for(Iterator i = keys.iterator();i.hasNext();) {
					Element element = sessionCache.get((String)i.next());
					Object object = element.getValue();
					if(object instanceof DownloadTask) {
						beans.add((DownloadTask)object);
					}
				}
			}catch(CacheException ce) {
				logger.error(ce);
			}
		}
		return beans;
	}
	/**
	 *  return a DownloadTask for a sessionId & taskId
	 */
	public DownloadTask getSessionDownload(String sessionId, String taskId){
		DownloadTask downloadTask = null;
		Collection<DownloadTask> downloadTasks = getAllSessionDownloads(sessionId);
		for(DownloadTask task:downloadTasks){
			if(task.getTaskId().equalsIgnoreCase(taskId)){
				downloadTask = task;
			}
		}
		return downloadTask;
	}
	/**
	 *  change the downloadStatus for a sessionId & taskId
	 */
	public Boolean setStatusInCache(String sessionId, String taskId, DownloadStatus status){
		Boolean flag = false;
		if(sessionId != null  && taskId != null && status != null){
			DownloadTask downloadTask = getSessionDownload(sessionId, taskId);
			if(downloadTask != null){
				downloadTask.setDownloadStatus(status);
				businessCacheManager.addToSessionCache(downloadTask.getCacheId(),
			  			 downloadTask.getTaskId(), downloadTask);
				flag = true;
			}
		}
		return flag;
	}
	public void updateDownloadTaskInCache(String sessionId, String taskId, DownloadTask downloadTask){
		if(sessionId != null  && taskId != null && downloadTask != null){
	    	 businessCacheManager.addToSessionCache(downloadTask.getCacheId(),
	    			 downloadTask.getTaskId(), downloadTask);
		}
	}

	public BusinessTierCache getBusinessCacheManager() {
		return businessCacheManager;
	}

	public void setBusinessCacheManager(BusinessTierCache businessCacheManager) {
		this.businessCacheManager = businessCacheManager;
	}

	public ThreadPoolExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(ThreadPoolExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public String getInputDirectory() {
		return outputZipDirectory;
	}

	public void setInputDirectory(String inputDirectory) {
		this.outputZipDirectory = inputDirectory;
	}

	public String getOutputZipDirectory() {
		return outputZipDirectory;
	}

	public void setOutputZipDirectory(String outputZipDirectory) {
		this.outputZipDirectory = outputZipDirectory;
	}
	
}
