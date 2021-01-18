package io.github.PheonixVX.TerraLauncher;

import io.github.PheonixVX.TerraLauncher.version.UltimateDownload;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;

public class TerraLauncher {

	@FXML
	private WebView webView;

	@FXML
	private void initialize() {
		WebEngine engine = webView.getEngine();
		engine.load("https://www.sithgames.tk/updatesinfdev/");
	}

	@FXML
	private void downloadAndPlay() throws InterruptedException, IOException {
		UltimateDownload download = new UltimateDownload();
		download.downloadClient();
		download.downloadLibraries();
		download.downloadNatives();

		download.launch();
	}
}
