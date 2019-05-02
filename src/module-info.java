/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module org.praia {
    
    /**
     * los paquetes de java fx ya no hacen parte del jdk.
     * 
     */
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    
    requires jxl;
    
    requires java.desktop;
    requires java.prefs;
    
    exports org.praia;
}
