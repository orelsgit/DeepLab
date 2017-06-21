package entities;

import java.io.IOException;
import java.io.PrintWriter;

public class Write {
    private static final Write write= new Write();

    private Write() {
        super();
    }

    public synchronized void writeVersion(int version) {
    	PrintWriter writer = null;
    	try{
    	    writer = new PrintWriter("Version.txt", "UTF-8");
    	    writer.print("");
    	    if(version<10)
    	    	writer.println("0" + Integer.toString(version));
    	    else
    	    	writer.println(Integer.toString(version));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}finally{
    	    writer.close();
    	}
    }

    
    
    public static Write getInstance() {
        return write;
    }

}