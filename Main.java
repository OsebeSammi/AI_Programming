package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application
{

    public Stage thisStage;

    @Override
    public void start(final Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("AI Programming");

        thisStage = primaryStage;

        primaryStage.setScene(new Scene(root,1300,800));
        primaryStage.show();

    }

    public void chooseFile()
    {


        try {
            Parent root = FXMLLoader.load(getClass().getResource("chooseFile.fxml"));
            thisStage.setTitle("AI Programming");


            thisStage.setScene(new Scene(root, 1000, 800));
            thisStage.show();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public static void main(String[] args)
    {
        launch(args);

    }
}
