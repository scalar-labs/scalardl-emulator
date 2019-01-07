package com.scalar.client.tool.emulator.command;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import java.util.List;
import javax.inject.Inject;
import picocli.CommandLine;

@CommandLine.Command(
    name = "list-contracts",
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description = "List the registered contracts",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n")
public class ListContracts extends AbstractCommand {

  @Inject
  public ListContracts(
      TerminalWrapper terminal,
      ContractManagerWrapper contractManager,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractManager, assetbase, ledger);
  }

  @Override
  public void run() {
    List<String> contractsIds = contractManager.getContractIds();
    if (contractsIds.isEmpty()) {
      terminal.println("No registered contracts");
    }
    contractsIds.forEach(
        id -> {
          String properties =
              contractManager.getProperties(id).isPresent()
                  ? ", properties: " + contractManager.getProperties(id).get()
                  : "";
          terminal.println("id: " + id + properties);
        });
  }
}
