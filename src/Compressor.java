package compressor;

import java.io.IOException;
import java.io.*;
/**
 * LZW Entry Point
 * @author Kuraara Forrest
 */
public class Compressor
{

    private static void usage()
    {
        System.out.println("Usage java -jar Compressor.jar [OPTION] <Input File> <Output File>");
        System.out.println("OPTIONS:");
        System.out.println("\t-v print verbose");
        System.out.println("\t-c Compress the input file to the specified output file");
        System.out.println("\t-d Decompress the input file to the specified output file");
        System.out.println("\nThe output file will have the extension .lzw");
        System.out.println("Using -d flag on a file that doesn't have this extension will present this screen");
    }
    
    /**
     * Compress an input file using the Lempel-Ziv Welch algorithm
     * @param args the command line arguments -cvd INPUTFILE OUTPUTFILE
     */
    public static void main(String[] args) throws IOException
    {
        
        boolean verbose = false;
        int action = -1;
        boolean test = false;

        for(int j = 0; j < args[0].length(); j++)
        {
            if( args[0].charAt(j) == 'v' )
            {
                verbose = true;
            }
            if( args[0].charAt(j) == 'c' && action == -1)
            {
                action = 1;
            }
            if( args[0].charAt(j) == 'd' && action == -1)
            {
                action = 2;
            }
            if( args[0].charAt(j) == 't' )
            {
                test = true;
            }
        }
        
        LZW lzw = new LZW(verbose);
        File in = new File(args[1]);
        
        File out = new File(args[2]+".lzw");
        String result = "";
        if( in.exists() && in.isFile() )
        {
            switch(action)
            {
                case 1:
                    result = lzw.compress(new FileInputStream(in), new FileOutputStream(out));
                     if(verbose)
                     {
                        System.out.println(result);
                     }
                     
                    break;
                case 2:
                    if(!args[1].endsWith(".lzw"))
                    {
                        usage();
                        break;
                    }
                    result = lzw.decompress(new FileInputStream(in), new FileOutputStream(out));
                    if(verbose)
                    {
                       System.out.println(result);
                    }
                    break;
                case -1:
                    usage();
                    break;
            }
        }

    }
    
}
