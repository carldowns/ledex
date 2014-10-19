package cmd;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.LinkedHashMap;

/**
 * This is a baseline command that is used by the system to track and manage requests
 * throughout the system.
 *
 * These are stateful commands used to track and log what a thread is doing in relation to
 * the system.  They can potentially be used later in a queue system to affect large scale
 * background or foreground activities.  Mainly however their purpose at least initially is
 * to track carefully what has taken place in the system for debugging purposes.
 *
 * Each command is build to encapsulate an action, the state of the action as it progresses
 * and any data members needed to carry out the action.  Data members are added in the
 * derivatives with the proviso that the resulting concrete commands must be Json-serializable.
 * That way, Commands can be persisted in a command log for later evaluation.
 *
 * Created by carl_downs on 10/12/14.
 */

@JsonInclude(value=JsonInclude.Include.NON_EMPTY)
public abstract class BaseCmd {

    /**
     * type of command
     */
    @JsonProperty
    private final String type;

    /**
     * state of the command.  Not overly structured yet.
     */
    @JsonProperty
    private CmdState state;

    /**
     * log for the command.  User classes can add information in here
     * to record what happened, adding things like "did this, did that, caught this exception..."
     * Steps are keyed by timestamp.
     */
    @JsonProperty
    private LinkedHashMap<String,String> log = new LinkedHashMap<>();


    /////////////////////////////////
    // Constructors
    /////////////////////////////////

    public BaseCmd(CmdState initialState) {
        this.type = this.getClass().getSimpleName();
        this.state = initialState;
    }

    public BaseCmd() {
        this.type = this.getClass().getSimpleName();
        this.state = CmdState.started;
    }

    /////////////////////////////////
    // Immutable
    /////////////////////////////////

    /**
     * what type of command is this?  Typically this determines the application
     * command handler that will process it.
     * @return
     */
    public String getType () {
        return type;
    }


    /////////////////////////////////
    // Methods
    /////////////////////////////////

    /**
     * what is the state of this command?
     * @return
     */
    public CmdState getState() {
        return state;
    }

    /**
     * record that the state of the command has changed
     * @param state
     */
    public void setState(CmdState state) {
        this.state = state;
    }

    public void showCompleted() {
        state = CmdState.completed;
    }

    public void showFailed() {
        state = CmdState.failed;
    }

    /////////////////////////////////
    // Logging
    /////////////////////////////////

    /**
     * log a step taken in the execution of this command.
     * @param step
     */
    public void log (String step) {
        log.put(DateTime.now().toString(), step);
    }

    /////////////////////////////////
    // state types
    /////////////////////////////////

    public static enum CmdState {
        started,
        waiting,
        completed,
        failed
    }
}
