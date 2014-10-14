package app;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
//import com.example.helloworld.resources.HelloWorldResource;
//import com.example.helloworld.health.TemplateHealthCheck;

import org.skife.jdbi.v2.DBI;
import supplier.SupplierDAO;
import supplier.SupplierSQL;
import task.GenericTask;

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

        // bootstrap.addBundle(new AssetsBundle("/assets/css", "/css", null, "css"));
        // bootstrap.addBundle(new AssetsBundle("/assets/js", "/js", null, "js"));
        // bootstrap.addBundle(new AssetsBundle("/assets/fonts", "/fonts", null, "fonts"));
        // bootstrap.addCommand(new ImportCommand());
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

        final SupplierSQL sql = dbi.onDemand(SupplierSQL.class);
        env.jersey().register(new SupplierResource(sql));

        final SupplierDAO dao2 = new SupplierDAO(dbi);
        env.jersey().register(new SupplierResource2(dao2));

        env.admin().addTask(new GenericTask(sql));
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