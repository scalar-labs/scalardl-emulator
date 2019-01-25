package com.scalar.client.tool.emulator.command;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import java.util.List;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import picocli.CommandLine;

@CommandLine.Command(
    name = "put",
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description =
        "Execute the put contract using simplified parameter format. This command is equivalent to 'execute put'.",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer = "Usage example: 'put foo {\"x\": \"y\"}'.%n")
public class Put extends AbstractCommand {

  @CommandLine.Parameters(
      index = "0",
      paramLabel = "key",
      description = "the key of the object on the ledger")
  private String key;

  @CommandLine.Parameters(
      index = "1..*",
      arity = "1..*",
      paramLabel = "JSON_object",
      description =
          "the JSON object to be saved on the ledger, it can be a plain text JSON object or the path to a file containing a JSON object")
  private List<String> argument;

  @Inject
  public Put(
      TerminalWrapper terminal,
      ContractManagerWrapper contractManager,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractManager, assetbase, ledger);
  }

  @Override
  public void run() {
    JsonObject data = convertJsonParameter(argument);

    if (data != null) {
      executeContract(
          toKey("put"), Json.createObjectBuilder().add("asset_id", key).add("data", data).build());
    }
  }
}
