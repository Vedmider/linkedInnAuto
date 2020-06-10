package linkedInnAuto.constants;

import org.openqa.selenium.By;

public class CSSSelectors {
    private static final By LOGIN_SELECTOR = By.cssSelector( "input#username" );
    private static final By PASSWORD_SELECTOR = By.cssSelector( "input#password" );

    public static By getSignInButton() {
        return SIGN_IN_BUTTON;
    }

    private static final By SIGN_IN_BUTTON = By.cssSelector( "button.btn__primary--large.from__button--floating" );

    public static By getLoginSelector() {
        return LOGIN_SELECTOR;
    }

    public static By getPasswordSelector() {
        return PASSWORD_SELECTOR;
    }
}
