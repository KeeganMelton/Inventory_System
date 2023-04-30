package Controller;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Model.Inventory;
import Model.Part;
import Model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @auther Keegan Melton
 *
 */

/** MainController - provides functionality to the "MainMenu.fxml" screen
 * */

public class MainController implements Initializable {

    public TableView<Part> MainPartsTable;
    public TableColumn<Part, Integer> MainPartID;
    public TableColumn<Part, String> MainPartName;
    public TableColumn<Part, Integer> MainPartInv;
    public TableColumn<Part, Double> MainPartPrice;
    public TextField PartsSearchTF;
    public Button DeletePartButton;
    public Button MainExitButton;
    public TextField ProductSearchTF;
    public TableView<Product> MainProductsTable;
    public TableColumn<Product, Integer> MainProductID;
    public TableColumn<Product, Double> MainProductName;
    public TableColumn<Product, Integer> MainProductInv;
    public TableColumn<Product, Double> MainProductPrice;

    /*
      "selectedPart" and "selectedProduct" are used to contain the
      user's selection and pass it to the appropriate modify controller.
    */
    private Part selectedPart = null;
    private Product selectedProduct = null;

    /*
      Initializes "newPartID" and "newProductID" at 1 instead of 0.
      In the event all parts or products have been removed, the new ID
      will be 1. If the list of parts or products is greater than 0,
      then "newPartID" and "newProductID" will be updated to one greater
      than the largest current ID and passed to the appropriate add controller.
    */
    private int newPartID = 1;
    private int newProductID = 1;

    /**Populates the "MainPartsTable" with the current list of parts and
    *  "MainProductsTable" with the current list of products.
    *  @param resourceBundle
    *  @param url
    */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Assigns values to populate Products Table View
        MainPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        MainPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        MainPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MainPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        MainPartsTable.setItems(Inventory.getAllParts());

        // Assigns values to populate Products Table View
        MainProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        MainProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        MainProductInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MainProductPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        MainProductsTable.setItems(Inventory.getAllProducts());
    }

    /** Provides "PartsSearchTF" Functionality
     * This method takes user input from the "PartSearchTF" and checks for matches,
     * Tableview "MainPartsTable" is populated with matching search results.
     * @param actionEvent
     * */
    public void PartsSearch(ActionEvent actionEvent) {
        String searchQuery = PartsSearchTF.getText();
        ObservableList<Part> namedParts = Inventory.lookupPartName(searchQuery);

        if(namedParts.size() == 0){
            MainPartsTable.refresh();
            PartsSearchTF.setText("");
        }
        else {
            MainPartsTable.setItems(namedParts);
            PartsSearchTF.setText("");
        }

    }

    /** Provides "ProductSearchTF" Functionality
     * This method takes user input from the "ProductSearchTF" and checks for matches,
     * Tableview "MainProductsTable" is populated with matching search results.
     * @param actionEvent
     * */
    public void ProductSearch(ActionEvent actionEvent) {
        String searchQuery = ProductSearchTF.getText();
        ObservableList<Product> namedProducts = Inventory.lookupProductName(searchQuery);

        if(namedProducts.size() == 0){
            MainProductsTable.refresh();
            ProductSearchTF.setText("");
        }
        else {
            MainProductsTable.setItems(namedProducts);
            PartsSearchTF.setText("");
        }

    }

    /** Provides "Add Button" Functionality (Parts/Left Side),
     * This method brings the user to the Add Part screen (AddParts.fxml),
     * On button click, sends the new part ID to the "AddPartsController".
     * @param actionEvent
     */
    public void ToAddParts(ActionEvent actionEvent) throws IOException {
        // gets an ID to be used for the new part
        generateNewPartID();

        // passes new part ID to "AddPartsController"
        AddPartsController.newPartID(newPartID);

        // Sends User to the "AddParts.fxml" screen
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddParts.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600,400);
        stage.setTitle("Add Parts");
        stage.setScene(scene);
        stage.show();
    }


    /** Provides "Modify Button" Functionality (Parts/Left Side),
     *  This method brings the user to the Modify Parts screen (ModifyParts.fxml),
     *  Passes user selected Part to the "ModifyPartsController",
     *  Alerts user if no product is selected.
     *  @param actionEvent
     * */
    public void ToModifyParts(ActionEvent actionEvent) throws IOException {
        // store user selection in "selectedPart"
        Part selectedPart = MainPartsTable.getSelectionModel().getSelectedItem();

        //Alerts the user if no part is selected to modify
        try {
            if(selectedPart == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Please select a part to modify.");
                alert.setTitle("Invalid Action!");
                alert.setHeaderText("No part selected");
                alert.showAndWait();
                return;
            }
            // Passes the selected part to "ModifyPartsController"
            ModifyPartsController.partToModify(selectedPart);

            // Brings user to modify parts screen
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyParts.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("Modify Parts");
            Scene scene = new Scene(root, 600,400);
            stage.setScene(scene);
            stage.show();
        }

        catch(Exception e){

        }
    }

    /** Provides "Add Button" Functionality (Products/Right Side),
     *  This method brings user to Add Products Screen (AddProducts.fxml),
     *  Sends the new product ID to the "AddProductController"
     *  @param actionEvent
     */
    public void ToAddProducts(ActionEvent actionEvent) throws IOException {
        // gets an ID to be used for the new product
        generateNewProductID();

        // Brings user to Add Products Screen
        AddProductsController.newProductID(newProductID);
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProducts.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 875,600);
        stage.setTitle("Add Products");
        stage.setScene(scene);
        stage.show();
    }


    /** Provides "Modify Button" Functionality (Products/Right Side),
     *  This method brings user to Modify Products screen (ModifyProducts.fxml),
     *  Passes selected product to "ModifyProductsController",
     *  Alerts user if no product is selected
     *  @param actionEvent
     * */
    public void ToModifyProducts(ActionEvent actionEvent) throws IOException {
        // store user selection in "selectedProduct"
        Product selectedProduct = MainProductsTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedProduct == null) {
                // Alerts user if no product is selected
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Please select a product to modify.");
                alert.setTitle("Invalid Action!");
                alert.setHeaderText("No product selected");
                alert.showAndWait();
                return;
            }

            // Passes selected product to "ModifyProductsController"
            ModifyProductsController.productToModify(selectedProduct);

            // Brings User to Modify Products screen
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProducts.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 875,600);
            stage.setTitle("Modify Products");
            stage.setScene(scene);
            stage.show();
        }

        catch(Exception e){
        }
    }

    /** Provides "Delete Button" Functionality (Part/Left Side),
     *  This method deletes user selected part, asks for confirmation,
     *  and alerts if no part has been selected
     *
     *  *FUTURE ENHANCEMENT*
     *
     *  Checking to see if a product contained that part in an associated list
     *  and notify it needed to be removed if found.
     *  @param actionEvent
     */
    public void DeleteSelectedPart(ActionEvent actionEvent) {
        // store user selection in "deletedPart"
        Part deletePart = MainPartsTable.getSelectionModel().getSelectedItem();
        try {
            if (deletePart == null) {
                // Alerts user if no part has been selected
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"Please select a part to delete.");
                alert.setTitle("Invalid Action!");
                alert.setHeaderText("No part selected");
                alert.showAndWait();
                return;
            }

            // Asks user for confirmation before deleting the part
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete \"" + deletePart.getName() + "\" from parts inventory?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                Inventory.deletePart(deletePart);
                MainPartsTable.refresh();
            }
        }

        catch(Exception e){
        }
    }

    /** Provides "Delete Button" Functionality (Product/Right Side),
     *  This method deletes user selected product, asks for confirmation,
     *  alerts if no part has been selected, and informs user that if
     *  the selected part has associated parts.
     *  @param actionEvent
     */
    public void DeleteProductButton(ActionEvent actionEvent) {
        // store user selection in "deletedProduct"
        Product deleteProduct = MainProductsTable.getSelectionModel().getSelectedItem();
        try {
            if (deleteProduct == null) {
                // Alerts user no product has been selected
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION, "Please select a product to delete.");
                informationAlert.setTitle("Invalid Action!");
                informationAlert.setHeaderText("No product selected");
                informationAlert.showAndWait();
            }
            if (deleteProduct != null && deleteProduct.getAssociatedParts().size() > 0) {
                // Informs user associated parts must be removed from the product
                Alert WarningAlert = new Alert(Alert.AlertType.WARNING,
                        "Please remove associated parts from the product by modifying the product");
                WarningAlert.setTitle("Invalid Action!");
                WarningAlert.setHeaderText("Associated parts detected!");
                WarningAlert.showAndWait();
            }
            if (deleteProduct != null && deleteProduct.getAssociatedParts().size() == 0) {
                // Asks user for confirmation before deleting the product
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure you want to delete \"" + deleteProduct.getName() + "\" from products inventory?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Inventory.deleteProduct(deleteProduct);
                    MainProductsTable.refresh();
                }
            }
        }
        catch (Exception e){
        }
    }


    /** Provides "Exit Button" Functionality
     *  This method closes the application.
     *  @param actionEvent
     *
     */
    public void ExitApp(ActionEvent actionEvent) {
        System.exit(0);
    }

    /** This method loops through the list of current IDs and adds 1 to the
     * highest value to generate the new ID "newPartID" will remain at 1
     * if all Parts have been removed
     *
     * *RUNTIME ERROR*
     * This corrected an issue where if a part that was not the one with the
     * highest part ID was deleted before adding another part, the new part
     * would be generated with the same ID as the highest ID.
     */
    private void generateNewPartID() {
        for (int i = 0; i < Inventory.getAllParts().size(); i++) {
            if (i == 0) {
                newPartID = Inventory.getAllParts().get(i).getId() + 1;
            }
            if (i > 0 && newPartID <= Inventory.getAllParts().get(i).getId()) {
                newPartID = Inventory.getAllParts().get(i).getId() + 1;
            }
        }
    }

    /** This method loops through the list of current IDs and adds 1 to the
     * highest value to generate the new ID "newProductID" will remain at 1
     * if all Parts have been removed
     *
     * *RUNTIME ERROR*
     * This corrected an issue where if a product that was not the one with the
     * highest product ID was deleted before adding another product, the new
     * product would be generated with the same ID as the highest ID.
     */
    private void generateNewProductID() {
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            if (i == 0) {
                newProductID = Inventory.getAllProducts().get(i).getId();
            }
            if (i > 0 && newProductID <= Inventory.getAllProducts().get(i).getId()) {
                newProductID = Inventory.getAllProducts().get(i).getId() + 1;
            }
        }
    }
}