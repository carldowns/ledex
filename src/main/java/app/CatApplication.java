package app;

import app.resource.AssemblyResource;
import app.resource.PartResource;
import app.resource.SupplierResource;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import task.PingTask;

/**
 * Drop wizard main
 */
public class CatApplication extends Application<CatConfiguration> {


    public static void main(String[] args)
            throws Exception {

        new CatApplication().run(args);
    }

    @Override
    public String getName() {
        return "LED Exchange Catalog";
    }

    @Override
    public void initialize(Bootstrap<CatConfiguration> bootstrap) {
        bootstrap.addCommand(new CatCommand());
    }

    @Override
    public void run(CatConfiguration config, Environment env)
            throws ClassNotFoundException {
        Injector injector = Guice.createInjector(new CatModule(config, env));
        initTasks(injector, config, env);
        initResources(injector, config, env);
        initHealthChecks(injector, config, env);
    }

    private void initResources(Injector injector, CatConfiguration config, Environment env)
            throws ClassNotFoundException {
        env.jersey().register(injector.getInstance(SupplierResource.class));
        env.jersey().register(injector.getInstance(AssemblyResource.class));
        env.jersey().register(injector.getInstance(PartResource.class));
    }

    private void initTasks(Injector injector, CatConfiguration config, Environment env)
            throws ClassNotFoundException {
        env.admin().addTask(injector.getInstance(PingTask.class));
    }

    private void initHealthChecks(Injector injector, CatConfiguration config, Environment env) {
        env.healthChecks().register("general", injector.getInstance(CatHealth.class));
    }
}