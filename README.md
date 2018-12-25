# ScalarDL emulator

This tools offers an interactive command line interface to run Scalar DL Contract implementation on an in-memory ledger.

## Generate the executables

```
./gradlew build install
```
The executable is found in `build/install/emulator/bin`.

## Run

To run the emulator
```
./build/install/emulator/bin/emulator
```

or pass a file containing a list of commands the tool will execute. For example,
this will run commands contained in [cmds.txt](cmds.txt)
```
./build/install/emulator/bin/emulator -f cmds.txt
```

### Exit/Suspend the terminal

The tool can be exited with the standard shortcut to kill a job or by executing the `exit` command.

## Help

Type `help` to display the list of available commands inside the interactive terminal.

```
scalar> help
Available commands:
 - execute
 - get
 - get -j
 - list-contracts
 - put
 - put -j
 - register
 - scan
 - scan -j
 - help
 - exit
Type '<command> -h' to display the command help.
```

Every command has a detailed help that can be displayed with -h. For example:

```
scalar> execute -h

Usage:
execute [-h] id argument...

Description:
Execute a contract that has been with the 'register' command.

Parameters:
      id            contract id. Use 'list-contract' to list registered contract id.
      argument...   the JSON contract argument. A plain text JSON object or the path to a file containing a JSON object

Options:
  -h, --help        print the help and exit

For example: 'execute get {"asset_id":"foo"}'
```

## Registering a contract

Write a contract and save it in the `contract` directory. You then have two ways to `register` the contract.
You can register your contract directly in `ContractRegistry.java` by adding a line to the `preregisterContracts()` method, or use the `register` command as in

```
register state-updater com.scalar.client.tool.emulator.contract.StateUpdater ./build/classes/java/main/com/scalar/client/tool/emulator/contract/StateUpdater.class
```
