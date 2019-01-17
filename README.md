# ScalarDL emulator

This tools offers an interactive command line interface to run Scalar DL on an in-memory ledger.

## Run the tests and generate an executable

```
./gradlew build install
```

The executable is found in `build/install/emulator/bin`.

## Run

To run the emulator

```
./build/install/emulator/bin/emulator
```

or pass a file with `-f` containing a list of commands the emulator will execute. For example,
this will run the commands contained in [cmds.txt](https://github.com/scalar-labs/scalardl-emulator/blob/master/cmds.txt)

```
./build/install/emulator/bin/emulator -f cmds.txt
```

## Exit the emulator

Exit the emulator `exit` or with the standard shortcut to kill a process.

## Registering a contract

Write a contract and save it in the `contract` directory. Run `./gradlew build` to compile it. Then you can start the emulator and register the contract using the `register`. For example, to register the contract `StateUpdater.java` in the contract directory you would

```
scalar> register state-updater com.scalar.client.tool.emulator.contract.StateUpdater ./build/classes/java/main/com/scalar/client/tool/emulator/contract/StateUpdater.class
```

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

## Command history

A history of executed commands is saved to `.scalardl_emulator_history` in your home directory.
