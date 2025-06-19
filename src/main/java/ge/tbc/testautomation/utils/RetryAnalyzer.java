package ge.tbc.testautomation.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        Retry retry = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Retry.class);
        if (retry == null) {
            return false;
        }
        int maxRetryCount = retry.maxRetries();
        if (retryCount < maxRetryCount) {
            System.out.println("ტესტის თავიდან გაშვება: " + result.getName() + ", მცდელობა: " + (retryCount + 1));
            retryCount++;
            return true;
        }
        retryCount = 0;
        return false;
    }
}