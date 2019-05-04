package org.praia;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.print.PrintException;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


/**
 *
 * @author esteban
 */
public class FXMLDocumentController implements Initializable {
    
    
    /**
     * campos de texto para ingresar los datos del codigo de barras que se
     * desea imprimir
     */
    @FXML
    public TextField TFDescripcion;

    @FXML
    public TextField TFCodigo;

    @FXML
    public TextField TFUbicacion;

    @FXML
    public TextField TFCopias;
    
    /**
     * campos de texto agregados despues de migrar a java 11
     */
    @FXML
    public TextField TFTipo; // para indicar si el producto es un juego conjunto etc (peticion cliente)
    
    @FXML
    public TextField TFMayor; // el precio que se pasara a letras 
    // (solo los trabajadores saben correspondencia Digito a letra facilitando negociacion con cliente )

    // para indicar coordenadas de inicio en x para todos los elementos del sticker
    @FXML
    public TextField TFConfV;
      
    
    
    /**
     * boton para imprimir el codigo de barras especificado por los campos
     * de texto de arriba
     */
    @FXML
    public Button Bimprimir;

    /**
     * boton para imprimir los codigos de barras en un archivo de excel
     */
    @FXML
    public Button Bexcel;

    /**
     * campos de texto para configurar la impresion dentro del doc de excel
     */
    @FXML
    public TextField TFdesde;

    @FXML
    public TextField TFhasta;

    @FXML
    public TextField TFcolDescri;

    @FXML
    public TextField TFcolCodigo;

    @FXML
    public TextField TFcolUbicacion;

    @FXML
    public TextField TFcolCant;
    
    @FXML
    public TextField TFcolTipo;
    
    @FXML
    public TextField TFcolMayor;
    
    /**
     * labels para notificar si la configuracion quedo guardada.
     */
    @FXML
    public Label LabDescri;
            
    @FXML
    public Label LabCodigo;
    
    @FXML
    public Label LabUbiq;
    
    @FXML
    public Label LabCant;
    
    @FXML
    public Label LabTipo;
    
    @FXML 
    public Label LabMayor;
    
    @FXML
    public TextField TFPrinter;
    
    @FXML
    public Label LabPrinter;
    
    @FXML
    public Label LabConfV;

    
    //check box para habilitar o deshabilitar la edicion de los campos de texto
    // de configuracion (columnas impresoras X e Y)
    @FXML
    public CheckBox CHB_Edit;
    
    //cuando esta activado no se manda al servicio de impresion sino a la consola
    @FXML
    public CheckBox CHB_Test;
    
    /**
     * para indicar estado de configuraciones que se guardan
     */
    public final String MOD = "(??)";
    public final String SAVED = "(ok)";
    
    /**
     * tokens para reemplazar mas facilmente en la plantilla del comando
     * de impresion: SKELETON.
     * se combina junto con el metodo replace.
     */
    public final String CODTK = "$COD";
    public final String NAMETK = "$NOM";
    public final String UBIQTK = "$UBQ";
    public final String COPIASTK = "$CANT";
    
    
    
    public final String SKELETON = "^XA" // inicio de doc
            + "^FO210,30^BY1^BCN,50,N,N,N^FD"+ CODTK +"^FS" // codigo de barras
            + "^FO210,94^A0,N,32,25^FD" + CODTK + "^FS" // codigo de barras en alfanumerico
            + "^FO210,137^A0,N,32,25^FD" + NAMETK + "^FS" // descripcion producto
            + "^FO210,170^A0,N,32,25^FDUbicacion:" + UBIQTK + "^FS" // ubicaicon del producto
            + "^PQ" + COPIASTK + ",0,0,N" // numero de copias
            + "^XZ"; // fin de doc, siempre se deben usar XA y XZ
    
    
    public ConfigHandler cfn_han = new ConfigHandler();
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        this.LoadConf();
        
        this.SetEditableTFS(false);
        
        // listener a cambios del ch box de edicion. determina si los TF de conf se pueden editar
        CHB_Edit.selectedProperty().addListener((observable, oldValue, newValues) ->{
                boolean ed = CHB_Edit.isSelected(); // si esta seleccionado permite edicion
                SetEditableTFS(ed);
            });
        
        /**
         * se configuran los grupos para que se establescan los listener para 
         * cada uno.
         */
        new ConfigTeam(TFcolDescri, LabDescri, cfn_han.COL_DESC);
        new ConfigTeam(TFcolCodigo, LabCodigo, cfn_han.COL_COD);
        new ConfigTeam(TFcolUbicacion, LabUbiq, cfn_han.COL_UBIQ);
        new ConfigTeam(TFcolCant, LabCant, cfn_han.COL_CANT);
        
        new ConfigTeamText(TFPrinter, LabPrinter, cfn_han.PRINTER);
        new ConfigTeamText(TFConfV, LabConfV, cfn_han.CONFV);
        
        new ConfigTeam(TFcolTipo, LabTipo, cfn_han.COL_TIPO);
        new ConfigTeam(TFcolMayor, LabMayor, cfn_han.COL_MAYOR);
        
    }


    public void SetEditableTFS(boolean ed){
        TFcolDescri.setEditable(ed);
                TFcolCodigo.setEditable(ed);
                TFcolTipo.setEditable(ed);
                TFcolMayor.setEditable(ed);
                TFcolUbicacion.setEditable(ed);
                TFcolCant.setEditable(ed);
                    
                TFPrinter.setEditable(ed);
                TFConfV.setEditable(ed);
    }
    
    
    /**
     * cuando se hace enter en los campos de texto para impresion de 
     * codigo manual, se hace request focus del siguiente TextField
     * @param event 
     */
    @FXML
    public void onActionTFDescripcion(ActionEvent event) {
        TFTipo.requestFocus();
    }
    
    @FXML
    public void onActionTFTipo(ActionEvent event){
        TFCodigo.requestFocus();
    }
    
    @FXML
    public void onActionTFCodigo(ActionEvent event) {
        TFMayor.requestFocus();
    }
    
    @FXML
    public void OnActionTFMayor(ActionEvent event){
        TFUbicacion.requestFocus();
    }
    
    @FXML
    public void OnActionTFUbicacion(ActionEvent event) {
        TFCopias.requestFocus();
    }
    
    @FXML
    public void OnActionCopias(ActionEvent event) {
        
    }
    
    
    /**
     * metodo que carga los valores de configuracion en los respectivos TF
     */
    public void LoadConf(){
        TFcolDescri.setText(cfn_han.LoadConfig_Str(cfn_han.COL_DESC));
        TFcolCodigo.setText(cfn_han.LoadConfig_Str(cfn_han.COL_COD));
        TFcolCant.setText(cfn_han.LoadConfig_Str(cfn_han.COL_CANT));
        TFcolUbicacion.setText(cfn_han.LoadConfig_Str(cfn_han.COL_UBIQ));
        TFPrinter.setText(cfn_han.LoadConfig_Str(cfn_han.PRINTER));
        
        TFcolTipo.setText(cfn_han.LoadConfig_Str(cfn_han.COL_CANT));
        TFcolMayor.setText(cfn_han.LoadConfig_Str(cfn_han.COL_MAYOR));
        
        TFConfV.setText(cfn_han.LoadConfig_Str(cfn_han.CONFV));
        
    }
    
    
    @FXML
    public void onClickPrint(MouseEvent evt){
        String desc = TFDescripcion.getText();
        String tipo = TFTipo.getText();
        String cod = TFCodigo.getText();
        String mayorista = TFMayor.getText();
        String ubiq = TFUbicacion.getText();
        String cant = TFCopias.getText();
        
        SendSticker2Printer(cod, desc, ubiq, cant, tipo, mayorista);
    }
    
    
    /**
     * clase que encapsula el conjunto de label textfield y su key string
     * de la api de java preferences.
     * 
     * en el constructor de esta clase se hace inicializacion del text field,
     * se agrega su onChange Listener y su onKeyPress Listener.
     * de esta forma el codigo no se tiene que escribir para cada TextField
     * sino que en el initialize de este DocController solo se invoca el
     * constructor y listo,, simplificando y haciendo mas limpio el codigo.
     */
    private class ConfigTeam{
        TextField campo;
        Label lab;
        String keyConfig;

        public ConfigTeam(TextField campo, Label lab, String keyConfig) {
            this.campo = campo;
            this.lab = lab;
            this.keyConfig = keyConfig;
            
            campo.setText(cfn_han.LoadConfig_Str(keyConfig));
            
            // se ponen ValueChange Listener
            this.campo.textProperty().addListener((observable, oldValue, newValues) ->{
                this.lab.setText(MOD); // se indica con el label que debe guardarse el cambio
            });
            
            // se pone el listener para captar el enter para guardar la configuracion.
            this.campo.setOnKeyPressed((event) -> {
                if(event.getCode().equals(KeyCode.ENTER)){
                    try{
                        Integer.parseInt(campo.getText()); // para validar con NumFormat Exception
                        cfn_han.SaveConfig_Str(keyConfig, campo.getText());
                    } catch (NumberFormatException ex){
                        campo.setText(cfn_han.LoadConfig_Str(keyConfig));
                    } finally{ // con o sin excepcion van a quedar actualizados.
                        lab.setText(SAVED);
                    }
                }
            });
        }
    }
    
    private class ConfigTeamText{
        TextField campo;
        Label lab;
        String keyConfig;

        public ConfigTeamText(TextField campo, Label lab, String keyConfig) {
            this.campo = campo;
            this.lab = lab;
            this.keyConfig = keyConfig;
            
            campo.setText(cfn_han.LoadConfig_Str(keyConfig));
            
            // se ponen ValueChange Listener
            this.campo.textProperty().addListener((observable, oldValue, newValues) ->{
                this.lab.setText(MOD); // se indica con el label que debe guardarse el cambio
            });
            
            // se pone el listener para captar el enter para guardar la configuracion.
            this.campo.setOnKeyPressed((event) -> {
                if(event.getCode().equals(KeyCode.ENTER)){
                    try{
                        cfn_han.SaveConfig_Str(keyConfig, campo.getText());
                    } catch (NumberFormatException ex){
                        campo.setText(cfn_han.LoadConfig_Str(keyConfig));
                    } finally{ // con o sin excepcion van a quedar actualizados.
                        lab.setText(SAVED);
                    }
                }
            });
        }
    }
    
    public void ShowError(String titulo, String subtitulo, String mensaje){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        
        alert.setTitle(titulo);
        alert.setHeaderText(subtitulo);
        alert.setContentText(mensaje);
        
        alert.showAndWait();
        //DialogPane Dpane = alert.getDialogPane();
        //Dpane.getStylesheets().add(VirtualAdministrator.class.getClass().getResource("estilo.css").toExternalForm());
    }
    
    
    /**
     * cuando se da click al boton de generar codigos de barras
     * a partir de archivo de excel entonces se muetra un directory chooser
     * y se procede a barrer las filas especificadas.
     * @param evt 
     */
    @FXML
    public void onClickExcel(MouseEvent evt){
        
        // se abre una ventana de dialogo para seleccionar el archivo de excel 
        // deseado.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Archivo de Excel");
        File selectedFile = fileChooser.showOpenDialog( (Stage) TFDescripcion.getScene().getWindow() );

        if(selectedFile == null){
            //No Directory selected
        }else{// si se selecciono un archivo de excel entonces
            String archivoExcel = selectedFile.getAbsolutePath();
            try{
                if(archivoExcel.contains(".xls")) HacerImpresionArchivoExcel(archivoExcel);
            } catch(BiffException | IOException ex){
                ShowError("ha ocurrido un error", "", "El archivo seleccionado no es valido");
            }
        }
    }
    
    
    /**
     * metodo que se ejecuta para realizar la impresion de un archivo de excel
     * a codigos de barras
     * @param rutaExcel 
     * @throws java.io.IOException 
     * @throws jxl.read.biff.BiffException 
     */
    public void HacerImpresionArchivoExcel(String rutaExcel)
            throws IOException, BiffException{
        
        // instacia de un archivo excel en formato workbook
        // no puede ser .xlsx, debe ser workbook .xls (98-2003)
        Workbook workbook = null;
        // construye el objeto usando la ruta donde esta ubicado el excel
        workbook = Workbook.getWorkbook(new File(rutaExcel));
        
        // se inicia en la hoja 0
        Sheet sheet = workbook.getSheet(0); // se insancia la hoja 0 del archivo de excel
        
        int desde;
        int hasta;
        
        // se leen los valores de configuracion para poder interpretar el excel
        // la fila inicial y final
        try{
            desde = Integer.parseInt(TFdesde.getText());
        } catch(NumberFormatException ex){ // si no se especifican los valores
            desde = 0; // entonces se asume que desde toma valor 0
        }
        try{
            hasta = Integer.parseInt(TFhasta.getText());
        } catch(NumberFormatException ex){
            hasta = sheet.getRows(); // y hasta toma el final del doc si no se especifica
        } 
        
        
        int COL_DESCRI = Integer.parseInt(TFcolDescri.getText())-1;
        int COL_COD = Integer.parseInt(TFcolCodigo.getText())-1;
        int COL_UBIQ = Integer.parseInt(TFcolUbicacion.getText())-1;
        int COL_CANT = Integer.parseInt(TFcolCant.getText())-1;
        
        int COL_TIPO = Integer.parseInt(TFcolTipo.getText())-1;
        int COL_MAYOR = Integer.parseInt(TFcolMayor.getText())-1;
        
        
        // variables auxiliares para guardar la informacion de cada sticker
        // en cada iteracion
        String codigo, desc, ubiq, cant, tipo, mayor;
        
        
        // se barre cada fila del excel
        for(int i=desde-1; i<=hasta-1; i++){
            // para cada fila se extrae la informacion
            codigo = sheet.getCell(COL_COD, i).getContents();
            desc = sheet.getCell(COL_DESCRI, i).getContents();
            ubiq = sheet.getCell(COL_UBIQ, i).getContents();
            cant = sheet.getCell(COL_CANT, i).getContents();
            tipo = sheet.getCell(COL_TIPO, i).getContents();
            mayor = sheet.getCell(COL_MAYOR, i).getContents();
            // se imprime el numero de copias especificadas
            SendSticker2Printer(codigo, desc, ubiq, cant, tipo, mayor);
        }
    }
    
    
    /**
     * metodo que recibe en formato string el codigo, la descripcion, 
     * la ubicacion y el numero de copias y forma el comando para
     * enviar a la impresora.
     * @param cod
     * @param desc
     * @param ubiq
     * @param cant 
     */
    public void SendSticker2Printer
        (String cod, String desc, String ubiq, String cant, String tipo, String mayor)
    {
        
        String Nombre;
        if(desc.length()>15) Nombre = desc.substring(0, 14);
        else Nombre = desc;

        Printer pri = new Printer();
        try {
            pri.Imprimir(cod, Nombre, ubiq, cant, tipo, mayor, this.CHB_Test.isSelected());
        } catch (PrintException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}

