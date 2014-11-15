package task;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.ImmutableMultimap;
import io.dropwizard.servlets.tasks.Task;
import supplier.SupplierSQL;

import java.io.PrintWriter;

/**
 * Created by carl_downs on 10/9/14.
 */
public class GenericTask extends Task {

    SupplierSQL dao;

    public GenericTask(SupplierSQL dao) {
        super("doit");
        this.dao = dao;
    }

    @Override
    @Timed
    @Metered
    @ExceptionMetered
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter out) throws Exception {
        out.println("doing stuff...");
        out.println("done doing stuff");
    }
}
