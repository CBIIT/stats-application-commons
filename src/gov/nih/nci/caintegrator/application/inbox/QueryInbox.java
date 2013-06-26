/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.inbox;

import gov.nih.nci.caintegrator.application.cache.BusinessTierCache;
import gov.nih.nci.caintegrator.application.cache.CacheFactory;
import gov.nih.nci.caintegrator.application.cache.PresentationTierCache;
import gov.nih.nci.caintegrator.exceptions.AnalysisServerException;
import gov.nih.nci.caintegrator.service.findings.Finding;
import gov.nih.nci.caintegrator.service.task.Task;
import gov.nih.nci.caintegrator.studyQueryService.FindingsManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;


public class QueryInbox {

    private HttpSession session;

    private BusinessTierCache businessTierCache;

    private PresentationTierCache presentationTierCache;

    private FindingsManager findingsManager;

    public QueryInbox() {
        businessTierCache = CacheFactory.getBusinessTierCache();
        presentationTierCache = CacheFactory.getPresentationTierCache();
    }

    public String checkSingle(String sid, String tid) {
        // check the status of a single task
        String currentStatus = "";

        Finding f = (Finding) businessTierCache.getObjectFromSessionCache(sid,
                tid);

        switch (f.getStatus()) {
        case Completed:
            currentStatus = "completed";
            break;
        case Running:
            currentStatus = "running";
            break;
        case Error:
            currentStatus = "error";
            break;
        default:
            currentStatus = "running";
            break;
        }

        return currentStatus;
    }

    public Map checkAllStatus(String sid) {
        Map currentStatuses = new HashMap();

        Collection<Finding> findings = businessTierCache
                .getAllSessionFindings(sid);
        for (Finding f : findings) {
            String tmp = new String();
            tmp = this.checkSingle(sid, f.getTaskId());

            Map fdata = new HashMap();
            fdata.put("time", String.valueOf(f.getElapsedTime()));
            fdata.put("status", tmp);
            if (f.getStatus() != null && f.getStatus().getComment() != null) {
                AnalysisServerException ase = (AnalysisServerException) businessTierCache
                        .getObjectFromSessionCache(sid, f
                                .getTaskId()
                                + "_analysisServerException");
                String comments = ase != null && ase.getMessage() != null ? ase
                        .getMessage() : "Unspecified Error";
                fdata.put("comments", StringEscapeUtils
                        .escapeJavaScript(comments));
                // fdata.put("comments",
                // StringEscapeUtils.escapeJavaScript(f.getStatus().getComment()));
            }
            currentStatuses.put(f.getTaskId(), fdata);
        }

        return currentStatuses;
    }

    public Map checkAllTasksStatus(String sid) {
        Map currentStatuses = new HashMap();
        updateTasks(sid);
        Collection<Task> tasks = presentationTierCache.getAllSessionTasks(sid);
        for (Task task : tasks) {
            String tmp = new String();
            tmp = this.checkSingleTask(task);
            Map fdata = new HashMap();
            fdata.put("time", String.valueOf(task.getElapsedTime()));
            fdata.put("status", tmp);
            if (task.getStatus() != null) {

                String comments = null;
                if (task.getStatus().getComment() != null) {
                    comments = task.getStatus().getComment();
                } else {
                    AnalysisServerException ase = (AnalysisServerException) businessTierCache
                            .getObjectFromSessionCache(task.getCacheId(), task
                                    .getId()
                                    + "_analysisServerException");
                    if (ase == null)
                        comments = "Unspecified Error";
                    else
                        comments = ase != null && ase.getMessage() != null ? ase
                                .getMessage()
                                : "Unspecified Error";
                }

                fdata.put("comments", StringEscapeUtils
                        .escapeJavaScript(comments));
            }
            currentStatuses.put(task.getId(), fdata);
        }

        return currentStatuses;
    }

    private void updateTasks(String sid) {
        Collection<Task> tasks = presentationTierCache.getAllSessionTasks(sid);
        for (Task task : tasks) {
            Task currentTask = findingsManager.checkStatus(task);
            presentationTierCache.addNonPersistableToSessionCache(sid, task
                    .getId(), currentTask);
        }
    }

    public String checkSingleTask(Task task) {
        // check the status of a single task
        String currentStatus = "";
        Task currentTask = findingsManager.checkStatus(task);
        switch (currentTask.getStatus()) {
        case Completed:
            currentStatus = "completed";
            break;
        case Running:
            currentStatus = "running";
            break;
        case Error:
            currentStatus = "error";
            break;
        default:
            currentStatus = "running";
            break;

        }

        return currentStatus;
    }

    public String checkStatus() {
        // simulate that the query is still running, assuming we have only 1
        // query for testing

        Random r = new Random();
        int randInt = Math.abs(r.nextInt()) % 11;
        if (randInt % 2 == 0)
            return "false";
        else
            return "true";
    }

    public String deleteFinding(String key) {
        String success = "fail";
        try {

            success = "pass";
        } catch (Exception e) {
        }
        return success;
    }

    public Map mapTest(String testKey) {
        Map myMap = new HashMap();
        myMap.put("firstKey", testKey);
        myMap.put("secondKey", testKey + "_1");
        return myMap;
    }

    /**
     * @return Returns the findingsManager.
     */
    public FindingsManager getFindingsManager() {
        return findingsManager;
    }

    /**
     * @param findingsManager
     *            The findingsManager to set.
     */
    public void setFindingsManager(FindingsManager findingsManager) {
        this.findingsManager = findingsManager;
    }

    public BusinessTierCache getBusinessTierCache() {
        return businessTierCache;
    }

    public void setBusinessTierCache(BusinessTierCache btc) {
        this.businessTierCache = btc;
    }

    public PresentationTierCache getPresentationTierCache() {
        return presentationTierCache;
    }

    public void setPresentationTierCache(PresentationTierCache ptc) {
        this.presentationTierCache = ptc;
    }

}
