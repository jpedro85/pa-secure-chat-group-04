package Utils.UserInputs;

import java.util.Objects;

/**
 * Represents a command that can be executed.
 */
public class Command
{
    // The name of the command.
    private final String NAME;

    // The description of the command.
    private final String DESCRIPTION;

    // The function to be executed when the command is invoked.
    private final CommandFunction FUNCTION;

    /**
     * Constructs a command with the given name and function.
     *
     * @param name     The name of the command.
     * @param function The function to be executed when the command is invoked.
     */
    public Command( String name , CommandFunction function )
    {
       this( name ,function, "No description." );
    }

    /**
     * Constructs a command with the given name, function, and description.
     *
     * @param name        The name of the command.
     * @param function    The function to be executed when the command is invoked.
     * @param description The description of the command.
     */
    public Command( String name, CommandFunction function, String description)
    {
        NAME = name;
        FUNCTION = function;
        DESCRIPTION = description;
    }

    /**
     * Retrieves the description of the command.
     *
     * @return The description of the command.
     */
    public String getDescription()
    {
        return DESCRIPTION;
    }

    /**
     * Retrieves the name of the command.
     *
     * @return The name of the command.
     */
    public String getName() {
        return NAME;
    }

    /**
     * Executes the command with the provided arguments.
     *
     * @param args The arguments for the command execution.
     */
    public void execute( String args )
    {
        FUNCTION.execute( args );
    }

    @Override
    public String toString() {
        return NAME + ": " + DESCRIPTION;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(NAME, command.NAME);
    }

}
