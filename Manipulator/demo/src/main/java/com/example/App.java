package com.example;

import javax.swing.JFrame;
import org.math.plot.*;
import java.lang.Math;
import java.awt.Color;
import java.util.Scanner;

import static org.math.array.DoubleArray.deleteColumnsRange;
import static org.math.array.DoubleArray.resize;
import org.math.array.DoubleArray;
import org.math.array.LinearAlgebra;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class App 
{
    //Code to render the manipulator.
    static double[][] vectors = {{0, 30, 0, 0}, 
                                 {0, 0, 30, 0}, 
                                 {0, 0, 0, 30}};


    static double phi1;
    static double phi2;
    static double phi3;

    static double l1 = 300;
    static double l2 = 300;
    static double l3 = 200;

    public static double[][] JointA(double[][] start_matrix){

        double tmp1[][] = LinearAlgebra.times(BasedMatrix.RotaciaZ(phi1), BasedMatrix.translaciaZ(l1));
        double jointA[][] = LinearAlgebra.times(tmp1, start_matrix);

        return jointA;
    }

    public static double[][] JointB(double[][] start_matrix){

        double tmp1[][] = LinearAlgebra.times(BasedMatrix.RotaciaZ(phi1), BasedMatrix.translaciaZ(l1));
        double tmp2[][] = LinearAlgebra.times(BasedMatrix.RotaciaY(phi2), BasedMatrix.translaciaZ(l2));
        tmp1 = LinearAlgebra.times(tmp1, tmp2);

        double[][] jointB = LinearAlgebra.times(tmp1, start_matrix);

        return jointB;
    }

    public static double[][] JointС(double[][] start_matrix){

        double tmp1[][] = LinearAlgebra.times(BasedMatrix.RotaciaZ(phi1), BasedMatrix.translaciaZ(l1));
        double tmp2[][] = LinearAlgebra.times(BasedMatrix.RotaciaY(phi2), BasedMatrix.translaciaZ(l2));
        double tmp3[][] = LinearAlgebra.times(BasedMatrix.RotaciaY(phi2+phi3), BasedMatrix.translaciaZ(l3));
        tmp1 = LinearAlgebra.times(tmp1, tmp2);
        tmp1 = LinearAlgebra.times(tmp1, tmp3);

        double[][] jointC = LinearAlgebra.times(tmp1, start_matrix);

        return jointC;
    }
    
    public static double[][] RobotArms(double[][] xyz, double[][] joint){
        for(int i = 0; i < xyz.length; i++){
            xyz[i][1] = joint[i][0];
        }

        return xyz;
    }
    
    public static void main( String[] args )
    {
        Scanner enter = new Scanner(System.in);

        System.out.println("Enter your phi1 in degrees: " );
        phi1 = Math.toRadians(enter.nextDouble());
        System.out.println("Enter your phi2 in degrees: " );
        phi2 = Math.toRadians(enter.nextDouble());
        System.out.println("Enter your phi3 in degrees: " );
        phi3 = Math.toRadians(enter.nextDouble());

        Plot3DPanel plot = new Plot3DPanel();
        

        double[][] xyz = {{0, 0}, 
                          {0, 0}, 
                          {0, 0}};
        double[][] joints = {{0, 0, 0}, 
                             {0, 0, 0}, 
                             {0, 0, 0}};


        double start_matrix[][] = resize(DoubleArray.getColumnsCopy(xyz, 0), 4, 1);
        start_matrix[3][0] = 1;

        //Point A
        double[][] jointA = JointA(start_matrix);

        xyz = RobotArms(xyz, jointA);

        for(int i = 0; i < xyz.length; i++){
            joints[i][0] = jointA[i][0];
        }

        plot.addLinePlot("my plot", Color.BLACK, xyz[0], xyz[1], xyz[2]);
        xyz = deleteColumnsRange(xyz, 0, 0);
        xyz = resize(xyz, 3, 2);

        //Point B
        double[][] jointB = JointB(start_matrix);

        xyz = RobotArms(xyz, jointB);

        for(int i = 0; i < xyz.length; i++){
            joints[i][1] = jointB[i][0];
        }

        plot.addLinePlot("my plot", Color.BLACK, xyz[0], xyz[1], xyz[2]);
        xyz = deleteColumnsRange(xyz, 0, 0);
        xyz = resize(xyz, 3, 2);
        
        //Point C
        double[][] jointC = JointС(start_matrix);

        xyz = RobotArms(xyz, jointC);

        for(int i = 0; i < xyz.length; i++){
            joints[i][2] = jointC[i][0];
        }
        
        joints = LinearAlgebra.transpose(joints);

        plot.addLinePlot("my plot", Color.BLACK, xyz[0], xyz[1], xyz[2]);
        plot.addScatterPlot("joints", Color.BLACK, joints[0], joints[1], joints[2]);
        plot.addLabel("A", Color.BLACK, LinearAlgebra.plus(joints[0], 10));
        plot.addLabel("B", Color.BLACK, LinearAlgebra.plus(joints[1], 10));
        plot.addLabel("C", Color.BLACK, LinearAlgebra.plus(joints[2], 10));
        plot.setFixedBounds(0, 0, 400);
        plot.setFixedBounds(1, 0, 400);
        plot.setFixedBounds(2, 0, 400);

        JFrame frame = new JFrame("Manipulator");
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
        
        

        WorkSpace.Print();


    }
}
