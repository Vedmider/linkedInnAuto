package linkedInnAuto.service;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import linkedInnAuto.page.PageObject;
import linkedInnAuto.service.filters.SearchFilter;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ContactsService {
    private static final Logger LOG = LoggerFactory.getLogger(ContactsService.class);
    @Autowired
    PageObject pageObject;
    @Autowired
    SearchFilter searchFilter;
    @Autowired
    ConfigurableApplicationContext context;


    public void findNewContactsByCompany( String companyName ) {
        try {
            pageObject.login();
            searchEmployeesByCriteria( companyName );
            pageObject.clickPeopleOnlyInSearchResults();
        } catch ( Exception e ){
            LOG.error( "Selenium error. Will init application shutdown", e );
            WebDriverRunner.closeWindow();
            WebDriverRunner.closeWebDriver();
            context.close();
        }
    }

    private void searchEmployeesByCriteria( String criteria ) {
        try{
            pageObject.typeCriteriaInSearch( criteria );
        } catch ( Exception e ){
            LOG.error( "Selenium error. Will init application shutdown", e );
            WebDriverRunner.closeWindow();
            WebDriverRunner.closeWebDriver();
            context.close();
        }
    }

    public void addAreaToFilters( String area ) {
        try{
            searchFilter.addAreaToFilters(area);
        }catch ( NoSuchElementException e ){
            LOG.error( "Selenium error. Will init application shutdown", e );
            WebDriverRunner.closeWindow();
            WebDriverRunner.closeWebDriver();
            context.close();
        }
    }

    public void login(){
        pageObject.login();
    }

    public ContactsService addContacts(int contactsNumber){
        pageObject.addContactsFromSearchResultsPage( contactsNumber );
        return this;
    }
}
