package gov.nih.nci.caintegrator.application.service;

import gov.nih.nci.caintegrator.studyQueryService.dto.ihc.LevelOfExpressionIHCFindingCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.study.SpecimenCriteria;
import gov.nih.nci.caintegrator.studyQueryService.ihc.LevelOfExpressionIHCFindingHandler;

import java.util.Collection;
import java.util.Set;


public class LevelOfExpressionIHCService {
    private static LevelOfExpressionIHCService ourInstance = null;
   
    
    /**
     * Singleton for the database based query service
     * 
     * @return the static instance of the query service
     */
    public static LevelOfExpressionIHCService getInstance()
    {        
        if (ourInstance == null)
        {
            ourInstance = new LevelOfExpressionIHCService();
        }
        return ourInstance;
    }
    
    /**
     * accepts a LOE Criteria , retrieves findings from db. Assumes a tables mapped to appropriate
     * object fields. -KR
     * @param criteria
     * @return theFindings
     */
    public Collection<? extends gov.nih.nci.caintegrator.domain.finding.bean.Finding> getFindings(LevelOfExpressionIHCFindingCriteria criteria){
        LevelOfExpressionIHCFindingHandler theHandler = criteria.getHandler();
        Collection<? extends gov.nih.nci.caintegrator.domain.finding.bean.Finding> theFindings = theHandler.getLevelExpFindings(criteria);
        return theFindings;
    }
    
    /**
     * Convenience method that takes sampleIds, builds specimen criteria itself and fetches findings
     * -KR
     * @param sampleIds
     * @return theFindings
     */
    public Collection<? extends gov.nih.nci.caintegrator.domain.finding.bean.Finding> getFindingsFromSampleIds(Set<String> sampleIds){
        SpecimenCriteria theSPCriteria = new SpecimenCriteria();
        LevelOfExpressionIHCFindingCriteria criteria = new LevelOfExpressionIHCFindingCriteria();
        theSPCriteria.setSpecimenIdentifierCollection(sampleIds);
        criteria.setSpecimenCriteria(theSPCriteria);        
        Collection<? extends gov.nih.nci.caintegrator.domain.finding.bean.Finding> theFindings = getFindings(criteria);
        return theFindings;
     
    }


}
