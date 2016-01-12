/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UtilsPkg;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    Version VersionClass = new Version();
 
    public Settings() {
        //user property to set settings to null.
        selectedversion = "";
        username = "";
        //version should be auto
        version = VersionClass.getversion();
        
    }
    
    public Settings(String username,String selectedversion){
        this.selectedversion = selectedversion;
        this.username = username;
        //version should be auto
        version = VersionClass.getversion();
        
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
    
    public void LoadSettings(){
        
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            setusername(prop.getProperty("username"));
            setselectedversion(prop.getProperty("selectedversion"));
            setversion(version);
           
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
    
    public void SaveSettings() {
        //can also be used to reset settings... Use Carefully.
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
