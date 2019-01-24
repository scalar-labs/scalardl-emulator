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
    name = "scan -j",
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description = "Execute the scan contract. This command is equivalent to 'execute scan'",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer =
        "The JSON argument format is {\"@|fg(blue) asset_id|@\":\"<an_asset_id>\", \"@|fg(yellow) start|@\":\"<start>\", \"@|fg(yellow) end|@\":\"<end>\", \"@|fg(yellow) desc_order|@\":<desc_order>, \"@|fg(yellow) limit|@\":<limit>}%n"
            + "@|italic 'start', 'end', 'desc' and 'limit' are optional parameters.|@%n"
            + "- start: an open (inclusive) or closed (exclusive) bracket followed by a number. For example : '[3' will return assets from age >= 3.%n"
            + "- end: a number followed by an closed (inclusive) or open (exclusive) bracket. For example : '10[' will return assets with age < 10.%n"
            + "- desc_order: set to true to return assets in a descending order. The default order is ascending.%n"
            + "- limit: an integer > 0 which is the maximum number of assets returned. By default there is no limit.%n")
public class ScanWithSingleParameter extends AbstractCommand {

  @CommandLine.Parameters(
      arity = "1..*",
      paramLabel = "argument",
      description =
          "the JSON contract argument. A plain text JSON object or the path to a file containing a JSON object")
  private List<String> jsonParameter;

  @Inject
  public ScanWithSingleParameter(
      TerminalWrapper terminal,
      ContractManagerWrapper contractManager,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractManager, assetbase, ledger);
  }

  @Override
  public void run() {
    JsonObject json = convertJsonParameter(this.jsonParameter);
    if (json != null) {
      executeContract(toKey("scan"), json);
    }
  }
}
