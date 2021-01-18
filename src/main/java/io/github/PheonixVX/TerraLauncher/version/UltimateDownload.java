package io.github.PheonixVX.TerraLauncher.version;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class UltimateDownload implements AbstractVersionDownload {
	private URL clientUrl;
	private URL[] libraries;
	private URL[] natives;

	public UltimateDownload() {
		try {
			clientUrl = new URL("https://www.sithgames.tk/launcher/minecraft.jar");
			libraries = new URL[] {
				new URL("https://libraries.minecraft.net/net/minecraft/launchwrapper/1.6/launchwrapper-1.6.jar"),
				new URL("https://libraries.minecraft.net/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar"),
				new URL("https://libraries.minecraft.net/org/ow2/asm/asm-all/4.1/asm-all-4.1.jar"),
				new URL("https://libraries.minecraft.net/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar"),
				new URL("https://libraries.minecraft.net/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar"),
				new URL("https://libraries.minecraft.net/org/lwjgl/lwjgl/lwjgl/2.9.0/lwjgl-2.9.0.jar"),
				new URL("https://libraries.minecraft.net/org/lwjgl/lwjgl/lwjgl_util/2.9.0/lwjgl_util-2.9.0.jar"),
				new URL("https://libraries.minecraft.net/org/lwjgl/lwjgl/lwjgl/2.9.1-nightly-20130708-debug3/lwjgl-2.9.1-nightly-20130708-debug3.jar"),
				new URL("https://libraries.minecraft.net/org/lwjgl/lwjgl/lwjgl_util/2.9.1-nightly-20130708-debug3/lwjgl_util-2.9.1-nightly-20130708-debug3.jar"),
				new URL("https://libraries.minecraft.net/net/java/jinput/jinput-platform/2.0.5/jinput-platform-2.0.5-natives-linux.jar"),
				new URL("https://libraries.minecraft.net/net/java/jinput/jinput-platform/2.0.5/jinput-platform-2.0.5-natives-osx.jar"),
				new URL("https://libraries.minecraft.net/net/java/jinput/jinput-platform/2.0.5/jinput-platform-2.0.5-natives-windows.jar")
			};
			natives = new URL[] {
				new URL("https://libraries.minecraft.net/org/lwjgl/lwjgl/lwjgl-platform/2.9.0/lwjgl-platform-2.9.0-natives-windows.jar"),
				new URL("https://libraries.minecraft.net/org/lwjgl/lwjgl/lwjgl-platform/2.9.0/lwjgl-platform-2.9.0-natives-osx.jar"),
				new URL("https://libraries.minecraft.net/org/lwjgl/lwjgl/lwjgl-platform/2.9.0/lwjgl-platform-2.9.0-natives-linux.jar")
			};
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void downloadClient () {
		try (InputStream in = clientUrl.openStream()) {
			File file = new File( System.getProperty("user.dir") + "/bin/minecraft.jar");
			if (!file.exists()) {
				file.mkdirs();
				file.createNewFile();
				Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void downloadLibraries () {
		for (int i = 0; i < libraries.length; i++) {
			try (InputStream in = libraries[i].openStream()) {
				String url = Paths.get(libraries[i].getPath()).getFileName().toString();
				File file = new File( System.getProperty("user.dir") + "/libs/" + url);
				if (!file.exists()) {
					file.mkdirs();
					file.createNewFile();
					Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void downloadNatives () {
		for (int i = 0; i < natives.length; i++) {
			try (InputStream in = natives[i].openStream()) {
				String url = Paths.get(natives[i].getPath()).getFileName().toString();
				File file = new File( System.getProperty("user.dir") + "/natives/" + url);
				if (!file.exists()) {
					file.mkdirs();
					file.createNewFile();
					Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
					if (file.canRead()) {
						unzip(file, new File(System.getProperty("user.dir") + "/natives/"));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void launch () throws IOException {
		ArrayList<String> libs = new ArrayList<>();
		for (int i = 0; i < libraries.length; i++) {
			String url = Paths.get(libraries[i].getPath()).getFileName().toString();
			libs.add(url);
		}
		StringBuilder sb = new StringBuilder();
		for (String s : libs)
		{
			sb.append("libs/" + s + ";");
		}
		sb.append("bin/minecraft.jar");
		BufferedReader stream = new BufferedReader(new FileReader("token.log"));
		String username = stream.readLine();
		String sessionToken = stream.readLine().replace("\"", "");
		ProcessBuilder pb = new ProcessBuilder("java", "-Djava.library.path=natives/", "-cp", "\"" + sb.toString() + "\"", "net.minecraft.client.Minecraft", username, sessionToken);
		System.out.println(pb.command());
		File file = new File("logs/");
		file.mkdirs();
		file = new File("logs/output.txt");
		pb.redirectOutput(file);
		pb.redirectError(file);
		pb.start();
	}

	public static void unzip(File archive, File destDir) throws IOException {
		byte[] buffer = new byte[256 * 1024];
		destDir.mkdirs();
		try (JarFile jar = new JarFile(archive)) {
			Enumeration<JarEntry> entries = jar.entries();
			while (entries.hasMoreElements()) {
				JarEntry ent = entries.nextElement();
				File f = new File(destDir, ent.getName());
				if (ent.isDirectory()) {
					f.mkdir();
					continue;
				}
				try (InputStream is = jar.getInputStream(ent);
				     FileOutputStream os = new FileOutputStream(f)) {
					for (int r; (r = is.read(buffer)) > 0; ) {
						os.write(buffer, 0, r);
					}
				}
			}
		}
	}
}
