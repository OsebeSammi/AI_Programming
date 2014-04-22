package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import org.ejml.simple.SimpleMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.TextField;
import java.io.*;
import java.util.ArrayList;


public class Controller
{
    public javafx.scene.control.TextField input1;
    public javafx.scene.control.TextField input2;
    public Text fileChosen;
    public RadioButton space;
    public RadioButton comma;
    public Text fileChosenInput;
    public static String algorithm;
    public static File file;
    public static String spaceBy;
    public static File fileInput;
    public static String nothing[] = new String[1];

    public void file(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("chooseFile.fxml"));
            scene.setRoot(root);



        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



    }

    public void train(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("MLType.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void chooseFile(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        File file = new FileChooser().showOpenDialog(stage);
        fileChosen.setText(file.getName());

        String spaceBy;
        if(space.isSelected())
        {
            spaceBy = " ";
        }
        else if(comma.isSelected())
        {
            spaceBy = ",";
        }

        else
        {
            spaceBy = " ";
        }

        this.file = file;
        this.spaceBy = spaceBy;
        nothing[0] = file.getPath();
    }

    public void clustering(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("cluster.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void predict(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();


        try {
            Parent root = FXMLLoader.load(getClass().getResource("predict.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void backPropagation(ActionEvent actionEvent)
    {
        Train train = new Train(file,spaceBy);

        algorithm = "back propagation";
        try
        {
            train.backPropagation();
        }

        catch (Exception ex) {
            ex.printStackTrace(); // Or better logging.
        }
    }

    public void kMeans(ActionEvent actionEvent)
    {
        algorithm = "k means";
        Kmeans kmeans = new Kmeans();

    }

    public void start(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void user(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();
        Scene scene = stage.getScene();



        try {
            Parent root = FXMLLoader.load(getClass().getResource("input.fxml"));
            //adding text fields



            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public void predictRun(ActionEvent actionEvent)
    {
        Train train = new Train(file,spaceBy);


        if(algorithm.equals("back propagation"))
        {
            train.work(fileInput);
        }
    }

    public void pickFile(ActionEvent actionEvent)
    {
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        fileInput = new FileChooser().showOpenDialog(stage);
        fileChosenInput.setText(fileInput.getName());
    }

    public void adaline(ActionEvent actionEvent)
    {
        Train train = new Train(file,spaceBy);
        algorithm = "adaline";
        train.adalineTrain();

        //new file
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        JOptionPane.showMessageDialog(null,"Choose the input file");
        File fileAdaline = new FileChooser().showOpenDialog(stage);
        train.adalineGo(fileAdaline);

    }

    public void kNN(ActionEvent actionEvent)
    {
        algorithm = "kNN";

        KNN knn = new KNN(file,spaceBy);

        knn.KNNGo();
    }

    public void perceptron(ActionEvent actionEvent)
    {
        Train train = new Train(file,spaceBy);
        algorithm = "perceptron";
        train.perceptronTrain();

        //new file
        Node node = (Node)actionEvent.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        JOptionPane.showMessageDialog(null,"Choose the input file");
        File fileAdaline = new FileChooser().showOpenDialog(stage);
        train.perceptronGo(fileAdaline);
    }
}
