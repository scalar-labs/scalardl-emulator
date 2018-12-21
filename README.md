# ScalarDL emulator

This tools offers an interactive command line interface to run Scalar DL Contract implementation on an in memory ledger.

### Generate the executables
```
./gradlew build install
```
The executable is found in `build/install/emulator/bin`.

### Run

To run the emulator
```
./build/install/emulator/bin/emulator
```

or pass a file containing a list of commands the tool will execute.
This will run commands contained into the [end 2 end test scenario](./src/test/resources/end_2_end_test) 
```
./build/install/emulator/bin/emulator -f src/test/resources/end_2_end_test
```
#### Exit/Suspend the terminal
The tool can be exited with the standard shortcut to kill a job or by executing the `exit` command.

The terminal can also be paused with Ctrl+Z

#### Show the help
To show the tool help
```
./build/install/emulator/bin/emulator -h
```

Every command has a detailed help that can be displayed with -h. For example:
```
dlt-e# execute -h

Usage:
execute [-h] id argument...

Description:
Execute a contract that has been with the `register` command.

Parameters:
      id            contract id. Use `list-contract` to list registered contract id.
      argument...   the JSON contract argument. A plain text JSON object or the path to a file containing a JSON object

Options:
  -h, --help        print the help and exit

For example : `execute get {"asset_id": "X"}`
```

### Available commands in the interactive terminal

To display the list of available commands inside the interactive terminal. Type `usage`
```
dlt-e# usage
Available commands :
	- execute
	- get
	- get -j
	- list-contract
	- put
	- put -j
	- register
	- scan
	- scan -j
Type "<command> -h" to display the command usage.
```
