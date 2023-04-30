package software_i.project;

 /**  @auther Keegan Melton */

import Model.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** This is the Main Class begins the application.
 *
 *  Begins the application and opens the main screen.
 *
 * */

public class Main extends Application {

/** start method initializes fxml file MainMenu.fxml
 *
 * @param primaryStage
 * */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 923, 426));
        primaryStage.show();

        initStartingData();
    }
        /** initStartingData method manually loads sample data is used to populate the tables and test the application */
        private void initStartingData() {
        Part part1 = new InHouse(1, "Seat",16.99,26, 30, 1, 123);
        Part part2 = new InHouse(2, "Brakes",22.25,15, 30, 1,5224);
        Part part3 = new Outsourced(3, "Tire",37.00,19, 30, 1, "Walmart");
        Part part4 = new Outsourced(4, "Handlebars",  27.75,8, 30, 1, "Amazon");

        Inventory.getAllParts().add(part1);
        Inventory.getAllParts().add(part2);
        Inventory.getAllParts().add(part3);
        Inventory.getAllParts().add(part4);

        Product product1 = new Product(1,"Unicycle",99.00,13,25,1);
        Product product2 = new Product(2,"Bicycle",199.99,12,25,1);
        Product product3 = new Product(3,"Tricycle",299.99,14,25,1);

        Inventory.getAllProducts().add(product1);
        Inventory.getAllProducts().add(product2);
        Inventory.getAllProducts().add(product3);

        product1.addAssociatedParts(part1);
        product1.addAssociatedParts(part3);

        product2.addAssociatedParts(part3);
        product2.addAssociatedParts(part3);
        product2.addAssociatedParts(part1);
        product2.addAssociatedParts(part2);
        product2.addAssociatedParts(part4);

        product3.addAssociatedParts(part3);
        product3.addAssociatedParts(part3);
        product3.addAssociatedParts(part3);
        product3.addAssociatedParts(part1);
        product3.addAssociatedParts(part2);
        product3.addAssociatedParts(part4);
        }

    /** main launches the application
     *
     * @param args
     * */
    public static void main(String[] args) {
        launch();
    }
}