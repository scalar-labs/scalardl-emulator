package com.scalar.client.tool.emulator;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.contract.ContractEntry;
import com.scalar.ledger.contract.ContractManager;
import com.scalar.ledger.exception.ContractValidationException;
import com.scalar.ledger.exception.MissingCertificateException;
import com.scalar.ledger.exception.MissingContractException;
import com.scalar.ledger.exception.RegistryException;
import com.scalar.ledger.exception.RegistryIOException;
import com.scalar.ledger.exception.SignatureException;
import com.scalar.ledger.exception.UnloadableContractException;
import com.scalar.ledger.exception.UnloadableKeyException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ContractManagerWrapper {
  private ContractManager manager;
  private final List<String> contractIds;

  public ContractManagerWrapper(ContractManager manager) {
    this.manager = manager;
    contractIds = new ArrayList<>();
  }

  public void register(String id, String name, File file, JsonObject properties) {
    try {
      byte[] contract = Files.readAllBytes(file.toPath());
      register(toContractEntry(id, name, contract, properties));
    } catch (IOException e) {
      throw new RegistryIOException("register error. unable to read file: " + e.getMessage());
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

  public void register(ContractEntry entry)
      throws RegistryException, ContractValidationException, SignatureException,
          UnloadableKeyException {
    contractIds.add(entry.getId());
    manager.register(entry);
  }

  public ContractEntry get(String id)
      throws MissingContractException, MissingCertificateException, RegistryIOException,
          SignatureException, ContractValidationException, UnloadableKeyException {
    return manager.get(id);
  }

  public Contract getInstance(String id) throws UnloadableContractException {
    return manager.getInstance(id);
  }

  public Optional<JsonObject> getProperties(String id) throws RegistryIOException {
    try {
      ContractEntry entry = manager.get(id);
      return entry.getProperties();
    } catch (Exception e) {
      throw new RegistryIOException("registry error: " + e.getMessage());
    }
  }

  public List<String> getContractIds() {
    return ImmutableList.copyOf(contractIds);
  }
}
