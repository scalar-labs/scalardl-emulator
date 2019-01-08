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

public class PutContractTest {
  private static final String ASSET_ID = "X";
  private PutContract contract = new PutContract();
  private TransactionalAssetbase assetbase = new AssetbaseEmulator();
  private Ledger ledger = new AssetLedger(assetbase);

  @Test
  public void invoke_AssetIdAndDataGiven_ShouldResultInSuccess() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder()
            .add("asset_id", ASSET_ID)
            .add("data", Json.createObjectBuilder().build())
            .build();

    // Act
    JsonObject result = contract.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result).isEqualTo(null);
  }

  @Test
  public void invoke_AssetIdNotGiven_ShouldResultInFailure() {
    // Arrange
    JsonObject argument = Json.createObjectBuilder().build();

    // Act
    JsonObject result = contract.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.getString("result")).isEqualTo("failure");
  }
}
