package linkedInnAuto.page;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import linkedInnAuto.constants.CSSSelectors;
import linkedInnAuto.setup.PropertiesHelper;
import linkedInnAuto.setup.WebDriverSetup;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

@Component
public class PageObject {
    private static final String LINKED_INN_LOGIN_PAGE = "https://www.linkedin.com/login?fromSignIn=true&trk=guest_homepage-basic_nav-header-signin";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    public static final int DEFAULT_VISIBILITY_ELEMENT_TIMEOUT = 10_000;
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
        open( LINKED_INN_LOGIN_PAGE );
        typeLogin( propertiesHelper.getProperty( LOGIN ) );
        typePassword( propertiesHelper.getProperty( PASSWORD ) );
        clickSignIn();
        waitForPageLoaded();
        return this;
    }

    private void typeLogin( String login ) {

        $( CSSSelectors.getLoginSelector() ).waitUntil( Condition.visible, 1000 ).setValue( login );
    }

    private void typePassword( String password ) {
        $( CSSSelectors.getPasswordSelector() ).waitUntil( Condition.visible, 1000 ).setValue( password );
    }

    private void clickSignIn() {
        $( CSSSelectors.getSignInButton() ).waitUntil( Condition.and(
                "clickable",
                Condition.visible,
                Condition.enabled
        ), 1000 ).click();
    }

    public PageObject typeCriteriaInSearch( String criteria ) {
        $( CSSSelectors.getGlobalSearch() ).waitUntil( Condition.visible, DEFAULT_VISIBILITY_ELEMENT_TIMEOUT ).setValue(
                criteria ).sendKeys( Keys.ENTER );
        return this;
    }

    public PageObject clickPeopleOnlyInSearchResults() {
        $( CSSSelectors.getPeopleOnlyInSearchResults() ).waitUntil(
                Condition.visible,
                DEFAULT_VISIBILITY_ELEMENT_TIMEOUT
        ).click();
        return this;
    }

    private void waitForPageLoaded(){
        new WebDriverWait( WebDriverRunner.getWebDriver(), 10)
                .until( driver -> ((JavascriptExecutor)driver).executeScript( "return document.readyState").equals( "complete"));
    }

}
