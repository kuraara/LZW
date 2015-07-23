package compressor;
import java.io.*;
import java.util.*;

public class WriteBuffer
{

    private int f;
    private int b;
    private int n;
    private int buf;
    
    private int count;
    
    private static final int CLEAR      = 256;
    private static final int NO_DATA    = 257;
    
    int A;
    int B;
    int C;
    
    int x;
    int y;
    
    OutputStream out;

	WriteBuffer(OutputStream out)
    {
		this.out = out;
	}

    public void write(int b) throws IOException
    {
        if( b > 4096 )
        {
            throw new IOException("Write error. Value too large.");
        }
        
        switch(n)
        {
            case 0:
                x = b;
                n++;
                break;
            case 1:
                y = b;
                buf = (x<<12) | y;
                
                C   = buf & 0xFF;
                
                B   = buf & 0xFF00;
                
                A   = buf & 0xFF0000;
				
                B = B >> 8;
                A = A >> 16;
                out.write(A);
                out.write(B);
                out.write(C);
                
                buf = 0;
                n = 0;
                count += 3;
                break;
            
        }
    }
    
    public int Write(int b)
    {
        try
        {
            write(b);
        }
        catch(Exception e)
        {
            return -1;
        }
        return b;
    }
    
    public int flush() throws IOException
    {
        int x = Write(NO_DATA);
        
        if( n > 1 )
        {
            x = Write(NO_DATA);
        }
        return x;
    }
    
    public int close() throws IOException
    {
        out.close();
        return count;
    }
    
}