package view.clientView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Writer {
    
	public void write(String a) throws FileNotFoundException{

		PrintWriter pw = new PrintWriter(new File("test2.csv"));
        
        pw.print("");
		
        pw.write(a);
        pw.close();
    }
}