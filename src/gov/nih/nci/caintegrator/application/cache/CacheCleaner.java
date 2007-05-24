package gov.nih.nci.caintegrator.application.cache;

import gov.nih.nci.caintegrator.application.util.PropertyLoader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * CacheCleaner checks in with the CacheTracker
 * at "check.session.cache.tracker" ms interval and reviews the
 * current SessionCaches for unused caches.  An unused
 * sessionCache is any cache that is associated with a session
 * that has been idle for longer than "session.cache.timeout"
 * property file time.  
 * 
 * 
 * @author BauerD
 * Feb 9, 2005
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

public class CacheCleaner extends Thread {
	private static Logger logger = Logger.getLogger(CacheCleaner.class);
    private CacheTracker cacheTracker;
    private SessionTracker sessionTracker;
    
    /***************** Configurable properties *******************/
    //Time to check the sessionCacheChecker: default 5 minutes (300000 ms) 
    private static long CHECK_CACHE_INTERVAL = 300000;  //300000;
    //Cache Timeout in milliseconds: default 20 minutes (1200000 ms)
    private static long CACHE_TIME_OUT = 1200000;  //1200000;
    
    /**
     * Constructor for the CacheCleaner.  Attempts to load a spcified property
     * file and warns if not found. Default values will be used.
     *  
     * @param cacheTracker required to get the current Caches
     * @param sessionTracker required to get the current HttpSessions
     */
     public CacheCleaner(CacheTracker cacheTracker, SessionTracker sessionTracker) {
		super("CacheCleaner");
        this.cacheTracker = cacheTracker;
        this.sessionTracker = sessionTracker;
		Properties cacheProperties = PropertyLoader.loadProperties(CacheConstants.CACHE_PROPERTIES,ClassLoader.getSystemClassLoader());
		String intervalString = null;
		String idleString = null;
		if(cacheProperties!=null) {
		 intervalString = cacheProperties.getProperty("check.session.cache.tracker");
		 idleString = cacheProperties.getProperty("session.cache.timeout");
		}else {
			logger.warn("Cache Property file: "+CacheConstants.CACHE_PROPERTIES+" was not found, using default settings!");	
		}
        if(intervalString!=null) {
        	CHECK_CACHE_INTERVAL = Long.parseLong(intervalString);
        }else {
        	logger.warn("property \"check.session.cache.tracker\" not found");
        	logger.warn("Using default value "+CHECK_CACHE_INTERVAL+" ms between checking caches");
        }
        if(idleString!=null) {
        	CACHE_TIME_OUT = Long.parseLong(idleString);
        }else {
        	logger.warn("property \"session.cache.timeout\" not found");
        	logger.warn("Using default value "+CACHE_TIME_OUT+" ms for idle caches");
        	
        }
	}
	/**
	 * This is the run method that will be in it's own thread.
	 * It get's the latest list of Sessions and Caches and iterates
	 * through the cache keys getting all the associated sessions.
	 * It checks the sessions last accessed time and sees how long
	 * the session has been idle. If it has been idle longer than the 
	 * set idle time ("session.cache.timeout") it will remove the cache.
	 * 
	 *  It should perform this action based on the setting of 
	 *  "check.session.cache.tracker" ms in the resources file.
	 * 
	 */
	public void run() {
		logger.debug("Starting CacheCleaner run() method");
		Thread myThread = Thread.currentThread();
		while (SessionTracker.isAppplicationRunning()){
			logger.debug("CacheCleaner awake: "+System.currentTimeMillis());
			logger.debug("Checking Session Caches");
			HashMap caches = CacheTracker.getActiveSessionCaches();
			SessionMap sessions = SessionTracker.getSessions();
			/*
			 * Only check the caches if there are any.
			 */
			if(caches!=null && !caches.isEmpty()) {
				Set cacheKeys = caches.keySet();
				for(Iterator i = cacheKeys.iterator();i.hasNext();) {
					String sessionId = (String)i.next();
					HttpSession session = sessions.getSession(sessionId);
					//session-timeout - 30 sec
					//CACHE_TIME_OUT = session.getMaxInactiveInterval()- 30000;
					//logger.debug("Session.getMaxInactiveInterval: "+ session.getMaxInactiveInterval());
					//logger.debug("CACHE_TIME_OUT: "+ CACHE_TIME_OUT);
					if(session!=null) {
						/*
						 * This is the check to see if the session has been idle
						 * long enough to toss out it's associated cache, 
						 * if it has one.  Another check that I could implement
						 * would be to check the cache statistics and see when
						 * any of the elements were last accessed, their size and
						 * other considerations to determine if they should be
						 * purged from the existing cache, rather than throwing
						 * the whole cache out.
						 */
						long idleTime = System.currentTimeMillis() - session.getLastAccessedTime();
						if(idleTime > CACHE_TIME_OUT) {
							logger.debug("Session "+sessionId+" idle too long. Removing cache");
							PresentationCacheManager.getInstance().removeSessionCache(sessionId);
						}
					}else {
						logger.error("Somehow the Sessions are not in synch with the session caches");
					}
				}
			}
				
	        try {
	        	logger.debug("CacheCleaner sleeping: " +System.currentTimeMillis());
	            Thread.sleep(CHECK_CACHE_INTERVAL);
	        } catch (InterruptedException e){
		            // the VM doesn't want us to sleep anymore,
		            // so get back to work
	        }
			
	    }
	}

}
