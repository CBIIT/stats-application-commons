package gov.nih.nci.caintegrator.application.service;

import gov.nih.nci.caintegrator.studyQueryService.dto.ihc.LevelOfExpressionIHCFindingCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.ihc.LossOfExpressionIHCFindingCriteria;
import gov.nih.nci.caintegrator.studyQueryService.dto.study.SpecimenCriteria;
import gov.nih.nci.caintegrator.studyQueryService.ihc.LossOfExpressionIHCFindingHandler;

import java.util.Collection;
import java.util.Set;


public class LossOfExpressionIHCService {
    private static LossOfExpressionIHCService ourInstance = null;
   
    
    /**
     * Singleton for the database based query service
     * 
     * @return the static instance of the query service
     */
    public static LossOfExpressionIHCService getInstance()
    {        
        if (ourInstance == null)
        {
            ourInstance = new LossOfExpressionIHCService();
        }
        return ourInstance;
    }
    
    /**
     * accepts a LOE Criteria , retrieves findings from db. Assumes a tables mapped to appropriate
     * object fields. -KR
     * @param criteria
     * @return theFindings
     */
    public Collection<? extends gov.nih.nci.caintegrator.domain.finding.bean.Finding> getFindings(LossOfExpressionIHCFindingCriteria criteria){
        LossOfExpressionIHCFindingHandler theHandler = criteria.getHandler();
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
        LossOfExpressionIHCFindingCriteria criteria = new LossOfExpressionIHCFindingCriteria();
        theSPCriteria.setSpecimenIdentifierCollection(sampleIds);
        criteria.setSpecimenCriteria(theSPCriteria);        
        Collection<? extends gov.nih.nci.caintegrator.domain.finding.bean.Finding> theFindings = getFindings(criteria);
        return theFindings;
     
    }


}
