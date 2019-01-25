/*
 * This file is part of the Scalar DL Emulator.
 * Copyright (c) 2019 Scalar, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. For more information, please contact Scalar, Inc.
 */
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
