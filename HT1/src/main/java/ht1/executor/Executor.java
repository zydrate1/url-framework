package ht1.executor;

/**
 * Class <code>Executor</code> is a parent test class,
 * for definite test class implementations
 */
public abstract class Executor {
	
	//max time to establish an url conneciton
	private double timeout;
	//current loaded page
	private String page;
	//test running time
	private double runningTime;

	public double getTimeout() {
		return timeout;
	}
	
	public void setTimeout(double timeout) {
		this.timeout = timeout;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		if (!page.isEmpty() && page != null) {
			this.page = page;
		}
	}

	public double getRunningTime() {
		return runningTime;
	}

	//for proper result output  
	//running time converts from ms to s
	public void setRunningTime(double runningTime) {
		this.runningTime = runningTime / 1000d;
	}

	/**
	 * Method execute all tests in class implementations
	 * @param instruction full test instruction received
	 * 		  from file
	 * @return true if test passed
	 * @throws MsgManagerException 
	 */
	public abstract boolean execute(String instruction);
}
