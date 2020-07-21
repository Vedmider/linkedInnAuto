package linkedInnAuto.service.filters;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import linkedInnAuto.constants.CSSSelectors;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static linkedInnAuto.page.PageObject.DEFAULT_VISIBILITY_ELEMENT_TIMEOUT;

//TODO class is not properly working. Will fix it later
@Component
public class SearchFilter {
    private static final Logger LOG = LoggerFactory.getLogger( SearchFilter.class );
    private WebDriver webDriver;

    public SearchFilter(){
    }

    public SearchFilter(WebDriver webDriver){
        this.webDriver = webDriver;
    }

    public void addAreaToFilters( String area ){
        $(CSSSelectors.getAreaFilterButton()).click();
        findAreaInSearchResultsAndClick( area );
    }

    private SelenideElement getSearchInputBoxArea(){
        return $(CSSSelectors.getSearchInputBoxInSearchArea());
    }

    private ElementsCollection getAreaSearchResults(){
        return getSearchInputBoxArea().$$( By.cssSelector( "li[class='search-s-facet-value']" ) );
    }

    private List<SelenideElement> getAreasList(){
        return getSearchInputBoxArea().$$( By.cssSelector( "span[class='search-typeahead-v2__hit-text t-14 t-black ']" ) );
    }

    private void findAreaInSearchResultsAndClick(String area){
        int numberOfArrowDownArgumentsNeeded = numberOfKeysArrowDownNeeded( area );
        if ( numberOfArrowDownArgumentsNeeded == 0){
            LOG.info( "There is no such area. Will skip selection" );
            return;
        }

        Keys[] keysArguments = setUpArrowDownKeys( numberOfArrowDownArgumentsNeeded );
        getAreaSearchInputField().setValue( area ).sendKeys( Keys.ARROW_DOWN, Keys.ENTER );
    }

    private void clickApplyButtonInGeolocationFilter(){
        $(By.cssSelector( "button[data-control-name='filter_pill_apply']" )).click();
    }

    private void clickAreaCheckBox(SelenideElement inputCheckBoxElement){
        executeJavaScript("arguments[0].scrollIntoView(true); arguments[0].click();", inputCheckBoxElement);
    }

    private int numberOfKeysArrowDownNeeded(String area){
        String[] areasList = getAreaSearchInputField().setValue( area ).parent().parent().$( By.cssSelector( "div[role='listbox']" ) ).text().split( "\\n" );
        int areasListLength = areasList.length;
        getAreaSearchInputField().clear();
        for (int i = 0; i < areasListLength; i++  ){
            if ( areasList[i].contains( area ) ){
                return i;
            }
        }
        return 0;
    }
    
    private SelenideElement getAreaSearchInputField(){
        return getSearchInputBoxArea().find( By.cssSelector("input[role='combobox']") ).waitUntil( Condition.visible, DEFAULT_VISIBILITY_ELEMENT_TIMEOUT );
    }

    private Keys[] setUpArrowDownKeys(int numberOfArrowDownArgumentsNeeded){
        Keys[] keysArguments = new Keys[numberOfArrowDownArgumentsNeeded + 1];
        for ( int i = 0; i < numberOfArrowDownArgumentsNeeded; i++ ){
            keysArguments[i] = Keys.ARROW_DOWN;
        }
        keysArguments[numberOfArrowDownArgumentsNeeded] = Keys.ENTER;
        return keysArguments;
    }
    // getSearchInputBoxArea().$$( By.cssSelector( "li[class='search-s-facet-value']" ) ).get( 0 ).text()
    // getSearchInputBoxArea().$$( By.cssSelector( "li[class='search-s-facet-value']" ) ).get( 0 ).$( By.cssSelector( "input" ) )
    // getSearchInputBoxArea().find( By.cssSelector("input[role='combobox']") ).setValue( "V" ).parent().parent().$( By.cssSelector( "div[role='listbox']" ) ).sendKeys( Keys.ARROW_DOWN)

}

