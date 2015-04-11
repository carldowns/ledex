package cmd;

/**
 * the pattern is that if during Cmd processing we detect a problem,
 * log the error message to the cmd log, set the cmd state to failed
 * and throw one of these.
 *
 * Do NOT log this again or add it to the command.
 */
public class CmdRuntimeException2 extends RuntimeException {
    public CmdRuntimeException2(String msg) {
        super (msg);
    }
}
