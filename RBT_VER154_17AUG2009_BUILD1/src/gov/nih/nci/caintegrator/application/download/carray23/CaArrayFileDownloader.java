package gov.nih.nci.caintegrator.application.download.carray23;
import gov.nih.nci.caarray.domain.file.FileType;
import gov.nih.nci.caarray.external.v1_0.CaArrayEntityReference;
import gov.nih.nci.caarray.external.v1_0.data.DataFile;
import gov.nih.nci.caarray.external.v1_0.data.FileTypeCategory;
import gov.nih.nci.caarray.external.v1_0.experiment.Experiment;
import gov.nih.nci.caarray.external.v1_0.query.BiomaterialSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.ExperimentSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.query.FileDownloadRequest;
import gov.nih.nci.caarray.external.v1_0.query.FileSearchCriteria;
import gov.nih.nci.caarray.external.v1_0.sample.Biomaterial;
import gov.nih.nci.caarray.external.v1_0.sample.BiomaterialType;
import gov.nih.nci.caarray.services.ServerConnectionException;
import gov.nih.nci.caarray.services.external.v1_0.InvalidReferenceException;
import gov.nih.nci.caarray.services.external.v1_0.UnsupportedCategoryException;
import gov.nih.nci.caarray.services.external.v1_0.data.DataApiUtils;
import gov.nih.nci.caarray.services.external.v1_0.data.DataTransferException;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchApiUtils;
import gov.nih.nci.caarray.services.external.v1_0.search.SearchService;
import gov.nih.nci.caintegrator.application.download.DownloadZipHelper;
import gov.nih.nci.caintegrator.application.zip.ZipItem;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;

/**
 * Imports caArray files from an experiment into a zip file for caIntegrator usage.
 * Based on CaArray2Importer created by GenePattern Team at Broad.
 * 
 * @author sahnih
 * 
 */
public class CaArrayFileDownloader  {

	public static Logger logger = Logger.getLogger(CaArrayFileDownloader.class);

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
	 * Writes ZipItems to a Zip file
	 * @param zipItems
	 * @param zipFileName
	 * @throws IOException
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
	 * @throws DataTransferException 
	 * @throws InvalidReferenceException 
	 */
	public Set<ZipItem> downloadFiles(SearchService searchService,DataApiUtils dataServiceHelper,
			 List<CaArrayEntityReference> fileRefs, String zipFileName ) throws IOException, InvalidReferenceException, DataTransferException {
		Set<ZipItem> zipItems = new HashSet<ZipItem>();
		logger.debug("downloading " + fileRefs.size() + " files");
		boolean compressFile = false;
		for (CaArrayEntityReference fileRef : fileRefs) {
			DataFile file = (DataFile) searchService.getByReference(fileRef);
			BufferedOutputStream bos = null;
			try {
				// if the file does not already exists than write it
				if(!DownloadZipHelper.checkIfFileExists(file.getName(), file.getUncompressedSize(), inputDirectory)){
					File tempFile = DownloadZipHelper.createFile(file.getName(), inputDirectory);
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			        long startTime = System.currentTimeMillis();
			        dataServiceHelper.copyFileContentsToOutputStream(fileRef, compressFile, outStream);
			        long totalTime = System.currentTimeMillis() - startTime;
			        byte[] byteArray = outStream.toByteArray();
					logger.debug("downloaded file: " + file.getName());					
					bos = new BufferedOutputStream(new FileOutputStream(tempFile));
					logger.debug("writing file: " + tempFile.getName());
					bos.write(byteArray);
					logger.debug("wrote file: " + tempFile.getName());
					if (byteArray != null) {
				            System.out.println("Retrieved " + byteArray.length + " bytes in " + totalTime + " ms.");
				     } else {
				            System.out.println("Error: Retrieved null byte array.");
				     }
				}
			} catch (IOException e) {
				reportError("Error writing temporary file: "
						+ file.getName(), e);
				throw e;
			} finally {
				if (null != bos)
					bos.close();
			}
			ZipItem zipItem = DownloadZipHelper.fileToZip(file.getName(), zipFileName, directoryInZip, inputDirectory);
			zipItems.add(zipItem);
		}

		return zipItems;
	}

	/**
	 * Tries to find an experiment with a publicIdentifier equal to the
	 * experimentName. If it can't find one, then it tries to find an experiment
	 * with a title equal to the experimentName. If it doesn't find one, then it
	 * throws an exception.
	 * 
	 * @param service
	 * @return experiment with publicIdentifier or title equal to the
	 *         experimentName.
	 * @throws UnsupportedCategoryException 
	 * @throws InvalidReferenceException 
	 */
	public CaArrayEntityReference findExperiment(SearchService searchService,
			String experimentPublicIdentifier) throws InvalidReferenceException, UnsupportedCategoryException {
		logger.debug("searching by public identifier for: [" + experimentPublicIdentifier
				+ "]");
        // Search for experiment with the given public identifier.
		ExperimentSearchCriteria experimentSearchCriteria = new ExperimentSearchCriteria();
        experimentSearchCriteria.setPublicIdentifier(experimentPublicIdentifier);
		// Look up experiment.
        List<Experiment> experimentList = (searchService.searchForExperiments(experimentSearchCriteria, null)).getResults();
        if (experimentList == null || experimentList.size() <= 0) {
        	experimentSearchCriteria = new ExperimentSearchCriteria();
        	experimentSearchCriteria.setTitle(experimentPublicIdentifier);
        	// Search for experiment with the given public identifier as the Title.
            experimentList = (searchService.searchForExperiments(experimentSearchCriteria, null)).getResults();
			logger.debug("searching by name for: [" + experimentPublicIdentifier + "]");
			if (experimentList == null || experimentList.size() <= 0) {
				String message = "Experiment not found: " + experimentPublicIdentifier;
				reportError(message, null);
				throw new IllegalArgumentException(message);
			}
		}

		if (experimentList.size() > 1) {
			StringBuffer buf = new StringBuffer();
			buf.append("Found multiple files matching name: ");
			buf.append(experimentPublicIdentifier);
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
		// Assuming that only one experiment was found, pick the first result.
        // This will always be true for a search by public identifier, but may not be true for a search by title.
        Experiment experiment = experimentList.iterator().next();
        return experiment.getReference();
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
    /**
     * Search for samples based on name.
     */
    private Set<CaArrayEntityReference> searchForSamples(SearchApiUtils searchServiceHelper, CaArrayEntityReference experimentRef, List<String> specimenList) throws RemoteException,
            InvalidReferenceException, UnsupportedCategoryException {
        BiomaterialSearchCriteria criteria = new BiomaterialSearchCriteria();
        criteria.setExperiment(experimentRef);
        criteria.getTypes().add(BiomaterialType.SAMPLE);
        List<Biomaterial> samples = (searchServiceHelper.biomaterialsByCriteria(criteria)).list();
        Set<CaArrayEntityReference> sampleRefs = new HashSet<CaArrayEntityReference>();
        for (Biomaterial sample : samples) {
        	 for (String specimenName : specimenList) {
        		 if (sample != null && sample.getName() != null
 						&& sample.getName().contains(specimenName.trim())) {
        			 	sampleRefs.add(sample.getReference());
        		 }
             }
            
        }
        return sampleRefs;
    }
    public List<CaArrayEntityReference> selectFilesFromSamples(SearchApiUtils searchServiceHelper,CaArrayEntityReference experimentRef,List<String> specimenList, FileType type) throws RemoteException, InvalidReferenceException, UnsupportedCategoryException {
        Set<CaArrayEntityReference> sampleRefs = searchForSamples(searchServiceHelper, experimentRef, specimenList);
        if (sampleRefs == null || sampleRefs.size() <= 0) {
            String message = "Could not find the requested samples.";
			reportError(message, null);
            return null;
        }
        List<CaArrayEntityReference> fileRefs = null;
        if (type == FileType.AFFYMETRIX_CEL) {
        	fileRefs = selectCelFilesFromSamples(searchServiceHelper, experimentRef, sampleRefs);
		} else if (type == FileType.AFFYMETRIX_CHP) {
			fileRefs = selectChpFilesFromSamples(searchServiceHelper, experimentRef, sampleRefs);
		}
        if (fileRefs == null) {
            String message = "Could not find any raw files associated with the given samples.";
			reportError(message, null);
            return null;
        } else {
            return fileRefs;
        }
    }
    /**
     * Select all raw data files associated with the given samples.
     */
    private List<CaArrayEntityReference> selectCelFilesFromSamples(SearchApiUtils searchServiceHelper, CaArrayEntityReference experimentRef,
            Set<CaArrayEntityReference> sampleRefs) throws RemoteException, InvalidReferenceException {
    	FileSearchCriteria fileSearchCriteria = new FileSearchCriteria();
        fileSearchCriteria.setExperiment(experimentRef);
        fileSearchCriteria.setExperimentGraphNodes(sampleRefs);
        fileSearchCriteria.getCategories().add(FileTypeCategory.RAW);
        fileSearchCriteria.setExtension("CEL");
        
        List<DataFile> files = (searchServiceHelper.filesByCriteria(fileSearchCriteria)).list();
        if (files.size() <= 0) {
            return null;
        }

        // Return references to the files.
        List<CaArrayEntityReference> fileRefs = new ArrayList<CaArrayEntityReference>();
        for (DataFile file : files) {
            System.out.print(file.getName() + "  ");
            fileRefs.add(file.getReference());
        }
        return fileRefs;
    }
    /**
     * Select all chp data files associated with the given samples.
     */
    private List<CaArrayEntityReference> selectChpFilesFromSamples(SearchApiUtils searchServiceHelper, CaArrayEntityReference experimentRef,
            Set<CaArrayEntityReference> sampleRefs) throws RemoteException, InvalidReferenceException {
        FileSearchCriteria fileSearchCriteria = new FileSearchCriteria();
        fileSearchCriteria.setExperiment(experimentRef);
        fileSearchCriteria.setExperimentGraphNodes(sampleRefs);
        fileSearchCriteria.getCategories().add(FileTypeCategory.DERIVED);
        fileSearchCriteria.setExtension("CHP");
        
        List<DataFile> files = (searchServiceHelper.filesByCriteria(fileSearchCriteria)).list();
        if (files.size() <= 0) {
            return null;
        }

        // Return references to the files.
        List<CaArrayEntityReference> fileRefs = new ArrayList<CaArrayEntityReference>();
        for (DataFile file : files) {
            System.out.print(file.getName() + "  ");
            fileRefs.add(file.getReference());
        }
        return fileRefs;
    }

	 /**
     * Download a zip of the given files.
     */
    public File downloadZipOfFiles(DataApiUtils dataServiceHelper, List<CaArrayEntityReference> fileRefs, String zipFileName ) throws RemoteException,
            MalformedURIException, IOException, Exception {
        FileDownloadRequest downloadRequest = new FileDownloadRequest();
        downloadRequest.setFiles(fileRefs);
        boolean compressEachIndividualFile = false;
        File zipFile = new File(outputZipDirectory+File.separator+zipFileName);
        long startTime = System.currentTimeMillis();
        dataServiceHelper.downloadFileContentsZipToFile(downloadRequest, compressEachIndividualFile, zipFile);
        long totalTime = System.currentTimeMillis() - startTime;
        if (zipFile != null  && zipFile.exists()  && zipFile.length() > 0) {
            System.out.println("Retrieved " + zipFile.length() + " bytes in " + totalTime + " ms.");
        } else {
            System.err.println("Error: Retrieved null byte array.");
        }
        return zipFile;
    }

}
