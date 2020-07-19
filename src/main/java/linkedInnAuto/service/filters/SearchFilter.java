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
import org.springframework.stereotype.Component;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@Component
public class SearchFilter {
    private WebDriver webDriver;

    public SearchFilter(){
    }

    public SearchFilter(WebDriver webDriver){
        this.webDriver = webDriver;
    }

    public void addAreaToFilters( String area ){
        $(CSSSelectors.getAreaFilterButton()).click();
        typeInSearchArea( area );
        findAreaInSearchResultsAndClick( area );

    }

    private void typeInSearchArea(String area){
        getSearchInputInArea().find( By.cssSelector("input[role='combobox']") ).setValue( area ).sendKeys( Keys.ENTER );
    }

    private SelenideElement getSearchInputInArea(){
        return $(CSSSelectors.getSearchInputBoxInSearchArea());
    }

    private List<SelenideElement> getAreasList(){
        return getSearchInputInArea().$$( By.cssSelector( "span[class='search-typeahead-v2__hit-text t-14 t-black ']" ) );
    }

    private void findAreaInSearchResultsAndClick(String area){
        getAreasList().stream().forEach( element -> {
            if ( element.getText().contains( area ) ){
                element.click();
            }
        } );

        ElementsCollection elements = getSearchInputInArea().$$( By.cssSelector( "input[id^='geoRegion']" ) );
        SelenideElement element = elements.get( 0 );

    }
}
