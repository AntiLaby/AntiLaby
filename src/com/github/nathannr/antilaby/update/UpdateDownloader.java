package com.github.nathannr.antilaby.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.github.nathannr.antilaby.main.AntiLaby;

public class UpdateDownloader extends Thread {

	boolean updateAvailable;
	
	@Override
	// Start update function async
	public void run() {
		// Check for update
		System.out.println(AntiLaby.getInstance().cprefixinfo + "Checking for updates on spigotmc.org...");
		try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + AntiLaby.getInstance().resource).getBytes("UTF-8"));
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (!version.equalsIgnoreCase(AntiLaby.getInstance().getDescription().getVersion())) {
            	System.out.println(AntiLaby.getInstance().cprefixinfo + "Update found! Version " + version + " is aviable.");
            	updateAvailable = true;
            } else {
            	System.out.println(AntiLaby.getInstance().cprefixinfo + "No update found. You are running the newest version.");
            	updateAvailable = false;
            }
        } catch (Exception ex) {
        	System.err.println(AntiLaby.getInstance().cprefixerr + "Failed to check for updates on spigotmc.org!");
    		updateAvailable = false;
        }
		// Download and install update if available
		if(updateAvailable == true) {
			System.out.println(AntiLaby.getInstance().cprefixinfo + "Downloading update...");
			URL url;
			try {
				url = new URL("http://adf.ly/1f1ZEn"); // TODO Download from GitHub
				final URLConnection conn = url.openConnection();
				final InputStream is = new BufferedInputStream(conn.getInputStream());
				if(is != null) {
					final OutputStream os = new BufferedOutputStream(new FileOutputStream("plugins/AntiLaby.tmp"));
					byte[] chunk = new byte[1024];
					int chunkSize;
					while ((chunkSize = is.read(chunk)) != -1) {
						os.write(chunk, 0, chunkSize);
					}
					os.close();
					File newfile = new File("plugins/AntiLaby.tmp");
					long newlength = newfile.length();
					if(newlength <= 10000) {
						newfile.delete();
						System.err.println(AntiLaby.getInstance().cprefixerr + "Auto-update failed! Update server down? You still have version " + AntiLaby.getInstance().getDescription().getVersion() + ". Please install the newest version manually from https://www.spigotmc.org/resources/" + AntiLaby.getInstance().resource + "/!");
					} else {
						System.out.println(AntiLaby.getInstance().cprefixinfo + "Installing update...");
						
						final FileInputStream is2 = new FileInputStream(new File("plugins/AntiLaby.tmp"));
						
						final OutputStream os2 = new BufferedOutputStream(new FileOutputStream(AntiLaby.getInstance().getPluginFile()));
						byte[] chunk2 = new byte[1024];
						int chunkSize2;
						while ((chunkSize2 = is2.read(chunk2)) != -1) {
							os2.write(chunk2, 0, chunkSize2);
						}
						is2.close();
						os2.close();
						
						File tmp = new File("plugins/AntiLaby.tmp");
						tmp.delete();
						
						System.out.println(AntiLaby.getInstance().cprefixinfo + "Auto-update complete! Reload / restart your server to activate the new version.");
					}
				}
				
			} catch (IOException e) {
				System.err.println(AntiLaby.getInstance().cprefixerr + "Auto-update failed! You still have version " + AntiLaby.getInstance().getDescription().getVersion() + ". Please install the newest version manually from https://www.spigotmc.org/resources/" + AntiLaby.getInstance().resource + "/!");
				e.printStackTrace();
			}
			File tmp = new File("plugins/AntiLaby.tmp");
			if(tmp.exists()) {
				tmp.delete();
			}
		}
		AntiLaby.getInstance().disableIfNotCompatible();
		this.interrupt();
	}
	
}
