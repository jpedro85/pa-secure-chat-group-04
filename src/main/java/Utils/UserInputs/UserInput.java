package Utils.UserInputs;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserInput
{
    private final List<Command> COMMANDS;
    private final Scanner SCANNER;

    public UserInput()
    {
        COMMANDS = new ArrayList<>();
        SCANNER = new Scanner(System.in);
    }

    public void addCommand( Command command )
    {
        if( ! COMMANDS.contains( command ) )
            COMMANDS.add( command );
    }

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
                args = input.substring( space );
            }

            command = selectCommand( commandName );
        }
        while( command == null);

        command.execute( args );
    }

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
