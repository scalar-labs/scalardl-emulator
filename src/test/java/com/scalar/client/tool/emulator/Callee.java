package com.scalar.client.tool.emulator;

import com.google.gson.JsonObject;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;

public class Callee extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    JsonObject result = new JsonObject();
    result.addProperty("called", true);

    return result;
  }
}
