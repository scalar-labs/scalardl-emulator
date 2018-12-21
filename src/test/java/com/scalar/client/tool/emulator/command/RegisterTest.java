package com.scalar.client.tool.emulator.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scalar.client.tool.emulator.ContractRegistry;
import com.scalar.client.tool.emulator.EmulatorModule;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.ledger.Ledger;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import picocli.CommandLine;

public class RegisterTest {
  private static final String CONTRACT_ID = "test_contract";
  Register register;
  ContractRegistry contractRegistry;

  @Before
  public void setUp() throws IOException {
    Injector injector = Guice.createInjector(new EmulatorModule());
    register = injector.getInstance(Register.class);
    contractRegistry = injector.getInstance(ContractRegistry.class);
  }

  @Test
  public void run_ProperContractGiven_ShouldRegisterSuccessfully() {
    // Act
    CommandLine.run(
        register,
        CONTRACT_ID,
        "com.scalar.client.tool.emulator.command.RegisterTest$TestContract",
        Paths.get(
                "build",
                "classes",
                "java",
                "test",
                "com",
                "scalar",
                "client",
                "tool",
                "emulator",
                "command",
                "RegisterTest$TestContract.class")
            .toString());

    assertThat(contractRegistry.getContract(CONTRACT_ID)).isNotEmpty();
  }

  public static class TestContract extends Contract {
    @Override
    public JsonObject invoke(Ledger ledger, JsonObject parameter, Optional<JsonObject> property) {
      return new JsonObject();
    }
  }
}
