package ht1.framework;

import java.util.List;

import org.apache.log4j.Logger;

import ht1.executor.CheckLinkPresentByHref;
import ht1.executor.CheckLinkPresentByName;
import ht1.executor.CheckPageContains;
import ht1.executor.CheckPageTitle;
import ht1.executor.OpenURL;
import ht1.file.FileWorker;
import ht1.file.exception.FileException;
import ht1.framework.exception.FrameworkException;
import ht1.manager.MsgManager;

/**
 * <code>Framework</code> is a test framework class
 * Class extracts test instructions and dispatch tests depends on command,
 * after tests execution, initialize testing result output
 */
public class Framework {

	private final static String COMMAND_OPEN = "open";
	private final static String COMMAND_CHECK_LINK_HREF = "checkLinkPresentByHref";
	private final static String COMMAND_CHECK_LINK_NAME = "checkLinkPresentByName";
	private final static String COMMAND_CHECK_TITLE = "checkPageTitle";
	private final static String COMMAND_CHECK_CONTENT = "checkPageContains";
	private final static String INITIATION_ERROR = "error.initiatioin";
	private final static String RESULT_WRITING_ERROR = "error.writing.result";
	private final static String COMMAND_NOT_RECOGNIZED = "warn.command.not.recognized";
	private final static String EMPTY_TEST_COMMAND = "warn.empty.command";
	private final static String FILES_ERROR = "error.files";
	private static final String FINISHED = "info.finished";
	private static final String EMPTY_LIST = "warn.empty.list";
	private static Logger logger = Logger.getLogger(Framework.class);
	private FileWorker fileWorker;
	private TestStarter testDispatcher;
	private String result;
	
	/**
	 * Method prepare instances for work with files and handling tests
	 * @param inputFileName input file name with test instructions
	 * @param outputFileName output file name for write test results
	 * @throws FrameworkException
	 * @throws  
	 */
	private void prepareTestEnvironment(String inputFileName,String outputFileName) throws FrameworkException {
		try {
			this.fileWorker = new FileWorker(inputFileName, outputFileName);
		} catch (FileException e) {
			logger.error(e.getClass() + " : "+ MsgManager.getInst().getStr(FILES_ERROR));
			throw new FrameworkException(MsgManager.getInst().getStr(FILES_ERROR),e);
		}
		this.testDispatcher = new TestStarter();
	}
	
	/**
	 * Method initialize test list from input file instructions
	 * @param inputFileName input file name with test instructions
	 * @param outputFileName output file name for write test results
	 * @throws FrameworkException
	 * @throws  
	 */
	public void initializeTests(String inputFileName,String outputFileName) throws FrameworkException {
		// prepare instances for work with files, and for handling tests
		if(!inputFileName.isEmpty() && inputFileName!=null && !outputFileName.isEmpty() && outputFileName!=null){
			prepareTestEnvironment(inputFileName, outputFileName);
		//read test list from file and dispatch tests to execution
		List<String> testList;
		try {
			testList = fileWorker.readTestsFromFile();

			if (testList != null && testList.size() > 0) {
				for (String testInstructions : testList) {
					dispatchTest(testInstructions);
				}
			} else {
				logger.warn(MsgManager.getInst().getStr(EMPTY_LIST));
			}
		} catch (FileException e) {
			logger.error(e.getClass() + " : "+ MsgManager.getInst().getStr(INITIATION_ERROR));
			throw new FrameworkException(MsgManager.getInst().getStr(INITIATION_ERROR), e);
		}
		formResult();
		sendTestsResultToOutput();
		}
	}

	/**
	 * Method extracts test command from test instruction string
	 * @param testInstructions full test instruction string
	 * @return test command
	 */
	private String extractCommand(String testInstructions) {
		String command = "";
		if (testInstructions != null && !testInstructions.isEmpty()) {
			//remove quotes from test string
			testInstructions = (testInstructions.replaceAll("\"", ""));
			//command should be the first word in string
			String[] commandParts = testInstructions.split(" ");
			command = commandParts[0];
		}
		return command;
	}

	/** Method dispatches tests to certain test classes depend on test command
	 * @param testInstructions full test instruction string
	 * @throws FrameworkException
	 * @throws  
	 */
	public void dispatchTest(String testInstructions) throws FrameworkException  {
		//extract test command from the test string
		String command = extractCommand(testInstructions);
		if (command != null && !command.isEmpty()) {
			switch (command) {
			case COMMAND_OPEN:
				testDispatcher.startOpenURLTest(new OpenURL(), testInstructions, command);
				break;
			case COMMAND_CHECK_LINK_HREF:
				testDispatcher.startTest(new CheckLinkPresentByHref(), testInstructions, command);
				break;
			case COMMAND_CHECK_LINK_NAME:
				testDispatcher.startTest(new CheckLinkPresentByName(), testInstructions, command);
				break;
			case COMMAND_CHECK_TITLE:
				testDispatcher.startTest(new CheckPageTitle(), testInstructions, command);
				break;
			case COMMAND_CHECK_CONTENT:
				testDispatcher.startTest(new CheckPageContains(), testInstructions, command);
				break;
				//if command not recognized, test considered failed
			default:
				logger.warn(MsgManager.getInst().getStr(COMMAND_NOT_RECOGNIZED));
				testDispatcher.registerFailedTest(testInstructions);
				}
			//empty command not allowed
		} else {
			logger.warn(MsgManager.getInst().getStr(EMPTY_TEST_COMMAND));
		}
	}

	/**
	 * Method calls <code>TestDispatcher</code> instance 
	 * to form test result
	 */
	public void formResult() {
		result = testDispatcher.formResult();
	}
	
	/**
	 * Method send tests result to output
	 * @throws FrameworkException
	 * @throws  
	 */
	public void sendTestsResultToOutput() throws FrameworkException {
		//not empty tests result will write to output file
		if (!result.isEmpty() && result != null) {
			try {
				fileWorker.writeTestsResultToFile(result);
				logger.info(MsgManager.getInst().getStr(FINISHED));
			} catch (FileException e) {
				logger.error(MsgManager.getInst().getStr(RESULT_WRITING_ERROR));
				throw new FrameworkException(MsgManager.getInst().getStr(RESULT_WRITING_ERROR), e);
			}
		}
	}
	
}
