package com.scalar.client.tool.emulator.command;

import com.scalar.client.tool.emulator.ContractRegistry;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import java.util.Map;
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
      ContractRegistry contractRegistry,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractRegistry, assetbase, ledger);
  }

  @Override
  public void run() {
    Map<String, Contract> contracts = contractRegistry.getContracts();
    if (contracts.isEmpty()) {
      terminal.println("No registered contracts");
    }
    for (Map.Entry<String, Contract> entry : contracts.entrySet()) {
      String id = entry.getKey();
      Contract contract = entry.getValue();
      String property =
          contractRegistry.getProperty(id).isPresent()
              ? ", property: " + contractRegistry.getProperty(id).get()
              : "";
      terminal.println("id: " + id + ", class name: " + contract.getClass().getName() + property);
    }
  }
}
