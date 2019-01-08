package com.scalar.client.tool.emulator.contract;

import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.database.AssetFilter;
import com.scalar.ledger.database.AssetFilter.VersionOrder;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import java.util.regex.Pattern;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class ScanContract extends Contract {
  @Override
  public JsonObject invoke(Ledger ledger, JsonObject argument, Optional<JsonObject> property) {
    final Pattern startPattern = Pattern.compile("(^\\[|\\]{1})(\\d+$)");
    final Pattern endPattern = Pattern.compile("(^\\d+)(\\[|\\]{1}$)");
    final Pattern numberPattern = Pattern.compile("[^\\d]");
    final Pattern startInclusivePattern = Pattern.compile("\\[");
    final Pattern endInclusivePattern = Pattern.compile("\\]");

    if (!argument.containsKey("asset_id")) {
      return Json.createObjectBuilder()
          .add("result", "failure")
          .add("message", "'asset_id' attribute is missing")
          .build();
    }

    String assetId = argument.getString("asset_id");
    AssetFilter filter = new AssetFilter(assetId);

    if (argument.containsKey("start")) {
      String start = argument.getString("start");
      int startIndex = 0;
      boolean startInclusive = false;
      if (startPattern.matcher(start).matches()) {
        startIndex = Integer.parseInt(numberPattern.matcher(start).replaceAll(""));
        if (startInclusivePattern.matcher(start).find()) {
          startInclusive = true;
        }
        filter.withStartVersion(startIndex, startInclusive);
      } else {
        return Json.createObjectBuilder()
            .add("result", "failure")
            .add("message", "Error parsing start option")
            .build();
      }
    }

    if (argument.containsKey("end")) {
      String end = argument.getString("end");
      int endIndex = 0;
      boolean endInclusive = false;
      if (endPattern.matcher(end).matches()) {
        endIndex = Integer.parseInt(numberPattern.matcher(end).replaceAll(""));
        if (endInclusivePattern.matcher(end).find()) {
          endInclusive = true;
        }
        filter.withEndVersion(endIndex, endInclusive);
      } else {
        return Json.createObjectBuilder()
            .add("result", "failure")
            .add("message", "Error parsing end option")
            .build();
      }
    }

    if (argument.containsKey("limit")) {
      int limit = argument.getInt("limit");
      if (limit > 0) {
        filter.withLimit(limit);
      } else {
        return Json.createObjectBuilder()
            .add("result", "failure")
            .add("message", "Error parsing limit option")
            .build();
      }
    }

    if (argument.containsKey("asc_order")) {
      boolean ascendingOrder = argument.getBoolean("asc_order");
      if (ascendingOrder) {
        filter.withVersionOrder(VersionOrder.ASC);
      } else {
        filter.withVersionOrder(VersionOrder.DESC);
      }
    }

    JsonArrayBuilder assets = Json.createArrayBuilder();
    ledger
        .scan(filter)
        .forEach(
            asset -> {
              JsonObject json =
                  Json.createObjectBuilder()
                      .add("asset_id", asset.id())
                      .add("age", asset.age())
                      .add("data", asset.data())
                      .build();
              assets.add(json);
            });

    return Json.createObjectBuilder().add("data", assets.build()).build();
  }
}
