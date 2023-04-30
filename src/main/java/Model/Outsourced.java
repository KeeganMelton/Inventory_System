/** Outsourced extension of Part class.
 *
 * Aligned with UML class Diagram
 * */

/**
 *
 * @auther Keegan Melton
 * */
package Model;

public class Outsourced extends Part{

    private String companyName;
    public Outsourced(int id, String name, double price,int stock, int max, int min, String companyName) {
        super(id, name, price,stock, max, min);
        this.companyName = companyName;
    }

    /**
     * @param companyName
     * */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return companyName
     * */
    public String getCompanyName() {
        return companyName;
    }
}