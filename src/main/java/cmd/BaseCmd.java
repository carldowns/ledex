package cmd;

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
public abstract class BaseCmd {

    /**
     * Governs the type of class that is expected to handle this command
     */
    @JsonProperty
    private final String type;

    /**
     * Action to be taken.  Contract with the handler
     */
    @JsonProperty
    private final String action;

    /**
     * state of the command.  Not overly structured yet.
     */
    @JsonProperty
    private String state;

    /**
     * log for the command.  User classes can add information in here
     * to record what happened, adding things like "did this, did that, caught this exception..."
     * Steps are keyed by timestamp.
     */
    @JsonProperty
    private LinkedHashMap<String,String> steps = new LinkedHashMap<>();


    /////////////////////////////////
    // Constructors
    /////////////////////////////////

    public BaseCmd(String type, String action, String initialState) {
        this.type = type;
        this.action = action;
        this.state = initialState;
        log ("created with state " + state);
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

    /**
     * what is the action we are trying to accomplish?
     * @return
     */
    public String getAction() {
        return action;
    }

    /////////////////////////////////
    // Methods
    /////////////////////////////////

    /**
     * what is the state of this command?
     * @return
     */
    public String getState() {
        return state;
    }

    /**
     * record that the state of the command has changed
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * log a step taken in the execution of this command.
     * @param step
     */
    public void log (String step) {
        DateTime dt = new DateTime();
        steps.put(dt.toString(), step);
    }
}
