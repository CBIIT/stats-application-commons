package gov.nih.nci.caintegrator.application.test;

import gov.nih.nci.caintegrator.analysis.messaging.CorrelationRequest;
import gov.nih.nci.caintegrator.analysis.messaging.DoubleVector;
import gov.nih.nci.caintegrator.application.analysis.AnalysisServerClientManager;
import gov.nih.nci.caintegrator.application.cache.CacheFactory;
import gov.nih.nci.caintegrator.enumeration.CorrelationType;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;

public class TestCorrelationAnalysisRequest {
	
	AnalysisServerClientManager analysisServerClientMgr;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestCorrelationAnalysisRequest tr = new TestCorrelationAnalysisRequest();
		tr.initialize();
		tr.sendRequest();

	}
	
	public void sendRequest() {
		
		CorrelationRequest corrRequest = new CorrelationRequest("1","1");
		
		
		double[] gx1 = {-0.331,
						-1,
						-0.392,
						-1.008,
						-1.849,
						0.019,
						-0.06,
						0.429,
						0.409,
						1.194,
						-1.103,
						1.288,
						-0.755,
						-0.18,
						-0.836,
						0.906,
						1.434,
						0.067,
						0.466,
						0.133,
						0.365,
						1.091,
						-0.949,
						-1.502,
						-0.739,
						-0.025,
						1.38,
						2.056,
						-0.598,
						1.219,
						-0.256,
						0.021,
						-1.059,
						-1.545,
						0.583,
						-0.469,
						2.478,
						1.146,
						0.727,
						-0.461,
						0.107,
						-0.325,
						-0.033,
						0.417};
		
		
		double[] gx2 = 
		{0,
		-1.06,
		-0.03,
		-0.46,
		-2,
		0.01,
		0.95,
		1.48,
		-0.77,
		0.3,
		0.16,
		1.03,
		0.61,
		0.03,
		-1.5,
		0.36,
		3.17,
		-0.89,
		0.42,
		1.41,
		-0.62,
		2.57,
		-1.13,
		-0.92,
		-0.66,
		-0.5,
		3.48,
		2.71,
		-0.22,
		0.94,
		1.56,
		0.56,
		-0.88,
		0.28,
		0.73,
		0.23,
		1.55,
		1.04,
		0.94,
		-0.9,
		0.28,
		0.36,
		-1.81,
		0.76};

	
		
	    List<Double> gx1List = new ArrayList<Double>();
	    List<Double> gx2List = new ArrayList<Double>();
	    
	    for (int i=0;i<gx1.length;i++) {
	      gx1List.add(gx1[i]);
	    }
	    
	    for (int j=0; j<gx2.length;j++) {
	      gx2List.add(gx2[j]);
	    }

	    DoubleVector v1 = new DoubleVector("GX1", gx1List);
	    DoubleVector v2 = new DoubleVector("GX2", gx2List);
	    
	    
		//corrRequest.setVector1(v1);
		//corrRequest.setVector2(v2);
		
		corrRequest.setCorrelationType(CorrelationType.PEARSON);
		
		try {
			analysisServerClientMgr.sendRequest(corrRequest);
		} catch (JMSException e) {
			e.printStackTrace(System.out);
		}
		
	}
	
	public void initialize() {
		
		
		try {
			analysisServerClientMgr = AnalysisServerClientManager.getInstance();
			analysisServerClientMgr.setCache(CacheFactory.getBusinessTierCache());
			
			String jmsProviderURL = "localhost:1099";
			String jndiFactoryName = "ConnectionFactory";
			String requestQueueName = "queue/SharedAnalysisRequest";
			String responseQueueName = "queue/ISPYanalysisResponse";
			   
			 analysisServerClientMgr.setJMSparameters(jmsProviderURL, jndiFactoryName, requestQueueName, responseQueueName);
			   
			 analysisServerClientMgr.establishQueueConnection();
		
			 System.out.println("Successfully established connection with JMS provider");
			 
			 
			 
		}
		catch (Exception ex) {
		  ex.printStackTrace(System.out);	
		
		}
		
	}

}
