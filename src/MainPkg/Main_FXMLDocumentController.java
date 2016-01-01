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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
        //txt.setText("HELLO");
        launchminecraftyo();

    }
  
    
    
    public void RunMineCraft() {

	try {
	    // finally use my minecraft credentials
            
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
            
            profileIO.write(profiles);
            
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
				if (line != null && line.length() > 0)
				    System.out.println(line);
			    }
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

    
    public void launchminecraftyo(){
        savesettings();
        btn.setDisable(true);
        txt.setDisable(true);
        cmbox.setDisable(true);
        
        int WhatDoIReturn = -1;
        
        WhatDoIReturn = BeforeDownload();
        if (WhatDoIReturn == 1){
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

        } else if (WhatDoIReturn == 0){
            ExecutorService executor = Executors.newCachedThreadPool();
            executor.submit(() -> {
                //test3(); //download latest (IF NO VERSION GIVEN..)
                //test4(); //installing...
                RunMineCraft();  //run minecraft

                return null;
            });
            executor.shutdown();

        } else if (WhatDoIReturn == -1){
            //something went wong...
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("TagCraftMC API");
            alert.setHeaderText("Version not installed!");
            alert.setContentText("Seems like the version is not installed using our new system.\nThe Launcher will now install it for you..");

            alert.showAndWait();
        }
        
        
    }

    
    public int BeforeDownload(){
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
                System.out.println("Version "+changed.getId());
                if(installed) return;
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
    
     public void savesettings(){
            Properties prop = new Properties();
	OutputStream output = null;

	try {

		output = new FileOutputStream("config.properties");

		// set the properties value
		prop.setProperty("username", txt.getText());
		prop.setProperty("selectedversion", (String) cmbox.getValue());
		
		// save properties to project root folder
		prop.store(output, null);

	} catch (IOException io) {
		io.printStackTrace();
	} finally {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
        }
    
        
        public void loadsettings(){
            
            Properties prop = new Properties();
	InputStream input = null;

	try {

		input = new FileInputStream("config.properties");

		// load a properties file
		prop.load(input);

		// get the property value and print it out
		//System.out.println(prop.getProperty("database"));
		//System.out.println(prop.getProperty("dbuser"));
		//System.out.println(prop.getProperty("dbpassword"));

                txt.setText(prop.getProperty("username"));
		cmbox.setValue(prop.getProperty("selectedversion"));
	} catch (IOException ex) {
		ex.printStackTrace();
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
        }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.print("TEST");
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
