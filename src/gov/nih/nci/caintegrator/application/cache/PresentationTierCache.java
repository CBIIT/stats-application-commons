/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.cache;

import gov.nih.nci.caintegrator.service.task.GPTask;
import gov.nih.nci.caintegrator.service.task.Task;

import java.io.Serializable;
import java.util.Collection;




public interface PresentationTierCache {

	//items to be persisted...must be serializable
	public void addPersistableToSessionCache(String sessionId, Serializable key, Serializable object);
	public Object getPersistableObjectFromSessionCache(String sessionId, String key);
    
    //retrieves all Task objects in the cache
    public Collection<Task> getAllSessionTasks(String sessionId);
    public Collection<GPTask> getAllSessionGPTasks(String sessionId);
	//items that will NOT be persisted..  Can be used similar to the HTTPSession
	//these things still need to implement serializable though
	public void addNonPersistableToSessionCache(String sessionId, Serializable key, Serializable object);
	public Object getNonPersistableObjectFromSessionCache(String sessionId, String key);
	
	//items that will be application cache..  Can be used similar to the HTTPSession
	//these things still need to implement serializable though
	public void addToApplicationCache(Serializable key, Serializable value);
	public Collection checkApplicationCache(String lookupType);
	//remove single objects
	public void removeObjectFromPersistableSessionCache(String id, String key);
	public void removeObjectFromNonPersistableSessionCache(String id, String key);
	
	//remove all
	public void removePersistableSessionCache(String sessionId);
	public void removeNonPersistableSessionCache(String sessionId);

	//will clear all session cache, persistable and non-persistable
	public boolean removeSessionCache(String sessionId);
	
	public void addSessionTempFolderPath(String sessionId, String sessionTempFolderPath);
	public String getSessionTempFolderPath(String sessionId);
}

	
