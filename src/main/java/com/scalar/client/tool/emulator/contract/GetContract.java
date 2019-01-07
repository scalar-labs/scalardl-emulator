package com.scalar.client.tool.emulator.contract;

import com.scalar.ledger.asset.Asset;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;

public class GetContract extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> property) {

    if (!argument.containsKey("asset_id")) {
      return Json.createObjectBuilder()
          .add("result", "failure")
          .add("message", "'asset_id' attribute is missing")
          .build();
    }

    String assetId = argument.getString("asset_id");
    Optional<Asset> asset = ledger.get(assetId);
    if (!asset.isPresent()) {
      return Json.createObjectBuilder()
          .add("result", "failure")
          .add("message", assetId + " is not in the ledger")
          .build();
    }

    return Json.createObjectBuilder()
        .add("asset_id", asset.get().id())
        .add("age", asset.get().age())
        .add("data", asset.get().data())
        .build();
  }
}
