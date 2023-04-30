package Controller;

import javafx.collections.FXCollections;
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

/**
 * AddProductsController - provides functionality to
 * the "AddProducts.fxml" screen
 * */

public class AddProductsController implements Initializable {
    public Label ProductLabel;
    public TextField ProductIDTF;
    public TextField ProductNameTF;
    public TextField ProductInvTF;
    public TextField ProductPriceTF;
    public TextField ProductMaxTF;
    public TextField ProductMinTF;
    public TableView<Part> AllPartsTableView;
    public TableColumn<Part, Integer> AllPartID;
    public TableColumn<Part, String> AllPartName;
    public TableColumn<Part, Integer> AllPartInv;
    public TableColumn<Part, Double> AllPartPrice;
    public TableView<Part> AssociatedPartsTableView;
    public TableColumn<Part, Integer> AssociatedPartID;
    public TableColumn<Part, String> AssociatedPartName;
    public TableColumn<Part, Integer> AssociatedPartInv;
    public TableColumn<Part, Double> AssociatedPartPrice;
    public TextField PartsSearchTF;
    public Button AddPartToAssociated;

    // nPdID (short for new product ID) holds the new product id number
    // once retrieved from "MainController"
    private static int nPdID;
    /** Receives the new product id number from the "MainController"
     *  and updates "nPdID" to be equal to that number
     *
     * @param newProductID
     */
    public static void newProductID(int newProductID) {
        nPdID = newProductID;
    }

    // An observable list called "partsToAdd" is created to store a temporary
    // list of parts to be associated with the product. Upon clicking the save
    // button, the list is added to the new product. If canceled however, the
    // list is deleted and all changes are not saved.
    private ObservableList<Part> partsToAdd = FXCollections.observableArrayList();

    /** Initializes controller
     *  Updates "AllPartsTableView" to match the "MainPartsTable" table view
     *  on the main menu, The "AssociatedPartsTableView" will display any
     *  part the user has added from the "AllPartsTableView"
     *
     * @param resourceBundle
     * @param url
     *  */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AllPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        AllPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        AllPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AllPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        AllPartsTableView.setItems(Inventory.getAllParts());

        AssociatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssociatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        AssociatedPartsTableView.setItems(partsToAdd);

    }


    /** Provides "Cancel Button" Functionality
     *  Any information entered is not saved and user is redirected back to the main menu screen
     *  (MainMenu.fxml), Clears "partsToAdd" list and no changes are saved.
     *
     *  @param actionEvent
     * */
    public void AddProductsCancel(ActionEvent actionEvent) throws IOException {
            partsToAdd.clear();

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 923,426);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
    }
    /** Provides "PartsSearchTF" Functionality
     * This method takes user input from the "PartSearchTF" and checks for matches.
     * Tableview "AllPartsTableView" is populated with matching search results.
     *
     * @param actionEvent
     * */
    public void PartsSearch(ActionEvent actionEvent) {
        String searchQuery = PartsSearchTF.getText();
        ObservableList<Part> namedParts = Inventory.lookupPartName(searchQuery);

        if(namedParts.size() == 0){
            AllPartsTableView.refresh();
            PartsSearchTF.setText("");
        }
        else {
            AllPartsTableView.setItems(namedParts);
            PartsSearchTF.setText("");
        }

    }


    /** Provides "Add Button" Functionality
     *  Clicking the add button will populate the selected part into the
     *  "AssociatedPartsTableView" by adding it to the "partsToAdd" list
     *
     * @param actionEvent
     * */
    public void AddPartToAssociated(ActionEvent actionEvent) {
        Part selectedPartToAdd = AllPartsTableView.getSelectionModel().getSelectedItem();
        partsToAdd.add(selectedPartToAdd);
    }

    /** Provides "Remove Associated Part" Functionality
     * This method removes the selected part in "AssociatedPartsTableView" on
     * button click, asks user for confirmation, and alerts user if nothing
     * is selected.
     *
     * @param actionEvent
     */
    public void RemoveAssociatedButton(ActionEvent actionEvent) {
        Part selectedPartToRemove = AssociatedPartsTableView.getSelectionModel().getSelectedItem();
        try {
            if (selectedPartToRemove == null) {
                // Alerts user if no part has been selected
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please select a part to remove.");
                alert.setTitle("Invalid Action!");
                alert.setHeaderText("No part selected");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to remove \"" + selectedPartToRemove.getName() + "\" from this product?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                partsToAdd.remove(selectedPartToRemove);
                AssociatedPartsTableView.refresh();
            }
        }
        catch (Exception e){
        }
    }


    /** Provides "Save Button" Functionality
     * On button click, this method gets user input from the text fields,
     * validates the input and alerts the user if needed, converts strings
     * to int/double data types as needed, adds new product and associated
     * parts if all validation checks are passed, and sends user back to the
     * Main Menu (MainMenu.fxml).
     *
     * @param actionEvent
     */
    public void SaveProductInfo(ActionEvent actionEvent) throws IOException {
        int id = nPdID;
        String nameInput = ProductNameTF.getText();
        String invInput = ProductInvTF.getText();
        String priceInput = ProductPriceTF.getText();
        String maxInput = ProductMaxTF.getText();
        String minInput = ProductMinTF.getText();

         /*
               checks for blank inputs in all field entered by the user
               if any blanks are found an alert will be displayed informing
               the which text field has been detected without information
            */
        String blankTF = "";
        if(nameInput.isBlank()){
            blankTF = "Name";
        }
        if(invInput.isBlank()){
            blankTF = "Inventory";
        }
        if(priceInput.isBlank()){
            blankTF = "Price/Cost";
        }
        if(maxInput.isBlank()){
            blankTF = "Max";
        }
        if(minInput.isBlank()){
            blankTF = "Min";
        }

        if (nameInput.isBlank() ||
                invInput.isBlank() ||
                priceInput.isBlank() ||
                maxInput.isBlank() ||
                minInput.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,blankTF + " must not be blank.");
            alert.setTitle("Invalid Action!");
            alert.setHeaderText("Blank Field Detected!");
            alert.showAndWait();
            return;
        }

        // Changes the necessary values to integers
        // outputs an error message if the wrong input is entered
        // notifies user numeric value is needed if parseInt fails
        String error = "";
        try {
            error = "Inventory";
            int stock = Integer.parseInt(invInput);
            error = "Price/Cost";
            double price = Double.parseDouble(priceInput);
            error = "Max";
            int max = Integer.parseInt(maxInput);
            error = "Min";
            int min = Integer.parseInt(minInput);
        } catch (NumberFormatException userInput) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText(error + " must be an number.");
            alert.showAndWait();
            return;
        }


        // changes strings to int/double as needed

        int stock = Integer.parseInt(invInput);
        double price = Double.parseDouble(priceInput);
        int max = Integer.parseInt(maxInput);
        int min = Integer.parseInt(minInput);

// Alerts user if a negative number has been entered
        if(min < 0 || stock < 0 || max < 0 || price < 0){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Values entered must be non-negative");
            alert.showAndWait();
            return;

        }

        // Alerts user if Min value is greater than Max
        if(max < min){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Min must not be greater than Max");
            alert.showAndWait();
            return;
        }

        // Alerts user if Inventory is not within Max/Min range
        if (stock > max || min > stock){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Inventory is not within Max/Min range.");
            alert.showAndWait();
            return;
        }

        // adds the product if all validation checks have passed
        Inventory.addProduct(new Product(id, nameInput,price,stock,max,min));

        //adds associated parts to the product
        associatePartsToProduct(partsToAdd);


        //redirects user to the main menu
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 923,426);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();

    }

    /** This method loops through the "partsToAdd" list and adds each part to the
     * "associatedParts" list for that product
     *
     * @param partsToAdd
     */
    public static void associatePartsToProduct(ObservableList<Part> partsToAdd) {
        int np = Inventory.getAllProducts().size() - 1;
        for(int i = 0; i < partsToAdd.size(); i++){
            Inventory.getAllProducts().get(np).addAssociatedParts(partsToAdd.get(i));
        }
    }
}
