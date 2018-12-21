package com.scalar.client.tool.emulator.command;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.jline.utils.WriterOutputStream;
import picocli.CommandLine;

public class CommandExceptionHandler extends CommandLine.DefaultExceptionHandler<List<Object>> {
  public CommandExceptionHandler(PrintWriter output) {
    OutputStream os = new WriterOutputStream(output, StandardCharsets.UTF_8);
    PrintStream ps = new PrintStream(os);
    useErr(ps);
  }

  @Override
  public List<Object> handleExecutionException(
      CommandLine.ExecutionException ex, CommandLine.ParseResult parseResult) {
    ex.getCause().printStackTrace(err());
    return null;
  }
}
