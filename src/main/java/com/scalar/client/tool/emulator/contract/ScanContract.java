package com.scalar.client.tool.emulator.contract;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.scalar.ledger.asset.Asset;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.database.AssetFilter;
import com.scalar.ledger.database.AssetFilter.VersionOrder;
import com.scalar.ledger.ledger.Ledger;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class ScanContract extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> property) {
    final Pattern startPattern = Pattern.compile("(^\\[|\\]{1})(\\d+$)");
    final Pattern endPattern = Pattern.compile("(^\\d+)(\\[|\\]{1}$)");
    final Pattern numberPattern = Pattern.compile("[^\\d]");
    final Pattern startInclusivePattern = Pattern.compile("\\[");
    final Pattern endInclusivePattern = Pattern.compile("\\]");
    JsonObject result = new JsonObject();
    AssetFilter filter;

    if (!argument.has("asset_id")) {
      result.addProperty("result", "failure");
      result.addProperty("message", "'asset_id' attribute is missing");
      return result;
    }

    String key = argument.get("asset_id").getAsString();
    filter = new AssetFilter(key);

    if (argument.has("start")) {
      String start = argument.get("start").getAsString();
      int startIndex = 0;
      boolean startInclusive = false;
      if (startPattern.matcher(start).matches()) {
        startIndex = Integer.parseInt(numberPattern.matcher(start).replaceAll(""));
        if (startInclusivePattern.matcher(start).find()) {
          startInclusive = true;
        }
        filter.withStartVersion(startIndex, startInclusive);
      } else {
        result.addProperty("result", "failure");
        result.addProperty("message", "Error parsing start option");
        return result;
      }
    }

    if (argument.has("end")) {
      String end = argument.get("end").getAsString();
      int endIndex = 0;
      boolean endInclusive = false;
      if (endPattern.matcher(end).matches()) {
        endIndex = Integer.parseInt(numberPattern.matcher(end).replaceAll(""));
        if (endInclusivePattern.matcher(end).find()) {
          endInclusive = true;
        }
        filter.withEndVersion(endIndex, endInclusive);
      } else {
        result.addProperty("result", "failure");
        result.addProperty("message", "Error parsing end option");
        return result;
      }
    }

    if (argument.has("limit")) {
      int limit = argument.get("limit").getAsInt();
      if (limit > 0) {
        filter.withLimit(limit);
      } else {
        result.addProperty("result", "failure");
        result.addProperty("message", "Error parsing limit option");
        return result;
      }
    }

    if (argument.has("asc_order")) {
      boolean ascendingOrder = argument.get("asc_order").getAsBoolean();
      if (ascendingOrder) {
        filter.withVersionOrder(VersionOrder.ASC);
      } else {
        filter.withVersionOrder(VersionOrder.DESC);
      }
    }

    List<Asset> history = ledger.scan(filter);
    JsonArray assets = new JsonArray();
    for (Asset asset : history) {
      JsonObject json = new JsonObject();
      json.addProperty("id", asset.id());
      json.addProperty("age", asset.age());
      json.add("data", asset.data());
      assets.add(json);
    }
    result.addProperty("result", "success");
    result.add("data", assets);
    return result;
  }
}
