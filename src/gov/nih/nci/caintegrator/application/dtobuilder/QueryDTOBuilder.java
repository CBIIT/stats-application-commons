package gov.nih.nci.caintegrator.application.dtobuilder;

import gov.nih.nci.caintegrator.dto.query.QueryDTO;


/**
 * This is the QueryBuilder interface.  It will take in an object and a string
 * representing the presentationTierCache id from which to retrieve
 * any user lists that it needs and builds a QueryDTO from the fields on that object.
 *
 * @author caIntegrator Team
 */

public interface QueryDTOBuilder {
    
    /**
     * takes the form and a string representing the cacheId of
     * the presentationTierCache. If a dto does not need to have
     * access to any objects (e.g. user lists) than pass in
     * a null for the cacheId. The presentationTier cache
     * is retrieved in this way either through list management
     * or calling it directly.
     * @param form
     * @param cacheId
     * @return
     */
    public QueryDTO buildQueryDTO(Object form, String cacheId);
}
