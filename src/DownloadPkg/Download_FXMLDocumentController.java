/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DownloadPkg;

import MainPkg.Main_FXMLDocumentController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

/**
 *
 * @author ammar
 */
public class Download_FXMLDocumentController implements Initializable {

    @FXML
    private ComboBox cmbox;
    @FXML
    private Button btndownload;
    @FXML
    private Button backbtn;
    @FXML
    private ListView<String> mylogs;

    private static List strHolder = new ArrayList();

    public void test1() {

        MCDownloadVersionList list = new MCDownloadVersionList();
        list.addObserver(new IObserver<String>() {
            @Override
            public void onUpdate(IObservable<String> observable, String changed) {
                System.out.println(changed);

                String nameX = changed;
                cmbox.getItems().addAll(nameX);

            }
        });
        try {
            list.startDownload();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // get list here...

        MCLauncherAPI.log.addHandler(new Handler() {
            @Override
            public void publish(LogRecord logRecord) {
                //strHolder.add(logRecord.getLevel() + ":");
                //strHolder.add(logRecord.getSourceClassName() + ":");
                //strHolder.add(logRecord.getSourceMethodName() + ":");
                //strHolder.add("<" + logRecord.getMessage() + ">");
                //strHolder.add("\n");
                //jLabel3.setText(logRecord.getMessage());

                ObservableList<String> items = FXCollections.observableArrayList(logRecord.getMessage());
                ObservableList<String> itemsX = FXCollections.observableArrayList(mylogs.getItems());
                items.addAll(itemsX);

                mylogs.setItems(items);

            }

            @Override
            public void flush() {
            }

            @Override
            public void close() {
            }
        });

        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                test1(); //version list (FULL LIST)

                return null;
            }
        });
        executor.shutdown();

    }

    ListView<String> list = new ListView<String>();

    @FXML
    private void handledwnBtnAction(ActionEvent event) {
        /*
        ObservableList<String> items =FXCollections.observableArrayList (Double.toString(Math.random()));
        ObservableList<String> itemsX =FXCollections.observableArrayList (mylogs.getItems());
        items.addAll(itemsX);
        
        mylogs.setItems(items);
         */
        //process download here...
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                test3(); //download latest (IF NO VERSION GIVEN..)
                //test4(); //installing...

                ObservableList<String> items = FXCollections.observableArrayList("DONE!");
                ObservableList<String> itemsX = FXCollections.observableArrayList(mylogs.getItems());
                items.addAll(itemsX);

                mylogs.setItems(items);
                
                //create file here...
                MakeFile();
                
                return null;
            }
        }
        );
        executor.shutdown();

    }

    public void MakeFile() {
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        System.out.print(workingDirectory);
        String myDir = workingDirectory.toString() + "/TagCraftMC_Files/" + (String) cmbox.getValue();

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(myDir, "UTF-8");
            writer.println("File Downloaded");
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main_FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main_FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }

    }
    
    
    @FXML
    private void handleBackBtnAction(ActionEvent event) {
    }

    public void test3() {
        final MCDownloadVersionList list = new MCDownloadVersionList();
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        //final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));
        final MinecraftInstance mc = new MinecraftInstance(new File(workingDirectory.toString()));

        list.addObserver(new IObserver<String>() {
            private boolean installed = false;

            @Override
            public void onUpdate(IObservable<String> observable, String id) {
                id = (String) cmbox.getValue();

                IVersion changed = null;
                try {
                    changed = list.retrieveVersionInfo(id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Version " + changed.getId());
                if (installed) {
                    return;
                }
                installed = true;
                System.out.println("Installing " + changed.getDisplayName());
                try {
                    changed.getInstaller().install(changed, mc, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    //fail();
                }
            }
        });
        try {

            list.startDownload();

        } catch (Exception e) {
            e.printStackTrace();
            //fail();

        }
    }

}
