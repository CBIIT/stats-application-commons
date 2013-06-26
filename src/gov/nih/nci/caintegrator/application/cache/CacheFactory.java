/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.cache;




public class CacheFactory{


	public static PresentationTierCache getPresentationTierCache() {
		return PresentationCacheManager.getInstance();
	}
	
	public static BusinessTierCache getBusinessTierCache() {
		return BusinessCacheManager.getInstance();
	}
	


}
