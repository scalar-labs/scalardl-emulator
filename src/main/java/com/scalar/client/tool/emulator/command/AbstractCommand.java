package com.scalar.client.tool.emulator.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.exception.LedgerException;
import com.scalar.ledger.exception.UnloadableContractException;
import com.scalar.ledger.ledger.Ledger;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.List;
import picocli.CommandLine;

public abstract class AbstractCommand implements Runnable {
  TerminalWrapper terminal;
  ContractManagerWrapper contractManager;
  TransactionalAssetbase assetbase;
  Ledger ledger;

  @CommandLine.Option(
      names = {"-h", "--help"},
      description = "print the help and exit",
      usageHelp = true)
  private boolean help;

  public AbstractCommand(
      TerminalWrapper terminal,
      ContractManagerWrapper contractManager,
      TransactionalAssetbase assetbase,
      Ledger ledger) {
    this.terminal = terminal;
    this.contractManager = contractManager;
    this.assetbase = assetbase;
    this.ledger = ledger;
  }

  void executeContract(String id, JsonObject argument) {
    try {
      Contract contract = contractManager.getInstance(id);
      JsonObject response =
          contract.invoke(this.ledger, argument, contractManager.getProperties(id));
      this.assetbase.commit();
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      terminal.println(gson.toJson(response));
    } catch (UnloadableContractException e) {
      terminal.println("Could not load contract " + id);
    } catch (LedgerException e) {
      terminal.println("Contract execution error: " + e.getMessage());
    }
  }

  JsonObject convertJsonParameter(List<String> values) {
    String text = values.stream().reduce("", (a, b) -> a = a + " " + b);
    try {
      if (text.contains(File.separator)) {
        text = new String(Files.readAllBytes(new File(text).toPath()));
      }
      JsonReader reader = new JsonReader(new StringReader(text));
      reader.setLenient(true);
      return new JsonParser().parse(reader).getAsJsonObject();
    } catch (IOException | JsonSyntaxException | JsonIOException | IllegalStateException e) {
      terminal.println("Error parsing json parameter: " + text);
      terminal.println(e.toString());
    }
    return null;
  }
}
