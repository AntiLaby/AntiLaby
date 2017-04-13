package com.github.nathannr.antilaby.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	private boolean updateAvailable;
	private String newVersion;

	@Override
	// Start update function async
	public void run() {
		// Check for updates
		System.out.println(AntiLaby.getInstance().getCprefixinfo() + "Checking for updates on spigotmc.org...");
		try {
			HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php")
					.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.getOutputStream()
					.write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource="
							+ AntiLaby.getInstance().getResource()).getBytes("UTF-8"));
			this.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
			if (!this.newVersion.equalsIgnoreCase(AntiLaby.getInstance().getDescription().getVersion())) {
				System.out.println(AntiLaby.getInstance().getCprefixinfo() + "Update found! Version " + this.newVersion
						+ " is aviable.");
				this.updateAvailable = true;
			} else {
				System.out.println(AntiLaby.getInstance().getCprefixinfo()
						+ "No update found. You are running the newest version.");
				this.updateAvailable = false;
			}
		} catch (Exception ex) {
			System.err.println(AntiLaby.getInstance().getCprefixerr() + "Failed to check for updates on spigotmc.org!");
			this.updateAvailable = false;
		}
		// Download and install update if available
		if (updateAvailable == true) {
			if (this.installUpdate(
					"https://github.com/NathanNr/AntiLaby/releases/download/" + this.newVersion + "/AntiLaby.jar", 1)) {
				System.out.println(AntiLaby.getInstance().getCprefixinfo()
						+ "Auto-update complete! Reload / restart your server to activate the new version.");
			} else {
				System.err.println(
						AntiLaby.getInstance().getCprefixerr() + "Failed to download update from download server 1!");
				if (this.installUpdate("http://adf.ly/1f1ZEn", 2)) {
					System.out.println(AntiLaby.getInstance().getCprefixinfo()
							+ "Auto-update complete! Reload / restart your server to activate the new version.");
				} else {
					System.err.println(AntiLaby.getInstance().getCprefixerr()
							+ "Failed to install update from download server 1 and 2! Please install the newest version manually from https://www.spigotmc.org/resources/"
							+ AntiLaby.getInstance().getResource() + "/!");
				}
			}
			File tmp = new File("plugins/AntiLaby.tmp");
			if (tmp.exists()) {
				tmp.delete();
			}
		}
		AntiLaby.getInstance().disableIfNotCompatible();
		this.interrupt();
	}

	private boolean installUpdate(String urlString, int urlId) {
		System.out.println(
				AntiLaby.getInstance().getCprefixinfo() + "Downloading update from download server " + urlId + "...");
		try {
			URL url = new URL(urlString);
			final URLConnection conn = url.openConnection();
			final InputStream is = new BufferedInputStream(conn.getInputStream());
			final OutputStream os = new BufferedOutputStream(new FileOutputStream("plugins/AntiLaby.tmp"));
			byte[] chunk = new byte[1024];
			int chunkSize;
			while ((chunkSize = is.read(chunk)) != -1) {
				os.write(chunk, 0, chunkSize);
			}
			os.close();
			File newfile = new File("plugins/AntiLaby.tmp");
			long newlength = newfile.length();
			if (newlength <= 10000) {
				newfile.delete();
				return false;
			} else {
				System.out.println(AntiLaby.getInstance().getCprefixinfo() + "Installing update...");

				final FileInputStream is2 = new FileInputStream(new File("plugins/AntiLaby.tmp"));

				final OutputStream os2 = new BufferedOutputStream(
						new FileOutputStream(AntiLaby.getInstance().getPluginFile()));
				byte[] chunk2 = new byte[1024];
				int chunkSize2;
				while ((chunkSize2 = is2.read(chunk2)) != -1) {
					os2.write(chunk2, 0, chunkSize2);
				}
				is2.close();
				os2.close();

				File tmp = new File("plugins/AntiLaby.tmp");
				tmp.delete();
				return true;
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
