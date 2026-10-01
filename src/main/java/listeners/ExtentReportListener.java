package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import utils.ExtentManager;

/**
 * Writes the ExtentReports HTML file once the whole suite has finished.
 * Registered in testng.xml under <listeners>.
 */
public class ExtentReportListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        // Nothing to do here: the report is created when the first test starts
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.flush();
    }
}
