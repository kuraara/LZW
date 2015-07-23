/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compressor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
/**
 *
 * @author Kuraara
 */
public class Reader
{
    
    InputStream in;
    
    private int[] q;
    private static final int BUF_SIZ = 256;
    private int f;
    private int n;
    
    private int BytesRead;
    private int a;
    private int b;
    private int c;
    private Stack<Integer> s;
    private Queue<Integer> l;
    private int X;
    private int Y;
    private int next;
    private int av;
    private int buf;
    
    
    public Reader(InputStream input) throws IOException
    {
        in = input;
        q = new int[BUF_SIZ];
        s = new Stack<Integer>();
        l = new LinkedList<Integer>();
        n = 0;
        buf = 0;
        av = (int)(0.75 * (double)input.available());
    }
    
    public int available() throws IOException
    {
        return in.available();
    }
    
    
    public int read() throws IOException
    {
        int r = 0;
        if(n == 0 )
        {
            a = next();
            b = next();
            c = next();
            
            if(a == -1 || b == -1 || c == -1)
            {
                return -1;
            }
            
            a = a << 16;
            b = b << 8;
            
            buf = a | b | c;
            Y = buf & 0xFFF;
            buf = buf & 0xFFF000;
            buf = buf >> 12;
            X = buf;
            n = 1;
            
            
            buf = 0;
            return X;
        }
        else if( n== 1 )
        {
            n = 0;
            r = Y;
            X = -2;
            Y = -2;
            av =- 2;
            return r;
        }
        if( in.available() == 0)
        {
            next = -1;
        }
        return r;
    }
    
    public boolean hasNext()
    {
        return peek() != -1;
    }
    
    private int next() throws IOException
    {
        next = in.read();
        BytesRead++;
        return next;
    }
    
    private int peek()
    {
        return next;
    }
    
    public int close() throws IOException
    {
        in.close();
        return BytesRead;
    }
}
