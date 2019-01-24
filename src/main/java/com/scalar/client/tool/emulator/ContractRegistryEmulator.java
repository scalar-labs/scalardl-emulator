package com.scalar.client.tool.emulator;

import com.scalar.ledger.contract.ContractEntry;
import com.scalar.ledger.database.ContractRegistry;
import com.scalar.ledger.exception.MissingContractException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ContractRegistryEmulator implements ContractRegistry {
  private final Map<String, ContractEntry> contracts;

  public ContractRegistryEmulator() {
    contracts = new LinkedHashMap<>();
  }

  @Override
  public void bind(ContractEntry entry) {
    contracts.put(entry.getId(), entry);
  }

  @Override
  public void unbind(ContractEntry.Key key) {
    contracts.remove(key.getId());
  }

  @Override
  public ContractEntry lookup(ContractEntry.Key key) {
    if (contracts.containsKey(key.getId())) {
      return contracts.get(key.getId());
    }
    throw new MissingContractException("Contract " + key.getId() + " has not been registered");
  }

  @Override
  public List<ContractEntry> scan(String certId) {
    return scan(certId, 0);
  }

  @Override
  public List<ContractEntry> scan(String certId, int certVersion) {
    // emulator assumes all contracts are registered to the same user
    return new ArrayList<>(contracts.values());
  }
}
