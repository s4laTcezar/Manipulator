package com.example;
import java.awt.Color;

import javax.swing.JFrame;

import org.math.array.LinearAlgebra;
import org.math.plot.Plot2DPanel;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WorkSpace {

    static double phi1_min;
    static double phi1_max;
    static double phi2_min;
    static double phi2_max;
    static double phi3_min;
    static double phi3_max;

    public static double[][] workspace(double[] angles, double[] length)
    {
        //Finds all manipulator endpoints
        double result [][] = {{0}, {0}, {0}, {1}};
        double [][]temp = LinearAlgebra.times(BasedMatrix.RotaciaZ( angles[0]), BasedMatrix.translaciaZ( length[0]));
        temp = LinearAlgebra.times(temp, LinearAlgebra.times(BasedMatrix.RotaciaY( angles[1]), BasedMatrix.translaciaZ(  length[1])));
        temp = LinearAlgebra.times( temp, LinearAlgebra.times(BasedMatrix.RotaciaY( angles[2]), BasedMatrix.translaciaZ(  length[2])));

        result = LinearAlgebra.times(temp, result);


        result = LinearAlgebra.deleteRowsRange(result, 3, 3);



        return result;
    }

    public static void Print(){
        //Draws graphs of workspaces.
        double[] length = {App.l1, App.l2, App.l3};

        int counter = 0;

        Scanner enter = new Scanner(System.in);

        System.out.println("Enter your minimum phi1 in degrees (for workspace): " );
        phi1_min = enter.nextDouble();
        System.out.println("Enter your maximum phi1 in degrees (for workspace): " );
        phi1_max = enter.nextDouble();
        System.out.println("Enter your minimum phi2 in degrees (for workspace): " );
        phi2_min = enter.nextDouble();
        System.out.println("Enter your maximum phi2 in degrees (for workspace): " );
        phi2_max = enter.nextDouble();
        System.out.println("Enter your minimum phi3 in degrees (for workspace): " );
        phi3_min = enter.nextDouble();
        System.out.println("Enter your maximum phi3 in degrees (for workspace): " );
        phi3_max = enter.nextDouble();

        int c1 = WorkSpaceXZ(length, counter, phi2_min, phi3_min, phi2_max, phi3_max);
        int c2 = WorkSpaceXY(length, counter, phi1_min, phi2_min, phi3_min, phi1_max, phi2_max, phi3_max);

        Plot2DPanel plot1 = new Plot2DPanel();
        Plot2DPanel plot2 = new Plot2DPanel();
        RedFromTheFileXZ("pointsXZ.csv", c1, "XZ", plot2);
        RedFromTheFileXY("pointsXY.csv", c2, "XY", plot1);
    }

    static public String[] WritePoints(double phi1, double phi2, double phi3, double[] length){

        //Writes all coordinates to the strings array.
        double[] angles = {Math.toRadians(phi1), Math.toRadians(phi2), Math.toRadians(phi3)};
        double[][] dot = workspace(angles, length);
        String x_coord = Double.toString(dot[0][0]);
        String y_coord = Double.toString(dot[1][0]);
        String z_coord = Double.toString(dot[2][0]);
        String[] record = {x_coord, y_coord, z_coord};

        return record;
    }
    
    static public int WorkSpaceXZ(double[] length, int counter, double phi2_min, double phi3_min, double phi2_max, double phi3_max){
        //Writes coordinates in the XZ plane to the file
        counter = 0;
        try (CSVWriter writer = new CSVWriter(new FileWriter("pointsXZ.csv"))) {
            for(double phi2_tmp = phi2_min; phi2_tmp <= 130; phi2_tmp+=1){
                for(double phi3_tmp = phi2_min; phi3_tmp <= 60; phi3_tmp+=1){
                    
                    writer.writeNext(WritePoints(0, phi2_tmp, phi3_tmp, length));
                    counter++;

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return counter;
    }

    static public int WorkSpaceXY(double[] length, int counter, double phi1_min, double phi2_min, double phi3_min, double phi1_max, double phi2_max, double phi3_max){
        //Writes coordinates in the XY plane to the file
        counter = 0;
        try (CSVWriter writer = new CSVWriter(new FileWriter("pointsXY.csv"))) {
            for(double phi1_tmp = phi1_min; phi1_tmp <= phi1_max; phi1_tmp+=2){
                for(double phi2_tmp = phi2_min; phi2_tmp <= phi2_max; phi2_tmp+=2){
                    for(double phi3_tmp = phi3_min; phi3_tmp <= phi3_max; phi3_tmp+=2){
                        
                        writer.writeNext(WritePoints(phi1_tmp, phi2_tmp, phi3_tmp, length));
                        counter++;

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return counter;
    }
    
    public static void RedFromTheFileXY(String namefile, int counter, String Axes, Plot2DPanel plot){
        // Reads coordinates from the file and writes them to the variable plot, area XY.
        double[] PointsX = new double[3*counter];
        double[] PointsY = new double[3*counter];
        double[] PointsZ = new double[3*counter];
        int counter_array_input = 0;

        try (CSVReader reader = new CSVReader(new FileReader(namefile))) {
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                
                PointsX[counter_array_input] = Double.parseDouble(line[0]);
                PointsY[counter_array_input] = Double.parseDouble(line[1]);
                PointsZ[counter_array_input] = Double.parseDouble(line[2]);
                counter_array_input ++;
                
                
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }


        plot.addScatterPlot("myplot", Color.ORANGE, PointsX, PointsY);
        plot.setFixedBounds(0, -500,500);
        
        plot.setAxisLabel(0, "X [mm]");
        plot.setAxisLabel(1, "Y [mm]");

        JFrame frame = new JFrame("XY - area");
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        frame.setSize(800, 600);

        frame.setContentPane(plot);
        frame.setVisible(true);



    }

    public static void RedFromTheFileXZ(String namefile, int counter, String Axes, Plot2DPanel plot){
        // Reads coordinates from the file and writes them to the variable plot area XZ.

        double[] PointsX = new double[3*counter];
        double[] PointsY = new double[3*counter];
        double[] PointsZ = new double[3*counter];

        int counter_array_input = 0;

        try (CSVReader reader = new CSVReader(new FileReader(namefile))) {
            String[] line;
            
            while ((line = reader.readNext()) != null) {
                
                PointsX[counter_array_input] = Double.parseDouble(line[0]);
                PointsY[counter_array_input] = Double.parseDouble(line[1]);
                PointsZ[counter_array_input] = Double.parseDouble(line[2]);
                counter_array_input ++;
                
                
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }

        plot.addScatterPlot("myplot", Color.ORANGE, PointsX, PointsZ);
        plot.setAxisLabel(0, "X [mm]");
        plot.setAxisLabel(1, "Z [mm]");

        JFrame frame = new JFrame("XZ - area");

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
        
        frame.setSize(800, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);


    }

    
}
