package com.scalar.client.tool.emulator.contract;

import com.google.gson.JsonObject;
import com.scalar.ledger.asset.Asset;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;

public class GetContract extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> property) {
    JsonObject result = new JsonObject();

    if (!argument.has("asset_id")) {
      result.addProperty("result", "failure");
      result.addProperty("message", "'asset_id' attribute is missing");
      return result;
    }

    String assetId = argument.get("asset_id").getAsString();
    Optional<Asset> asset = ledger.get(assetId);
    if (!asset.isPresent()) {
      result.addProperty("result", "failure");
      result.addProperty("message", assetId + " is not in the ledger");
      return result;
    }

    result.addProperty("result", "success");
    result.addProperty("id", asset.get().id());
    result.addProperty("age", asset.get().age());
    result.add("data", asset.get().data());
    return result;
  }
}
