package app;

import cmd.CmdMgr;
import cmd.ICmdHandler;
import cmd.dao.CmdSQL;
import catalog.dao.AssemblySQL;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import io.dropwizard.setup.Environment;
import mgr.AssemblyMgr;
import mgr.CatalogMgr;
import mgr.SupplierMgr;
import part.dao.PartSQL;
import project.ProjectCommentHandler;
import project.ProjectStartHandler;
import quote.handler.*;
import supplier.dao.SupplierSQL;
import task.PingTask;

/**
 * Injection Module
 */
public class CatModule extends AbstractModule {

    final CatConfiguration config;
    final Environment env;

    public CatModule (final CatConfiguration config, final Environment env) {
        this.config = config;
        this.env = env;
    }

    protected void configure() {

        // inject the configuration and environment
        bind(CatConfiguration.class).toInstance(config);
        bind(Environment.class).toInstance(env);

        // inject database connection pool
        bind(CatDBI.class);

        // inject managers
        bind(CatalogMgr.class);
        bind(SupplierMgr.class);
        bind(AssemblyMgr.class);
        bind(CmdMgr.class);

        // inject tasks
        bind(PingTask.class);

        // inject health checks
        bind(CatHealth.class);

        // inject quote handler interfaces
        bind(QuotePartResolver.class);
        bind(QuoteIncrementResolver.class);
        bind(QuoteChoiceResolver.class);
        bind(QuoteProductCostResolver.class);
        bind(QuoteProductPriceResolver.class);
        bind(QuoteSkuLabelResolver.class);

        Multibinder<ICmdHandler> multiBinder = Multibinder.newSetBinder(binder(), ICmdHandler.class);;
        multiBinder.addBinding().to(ProjectStartHandler.class);
        multiBinder.addBinding().to(ProjectCommentHandler.class);
    }

    @Provides
    AssemblySQL getAssemblySQL (CatDBI connectionPool) {
        return connectionPool.getDbi().onDemand(AssemblySQL.class);
    }

    @Provides
    SupplierSQL getSupplierSQL (CatDBI connectionPool) {
        return connectionPool.getDbi().onDemand(SupplierSQL.class);
    }

    @Provides
    PartSQL getPartSQL (CatDBI connectionPool) {
        return connectionPool.getDbi().onDemand(PartSQL.class);
    }

    @Provides
    CmdSQL getCmdSQL (CatDBI connectionPool) {
        return connectionPool.getDbi().onDemand(CmdSQL.class);
    }

}
