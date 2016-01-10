/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UtilsPkg;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author ammar
 */
public class Settings {
    
    private String selectedversion;
    private String username;
    private String version;
    
    public Settings() {
        //user property to set settings to null.
        
    }
    
    public String getselectedversion(){
        return selectedversion;
    }
    
    public String getusername(){
        return username;
    }
    
    public String getversion(){
        return version;
    }
    
    public void setselectedversion(String selectedversion){
        this.selectedversion = selectedversion;
    }

    public void setusername(String username){
        this.username = username;
    }
    
    public void setversion(String version){
        this.version = version;
    }
    
    public void RebuildSettings(){
        
        
        Properties prop = new Properties();
        OutputStream output = null;

        try {

            output = new FileOutputStream("config.properties");

            // set the properties value
            prop.setProperty("username", username);
            prop.setProperty("selectedversion", selectedversion);
            prop.setProperty("version", version);
            
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
    
}
