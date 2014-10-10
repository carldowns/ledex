package app;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
//import com.example.helloworld.resources.HelloWorldResource;
//import com.example.helloworld.health.TemplateHealthCheck;

import org.skife.jdbi.v2.DBI;
import supplier.SupplierDAO;

/**
 * Dropwizard application
 * 
 * @author carl_downs
 * 
 */
public class CatApplication extends Application<CatConfiguration> {

    /**
     * Entry point for the application.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) 
            throws Exception {
        
        new CatApplication().run(args);
    }

    @Override
    public String getName() {
        return "LED Exchange Catalog";
    }

    /**
     * 
     */
    @Override
    public void initialize(Bootstrap<CatConfiguration> bootstrap) {
        // nothing to do yet
        //bootstrap.addBundle(bundle);
        //bootstrap.addCommand(command);
    }

    /**
     * 
     */
    @Override
    public void run(CatConfiguration config, Environment env)
            throws ClassNotFoundException {

        initResources(config, env);
        initHealthChecks(config, env);
    }

    /**
     * 
     * @param config
     * @param env
     * @throws ClassNotFoundException
     */
    private void initResources(CatConfiguration config, Environment env) 
            throws ClassNotFoundException {

        final DBIFactory factory = new DBIFactory();
        final DBI dbi = factory.build(env, config.getDataSourceFactory(), "postgresql");
        
        final SupplierDAO dao = dbi.onDemand(SupplierDAO.class);
        env.jersey().register(new SupplierResource(dao));
    }
    
    /**
     * 
     * @param config
     * @param env
     */
    private void initHealthChecks(CatConfiguration config, Environment env) {

        final CatHealth check = new CatHealth(
                config.getTemplate());

        env.healthChecks().register("template", check);
    }

}