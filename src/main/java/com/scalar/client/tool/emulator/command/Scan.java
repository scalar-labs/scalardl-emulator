package com.scalar.client.tool.emulator.command;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import picocli.CommandLine;

@CommandLine.Command(
    name = Scan.COMMAND_NAME,
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description = "Execute the scan contract. This command is equivalent to 'execute scan'.",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer = "For example:%n" + "- 'scan foo'%n" + "- 'scan foo -ascending -start 2 -end 5 -l 2'%n")
public class Scan extends AbstractCommand {
  static final String COMMAND_NAME = "scan";

  @CommandLine.Option(
      names = {"-s", "--start"},
      description = "return only assets with age >= start")
  private int start;

  @CommandLine.Option(
      names = {"-e", "--end"},
      description = "return only assets with age < end")
  private int end;

  @CommandLine.Option(
      names = {"-a", "--ascending"},
      description =
          "add this flag to return assets in ascending order. The default order is descending.")
  private boolean ascendingOrder;

  @CommandLine.Option(
      names = {"-l", "--limit"},
      description =
          "an integer > 0 which is the maximum number of assets returned. By default there is no limit")
  private int limit;

  @CommandLine.Parameters(
      index = "0",
      paramLabel = "assetId",
      description = "the asset id of the object on the ledger")
  private String assetId;

  @Inject
  public Scan(
      TerminalWrapper terminal,
      ContractManagerWrapper contractManager,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractManager, assetbase, ledger);
  }

  @Override
  public void run() {
    JsonObjectBuilder argument = Json.createObjectBuilder();
    argument.add("asset_id", assetId);

    if (ascendingOrder) {
      argument.add("asc_order", true);
    }

    if (start > 0) {
      argument.add("start", start);
    }

    if (end > 0) {
      argument.add("end", end);
    }

    if (limit > 0) {
      argument.add("limit", limit);
    }

    executeContract(COMMAND_NAME, argument.build());
  }
}
