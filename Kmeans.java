package sample;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class Kmeans
{
    Kmeans()
    {
        String nothing[] = new String[2];
        main(nothing);
    }

    static int count1,count2,count3;
    static int elements[];
    static int k[][];
    static int tempk[][];
    static double m[];
    static double diff[];
    static int numberOfElements,p;

    static int cal_diff(int a) // This method will determine the cluster in which an element go at a particular step.
    {
        int temp1=0;

        for(int i=0;i<p;++i)
        {
            if(a>m[i])
            diff[i]=a-m[i];

            else
            diff[i]=m[i]-a;
        }

        int val=0;
        double temp=diff[0];

        for(int i=0;i<p;++i)
        {
            if(diff[i]<temp)
            {
                temp=diff[i];
                val=i;
            }
        }//end of for loop
        return val;
    }

    static void cal_mean() // This method will determine intermediate mean values
    {
        for(int i=0;i<p;++i)
        m[i]=0; // initializing means to 0

        int cnt=0;
        for(int i=0;i<p;++i)
        {
            cnt=0;

            for(int j=0;j< numberOfElements -1;++j)
            {
                if(k[i][j]!=-1)
                {
                    m[i]+=k[i][j];
                    ++cnt;
                }
            }
            m[i]=m[i]/cnt;
        }
    }

    static int check1() // This checks if previous k ie. tempk and current k are same.Used as terminating case.
    {
        for(int i=0;i<p;++i)
        for(int j=0;j< numberOfElements;++j)
        if(tempk[i][j]!=k[i][j])
        {
            return 0;
        }
        return 1;
    }

    public static void main(String args[])
    {

        numberOfElements = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of elements "));
        elements=new int[numberOfElements];

        /* Accepting elements */

        for(int i=0;i< numberOfElements;++i)
            elements[i]=Integer.parseInt(JOptionPane.showInputDialog("Enter element "+(i+1)));

        /* Accepting num of clusters */
        p=Integer.parseInt(JOptionPane.showInputDialog("Enter the number of clusters"));;


        /* Initialising arrays */
        k=new int[p][numberOfElements];
        tempk=new int[p][numberOfElements];
        m=new double[p];
        diff=new double[p];
        /* Initializing m */
        for(int i=0;i<p;++i)
        m[i]=elements[i];

        int temp=0;
        int flag=0;
        do
        {
            for(int i=0;i<p;++i)
            for(int j=0;j< numberOfElements;++j)
           {
            k[i][j]=-1;
           }
        for(int i=0;i< numberOfElements;++i) // for loop will cal cal_diff(int) for every element.
        {
            temp=cal_diff(elements[i]);

            System.out.println(temp+" "+count2+" "+i);

            if(temp==0)
            k[temp][count1++]=elements[i];



            else
            if(temp==1)
            k[temp][count2++]=elements[i];

            else
            if(temp==2)

            k[temp][count3++]=elements[i];
        }
        cal_mean(); // call to method which will calculate mean at this step.

        flag=check1(); // check if terminating condition is satisfied.

        if(flag!=1)
        /*Take backup of k in tempk so that you can check for equivalence in next step*/
        for(int i=0;i<p;++i)
        for(int j=0;j< numberOfElements;++j)
        tempk[i][j]=k[i][j];

        count1=0;count2=0;count3=0;
        }
        while(flag==0);

        try {
            PrintWriter write = new PrintWriter(new File("/home/sammi/Desktop/AI_Output/kMeanOutput"));


        JOptionPane.showMessageDialog(null,"/home/sammi/Desktop/AI_Output/kMeanOutput");
        for(int i=0;i<p;++i)
        {
            write.print("Cluster "+(i+1)+"{ ");
            for(int j=0;k[i][j]!=-1 && j< numberOfElements -1;++j)
                write.print(k[i][j]+" ");

            write.print("}");
            write.println();
        }

            write.println("");
            write.flush();
            write.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
