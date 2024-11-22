package com.example.planetze86;

import java.util.ArrayList;
import java.util.List;

public class AnnualCalculator {
    private ArrayList<Questions> ques;
    double Total;
    double transport;
    double food;
    double housing;
    double consumption;
    double countrycompare;

    String country;

    public AnnualCalculator(ArrayList<Questions> ques,String country){
        this.ques = ques;
        this.country = country;
    }

    public double getTransport(){
        if(ques.get(0).answer == 0){
            double[] CarsE = {0.24, 0.27, 0.16, 0.05,0.18};
            int[] dist = {5000, 10000, 15000,20000,25000,35000};
            transport = transport + CarsE[ques.get(1).answer]*dist[ques.get(2).answer];
        }
        


        return transport;
    }



}
