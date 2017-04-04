package ht1.executor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import ht1.manager.MsgManager;

/**
 * Class <code>CheckLinkPresentByName</code> is an implementation for <code>Executor</code> class, responsible for
 * checkLinkPresentByName tests
 */
public class CheckLinkPresentByName extends Executor {
	private static Logger logger = Logger.getLogger(CheckLinkPresentByName.class);
	public static final String lINK_NAME_EMPTY = "warn.empty.link";

	@Override
	public boolean execute(String linkName) {
		if (!linkName.isEmpty() && linkName != null) {
			long startTime = System.currentTimeMillis();
			if (findLinkByHref(linkName)) {
				// calculate test running time
				setRunningTime(System.currentTimeMillis() - startTime);
				return true;
			}
			return false;
		}
		logger.warn(MsgManager.getInst().getStr(lINK_NAME_EMPTY));
		return false;
	}

	/**
	 * Method receive a link name to be checked for compliance with page link
	 * names
	 * @param linkName - link name to match with link names on loaded page
	 * @return true if match found
	 */
	private boolean findLinkByHref(String linkName) {
			// create link name pattern to be checked.
			String linkNamePattern = ">" + linkName + "</a>";
			// load page content to perform check
			String pageContent = getPage();
			// find link name matching with page content
			Matcher matcher = Pattern.compile(linkNamePattern).matcher(pageContent);
			return matcher.find();
	}

}