package com.scalar.client.tool.emulator.command;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.EmulatorModule;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.exception.ContractValidationException;
import com.scalar.ledger.exception.MissingCertificateException;
import com.scalar.ledger.exception.MissingContractException;
import com.scalar.ledger.exception.RegistryIOException;
import com.scalar.ledger.exception.SignatureException;
import com.scalar.ledger.exception.UnloadableKeyException;
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
  ContractManagerWrapper contractManager;

  @Before
  public void setUp() throws IOException {
    Injector injector = Guice.createInjector(new EmulatorModule());
    register = injector.getInstance(Register.class);
    contractManager = injector.getInstance(ContractManagerWrapper.class);
  }

  @Test
  public void run_ProperContractGiven_ShouldRegisterSuccessfully()
      throws MissingContractException, UnloadableKeyException, MissingCertificateException,
          SignatureException, ContractValidationException, RegistryIOException {
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

    assertThat(contractManager.get(CONTRACT_ID).getId()).isEqualTo(CONTRACT_ID);
  }

  public static class TestContract extends Contract {
    @Override
    public JsonObject invoke(Ledger ledger, JsonObject parameter, Optional<JsonObject> property) {
      return new JsonObject();
    }
  }
}
