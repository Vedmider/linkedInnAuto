package linkedInnAuto.page;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import linkedInnAuto.constants.CSSSelectors;
import linkedInnAuto.setup.PropertiesHelper;
import linkedInnAuto.setup.WebDriverSetup;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

@Component
public class PageObject {
    private Logger LOG = LoggerFactory.getLogger( PageObject.class );
    private static final String LINKED_INN_LOGIN_PAGE = "https://www.linkedin.com/login?fromSignIn=true&trk=guest_homepage-basic_nav-header-signin";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";
    private static final Condition clickable = Condition.and( "clickable", Condition.visible, Condition.enabled );
    private static final List<SelenideElement> contactsToAddInSearchResultsPage = $(
            "div[class^='blended-srp-results-js']" )
            .$$( "button[data-control-name='srp_profile_actions']" );
    private static final SelenideElement invitationDialogElement = $( "div[role='dialog']" );
    private static final List<SelenideElement> paginationNumbers = $$( "li[data-test-pagination-page-btn]" );
    private static final SelenideElement nextPageButton = $( "button[class*='artdeco-pagination__button--next']" );
    private static final SelenideElement previousPageButton = $( "button[class*='artdeco-pagination__button--previous']" );
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

    public PageObject addContactsFromSearchResultsPage( int numberOfContactsToAdd ) {
        int lastSearchResultPage = getNumberOfSearchResultsPages();
        int contactsListSize;
        int currentPage;
        int addedContacts = 0;
        WebElement addContactButton;
        while ( addedContacts < numberOfContactsToAdd ) {
            LOG.info( "Start of adding contacts. Added contacts {}", addedContacts );
            contactsListSize = WebDriverRunner.getWebDriver().findElement( By.cssSelector(
                    "div[class^='blended-srp-results-js']" ) ).findElements( By.cssSelector(
                    "button[data-control-name='srp_profile_actions']" ) ).size();
            LOG.info( "Contacts list size - {}", contactsListSize );
            for ( int i = 0; i < contactsListSize; i++ ) {
                if ( addedContacts == numberOfContactsToAdd ) {
                    LOG.info( "End of adding contacts due to addedContacts is equal to numberContactsToAdd" );
                    break;
                }
                LOG.info( "Adding contacts on page process. Iteration - {}.", i );
                addContactButton = WebDriverRunner.getWebDriver().findElement( By.cssSelector(
                        "div[class^='blended-srp-results-js']" ) ).findElements( By.cssSelector(
                        "button[data-control-name='srp_profile_actions']" ) ).get( i );
                if ( addContactButton.isEnabled() ) {
                    LOG.info( "Trying to click element : {}", addContactButton.toString() );
                    addContactButton.click();
                    acceptInvitationDialog();
                    addedContacts++;
                }
            }
            currentPage = getCurrentPageNumber();
            if ( currentPage == lastSearchResultPage ) {
                LOG.info( "End of adding contacts due to currentPage is equal to lastPage" );
                break;
            }
            nextPageButton.click();
            waitForPageLoaded();
        }
        return this;
    }

    private void waitForPageLoaded() {
        new WebDriverWait( WebDriverRunner.getWebDriver(), 20 )
                .until( driver -> ( (JavascriptExecutor) driver ).executeScript( "return document.readyState" )
                                                                 .equals( "complete" ) );
    }

    private int getNumberOfSearchResultsPages() {
        scrollDownSearchResultsPage();
        List<Integer> searchResultsPageNumbers = paginationNumbers.stream()
                                                                  .map( element -> Integer.valueOf( element.$$( "span" )
                                                                                                           .get( 0 )
                                                                                                           .getText() ) )
                                                                  .collect( Collectors.toList() );
        scrollToStartOfSearchResultsPage();
        return Collections.max( searchResultsPageNumbers );
    }

    private int getCurrentPageNumber() {
        String currentPageValue = paginationNumbers.stream()
                                                   .map( element -> element.$( "button" ) )
                                                   .filter( element -> element.getAttribute( "aria-label" )
                                                                              .contains( "current" ) )
                                                   .findFirst().get().$$( "span" ).get( 0 ).getText();
        return Integer.valueOf( currentPageValue );
    }

    private void scrollDownSearchResultsPage() {
        LOG.info( "Start to execute java script to scroll down" );
        executeJavaScript( "window.scrollTo(0, document.body.scrollHeight);" );
        waitForPageLoaded();
    }

    private void scrollToStartOfSearchResultsPage() {
        LOG.info( "Start to execute java script to scroll up" );
        executeJavaScript( "window.scrollTo(0, 0);" );
    }

    private void acceptInvitationDialog() {
        invitationDialogElement.$$( "button" ).get( 2 ).waitUntil( Condition.visible, 3000 ).click();
    }
}
