package com.scalar.client.tool.emulator.command;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.ledger.Ledger;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

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
    Contract contract = contractManager.getInstance(id);
    JsonObject response = contract.invoke(this.ledger, argument, contractManager.getProperties(id));
    this.assetbase.commit();
    if (response != null) {
      Map<String, Object> properties = new HashMap<>();
      properties.put(JsonGenerator.PRETTY_PRINTING, true);
      JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
      StringWriter stringWriter = new StringWriter();
      JsonWriter jsonWriter = writerFactory.createWriter(stringWriter);
      jsonWriter.writeObject(response);
      terminal.println(stringWriter.toString());
    }
  }

  JsonObject convertJsonParameter(List<String> values) {
    String text = values.stream().reduce("", (a, b) -> a = a + " " + b);
    try {
      if (text.contains(File.separator)) {
        text = new String(Files.readAllBytes(new File(text).toPath()));
      }

      JsonReader reader = Json.createReader(new StringReader(text));
      return reader.readObject();
    } catch (IOException e) {
      terminal.println("Error parsing json parameter: " + text);
      terminal.println(e.toString());
    }
    return null;
  }
}
