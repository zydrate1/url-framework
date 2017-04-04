package ht1.executor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import ht1.manager.MsgManager;


/**
 * Class <code>CheckPageTitle</code> is an implementation for <code>Executor</code> class, responsible for
 * checkPageTitle tests
 */
public class CheckPageTitle extends Executor {

	private static Logger logger = Logger.getLogger(CheckPageTitle.class);
	public static final String TITLE_IS_EMPTY = "warn.empty.title";

	@Override
	public boolean execute(String pageTitle) {
		if (!pageTitle.isEmpty() && pageTitle != null) {
			long startTime = System.currentTimeMillis();
			if (findPageTitle(pageTitle)) {
				// calculate test running time
				setRunningTime(System.currentTimeMillis() - startTime);
				return true;
			}
			return false;
		}
		logger.warn(MsgManager.getInst().getStr(TITLE_IS_EMPTY));
		return false;
	}

	/**
	 * Method receive a page title to be checked for compliance with
	 * page title
	 * @param href - a page title to match with loaded page title
	 * @return true if match found
	 */
	private boolean findPageTitle(String pageTitle) {
		// create page title pattern to be checked.
		String titlePattern = "<title>" + pageTitle + "</title>";
		// load page content to perform check
		String pageContent = getPage();
		// find page title matching with page content
		Matcher matcher = Pattern.compile(titlePattern).matcher(pageContent);
		return matcher.find();
	}

}
