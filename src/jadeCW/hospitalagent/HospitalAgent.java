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
            throw new ArrayIndexOutOfBoundsException("The Input array did not contain ");
        }

    }
}
