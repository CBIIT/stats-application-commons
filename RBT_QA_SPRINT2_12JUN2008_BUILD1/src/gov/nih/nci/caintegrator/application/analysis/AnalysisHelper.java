package gov.nih.nci.caintegrator.application.analysis;


import gov.nih.nci.caintegrator.analysis.messaging.CopyNumberLookupRequest;
import gov.nih.nci.caintegrator.analysis.messaging.DataPoint;
import gov.nih.nci.caintegrator.analysis.messaging.DataPointVector;
import gov.nih.nci.caintegrator.analysis.messaging.ExpressionLookupRequest;
import gov.nih.nci.caintegrator.analysis.messaging.ReporterGroup;
import gov.nih.nci.caintegrator.analysis.messaging.SampleGroup;
import gov.nih.nci.caintegrator.application.cache.BusinessTierCache;
import gov.nih.nci.caintegrator.application.cache.CacheFactory;
import gov.nih.nci.caintegrator.domain.finding.copyNumber.bean.CopyNumberFinding;
import gov.nih.nci.caintegrator.enumeration.FindingStatus;
import gov.nih.nci.caintegrator.service.findings.AnalysisFinding;
import gov.nih.nci.caintegrator.service.findings.CopyNumberLookupFinding;
import gov.nih.nci.caintegrator.service.findings.ExpressionLookupFinding;
import gov.nih.nci.caintegrator.service.findings.Finding;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

/**
 * This class provides some helper methods for making synchronous calls to the analysis server
 * functions. The analysis server is asynchronous so the methods in this class are requrired to transform
 * them into synchronous calls.
 * @author harrismic
 *
 */
public class AnalysisHelper {
	
	private static Logger logger = Logger.getLogger(AnalysisHelper.class);

	
	private static void loadTestVec(DataPointVector vec, SampleGroup samples, boolean log) {
		DataPoint dp;
		for (String sample : samples) {
		  dp = new DataPoint(sample);
		  if (log) {
		    dp.setX(Math.random()+15.0);
		  }
		  else {
		    dp.setX(Math.random()*6000.0);
		  }
		  vec.addDataPoint(dp);
		}
	}
	
//	public static void main(String[] args) {
//	    System.out.println("Hi");
//	    SampleGroup sg = new SampleGroup("Grp1");
//	    sg.add("001");
//	    sg.add("002");
//	    sg.add("003");
//	    sg.add("004");
//		ExpressionLookupFinding f = getExpressionValuesForGeneSymbol("EGFR", "rbinsample.txt", sg, false);
//		
//	}
	
//	public static ExpressionLookupFinding getExpressionValuesForGeneSymbol(String geneSymbol,String rbinaryFileName,SampleGroup samples, boolean computeLogValue) {
//			
//		
//		String sessionId = new Long(System.currentTimeMillis()).toString();
//		String taskId = sessionId + "_1";
//		
//		
//		//Dummy Result for now.
//		ExpressionLookupFinding luf = new ExpressionLookupFinding("1","1", FindingStatus.Completed);
//		
//		DataPointVector v1 = new DataPointVector("15678_at");
//		loadTestVec(v1, samples, computeLogValue);
//		luf.addDataPointVector(v1);
//		
//		DataPointVector v2 = new DataPointVector("15679_at");
//		loadTestVec(v2, samples, computeLogValue);
//		luf.addDataPointVector(v2);
//		
//		DataPointVector v3 = new DataPointVector("15680_at");
//		loadTestVec(v3, samples, computeLogValue);
//		luf.addDataPointVector(v3);
//		
//		DataPointVector v4 = new DataPointVector("15690_at");
//		loadTestVec(v4, samples, computeLogValue);
//		luf.addDataPointVector(v4);
//		
//		DataPointVector v5 = new DataPointVector("15695_at");
//		loadTestVec(v5, samples, computeLogValue);
//		luf.addDataPointVector(v5);
//		
//		return luf;
		
//		
//		//RembrandtFindingsFactory factory = new RembrandtFindingsFactory();
//		Finding finding = null;
//		try {
//			AnalysisServerClientManager mgr = AnalysisServerClientManager.getInstance();
//			mgr.sendRequest(elr);
//			
//			
//			
//			
//			while(finding.getStatus() == FindingStatus.Running){
//				 finding = businessTierCache.getSessionFinding(finding.getSessionId(),finding.getTaskId());			 
//					 try {				
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						logger.error(e);
//					}
//			}
//			return finding;
//			
//			
//			
//			ExpressionLookupResult result = new ExpressionLookupResult(sessionId, taskId);
//			
//			return result;
//	} catch (Exception ex) {
//		
//	}
		
//		ExpressionLookupResult result = new ExpressionLookupResult("1", "1");
//		
//		DataPointVector vec = new DataPointVector("ExpressionValues");
//		DataPoint p = new Data
//		vec.addDataPoint()
//		
//		
//		result.setDataPoints(dataPoints);
//		return result;

//	}
//	
	public static List<ExpressionLookupFinding> getExpressionValuesForReporters(ReporterGroup reporters, String rbinaryFileName, List<SampleGroup> sampleGroups) {
		
		List<ExpressionLookupFinding> findings = new ArrayList<ExpressionLookupFinding>();
		ExpressionLookupFinding finding = null;
		for (SampleGroup sampleGroup : sampleGroups) {
		  finding = getExpressionValuesForReporters(reporters, rbinaryFileName, sampleGroup);
		  findings.add(finding);			
		}
		return findings;
	}
	
	public static ExpressionLookupFinding getExpressionValuesForReporters(ReporterGroup reporters,String rbinaryFileName,SampleGroup samples, String sessionId) {
	    Finding finding = null;
        
        try {
            AnalysisServerClientManager as = AnalysisServerClientManager.getInstance();
                    
            String taskId = new Long(System.currentTimeMillis()).toString();
            
            ExpressionLookupRequest lookupReq = new ExpressionLookupRequest(sessionId, taskId); 
            ExpressionLookupFinding elf = new ExpressionLookupFinding(sessionId,taskId, FindingStatus.Running);
                        
            BusinessTierCache btcache = CacheFactory.getBusinessTierCache();
            btcache.addToSessionCache(sessionId, taskId, elf);
            
            as.setCache(btcache);
            
            lookupReq.setReporters(reporters);
            lookupReq.setSamples(samples);
            lookupReq.setDataFileName(rbinaryFileName);
            
            as.sendRequest(lookupReq);
            finding = btcache.getSessionFinding(sessionId, taskId);
            while(finding.getStatus() == FindingStatus.Running){
                 finding = btcache.getSessionFinding(sessionId,taskId);          
                     try {              
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        logger.error(e);
                    }
            }
                           
        }
        catch (NamingException e) {
            // TODO Auto-generated catch block
            logException(e);
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            logException(e);
        }
        catch (Exception e) {
            logException(e); 
         
        }
        
        return (ExpressionLookupFinding) finding;
        
    }
	
 	
	public static ExpressionLookupFinding getExpressionValuesForReporters(ReporterGroup reporters,String rbinaryFileName,SampleGroup samples) {
		
		Finding finding = null;
		
		try {
			AnalysisServerClientManager as = AnalysisServerClientManager.getInstance();
					
			String sessionId = new Long(System.currentTimeMillis()).toString();
			String taskId = new Long(System.currentTimeMillis()).toString();
			
			ExpressionLookupRequest lookupReq = new ExpressionLookupRequest(sessionId, taskId); 
			ExpressionLookupFinding elf = new ExpressionLookupFinding(sessionId,taskId, FindingStatus.Running);
						
			BusinessTierCache btcache = CacheFactory.getBusinessTierCache();
		    btcache.addToSessionCache(sessionId, taskId, elf);
		    
		    as.setCache(btcache);
		    
		    lookupReq.setReporters(reporters);
		    lookupReq.setSamples(samples);
		    lookupReq.setDataFileName(rbinaryFileName);
		    
		    as.sendRequest(lookupReq);
		    finding = btcache.getSessionFinding(sessionId, taskId);
		    while(finding.getStatus() == FindingStatus.Running){
				 finding = btcache.getSessionFinding(sessionId,taskId);			 
					 try {				
						Thread.sleep(500);
					} catch (InterruptedException e) {
						logger.error(e);
					}
			}
	    	    	       
		}
		catch (NamingException e) {
			// TODO Auto-generated catch block
			logException(e);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			logException(e);
		}
		catch (Exception e) {
		    logException(e); 
		 
		}
		
		return (ExpressionLookupFinding) finding;
	    
	}
	
	public static Collection<CopyNumberFinding> getCopyNumberFindingsForReporters(ReporterGroup reporters,String rbinaryFileName,SampleGroup samples) {
		
		
		AnalysisServerClientManager as;
		try {
			as = AnalysisServerClientManager.getInstance();
		
		
			String sessionId = new Long(System.currentTimeMillis()).toString();
			String taskId = new Long(System.currentTimeMillis()).toString();
			
			CopyNumberLookupRequest lookupReq = new CopyNumberLookupRequest(sessionId, taskId); 
			CopyNumberLookupFinding elf = new CopyNumberLookupFinding(sessionId,taskId, FindingStatus.Running);
						
			BusinessTierCache btcache = CacheFactory.getBusinessTierCache();
		    btcache.addToSessionCache(sessionId, taskId, elf);
		    
		    as.setCache(btcache);
			
			//Do analysis server call to get copy number values.
			
			lookupReq.setDataFileName(rbinaryFileName);
			lookupReq.setReporters(reporters);
			lookupReq.setSamples(samples);
			
			//Create and load copy number findings
			
			//return copy number findings
		
		} catch (NamingException e) {
			logger.error(e);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		
		return Collections.emptyList();
	}
	
	private static void logException(Exception ex) {
		 StringWriter sw = new StringWriter();
		 PrintWriter pw = new PrintWriter(sw);
		 ex.printStackTrace(pw);
		 logger.error(sw.toString());	
	}

}
