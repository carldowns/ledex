package hello;

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
public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    /**
     * Entry point for the application.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        // nothing to do yet
        //bootstrap.addBundle(bundle);
        //bootstrap.addCommand(command);
    }

    @Override
    public void run(HelloWorldConfiguration config, Environment env)
            throws ClassNotFoundException {

        initResources(config, env);
        initHealthChecks(config, env);
        initDatabase(config, env);
    }

    /**
     * 
     * @param config
     * @param env
     */
    private void initResources(HelloWorldConfiguration config, Environment env) {

        final HelloWorldResource resource = new HelloWorldResource(
                config.getTemplate(), config.getDefaultName());

        env.jersey().register(resource);
    }

    /**
     * 
     * @param config
     * @param env
     */
    private void initHealthChecks(HelloWorldConfiguration config, Environment env) {

        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(
                config.getTemplate());

        env.healthChecks().register("template", healthCheck);
    }

    /**
     * 
     * @param config
     * @param environment
     * @throws ClassNotFoundException
     */
    private void initDatabase(HelloWorldConfiguration config,
            Environment environment) throws ClassNotFoundException {

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment,
                config.getDataSourceFactory(), "postgresql");

        jdbi.onDemand(MyDAO.class);
        // final MyDAO dao = jdbi.onDemand(MyDAO.class);
        // environment.jersey().register(new UserResource(dao));
    }
}