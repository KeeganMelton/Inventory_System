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
 * ModifyProductsController - provides functionality to
 * the "ModifyProducts.fxml" screen
 * */

public class ModifyProductsController implements Initializable {
    public TextField ProductIDTF;
    public TextField ProductNameTF;
    public TextField ProductInvTF;
    public TextField ProductPriceTF;
    public TextField ProductMaxTF;
    public TextField ProductMinTF;
    public TextField PartsSearchTF;
    public TableView<Part> AllPartsTableView;
    public TableColumn<Part, Integer> AllPartID;
    public TableColumn<Part, String> AllPartName;
    public TableColumn<Part,Integer> AllPartInv;
    public TableColumn<Part, Double> AllPartPrice;
    public Button AddPartToAssociated;
    public TableView<Part> AssociatedPartsTableView;
    public TableColumn<Part, Integer> AssociatedPartID;
    public TableColumn<Part, String> AssociatedPartName;
    public TableColumn<Part, Integer> AssociatedPartInv;
    public TableColumn<Part, Double> AssociatedPartPrice;

    // mProduct (short for modify Product) holds the selected
    // product sent over from "MainController"
    private static Product mProduct = null;

    // gets index of mProduct
    private int productIndex = Inventory.getAllProducts().indexOf(mProduct);

    /**  mProduct is updated to the selected product sent over from
     *  "MainController"
     *
     * @param selectedProduct
     */
    public static void productToModify(Product selectedProduct) {
        mProduct = selectedProduct;
    }

    // An observable list called "tempAssociatedPartsList" is created to store the
    // current list of parts associated with the selected product. If the modification
    // is canceled, changes will not be saved and the original list is maintained. If
    // the modification is saved then the associatedParts list with that product will
    // be updated accordingly
    private ObservableList<Part> tempAssociatedPartsList = FXCollections.observableArrayList();

    /** Initializes controller
     *  Updates "AllPartsTableView" to match the "MainPartsTable" table view
     *  on the main menu, The parts associated with the selected product are
     *  loaded into a temporary list which holds any changes until saved or
     *  canceled, The "AssociatedPartsTableView" displays the
     *  "tempAssociatedPartsList"
     *
     *
     * @param resourceBundle
     * @param url
     *  */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { fillProductInfo();}

    /** Fills the text fields with information of the selected product
     * Updates "AllPartsTableView" to match the "MainPartsTable" table view
     * on the main menu, The parts associated with the selected product are
     * loaded into a temporary list which holds any changes until saved or
     * canceled, The "AssociatedPartsTableView" displays the
     * "tempAssociatedPartsList"
     */
    private void fillProductInfo() {
        for(int i = 0; i < mProduct.getAssociatedParts().size(); i++){
            tempAssociatedPartsList.add(Inventory.getAllProducts().get(productIndex).getAssociatedParts().get(i));
        }

        AllPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        AllPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        AllPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AllPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        AllPartsTableView.setItems(Inventory.getAllParts());

        AssociatedPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssociatedPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        AssociatedPartsTableView.setItems(tempAssociatedPartsList);

        ProductIDTF.setPromptText(Integer.toString(mProduct.getId()));
        ProductNameTF.setText(mProduct.getName());
        ProductInvTF.setText(Integer.toString(mProduct.getStock()));
        ProductPriceTF.setText(Double.toString(mProduct.getPrice()));
        ProductMaxTF.setText(Integer.toString(mProduct.getMax()));
        ProductMinTF.setText(Integer.toString(mProduct.getMin()));

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
     *  "AssociatedPartsTableView" by adding it to the "tempAssociatedPartsList" list
     *
     * @param actionEvent
     * */
    public void AddPartToAssociated(ActionEvent actionEvent) {
        Part selectedPartToAdd = AllPartsTableView.getSelectionModel().getSelectedItem();
        tempAssociatedPartsList.add(selectedPartToAdd);
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

                tempAssociatedPartsList.remove(selectedPartToRemove);
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
     *
     * @param actionEvent
     */
    public void SaveProductInfo(ActionEvent actionEvent) throws IOException{

        int id = mProduct.getId();
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

        // after validation checks have passed the current list of associated parts
        // is deleted and replaced with the temporary list as the product modifications
        // are saved

        int numberOfPartsToRemove = mProduct.getAssociatedParts().size() - 1;

        for(int i = numberOfPartsToRemove; i > -1; i--){
            mProduct.deleteAssociatedParts(Inventory.getAllProducts().get(productIndex).getAssociatedParts().get(i));
        }
        mProduct.setName(nameInput);
        mProduct.setStock(stock);
        mProduct.setPrice(price);
        mProduct.setMax(max);
        mProduct.setMin(min);
        Inventory.updateProduct(productIndex, mProduct);
        associatePartsToProduct(tempAssociatedPartsList);


        // redirects user back to the Main Menu
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 923,426);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();

    }

    /** This method loops through the "tempAssociatedPartsList" list and adds
     *  each part to the "associatedParts" list for that product
     *
     * @param tempAssociatedPartsList
     */
    public static void associatePartsToProduct(ObservableList<Part> tempAssociatedPartsList) {
        int np = Inventory.getAllProducts().size() - 1;
        for(int i = 0; i < tempAssociatedPartsList.size(); i++){
            Inventory.getAllProducts().get(np).addAssociatedParts(tempAssociatedPartsList.get(i));
        }
    }


    /** Provides "Cancel Button" Functionality
     *  Any information entered is not saved and user is redirected back to the main menu screen
     *  (MainMenu.fxml), Clears "tempAssociatedPartsList" list and no changes are saved.
     *
     *  @param actionEvent
     * */
    public void ModifyProductsCancel(ActionEvent actionEvent) throws IOException {
        // For loop clears out tempAssociatedPartsList not altering associated parts
        for(int i = 0; i < tempAssociatedPartsList.size(); i++){
            tempAssociatedPartsList.remove(i);
        }

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 923,426);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
}
