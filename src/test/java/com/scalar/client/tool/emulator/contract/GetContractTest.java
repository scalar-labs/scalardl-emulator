package com.scalar.client.tool.emulator.contract;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.JsonObject;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.exception.ContractExecutionException;
import com.scalar.ledger.ledger.AssetLedger;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;

public class GetContractTest {
  private static final String ASSET_ID = "X";
  private static final String WRONG_ASSET_ID = "Y";
  private PutContract put = new PutContract();
  private GetContract get = new GetContract();
  private TransactionalAssetbase assetbase = new AssetbaseEmulator();
  private Ledger ledger = new AssetLedger(assetbase);
  private JsonObject argument;

  @Before
  public void setUp() throws ContractExecutionException {
    addAssetToLedger();
    argument = new JsonObject();
  }

  private void addAssetToLedger() throws ContractExecutionException {
    JsonObject argument = new JsonObject();
    argument.addProperty("asset_id", ASSET_ID);
    argument.add("data", new JsonObject());
    put.invoke(ledger, argument, Optional.empty());
  }

  @Test
  public void invoke_AssetIdGiven_ShouldResultInSuccess() throws ContractExecutionException {
    // Arrange
    argument.addProperty("asset_id", ASSET_ID);

    // Act
    JsonObject result = get.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
  }

  @Test
  public void invoke_AssetNotInLedger_ShouldResultInFailure() throws ContractExecutionException {
    // Arrange
    argument.addProperty("asset_id", WRONG_ASSET_ID);

    // Act
    JsonObject result = get.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("failure");
    assertThat(result.get("message").getAsString()).endsWith("is not in the ledger");
  }

  @Test
  public void invoke_AssetIdNotGiven_ShouldResultInFailure() throws ContractExecutionException {
    // Arrange
    JsonObject argument = new JsonObject();

    // Act
    JsonObject result = get.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("failure");
    assertThat(result.get("message").getAsString()).isEqualTo("'asset_id' attribute is missing");
  }
}
