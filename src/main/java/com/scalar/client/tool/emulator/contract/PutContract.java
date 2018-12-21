package com.scalar.client.tool.emulator.contract;

import com.google.gson.JsonObject;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.exception.AssetbaseIOException;
import com.scalar.ledger.exception.ContractExecutionException;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;

public class PutContract extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> property)
      throws ContractExecutionException {
    JsonObject result = new JsonObject();
    if (!argument.has("asset_id")) {
      result.addProperty("result", "failure");
      result.addProperty("message", "'asset_id' attribute is missing");
      return result;
    }

    String assetId = argument.get("asset_id").getAsString();
    try {
      ledger.get(assetId);
    } catch (AssetbaseIOException e) {
      throw new ContractExecutionException("Error performing get: " + e.getMessage());
    }

    ledger.put(assetId, argument.get("data").getAsJsonObject());
    result.addProperty("result", "success");
    return result;
  }
}
