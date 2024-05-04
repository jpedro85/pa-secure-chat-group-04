package Utils.UserInputs;

import java.util.Objects;

public class Command
{
    private final String NAME;
    private final String DESCRIPTION;
    private final CommandFunction FUNCTION;

    public Command( String name , CommandFunction function )
    {
       this( name ,function, "No description." );
    }
    public Command( String name, CommandFunction function, String description)
    {
        NAME = name;
        FUNCTION = function;
        DESCRIPTION = description;
    }

    public String getDescription()
    {
        return DESCRIPTION;
    }

    public String getName() {
        return NAME;
    }

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
