package com.scalar.client.tool.emulator.command;

import static org.mockito.Mockito.verify;

import com.scalar.client.tool.emulator.ContractManagerWrapper;
import com.scalar.client.tool.emulator.TerminalWrapper;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.ledger.Ledger;
import java.io.File;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import picocli.CommandLine;

public class RegisterTest {
  private static final String CONTRACT_ID = "id";
  private static final String CONTRACT_NAME = "name";
  private static final String CONTRACT_FILE = "file";
  private Register register;
  private TransactionalAssetbase assetbase;
  @Mock TerminalWrapper terminal;
  @Mock ContractManagerWrapper contractManager;
  @Mock Ledger ledger;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    assetbase = new AssetbaseEmulator();
    register = new Register(terminal, contractManager, assetbase, ledger);
  }

  @Test
  public void run_ProperContractGiven_ShouldRegisterSuccessfully() {
    // Act
    CommandLine.run(register, CONTRACT_ID, CONTRACT_NAME, CONTRACT_FILE);

    verify(contractManager).register(CONTRACT_ID, CONTRACT_NAME, new File(CONTRACT_FILE), null);
  }
}
