package com.scalar.client.tool.emulator.command;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import java.util.List;
import javax.inject.Inject;
import javax.json.JsonObject;
import picocli.CommandLine;

@CommandLine.Command(
    name = "put -j",
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description = "Execute the put contract. This command is equivalent to 'execute put'.",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer =
        "The JSON argument format is {\"@|fg(blue) asset_id|@\":\"<an_asset_id>\",\"@|fg(yellow) data|@\":<a_JSON_object>}.%n"
            + "For example :'"
            + PutWithSingleParameter.COMMAND_NAME
            + " {\"asset_id\":\"foo\",\"data\":{\"x\": \"y\"}}'%n")
public class PutWithSingleParameter extends AbstractCommand {
  static final String COMMAND_NAME = "put -j";

  @CommandLine.Parameters(
      arity = "1..*",
      paramLabel = "argument",
      description =
          "the JSON contract argument. A plain text JSON object or the path to a file containing a JSON object")
  private List<String> argument;

  @Inject
  public PutWithSingleParameter(
      TerminalWrapper terminal,
      ContractManagerWrapper contractManager,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractManager, assetbase, ledger);
  }

  @Override
  public void run() {
    JsonObject json = convertJsonParameter(argument);
    if (json != null) {
      executeContract(toKey("put"), json);
    }
  }
}
