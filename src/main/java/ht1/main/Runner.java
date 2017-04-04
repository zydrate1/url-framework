package ht1.main;

import org.apache.log4j.Logger;
import ht1.framework.Framework;
import ht1.framework.exception.FrameworkException;
import ht1.manager.MsgManager;

public class Runner {

	public static final String OUTPUT_FILE_NAME = "resource.output.filename";
	public static final String NOT_ENOUGH_ARGUMENTS = "error.not.enough.arg";
	public static final String OUTPUT_NOT_DEFINED = "warn.output.not.defined";
	public static final String FRAMEWORK_ERROR = "error.framework";
	private static Logger logger = Logger.getLogger(Runner.class);

	/**
	 * Starts program
	 * @param args - program arguments
	 * @throws MsgManagerException 
	 */
	public static void main(String[] args) {

		if (args.length > 0) {
			String inputFileName = args[0];
			String outputFileName;
			// object to run tests
			try {
				if (args.length > 1) {
					outputFileName = args[1];
				} else {
					// if log not specified, create one in project resource
					// folder
					logger.warn(MsgManager.getInst().getStr(OUTPUT_NOT_DEFINED));
					String pathToOutputFile = Runner.class.getProtectionDomain().getCodeSource().getLocation().getPath()
							+ MsgManager.getInst().getStr(OUTPUT_FILE_NAME);
					outputFileName = pathToOutputFile;
				}
				//run framework for execution all tests
				new Framework().initializeTests(inputFileName,outputFileName);;
			} catch (FrameworkException e) {
					logger.error(e.getClass() + " : " + MsgManager.getInst().getStr(FRAMEWORK_ERROR));
			}
		} else {
			logger.error(MsgManager.getInst().getStr(NOT_ENOUGH_ARGUMENTS));
		}
	}

}
