package io.github.PheonixVX.TerraLauncher;

import io.github.PheonixVX.TerraLauncher.authentication.AuthenticationManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Controller {

	@FXML private TextField username;
	@FXML private TextField password;

	@FXML
	private void requestAuthentication(Event event) {
		event.consume();
		AuthenticationManager authManager = new AuthenticationManager(username.getText(), password.getText());
		authManager.request();
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TerraLauncher.fxml"));
			Parent root1 = fxmlLoader.load();
			Stage stage = new Stage();
			stage.setTitle("TerraLauncher");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
