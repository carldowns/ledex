package task;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.dropwizard.servlets.tasks.Task;
import mgr.SupplierMgr;

import java.io.PrintWriter;

/**
 */
@Singleton
public class PingTask extends Task {

    SupplierMgr dao;

    @Inject
    public PingTask(SupplierMgr dao) {
        super("ping");
        this.dao = dao;
    }

    @Override
    @Timed
    @Metered
    @ExceptionMetered
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter out) throws Exception {
        out.println("pong");
    }
}
