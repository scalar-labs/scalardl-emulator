package com.scalar.client.tool.emulator.contract;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.JsonObject;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.exception.ContractExecutionException;
import com.scalar.ledger.ledger.AssetLedger;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import org.junit.Test;

public class PutContractTest {
  private static final String ASSET_ID = "X";
  private PutContract contract = new PutContract();
  private TransactionalAssetbase assetbase = new AssetbaseEmulator();
  private Ledger ledger = new AssetLedger(assetbase);

  @Test
  public void invoke_AssetIdAndDataGiven_ShouldResultInSuccess() throws ContractExecutionException {
    // Arrange
    JsonObject argument = new JsonObject();
    argument.addProperty("asset_id", ASSET_ID);
    argument.add("data", new JsonObject());

    // Act
    JsonObject result = contract.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("success");
  }

  @Test
  public void invoke_AssetIdNotGiven_ShouldResultInFailure() throws ContractExecutionException {
    // Arrange
    JsonObject argument = new JsonObject();

    // Act
    JsonObject result = contract.invoke(ledger, argument, Optional.empty());

    // Assert
    assertThat(result.get("result").getAsString()).isEqualTo("failure");
  }
}
