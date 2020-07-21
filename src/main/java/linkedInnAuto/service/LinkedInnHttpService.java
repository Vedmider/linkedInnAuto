package linkedInnAuto.service;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import linkedInnAuto.service.filters.AreaCode;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class LinkedInnHttpService {
    private static final String PEOPLE_SEARCH_URL = "https://www.linkedin.com/search/results/people/?";
    private static final String SEARCH_PARAM = "keywords";
    private static final String ORIGIN_FACETED_SEARCH = "origin=FACETED_SEARCH";
    private static final String GEO_REGION_PARAM = "facetGeoRegion";
    private static final String GEO_REGION_PATTERN = "(facetGeoRegion.+?[&$])";
    private static final String PEOPLE_SEARCH_PARAM_PATTERN = "(keywords.*?[&$])";
    private static final String JSON_ARRAY_PATTERN = "\\[.+?\\]";
    private Pattern pattern;
    private Matcher matcher;
    private String tempString;
    private String url;


    public LinkedInnHttpService searchPeopleByCriteria( String criteria ) {
        url = WebDriverRunner.url();
        if ( url.contains( SEARCH_PARAM ) && url.contains( PEOPLE_SEARCH_URL )) {
            tempString = getParamFromUrl( url, PEOPLE_SEARCH_PARAM_PATTERN );
            url.replace( tempString, SEARCH_PARAM + "=" + criteria + "&" );
        } else {
            url = PEOPLE_SEARCH_URL + SEARCH_PARAM + "=" + criteria;
        }
        WebDriverRunner.getWebDriver().get( url );
        waitForPageLoaded();
        return this;
    }

    public LinkedInnHttpService addAreaToFilter( AreaCode area ){
        String jsonArray;
        List<String> jsonList;
        String areaCode = area.getAreaCode();
        url = java.net.URLDecoder.decode( WebDriverRunner.url(), StandardCharsets.UTF_8);
        if ( url.contains( GEO_REGION_PARAM ) && url.contains( PEOPLE_SEARCH_URL ) ) {
            tempString = getParamFromUrl( url, GEO_REGION_PATTERN );
            jsonArray = getSubstringByRegex( tempString, JSON_ARRAY_PATTERN );
            jsonList = parseJSONString( jsonArray );
            if ( jsonList.contains( areaCode ) ){
                return this;
            }
            jsonList.add( areaCode );
            jsonArray = convertListToJSONString( jsonList );
            url = url.replace( tempString, GEO_REGION_PARAM + "=" + jsonArray + "&" );
            url = url.replaceAll( "&{2,}", "&" );
            url = url.replaceFirst( "&$", "" );
        } else {
            url = url + "&" + GEO_REGION_PARAM + "=[\"" + areaCode + "\"]" + "&" + ORIGIN_FACETED_SEARCH;
        }
        WebDriverRunner.getWebDriver().get( url );
        waitForPageLoaded();
        return this;
    }

    private void waitForPageLoaded(){
        new WebDriverWait(WebDriverRunner.getWebDriver(), 10)
                .until( driver -> ((JavascriptExecutor)driver).executeScript( "return document.readyState").equals( "complete"));
    }

    private List<String> parseJSONString(String jsonString){
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        List<String> stringList = new Gson().fromJson( jsonString, listType );
        return stringList;
    }

    private String getParamFromUrl(String url, String paramPattern){
        pattern = Pattern.compile( paramPattern );
        matcher = pattern.matcher( url );
        matcher.find();
        return url.substring( matcher.start(), matcher.end() );
    }

    private String convertListToJSONString(List<String> jsonList){
        return new Gson().toJson( jsonList );
    }

    private String getSubstringByRegex(String source, String pattern){
        return getParamFromUrl( source, pattern );
    }

}
