/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPkg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;
import UtilsPkg.Version;
import UtilsPkg.Settings;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

/**
 *
 * @author ammar
 */
public class Main_FXMLDocumentController implements Initializable {

    @FXML
    private Button btndownload;
    @FXML
    private Button exitbtn;
    @FXML
    private TextField txt;
    @FXML
    private Button btn;
    @FXML
    private ComboBox cmbox;
    @FXML
    private WebView browserX;

    public void make_server_resource_packs() {
        //make a new folder here if it doesnt exist.
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        String myDir = workingDirectory.toString() + "/server-resource-packs";

        File file = new File(myDir);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }

    public void listFolders(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();

        for (File file : fList) {
            if (file.isDirectory()) {
                System.out.println(file.getName());
                String nameX = file.getName();
                cmbox.getItems().addAll(nameX);

            }
        }

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        String WhatDoesTheComboBoxHaz = (String) cmbox.getValue();
        
        //do bit of error handling here...
        if (txt.getText().isEmpty()) {
            //show errorbox
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TagCraftMC API");
            alert.setHeaderText("Username empty!");
            alert.setContentText("Please fill in the username");

            alert.showAndWait();
            
        } else if (WhatDoesTheComboBoxHaz == null){
            //show errorbox
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TagCraftMC API");
            alert.setHeaderText("Version empty!");
            alert.setContentText("Please select version to launch");

            alert.showAndWait();
        } else {
            
            //txt.setText("HELLO");
            launchminecraftyo();

        }
    }

    public void RecreateFakeProfile() {
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        String myDir = workingDirectory.toString() + "/launcher_profiles.json";
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(myDir, "UTF-8");
            writer.println("{\n"
                    + "  \"profiles\": {\n"
                    + "    \"Ammar_Ahmad\": {\n"
                    + "      \"name\": \"Ammar_Ahmad\"\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"selectedProfile\": \"Ammar_Ahmad\",\n"
                    + "  \"clientToken\": \"5cf1009c-2be7-437f-9966-9f382ac82e2c\",\n"
                    + "  \"authenticationDatabase\": {\n"
                    + "    \"4db1fbf430f344498dea7663e108a1d2\": {\n"
                    + "      \"displayName\": \"Ammar_Ahmad\",\n"
                    + "      \"userProperties\": [\n"
                    + "        {\n"
                    + "          \"name\": \"twitch_access_token\",\n"
                    + "          \"value\": \"emoitqdugw2h8un7psy3uo84uwb8raq\"\n"
                    + "        }\n"
                    + "      ],\n"
                    + "      \"accessToken\": \"065d7ddad492417ebf7998b029734907\",\n"
                    + "      \"userid\": \"793addf968294e04aac2023db794f0b5\",\n"
                    + "      \"uuid\": \"4db1fbf4-30f3-4449-8dea-7663e108a1d2\",\n"
                    + "      \"username\": \"ammar.ahmad1993@gmail.com\"\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"selectedUser\": \"4db1fbf430f344498dea7663e108a1d2\",\n"
                    + "  \"launcherVersion\": {\n"
                    + "    \"name\": \"1.6.44\",\n"
                    + "    \"format\": 17\n"
                    + "  }\n"
                    + "}");
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main_FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Main_FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }

    public void RunMineCraft_OLD() {
        //method not used. Test method.
        try {
            // finally use my minecraft credentials
            System.out.println("Generating fake profile...");
            RecreateFakeProfile();
            System.out.println("Logging in...");

            //set a false login service...
            YDLoginService service = new YDLoginService();
            service.load(Platform.getCurrentPlatform().getWorkingDirectory());
            YDProfileIO profileIO = new YDProfileIO(Platform.getCurrentPlatform().getWorkingDirectory());

            IProfile[] profiles = profileIO.read();
            final ISession session = service.login(profiles[0]);

            session.setUsername(txt.getText());
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
                            Process proc = pb.start();
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(proc.getInputStream()));
                            String line;
                            while (isProcessAlive(proc)) {

                                //lets just exit it here...
                                System.exit(0);

                                line = br.readLine();
                                if (line != null && line.length() > 0) {
                                    System.out.println(line);
                                }
                            }
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                    }
                }
            });
            
            if (isInternetReachable() == true){
                
                
                 File workingDirectoryX = Platform.getCurrentPlatform().getWorkingDirectory();
                 String myFile = workingDirectoryX.toString() + "/TagCraftMC_Files/versions.json";

                 File fx = new File(myFile);
                 downloadFileFromURL("http://s3.amazonaws.com/Minecraft.Download/versions/versions.json", fx);
                 versionList.startDownload();
 
                 //just download the list to system here..
                
            } else {
                
                versionList.startOfflineDownload();
                //System.out.println("MainPkg.Main_FXMLDocumentController.RunMineCraft()");
                //start offline download here...
                
            }
        } catch (Exception e) {
            //  e.printStackTrace();
        }
    }
    
    public void RunMineCraft(){
        try {
            
            // finally use my minecraft credentials
            System.out.println("Generating fake profile...");
            RecreateFakeProfile();
            System.out.println("Logging in...");
            YDLoginService service = new YDLoginService();
            service.load(Platform.getCurrentPlatform().getWorkingDirectory());
            YDProfileIO profileIO = new YDProfileIO(Platform
                    .getCurrentPlatform().getWorkingDirectory());
            IProfile[] profiles = profileIO.read();
        
            final ISession session = service.login(profiles[0]);
            session.setUsername(txt.getText());
            session.setSessionID("NULL");
            session.setUUID("NULL");

            //profileIO.write(profiles);
            System.out.println("Success! Launching...");
             File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
            System.out.print(workingDirectory);

            //final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));
            final MinecraftInstance mc = new MinecraftInstance(new File(workingDirectory.toString()));

            final MCDownloadVersionList versionList = new MCDownloadVersionList();
            String idX = (String) cmbox.getValue();
            IVersion changed = null;
            try {
                
                //do editing here for offline mode..
                changed = versionList.retrieveVersionInfo(idX);
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                                                .asList("-XX:+UseConcMarkSweepGC",
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
                for (String cmd : launchCommand) {
                    System.out.print(cmd + " ");
                }
                System.out.println();
                ProcessBuilder pb = new ProcessBuilder(
                        launchCommand);
                pb.redirectError(new File("mcerr.log"));
                pb.redirectOutput(new File("mcout.log"));
                pb.directory(mc.getLocation());
                Process proc = pb.start();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(proc.getInputStream()));
                String line;
                while (isProcessAlive(proc)) {
                    line = br.readLine();
                    if (line != null && line.length() > 0)
                        System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void downloadFileFromURL(String urlString, File destination) {    
        try {
            URL website = new URL(urlString);
            ReadableByteChannel rbc;
            rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(destination);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean isInternetReachable()
        {
            try {
                //make a URL to a known source
                URL url = new URL("http://s3.amazonaws.com/");

                //open a connection to that source
                HttpURLConnection urlConnect = (HttpURLConnection)url.openConnection();

                //trying to retrieve data from the source. If there
                //is no connection, this line will fail
                Object objData = urlConnect.getContent();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                return false;
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                return false;
            }
        
            return true;
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

    public void launchminecraftyo() {
        savesettings();
        btn.setDisable(true);
        txt.setDisable(true);
        cmbox.setDisable(true);

        int WhatDoIReturn = -1;

        WhatDoIReturn = BeforeDownload();
        if (WhatDoIReturn == 1) {
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(() -> {
                //test3(); //download latest (IF NO VERSION GIVEN..)
                //test4(); //installing...

                DownloadMineCraft();
                MakeFile();
                RunMineCraft();  //run minecraft

                return null;
            });
            executor.shutdown();

        } else if (WhatDoIReturn == 0) {
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(() -> {
                //test3(); //download latest (IF NO VERSION GIVEN..)
                //test4(); //installing...
                RunMineCraft();  //run minecraft

                return null;
            });
            executor.shutdown();

        } else if (WhatDoIReturn == -1) {
            //something went wong...
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TagCraftMC API");
            alert.setHeaderText("Version not installed!");
            alert.setContentText("Seems like the version is not installed using our new system.\nThe Launcher will now install it for you..");

            alert.showAndWait();
        }

    }

    public int BeforeDownload() {
        //download minecraft if something is files unavailable...
        //concept:
        //we make tagcraftmc_files folder. in that folder keep txt file
        //if txt available then
        //file downloaded
        //else
        //download file

        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        System.out.print(workingDirectory);
        String myDir = workingDirectory.toString() + "/TagCraftMC_Files";

        File f = new File(myDir);
        if (f.isDirectory()) {
            //if directory available.. check for file...
            File myfileX = new File(myDir + "/" + (String) cmbox.getValue());
            if (myfileX.isFile()) {
                //file is available.. no need to download.
                return 0;
            } else {
                //show messagebox.. and start downloading...
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("TagCraftMC API");
                alert.setHeaderText("Version not installed!");
                alert.setContentText("Seems like the version is not installed using our new system.\nThe Launcher will now install it for you..");

                alert.showAndWait();
                return 1;
            }

        } else {
            //make directory.. no need to check for file bc file wont be available...
            new File(myDir).mkdir();
            //if directory made.. show message.. and star downloading..
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TagCraftMC API");
            alert.setHeaderText("Version not installed!");
            alert.setContentText("Seems like the version is not installed using our new system. The Launcher will now install it for you..");

            alert.showAndWait();
            return 1;
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

    public void DownloadMineCraft() {

        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        System.out.print(workingDirectory);

        final MCDownloadVersionList list = new MCDownloadVersionList();
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

    public void savesettings() {
        Settings settings = new Settings(txt.getText(), (String) cmbox.getValue());
        settings.SaveSettings();
    }

    public void loadsettings() {
        Settings settings = new Settings();
        settings.LoadSettings();
        txt.setText(settings.getusername());
        cmbox.setValue(settings.getselectedversion());
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //WebView  browserX = new WebView();
        WebEngine engine = browserX.getEngine();
        String urlx = "http://mcupdate.tumblr.com/";
        
        engine.load(urlx);

        make_server_resource_packs();
        //System.out.print("TEST");
        final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));

        System.out.print(mc.getLocation().toString());

        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        System.out.print(workingDirectory);
        listFolders(workingDirectory + "/versions/");
        //listFolders(mc.getLocation().toString() + "/versions/");

        loadsettings();

    }

    @FXML
    private void handledwnBtnAction(ActionEvent event) {
        //open a new form here...
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/DownloadPkg/Download_FXMLDocument.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Download");
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void handleextBtnAction(ActionEvent event) {
        System.exit(0);
    }

}
