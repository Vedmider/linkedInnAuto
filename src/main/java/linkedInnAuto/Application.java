package linkedInnAuto;

import linkedInnAuto.service.ContactsService;
import linkedInnAuto.service.LinkedInnHttpService;
import linkedInnAuto.service.filters.AreaCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class})
public class Application implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger( Application.class );
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private LinkedInnHttpService linkedInnHttpService;

    public static void main( String[] args ) {
        SpringApplication.run( Application.class, args );
    }

    @Override
    public void run( String... args ) throws Exception {
        LOG.info( "linkedInn.Application start" );
        contactsService.login();
        linkedInnHttpService.searchPeopleByCriteria( "AMAZON" )
                            .addAreaToFilter( AreaCode.USA )
                            .addAreaToFilter( AreaCode.CANADA );

    }


}
