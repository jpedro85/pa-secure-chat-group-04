package Utils.UserInputs;

/**
 * Represents a functions de executed.
 */
public interface CommandFunction
{
    /**
     * The function to be executed.
     * @param args the arguments of the command.
     */
    void execute( String args );
}
