package com.scalar.client.tool.emulator;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.concurrent.Immutable;
import org.jline.builtins.Completers;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.reader.impl.history.DefaultHistory;
import org.jline.terminal.Terminal;
import picocli.CommandLine;

@Immutable
public final class TerminalWrapper {
  public static final int USAGE_HELP_WIDTH = 150;
  public static final String LINE_HEADER = "scalar> ";
  private final Terminal terminal;

  public TerminalWrapper(Terminal terminal) {
    this.terminal = terminal;
  }

  public Terminal getTerminal() {
    return terminal;
  }

  public void println(String text, boolean withHeader) {
    if (withHeader) {
      text = LINE_HEADER + text;
    }
    this.terminal.writer().println(text);
    this.terminal.writer().flush();
  }

  public void println(String text) {
    this.println(text, false);
  }

  public void printWelcomeMessage() {
    println("-------------------------------------------------------", true);
    println("Welcome to the Scalar DLT emulator interactive ", true);
    println("-------------------------------------------------------", true);
    println("Type 'usage' for more information", true);
    println("");
  }

  public void resume() {
    terminal.resume();
  }

  public LineReader setUpAutoCompletionAndTerminalHistory(List<CommandLine> commands) {
    List<String> commandsName =
        commands.stream().map(e -> e.getCommandName()).collect(Collectors.toList());
    commandsName.add("usage");
    commandsName.add("exit");

    AggregateCompleter completer =
        new AggregateCompleter(
            new StringsCompleter(commandsName),
            new Completers.FileNameCompleter(),
            new Completers.DirectoriesCompleter(new File(System.getProperty("user.dir"))));

    LineReader inputReader =
        LineReaderBuilder.builder()
            .history(new DefaultHistory())
            .completer(completer)
            .terminal(terminal)
            .variable(
                // Location of the file where the executed commands history is saved
                LineReader.HISTORY_FILE,
                Paths.get(System.getProperty("user.home"), ".scalardlt_emulator_history"))
            .build();

    return inputReader;
  }
}
