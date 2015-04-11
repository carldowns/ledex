package app;

//import ch.qos.logback.classic.Logger;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class CatCommand extends ConfiguredCommand<CatConfiguration> {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(CatCommand.class);

    public CatCommand() {
        super("generate-catalog", "generates (or regenerates) the catalog based on the installed assemblies and parts");
    }

    @Override
    public void configure(Subparser subparser) {
        super.configure(subparser); // this is key!  without it, the command does not recognize the yml file
    }

    @Override
    protected void run(Bootstrap<CatConfiguration> bootstrap, Namespace namespace, CatConfiguration configuration) throws Exception {
        logger.info("TODO: implement this command!");
    }
}
