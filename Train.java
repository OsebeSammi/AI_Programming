package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ejml.simple.SimpleMatrix;

import javax.swing.*;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.lang.NumberFormatException;

/**
 * Created with IntelliJ IDEA.
 * User: sammi
 * Date: 2/27/14
 * Time: 11:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Train
{
    BufferedReader read;
    String line;
    String[] content,titles;


    double[][] weightAfterInput,weightAfterHidden;//used for back propagation
    double[] weights;//used for adaline
    double[][] outputTraining,errors;
    double[] valueAtHiddenNeuron,error2;

    int numberOfHiddenNeurons;

    int countColumn = 0;
    int countRow = 0;
    int maxColumns = 0,maxRows = 0;
    int outputCount = 0;
    int colOutput = 0,colInput = 0;
    int maxLoop = 100;
    double threshold = 0;
    boolean continueLoop = false;

    double divisor = 1;
    double maxValue = 1;//has the maximum value,will be used in normalisation

    double[][] fileInput;
    double[][] input,output;

    double learning_rate = 0.1;

    String spaceBy;


    File file;

    public Train(File file,String spaceBy)
    {
        this.spaceBy = spaceBy;
        this.file = file;
        try
        {
            read = new BufferedReader(new FileReader(file));

            //determining number of columns and rows
            line = read.readLine();
            content = line.split(spaceBy);
            maxColumns = content.length;

            while((line = read.readLine())!=null)
            {
                maxRows++;
            }

            fileInput = new double[maxColumns][maxRows];



            //again
            read = new BufferedReader(new FileReader(file));

            //reading header
            line = read.readLine();
            line = line.toLowerCase();
            titles = line.split(spaceBy);

            for(int x=0;x<maxColumns;x++)
            {
                if(titles[x].contains("output"))
                {
                    outputCount++;
                }
            }

            if(content.length<2)
            {
                JOptionPane.showMessageDialog(null,"The file is not separated by "+spaceBy);
                return;
            }

            while((line = read.readLine())!=null)
            {

                content = line.split(spaceBy);
                for(int x=0;x<maxColumns;x++)
                {
                    fileInput[x][countRow] = Double.parseDouble(content[x]);
                    if(fileInput[x][countRow]>maxValue)
                    {
                        maxValue = fileInput[x][countRow];
                    }
                }

                countRow++;
            }

            countColumn = 0;

            read.close();


            //assigning rows to input and outputs
            input = new double[maxColumns - outputCount][maxRows];
            output = new double[outputCount][maxRows];



            for(int cols=0;cols<maxColumns;cols++)
            {
                if(!titles[cols].contains("output"))
                {
                    for(int rows = 0;rows<maxRows;rows++)
                    {
                        input[colInput][rows] = fileInput[cols][rows];
                    }
                    colInput++;
                }
                else
                {
                    for(int rows = 0;rows<maxRows;rows++)
                    {
                        output[colOutput][rows] = fileInput[cols][rows];
                    }
                    colOutput++;
                }
            }




        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }

        //JOptionPane.showMessageDialog(null,"file read successfully,You have "+maxColumns+"columns and "+maxRows+"rows");
        //backPropagation();
    }

    public void normalise()
    {
        //ensuring the epoch values is between -1 and 1

        double temp;

        while(true)
        {
            maxValue = maxValue/divisor;

            if(maxValue>1 || maxValue<-1)
            {
                divisor *= 10;
            }

            else if(maxValue<0.1 || maxValue<-0.1)
            {
                divisor *=(1/10);
            }

            else
            {
                break;
            }


        }

        for(int rows = 0;rows<maxRows;rows++)
        {
            for(int columns = 0;columns<maxColumns;columns++)
            {
                fileInput[columns][rows] = fileInput[columns][rows]/divisor;
            }
        }
    }




    public void backPropagation()
    {
        //normalise
        normalise();

        learning_rate = Double.parseDouble(JOptionPane.showInputDialog("Enter the learning rate"));

        numberOfHiddenNeurons = (int) (maxColumns*(0.6667));
        valueAtHiddenNeuron = new double[numberOfHiddenNeurons];

        weightAfterInput = new double[numberOfHiddenNeurons][maxColumns - outputCount];
        weightAfterHidden = new double[outputCount][numberOfHiddenNeurons];

        //filling weights after inputs with 0.1
        for(int rows = 0;rows<(maxColumns - outputCount);rows++)
        {
            for(int columns = 0;columns<numberOfHiddenNeurons;columns++)
            {
                weightAfterInput[columns][rows] = -1.0+(Math.random()*2);
            }
        }

        //filling weights after hidden with random weights
        for(int rows = 0;rows<numberOfHiddenNeurons;rows++)
        {
            for(int columns = 0;columns<outputCount;columns++)
            {
                weightAfterHidden[columns][rows] = -1.0+(Math.random()*2);
            }
        }


        //will hold the values for output from training and the errors
        outputTraining = new double[outputCount][maxRows];
        errors = new double[outputCount][maxRows];
        error2 = new double[numberOfHiddenNeurons];//error before hidden

        //we can start

        //iterate through all the input array row by row
        for(int rows = 0;rows<maxRows; rows++)
        {
             //filling the value at the hidden neuron
             for(int neuronCol = 0; neuronCol<numberOfHiddenNeurons; neuronCol++)
             {
                 valueAtHiddenNeuron[neuronCol] = atHiddenNeuron(rows,neuronCol);
             }


            //iterating through outputs and calculating the values they  should have
            for(int col = 0; col<outputCount; col++)
            {
                outputTraining[col][rows] = atOutput(col);

                //calculating errors corresponds to step 1 of second tutorial
                errors[col][rows] = outputTraining[col][rows]*(1 - outputTraining[col][rows])*(output[col][rows]);

                threshold = errors[col][rows];

                if(threshold>0.0001)
                {
                    continueLoop = true;
                }
                else
                {
                    continueLoop = false;
                }

                //correcting the weights, starting with the weights after hidden
                //Steps 2
                updateWeightAfterHidden(errors[col][rows],col);


            }

            //update weights after input
            //step 3 back propagating errors
            for(int hiddenNeurons = 0; hiddenNeurons<numberOfHiddenNeurons; hiddenNeurons++)
            {
                error2[hiddenNeurons] = valueAtHiddenNeuron[hiddenNeurons]*(1 - valueAtHiddenNeuron[hiddenNeurons])*(weightsSum(hiddenNeurons,rows));

                if(threshold<0.0001)
                {
                    threshold = error2[hiddenNeurons];

                    if(threshold>0.0001)
                    {
                        continueLoop = true;
                    }
                    else
                    {
                        continueLoop = false;
                    }
                }

            }


            //step 4 modifying weight after input
            for(int y=0; y<(maxColumns - outputCount); y++)
            {
                for(int x=0; x<numberOfHiddenNeurons; x++)
                {
                    weightAfterInput[x][y] = weightAfterInput[x][y] + (learning_rate*error2[x]*input[x][rows]);

                }
            }

            if((rows+2)>maxRows)
            {
                //threshold is not equal to 0, loop
                if(continueLoop)
                {
                   rows = 0;
                   maxLoop--;

                }
            }

            if(maxLoop<1)
            {break;}
        }

        JOptionPane.showMessageDialog(null,"Trained successfully, proceed");

    }

    public double atHiddenNeuron(int row,int nCol)//calculate the value that a neuron should receive
    {
        double value = 0;

        //iterating through the inputs at a specific row
        for(int column = 0; column<colInput; column++)
        {
            value += input[column][row]*weightAfterInput[nCol][column];
        }

        //calculating the value after applying sigmoid function
        return (1/(1+Math.pow(Math.E,-value)));
    }

    public double atOutput(int outputCalc)//calculate the value that a outputs should receive
    {
        double value = 0;

        //iterating through the neurons
        for(int column = 0; column<numberOfHiddenNeurons; column++)
        {
            value += valueAtHiddenNeuron[column]*weightAfterHidden[outputCalc][column];//see model for more information
        }

        //sigmoid function calculation

        return (1/(1+Math.pow(Math.E,-value)));
    }

    public void updateWeightAfterHidden(double err,int column)
    {
       /*iterating through weight after hidden*/
        for(int hiddenLayer = 0; hiddenLayer<numberOfHiddenNeurons; hiddenLayer++)
        {
            //view model to understand their structures
            // 0.6 is the standard learning rate
            weightAfterHidden[column][hiddenLayer] = weightAfterHidden[column][hiddenLayer] + (0.6*err*valueAtHiddenNeuron[hiddenLayer]);

        }
    }

    public double weightsSum(int neuron,int row)
    {
        double value = 0;
        int countWeightColumn = 0;

        for(int col = 0; col<colOutput; col++)
        {
            while(true)
            {
                value += errors[col][row]*weightAfterHidden[countWeightColumn][neuron];
                countWeightColumn++;
                break;
            }
        }

        return value;
    }

    public void work(File file)
    {
        maxColumns = maxRows = 0;

        try
        {
            read = new BufferedReader(new FileReader(file));

            //determining number of columns and rows
            line = read.readLine();
            content = line.split(spaceBy);
            maxColumns = content.length;

            while((line = read.readLine())!=null)
            {
                maxRows++;
            }

            fileInput = new double[maxColumns][maxRows];



            //again
            read = new BufferedReader(new FileReader(file));

            //reading header
            line = read.readLine();
            line = line.toLowerCase();
            titles = line.split(spaceBy);

            for(int x=0;x<maxColumns;x++)
            {
                if(titles[x].contains("output"))
                {
                    outputCount++;
                }
            }

            if(content.length<2)
            {
                JOptionPane.showMessageDialog(null,"The file is not separated by "+spaceBy);
                return;
            }

            countRow = 0;
            while((line = read.readLine())!=null)
            {

                content = line.split(spaceBy);
                for(int x=0;x<maxColumns;x++)
                {
                    fileInput[x][countRow] = Double.parseDouble(content[x]);
                    if(fileInput[x][countRow]>maxValue)
                    {
                        maxValue = fileInput[x][countRow];
                    }
                }

                countRow++;
            }

            countColumn = 0;

            read.close();


            //assigning rows to input and outputs
            input = new double[maxColumns][maxRows];


            for(int cols=0;cols<maxColumns;cols++)
            {
                    for(int rows = 0;rows<maxRows;rows++)
                    {
                        input[cols][rows] = fileInput[cols][rows];
                    }

            }




        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (NumberFormatException nfe)
        {
            nfe.printStackTrace();
        }

        //calculate values

        normalise();

        try
        {
            File newFile = new File("/home/sammi/Desktop/AI_Output/"+file.getName()+"Output");
            PrintWriter write = new PrintWriter(newFile);


        //iterate through all the input array row by row
        for(int rows = 0;rows<maxRows; rows++)
        {
            //filling the value at the hidden neuron
            for(int neuronCol = 0; neuronCol<numberOfHiddenNeurons; neuronCol++)
            {
                valueAtHiddenNeuron[neuronCol] = atHiddenNeuron(rows,neuronCol);
            }


            //iterating through outputs and calculating the values they  should have
            for(int col = 0; col<outputCount; col++)
            {
                write.println(atOutput(col));
            }

        }

        write.flush();
        write.close();
        JOptionPane.showMessageDialog(null,"Please look at the output file"+file.getPath()+"Output");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    public double[] weightsAdaline;
    public void adalineTrain()
    {
        weightsAdaline = new double[maxColumns - outputCount];
        //enter the bias
        double bias = Float.parseFloat(JOptionPane.showInputDialog("Enter bias value:"));

        //enter the learning rate
        double learningrate = Float.parseFloat(JOptionPane.showInputDialog("Enter the learning rate"));


        //assigning default weights
        for(int w=0;w<(maxColumns - outputCount);w++)
        {
            weightsAdaline[w] = input[w][0];
        }

        //iterating through the inputs
        for(int y=0;y<maxRows-1;y++)
        {
            double activation=0;

            for(int x=0;x<(maxColumns - outputCount);x++)
            {
                activation=activation+weightsAdaline[x]*input[x][y];
            }

            activation=activation-bias;

            System.out.println(activation);
            double error=output[0][y]-activation;

            if(error!=0)
            {
                for(int x=0;x<(maxColumns - outputCount);x++)
                {
                    weightsAdaline[x] += learningrate*error*input[x][y];

                    System.out.println("Weight "+weightsAdaline[x]+"error "+error);
                }
            }
        }

        //calculating threshold instead of specifying
        int y;

        for(y=0;y<maxRows-1;y++)
        {
            threshold += output[0][y];
        }

        threshold /= y;


    }

    public void adalineGo(File file)
    {

        try
        {
            read = new BufferedReader(new FileReader(file));


            //determining number of columns and rows
            line = read.readLine();
            content = line.split(spaceBy);
            maxColumns = content.length;

            maxRows = 0;
            while((line = read.readLine())!=null)
            {
                maxRows++;
            }

            fileInput = new double[maxColumns][maxRows];



            //again
            read = new BufferedReader(new FileReader(file));

            //reading header
            line = read.readLine();
            line = line.toLowerCase();
            titles = line.split(spaceBy);

            outputCount = 0;
            for(int x=0;x<maxColumns;x++)
            {
                if(titles[x].contains("output"))
                {
                    outputCount++;
                }
            }

            if(content.length<2)
            {
                JOptionPane.showMessageDialog(null,"The file is not separated by "+spaceBy);
                return;
            }

            countRow = 0;

            while((line = read.readLine())!=null)
            {

                content = line.split(spaceBy);
                for(int x=0;x<maxColumns;x++)
                {
                    fileInput[x][countRow] = Double.parseDouble(content[x]);
                    if(fileInput[x][countRow]>maxValue)
                    {
                        maxValue = fileInput[x][countRow];
                    }
                }

                countRow++;
            }

            countColumn = 0;

            read.close();

            //assigning rows to input and outputs
            input = new double[maxColumns - outputCount][maxRows];

            output = new double[outputCount][maxRows];

            colInput = 0;colOutput = 0;
            for(int cols=0;cols<maxColumns;cols++)
            {
                if(!titles[cols].contains("output"))
                {
                    for(int rows = 0;rows<maxRows;rows++)
                    {
                        input[colInput][rows] = fileInput[cols][rows];
                    }
                    colInput++;
                }
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        //output file
        File fileOut = new File("/home/sammi/Desktop/AI_Output/"+file.getName()+"Output");
        try
        {
            PrintWriter write = new PrintWriter(fileOut);

            double activation=0;

            for(int y=0;y<maxRows;y++)
            {
                for(int x=0;x<maxColumns;x++)
                {
                    activation=activation+weightsAdaline[x]*input[x][y];

                    write.print(input[x][y]+" ");
                }

                System.out.println(activation);
                if(activation>threshold)
                {
                    write.println(1);
                }
                else
                {
                    write.println(0);
                }
            }

            write.flush();
            write.close();
            JOptionPane.showMessageDialog(null,"Check the output file at "+fileOut.getPath());
        }

        catch (FileNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }










    }



    public double[] weightsPerceptron;

    public void perceptronTrain()
    {
        weightsPerceptron = new double[maxColumns - outputCount];
        threshold = 0;
        //enter the bias
        double bias = Float.parseFloat(JOptionPane.showInputDialog("Enter bias value:"));

        //enter the learning rate
        double learningrate = Float.parseFloat(JOptionPane.showInputDialog("Enter the learning rate"));


        //assigning default weights
        for(int w=0;w<(maxColumns - outputCount);w++)
        {
            weightsPerceptron[w] = input[w][0];
        }

        //iterating through the inputs
        for(int y=0;y<maxRows-1;y++)
        {
            double activation=0;

            for(int x=0;x<(maxColumns - outputCount);x++)
            {
                activation=activation+weightsPerceptron[x]*input[x][y];
            }

            activation=activation-bias;

            System.out.println(activation);
            if(activation>threshold)
            {
                activation = 1;
            }
            else
            {
                activation = 0;
            }
            double error=output[0][y]-activation;

            if(error!=0)
            {
                for(int x=0;x<(maxColumns - outputCount);x++)
                {
                    weightsPerceptron[x] += learningrate*error*input[x][y];

                    System.out.println("Weight "+weightsPerceptron[x]+"error "+error);
                }
            }
        }

        //calculating threshold instead of specifying
        int y;

        for(y=0;y<maxRows-1;y++)
        {
            threshold += output[0][y];
        }

        threshold /= y;


    }

    public void perceptronGo(File file)
    {

        try
        {
            read = new BufferedReader(new FileReader(file));


            //determining number of columns and rows
            line = read.readLine();
            content = line.split(spaceBy);
            maxColumns = content.length;

            maxRows = 0;
            while((line = read.readLine())!=null)
            {
                maxRows++;
            }

            fileInput = new double[maxColumns][maxRows];



            //again
            read = new BufferedReader(new FileReader(file));

            //reading header
            line = read.readLine();
            line = line.toLowerCase();
            titles = line.split(spaceBy);

            outputCount = 0;
            for(int x=0;x<maxColumns;x++)
            {
                if(titles[x].contains("output"))
                {
                    outputCount++;
                }
            }

            if(content.length<2)
            {
                JOptionPane.showMessageDialog(null,"The file is not separated by "+spaceBy);
                return;
            }

            countRow = 0;

            while((line = read.readLine())!=null)
            {

                content = line.split(spaceBy);
                for(int x=0;x<maxColumns;x++)
                {
                    fileInput[x][countRow] = Double.parseDouble(content[x]);
                    if(fileInput[x][countRow]>maxValue)
                    {
                        maxValue = fileInput[x][countRow];
                    }
                }

                countRow++;
            }

            countColumn = 0;

            read.close();

            //assigning rows to input and outputs
            input = new double[maxColumns - outputCount][maxRows];

            output = new double[outputCount][maxRows];

            colInput = 0;colOutput = 0;
            for(int cols=0;cols<maxColumns;cols++)
            {
                if(!titles[cols].contains("output"))
                {
                    for(int rows = 0;rows<maxRows;rows++)
                    {
                        input[colInput][rows] = fileInput[cols][rows];
                    }
                    colInput++;
                }
            }
        }

        catch (IOException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        //output file
        File fileOut = new File("/home/sammi/Desktop/AI_Output/"+file.getName()+"Output");
        try
        {
            PrintWriter write = new PrintWriter(fileOut);

            double activation=0;

            for(int y=0;y<maxRows;y++)
            {
                for(int x=0;x<maxColumns;x++)
                {
                    activation=activation+weightsPerceptron[x]*input[x][y];

                    write.print(input[x][y]+" ");
                }

                System.out.println(activation);
                if(activation>threshold)
                {
                    write.println(1);
                }
                else
                {
                    write.println(0);
                }
            }

            write.flush();
            write.close();
            JOptionPane.showMessageDialog(null,"Check the output file at "+fileOut.getPath());
        }

        catch (FileNotFoundException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }










    }
}
