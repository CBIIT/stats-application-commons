/*L
 *  Copyright SAIC
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/stats-application-commons/LICENSE.txt for details.
 */

package gov.nih.nci.caintegrator.application.bean;

import gov.nih.nci.caintegrator.service.findings.Finding;

import java.io.Serializable;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.dom4j.Document;





public class FindingReportBean implements Serializable {
	
	private Document xmlDoc;
	private gov.nih.nci.caintegrator.service.findings.Finding finding;
    private Document xmlDocCSV;
    private HSSFWorkbook excelDoc;
	
	public FindingReportBean()	{}

	

    /**
     * @return Returns the excelDoc.
     */
    public HSSFWorkbook getExcelDoc() {
        return excelDoc;
    }



    /**
     * @param excelDoc The excelDoc to set.
     */
    public void setExcelDoc(HSSFWorkbook excelDoc) {
        this.excelDoc = excelDoc;
    }



    public Finding getFinding() {
		return finding;
	}

	public void setFinding(Finding finding) {
		this.finding = finding;
	}

	public Document getXmlDoc() {
		return xmlDoc;
	}

	public void setXmlDoc(Document xmlDoc) {
		this.xmlDoc = xmlDoc;
	}

	public Document getXmlDocCSV() {
		return xmlDocCSV;
	}

	public void setXmlDocCSV(Document xmlDocCSV) {
		this.xmlDocCSV = xmlDocCSV;
	}

    }
