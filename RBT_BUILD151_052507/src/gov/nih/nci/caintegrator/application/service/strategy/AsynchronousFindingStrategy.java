package gov.nih.nci.caintegrator.application.service.strategy;

import gov.nih.nci.caintegrator.application.cache.BusinessTierCache;
import gov.nih.nci.caintegrator.dto.query.QueryDTO;
import gov.nih.nci.caintegrator.enumeration.FindingStatus;
import gov.nih.nci.caintegrator.exceptions.FindingsAnalysisException;
import gov.nih.nci.caintegrator.exceptions.FindingsQueryException;
import gov.nih.nci.caintegrator.exceptions.ValidationException;
import gov.nih.nci.caintegrator.service.findings.Finding;
import gov.nih.nci.caintegrator.service.findings.strategies.SessionBasedFindingStrategy;
import gov.nih.nci.caintegrator.service.task.Task;
import gov.nih.nci.caintegrator.service.task.TaskResult;
import gov.nih.nci.caintegrator.studyQueryService.QueryHandler;

import java.util.List;

import org.springframework.core.task.TaskExecutor;

/**
 * This is a generic implementation of a FindingStrategy.  It allows
 * for a QueryHandler to be set in it and will run a passed in 
 * QueryDTO against the QueryHandler.
 * 
 *
 * @author caIntegrator Team
 */
public class AsynchronousFindingStrategy extends SessionBasedFindingStrategy {

    // Handler that will handle this particular type of query
    protected QueryHandler queryHandler;
    // Cache manager which the ApplicationFinding result will be put into
    protected BusinessTierCache businessCacheManager;
    // This is the "Application-Level" finding that contains a collection
    // of "domain findings" from the executed query.  This will be
    // placed into the BusinessTierCache
    protected Finding applicationFinding;
    
    protected TaskExecutor taskExecutor;
    
    
    public TaskResult retrieveTaskResult(Task task){
        TaskResult taskResult = (TaskResult) businessCacheManager.getObjectFromSessionCache(task.getCacheId(),task.getId());
        return taskResult;
    }

    /**
     * @return Returns the taskResult.
     */
    public TaskResult getTaskResult() {
        return taskResult;
    }


    /**
     * @param taskResult The taskResult to set.
     */
    public void setTaskResult(TaskResult taskResult) {
        this.taskResult = taskResult;
    }


    /**
     * This default constructor is necessary because we are using
     * the strategies as managed beans from spring. Spring needs a
     * blank default construcctor.
     *
     */
    public AsynchronousFindingStrategy() {
        super(null, null);
    }
    
    
    public Finding getApplicationFinding() {
        return applicationFinding;
    }

    public void setApplicationFinding(Finding applicationFinding) {
        this.applicationFinding = applicationFinding;
    }

    public boolean analyzeResultSet() throws FindingsAnalysisException {
        return true;
    }

    public boolean createQuery() throws FindingsQueryException {
        return true;
    }

    /**
     * This executeQuery method can be called from anyone wanting to 
     * invoke the entire strategy.  First, it will call createQuery, then
     * validate.  It then will call executeStrategy which is where the 
     * query will be sent to the QueryHandler and results will be
     * placed into the BusinessTierCache.  It will then call analyzeResultSet.
     *
     * @return
     * @throws FindingsQueryException
     */
    public boolean executeQuery() throws FindingsQueryException {
        try {            
            createQuery();
            validate();
            executeStrategy();
            analyzeResultSet();
        } catch (ValidationException e) {
            e.printStackTrace();
            throw new FindingsQueryException(e);
        } catch (FindingsAnalysisException e) {
            e.printStackTrace();
            throw new FindingsQueryException(e);
        }
        return true;
    }

    /**
     * This will return the ApplicationFinding that was created from the query.
     *
     * @return
     */
    public Finding getFinding() {
        return applicationFinding;
    }

    /**
     * The validate method will validate the query to make sure it
     * is valid.  It also checks to see that the strategy is in the 
     * correct state and a taskId, sessionId and queryDto have all been
     * passed to it.
     *
     * @param query
     * @return
     * @throws ValidationException
     */
    public boolean validate() throws ValidationException {
        if (getTaskResult().getTask().getCacheId() == null || getTaskResult().getTask().getId() == null 
                || getTaskResult().getTask().getQueryDTO() == null)
            throw new ValidationException(
                    "Session ID, Task ID or Query cannot be null");
        return true;
    }

    /**
     * The executeStrategy method is what performs the heavy lifting in the
     * strategy.  It will get the results from the QueryHandler back, 
     * and place the "domain findings" into a taskResult.  
     * It then places the taskResult finding into the cache. Runs the
     * execute method in a separate thread for asynchronous operations.     *
     */
    protected void executeStrategy() {         
        
        businessCacheManager.addToSessionCache(getTaskResult().getTask().getCacheId(),
                getTaskResult().getTask().getId(), getTaskResult());
        System.out.println("Task has been set to running and placed in cache, query will be run");
        
        Runnable task = new Runnable() {
            public void run() {
                List<gov.nih.nci.caintegrator.domain.finding.bean.Finding> findings = null;
                try {
                findings = queryHandler
                    .getResults(getTaskResult().getTask().getQueryDTO());
                getTaskResult().setTaskResults(findings);
                getTaskResult().getTask().setStatus(FindingStatus.Completed);
                } catch(Exception e) {
                    FindingStatus status = FindingStatus.Error;
                    status.setComment(e.getMessage());
                    getTaskResult().getTask().setStatus(status);
                }

            businessCacheManager.addToSessionCache(getTaskResult().getTask().getCacheId(), 
                    getTaskResult().getTask().getId(), getTaskResult());
            System.out.println("Query has completed, task has been placed back in cache");
    
            }
        };
        taskExecutor.execute(task);
    }
    
    
    public QueryHandler getQueryHandler() {
        return queryHandler;
    }

    public void setQueryHandler(QueryHandler queryHandler) {
        this.queryHandler = queryHandler;
    }

    

    public boolean canHandle(QueryDTO query) {
        if(this.queryHandler.canHandle(query)){
            return true;
        }
        else return false;
    }

    /**
     * @return Returns the taskExecutor.
     */
    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }


    /**
     * @param taskExecutor The taskExecutor to set.
     */
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }


    /**
     * @return Returns the businessCacheManager.
     */
    public BusinessTierCache getBusinessCacheManager() {
        return businessCacheManager;
    }


    /**
     * @param businessCacheManager The businessCacheManager to set.
     */
    public void setBusinessCacheManager(BusinessTierCache businessCacheManager) {
        this.businessCacheManager = businessCacheManager;
    }


    public boolean validate(QueryDTO query) throws ValidationException {
        // TODO Auto-generated method stub
        return false;
    }

    
}
