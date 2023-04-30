/** In-House extension of Part class.
 *
 * Aligned with UML class Diagram
 * */

/**
 *
 * @auther Keegan Melton
 * */

package Model;

public class InHouse extends Part{

    private int machineID;
    public InHouse(int id, String name, double price,int stock, int max, int min, int machineID) {
        super(id, name, price, stock, max, min);
        this.machineID = machineID;
    }

    /**
     * @param machineID
     * */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     * @return machineID
     * */
    public int getMachineID() {
        return machineID;
    }
}
