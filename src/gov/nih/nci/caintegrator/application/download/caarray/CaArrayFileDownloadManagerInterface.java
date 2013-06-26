/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.download.caarray;

import gov.nih.nci.caarray.domain.file.FileType;
import gov.nih.nci.caintegrator.application.cache.BusinessTierCache;
import gov.nih.nci.caintegrator.application.download.DownloadTask;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.core.task.TaskExecutor;

public interface CaArrayFileDownloadManagerInterface {

	String ZIP_FILE_PATH = "ZipFilePath";

	/**
	 * The executeStrategy method is what performs the heavy lifting in the
	 * strategy.  It will get the results from the QueryHandler back, 
	 * and place the "domain findings" into a taskResult.  
	 * It then places the taskResult finding into the cache. Runs the
	 * execute method in a separate thread for asynchronous operations.     *
	 */
	public abstract Future<?> executeDownloadStrategy(final String session,
			final String taskId, String zipFileName, List<String> specimenList,
			FileType type, final String experimentName);

	/**
	 * @return the caarrayUrl
	 */
	public abstract URL getCaarrayUrl();

	/**
	 * @return the username
	 */
	public abstract String getUsername();

	/**
	 * @return the password
	 */
	public abstract String getPassword();

	/* 
	 * return all DownloadTasks for a sessionId 
	 */
	@SuppressWarnings("unchecked")
	public abstract Collection<DownloadTask> getAllSessionDownloads(
			String sessionId);

	public abstract void updateDownloadTaskInCache(String sessionId,
			String taskId, DownloadTask downloadTask);

	public abstract BusinessTierCache getBusinessCacheManager();

	public abstract ThreadPoolExecutor getTaskExecutor();

	public abstract String getOutputZipDirectory();

	public abstract void setBusinessCacheManager(
			BusinessTierCache businessTierCache);

	public abstract void setTaskExecutor(ThreadPoolExecutor taskExecutor);

}