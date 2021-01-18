package io.github.PheonixVX.TerraLauncher.authentication;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class AuthenticationManager {
	private String EMAIL;
	private String PASSWORD;
	private URL YGGDRASIL_URL;

	public AuthenticationManager(String email, String password) {
		this.EMAIL = email;
		this.PASSWORD = password;
		try {
			YGGDRASIL_URL = new URL("https://authserver.mojang.com/authenticate");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void request () {
		try {
			JsonObject object = new JsonObject();
			object.add("username", new Gson().toJsonTree(this.EMAIL));
			object.add("password", new Gson().toJsonTree(this.PASSWORD));
			byte[] message = object.toString().getBytes();

			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
				                      .uri(YGGDRASIL_URL.toURI())
				                      .POST(HttpRequest.BodyPublishers.ofByteArray(message))
				                      .build();

			HttpResponse<String> response = client.send(request,
				HttpResponse.BodyHandlers.ofString());

			System.out.println(response.body());
			JsonParser parser = new JsonParser();
			JsonObject responseObject = parser.parse(response.body()).getAsJsonObject();
			File file = new File("token.log");
			if (file.createNewFile()) {
				FileWriter writer = new FileWriter(file.getAbsoluteFile());
				writer.write(this.EMAIL + "\n");
				writer.write(String.valueOf(responseObject.get("accessToken")));
				writer.close();
			} else {
				throw new IllegalStateException("Failed to write file");
			}
		} catch (IOException | URISyntaxException | InterruptedException e) {
				e.printStackTrace();
		}
	}
}
