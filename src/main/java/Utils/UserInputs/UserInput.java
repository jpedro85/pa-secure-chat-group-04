package Utils.UserInputs;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The UserInput class provides functionality to interactively receive user input
 * and execute corresponding commands.
 */
public class UserInput
{
    /** List of available commands */
    private final List<Command> COMMANDS;

    /** Scanner object to read user input */
    private final Scanner SCANNER;

    /**
     * Constructs a new UserInput object.
     */
    public UserInput()
    {
        COMMANDS = new ArrayList<>();
        SCANNER = new Scanner(System.in);
    }

    /**
     * Adds a new command to the list of available commands.
     *
     * @param command The command to add.
     */
    public void addCommand( Command command )
    {
        if( ! COMMANDS.contains( command ) )
            COMMANDS.add( command );
    }

    /**
     * Displays the list of available commands.
     */
    public void removeCommand( Command command )
    {
        COMMANDS.remove( command );
    }

    public void showOptions()
    {
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Command options:");
        for (int i = 0; i < COMMANDS.size(); i++)
        {
            System.out.println( " -> " + COMMANDS.get(i) );
        }

    }

    /**
     * Prompts the user for input with the specified text.
     *
     * @param text The prompt text.
     * @return The user input always a non-empty string.
     */
    public String askInput( String text )
    {
        String input;
        do
        {
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println( text + ":");
            input = SCANNER.nextLine();

            if(input.isBlank())
                System.out.println("The input can not be empty.");
            else
                return input;

        }while( true);

    }

    /**
     * Executes a command based on user input.
     */
    public void executeFromInput()
    {
        Command command;
        int space;
        String args,commandName,input;
        do
        {
            showOptions();
            input = askInput("Command" );
            space = input.indexOf(' ');
            if( space == -1 )
            {
                commandName = input.trim();
                args = "";
            }
            else
            {
                commandName = input.substring(0, space );
                args = input.substring( space + 1);
            }

            command = selectCommand( commandName );
        }
        while( command == null);

        command.execute( args );
    }


    /**
     * Selects a command based on the provided name.
     *
     * @param name The name of the command.
     * @return The selected command, or null if no command matches or multiple commands match.
     */
    private Command selectCommand( String name )
    {
        ArrayList<Command> matchingCommands = new ArrayList<>();

        for( Command command : COMMANDS )
        {
            if ( command.getName().startsWith( name ))
                matchingCommands.add( command );
        }

        if ( matchingCommands.size() == 1)
            return matchingCommands.get(0);
        else
        {
            if( matchingCommands.isEmpty() )
                System.out.println("No commands matches the provided command.");
            else
                System.out.println("More than one command matches the provided command.");

            return null;
        }

    }

}
