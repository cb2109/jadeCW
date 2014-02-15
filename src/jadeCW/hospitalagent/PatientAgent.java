package jadeCW.hospitalagent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.introspection.ACLMessage;
import jade.proto.SubscriptionInitiator;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Author: Christopher Bates
 * Date: 15/02/14
 */
public class PatientAgent extends Agent {

    private static final String APPT_SEPARATOR = "-";
    private static final String SERVICE_TYPE = "hospital_management";

    private ArrayList<Integer> appointmentPrefs;
    private Integer lowestAppointmentLevel;

    private ArrayList<ServiceDescription> descriptions;

    @Override
    protected void setup() {
        super.setup();

        Object[] input = getArguments();

        if(input.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("The Input array did not contain any elements.");
        }

        readPreferences((String) input[0]);

        register();
    }

    private void register() {

        descriptions = new ArrayList<ServiceDescription>();

        DFAgentDescription searchDesc = new DFAgentDescription();
        ServiceDescription serviceTypeSearch = new ServiceDescription();
        serviceTypeSearch.setType(SERVICE_TYPE);
        searchDesc.addServices(serviceTypeSearch);

        SearchConstraints sc = new SearchConstraints();
        // We want to receive 10 results at most
        sc.setMaxResults((long) 10);

        addBehaviour(new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, getDefaultDF(), searchDesc, sc)) {
            protected void handleInform(ACLMessage inform) {
                try {
                    DFAgentDescription[] results = DFService.decodeNotification(inform.getAclRepresentation());
                    if (results.length > 0) {
                        for (DFAgentDescription dfd : results) {
                            //AID provider = dfd.getName();

                            jade.util.leap.Iterator it = dfd.getAllServices();
                            while (it.hasNext()) {
                                ServiceDescription sd = (ServiceDescription) it.next();

                                if (sd.getType().equals(SERVICE_TYPE)) {
                                    descriptions.add(sd);
                                }
                            }
                        }
                    }
                }
                catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        } );
    }

    private void readPreferences(String input) {

        // sets up appointment preferences, which is a list of appointments
        // and a number describing which preference level they are at
        appointmentPrefs = new ArrayList<Integer>();

        StringTokenizer tokenizer = new StringTokenizer(input);
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if(token.equals(APPT_SEPARATOR)) {
                lowestAppointmentLevel++;
            } else {
                Integer num;
                try {
                    num = Integer.decode(token);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }
                // only set if not set before (always assume higher prefs hold)
                if(appointmentPrefs.get(num) == null) {
                    appointmentPrefs.set(num, lowestAppointmentLevel);
                }
            }
        }



    }
}
