/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DownloadPkg;

import MainPkg.Main_FXMLDocumentController;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;

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
    @FXML
    private ProgressIndicator progressing;
    @FXML
    private ProgressIndicator progressing_done;

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
                
                progressing_done.setVisible(false);
                progressing.setVisible(true);
                
                startzDownload(); //download latest (IF NO VERSION GIVEN..)
                CreateRES_RunMineCraft(); //this should fix the issue with files 
                //test4(); //installing...

                /*ObservableList<String> items = FXCollections.observableArrayList("DONE!");
                ObservableList<String> itemsX = FXCollections.observableArrayList(mylogs.getItems());
                items.addAll(itemsX);
                mylogs.setItems(items);
                */
                
                //create file here...
                MakeFile();
                progressing.setVisible(false);
                progressing_done.setVisible(true);
                
                return null;
            }
        }
        );
        executor.shutdown();

    }

    
    
    
    public void CreateRES_RunMineCraft() {

        try {
            // finally use my minecraft credentials
            System.out.println("Logging in...");

            //set a false login service...
            YDLoginService service = new YDLoginService();
            service.load(Platform.getCurrentPlatform().getWorkingDirectory());
            YDProfileIO profileIO = new YDProfileIO(Platform.getCurrentPlatform().getWorkingDirectory());

            IProfile[] profiles = profileIO.read();
            final ISession session = service.login(profiles[0]);

            session.setUsername("NULL");
            session.setSessionID("NULL");
            session.setUUID("NULL");

            //profileIO.write(profiles);
            System.out.println("Success! Launching...");

            File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
            System.out.print(workingDirectory);

            //final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));
            final MinecraftInstance mc = new MinecraftInstance(new File(workingDirectory.toString()));

            final MCDownloadVersionList versionList = new MCDownloadVersionList();
            versionList.addObserver(new IObserver<String>() {

                private boolean launched = false;

                @Override
                public void onUpdate(IObservable<String> observable,
                        String id) {
                    id = (String) cmbox.getValue();
                    IVersion changed = null;
                    try {
                        changed = versionList.retrieveVersionInfo(id);
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                    if (!launched) {
                        launched = true;
                        try {
                            List<String> launchCommand = changed.getLauncher()
                                    .getLaunchCommand(session, mc, null,
                                            changed, new ILaunchSettings() {

                                        @Override
                                        public boolean isModifyAppletOptions() {
                                            return false;
                                        }

                                        @Override
                                        public File getJavaLocation() {
                                            return null;
                                        }

                                        @Override
                                        public List<String> getJavaArguments() {
                                            return Arrays
                                                    .asList("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_ammar.exe_javaw.exe_minecraft.exe.heapdump",
                                                            "-XX:+UseConcMarkSweepGC",
                                                            "-XX:+CMSIncrementalMode",
                                                            "-XX:-UseAdaptiveSizePolicy",
                                                            "-Xmn128M");
                                        }

                                        @Override
                                        public String getInitHeap() {
                                            return "512M";
                                        }

                                        @Override
                                        public String getHeap() {
                                            return "1G";
                                        }

                                        @Override
                                        public Map<String, String> getCustomParameters() {
                                            return null;
                                        }

                                        @Override
                                        public List<String> getCommandPrefix() {
                                            return null;
                                        }
                                    }, null);
                            launchCommand.stream().forEach((cmd) -> {
                                System.out.print(cmd + " ");
                            });
                            System.out.println();
                            ProcessBuilder pb = new ProcessBuilder(
                                    launchCommand);
                            pb.redirectError(new File("mcerr.log"));
                            pb.redirectOutput(new File("mcout.log"));
                            pb.directory(mc.getLocation());
                            
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                    }
                }
            });
            versionList.startDownload();

        } catch (Exception e) {
            //  e.printStackTrace();
        }
    }

    protected boolean isProcessAlive(Process proc) {
        try {
            System.out.println("Process exited with error code:"
                    + proc.exitValue());
            return false;
        } catch (Exception e) {
            return true;
        }

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
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }

    public void startzDownload() {
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
