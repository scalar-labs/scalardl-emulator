package com.scalar.client.tool.emulator;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.contract.ContractManager;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.ledger.AssetLedger;
import com.scalar.ledger.ledger.Ledger;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
import org.junit.Before;
import org.junit.Test;

public class NestedInvocationTest {
  private static final String CONTRACT_ID_ATTRIBUTE_NAME = "contract_id";
  private Ledger ledger;
  private ContractManagerWrapper contractManager;

  @Before
  public void setUp() {
    ledger = new AssetLedger(new AssetbaseEmulator());
    contractManager =
        new ContractManagerWrapper(new ContractManager(new ContractRegistryEmulator()));

    registerContract("caller", "Caller");
    registerContract("callee", "Callee");
  }

  private void registerContract(String id, String name) {
    Path parent =
        Paths.get(
            "build", "classes", "java", "test", "com", "scalar", "client", "tool", "emulator");
    contractManager.register(
        id,
        "com.scalar.client.tool.emulator." + name,
        new File(parent.toFile(), name + ".class"),
        null);
  }

  @Test
  public void invoke_NestedInvocation_ShouldExecuteBothContracts() {
    // Arrange
    Contract contract = contractManager.getInstance("caller");
    JsonObject argument =
        Json.createObjectBuilder().add(CONTRACT_ID_ATTRIBUTE_NAME, "callee").build();

    // Act assert
    assertThatCode(() -> contract.invoke(ledger, argument, Optional.empty()))
        .doesNotThrowAnyException();
  }
}
