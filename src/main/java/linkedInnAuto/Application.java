package linkedInnAuto;

import linkedInnAuto.service.ContactsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, XADataSourceAutoConfiguration.class})
public class Application implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger( Application.class );
    @Autowired
    private ContactsService contactsService;


    public static void main( String[] args ) {
        SpringApplication.run( Application.class, args );
    }

    @Override
    public void run( String... args ) throws Exception {
        LOG.info( "com.Application start" );
        contactsService.addNewContactsByCompany( "AMAZON" );
    }


}
