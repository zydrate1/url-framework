package ht1.executor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLHandshakeException;

import org.apache.log4j.Logger;

import ht1.manager.MsgManager;

/**
 * Class <code>OpenURL</code> is an implementation for <code>Executor</code> class, responsible for openUrl
 * tests and loading resource for next tests
 */
public class OpenURL extends Executor {

	private static final String PROTOCOL_UNDEFINED = "warn.protocol.undefined";
	private static final String URL_UNDEFINED = "warn.url.undefined";
	private static final String URL_IO = "warn.url.io";
	private static final String HOST_UNKNOWN = "warn.host.unknown";
	private static final String INVALID_URL = "warn.url.not.valid";
	private static final String TIMEOUT_EXCEEDED = "warn.timeout.exceed";
	private static final String PAGE_NOT_EXISTS = "warn.page.not.exists";
	private static final String PAGE_READING_ERROR = "warn.page.reading";
	private static final String URL_PATTERN = "pattern.url";
	private static final int CODE_SUCCESS_START = 200;
	private static final int CODE_SUCCESS_END = 226;
	private static final int CODE_REDIRECT_START = 300;
	private static final int CODE_REDIRECT_END = 307;
	private static Logger logger = Logger.getLogger(OpenURL.class);

	/**
	 * Method checks if url for connection is valid 
	 * @param urlString - url to validate
	 * @return true if url is valid
	 */
	private boolean checkUrl(String urlString) {
		boolean isValidUrl = false;
		if (!urlString.isEmpty() && urlString != null) {
			Matcher matcher = Pattern.compile(MsgManager.getInst().getStr(URL_PATTERN)).matcher(urlString);
			isValidUrl = matcher.matches();
		}
		return isValidUrl;
	}

	@Override
	public boolean execute(String urlString) {

		if (checkUrl(urlString)) {
			URL url;
			int responseCode = 0;
			HttpURLConnection connection;
			try {
				url = new URL(urlString);
				// save test start time
				long startTime = System.currentTimeMillis();
				// establish connection with resource by http protocol
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				// save test end time
				long endTime = System.currentTimeMillis();
				// count test running time
				setRunningTime(endTime - startTime);
				// test successful if request return correct page state code
				responseCode = connection.getResponseCode();
				if (!(responseCode >= CODE_SUCCESS_START && responseCode <= CODE_SUCCESS_END)
						&& !(responseCode >= CODE_REDIRECT_START && responseCode <= CODE_REDIRECT_END)) {
					logger.warn(MsgManager.getInst().getStr(PAGE_NOT_EXISTS));
					return false;
				}
				// test failed if page connection timeout reached
				if (getRunningTime() > getTimeout()) {
					logger.warn(MsgManager.getInst().getStr(TIMEOUT_EXCEEDED));
					return false;
				}
				// save current page
				setPage(readPageContent(connection));
				return true;
			} catch (MalformedURLException e) {
				logger.warn(MsgManager.getInst().getStr(PROTOCOL_UNDEFINED));
			} catch (SSLHandshakeException e) {
				logger.warn(
						MsgManager.getInst().getStr(URL_UNDEFINED));
			} catch (UnknownHostException e) {
				logger.warn(
						MsgManager.getInst().getStr(HOST_UNKNOWN));
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn(MsgManager.getInst().getStr(URL_IO));
			}
		} else {
			logger.warn(
					MsgManager.getInst().getStr(INVALID_URL));
			return false;
		}
		return false;
	}

	/**
	 * Method to open stream and load a page content from url connection
	 * @param HttpURLConnection	connection
	 * @return a loaded page content
	 */
	private String readPageContent(HttpURLConnection connection) {
		String pageContent = "";
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			StringBuilder pageContentBuilder = new StringBuilder();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				pageContentBuilder.append(line);
			}
			pageContent = pageContentBuilder.toString();
		} catch (IOException e) {
			logger.warn(MsgManager.getInst().getStr(PAGE_READING_ERROR));
		}
		return pageContent;
	}
}
