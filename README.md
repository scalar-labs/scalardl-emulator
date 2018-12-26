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
this will run commands contained in [cmds.txt](https://github.com/scalar-labs/scalardl-emulator/blob/master/cmds.txt)
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
Type '<command> -h' to display help for the command.
```

Every command has a detailed help that can be displayed with -h. For example:

```
scalar> execute -h

Usage:
execute [-h] id argument...

Description:
Execute a registered contract.

Parameters:
      id            contract id. Use 'list-contracts' to list all the registered contracts and their ids.
      argument...   the JSON contract argument. A plain text JSON object or the path to a file containing a JSON object

Options:
  -h, --help        print the help and exit

For example: 'execute get {"asset_id":"foo"}'
```

## Registering a contract

Write a contract and save it in the `contract` directory. You can then register the contract using the `register` command as in

```
register state-updater com.scalar.client.tool.emulator.contract.StateUpdater ./build/classes/java/main/com/scalar/client/tool/emulator/contract/StateUpdater.class
```
