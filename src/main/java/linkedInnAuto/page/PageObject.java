package linkedInnAuto.page;


import linkedInnAuto.constants.CSSSelectors;
import linkedInnAuto.setup.PropertiesHelper;
import linkedInnAuto.setup.WebDriverSetup;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Component
public class PageObject {
    private static final String LINKED_INN_LOGIN_PAGE = "https://www.linkedin.com/login?fromSignIn=true&trk=guest_homepage-basic_nav-header-signin";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private WebDriverSetup webDriverSetup;
    private WebDriver webDriver;
    private PropertiesHelper propertiesHelper;

    @Autowired
    public PageObject( WebDriverSetup webDriverSetup, PropertiesHelper propertiesHelper ) {
        this.webDriverSetup = webDriverSetup;
        this.propertiesHelper = propertiesHelper;
        this.webDriver = webDriverSetup.createWebDriver();
    }

    public PageObject login() {
        //webDriver.get( LINKED_INN_LOGIN_PAGE );
        open(LINKED_INN_LOGIN_PAGE);
        typeLogin( propertiesHelper.getProperty( LOGIN ) );
        typePassword( propertiesHelper.getProperty( PASSWORD ) );
        clickSignIn();
        return this;
    }

    private void typeLogin( String login ) {
//        WebDriverWait wait = new WebDriverWait( webDriver, 5 );
//        wait.until( ExpectedConditions.visibilityOf( webDriver.findElement( CSSSelectors.getLoginSelector() ) ) );
//        webDriver.findElement( CSSSelectors.getLoginSelector() ).clear();
//        webDriver.findElement( CSSSelectors.getLoginSelector() ).sendKeys( login );
        $(CSSSelectors.getLoginSelector()).setValue( login );
    }

    private void typePassword( String password ) {
        $(CSSSelectors.getPasswordSelector()).setValue( password );
    }

    private void clickSignIn() {
        $(CSSSelectors.getSignInButton()).click();
    }

    public PageObject typeCriteriaInSearch( String criteria ) {
       $(CSSSelectors.getGlobalSearch()).setValue( criteria ).sendKeys( Keys.ENTER );
       return this;
    }

    public PageObject clickPeopleOnlyInSearchResults() {
        $(CSSSelectors.getPeopleOnlyInSearchResults()).click();
        return this;
    }

}
