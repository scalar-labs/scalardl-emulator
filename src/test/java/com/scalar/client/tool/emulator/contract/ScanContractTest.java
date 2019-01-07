package com.scalar.client.tool.emulator.contract;

import static org.assertj.core.api.Assertions.assertThat;

import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.ledger.AssetLedger;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import org.junit.Before;
import org.junit.Test;

public class ScanContractTest {
  private static final String ASSET_ID = "X";
  private static final String WRONG_ASSET_ID = "Y";
  private PutContract put = new PutContract();
  private ScanContract scan = new ScanContract();
  private TransactionalAssetbase assetbase = new AssetbaseEmulator();
  private Ledger ledger = new AssetLedger(assetbase);

  private void putAnAgeTwoAssetInTheLedger() {
    JsonObject argument =
        Json.createObjectBuilder()
            .add("asset_id", ASSET_ID)
            .add("data", Json.createObjectBuilder().build())
            .build();
    put.invoke(ledger, argument, Optional.empty());
    assetbase.commit();
    put.invoke(ledger, argument, Optional.empty());
    assetbase.commit();
  }

  @Before
  public void setUp() {
    putAnAgeTwoAssetInTheLedger();
  }

  @Test
  public void invoke_AssetExistsAndIdGiven_ShouldResultInSuccessWithAsset() {
    // Arrange
    JsonObject argument = Json.createObjectBuilder().add("asset_id", ASSET_ID).build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(2);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
  }

  @Test
  public void invoke_AssetDoesNotExist_ShouldResultInSuccessWithEmptyList() {
    // Arrange
    JsonObject argument = Json.createObjectBuilder().add("asset_id", WRONG_ASSET_ID).build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(0);
  }

  @Test
  public void run_AssetIdNotGiven_ShouldFailProperly() {
    // Arrange
    JsonObject argument = Json.createObjectBuilder().build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.getString("result")).isEqualTo("failure");
    assertThat(result.getString("message")).isEqualTo("'asset_id' attribute is missing");
  }

  @Test
  public void invoke_GivenInclusiveStartVersion_ShouldReturnCorrectData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("start", "[1").build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
    assertThat(data.getJsonObject(0).getInt("age")).isEqualTo(1);
  }

  @Test
  public void invoke_GivenExclusiveStartVersion_ShouldReturnCorrectData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("start", "]0").build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
    assertThat(data.getJsonObject(0).getInt("age")).isEqualTo(1);
  }

  @Test
  public void invoke_GivenStartVersionTooLarge_ShouldReturnEmptyData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("start", "[5").build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(0);
  }

  @Test
  public void invoke_GivenMalformedStartVersion_ShouldResultInFailure() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("start", "&&").build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.getString("result")).isEqualTo("failure");
    assertThat(result.getString("message")).isEqualTo("Error parsing start option");
  }

  @Test
  public void invoke_GivenInclusiveEndVersion_ShouldReturnCorrectData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("end", "0]").build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
    assertThat(data.getJsonObject(0).getInt("age")).isEqualTo(0);
  }

  @Test
  public void invoke_GivenExclusiveEndVersion_ShouldReturnCorrectData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("end", "1[").build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
    assertThat(data.getJsonObject(0).getInt("age")).isEqualTo(0);
  }

  @Test
  public void invoke_GivenMalformedEndVersion_ShouldResultInFailure() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("end", "&&").build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.getString("result")).isEqualTo("failure");
    assertThat(result.getString("message")).isEqualTo("Error parsing end option");
  }

  @Test
  public void invoke_GivenProperLimit_ShouldReturnCorrectData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("limit", 1).build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
    assertThat(data.getJsonObject(0).getInt("age")).isEqualTo(1);
  }

  @Test
  public void invoke_GivenNegativeLimit_ShouldResultInFailure() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("limit", -5).build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.getString("result")).isEqualTo("failure");
    assertThat(result.getString("message")).isEqualTo("Error parsing limit option");
  }

  @Test
  public void invoke_GivenAscOrdering_ShouldResultCorrectData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder().add("asset_id", ASSET_ID).add("asc_order", true).build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(2);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
    assertThat(data.getJsonObject(0).getInt("age")).isEqualTo(0);
    assertThat(data.getJsonObject(1).getInt("age")).isEqualTo(1);
  }

  @Test
  public void invoke_GivenAscOrderingWithLimit_ShouldResultCorrectData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder()
            .add("asset_id", ASSET_ID)
            .add("limit", 1)
            .add("asc_order", true)
            .build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
    assertThat(data.getJsonObject(0).getInt("age")).isEqualTo(0);
  }

  @Test
  public void invoke_GivenStartInclusiveAscOrderingWithLimit_ShouldResultCorrectData() {
    // Arrange
    JsonObject argument =
        Json.createObjectBuilder()
            .add("asset_id", ASSET_ID)
            .add("start", "[1")
            .add("limit", 1)
            .add("asc_order", true)
            .build();

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.getJsonArray("data");

    // Assert
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.getJsonObject(0).getString("asset_id")).isEqualTo(ASSET_ID);
    assertThat(data.getJsonObject(0).getInt("age")).isEqualTo(1);
  }
}
