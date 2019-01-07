package com.scalar.client.tool.emulator;

import com.google.gson.JsonObject;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;

public class Caller extends Contract {
  private static final String CONTRACT_ID_ATTRIBUTE_NAME = "contract_id";

  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    String contractId = argument.get(CONTRACT_ID_ATTRIBUTE_NAME).getAsString();

    return invoke(contractId, ledger, new JsonObject());
  }
}
