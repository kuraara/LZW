/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compressor;

import java.io.*;
import java.util.*;
/**
 * LZW Implementation
 * @author Kuraara Forrest
 */
public class LZW
{
    
    private static final int MAP_SIZE   = 260;  // Size of the inital lookup dictionary ( single element byte arrays w/ values 0 - 255 //
    private static final int MAX_WIDTH  = (1 << 12) - 1;
    
    private static final int CLEAR      = 256;
    private static final int NO_DATA    = 257;
    
    private int mapSize = MAP_SIZE;
    private int hash;
    
    private int bytesIn;
    private int bytesOut;
    
    private boolean                 verbose;
    private Map<String, Integer>    CodeMap;
    private List<String>            DecodeMap;

    private long elapsed;
    private WriteBuffer wrt;
    
    /***
     * LZW Compressor
     * @param v print verbose output
     */
    public LZW(boolean v)
    {
        verbose = v;
    }
    
    // instantiate all of the things to use in the constructor and compress/decompress methods
    private void initialize() throws IOException
    {
        clear();
        bytesIn = 0;
        bytesOut = 0;
        elapsed = System.nanoTime();
    }
    
    /***
     * Clear method, unused - intended for resetting the dictionaries should the
     * current output efficiency drop below a certain threshold
     */
    private void clear()
    {
        CodeMap = new TreeMap<String, Integer>();
        DecodeMap = new ArrayList<String>();
        mapSize = MAP_SIZE;
        
        for(int i = 0; i < MAP_SIZE; i++)
        {
            CodeMap.put(Character.toString((char)i), Integer.valueOf(i));
            DecodeMap.add( Character.toString((char)i));
        }
    }
    
    /***
     * Compress an input stream
     * @param in the input stream to compress
     * @param out the destination of the compressed bytes
     * @return statistics of the compression process in the form of:
     * BytesRead, BytesWritten, Efficiency, Time(ms), DictionarySize
     */
    @Override
    public String compress(InputStream in, OutputStream out)
    {

        StringBuffer newWord = new StringBuffer();
        int current;
        
        String oldWord = "";
        int size = MAP_SIZE;

        try
        {
            initialize();
            current = in.read();
            oldWord += (char)current;

            wrt = new WriteBuffer(out);
            int n = 0;
			
            while( (current = in.read()) != -1)
            {
                
                bytesIn++;
                if(verbose)
                {
                    System.out.print( String.format("%6d KB Read %6d Written     \r", (bytesIn>>10), (bytesOut>>10))  );
                }
                newWord.setLength(0);
                newWord.append(oldWord);
                newWord.append((char)current);
                
                if( CodeMap.containsKey(newWord.toString()) )
                {
                    oldWord = newWord.toString();
                }
                else
                {
                    wrt.write(CodeMap.get(oldWord));
                    bytesOut += 2;
                    if( size < MAX_WIDTH )
                    {
                        CodeMap.put(newWord.toString(), size++);
                    }

                    oldWord = Character.toString((char)current);

                }
                
            }
            wrt.write(CodeMap.get(oldWord));
            wrt.write(NO_DATA);
            wrt.flush();
            bytesOut = wrt.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        elapsed = System.nanoTime() - elapsed;
        return compInfo();
        
    }

        /***
     * Decompress an input stream that was previously encoded using LZW
     * @param in the input stream to decompress
     * @param out the destination of the decompressed bytes
     * @return statistics of the decompression process in the form of:
     * BytesRead, BytesWritten, Efficiency, Time(ms), DictionarySize
     */
    @Override
    public String decompress(InputStream in, OutputStream out) 
    {
        int k = 0;
        
        String w;
        String entry;
        int size = MAP_SIZE;
        int n = 0;
        try
        {
            initialize();
            Reader r = new Reader(in);
            
            k = r.read();
            w = Character.toString((char)k);
            
            out.write(k);
            bytesOut++;
            if(verbose)    
            {
                System.out.print( String.format("\n%6d KB Read %6d Written     \r", (bytesIn>>10), (bytesOut>>10))  );
            }
            while( k != NO_DATA || k != -1)
            {
                k = r.read();
                
                if(verbose)
                {
                    System.out.print( String.format("%6d KB Read %6d Written     \r", (bytesIn>>10), (bytesOut>>10))  );
                }
                if( k == NO_DATA || k == -1 )
                {
                    break;
                }
                
                if( k >= DecodeMap.size() )
                {
                    entry = w + w.charAt(0);
                }
                else
                {
                    entry = DecodeMap.get(k);
                }

                for(int c = 0; c < entry.length(); c++)
                {
                    out.write( ((int)entry.charAt(c)) );
                }
                bytesOut += entry.length();
                if( DecodeMap.size() < 4095)
                {
                    DecodeMap.add((w + entry.charAt(0)) );
                }
                w = entry;
                
            }
            
            out.flush();
            out.close();
            bytesIn = r.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        elapsed = System.nanoTime() - elapsed;
        return DecompInfo();
    }
	
    public String compInfo()
    {
        return String.format("%d %d %f %d %d\n", bytesIn, bytesOut, ((float)bytesOut / bytesIn), elapsed/1000000, CodeMap.size());
    }
    
    public String DecompInfo()
    {
        return String.format("%d %d %f %d %d", bytesIn, bytesOut, ((float)bytesOut / bytesIn), elapsed/1000000, DecodeMap.size());
    }
	
}