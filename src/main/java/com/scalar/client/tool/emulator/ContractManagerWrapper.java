package com.scalar.client.tool.emulator;

import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.contract.ContractEntry;
import com.scalar.ledger.contract.ContractManager;
import com.scalar.ledger.crypto.CertificateEntry;
import com.scalar.ledger.exception.RegistryIOException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import javax.json.JsonObject;

public class ContractManagerWrapper {
  private ContractManager manager;

  public ContractManagerWrapper(ContractManager manager) {
    this.manager = manager;
  }

  public void register(String id, String name, File file, JsonObject properties) {
    try {
      byte[] contract = Files.readAllBytes(file.toPath());
      register(toContractEntry(id, name, contract, properties));
    } catch (IOException e) {
      throw new RegistryIOException("could not register contract " + id);
    }
  }

  private ContractEntry toContractEntry(
      String id, String name, byte[] contract, JsonObject properties) {
    return new ContractEntry(
        id,
        name,
        "holder_id",
        1,
        contract,
        properties,
        System.currentTimeMillis(),
        "signature".getBytes());
  }

  public void register(ContractEntry entry) {
    manager.register(entry);
  }

  public ContractEntry get(ContractEntry.Key key) {
    return manager.get(key);
  }

  public Contract getInstance(ContractEntry.Key key) {
    return manager.getInstance(key);
  }

  public List<ContractEntry> scan() {
    CertificateEntry.Key certKey = new CertificateEntry.Key("holderId", 0);
    return manager.scan(certKey);
  }
}
