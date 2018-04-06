package sap.prd.cmintegration.cli;

import static sap.prd.cmintegration.cli.Commands.Helpers.getHost;
import static sap.prd.cmintegration.cli.Commands.Helpers.getPassword;
import static sap.prd.cmintegration.cli.Commands.Helpers.getUser;
import static sap.prd.cmintegration.cli.Commands.Helpers.handleHelpOption;
import static sap.prd.cmintegration.cli.Commands.Helpers.helpRequested;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProviderException;

import com.sap.cmclient.http.UnexpectedHttpResponseException;

import sap.prd.cmintegration.cli.TransportRelated.Opts;

@CommandDescriptor(name="export-transport", type = BackendType.ABAP)
public class ExportTransport extends Command {

    private final String transportId;

    public ExportTransport(String host, String user, String password, String transportId) {
        super(host, user, password);
        this.transportId = transportId;
    }

    @Override
    void execute() throws UnexpectedHttpResponseException, IOException, URISyntaxException, EntityProviderException, EdmException {
        AbapClientFactory.getInstance().newClient(host, user, password).releaseTransport(transportId);
    }

    public final static void main(String[] args) throws Exception {
        Options options = new Options();
        Command.addOpts(options);
        options.addOption(Commands.CMOptions.CHANGE_ID);
        options.addOption(Opts.TRANSPORT_ID);

        if(helpRequested(args)) {
            handleHelpOption(String.format("%s -%s %s", ExportTransport.class.getAnnotation(CommandDescriptor.class).name(),
                             Opts.TRANSPORT_ID.getOpt(),
                             Opts.TRANSPORT_ID.getArgName()),
                             "Exports a transport.", new Options()); return;
        }

        CommandLine commandLine = new DefaultParser().parse(options, args);

        new ExportTransport(
                getHost(commandLine),
                getUser(commandLine),
                getPassword(commandLine),
                TransportRelated.getTransportId(commandLine)).execute();
    }
}
