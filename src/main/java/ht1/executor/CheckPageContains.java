package ht1.executor;

import org.apache.log4j.Logger;
import ht1.manager.MsgManager;

/**
 * Class <code>CheckPageContains</code> is an implementation for <code>Executor</code> class, responsible for
 * checkPageContains tests
 */
public class CheckPageContains extends Executor {

	private static Logger logger = Logger.getLogger(CheckPageContains.class);
	public static final String CONTENT_IS_EMPTY = "warn.empty.content";

	@Override
	public boolean execute(String contentToMatch) {
		if (!contentToMatch.isEmpty() && contentToMatch != null) {
			long startTime = System.currentTimeMillis();
			if (isPageContains(contentToMatch)) {
				// calculate test running time
				setRunningTime(System.currentTimeMillis() - startTime);
				return true;
			}
			return false;
		}
		logger.warn(MsgManager.getInst().getStr(CONTENT_IS_EMPTY));
		return false;
	}

	/**
	 * Method receive a content to be checked for compliance with
	 * page content
	 * @param contentToMatch - content to match with loaded page content
	 * @return true if match found
	 */
	private boolean isPageContains(String contentToMatch) {
		// load page content to perform check
			String pageContent = getPage();
			return pageContent.contains(contentToMatch);
	}

}
