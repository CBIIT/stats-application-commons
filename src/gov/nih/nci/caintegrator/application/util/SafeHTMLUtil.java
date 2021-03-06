/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.util;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.util.Translate;

public class SafeHTMLUtil {

	public static String clean(String s) {
		String clean = Translate.decode(s).replace("<", "").replace(">", "");
		if(!clean.contains("description") && !clean.contains("Description") ){
		clean = StringUtils.replace(clean, "script", "");
		}
		clean = StringUtils.replace(clean, "%", "");
		clean = StringUtils.replace(clean, "#", "");
		clean = StringUtils.replace(clean, ";", "");
		clean = StringUtils.replace(clean, "'", "");
		clean = StringUtils.replace(clean, "\"", "");
		clean = StringUtils.replace(clean, "$", "");
		clean = StringUtils.replace(clean, "&", "");
		clean = StringUtils.replace(clean, "(", "");
		clean = StringUtils.replace(clean, ")", "");
		clean = StringUtils.replace(clean, "/", "");
		clean = StringUtils.replace(clean, "\\", "");
		if (clean.length() == 0) {
			clean = "unamedQuery";
		}
		return clean;

	}
}
