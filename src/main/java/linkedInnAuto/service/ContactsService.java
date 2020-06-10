package linkedInnAuto.service;

import linkedInnAuto.constants.CSSSelectors;
import linkedInnAuto.setup.PropertiesHelper;
import linkedInnAuto.setup.WebDriverSetup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactsService {
    private static final String LINKED_INN_LOGIN_PAGE = "https://www.linkedin.com/login?fromSignIn=true&trk=guest_homepage-basic_nav-header-signin";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private WebDriver webDriver;
    @Autowired
    private WebDriverSetup webDriverSetup;
    @Autowired
    private PropertiesHelper propertiesHelper;


    private ContactsService login() {
        webDriver = webDriverSetup.createWebDriver();
        webDriver.get( LINKED_INN_LOGIN_PAGE );
        typeLogin( propertiesHelper.getProperty( LOGIN ) );
        typePassword( propertiesHelper.getProperty( PASSWORD ) );
        clickSignIn();
        return this;
    }

    public void addNewContactsByCompany( String companyName ) {
        login();
    }

    private void typeLogin( String login ) {
        WebDriverWait wait = new WebDriverWait( webDriver, 5 );
        wait.until( ExpectedConditions.visibilityOf( webDriver.findElement( CSSSelectors.getLoginSelector() ) ) );
        webDriver.findElement( CSSSelectors.getLoginSelector() ).clear();
        webDriver.findElement( CSSSelectors.getLoginSelector() ).sendKeys( login );
    }

    private void typePassword( String password ) {
        WebDriverWait wait = new WebDriverWait( webDriver, 5 );
        wait.until( ExpectedConditions.visibilityOf( webDriver.findElement( CSSSelectors.getPasswordSelector() ) ) );
        webDriver.findElement( CSSSelectors.getPasswordSelector() ).clear();
        webDriver.findElement( CSSSelectors.getPasswordSelector() ).sendKeys( password );
    }

    private void clickSignIn(){
        WebDriverWait wait = new WebDriverWait( webDriver, 5 );
        wait.until( ExpectedConditions.visibilityOf( webDriver.findElement( CSSSelectors.getSignInButton() ) ) );
        webDriver.findElement( CSSSelectors.getSignInButton() ).click();
    }


}
