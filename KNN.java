package sample;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: sammi
 * Date: 4/12/14
 * Time: 9:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class KNN
{
    BufferedReader read;
    String line;
    String[] content,titles;

    int countColumn = 0;
    int countRow = 0;
    int maxColumns = 0,maxRows = 0;
    int outputCount = 0;
    int colOutput = 0,colInput = 0;


    String[][] fileInput;
    int[][] input;
    String[][] output;

    String spaceBy;

    File file;

    public KNN(File file,String spaceBy)
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

            fileInput = new String[maxColumns][maxRows];



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
                JOptionPane.showMessageDialog(null, "The file is not separated by " + spaceBy);
                return;
            }

            while((line = read.readLine())!=null)
            {

                content = line.split(spaceBy);
                for(int x=0;x<maxColumns;x++)
                {
                    fileInput[x][countRow] = content[x];
                }

                countRow++;
            }

            countColumn = 0;

            read.close();


            //assigning rows to input and outputs
            input = new int[maxColumns - outputCount][maxRows];
            output = new String[outputCount][maxRows];



            for(int cols=0;cols<maxColumns;cols++)
            {
                if(!titles[cols].contains("output"))
                {
                    for(int rows = 0;rows<maxRows;rows++)
                    {
                        input[colInput][rows] = Integer.parseInt(fileInput[cols][rows]);
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


    //K Nearest Neighbour
    public void KNNGo()
    {
        int nearestNeighbour = Integer.parseInt(JOptionPane.showInputDialog("You have "+maxRows+" elements\nEnter the number of nearest neighbours"));

        int[] voter = new int[nearestNeighbour];//those who qualify to be the nearest neighbours
        int[] diff = new int[maxColumns - outputCount];//value used to determine the nearest neighbours
        int min,minIndex;

        String inString = JOptionPane.showInputDialog("Enter "+(maxColumns-outputCount)+" elements separated by spaces");

        String[] inStringArray = inString.split(" ");

        int len = inStringArray.length;

        int[] inputKNN = new int[len];
        for(int x=0;x<len;x++)
        {
            inputKNN[x] = Integer.parseInt(inStringArray[x]);

            //calculating the differences
            for(int y=0;y<maxRows;y++)
            {
                diff[x] = (input[x][y]*input[x][y]) - (inputKNN[x]*inputKNN[x]);

                //if difference is 0 the exit
                if(diff[x] == 0)
                {
                    JOptionPane.showMessageDialog(null,"The prediction is"+output[0][y]);
                    return;
                }
            }
        }


        min = diff[0];//has the minimum value used to calculate order
        minIndex = 0;//has position of minimum value

        int y=0;//counter
        int voterIndex=0;

        int tempNeighbour = nearestNeighbour;
        while(true)
        {
            if((min == diff[y] || min>diff[y]) && diff[y]!=0)
            {
                min = diff[y];
                minIndex = y;
            }

            if(y+1==maxColumns)
            {
               y = 0;
               diff[minIndex] = 0;

                voter[voterIndex] = minIndex;
                voterIndex++;
            }
            tempNeighbour--;//when 0 means voters are enough
            if(tempNeighbour<0)
            {break;}
        }

        ArrayList<String> outputsList = new ArrayList<String>();

        for(y=0;y<maxRows;y++)
        {

            if(!outputsList.contains(output[0][y]))
            {
                outputsList.add(output[0][y]);
            }
        }

        String tempWInner="",temp;
        int tempWInnerVotes=0, tempVotes=0;

        for(y=0;y<outputsList.size();y++)
        {
            temp = outputsList.get(y);


            for(int v=0;v<nearestNeighbour;v++)
            {
                if(output[0][voter[v]].equals(temp))
                {

                    tempVotes++;
                }
            }

            if(tempVotes>tempWInnerVotes)
            {
                tempWInnerVotes = tempVotes;
                tempWInner = temp;
                tempVotes = 0;

            }
        }

        JOptionPane.showMessageDialog(null,"The new element belongs to "+tempWInner);

    }
}
