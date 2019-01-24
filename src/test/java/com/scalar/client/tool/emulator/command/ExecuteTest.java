package com.scalar.client.tool.emulator.command;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.contract.Contract;
import com.scalar.ledger.contract.ContractEntry;
import com.scalar.ledger.crypto.CertificateEntry;
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
  private static final String CONTRACT_ID = "contract";
  private Execute execute;
  private AssetbaseEmulator assetbase;
  @Mock private Contract contract;
  @Mock private ContractManagerWrapper contractManager;
  @Mock private Ledger ledger;
  @Mock private TerminalWrapper terminal;
  private ContractEntry entry;

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
    ContractEntry.Key key =
        new ContractEntry.Key(CONTRACT_ID, new CertificateEntry.Key("emulator_user", 0));
    entry =
        new ContractEntry(
            "id",
            "binaryName",
            "cert_holder_id",
            1,
            "contract".getBytes(),
            null,
            1,
            "signature".getBytes());
    when(contractManager.get(key)).thenReturn(entry);
    when(contractManager.getInstance(key)).thenReturn(contract);
    when(contract.invoke(ledger, argument, Optional.empty())).thenReturn(null);

    // Act
    CommandLine.run(execute, CONTRACT_ID, argument.toString());

    // Assert
    verify(contract).invoke(ledger, argument, Optional.empty());
  }
}
