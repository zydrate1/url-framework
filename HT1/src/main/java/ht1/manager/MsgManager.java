package ht1.manager;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

/**
 * class controls application messages, which are keeps in
 * property file.
 */
public class MsgManager {

	/* path to resource files */
	public static final String RESOURCE_PATH = "property/Resource";
	
	private static MsgManager instance;
	private static Logger logger = Logger.getLogger(MsgManager.class);
	private final String RECOURCE_NOT_FOUND = "Resource for message wasn't found. Short message:";
    /**
     * Resource bundles contain locale-specific objects
     */
	private ResourceBundle resourceBundle;
	
	/**
	 * @return instance MessageManager class instance
	 */
	public static MsgManager getInst() {
		if (instance == null) {
			instance = new MsgManager();
			instance.resourceBundle = ResourceBundle.getBundle(RESOURCE_PATH);
		}
		return instance;
	}
	
	/**
	 * method finds appropriate <code>String</code>
	 * @param key <code>ResourceBundle</code> key
	 * @return appropriate <code>String</code>
	 * by <code>ResourceBundle</code> key
	 */
	public String getStr(String key) {
		try {
			return (String) resourceBundle.getObject(key);	
		} catch (MissingResourceException e) {
			logger.warn(RECOURCE_NOT_FOUND);
		}
		//if bundle not found, return key back
		return key;
	}

}
