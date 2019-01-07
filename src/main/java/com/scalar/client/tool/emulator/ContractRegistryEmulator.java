package com.scalar.client.tool.emulator;

import com.scalar.ledger.contract.ContractEntry;
import com.scalar.ledger.database.SignedContractRegistry;
import com.scalar.ledger.exception.MissingContractException;

import java.util.HashMap;
import java.util.Map;

public class ContractRegistryEmulator implements SignedContractRegistry {
  private final Map<String, ContractEntry> contracts;

  public ContractRegistryEmulator() {
    contracts = new HashMap<>();
  }

  @Override
  public void bind(ContractEntry entry) {
    contracts.put(entry.getId(), entry);
  }

  @Override
  public void unbind(String id) {
    contracts.remove(id);
  }

  public ContractEntry lookup(String id) {
    if (contracts.containsKey(id)) {
      return contracts.get(id);
    }
    throw new MissingContractException("Contract " + id + " has not been registered");
  }
}
