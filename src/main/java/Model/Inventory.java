/** Inventory class.
 *
 * Aligned with UML class Diagram
 *
 * Stores observable lists of parts and products
 * */

/**
 *
 * @auther Keegan Melton
 * */

package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

public class Inventory{

    //creates observable lists of all parts called "allParts"
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    //creates observable list of all products called "allProducts"
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * @param part
     * */
    public static void addPart(Part part){allParts.add(part);}

    /**
     * @param product
     * */
    public static void addProduct(Product product) {allProducts.add(product);}

    /**
     * Takes the Integer value of the users search and
     * checks the current list of part IDs for a match
     * if a match is found, it will be return and used
     * in the "lookupPartName" method. If no match is
     * found, nothing is returned.
     * @param searchID
     * @return pt or
     * @return null
     */
    public static Part lookupPartID(int searchID) {
        for (int i = 0; i < allParts.size(); i++){
            Part pt = allParts.get(i);
            if (pt.getId() == searchID) {
                return pt;
            }
        }
        return null;
    }
    /**
     * Takes the Integer value of the users search and
     * checks the current list of product IDs for a match
     * if a match is found, it will be return and used
     * in the "lookupProductName" method. If no match is
     * found, nothing is returned.
     * @param searchID
     * @return pt or
     * @return null
     */
    public static Product lookupProductID(int searchID) {
        for (int i = 0; i < allProducts.size(); i++){
            Product pd = allProducts.get(i);
            if (pd.getId() == searchID) {
                return pd;
            }
        }
        return null;
    }

    /**
     * Takes the String value of the user's search query and
     * checks for matching results, If a result is found, it
     * is added to the "namedParts" observable list, If no
     * result is found, the String is parsed into an Integer value
     * and check again for matching values using the "lookupPartID"
     * method. If the parse is unsuccessful or no results are found,
     * the users will be alerted that there were no results found,
     * A try/catch block used in the event the string could not
     * successfully parse in to an Integer and to alert the user if
     * no results were found, The "namedParts" list is returned to
     * update the table view with successful results
     *
     * *RUNTIME ERROR*
     *
     * Try/Catch block was added to alert the user if a string could
     * not be parsed into an integer successfully, avoiding an error.
     *
     * @param partialName
     * @return namedParts
     */
    public static ObservableList<Part> lookupPartName(String partialName){
        ObservableList<Part> namedParts = FXCollections.observableArrayList();
        try {
            for (Part pt : allParts) {
                if (pt.getName().contains(partialName)) {
                    namedParts.add(pt);
                }
            }

            if (namedParts.size() == 0) {
                int id = Integer.parseInt(partialName);
                Part pt = lookupPartID(id);
                if (pt != null) {
                    namedParts.add(pt);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"No search results found.");
                    alert.setHeaderText("Part Not Found");
                    alert.showAndWait();
                }
            }
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"No search results found.");
            alert.setHeaderText("Part Not Found");
            alert.showAndWait();
        }

        return namedParts;
    }

    /**
     * Takes the String value of the user's search query and
     * checks for matching results, If a result is found, it
     * is added to the "namedProducts" observable list, If no
     * result is found, the String is parsed into an Integer value
     * and check again for matching values using the "lookupProductID"
     * method, If the parse is unsuccessful or no results are found,
     * the users will be alerted that there were no results found,
     * A try/catch block used in the event the string could not
     * successfully parse in to an Integer and to alert the user if
     * no results were found, The "namedProducts" list is returned to
     * update the table view with successful results
     *
     * *RUNTIME ERROR*
     *
     * Try/Catch block was added to alert the user if a string could
     * not be parsed into an integer successfully, avoiding an error.
     * @param partialName
     * @return namedParts
     */
    public static ObservableList<Product> lookupProductName(String partialName){
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();

        try {
            for (Product pd : allProducts) {
                if (pd.getName().contains(partialName)) {
                    namedProducts.add(pd);
                }
            }

            if (namedProducts.size() == 0) {
                int id = Integer.parseInt(partialName);
                Product pd = lookupProductID(id);
                if (pd != null) {
                    namedProducts.add(pd);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,"No search results found.");
                    alert.setHeaderText("Product Not Found");
                    alert.showAndWait();
                }
            }
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"No search results found.");
            alert.setHeaderText("Product Not Found");
            alert.showAndWait();
        }

        return namedProducts;
    }

    /**
     * @param index
     * @param selectedPart
     * */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index,selectedPart);
    }
    /**
     * @param index
     * @param selectedProduct
     * */
    public static void updateProduct(int index, Product selectedProduct){
    allProducts.set(index,selectedProduct);
    }

    /**
     * @param deletePart
     * @return true
     * */
    public static boolean deletePart(Part deletePart) {
        allParts.remove(deletePart);
        return true;
    }

    /**
     * @param deleteProduct
     * @return true
     * */
    public static boolean deleteProduct(Product deleteProduct) {
        allProducts.remove(deleteProduct);
        return true;
    }

    /**
     * @return allParts
     * */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * @return allProducts
     * */
    public static ObservableList<Product> getAllProducts() { return allProducts;}

}