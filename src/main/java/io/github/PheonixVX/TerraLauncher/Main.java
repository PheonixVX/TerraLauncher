package io.github.PheonixVX.TerraLauncher;

import io.github.PheonixVX.TerraLauncher.authentication.AuthenticationManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String username = System.getProperty("launch.username");
        String password = System.getProperty("launch.password");
        boolean stopLoading = false;
        if (username != null && password != null) {
            AuthenticationManager authManager = new AuthenticationManager(username, password);
            authManager.request();
            stopLoading = true;
        }
        File file = new File("token.log");
        if (file.exists()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TerraLauncher.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage2 = new Stage();
            stage2.setTitle("TerraLauncher");
            stage2.setScene(new Scene(root1));
            stage2.show();
            stopLoading = true;
        }
        if (!stopLoading) {
            Parent root = FXMLLoader.load(getClass().getResource("/LoginWindow.fxml"));
            Scene scene = new Scene(root, 300, 275);

            stage.setTitle("TerraLauncher | Login");
            stage.setScene(scene);
            stage.show();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
