package com.example;


public class BasedMatrix {
    //Transformation matrix
    static double[][] translaciaZ(double l){
        double Tz[][] = {{1, 0, 0, 0}, 
                         {0, 1, 0, 0}, 
                         {0, 0, 1, l}, 
                         {0, 0, 0, 1}};
        return Tz;
    } 
    //Rotation matrix around the Z axis
    static double[][] RotaciaZ(double phi){
        double Rz[][] = {{Math.cos(Math.PI/2.0 - phi), -Math.sin(Math.PI/2.0 - phi), 0, 0}, 
                         {Math.sin(Math.PI/2.0 - phi), Math.cos(Math.PI/2.0 - phi), 0, 0}, 
                         {0, 0, 1, 0}, 
                         {0, 0, 0, 1}};
        return Rz;
    } 
    //Rotation matrix around the Y axis
    static double[][] RotaciaY(double phi){
        double Ry[][] = {{Math.cos(phi), 0, Math.sin(phi), 0}, 
                         {0, 1, 0, 0}, 
                         {-Math.sin(phi), 0, Math.cos(phi), 0}, 
                         {0, 0, 0, 1}};
        return Ry;
    } 
    
}
