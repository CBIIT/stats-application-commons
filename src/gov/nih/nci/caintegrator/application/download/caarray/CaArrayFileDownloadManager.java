package gov.nih.nci.caintegrator.application.download.caarray;
import gov.nih.nci.caarray.domain.file.CaArrayFile;
import gov.nih.nci.caarray.domain.file.FileType;
import gov.nih.nci.caarray.domain.project.Experiment;
import gov.nih.nci.caarray.services.CaArrayServer;
import gov.nih.nci.caarray.services.ServerConnectionException;
import gov.nih.nci.caarray.services.file.FileRetrievalService;
import gov.nih.nci.caarray.services.search.CaArraySearchService;
import gov.nih.nci.caintegrator.application.cache.BusinessTierCache;
import gov.nih.nci.caintegrator.application.download.DownloadStatus;
import gov.nih.nci.caintegrator.application.download.DownloadTask;
import gov.nih.nci.caintegrator.application.zip.ZipItem;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.LoginException;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;
import org.springframework.core.task.TaskExecutor;

public abstract class CaArrayFileDownloadManager {
	public static Logger logger = Logger
			.getLogger(CaArrayFileDownloadManager.class);
	protected static CaArrayFileDownloadManager instance = null;
    /**
     * Cache manager which the ApplicationFinding result will be put into
     */ 
    protected BusinessTierCache businessCacheManager;
	/**
	 * Name of the experiment.
	 */
	private String experimentName;
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
    protected TaskExecutor taskExecutor;
	private String inputDirectory;
	private String outputZipDirectory;
	private URL zipFileUrl;

	protected CaArrayFileDownloadManager(String caarrayUrl,
			String experimentName, String username, String password,
			String inputDirectory, String outputZipDirectory, String directoryInZip, String zipFileUrl) 
			throws MalformedURLException, LoginException, ServerConnectionException {
		try {
			this.caarrayUrl = new URL(caarrayUrl);
			this.zipFileUrl = new URL(zipFileUrl);
		} catch (MalformedURLException e) {
			reportError("URL was invalid: " + caarrayUrl, e);
			throw e;
		}
		if (null != experimentName)
			this.experimentName = experimentName.trim();
		if (null != username)
			this.username = username.trim();
		if (null != password)
			this.password = password.trim();
		if (null != inputDirectory)
			this.inputDirectory = inputDirectory.trim();
		if (null != outputZipDirectory)
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
    public void executeDownloadStrategy(final String session, final String taskId,String zipFileName, List<String> specimenList, FileType type) {   
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
        
        Runnable task = new Runnable() {
            public void run() {
            	try {
					downloadFiles(session,  taskId);
				} catch (IllegalArgumentException e) {
					logger.error(e.getMessage());
				} catch (IOException e) {
					logger.error(e.getMessage());					
				}
            }
        };
        taskExecutor.execute(task);
    }
    
	/**
	 * Tries to find an experiment with a publicIdentifier
	 * equal to the experimentName.  If it doesn't find one with a matching public
	 * identifier, then it tries to find one with title equal to the experimentName.
	 * If it doesn't find one, throws exception.  Otherwise, tries to download
	 * data files from the experiment that it found.
	 * @throws IOException 
	 *  
	 * @throws ServerConnectionException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws LoginException
	 */
	private void downloadFiles(String sessionId, String taskId) throws IOException {
		long startTime = 0;
        long endTime = 0;
        double totalTime = 0;
        DownloadTask downloadTask = getSessionDownload(sessionId, taskId);
        if(downloadTask == null){
        	throw new IllegalStateException();
        }
        try{
        // Connect to server.
        startTime = System.currentTimeMillis();
        long totalStartTime = startTime;
	
		CaArraySearchService searchService = server.getSearchService();
		FileRetrievalService fileService = server.getFileRetrievalService();
//		businessCacheManager.addToSessionCache(downloadTask.getCacheId(),
//   			 downloadTask.getTaskId(), downloadTask);
		logger.debug("searching for experiment");
        //find Experiment
        startTime = System.currentTimeMillis();
        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.SearchingForExperiment);
		Experiment experiment = importer.findExperiment(searchService, getExperimentName());
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime) / 1000.0;
        logger.debug("Search for experiment took " + totalTime + " second(s).");
        
         //get Data Files 
        startTime = System.currentTimeMillis();
		logger.debug("getting file info");
        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.GettingDataFiles);
		Set<CaArrayFile> files = importer.getAllDataFiles(searchService, experiment, downloadTask.getSpecimenList(), downloadTask.getType());
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime) / 1000.0;
        logger.debug("getDataFile for all files took " + totalTime + " second(s).");
        
        //get Data Files 
        startTime = System.currentTimeMillis();    
		logger.debug("downloading files");
        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.DownloadingFiles);
		Set<ZipItem> zipItems = importer.downloadFiles(fileService, files, inputDirectory);
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime) / 1000.0;
        logger.debug("downloadFiles for all files took " + totalTime + " second(s).");
        
        //Zip Data Files 
        startTime = System.currentTimeMillis();    
		logger.debug("writing zip files");
        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),DownloadStatus.ZippingFiles);
		importer.writeZipFile(zipItems, downloadTask.getZipFileName());
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime) / 1000.0;
        logger.debug("writeZipFile for all files took " + totalTime + " second(s).");
        
        
        File zipfile= new File(outputZipDirectory+File.separator+downloadTask.getZipFileName());
        if(zipfile.exists()  && zipfile.length()> 0){
            downloadTask.setEndTime(endTime); 
        	URL zipUrl = new URL(zipFileUrl.toString()+"/"+downloadTask.getZipFileName());
        	downloadTask.setZipFileURL(zipUrl);
        	downloadTask.setDownloadStatus(DownloadStatus.Completed);
    		updateDownloadTaskInCache(downloadTask.getCacheId(),
       			 downloadTask.getTaskId(), downloadTask);
    
            double totalProcessTime = (endTime - totalStartTime)/1000.0;
            logger.debug("Total processing time for all files took " + totalProcessTime + " second(s) or "+ totalProcessTime/60+" minute(s).");
    		logger.debug("zip file completed");
        }
		} catch (IOException e) {
			logger.error(e.getMessage());
	    	DownloadStatus status = DownloadStatus.Error;
	    	status.setComment(e.getMessage());
	    	downloadTask.setDownloadStatus(status);
	        setStatusInCache(downloadTask.getCacheId(),downloadTask.getTaskId(),status);
	        throw e;
		}
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
			throws ServerConnectionException, LoginException {

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
	 * @return the experimentName
	 */
	public String getExperimentName() {
		return experimentName;
	}

	/**
	 * @param experimentName the experimentName to set
	 */
	public void setExperimentName(String experimentName) {
		this.experimentName = experimentName;
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
		Collection<DownloadTask> beans = new ArrayList<DownloadTask>();
		Cache sessionCache = businessCacheManager.getSessionCache(sessionId);
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

	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
}
