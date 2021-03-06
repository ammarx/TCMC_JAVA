/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPkg;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import UtilsPkg.Version;

/**
 *
 * @author ammar
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Version version =  new Version();
        
        Parent root = FXMLLoader.load(getClass().getResource("Main_FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Launcher - " + version.getversion());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
