package com.scalar.client.tool.emulator.contract;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.exception.AssetCommitException;
import com.scalar.ledger.exception.ContractExecutionException;
import com.scalar.ledger.exception.UnknownAssetStatusException;
import com.scalar.ledger.ledger.AssetLedger;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class ScanContractTest {
  private static final String ASSET_ID = "X";
  private static final String WRONG_ASSET_ID = "Y";
  private PutContract put = new PutContract();
  private ScanContract scan = new ScanContract();
  JsonObject argument;
  private TransactionalAssetbase assetbase = new AssetbaseEmulator();
  private Ledger ledger = new AssetLedger(assetbase);

  @Before
  public void setUp() throws ContractExecutionException {
    putAnAgeTwoAssetInTheLedger();
    argument = new JsonObject();
    argument.addProperty("asset_id", ASSET_ID);
  }

  private void putAnAgeTwoAssetInTheLedger() throws ContractExecutionException {
    try {
      JsonObject argument = new JsonObject();
      argument.addProperty("asset_id", ASSET_ID);
      argument.add("data", new JsonObject());
      put.invoke(ledger, argument, Optional.empty());
      assetbase.commit();
      put.invoke(ledger, argument, Optional.empty());
      assetbase.commit();
    } catch (AssetCommitException | UnknownAssetStatusException e) {
      throw new ContractExecutionException(e.getMessage());
    }
  }

  @Test
  public void invoke_AssetExistsAndIdGiven_ShouldResultInSuccessWithAsset()
      throws ContractExecutionException {
    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(result.get("data").getAsJsonArray().size()).isEqualTo(2);
  }

  @Test
  public void invoke_AssetDoesNotExist_ShouldResultInSuccessWithEmptyList()
      throws ContractExecutionException {
    // Arrange
    JsonObject argument = new JsonObject();
    argument.addProperty("asset_id", WRONG_ASSET_ID);

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(result.get("data").getAsJsonArray().size()).isEqualTo(0);
  }

  @Test
  public void run_AssetIdNotGiven_ShouldFailProperly() throws Exception {
    // Arrange
    JsonObject argument = new JsonObject();

    // Act
    JsonObject response = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(response.get("result").getAsString()).isEqualTo("failure");
    assertThat(response.get("message").getAsString()).isEqualTo("'asset_id' attribute is missing");
  }

  @Test
  public void invoke_GivenInclusiveStartVersion_ShouldReturnCorrectData()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("start", "[1");

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.get("data").getAsJsonArray();

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.get(0).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(1);
  }

  @Test
  public void invoke_GivenExclusiveStartVersion_ShouldReturnCorrectData()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("start", "]0");

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.get("data").getAsJsonArray();

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.get(0).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(1);
  }

  @Test
  public void invoke_GivenStartVersionTooLarge_ShouldReturnEmptyData()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("start", "[5");

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(result.get("data").getAsJsonArray().size()).isEqualTo(0);
  }

  @Test
  public void invoke_GivenMalformedStartVersion_ShouldResultInFailure()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("start", "&&");

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("failure");
    assertThat(result.get("message").getAsString()).isEqualTo("Error parsing start option");
  }

  @Test
  public void invoke_GivenInclusiveEndVersion_ShouldReturnCorrectData()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("end", "0]");

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.get("data").getAsJsonArray();

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.get(0).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(0);
  }

  @Test
  public void invoke_GivenExclusiveEndVersion_ShouldReturnCorrectData()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("end", "1[");

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.get("data").getAsJsonArray();

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.get(0).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(0);
  }

  @Test
  public void invoke_GivenMalformedEndVersion_ShouldResultInFailure()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("end", "&&");

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("failure");
    assertThat(result.get("message").getAsString()).isEqualTo("Error parsing end option");
  }

  @Test
  public void invoke_GivenProperLimit_ShouldReturnCorrectData() throws ContractExecutionException {
    // Arrange
    argument.addProperty("limit", 1);

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.get("data").getAsJsonArray();

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.get(0).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(1);
  }

  @Test
  public void invoke_GivenNegativeLimit_ShouldResultInFailure() throws ContractExecutionException {
    // Arrange
    argument.addProperty("limit", -5);

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("failure");
    assertThat(result.get("message").getAsString()).isEqualTo("Error parsing limit option");
  }

  @Test
  public void invoke_GivenAscOrdering_ShouldResultCorrectData() throws ContractExecutionException {
    // Arrange
    argument.addProperty("asc_order", true);

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.get("data").getAsJsonArray();

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(data.size()).isEqualTo(2);
    assertThat(data.get(0).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(0);
    assertThat(data.get(1).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(1);
  }

  @Test
  public void invoke_GivenAscOrderingWithLimit_ShouldResultCorrectData()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("asc_order", true);
    argument.addProperty("limit", 1);

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.get("data").getAsJsonArray();

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.get(0).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(0);
  }

  @Test
  public void invoke_GivenStartInclusiveAscOrderingWithLimit_ShouldResultCorrectData()
      throws ContractExecutionException {
    // Arrange
    argument.addProperty("start", "[1");
    argument.addProperty("limit", 1);
    argument.addProperty("asc_order", true);

    // Act
    JsonObject result = scan.invoke(ledger, argument, Optional.empty());
    JsonArray data = result.get("data").getAsJsonArray();

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
    assertThat(data.size()).isEqualTo(1);
    assertThat(data.get(0).getAsJsonObject().get("age").getAsBigInteger()).isEqualTo(1);
  }
}
