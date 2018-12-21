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
    name = "execute",
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description = "Execute a contract that has been with the 'register' command.",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer = "For example: 'execute get {\"asset_id\":\"foo\"}'%n")
public class Execute extends AbstractCommand {
  @CommandLine.Parameters(
      index = "0",
      paramLabel = "id",
      description = "contract id. Use 'list-contract' to list registered contract id.")
  private String id;

  @CommandLine.Parameters(
      index = "1..*",
      arity = "1..*",
      paramLabel = "argument",
      description =
          "the JSON contract argument. A plain text JSON object or the path to a file containing a JSON object")
  private List<String> jsonOption;

  @Inject
  public Execute(
      TerminalWrapper terminal,
      ContractRegistry contractRegistry,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractRegistry, assetbase, ledger);
  }

  @Override
  public void run() {
    JsonObject json = convertJsonParameter(jsonOption);
    if (json != null) {
      executeContract(id, json);
    }
  }
}
