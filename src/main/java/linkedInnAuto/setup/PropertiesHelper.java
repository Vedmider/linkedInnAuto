package linkedInnAuto.setup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class PropertiesHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger( PropertiesHelper.class );
    private static final Properties properties  = new Properties();
    @Autowired
    private Environment env;

    public String getProperty(String propertyKey){
        return env.getProperty( propertyKey );
    }
}
