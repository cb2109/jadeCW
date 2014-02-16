package jadeCW.appointment;

import java.io.Serializable;

/**
 * Author: Christopher Bates
 * Date: 15/02/14
 */
public class Appointment implements Serializable {
    private int slot;

    public Appointment(int slotNumber) {
        this.slot = slotNumber;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
