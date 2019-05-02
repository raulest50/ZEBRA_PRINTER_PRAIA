package org.praia;



// importantisimo, requiere java.desktop
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
    
    
    
    public Printer(){
        
    }
    
    
    /**
     * metodo que recibe una serie de comandos de ZPL y los envia a la 
     * impresora.no requiere que este habilitado ZBI
     * @param CmdArray 
     * @param printer 
     * @throws javax.print.PrintException 
     */
    public void Imprimir(String CmdArray, String printer) throws PrintException{
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
    
}
