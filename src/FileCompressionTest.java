/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compressor;
import java.util.*;
import java.io.*;
/**
 *
 * @author Kuraara
 */
public class FileCompressionTest
{
    
    public static String sourceDir    = "C:\\Compressor\\CompressorTestFiles\\Files\\";
    public static String compressed   = "C:\\Compressor\\CompressorTestFiles\\Compressed\\";
    public static String decompressed = "C:\\Compressor\\CompressorTestFiles\\Decompressed\\";
    
    
    //public static String sourceDir  = "~/Compressor/CompressorTestFiles/files";
    //public static String compressed  = "~/Compressor/CompressorTestFiles/compressed";
    //public static String decompressed  = "~/Compressor/CompressorTestFiles/decompressed";
    
    public static void compress()
    {
        try
        {

            File dir            = new File(sourceDir);
            
            
            File[] files = dir.listFiles();
            LZWImp lzw = new LZWImp(true);
            String info;
            
            FileWriter log = new FileWriter("C:\\DataStructuresLabs\\CompressorTestFiles\\compressionlog.txt");
            log.write("Filename, BytesRead, BytesWritten, Efficiency, TimeTake(ms), DecodeMapSize\n");
            System.out.print("Filename, BytesRead, BytesWritten, Efficiency, TimeTake(ms), DecodeMapSize\n");
            for( File f : files)
            {
                if( f.isFile() )
                {
                    //System.out.println("Attempting to compress: " + f.getName());
                    info = f.getName() + ", ";
                    info += lzw.compress(new FileInputStream(sourceDir + f.getName()), new FileOutputStream(compressed + f.getName() + ".z"));
                    System.out.print(info);
                    log.write(info);
                }
            }
            log.close();
            
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void decompress()
    {
     try
        {
            File dir            = new File(compressed);
            
            File[] files = dir.listFiles();
            LZWImp lzw = new LZWImp(true);
            String info;
            
            FileWriter log = new FileWriter("C:\\DataStructuresLabs\\CompressorTestFiles\\decompressionlog.txt");

            log.write("Filename, BytesRead, BytesWritten, Efficiency, TimeTaken, FileComparison\n");
            System.out.print("Filename, BytesRead, BytesWritten, Efficiency, TimeTake, FileComparison\n");
            for( File f : files)
            {
                if( f.isFile() )
                {
                    try
                    {
                        
                        //System.out.println("Attempting to decompress: " + f.getName());
                        info = f.getName() + ", ";
                        
                        info += lzw.decompress(new FileInputStream(compressed + f.getName()), new FileOutputStream(decompressed + f.getName().substring(0, f.getName().length()-2)));
                        info += String.format(", %d\n", f.compareTo( new File(sourceDir+f.getName()) ));
                        System.out.print(info);
                        log.write(info);
                        
                        
                    }
                    catch(Exception ex)
                    {
                      ex.printStackTrace();
                      log.write(ex.getMessage());
                    }
                    
                }
                
            }
            log.close();
            
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }   
    }
    
    private static void compare()
    {
        try
        {

            File dir            = new File(decompressed);
            
            
            File[] files = dir.listFiles();
            LZWImp lzw = new LZWImp(true);
            String info;
            
            for( File f : files)
            {
                if( f.isFile() )
                {
                    
                    File b = new File(sourceDir+f.getName());
                    
                    FileInputStream x = new FileInputStream(f);
                    FileInputStream y = new FileInputStream(b);
                    
                    int u;
                    int v;
                    int n =0;
                    boolean result = true;
                    while( (u = x.read()) != -1 && (v = y.read()) != -1)
                    {
                        n++;
                        if( u == v )
                        {
                            
                        }
                        else
                        {
                            System.out.print("\n"+n+" "+(u-v)+" fail\n");
                            result = !result;
                            break;
                        }
                    }
                    if( result )
                    {
                        System.out.println("Result: OK" );
                    }
                    else
                    {
                        System.out.println("Result: Fail" );
                    }
                    x.close();
                    y.close();
                    
                }
            }
            //log.close();
            
            
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }        
    }
    
    private static void setDir()
    {
        String os = System.getProperty("os.name").toLowerCase();
        
        if( os.substring(0, 6) == "windows" )
        {
            
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {
        /*
         * rather large file test....
        FileWriter x = new FileWriter(sourceDir+"aaaa.txt", false);
        
        for( int i = 0 ; i < (1 << 30)-1; i++)
        {
            x.write((int)'a');
        }
        */
        System.out.println("Totally sweet rockin LZW Compressor Implementation");
        System.out.println("Running Compression Test");
        compress();
        System.out.println("Running decompression Test");
        decompress();
        System.out.println("Running comparrison test");
        compare();
    }
}
