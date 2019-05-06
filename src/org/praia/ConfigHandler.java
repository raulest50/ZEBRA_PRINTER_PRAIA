package org.praia;


import java.util.prefs.Preferences;




/**
 *
 * @author esteban
 * 
 * Clase para gestionar las preferencias de java de la aplicacion.
 */
public class ConfigHandler {
    
    /**
     * key para el texto deseado para anteceder el campo de ubicacion
     */
    public final String UB_STR_FIELD = "preferencias_java_praia_ub_tf";
    
    /**
     * key para la columna de descripcion.
     */
    public final String COL_DESC = "preferencias_java_praia_gap_num_col_descri";
    
    
    /**
     * para guardar el numero de columna del tipo de producto (juego, accesorio etc)
     */
    public final String COL_TIPO = "preferencias_java_praia_tipo_accesorio";
    
    
    /**
     * nombre key para guardar numero de columna en excel del precio de venta pormayor
     */
    public final String COL_MAYOR = "preferencias_java_praia_precio_mayorista";
    
    
    /**
     * la posicion inicial en x para todos los elementos del label
     */
    public final String CONFV = "preferencias_java_praia_conf_vector";
    
    
    /**
     * key para la columna del codigo
     */
    public final String COL_COD = "preferencias_java_praia_num_col_cod";
    
    /**
     * key para el alto del label
     */
    public final String COL_UBIQ = "preferencias_java_praia_num_col_ubiq";
    
    /**
     * key para la columna de cantidad
     */
    public final String COL_CANT = "preferencias_java_praia_num_col_cant";
    
    /**
     * nombre de la impresora de codigos de barras, ver escaneres e impresoras
     */
    public final String PRINTER = "preferencias_java_praia_printer";
    
    
    /**
     * objeto de la api de preferencias que se usa para guardar y cargar configuraciones.
     */
    public Preferences pref = Preferences.userRoot().node(this.getClass().getName());
    
    
    public ConfigHandler(){}
    
    /**
     * para guardar una configuracion tipo String
     * @param Key
     * @param valor 
     */
    public void SaveConfig_Str(String Key, String valor){
        pref.put(Key, valor);
    }
    
    /**
     * Para guardar una configuracion tipo entero
     * @param Key
     * @param valor 
     */
    public void SaveConfig_Int(String Key, int valor){
        String str_val = Integer.toString(valor);
        pref.put(Key, str_val);
    }
    
    /**
     * para cargar una configuracion tipo String
     * @param Key
     * @return 
     */
    public String LoadConfig_Str(String Key){
        String r = "";
        r = pref.get(Key, "");
        return r;
    }
    
    /**
     * para cargar una configuracion tipo entero.
     * @param Key
     * @return 
     */
    public double LoadConfig_Int(String Key){
        double r = 0;
        String aux = pref.get(Key, "0");
        r = Integer.parseInt(aux);
        return r;
    }
    
}
