package linkedInnAuto.setup;

import java.util.Properties;

public class WebDriverProperties {
    private static final String VERSION = "83.0.4103.39";
    private static final int DEFAULT_WIDTH = 1600;
    private static final int DEFAULT_HEIGHT = 900;
    private static final Properties properties = System.getProperties();

    public static String getVersion() {
        return VERSION;
    }

    public static int getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    public static int getDefaultHeight() {
        return DEFAULT_HEIGHT;
    }

    protected static String getWindowsSizeArgument() {
        int windowWidth = getDefaultWidth();
        int windowHeight = isWindows()
                ? getDefaultHeight()
                : getDefaultHeight() + 1;
        return "window-size=" + windowWidth + "," + windowHeight;
    }

    private static String getOperatingSystem() {
        return properties.getProperty("os.name").toLowerCase();
    }

    public static boolean isWindows() {
        return getOperatingSystem().contains("windows");
    }
}
