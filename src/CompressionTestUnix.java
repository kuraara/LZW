
package compressor;
import java.util.*;
import java.io.*;
/**
 * Duplicate of the test I was using on my Windows based systems
 * @author Kuraara Forrest 20488223
 */
public class CompressionTestUnix
{
    
    public static String sourceDir  = "/home/uniwa/students/students3/20488223/linux/Compressor20488223/CompressorTestFiles/Files/";
    public static String compressed  = "/home/uniwa/students/students3/20488223/linux/Compressor20488223/CompressorTestFiles/Compressed/";
    public static String decompressed  = "/home/uniwa/students/students3/20488223/linux/Compressor20488223/CompressorTestFiles/Decompressed/";
    
    public static String compressLog = "/home/uniwa/students/students3/20488223/linux/Compressor20488223/LogFiles/BULKcompressionlog.txt";
    public static String decompressLog = "/home/uniwa/students/students3/20488223/linux/Compressor20488223/LogFiles/BULKdecompressionlog.txt";
    
    public static void compress()
    {
        try
        {

            File dir            = new File(sourceDir);
            
            
            File[] files = dir.listFiles();
            LZWImp lzw = new LZWImp(true);
            String info;
            
            FileWriter log = new FileWriter(compressLog, true);
            System.out.print("Filename, BytesRead, BytesWritten, Efficiency, TimeTake(ms), DecodeMapSize\n");
            for( File f : files)
            {
                if( f.isFile() )
                {
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
            
            FileWriter log = new FileWriter(decompressLog, true);

            System.out.print("Filename, BytesRead, BytesWritten, Efficiency, TimeTake, FileComparison\n");
            for( File f : files)
            {
                if( f.isFile() )
                {
                    try
                    {
                        info = f.getName() + ", ";
                        info += lzw.decompress(new FileInputStream(compressed + f.getName()), new FileOutputStream(decompressed + f.getName().substring(0, f.getName().length()-2)));
                        info += String.format(", %d\n", f.compareTo( new File(sourceDir+f.getName()) ));
                        System.out.print(info);
                        log.write(info);   
                    }
                    catch(Exception ex)
                    {
                      ex.printStackTrace();
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
            
            //FileWriter log = new FileWriter("C:\\DataStructuresLabs\\CompressorTestFiles\\compressionlog.txt");
            //log.write("Filename, BytesRead, BytesWritten, Efficiency, TimeTake(ms), DecodeMapSize\n");
            //System.out.print("Filename, BytesRead, BytesWritten, Efficiency, TimeTake(ms), DecodeMapSize\n");
            for( File f : files)
            {
                if( f.isFile() )
                {
                    //System.out.println("Attempting to compress: " + f.getName());
                    //info = f.getName() + ", ";
                    //info += lzw.compress(new FileInputStream(sourceDir + f.getName()), new FileOutputStream(compressed + f.getName() + ".z"));
                    //System.out.print(info);
                    //log.write(info);
                    
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
                            //System.out.print("\n"+ (n) + " ok");
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
    
    private static void usage()
    {
        
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException
    {

        System.out.println("Totally sweet rockin LZW Compressor Implementation");
        System.out.println("Running Compression Test");
        compress();
        System.out.println("Running decompression Test");
        decompress();
        System.out.println("Running comparrison test");
        compare();
    }
}
