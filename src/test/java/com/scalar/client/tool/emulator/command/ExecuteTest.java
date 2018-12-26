package com.scalar.client.tool.emulator.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.EmulatorModule;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.exception.LedgerException;
import com.scalar.ledger.ledger.Ledger;
import java.io.IOException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import picocli.CommandLine;

@Ignore
public class ExecuteTest {
  private static final String CONTRACT_ID = "test_contract";
  @Mock Contract contract;
  Ledger ledger;
  private JsonObject argument;
  Injector injector;
  Execute execute;
  ContractManagerWrapper contractManager;

  @Before
  public void setUp() throws LedgerException, IOException {
    MockitoAnnotations.initMocks(this);
    injector = Guice.createInjector(new EmulatorModule());
    execute = injector.getInstance(Execute.class);
    ledger = injector.getInstance(Ledger.class);
    contractManager = injector.getInstance(ContractManagerWrapper.class);
    // contractManager.register(CONTRACT_ID, contract);

    argument = new JsonObject();
    argument.addProperty("x", "y");
    when(contract.invoke(ledger, argument, Optional.empty())).thenReturn(new JsonObject());
  }

  @Test
  public void run_JsonArgumentAsText_nominal() throws LedgerException {
    // Act
    CommandLine.run(execute, CONTRACT_ID, argument.toString());

    // Assert
    verify(contract).invoke(ledger, argument, Optional.empty());
  }
}
