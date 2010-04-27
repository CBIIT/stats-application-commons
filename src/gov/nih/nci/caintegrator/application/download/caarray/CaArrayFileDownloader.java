package gov.nih.nci.caintegrator.application.download.caarray;
import gov.nih.nci.caarray.domain.data.DerivedArrayData;
import gov.nih.nci.caarray.domain.data.RawArrayData;
import gov.nih.nci.caarray.domain.file.CaArrayFile;
import gov.nih.nci.caarray.domain.file.FileType;
import gov.nih.nci.caarray.domain.hybridization.Hybridization;
import gov.nih.nci.caarray.domain.project.Experiment;
import gov.nih.nci.caarray.domain.sample.Extract;
import gov.nih.nci.caarray.domain.sample.LabeledExtract;
import gov.nih.nci.caarray.domain.sample.Sample;
import gov.nih.nci.caarray.services.ServerConnectionException;
import gov.nih.nci.caarray.services.file.FileRetrievalService;
import gov.nih.nci.caarray.services.search.CaArraySearchService;
import gov.nih.nci.caintegrator.application.download.DownloadZipHelper;
import gov.nih.nci.caintegrator.application.zip.ZipItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;

/**
 * Imports caArray files from an experiment into a zip file for caIntegrator usage.
 * Based on CaArray2Importer created by GenePattern Team at Broad.
 * 
 * @author sahnih
 * 
 */
public class CaArrayFileDownloader  {

	public static Logger logger = Logger.getLogger(CaArrayFileDownloader.class);

	/**
	 * Extension of data files requested. Default is CEL.
	 */
	private String inputDirectory;
	private String outputZipDirectory;
	private String directoryInZip;
	public CaArrayFileDownloader(String inputDirectory,
			String outputZipDirectory, String directoryInZip) {
		super();
		this.inputDirectory = inputDirectory;
		this.outputZipDirectory = outputZipDirectory;
		this.directoryInZip = directoryInZip;
	}


	/**
	 * Tries to find an experiment with a publicIdentifier equal to the
	 * experimentName. If it doesn't find one with a matching public identifier,
	 * then it tries to find one with title equal to the experimentName. If it
	 * doesn't find one, throws exception. Otherwise, tries to download data
	 * files from the experiment that it found.
	 * 
	 * @throws ServerConnectionException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws LoginException
	 */

	public List<String> writeZipFile(Set<ZipItem> zipItems, String zipFileName) throws IOException {
		return DownloadZipHelper.zipFile(new ArrayList<ZipItem>(zipItems), zipFileName, outputZipDirectory, true);
	}

	/**
	 * Downloads files to temporary files.
	 * 
	 * @param fileService
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public Set<ZipItem> downloadFiles(FileRetrievalService fileService,
		Set<CaArrayFile> files, String zipFileName) throws IOException {
		Set<ZipItem> zipItems = new HashSet<ZipItem>();
		logger.debug("downloading " + files.size() + " files");
		int i = 1;
		for (CaArrayFile file : files) {
			File tempFile = DownloadZipHelper.createFile(file.getName(), inputDirectory);
			logger.debug("downloading file: " + i + " out of " + files.size());
			logger.debug("downloading file: " + file.getName());
			long startTime = System.currentTimeMillis();
			byte[] byteArray = fileService.readFile(file);
			long totalTime = System.currentTimeMillis() - startTime;
			logger.debug("downloaded file: " + file.getName());
			if (byteArray != null) {
	            System.out.println("Retrieved " + byteArray.length + " bytes in " + totalTime + " ms.");
		     } else {
		            System.out.println("Error: Retrieved null byte array.");
		     }
			i++;

			BufferedOutputStream bos = null;
			try {
				// if the file does not already exists than write it
				if(!DownloadZipHelper.checkIfFileExists(tempFile.getName(), byteArray.length, inputDirectory)){
					bos = new BufferedOutputStream(new FileOutputStream(tempFile));
					logger.debug("writing file: " + tempFile.getName());
					bos.write(byteArray);
					logger.debug("wrote file: " + tempFile.getName());
				}
			} catch (IOException e) {
				reportError("Error writing temporary file: "
						+ tempFile.getName(), e);
				throw e;
			} finally {
				if (null != bos)
					bos.close();
			}
			ZipItem zipItem = DownloadZipHelper.fileToZip(tempFile.getName(), zipFileName, directoryInZip, inputDirectory);
			zipItems.add(zipItem);
		}

		return zipItems;
	}

	/**
	 * Retrieves all the data files for the requested specimens in the
	 * experiment.
	 * 
	 * @param service
	 * @param experiment
	 * @throws IllegalArgumentException
	 *             if no hybridizations are found for the experiment;
	 * @return
	 */

	/**
	 * Tries to find an experiment with a publicIdentifier equal to the
	 * experimentName. If it can't find one, then it tries to find an experiment
	 * with a title equal to the experimentName. If it doesn't find one, then it
	 * throws an exception.
	 * 
	 * @param service
	 * @return experiment with publicIdentifier or title equal to the
	 *         experimentName.
	 */
	public Experiment findExperiment(CaArraySearchService service,
			String experimentName) {
		logger.debug("searching by public identifier for: [" + experimentName
				+ "]");
		Experiment exampleExperiment = new Experiment();
		exampleExperiment.setPublicIdentifier(experimentName);
		// Look up experiment.
		List<Experiment> experimentList = service.search(exampleExperiment);

		if (experimentList.size() == 0) {
			exampleExperiment = new Experiment();
			exampleExperiment.setTitle(experimentName);
			experimentList = service.search(exampleExperiment);
			logger.debug("searching by name for: [" + experimentName + "]");
			if (experimentList.size() == 0) {
				String message = "Experiment not found: " + experimentName;
				reportError(message, null);
				throw new IllegalArgumentException(message);
			}
		}

		if (experimentList.size() > 1) {
			StringBuffer buf = new StringBuffer();
			buf.append("Found multiple files matching name: ");
			buf.append(experimentName);
			buf.append("\n");
			for (Experiment experiment : experimentList) {
				buf.append("\t: ");
				buf.append(experiment.getTitle());
				buf.append("\n");
			}
			buf.append("downloading experiment: ");
			buf.append(experimentList.get(0).getTitle());
			String message = new String(buf);
			reportError(message, null);
		}
		return experimentList.get(0);
	}



	/**
	 * Application is called as: java CaArrayFileDownloader -u <url to carray>
	 * -x <experiment>
	 * 
	 * with optional arguments:
	 * 
	 * -n <username> -p <password> -t <type> -e <extension>
	 * 
	 * If -u is specified, then -p must also be specified
	 * 
	 * Possible values for -t are "raw" or "derived"
	 * 
	 * -e is the extension for the files. The default is "CEL"
	 * 
	 * @param args
	 * @throws ServerConnectionException
	 * @throws ServerConnectionException
	 */
	public static void main(String[] args) {
		CaArrayFileDownloader importer = null;
		try {
			importer = new CaArrayFileDownloader(args[0],args[1],args[3]);

		} catch (Exception e) {
			reportError(e.getMessage(), null);
			e.printStackTrace(System.err);
			System.exit(1);
		}

		// try {
		// importer.downloadExperiment();
		// } catch (ServerConnectionException e) {
		// System.exit(2);
		// } catch (IllegalArgumentException e) {
		// System.exit(3);
		// } catch (IOException e) {
		// System.exit(4);
		// } catch (LoginException e) {
		// System.exit(5);
		// }
		// System.exit(0);
	}

	protected static void reportError(String message, Throwable e) {
		if (null == message)
			message = "";
		logger.error(message, e);
		String e_message = "";
		if (null != e)
			e_message = e.getMessage();
		System.err.println(message + ": " + e_message);
	}

	// Get all raw data file (CaArrayFile) objects.
	private Set<Hybridization> getAllHybridizationsForSamples(CaArraySearchService service,
			Experiment experiment, List<String> specimenList) {
		Set<Hybridization> allHybridizations = new HashSet<Hybridization>();
		Set<Sample> allSamples = experiment.getSamples();
		//Sample->Extract->LabeledExtract->Hybridization->RawArrayData or DerivedArrayData.
		for (Sample sample : allSamples) {
			for (String specimenName : specimenList) {
				if (sample != null && sample.getName() != null
						&& sample.getName().contains(specimenName.trim())) {
					Sample populatedSample = service.search(sample).get(0);
					System.out.println("Sample Name: "+populatedSample.getName());
					Set<Extract> allExtracts = populatedSample.getExtracts();
					for(Extract extract:allExtracts){
						Extract populatedExtract = service.search(extract).get(0);
						Set<LabeledExtract> allLabeledExtracts = populatedExtract.getLabeledExtracts();
						for(LabeledExtract labeledExtract:allLabeledExtracts){
							LabeledExtract populatedLabeledExtract = service.search(labeledExtract).get(0);
							allHybridizations.addAll(populatedLabeledExtract.getHybridizations());
						}
					}
				}
			}
		}
		return allHybridizations;
	}
	// Get all raw data file (CaArrayFile) objects.
	public Set<CaArrayFile> getAllDataFiles(CaArraySearchService service,
			Experiment experiment, List<String> specimenList, FileType type) {
		Set<Hybridization> allHybridizations = getAllHybridizationsForSamples( service,
				 experiment,  specimenList);
		Set<CaArrayFile> files = new HashSet<CaArrayFile>();
		int i = 1;
		for (Hybridization hybridization : allHybridizations) {
			Hybridization populatedHybridization = service
					.search(hybridization).get(0);
			Set<CaArrayFile> fileset = getDataFile(service,
					populatedHybridization, type);
			files.addAll(fileset);
			i++;
		}
		return files;
	}

	private Set<CaArrayFile> getDataFile(CaArraySearchService service,
			Hybridization hybridization, FileType type) {
		Set<CaArrayFile> files = new HashSet<CaArrayFile>();
		if (type == FileType.AFFYMETRIX_CEL) {
			files.addAll(getRawDataFiles(service, hybridization));
		} else if (type == FileType.AFFYMETRIX_CHP) {
			files.addAll(getDerivedDataFile(service, hybridization));
		}
		return files;
	}


	private Set<CaArrayFile> getDerivedDataFile(CaArraySearchService service,
			Hybridization hybridization) {
		Set<CaArrayFile> files = new HashSet<CaArrayFile>();
		Set<DerivedArrayData> derivedArrayDataSet = hybridization
				.getDerivedDataCollection();
		for (DerivedArrayData derivedArrayData : derivedArrayDataSet) {
				// Return the file associated with the first raw data.
				DerivedArrayData populatedArrayData = service.search(derivedArrayData).get(0);
				CaArrayFile file = populatedArrayData.getDataFile();
				if (null != file){
					files.add(file);
				}
			}
		return files;
	}

	private Set<CaArrayFile> getRawDataFiles(CaArraySearchService service,
			Hybridization hybridization) {
		Set<CaArrayFile> files = new HashSet<CaArrayFile>();
		Set<RawArrayData> rawArrayDataSet = hybridization.getRawDataCollection() ;
		for (RawArrayData rawArrayData : rawArrayDataSet) {
			// Return the file associated with the first raw data.
			RawArrayData populatedArrayData = service.search(rawArrayData).get(0);
			CaArrayFile file = populatedArrayData.getDataFile();
			if (null != file){
				files.add(file);
			}
		}
	return files;
	
	}


}
