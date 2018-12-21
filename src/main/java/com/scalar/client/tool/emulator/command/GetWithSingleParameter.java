package com.scalar.client.tool.emulator.command;

import com.google.gson.JsonObject;
import com.scalar.client.tool.emulator.ContractRegistry;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import java.util.List;
import javax.inject.Inject;
import picocli.CommandLine;

@CommandLine.Command(
    name = GetWithSingleParameter.COMMAND_NAME,
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description = "Execute the get contract. This command is equivalent to 'execute get'.",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer =
        "The JSON argument format is {\"@|fg(blue) asset_id|@\":\"<an_asset_id>\"}.%n"
            + "For example: 'get -j {\"asset_id\":\"foo\"}'.%n")
public class GetWithSingleParameter extends AbstractCommand {
  static final String COMMAND_NAME = "get -j";
  public static final String CONTRACT_NAME = "get";

  @CommandLine.Parameters(
      arity = "1..*",
      paramLabel = "argument",
      description =
          "the JSON contract argument. A plain text JSON object or the path to a file containing a JSON object")
  private List<String> jsonParameter;

  @Inject
  public GetWithSingleParameter(
      TerminalWrapper terminal,
      ContractRegistry contractRegistry,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractRegistry, assetbase, ledger);
  }

  @Override
  public void run() {
    JsonObject json = convertJsonParameter(jsonParameter);
    if (json != null) {
      executeContract(CONTRACT_NAME, json);
    }
  }
}
