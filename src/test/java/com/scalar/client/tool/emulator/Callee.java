package com.scalar.client.tool.emulator;

import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class Callee extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    return Json.createObjectBuilder().add("called", true).build();
  }
}
