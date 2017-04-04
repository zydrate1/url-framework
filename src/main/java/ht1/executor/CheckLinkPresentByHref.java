package ht1.executor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import ht1.manager.MsgManager;

/**
 * Class <code>CheckLinkPresentByHref</code> is an implementation for <code>Executor</code> class, responsible for
 * checkLinkPresentByHref tests
 */
public class CheckLinkPresentByHref extends Executor {

	public static final String HREF_PATTERN = "pattern.href";
	public static final String HREF_EMPTY ="warn.empty.href";
	private static Logger logger = Logger.getLogger(CheckLinkPresentByHref.class);
	
	@Override
	public boolean execute(String href) {
		if (!href.isEmpty() && href != null) {
			long startTime = System.currentTimeMillis();
			if (findLinkByHref(href)) {
				// calculate test running time
				setRunningTime(System.currentTimeMillis() - startTime);
				return true;
			}
			return false;
		}
		logger.warn(MsgManager.getInst().getStr(HREF_EMPTY));
		return false;
	}

	/**
	 * Method receive a link href attribute to be checked for compliance with
	 * page link hrefs
	 * @param href - link href to match with link hrefs on loaded page
	 * @return true if match found
	 */
	private boolean findLinkByHref(String href) {
		// create href pattern to be checked. '?' symbol escapes to not perceive
		// as regex quantifier
		String hrefPattern = MsgManager.getInst().getStr(HREF_PATTERN) + href.replace("?", "\\?") + "\"";
		// load page content to perform check
		String pageContent = getPage();
		// find href matching with page content
		Matcher matcher = Pattern.compile(hrefPattern).matcher(pageContent);
		return matcher.find();
	}

}
