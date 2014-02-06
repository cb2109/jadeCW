package jadeCW.hospitalagent;


import jade.core.Agent;

/**
 * User: cb2109
 * Date: 06/02/14
 * Time: 15:45
 */
public class HospitalAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();

        Object[] input = getArguments();

        if(input.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("The Input array did not contain any elements.");
        }

        try {
            Integer i = (Integer) input[0];
        } catch(ClassCastException e) {
            throw new ClassCastException("The input array did not contain the total number of " +
                    "available appointments as the first argument.");
        }

    }
}
