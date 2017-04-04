package ht1.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import ht1.executor.CheckPageContains;
import ht1.executor.Executor;
import ht1.manager.MsgManager;

public class TestStarter {

	// total tests running time
	private double totalTime;
	// all tests amount
	private int testsAmount;
	// passed test counter
	private int passed;
	// failed test counter
	private int failed;
	// store current page
	private String currentPage;
	// results for file output
	private ArrayList<String> resultForOutput;
	private static Logger logger = Logger.getLogger(TestStarter.class);
	private static final String EMPTY_TEST_INSTRUCTIONS = "warn.empty.instruction";
	private static final String WRONG_TEST_INSTRUCTIONS = "warn.wrong.instruction";
	private static final String NO_TIMEOUT = "warn.no.timeout";
	private static final String EMPTY_TEST_INSTRUCTION = "warn.empty.test.result";
	private static final String URL_NOT_OPENED = "warn.url.not.opened";
	private static final String TEST_STRING_PATTERN = "pattern.test.string";
	private static final String TEST_INSTRUCTIONS_PATTERN = "pattern.test.instruction";
	private static final String CONTAINS_TEST_INSTRUCTIONS_PATTERN = "pattern.test.instruction.contains";
	private static final int FAILED_TEST_TIME = 0;

	public TestStarter() {
		resultForOutput = new ArrayList<>();
	}

	/**
	 * Method start open url tests for execution
	 * @param executor - an instance of <code>Executor</code> test object
	 * @param testInputString - full test instruction
	 * @param command - a test command
	 */
	public void startOpenURLTest(Executor executor, String testInputString, String command) {
		List<String> instructionsParts = null;
		if (executor != null) {
			instructionsParts = extractInstructions(testInputString);
			boolean isTestPassed = false;
			// instruction 'open' must have 3 arguments
			if (instructionsParts.size() == 3) {
				double timeout = Double.parseDouble(instructionsParts.get(2));
				executor.setTimeout(timeout);
				//resource to be checked for compliance with page content
				String resource = instructionsParts.get(1);
				isTestPassed = executor.execute(resource);
				saveTestResults(testInputString, isTestPassed, executor.getRunningTime());
				// running time from s to ms
				totalTime += executor.getRunningTime();
				currentPage = executor.getPage();
				// test considered failed if no instructions and timeout found
			} else {
				if (instructionsParts.size() == 2) {
					logger.warn(MsgManager.getInst().getStr(NO_TIMEOUT));
				} else if (instructionsParts.size() == 1) {
					logger.warn(MsgManager.getInst().getStr(EMPTY_TEST_INSTRUCTIONS));
				} else {
					logger.warn(MsgManager.getInst().getStr(WRONG_TEST_INSTRUCTIONS));
				}
				registerFailedTest(testInputString);
			}
		}
	}

	/**
	 * Method start tests for execution
	 * @param executor - an instance of <code>Executor</code> test object
	 * @param testInputString - full test instruction
	 * @param command - a test command
	 */
	public void startTest(Executor executor, String testInputString, String command) {
		// content on which perform an operation
		List<String> instructionsParts = null;
		if (executor != null) {
			// content on which perform checkPageContains
			if (!(executor instanceof CheckPageContains)) {
				instructionsParts = extractInstructions(testInputString);
			} else {
				instructionsParts = extractInstructionsForCheckPageContains(testInputString);
			}
			if (instructionsParts.size() > 1) {
				// resource on which to perform a command
				String resource = instructionsParts.get(1);
				// test is considered failed if no URL wasn't load before
				// test
				if (currentPage != null) {
					executor.setPage(currentPage);
					// execute test and preparing data for output
					boolean isPassed = executor.execute(resource);
					// add test results to output list
					saveTestResults(testInputString, isPassed, executor.getRunningTime());
					totalTime += executor.getRunningTime();
				} else {
					logger.warn(MsgManager.getInst().getStr(URL_NOT_OPENED));
					registerFailedTest(testInputString);
				}
				// test considered failed if no instruction found
			} else if (instructionsParts.size() == 1) {
				logger.warn(MsgManager.getInst().getStr(EMPTY_TEST_INSTRUCTIONS));
				registerFailedTest(testInputString);
			} else {
				logger.warn(MsgManager.getInst().getStr(WRONG_TEST_INSTRUCTIONS));
			}
		}
	}

	// form result output
	/**
	 * Method forms result from test result data to a single string
	 * @return test result string
	 */
	public String formResult() {
		// calculate average time and round to to the thousandth
		double averageTime = roundDouble(totalTime / testsAmount);
		StringBuilder resultInfo = new StringBuilder();
		for (String testResult : resultForOutput) {
			resultInfo.append(testResult + "\n");
		}
		resultInfo.append("Total tests: " + testsAmount + '\n')
				.append("Passed/Failed: " + passed + "/" + failed + "\n").append("Total time: " + totalTime + "\n")
				.append("Average time: " + averageTime);
		return resultInfo.toString();
	}

	/**
	 * Method rounds double to 3 decimal places
	 * @param number - number to round
	 * @return rounded number
	 */
	private double roundDouble(double number) {
		number = Math.round(number * 1000);
		return number / 1000;
	}
	
	/**
	 * Method saves test result to list
	 * @param testResult - test result to save
	 * @param passed - if test is passed
	 * @param runningTime - test running time
	 * @return true if test result added successfully
	 */
	private void saveTestResults(String testResult, boolean isPassed, double runningTime) {
		testsAmount++;
		String fullInfo = null;
		if (isPassed) {
			passed++;
			fullInfo = "+ [" + testResult + "] " + runningTime;
		} else {
			failed++;
			fullInfo = "! [" + testResult + "] " + runningTime;
		}
		logTestResultToConsole(fullInfo);
		resultForOutput.add(fullInfo);
	}

	/**
	 * Method saves test result beforehand considered as failed
	 * @param testInputString
	 */
	public void registerFailedTest(String testInputString) {
		if (!testInputString.isEmpty() && testInputString != null) {
			saveTestResults(testInputString, false, FAILED_TEST_TIME);
		} else {
			logger.warn(MsgManager.getInst().getStr(EMPTY_TEST_INSTRUCTION));
		}
	}
	
	/**
	 * Method logs test result to console
	 * @param consoleInfo test result string
	 */
	private void logTestResultToConsole(String consoleInfo){
		logger.info(consoleInfo);
	}

	/**
	 * Method extracts test instructions for execution
	 * chekPageContains test
	 * @param testInputString full test instruction string
	 * @return List<String> of instruction parts
	 */
	private List<String> extractInstructionsForCheckPageContains(String testInputString) {
		List<String> commandParts = new ArrayList<String>();
		if (!testInputString.isEmpty() && testInputString != null) {
			String contentPattern = MsgManager.getInst().getStr(CONTAINS_TEST_INSTRUCTIONS_PATTERN);
			Matcher matcher = Pattern.compile(contentPattern).matcher(testInputString);
			if (matcher.matches()) {
				commandParts.add(matcher.group(1));
				commandParts.add(matcher.group(2));
			} else {
				logger.warn(MsgManager.getInst().getStr(WRONG_TEST_INSTRUCTIONS));
			}
		}
		return commandParts;
	}

	/**
	 * Method extracts test instructions for test execution
	 * @param testInputString full test instruction string
	 * @return List<String> of instruction parts
	 */
	private List<String> extractInstructions(String testInputString) {
		List<String> commangParts = new ArrayList<String>();
		if (!testInputString.isEmpty() && testInputString != null) {
			String commandPattern = MsgManager.getInst().getStr(TEST_STRING_PATTERN);
			Matcher matcher = Pattern.compile(commandPattern).matcher(testInputString);
			if (matcher.find()) {
				testInputString = matcher.group(0);
				Matcher m = Pattern.compile(MsgManager.getInst().getStr(TEST_INSTRUCTIONS_PATTERN))
						.matcher(testInputString);
				while (m.find()) {
					commangParts.add(m.group(1).replaceAll("\"", ""));
				}
			} else {
				logger.warn(MsgManager.getInst().getStr(WRONG_TEST_INSTRUCTIONS));
			}
		}
		return commangParts;
	}

}
