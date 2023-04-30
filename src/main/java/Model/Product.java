/** Product Class
 *
 *  * Aligned with UML class Diagram
 * */

/**
 *
 * @auther Keegan Melton
 * */
package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private int id;
    private String name;
    private int stock;
    private double price;
    private int max;
    private int min;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();



    public Product(int id, String name, double price,int stock, int max, int min){
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.max = max;
        this.min = min;
    }

    /**
     * @return id
     * */
    public int getId() {return id;}

    /**
     * @param id
     * */
    public void setId(int id) {this.id = id;}

    /**
     * @return name
     * */
    public String getName() {return name;}

    /**
     * @param name
     * */
    public void setName(String name) {this.name = name;}

    /**
     * @return price
     * */
    public double getPrice() {return price;}

    /**
     * @param price
     * */
    public void setPrice(double price) {this.price = price;}

    /**
     * @return stock
     * */
    public int getStock() {return stock;}

    /**
     * @param stock
     * */
    public void setStock(int stock) {this.stock = stock;}

    /**
     * @return max
     * */
    public int getMax() {return max;}

    /**
     * @param max
     * */
    public void setMax(int max) {this.max = max;}

    /**
     * @return min
     * */
    public int getMin() {return min;}

    /**
     * @param min
     * */
    public void setMin(int min) {this.min = min;}

    /**
     * @return associatedParts
     * */
    public ObservableList<Part> getAssociatedParts() {return associatedParts;}

    /**
     * @param part
     * */
    public void addAssociatedParts(Part part){
        associatedParts.add(part);}
    /**
     * @param part
     * @return true
     * */
    public boolean deleteAssociatedParts(Part part){
        associatedParts.remove(part);
        return true;
    }
}