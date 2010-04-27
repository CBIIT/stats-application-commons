package gov.nih.nci.caintegrator.application.zip;

import gov.nih.nci.caintegrator.analysis.messaging.AnalysisRequest;
import gov.nih.nci.caintegrator.application.cache.BusinessTierCache;
import gov.nih.nci.caintegrator.application.service.ApplicationService;

import java.util.Hashtable;

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

public abstract class  ZipFileJMSListener implements ApplicationService, MessageListener, ExceptionListener{
	private static Logger logger = Logger.getLogger(ZipFileJMSListener.class);
	
	//private BusinessTierCache _cacheManager = (BusinessTierCache)ApplicationContext.getApplicationService("BUSINESS_TIER_CACHE");
	protected BusinessTierCache _cacheManager = null; 
	
    //private Properties messagingProps;
	private QueueSession queueSession;
 
	private QueueSender requestSender;
	private QueueReceiver resultReceiver;

	private Queue requestQueue;
	private Queue resultQueue;
	private QueueConnection queueConnection;
    private static final long reconnectWaitTimeMS = 5000L;
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
	protected ZipFileJMSListener() throws NamingException, JMSException {
		 try {
			logger.debug("ZipFileJMSListener constructor start");
			
			//establishQueueConnection();
			
			logger.debug("ZipFileJMSListener constructor finished successfully");
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
	
	/**
	 * Establish a connection to the JMS queues.  If it is not possible
	 * to connect then this method will sleep for reconnectWaitTimeMS milliseconds and
	 * then try to connect again.  
	 *
	 */
	@SuppressWarnings("unchecked")
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
				props.put(Context.URL_PKG_PREFIXES, "org.jboss.naming");
			
				// Get the initial context with given properties
				Context context = new InitialContext(props);
			
				// Get the connection factory
			
				QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context
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
			if( result instanceof ZipFileRequest){
				receiveZipFileRequest((ZipFileRequest) result);
			}
			else if( result instanceof Exception){
				receiveZipFileException((Exception) result);
			}
			
		} catch (JMSException e) {
			logger.error(e);
		}
		
	}
	
	protected abstract void receiveZipFileException(Exception exception);


	protected abstract void receiveZipFileRequest(ZipFileRequest request);


	/**
	 * JMS notification about an exception
	 */
    public void onException(JMSException jmsException) {
    	 //System.out.println("onException: caught JMSexception: " + exception.getMessage());
  	  logger.error("onException: caught JMSexception: " + jmsException.getMessage());
  	  try
        {
  		 if (queueConnection != null) {
             queueConnection.setExceptionListener(null);
             //close();
             queueConnection.close();
  		 }
        }
        catch (JMSException c)
        {
      	logger.info("Ignoring exception thrown when closing broken connection msg=" + c.getMessage());
          //System.out.println("Ignoring exception thrown when closing broken connection msg=" + c.getMessage());
          //c.printStackTrace(System.out);
        }
  	  
  	    //attempt to re-establish the queue connection
  	    establishQueueConnection();
	}
	/**
	 * 		//Instantiat this class, using code something like this
		if(instance == null){
			try {
				instance = new ZipFileJMSListener();
			} catch (NamingException e) {
				logger.error(e.getMessage());
				throw e;
			} catch (JMSException e) {
				logger.error(e.getMessage());
				throw e;
			}
		}
		return instance;
	 * @returnReturns the instance.
	 * @throws NamingException
	 * @throws JMSException
	 */
	public abstract ZipFileJMSListener getInstance() throws NamingException, JMSException;

	/**
	 * Send an AnalysisRequest to the JMS request queue. Note this method does not store anything
	 * in the cache. 
	 * @throws JMSException 
	 * @see sendRequest(Query query, AnalysisRequest request)
	 */
	public void sendRequest(AnalysisRequest request) throws JMSException {
		ObjectMessage msg;
		try {
			
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
			logger.debug("sendRequest session: "+request.getSessionId()+" & task: "+request.getTaskId()+" has been sent to the JMQ");

		} catch (JMSException e) {
			logger.error(e);
			throw e;
		} 
	}
	
}
