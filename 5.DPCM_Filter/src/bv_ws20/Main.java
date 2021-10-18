// BV Ue2 WS20/21 Vorgabe
//
// Copyright (C) 2019-2020 by Klaus Jung
// All rights reserved.
// Date: 2020-10-08
package bv_ws20;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("AppView.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("DPCM - WS20/21 - Poetsch"); // TODO: add your name(s)
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
