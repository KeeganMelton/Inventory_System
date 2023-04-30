package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Model.Inventory;
import Model.Part;
import Model.InHouse;
import Model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @auther Keegan Melton
 *
 */

/**
 * ModifyPartsController - provides functionality to
 * the "ModifyParts.fxml" screen
 * */
public class ModifyPartsController implements Initializable{

    public RadioButton InHouseRB;
    public RadioButton OutsourcedRB;
    public TextField PartIDTF;
    public TextField PartNameTF;
    public TextField PartInvTF;
    public TextField PartPriceTF;
    public TextField PartMaxTF;
    public TextField PartMinTF;
    public TextField SourceTF;
    public Label RadioButtonLabel;

    // mPart (short for modify Part) holds the selected part
    // sent over from "MainController"
    private static Part mPart = null;

    /**  mPart is updated to the selected part sent over from
     *  "MainController"
     *
     * @param selectedPart
     */
    public static void partToModify(Part selectedPart) {
        mPart = selectedPart;
    }

    /**Initializes controller
     *
     * Fills in the information of the selected part (mPart).
     *
     * @param url
     * @param resourceBundle
     * */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillPartInfo();
    }


    /** Fills the text fields with information of the selected part.
     *  Set radio buttons and "RadioButtonLabel" to the correct setting.
     */
    private void fillPartInfo() {
        if(mPart instanceof InHouse) {
            InHouseRB.setSelected(true);
            RadioButtonLabel.setText("Machine ID");
            PartIDTF.setPromptText(Integer.toString(mPart.getId()));
            PartNameTF.setText(mPart.getName());
            PartInvTF.setText(Integer.toString(mPart.getStock()));
            PartPriceTF.setText(Double.toString(mPart.getPrice()));
            PartMaxTF.setText(Integer.toString(mPart.getMax()));
            PartMinTF.setText(Integer.toString(mPart.getMin()));
            SourceTF.setText(Integer.toString(((InHouse) mPart).getMachineID()));
        }

        if(mPart instanceof Outsourced){
            OutsourcedRB.setSelected(true);
            RadioButtonLabel.setText("Company Name");
            PartIDTF.setPromptText(Integer.toString(mPart.getId()));
            PartNameTF.setText(mPart.getName());
            PartInvTF.setText(Integer.toString(mPart.getStock()));
            PartPriceTF.setText(Double.toString(mPart.getPrice()));
            PartMaxTF.setText(Integer.toString(mPart.getMax()));
            PartMinTF.setText(Integer.toString(mPart.getMin()));
            SourceTF.setText(((Outsourced) mPart).getCompanyName());
        }
    }

    /** Updates "RadioButtonLabel" to "Machine ID" if "In-House" is selected
     *
     * @param actionEvent
     * */
    public void InHouseSelected(ActionEvent actionEvent) {
        RadioButtonLabel.setText("Machine ID");
    }

    /** Updates "RadioButtonLabel" to "Company Name" if "Outsourced" is selected
     *
     * @param actionEvent
     * */
    public void OutsourcedSelected(ActionEvent actionEvent) {
        RadioButtonLabel.setText("Company Name");
    }

    /** Provides "Save Button" Functionality
     *  Gets the information from text fields,
     *  validates the input and alerts the user if needed,
     *  and converts strings to other data types as needed,
     *  Radio button selection is broken into IF statements,
     *  If the radio button selection remains the same, the
     *  information is updated, if the selection is changed
     *  from what originally was, the part is deleted and
     *  re-added with the updated information. Redirects
     *  user back to Main Menu (MainMenu.fxml)
     *
     * *FUTURE ENHANCEMENT*
     * Checking the part name to see if it already exists
     * in the current list of parts. Alternatively adding a
     * description box to help differentiate parts of the
     * same name
     *
     * *FUTURE ENHANCEMENT*
     * Checking to see if the Company Name on outsourced
     * parts contains only numbers.
     *
     * @param actionEvent
     */

    public void SavePartInfo(ActionEvent actionEvent) throws Exception{

        // gets the index number of the selected part
        int indexNum = Inventory.getAllParts().indexOf(mPart);

        // "In-House" radio button option code begins
        if(InHouseRB.isSelected()) {

            // get the part ID, this is not altered
            int id = mPart.getId();

            //begins by setting all the inputs from the user into strings
            String nameInput = PartNameTF.getText();
            String invInput = PartInvTF.getText();
            String priceInput = PartPriceTF.getText();
            String maxInput = PartMaxTF.getText();
            String minInput = PartMinTF.getText();
            String machineIDInput = SourceTF.getText();

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
            if(machineIDInput.isBlank()){
                blankTF = "Machine ID";
            }

            if (nameInput.isBlank() ||
                    invInput.isBlank() ||
                    priceInput.isBlank() ||
                    maxInput.isBlank() ||
                    minInput.isBlank() ||
                    machineIDInput.isBlank()) {
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
                error = "Machine ID";
                int machineID = Integer.parseInt(machineIDInput);
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
            int machineID = Integer.parseInt(machineIDInput);

            // Alerts user if a negative number has been entered
            if(min < 0 || stock < 0 || max < 0 || machineID < 0 || price < 0){
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

            // Saves any changes once validation checks pass
            mPart.setName(nameInput);
            mPart.setStock(stock);
            mPart.setPrice(price);
            mPart.setMax(max);
            mPart.setMin(min);
            try {
                ((InHouse) Inventory.getAllParts().get(indexNum)).setMachineID(machineID);
                Inventory.updatePart(indexNum, mPart);
            }
            catch(Exception e){
                Inventory.deletePart(mPart);
                Inventory.addPart(new InHouse(id,nameInput,price,stock,max,min,machineID));
            }

            //brings main menu back up
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 923,426);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();

        }
        if(OutsourcedRB.isSelected()) {
            int id = mPart.getId();
            //begins by setting all the inputs from the user into strings
            String nameInput = PartNameTF.getText();
            String invInput = PartInvTF.getText();
            String priceInput = PartPriceTF.getText();
            String maxInput = PartMaxTF.getText();
            String minInput = PartMinTF.getText();
            String companyNameInput = SourceTF.getText();

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
            if(companyNameInput.isBlank()){
                blankTF = "Machine ID";
            }

            if (nameInput.isBlank() ||
                    invInput.isBlank() ||
                    priceInput.isBlank() ||
                    maxInput.isBlank() ||
                    minInput.isBlank() ||
                    companyNameInput.isBlank()) {
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

            // Saves any changes once validation checks pass
            mPart.setName(nameInput);
            mPart.setStock(stock);
            mPart.setPrice(price);
            mPart.setMax(max);
            mPart.setMin(min);
            try {
                ((Outsourced) Inventory.getAllParts().get(indexNum)).setCompanyName(companyNameInput);
                Inventory.updatePart(indexNum, mPart);
            }
            catch(Exception e){
                Inventory.deletePart(mPart);
                Inventory.addPart(new Outsourced(id,nameInput,price,stock,max,min,companyNameInput));
            }

            //brings main menu back up
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 923, 426);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        }
    }

    /** Provides "Cancel Button" Functionality
     *  Any information entered is not saved and user is redirected back to the main menu screen
     * (MainMenu.fxml)
     *
     * @param actionEvent
     * */
    public void ModifyPartsCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 923,426);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
}
