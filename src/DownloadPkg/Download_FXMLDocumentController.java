/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DownloadPkg;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

/**
 *
 * @author ammar
 */
public class Download_FXMLDocumentController implements Initializable {

    @FXML
    private ComboBox<?> cmbox;
    @FXML
    private Button btndownload;
    @FXML
    private Button backbtn;
    @FXML
    private ListView<String> mylogs;
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    @FXML
    private void handledwnBtnAction(ActionEvent event) {
       
        ListView<String> list = new ListView<String>();
        ObservableList<String> items =FXCollections.observableArrayList (
        "Single", "Double", "Suite", "Family App");
        mylogs.setItems(items);
        
    }

    @FXML
    private void handleBackBtnAction(ActionEvent event) {
    }

   
}
