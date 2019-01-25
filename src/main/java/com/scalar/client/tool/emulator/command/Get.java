package com.scalar.client.tool.emulator.command;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import javax.inject.Inject;
import javax.json.Json;
import picocli.CommandLine;

@CommandLine.Command(
    name = "get",
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description =
        "Execute the get contract using simplified parameter format. This command is equivalent to 'execute get'.",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer = "Usage example: 'get foo'.%n")
public class Get extends AbstractCommand {

  @CommandLine.Parameters(
      index = "0",
      paramLabel = "asset_id",
      description = "the asset id of the object stored on the Ledger. For example: 'foo'")
  private String assetId;

  @Inject
  public Get(
      TerminalWrapper terminal,
      ContractManagerWrapper contractManager,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractManager, assetbase, ledger);
  }

  @Override
  public void run() {
    executeContract(toKey("get"), Json.createObjectBuilder().add("asset_id", assetId).build());
  }
}
