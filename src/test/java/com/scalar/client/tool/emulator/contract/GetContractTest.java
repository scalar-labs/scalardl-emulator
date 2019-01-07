package com.scalar.client.tool.emulator.contract;

import static org.assertj.core.api.Assertions.assertThat;

import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.ledger.AssetLedger;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
import org.junit.Test;

public class GetContractTest {
  private static final String ASSET_ID = "X";
  private PutContract put = new PutContract();
  private GetContract get = new GetContract();
  private TransactionalAssetbase assetbase = new AssetbaseEmulator();
  private Ledger ledger = new AssetLedger(assetbase);

  private void addAssetToLedger() {
    JsonObject argument =
        Json.createObjectBuilder()
            .add("asset_id", ASSET_ID)
            .add("data", Json.createObjectBuilder().build())
            .build();
    put.invoke(ledger, argument, Optional.empty());
  }

  @Test
  public void invoke_AssetIdGiven_ShouldResultInSuccess() {
    // Arrange
    addAssetToLedger();
    JsonObject argument = Json.createObjectBuilder().add("asset_id", ASSET_ID).build();

    // Act
    JsonObject result = get.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.getString("asset_id")).isEqualTo(ASSET_ID);
  }

  @Test
  public void invoke_AssetNotInLedger_ShouldResultInFailure() {
    // Arrange
    JsonObject argument = Json.createObjectBuilder().add("asset_id", ASSET_ID).build();

    // Act
    JsonObject result = get.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.getString("result")).isEqualTo("failure");
    assertThat(result.getString("message")).endsWith("is not in the ledger");
  }

  @Test
  public void invoke_AssetIdNotGiven_ShouldResultInFailure() {
    // Arrange
    JsonObject argument = Json.createObjectBuilder().build();

    // Act
    JsonObject result = get.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.getString("result")).isEqualTo("failure");
    assertThat(result.getString("message")).isEqualTo("'asset_id' attribute is missing");
  }
}
