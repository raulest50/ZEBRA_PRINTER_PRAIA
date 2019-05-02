
package org.praia;


/**
 * La transicion de java 8 a java 9 cambia radicalmente la forma en que
 * se hacen las aplicaciones.
 * 
 * se debe crear un archivo llamado module info donde se agregan los
 * requirements que especifican que modulos de java se van a usar.
 * 
 * en las propiedades del proyecto se debe agregar en la seccion de librerias
 * los jars de las apis externas y luego en el module info agregar los 
 * respectivos requirements.
 * 
 * tambien se debe poner el main en un paquete cuyo nombre es el mismo del
 * module info que en este caso es org.praia
 * 
 * al final del module se debe colocar un exports
 * 
 * las api tambien cambiaron de lugar. javax.print.. por ejemplo quedo 
 * en el modulo java.desktop
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author esteban
 */
public class PraiaMain extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
