package com.scalar.client.tool.emulator.command;

import com.google.gson.JsonObject;
import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import javax.inject.Inject;
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
    footer = "For example:%n" + "- 'scan foo'%n" + "- 'scan foo -a -s [2 -e 5[ -l 2'%n")
public class Scan extends AbstractCommand {
  static final String COMMAND_NAME = "scan";

  @CommandLine.Option(
      names = {"-s", "--start"},
      description =
          "an open (inclusive) or closed (exclusive) bracket followed by a number. For example: '[3' will return assets from age >= 3")
  private String start;

  @CommandLine.Option(
      names = {"-e", "--end"},
      description =
          "a number followed by an closed (inclusive) or open (exclusive) bracket. For example: '10[' will return assets with age < 10")
  private String end;

  @CommandLine.Option(
      names = {"-a", "--ascending"},
      description =
          "add this flag to return assets in a ascending order. The default order is descending.")
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
    JsonObject argument = new JsonObject();
    argument.addProperty("asset_id", assetId);

    if (ascendingOrder) {
      argument.addProperty("asc_order", true);
    }

    if (start != null) {
      argument.addProperty("start", start);
    }

    if (end != null) {
      argument.addProperty("end", end);
    }

    if (limit > 0) {
      argument.addProperty("limit", limit);
    }

    executeContract(COMMAND_NAME, argument);
  }
}
