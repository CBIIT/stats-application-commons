/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.analysis;

import gov.nih.nci.caintegrator.analysis.messaging.AnalysisRequest;
import gov.nih.nci.caintegrator.analysis.messaging.AnalysisRequestSender;
import gov.nih.nci.caintegrator.analysis.messaging.AnalysisResult;
import gov.nih.nci.caintegrator.application.cache.BusinessTierCache;
import gov.nih.nci.caintegrator.application.cache.CacheFactory;
import gov.nih.nci.caintegrator.application.service.ApplicationService;
import gov.nih.nci.caintegrator.application.service.annotation.GeneExprAnnotationService;
import gov.nih.nci.caintegrator.domain.annotation.gene.bean.GeneExprReporter;
import gov.nih.nci.caintegrator.domain.annotation.service.AnnotationManager;
import gov.nih.nci.caintegrator.dto.query.ClassComparisonQueryDTO;
import gov.nih.nci.caintegrator.enumeration.FindingStatus;
import gov.nih.nci.caintegrator.exceptions.AnalysisServerException;
import gov.nih.nci.caintegrator.service.findings.AnalysisFinding;
import gov.nih.nci.caintegrator.service.findings.ReporterBasedFinding;
import gov.nih.nci.caintegrator.studyQueryService.dto.annotation.AnnotationCriteria;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.jms.DeliveryMode;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * This object is used by the Rembrandt application send analysis requests to and receive results from the 
 * analysis server(s). There is only one instance of this object (singleton object) for the application. 
 * Communication with the analysis server(s) is implemented using the JBossMQ JMS implementation. Requests are sent to the
 * AnalysisRequest JMS queue and results are returned to the AnalysisResponse JMS queue. 
 * 
 * @author sahnih, harrismic
 * 
 */




public class AnalysisServerClientManager implements ApplicationService, MessageListener, ExceptionListener, AnalysisRequestSender{
	private static Logger logger = Logger.getLogger(AnalysisServerClientManager.class);
	
	//private BusinessTierCache _cacheManager = (BusinessTierCache)ApplicationContext.getApplicationService("BUSINESS_TIER_CACHE");
	private BusinessTierCache _cacheManager = null; 
	
    //private Properties messagingProps;
	private QueueSession queueSession;
 
	//private QueueSender requestSender;
	private QueueReceiver resultReceiver;

	private Queue requestQueue;
	private Queue resultQueue;
	private QueueConnectionFactory queueConnectionFactory;
    private QueueConnection queueConnection;
    private QueueConnection oldConnection = null;
    private Boolean managedBean = false;
    private static AnalysisServerClientManager instance = null;
    private static final long reconnectWaitTimeMS = 5000L;
    //private Properties messagingProps = null;
    private GeneExprAnnotationService gxAnnotService = null;
    
    // Spring managed annotation service
    private AnnotationManager annotationManager;
    
    private String jmsProviderURL = null;
    private String requestQueueName = null;
    private String responseQueueName = null;
    private String jndiFactoryName = null;
    private boolean jmsParamsSet = false;
    
	/**
	 * @param properties
	 * @throws NamingException 
	 * @throws JMSException 
	 */
	@SuppressWarnings("unchecked")
	private AnalysisServerClientManager() throws NamingException, JMSException {
		 try {
			logger.debug("AnalysisServerClientManager constructor start");
			
			//establishQueueConnection();
			
			logger.debug("AnalysisServerClientManager constructor finished successfully");
	    }catch(Throwable t) {
	    	logger.error("Constructor has thrown an exception of type:"+t.getClass());
	    	logger.error(t);
	    }
	}
	
	
	public void setCache(BusinessTierCache cache) {
	  this._cacheManager = cache;
	}
	
	public void setJMSparameters(String jmsProviderURL, String jndiFactoryName, String requestQueueName, String responseQueueName) {
	  this.jmsProviderURL = jmsProviderURL;
	  this.jndiFactoryName = jndiFactoryName;
	  this.requestQueueName = requestQueueName;
	  this.responseQueueName = responseQueueName;
	  this.jmsParamsSet = true;
	}
	
	public void setGeneExprAnnotationService(GeneExprAnnotationService annotationService) {
	  this.gxAnnotService = annotationService;
	}
	
	
	/**
	 * Establish a connection to the JMS queues.  If it is not possible
	 * to connect then this method will sleep for reconnectWaitTimeMS milliseconds and
	 * then try to connect again.  
	 *
	 */
	public void establishQueueConnection() {
        
		boolean connected = false;
		int numConnectAttempts = 0;
		
		if (!jmsParamsSet) {
		  logger.error("Attempted to establish queue connection with unset JMS parameters.  Must first call setJMSparameters method.");
		  throw new IllegalStateException("Attempted to establish queue connection with unset JMS parameters.  Must first call setJMSparameters method.");
		}
		
		//Properties messagingProps = ApplicationContext.getJMSProperties();
		//String jbossURL = System.getProperty("gov.nih.nci.caintegrator.analysis.jms.jboss_url");
		
			while (!connected) {
		
			  try {
					
				//logger.debug("AnalysisServerClientManager constructor start");
				//Properties messagingProps = ApplicationContext.getJMSProperties();
				
				logger.info("Attempting to establish queue connection with provider: " + jmsProviderURL);
				
				// Populate with needed properties
				Hashtable props = new Hashtable();
				props.put(Context.INITIAL_CONTEXT_FACTORY,
						"org.jnp.interfaces.NamingContextFactory");
				props.put(Context.PROVIDER_URL, jmsProviderURL);
				props.put("java.naming.rmi.security.manager", "yes");
				//props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces:org.jboss.naming.client");
			
				// Get the initial context with given properties
				Context context = new InitialContext(props);
			
				// Get the connection factory
			
			    //String factoryJNDI = System.getProperty("gov.nih.nci.caintegrator.analysis.jms.factory_jndi");
				
				queueConnectionFactory = (QueueConnectionFactory) context
						.lookup(jndiFactoryName);
			
				// Create the connection
			
				queueConnection = queueConnectionFactory.createQueueConnection();
			
				queueConnection.setExceptionListener(this);
			
				// Create the session
				queueSession = queueConnection.createQueueSession(
				// No transaction
						false,
						// Auto ack
						Session.AUTO_ACKNOWLEDGE);
			
				// Look up the destination
				//String requestQueueName = System.getProperty("gov.nih.nci.caintegrator.analysis.jms.analysis_request_queue");
				
				//String responseQueueName = System.getProperty("gov.nih.nci.caintegrator.analysis.jms.analysis_response_queue");
				
				requestQueue = (Queue) context.lookup(requestQueueName);
				resultQueue = (Queue) context.lookup(responseQueueName);
			
				// Create a publisher
				resultReceiver = queueSession.createReceiver(resultQueue);
				resultReceiver.setMessageListener(this);
			
				queueConnection.start();
		
			    connected = true;
			    numConnectAttempts = 0;
			  
			    logger.info("  successfully established queue connection with provider=" + jmsProviderURL);
			    logger.info("  successfully found request queue=" + requestQueueName);
			    logger.info("  successfully found response queue=" + responseQueueName);
			    logger.info("Now listening for requests...");
			  }
			  catch (Exception ex) {
				  logger.error("Caught exception when trying to establish connection.");
				  logger.error(ex);
			    numConnectAttempts++;
			  
			    if (numConnectAttempts <= 10) {
			      logger.warn("  could not establish connection with provider=" + jmsProviderURL + " after numAttempts=" + numConnectAttempts + "  Will try again in  " + Long.toString(reconnectWaitTimeMS/1000L) + " seconds...");
			      if (numConnectAttempts == 10) {
			        logger.warn("  Will only print connection attempts every 600 atttempts to reduce log size.");
			      }
			    }
			    else if ((numConnectAttempts % 600) == 0) {
				  logger.info("  could not establish connection after numAttempts=" + numConnectAttempts + " will keep trying every " + Long.toString(reconnectWaitTimeMS/1000L) + " seconds...");
			    }
			  
			    try { 
			      Thread.sleep(reconnectWaitTimeMS);
			    }
			    catch (Exception ex2) {
			      logger.error("Caugh exception while trying to sleep.." + ex2.getMessage());
			      logger.error(ex2);
			      //ex2.printStackTrace(System.out);
			      return;
			    }
		    }
		  }	
		}

	
	/**
	 * JMS notification about a new message
	 */
	public void onMessage(Message message) {
		 //String msg = ((TextMessage)m).getText();
		  logger.debug("onMessage has been called");
	      ObjectMessage msg = (ObjectMessage) message;
	      try {
			Object result = msg.getObject();
			if( result instanceof AnalysisResult){
				receiveResult((AnalysisResult) result);
			}
			else if( result instanceof AnalysisServerException){
				receiveException((AnalysisServerException) result);
			}
			
		} catch (JMSException e) {
			logger.error(e);
		}
		
	}
	////////////////////
	/**
	 * JMS notification about an exception
	 */
	public synchronized void onException(JMSException jmsException) {
		logger.error("onException: caught JMSexception: " + jmsException.getMessage());
		jmsException.printStackTrace();
		oldConnection = queueConnection;
		boolean reconnection = false;
		//retry : for (int i = 0; i < 10; i++) {
			try {
		   	    //attempt to re-establish the queue connection
		   	    establishQueueConnection();
		   	    reconnection = true;
				//break retry;
			} catch (Exception e2) {
				logger.error( "reconnection failed ");
				try {
					logger.error( "sleep for 2 seconds and try reconnect again ");
					Thread.sleep(2000);
				} catch (InterruptedException e1) {
					logger.error( "reconnection failed "+e1.getMessage());
					e1.printStackTrace();
				}
			}
		//}
		if (!reconnection) {
			logger.error( "RECONNECTION FAILED ");
		}

		Thread disconnThread = new Thread() {
			public void run() {
				try {
					if (oldConnection != null) {
						oldConnection.setExceptionListener(null);
						oldConnection.close();
						oldConnection = null;
					}
					logger.debug( "close the old connection success ");
					return;
				} catch (Exception e) {
			      	logger.error("Catching exception thrown when closing broken connection msg=" + e.getMessage());
			      	final Writer result = new StringWriter();
			        final PrintWriter printWriter = new PrintWriter(result);
			        e.printStackTrace(printWriter);
			        logger.error(result.toString());
				}
			}
		};
		disconnThread.start();
	} 
	
	////////////////////////
	/**
	 * JMS notification about an exception
	 */
	/*
    public void onException(JMSException jmsException) {
    	 //System.out.println("onException: caught JMSexception: " + exception.getMessage());
  	  logger.error("onException: caught JMSexception: " + jmsException.getMessage());
  	  try
        {
  		 if (queueConnectionFactory != null) {
             QueueConnection connection = queueConnectionFactory.createQueueConnection();
             if(connection != null) {
                 connection.setExceptionListener(null);
                 //close();
                 connection.close();
             }
  		 }  	  	  
   	    //attempt to re-establish the queue connection
   	    establishQueueConnection();
        }
        catch (JMSException c)
        {
      	logger.error("Catching exception thrown when closing broken connection msg=" + c.getMessage());
      	final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        c.printStackTrace(printWriter);
        logger.error(result.toString());

        }

	}
	*/
    /***
     * Receive an analysis result
     * 
     * @param analysisResult is the result 
     */
    public void receiveResult(AnalysisResult analysisResult) {
    	String sessionId = analysisResult.getSessionId();
		String taskId = analysisResult.getTaskId();
		logger.debug("AnalysisResult session: "+sessionId+" & task: "+taskId+" has been returned");
		logger.debug("Retreiving finding for session: "+sessionId+" & task: "+taskId+" from cache");
		AnalysisFinding finding = (AnalysisFinding)_cacheManager.getSessionFinding(sessionId, taskId);
		if(finding != null){
			finding.setAnalysisResult(analysisResult);
			if (finding instanceof ReporterBasedFinding)  {
				//generate the annotations
				ReporterBasedFinding rf = (ReporterBasedFinding) finding;
				List<String> reporterIds = rf.getReporterIds();
					
				Map reporterAnnotationMap = null;
			
		        try {
                    // Use cgom based annotation service if it has been injected
                    if(managedBean && annotationManager != null) {
                        if(reporterIds == null || reporterIds.size() < 1) {
                            throw new Exception("Query returned no results");
                        }
                        AnnotationCriteria criteria = new AnnotationCriteria();
                        ClassComparisonQueryDTO queryDTO = (ClassComparisonQueryDTO)finding.getTask().getQueryDTO();
                        
                        criteria.setReporterIds(reporterIds);
                        criteria.setArrayPlatformType(queryDTO.getArrayPlatformDE().getValueObjectAsArrayPlatformType());
                        reporterAnnotationMap = new HashMap();
                        Collection<GeneExprReporter> reporters = annotationManager.getReporterAnnotations(criteria);
                        for(GeneExprReporter reporter : reporters) {
                            reporterAnnotationMap.put(reporter.getName(), reporter);
                        }
                        rf.setReporterAnnotationsMap(reporterAnnotationMap);
                    } else if (gxAnnotService != null) {
		        	  reporterAnnotationMap = gxAnnotService.getAnnotationsMapForReporters(reporterIds);
		        	  rf.setReporterAnnotationsMap(reporterAnnotationMap);
		        	}
		        }
		        catch(Exception e){
		          logger.error("Caught exception trying to get annotations for reporters sessionId=" + sessionId + " taskId=" + taskId);
		          logger.error(e);
		         
		          //Set the finding status to error and store in the cache
		          FindingStatus newStatus = FindingStatus.Error;
				  newStatus.setComment(e.getMessage());
				  finding.setStatus(newStatus);
                    if(finding.getTask() != null) {
                        finding.getTask().setStatus(newStatus);
                    }
				  _cacheManager.addToSessionCache(sessionId,taskId,finding);				  
		          return;
		        }
			}
			
			finding.setStatus(FindingStatus.Completed);
            if(finding.getTask() != null) {
                finding.getTask().setStatus(FindingStatus.Completed);
            }
			logger.debug("Following task has been completed:/n  SessionId: "+sessionId+"/n  TaskId: "+taskId);
			_cacheManager.addToSessionCache(sessionId,taskId,finding);
			logger.debug("Following finding has been placed in cache:/n  SessionId: "+sessionId+"/n  TaskId: "+taskId);
		}
	}

    /**
     * Receive an analysis exception.  Analysis exceptions can be returned from an analysis server
     * where there is a problem completing the computation.  Exceptions can be caused by invalid request parameters or parameters
     * that would produce statistically invalid results.
     *  
     * @param analysisServerException the exception.
     */
	public void receiveException(AnalysisServerException analysisServerException) {
		String sessionId = analysisServerException.getFailedRequest().getSessionId();
		String taskId = analysisServerException.getFailedRequest().getTaskId();
		logger.debug("AnalysisServerException session: "+sessionId+" & task: "+taskId+" has been returned");
		AnalysisFinding finding = (AnalysisFinding)_cacheManager.getSessionFinding(sessionId, taskId);
		if(finding != null){
			FindingStatus newStatus = FindingStatus.Error;
			//the below actually causes an error/caching effect since this is static
			newStatus.setComment(analysisServerException.getMessage());
            if(finding.getTask() != null) {
                finding.getTask().setStatus(newStatus);
            }
			finding.setStatus(newStatus);
			logger.debug("Retreiving finding for session: "+sessionId+" & task: "+taskId+" from cache");
			_cacheManager.addToSessionCache(sessionId,taskId+"_analysisServerException",analysisServerException);
			_cacheManager.addToSessionCache(sessionId,taskId,finding);
			logger.debug("Following finding has been placed in cache:/n  SessionId: "+sessionId+"/n  TaskId: "+taskId);
			logger.error(analysisServerException);
		}
	}

	/**
	 * @return Returns the instance.
	 */
	public static AnalysisServerClientManager getInstance()  throws NamingException, JMSException{
		//first time
		if(instance == null){
			try {
				instance = new AnalysisServerClientManager();
			} catch (NamingException e) {
				logger.error(e.getMessage());
				throw e;
			} catch (JMSException e) {
				logger.error(e.getMessage());
				throw e;
			}
		}
		return instance;
	}

	/**
	 * Send an AnalysisRequest to the JMS request queue. Note this method does not store anything
	 * in the cache. 
	 * @throws JMSException 
	 * @see sendRequest(Query query, AnalysisRequest request)
	 */
	public void sendRequest(final AnalysisRequest request) throws JMSException {
		ObjectMessage msg;
        
		try {
            if(managedBean) {
                queueConnection = queueConnectionFactory.createQueueConnection();
            }
		    
			QueueSession requestSession = queueConnection.createQueueSession(
				      // No transaction
				      false,
				      // Auto ack
				      Session.AUTO_ACKNOWLEDGE);
			
		    // Create a message
			msg = requestSession.createObjectMessage(request);
			
			msg.setJMSReplyTo(resultQueue);
			
			QueueSender requestSender = requestSession.createSender(requestQueue);
			// Send the message
		    requestSender.send(msg, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
		    requestSender.close();
		    requestSession.close();
            if(managedBean) {
                queueConnection.close();
            }
			logger.debug("sendRequest session: "+request.getSessionId()+" & task: "+request.getTaskId()+" has been sent to the JMQ");

		} catch (JMSException e) {
			logger.error(e);
			throw e;
		} 
	}


    public Queue getResultQueue() {
        return resultQueue;
    }


    public void setResultQueue(Queue resultQueue) {
        this.resultQueue = resultQueue;
    }


    public QueueConnectionFactory getQueueConnectionFactory() {
        return queueConnectionFactory;
    }


    public void setQueueConnectionFactory(
            QueueConnectionFactory queueConnectionFactory) {
        this.queueConnectionFactory = queueConnectionFactory;
    }


    public Queue getRequestQueue() {
        return requestQueue;
    }


    public void setRequestQueue(Queue requestQueue) {
        this.requestQueue = requestQueue;
    }


    public Boolean isManagedBean() {
        return managedBean;
    }


    public void setManagedBean(Boolean managedBean) {
        this.managedBean = managedBean;
    }


    public AnnotationManager getAnnotationManager() {
        return annotationManager;
    }


    public void setAnnotationManager(AnnotationManager annotationManager) {
        this.annotationManager = annotationManager;
    }

}
