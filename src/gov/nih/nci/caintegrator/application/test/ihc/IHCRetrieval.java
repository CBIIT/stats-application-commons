package gov.nih.nci.caintegrator.application.test.ihc;

//import gov.nih.nci.caintegrator.application.service.LevelOfExpressionIHCService;
import gov.nih.nci.caintegrator.application.service.LevelOfExpressionIHCService;
import gov.nih.nci.caintegrator.util.HibernateUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;

public class IHCRetrieval {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Session theSession2 = null;
        System.setProperty("gov.nih.nci.caintegrator.configFile","C:/local/content/ispyportal/config/caIntegratorConfig.xml");


//        StringBuilder theHQL = new StringBuilder();
//        // Start of the where clause
//        theHQL.append("from ProteinBiomarker AS p  ");
//        Session theSession = HibernateUtil.getSession();
//        theSession.beginTransaction();
//        Query theQuery = theSession.createQuery(theHQL.toString());
//        System.out.println("HQL: " + theHQL.toString());
//        long theStartTime = System.currentTimeMillis();
//        Collection objs = theQuery.list();
       try{ 
        theSession2 = HibernateUtil.getSession();
        theSession2.beginTransaction();
        String theHQL = "";
        Query theQuery = null;
        Collection objs = null;
//        theHQL = "select distinct loe.stainIntensity from LevelOfExpressionIHCFinding loe where loe.stainIntensity!=null";
//        theQuery = theSession2.createQuery(theHQL);
//        System.out.println("HQL: " + theHQL);        
//        objs = theQuery.list();
//        ArrayList<String> intensityValues = new ArrayList<String>(objs);
        theHQL = "select distinct loe.stainLocalization from LevelOfExpressionIHCFinding loe where loe.stainLocalization!=null";
        theQuery = theSession2.createQuery(theHQL);
        System.out.println("HQL: " + theHQL);        
        objs = theQuery.list();
        ArrayList<String> localeValues = new ArrayList<String>(objs);
        Set<String> mySamples = new HashSet<String>();
        mySamples.add("220268");
        mySamples.add("220065");
        mySamples.add("224534");
        LevelOfExpressionIHCService loes = LevelOfExpressionIHCService.getInstance();
        Collection<? extends gov.nih.nci.caintegrator.domain.finding.bean.Finding> myFindings = loes.getFindingsFromSampleIds(mySamples);
        System.out.println("fetched finding for sampleIds");
    }finally
    {
        // Close the session if necessart
        if (theSession2 != null)
        {
            theSession2.close();
        }
    }
    }

}
