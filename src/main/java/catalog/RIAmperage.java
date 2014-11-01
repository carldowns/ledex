package catalog;

import mgr.AssemblyMgr;

import java.util.Map;


public class RIAmperage implements IRuleInterpreter {
    
    /**
     * verifies that if there are multiple power sources, each will provide the same AMP output.
     * verifies that all parts consuming Amps will together fall below the threshold of AMP output provided by the power source
     */
    public AssemblyMgr.CandidateProblem evaluate (Assembly assembly, AssemblyMgr.CandidateProduct candidate) {
        return null;
    }

}
