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
            System.out.println( "("+i+") -> " + COMMANDS.get(i) );
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

    public void executeFromInput( String input )
    {
        int space = input.indexOf(" ");
        String commandName = input.substring(0, input.indexOf(" ") );
        String args = input.substring( space , input.length()-1 );

        Command command;
        do
        {
            command = selectCommand( askInput("Command") );
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
