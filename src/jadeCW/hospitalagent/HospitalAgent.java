package jadeCW.hospitalagent;


import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;

/**
 * User: cb2109
 * Date: 06/02/14
 * Time: 15:45
 */
public class HospitalAgent extends Agent {

    private static final String SERVICE_NAME = "hospital_agent";
    private static final String SERVICE_TYPE = "hospital_management";
    private static final String SERVICE_ONTOLOGY = "hospital_ontology";

    private Integer appointmentsAvailable;

    @Override
    protected void setup() {
        super.setup();

        Object[] input = getArguments();

        if(input.length <= 0) {
            throw new ArrayIndexOutOfBoundsException("The Input array did not contain any elements.");
        }

        try {
            this.appointmentsAvailable = (Integer) input[0];
        } catch(ClassCastException e) {
            throw new ClassCastException("The input array did not contain the total number of " +
                    "available appointments as the first argument. \n Original message: " + e.getMessage());
        }

        System.out.println("Agent "+getLocalName()+" registering service \""+SERVICE_NAME+"\" of type " + SERVICE_TYPE +
                " with " + appointmentsAvailable + " max appointments available.");
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setName(SERVICE_NAME);
            sd.setType(SERVICE_TYPE);
            // Agents that want to use this service need to "know" the weather-forecast-ontology
            sd.addOntologies(SERVICE_ONTOLOGY);
            // Agents that want to use this service need to "speak" the FIPA-SL language
            sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
            dfd.addServices(sd);

            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            System.err.println(fe.getMessage());
            fe.printStackTrace();
        }

    }
}
