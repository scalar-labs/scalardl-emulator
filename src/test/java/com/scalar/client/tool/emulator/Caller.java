package com.scalar.client.tool.emulator;

import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class Caller extends Contract {
  private static final String CONTRACT_ID_ATTRIBUTE_NAME = "contract_id";

  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    String contractId = argument.getString(CONTRACT_ID_ATTRIBUTE_NAME);

    return invoke(contractId, ledger, Json.createObjectBuilder().build());
  }
}
