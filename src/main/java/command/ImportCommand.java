package command;

import app.CatConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.cli.Command;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

/**
 * Created by carl_downs on 10/9/14.
 */
public class ImportCommand extends ConfiguredCommand<CatConfiguration> {

    public ImportCommand () {
        super ("importParts", "imports supplier part files");
    }

    @Override
    public void configure(Subparser subparser) {

    }

    @Override
    protected void run(Bootstrap<CatConfiguration> bootstrap, Namespace namespace, CatConfiguration configuration) throws Exception {
        System.out.println ("cool, we are in our command");
    }
}
