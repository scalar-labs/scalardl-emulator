package com.scalar.client.tool.emulator.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.ledger.Ledger;
import java.util.Optional;
import javax.json.Json;
import javax.json.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import picocli.CommandLine;

public class ExecuteTest {
  private static final String CONTRACT_ID = "test_contract";
  private Execute execute;
  AssetbaseEmulator assetbase;
  @Mock Contract contract;
  @Mock ContractManagerWrapper contractManager;
  @Mock Ledger ledger;
  @Mock TerminalWrapper terminal;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    assetbase = new AssetbaseEmulator();
    execute = new Execute(terminal, contractManager, assetbase, ledger);
  }

  @Test
  public void run_ExecuteContract_ShouldCallInvokeOnTheContract() {
    // Arrange
    JsonObject argument = Json.createObjectBuilder().add("x", "y").build();
    when(contractManager.getInstance(CONTRACT_ID)).thenReturn(contract);
    when(contract.invoke(ledger, argument, Optional.empty())).thenReturn(null);

    // Act
    CommandLine.run(execute, CONTRACT_ID, argument.toString());

    // Assert
    verify(contract).invoke(ledger, argument, Optional.empty());
  }
}
