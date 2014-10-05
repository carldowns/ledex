package diary;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
//import com.example.helloworld.resources.HelloWorldResource;
//import com.example.helloworld.health.TemplateHealthCheck;

import org.skife.jdbi.v2.DBI;

/**
 * Dropwizard application
 * 
 * @author carl_downs
 * 
 */
public class DiaryApplication extends Application<DiaryConfiguration> {

    /**
     * Entry point for the application.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) 
            throws Exception {
        
        new DiaryApplication().run(args);
    }

    @Override
    public String getName() {
        return "diary";
    }

    /**
     * 
     */
    @Override
    public void initialize(Bootstrap<DiaryConfiguration> bootstrap) {
        // nothing to do yet
        //bootstrap.addBundle(bundle);
        //bootstrap.addCommand(command);
    }

    /**
     * 
     */
    @Override
    public void run(DiaryConfiguration config, Environment env)
            throws ClassNotFoundException {

        initResources(config, env);
        initHealthChecks(config, env);
    }

    /**
     * 
     * @param config
     * @param environment
     * @throws ClassNotFoundException
     */
    private void initResources(DiaryConfiguration config, Environment env) 
            throws ClassNotFoundException {

        final DBIFactory factory = new DBIFactory();
        final DBI dbi = factory.build(env, config.getDataSourceFactory(), "postgresql");
        
        final DiaryDAO dao = dbi.onDemand(DiaryDAO.class);
        env.jersey().register(new DiaryResource(dao));
    }
    
    /**
     * 
     * @param config
     * @param environment
     */
    private void initHealthChecks(DiaryConfiguration config, Environment env) {

        final DiaryHealth check = new DiaryHealth(
                config.getTemplate());

        env.healthChecks().register("template", check);
    }

}