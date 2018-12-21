package com.scalar.client.tool.emulator.command;

import com.google.gson.JsonObject;
import com.scalar.client.tool.emulator.ContractClassLoader;
import com.scalar.client.tool.emulator.ContractRegistry;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import java.io.File;
import java.util.List;
import javax.inject.Inject;
import picocli.CommandLine;

@CommandLine.Command(
    name = "register",
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    synopsisHeading = "",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description = "Register a contract",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer =
        "For example: 'register ./Get.class com.scalar.client.tool.emulator.contract.Get get'%n")
public class Register extends AbstractCommand implements Runnable {
  @CommandLine.Parameters(
      index = "0",
      paramLabel = "id",
      description =
          "id that will be used when executing the contract with the execute command, e.g. 'get'")
  private String id;

  @CommandLine.Parameters(
      index = "1",
      paramLabel = "name",
      description = "contract canonical name, e.g. 'com.scalar.client.tool.emulator.contract.Get'")
  private String name;

  @CommandLine.Parameters(
      index = "2",
      paramLabel = "file",
      description =
          "compiled contract class file, e.g. 'build/java/main/com/scalar/client/tool/emulator/contract/Get.class'")
  private File file;

  @CommandLine.Parameters(
      index = "3..*",
      paramLabel = "contract_property",
      description =
          "the JSON contract property. A plain text JSON object or the path to a file containing a JSON object. For example: {\"x\": \"y\"}")
  private List<String> contractProperty;

  @Inject
  public Register(
      TerminalWrapper terminal,
      ContractRegistry contractRegistry,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    super(terminal, contractRegistry, assetbase, ledger);
  }

  @Override
  public void run() {
    if (contractRegistry.getContract(id).isPresent()) {
      terminal.println("Error: contract '" + id + "' has already been registered");
      return;
    }

    try {
      ContractClassLoader loader =
          new ContractClassLoader(terminal.getTerminal().writer(), this.name);
      Class<?> loadedClass = loader.load(this.file, this.name);
      // Test if the loaded class extends from Contract
      if (Contract.class.isAssignableFrom(loadedClass)) {
        Contract contract = (Contract) loadedClass.getConstructor().newInstance();
        contractRegistry.putContract(id, contract);
      } else {
        terminal.println("Error: the loaded class does not inherit from Contract class");
        return;
      }
      if (contractProperty != null) {
        JsonObject property = convertJsonParameter(contractProperty);
        contractRegistry.putProperty(id, property);
      }
      terminal.println("Registration success");
    } catch (Exception e) {
      terminal.println("Contract registration error: " + e.getMessage());
      terminal.println("Failed to register " + name + " -> " + id);
    }
  }
}
