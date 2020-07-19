package linkedInnAuto.setup;

import com.codeborne.selenide.WebDriverRunner;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.logging.Level;

@Component
public class ChromeDriverSetup extends WebDriverSetup {
    private WebDriver webDriver;

    @Override
    public WebDriver createWebDriver() {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put( "profile.default_content_settings.popups", 0 );
        chromePrefs.put( "credentials_enable_service", false );
        chromePrefs.put( "profile.password_manager_enabled", false );

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption( "prefs", chromePrefs );
        options.addArguments( WebDriverProperties.getWindowsSizeArgument() );
        options.addArguments( "--disable-extensions" );
        options.addArguments( "--enable-app-install-alerts" );
        options.addArguments( "--disable-popup-blocking" );
        options.addArguments( "--no-sandbox" );
        options.addArguments( "--disable-infobars" );

        LoggingPreferences loggingPrefs = new LoggingPreferences();
        loggingPrefs.enable( LogType.BROWSER, Level.ALL );

        options.setCapability( CapabilityType.ACCEPT_SSL_CERTS, true );
        options.setCapability( CapabilityType.SUPPORTS_ALERTS, true );
        options.setCapability( CapabilityType.SUPPORTS_JAVASCRIPT, true );
        options.setCapability( CapabilityType.LOGGING_PREFS, loggingPrefs );
        String pathToChromeDriver = downloadChromeDriver( WebDriverProperties.getVersion() );
        System.setProperty( "webdriver.chrome.driver", pathToChromeDriver );
        webDriver = new ChromeDriver( options );
        WebDriverRunner.setWebDriver( webDriver );
        return webDriver;
    }

    public static String downloadChromeDriver( String version ) {
        ChromeDriverManager.chromedriver().browserVersion( version ).setup();
        return ChromeDriverManager.chromedriver().getBinaryPath();
    }
}
