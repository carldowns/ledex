package app;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

/**
 *
 */
@Singleton
public class CatDBI {

    private final DBI dbi;

    @Inject
    public CatDBI(CatConfiguration config, Environment environment) throws ClassNotFoundException {
        this.dbi = new DBIFactory().build(environment, config.getDataSourceFactory(), "postgresql");
    }

    public DBI getDbi() {
        return dbi;
    }

}