package com.scalar.client.tool.emulator;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.scalar.client.tool.emulator.command.CommandExceptionHandler;
import com.scalar.client.tool.emulator.command.Execute;
import com.scalar.client.tool.emulator.command.Get;
import com.scalar.client.tool.emulator.command.GetWithSingleParameter;
import com.scalar.client.tool.emulator.command.ListContract;
import com.scalar.client.tool.emulator.command.Put;
import com.scalar.client.tool.emulator.command.PutWithSingleParameter;
import com.scalar.client.tool.emulator.command.Register;
import com.scalar.client.tool.emulator.command.Scan;
import com.scalar.client.tool.emulator.command.ScanWithSingleParameter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.UserInterruptException;
import picocli.CommandLine;

@CommandLine.Command(
    name = "emulator",
    sortOptions = false,
    usageHelpWidth = TerminalWrapper.USAGE_HELP_WIDTH,
    headerHeading = "%n@|bold,underline Usage|@:%n",
    descriptionHeading = "%n@|bold,underline Description|@:%n",
    description = "Interactive emulator for Scalar DL",
    parameterListHeading = "%n@|bold,underline Parameters|@:%n",
    optionListHeading = "%n@|bold,underline Options|@:%n",
    footerHeading = "%n",
    footer = "%n")
public class EmulatorTerminal implements Runnable {
  private static List<CommandLine> commands;
  private TerminalWrapper terminal;
  private ContractRegistry contractRegistry;
  private boolean shouldExit;

  @CommandLine.Option(
      names = {"-f", "--file"},
      paramLabel = "FILE",
      description =
          "a file containing a list of commands that will be executed. The file should contain one command at most per line")
  private File commandsFile;

  @Inject
  public EmulatorTerminal(TerminalWrapper terminal, ContractRegistry contractRegistry) {
    this.terminal = terminal;
    this.contractRegistry = contractRegistry;
  }

  public static void main(String[] args) throws IOException {
    Injector injector = Guice.createInjector(new EmulatorModule());

    commands =
        Arrays.asList(
            new CommandLine(injector.getInstance(Execute.class)),
            new CommandLine(injector.getInstance(Get.class)),
            new CommandLine(injector.getInstance(GetWithSingleParameter.class)),
            new CommandLine(injector.getInstance(ListContract.class)),
            new CommandLine(injector.getInstance(Put.class)),
            new CommandLine(injector.getInstance(PutWithSingleParameter.class)),
            new CommandLine(injector.getInstance(Register.class)),
            new CommandLine(injector.getInstance(Scan.class)),
            new CommandLine(injector.getInstance(ScanWithSingleParameter.class)));

    CommandLine.run(injector.getInstance(EmulatorTerminal.class));
  }

  @Override
  public void run() {
    LineReader inputReader = terminal.setUpAutoCompletionAndTerminalHistory(commands);
    terminal.printWelcomeMessage();

    if (commandsFile != null) {
      executeCommandsFile();
    }

    terminal.resume();

    while (!shouldExit) {
      try {
        String line = inputReader.readLine(TerminalWrapper.LINE_HEADER);
        if (!processLine(line)) {
          terminal.println("Unknown command: " + line);
        }
      } catch (UserInterruptException e) {
        break;
      } catch (EndOfFileException e) {
        terminal.println(e.getMessage());
        break;
      }
    }
    terminal.println("bye for now!");
  }

  private void executeCommandsFile() {
    try (Stream<String> stream = Files.lines(commandsFile.toPath())) {
      stream.forEach(
          line -> {
            terminal.println(line, true);
            processLine(line);
          });
    } catch (IOException e) {
      terminal.println(e.getMessage());
    }
  }

  private boolean processLine(String line) {
    line = line.trim();
    if (line.equals("usage")) {
      printUsage();
      return true;
    } else if (line.equals("exit")) {
      shouldExit = true;
      return true;
    } else {
      return parseAndRunCommand(line);
    }
  }

  private void printUsage() {
    terminal.println("Available commands :");
    commands
        .stream()
        .map(CommandLine::getCommandName)
        .sorted(String::compareToIgnoreCase)
        .forEach(commandName -> terminal.println("\t- " + commandName));
    terminal.println("Type '<command> -h' to display the command usage.");
  }

  private boolean parseAndRunCommand(String line) {
    for (CommandLine command : commands) {
      if (line.startsWith(command.getCommandName())) {
        String params = line.replaceFirst(command.getCommandName(), "").trim();
        String[] paramsArray = params.isEmpty() ? new String[] {} : params.split(" ");
        command.parseWithHandlers(
            new CommandLine.RunFirst(),
            new CommandExceptionHandler(terminal.getTerminal().writer()),
            paramsArray);
        return true;
      }
    }
    return false;
  }
}
