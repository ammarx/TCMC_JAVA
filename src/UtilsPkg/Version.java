/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UtilsPkg;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author ammar
 */
public class Version {
    
    private String Versions;
    
    public Version() {
        //load version number here...
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);
            setVersionNumber(prop.getProperty("version"));
            
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
    
    public String getVersionNumber() {
        return Versions;
    }
    
    public void setVersionNumber(String Versions) {
        this.Versions = Versions;
    }
    
}
