package org.praia;



// importantisimo, requiere java.desktop
import java.util.ArrayList;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

/**
 *
 * @author esteban
 * 
 * 
 * clase que implementa toda la logica necesaria para llevar a cabo una impresion.
 */
public class Printer {
    
    // cadena de base para agregar cada uno de los comandos ZPL
    // cada envio debe tener XA para inicial y XZ para finalizar
    public static String BASE = "^XA$^XZ";
    
    
    
    public Printer(){
        
    }
    
    
    /**
     * metodo que recibe una serie de comandos de ZPL y los envia a la 
     * impresora.no requiere que este habilitado ZBI
     * @param CmdArray 
     * @param printer 
     * @throws javax.print.PrintException 
     */
    public void SendBytes2Printer(String CmdArray, String printer) throws PrintException{
        PrintService pservice = GetPrinterService(printer); // acquire print service of your printer
        DocPrintJob job = pservice.createPrintJob();  
        String commands = CmdArray;
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        Doc doc = new SimpleDoc(commands.getBytes(), flavor, null);
        job.print(doc, null);
    }
    // "^XA\n\r^MNM\n\r^FO050,50\n\r^B8N,100,Y,N\n\r^FD1234567\n\r^FS\n\r^PQ3\n\r^XZ"
    
    public PrintService GetPrinterService(String printer_name) 
            throws RuntimeException{
        
        // se obtienen todos los servicios de impresion.
        PrintService ps[] = PrintServiceLookup.lookupPrintServices(null, null);
        
        PrintService printer = null;
        
        // se obtiene como PrintService la impresora tmu220pd epson
        for (PrintService p : ps){
            if(p.getName().equals(printer_name)) printer = p;
        }
        
        if(printer == null) throw new RuntimeException("No printer services available.");
        
        return printer;
    }
    
    
    /**
     * metodo que recibe un precio y lo transforma a su equivalente en letras
     * 
     * se trata de una funcionalidad para que los trabajadores puedan saber
     * el precio de un articulo por medio de la etiqueta pero que al
     * mimo tiempo los clientes no sepan.
     * @param val
     * @return 
     */
    public String Coding(String val){
        String r = "";
        
        for (int k=0; k<= val.length()-1 ; k++){
            
            switch(val.substring(k, k+1)){
                
                case "0":
                    r = r.concat("D");
                    break;
                
                case "1":
                    r = r.concat("N");
                    break;
                    
                case "2":
                    r = r.concat("O");
                    break;
                    
                case "3":
                    r = r.concat("B");
                    break;
                    
                case "4":
                    r = r.concat("E");
                    break;
                    
                case "5":
                    r = r.concat("L");
                    break;
                    
                case "6":
                    r = r.concat("S");
                    break;
                    
                case "7":
                    r = r.concat("A");
                    break;
                    
                case "8":
                    r = r.concat("U");
                    break;
                    
                case "9":
                    r = r.concat("M");
                    break;
                    
                default:       
            }
        }
        
        return r;
    }
    
    
    /**
     * despues de la migracion de java9 a java11 se delego toda la logica
     * de impresion a este metodo.Este es responsable de tomar la informacion
 que debe ir en el sticker en forma string, de cargar los parametros
 de configuracion y con ambos formar la correspondiente cadena
 de comandos ZPL, que finalmente se envia al metodo que convierte
 string a bytes y los manda a un servicio de impresion especificado
 por el nombre.
     * @param cod
     * @param desc
     * @param ubiq
     * @param cant
     * @param tipo
     * @param mayor
     * @param test
     * @throws PrintException 
     */
    public void Imprimir 
        (String cod, String desc, String ubiq, String cant, String tipo, String mayor, boolean test) 
                throws PrintException
        {
        
        ConfigHandler cfg = new ConfigHandler();
        
        // el precio mayorista se pasa a su equivalente en letras
        String mayor_letras = this.Coding(mayor);
        
        // se carga el nombre de la impresora
        String PrinterName = cfg.LoadConfig_Str(cfg.PRINTER);
        
        // los parametros de cada comando ZPL se separan por comas
        // y la separacion entre grupo de parametros se hace por "-"
        // en la siguiente linea de codigo se hace un split a cada grupo de parametros
        String[] P = cfg.LoadConfig_Str(cfg.CONFV).split("-");
        
        // cada lista representa un grupo de parametros para un comando ZPL
        String[] Pbarcode, Ptx_barcode, Pnombre, Pmayor, Pubiq;
        
        Pbarcode = P[0].split(","); // parametros del codigo de barras
        Ptx_barcode = P[1].split(","); // del texto del cod barras
        Pnombre = P[2].split(","); // descripcion
        Pmayor = P[3].split(","); // precio pormayor
        Pubiq = P[4].split(","); // de la ubicacion
        
        String commds = "";
        
        commds = this.AgregarCodBarras(commds,Pbarcode, cod);
        commds = this.AgregarTexto(commds,Ptx_barcode, cod);
        commds = this.AgregarTexto(commds,Pnombre, desc + " - " + tipo );
        commds = this.AgregarTexto(commds,Pmayor, mayor_letras);
        commds = this.AgregarTexto(commds,Pubiq, ubiq);
        
        // se agrega el numero de copias
        commds = commds.concat("^PQ" + cant + ",0,0,N");
        
        String fi = BASE.replace("$", commds);
        
        if(test) System.out.println(fi);
        else this.SendBytes2Printer(fi, PrinterName);
        
    }
    
    
    /**
     * retorna la cadena de comandos ZPL para agregar un codigo de barras
     * @param in
     * @param p
     * @param CODTK
     * @return 
     */
    public String AgregarCodBarras(String in, String[] p, String CODTK)
        throws IndexOutOfBoundsException
    {
        String r = "^FO$x,$y^BY$p1^BCN,$p2,N,N,N^FD" + CODTK + "^FS";
        r = r.replace("$x", p[0]);// coordenadas xy
        r = r.replace("$y", p[1]);
        r = r.replace("$p1", p[2]); // valor reset default bar code
        r = r.replace("$p2", p[3]); // alto del barcode
        return in.concat(r);
    }
    
    /**
     * agrega la cadena de comandos ZPL para imprimir un texto
     * @param in
     * @param p
     * @param TXT
     * @return 
     */
    public String AgregarTexto(String in, String[] p, String TXT)
        throws IndexOutOfBoundsException
    {
        String r = "^FO$x,$y^A$p1,N,$p2,$p3^FD" + TXT + "^FS";
        r = r.replace("$x", p[0]);// coordenadas xy
        r = r.replace("$y", p[1]);
        
        r = r.replace("$p1", p[2]);// font type
        r = r.replace("$p2", p[3]);// alto
        r = r.replace("$p3", p[4]);// ancho
        return in.concat(r);
    }
    
}
