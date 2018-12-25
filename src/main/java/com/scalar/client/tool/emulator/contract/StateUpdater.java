package com.scalar.client.tool.emulator.contract;

import com.google.gson.JsonObject;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.exception.AssetbaseIOException;
import com.scalar.ledger.ledger.Ledger;
import com.scalar.ledger.asset.Asset;
import java.util.Optional;

public class StateUpdater extends Contract {

  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> properties) {
    JsonObject json = new JsonObject();
    String assetId = argument.get("asset_id").getAsString();
    int state = argument.get("state").getAsInt();

    Optional<Asset> asset = null;
    try {
      asset = ledger.get(assetId);
    } catch (AssetbaseIOException e) {
      JsonObject response = new JsonObject();
      response.addProperty("error_message", e.getMessage());
      return response;
    }

    if (!asset.isPresent() || asset.get().data().get("state").getAsInt() != state) {
      json.addProperty("state", state);
      ledger.put(assetId, json);
    }

    return new JsonObject();
  }
}
