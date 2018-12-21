package com.scalar.client.tool.emulator;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.scalar.client.tool.emulator.contract.GetContract;
import com.scalar.client.tool.emulator.contract.PutContract;
import com.scalar.client.tool.emulator.contract.ScanContract;
import com.scalar.ledger.contract.Contract;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ContractRegistry {
  private final Map<String, Contract> contracts;
  private final Map<String, JsonObject> properties;

  public ContractRegistry() {
    contracts = new HashMap<>();
    properties = new HashMap<>();

    this.putContract("get", new GetContract());
    this.putContract("put", new PutContract());
    this.putContract("scan", new ScanContract());
  }

  public void putContract(String id, Contract contract) {
    contracts.put(id, contract);
  }

  public Optional<Contract> getContract(String id) {
    if (contracts.containsKey(id)) {
      return Optional.of(contracts.get(id));
    } else {
      return Optional.empty();
    }
  }

  public ImmutableMap<String, Contract> getContracts() {
    return ImmutableMap.copyOf(contracts);
  }

  public void putProperty(String id, JsonObject property) {
    properties.put(id, property);
  }

  public Optional<JsonObject> getProperty(String id) {
    if (properties.containsKey(id)) {
      return Optional.of(properties.get(id));
    } else return Optional.empty();
  }
}
