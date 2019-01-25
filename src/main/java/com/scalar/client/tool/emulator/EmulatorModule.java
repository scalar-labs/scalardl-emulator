package com.scalar.client.tool.emulator;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.scalar.ledger.contract.ContractManager;
import com.scalar.ledger.database.ContractRegistry;
import com.scalar.ledger.database.TransactionalAssetbase;
import com.scalar.ledger.emulator.AssetbaseEmulator;
import com.scalar.ledger.ledger.AssetLedger;
import com.scalar.ledger.ledger.Ledger;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.jline.terminal.TerminalBuilder;

public class EmulatorModule extends AbstractModule {
  private final AssetbaseEmulator assetbase;
  private final ContractRegistry registry;
  private final ContractManager manager;

  public EmulatorModule() {
    assetbase = new AssetbaseEmulator();
    registry = new ContractRegistryEmulator();
    manager = new ContractManager(registry);
  }

  @Provides
  @Singleton
  TerminalWrapper provideTerminalWrapper() throws IOException {
    return new TerminalWrapper(
        TerminalBuilder.builder().paused(true).encoding(StandardCharsets.UTF_8).build());
  }

  @Provides
  @Singleton
  TransactionalAssetbase provideAssetbase() {
    return assetbase;
  }

  @Provides
  @Singleton
  ContractRegistry provideContractRegistry() {
    return registry;
  }

  @Provides
  @Singleton
  ContractManager provideContractManager() {
    return manager;
  }

  @Provides
  @Singleton
  ContractManagerWrapper provideContractManagerWrapper() {
    return new ContractManagerWrapper(manager);
  }

  @Provides
  @Singleton
  Ledger provideLedger() {
    return new AssetLedger(assetbase);
  }
}
