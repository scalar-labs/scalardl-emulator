package com.scalar.client.tool.emulator;

import com.scalar.ledger.contract.Contract;
import java.io.*;

public class ContractClassLoader extends ClassLoader {
  private File contractClassFile;
  private String contractName;
  private PrintWriter outputWriter;

  public ContractClassLoader(PrintWriter outputWriter, String name) {
    super(Contract.class.getClassLoader());
    this.outputWriter = outputWriter;
    this.contractName = name;
  }

  public Class<?> loadClass(String name) throws ClassNotFoundException {
    if (!this.contractName.equals(name)) {
      return super.loadClass(name);
    }

    try (InputStream input =
        this.contractClassFile.toURI().toURL().openConnection().getInputStream()) {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int data = input.read();
      while (data != -1) {
        buffer.write(data);
        data = input.read();
      }
      byte[] bytes = buffer.toByteArray();

      return defineClass(name, buffer.toByteArray(), 0, bytes.length);
    } catch (IOException e) {
      e.printStackTrace(outputWriter);
    }

    return null;
  }

  public Class<?> load(File file, String name) throws ClassNotFoundException {
    this.contractClassFile = file;
    return loadClass(name);
  }
}
