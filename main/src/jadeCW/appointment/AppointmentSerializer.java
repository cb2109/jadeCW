package jadeCW.appointment;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Author: Christopher Bates
 * Date: 16/02/14
 */
public class AppointmentSerializer {

    public static ArrayList<Appointment> deserialize(String appointments) {
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
        StringTokenizer tok = new StringTokenizer(appointments);
        while(tok.hasMoreTokens()) {
            int appt = Integer.decode(tok.nextToken());
            appointmentList.add(new Appointment(appt));
        }

        return appointmentList;
    }

    public static String serialize(ArrayList<Appointment> appointments) {
        StringBuilder builder = new StringBuilder();
        for(Appointment a : appointments) {
            builder.append(a.getSlot()).append(" ");
        }

        return builder.toString();
    }

}
