package jadeCW.patient;

import jade.content.OntoACLMessage;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jadeCW.appointment.Appointment;
import jadeCW.appointment.AppointmentSerializer;

import java.util.ArrayList;

/**
 * Author: Christopher Bates
 * Date: 15/02/14
 */
public class RequestAppointment extends SimpleBehaviour {

    private PatientAgent agent;
    private MessageTemplate template;
    private State state = State.START;
    private Appointment best;

    RequestAppointment(PatientAgent agent) {
        this.agent = agent;
    }

    @Override
    public void action() {

        if(agent.getAppointmentProvider() == null || agent.hasAppointment()) {
            return;
        }

        switch(state) {
            case START:
                OntoACLMessage message = new OntoACLMessage();
                message.setSender(agent.getAID());
                message.addReceiver(agent.getAppointmentProvider());
                message.setPerformative(ACLMessage.INFORM);
                message.setConversationId("allocate-appointment");
                message.setReplyWith("cfp" + System.currentTimeMillis());


                agent.send(message);
                // Prepare the template to get proposals
                template = MessageTemplate.and(MessageTemplate.MatchConversationId("allocate-appointment"),
                        MessageTemplate.MatchInReplyTo(message.getReplyWith()));
                state = State.SENT_REQUEST;
                break;
            case SENT_REQUEST:
                ACLMessage reply = myAgent.receive();
                if(reply!= null) {
                    if(reply.getPerformative() == ACLMessage.PROPOSE) {
                        String apptsString = reply.getContent();
                        ArrayList<Appointment> appts = AppointmentSerializer.deserialize(apptsString);
                        best = agent.getBestAppointment(appts);

                        ACLMessage myReply = reply.createReply();
                        myReply.setContent(best.getSlot() + "");
                        agent.send(myReply);

                        MessageTemplate.and(MessageTemplate.MatchConversationId("allocate-appointment"),
                                MessageTemplate.MatchInReplyTo(myReply.getReplyWith()));

                        state = State.CONFIRMING_APPOINTMENT;
                    }
                }
                break;
            case CONFIRMING_APPOINTMENT:
                ACLMessage replyFinal = myAgent.receive();
                if(replyFinal.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                    agent.setAppointment(best);
                } else {
                    this.best = null;
                    this.state = State.START;
                }

                break;
        }




    }

    @Override
    public boolean done() {
        return agent.hasAppointment();  //To change body of implemented methods use File | Settings | File Templates.
    }

    private enum State {
        START, SENT_REQUEST, CONFIRMING_APPOINTMENT
    }
}
