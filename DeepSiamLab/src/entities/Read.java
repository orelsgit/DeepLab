package entities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Read {
	private static final Read read= new Read();


	private Read() {
		super();
	}

	@SuppressWarnings("unused")
	public synchronized int readVersion(int latestVersion) {
		InputStream is = null;
		
		int prevVersion = latestVersion-1;
		if(latestVersion == 1)
			prevVersion = 1;

		try{
			is = new FileInputStream("Version.txt");

			String version = "";

			for(int i=0;i<2;++i){	
				version += (char) is.read();
			}
			return Integer.parseInt(version);

		}catch(FileNotFoundException e){
			Write write = Write.getInstance();
			write.writeVersion(prevVersion);
			return (prevVersion);
		} 

		catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return 01;
	}



	public static Read getInstance() {
		return read;
	}

}