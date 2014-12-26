package app;

import catalog.AssemblyEngine;
import catalog.dao.AssemblySQL;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.setup.Environment;
import mgr.CatalogMgr;
import mgr.SupplierMgr;
import part.dao.PartSQL;
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

        // inject DAO managers
        bind(CatalogMgr.class);
        bind(SupplierMgr.class);

        // inject engines
        bind(AssemblyEngine.class);

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
}
