package ht1.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ht1.file.exception.FileException;
import ht1.manager.MsgManager;

/**
 * Class <code>FileWorker<code> is responsible for
 * read test instructions from input file and
 * writing test results to output file
 */
public class FileWorker {

	//to read input file with test instructions
	private BufferedReader inputFileReader;
	//to write test results to output file
	private BufferedWriter outputFileWriter;
	private static Logger logger = Logger.getLogger(FileWorker.class);
	private final String INPUT_FILE_NOT_FOUND = "error.input.not.found";
	private final String INPUT_READ_ERROR = "error.input.read";
	private final String OUTPUT_READ_ERROR = "error.output.read";
	private final String OUTPUT_NOT_A_FILE = "error.output.not.file";
	private final String OUTPUT_NO_ACCESS = "error.output.no.access";
	private final String INPUT_NOT_A_FILE = "error.input.not.file";
	private final String INPUT_NOT_EXISTS = "error.input.not.exists";
	private final String READER_NOT_CLOSED = "warn.reader.not.closed";
	private final String WRITER_NOT_CLOSED = "warn.writer.not.closed";

	/**
	 * Constructor creates an instance of <code>FileWorker</code>
	 * @param inputFileName - input file name with test instructions
	 * @param outputFileName - output file name for writing test results
	 * @throws FileException
	 */
	public FileWorker(String inputFileName, String outputFileName) throws FileException {

		if (!inputFileName.isEmpty() && inputFileName != null && !outputFileName.isEmpty() && outputFileName != null) {
			//create input file with test instructions and output result file
			File inputFile = createFileFromArgPath(inputFileName);
			File outputFile = createFileFromArgPath(outputFileName);
			//check files state and access
			checkInputFile(inputFile);
			checkOutputFile(outputFile);
			try {
				inputFileReader = new BufferedReader(new FileReader(inputFile));
				outputFileWriter = new BufferedWriter(new FileWriter(outputFile));
			} catch (FileNotFoundException e) {
				logger.error(MsgManager.getInst().getStr(INPUT_FILE_NOT_FOUND));
				throw new FileException(MsgManager.getInst().getStr(INPUT_FILE_NOT_FOUND), e);
			} catch (IOException e) {
				logger.error(MsgManager.getInst().getStr(INPUT_READ_ERROR));
				throw new FileException(MsgManager.getInst().getStr(INPUT_READ_ERROR), e);
			}
		}
	}
	
	/**
	 * Method to read test instructions strings from file
	 * @return List<String> list with test instructions
	 * @throws FileException
	 */
	public List<String> readTestsFromFile() throws FileException {
		List<String> testList = new ArrayList<>();
		try {
			String stringWithTest = "";
			while ((stringWithTest = inputFileReader.readLine()) != null) {
				testList.add(stringWithTest.trim());
			}
		} catch (IOException e) {
			closeReader();
			logger.error(MsgManager.getInst().getStr(INPUT_READ_ERROR));
			throw new FileException(MsgManager.getInst().getStr(INPUT_READ_ERROR), e);
		}
		closeReader();
		return testList;
	}

	/**
	 * Method to write test results to output file
	 * @param testsResult - list with test instructions
	 * @throws FileException
	 */
	public void writeTestsResultToFile(String testsResult) throws FileException {
		if (!testsResult.isEmpty() && testsResult != null) {
			try {
				outputFileWriter.write(testsResult);
			} catch (IOException e) {
				closeWriter();
				logger.error(MsgManager.getInst().getStr(OUTPUT_READ_ERROR));
				throw new FileException(MsgManager.getInst().getStr(OUTPUT_READ_ERROR), e);
			}
		}
		closeWriter();
	}

	/**
	 * Method to check output file state and access
	 * @param outputFile - output file to check
	 * @throws FileException 
	 */
	private void checkOutputFile(File outputFile) throws FileException {
		if (outputFile != null) {
			// if output is exists and is a file, check if has access to file
			if (outputFile.exists()) {
				if (!outputFile.isFile()) {
					logger.error(MsgManager.getInst().getStr(OUTPUT_NOT_A_FILE));
					throw new FileException(MsgManager.getInst().getStr(OUTPUT_NOT_A_FILE));
				}
				// check if not read only
				if (!outputFile.canWrite()) {
					logger.error(MsgManager.getInst().getStr(OUTPUT_NO_ACCESS));
					throw new FileException(MsgManager.getInst().getStr(OUTPUT_NO_ACCESS));
				}
			}
		}
	}

	/**
     * Method to check input file state
	 * @param inputFile- input file to check
	 * @throws FileException
	 */
	private void checkInputFile(File inputFile) throws FileException {
		if (inputFile != null) {
			// check if input file is exists and is a file
			if (inputFile.exists()) {
				if (!inputFile.isFile()) {
					logger.error(MsgManager.getInst().getStr(INPUT_NOT_A_FILE));
					throw new FileException(MsgManager.getInst().getStr(INPUT_NOT_A_FILE));
				}
			} else {
				logger.error(MsgManager.getInst().getStr(INPUT_NOT_EXISTS));
				throw new FileException(MsgManager.getInst().getStr(INPUT_NOT_EXISTS));
			}
		}
	}
	
	/**
	 * Method creates file with pathname
	 * @param fileNameFromArg
	 * @return fileFromArg - file created with pathname
	 */
	private File createFileFromArgPath(String fileNameFromArg) {
		File fileFromArg = null;
		if (!fileNameFromArg.isEmpty() && fileNameFromArg != null) {
			fileFromArg = new File(fileNameFromArg);
		}
		return fileFromArg;
	}

	/**
	 * Method closes the input stream
	 */
	private void closeReader() {
		try {
			inputFileReader.close();
		} catch (IOException e) {
			logger.warn(MsgManager.getInst().getStr(READER_NOT_CLOSED), e);
		}
	}

	/**
	 * Method closes the output stream
	 */
	private void closeWriter() {
		try {
			outputFileWriter.close();
		} catch (IOException e) {
			logger.warn(MsgManager.getInst().getStr(WRITER_NOT_CLOSED), e);
		}
	}

}
