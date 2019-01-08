package com.scalar.client.tool.emulator.contract;

import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class PutContract extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> property) {
    if (!argument.containsKey("asset_id")) {
      return Json.createObjectBuilder()
          .add("result", "failure")
          .add("message", "'asset_id' attribute is missing")
          .build();
    }

    String assetId = argument.getString("asset_id");
    ledger.get(assetId);
    ledger.put(assetId, argument.getJsonObject("data"));
    return null;
  }
}
