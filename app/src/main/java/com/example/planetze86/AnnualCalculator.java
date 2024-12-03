package com.example.planetze86;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.math.BigDecimal;
import java.math.RoundingMode;
import android.content.Context;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AnnualCalculator {
    private List<Questions> ques;
    private Context context;
    double Total;
    double transportation;
    double food;
    double housing;
    double consumption;
    double countryCompare;

    String country;

    public AnnualCalculator(List<Questions> ques, String country, Context context){
        this.ques = ques;
        this.context = context;
        this.country = country;
    }

    public double getTransportation(){
        transportation = 0;
        if(ques == null){
            return transportation;
        }
        if(ques.get(0).answer == 0){
            double[] CarsE = {0.24, 0.27, 0.16, 0.05,0.18};
            int[] dist = {5000, 10000, 15000,20000,25000,35000};
            transportation = transportation + CarsE[ques.get(1).answer]*dist[ques.get(2).answer];
        }
        int[][] publicT = {{0},{246,819,1638,3071,4095},{573,1911,3822,7166,9555},{573,1911,3822,7166,9555}};
        int ind = ques.get(3).answer;
        if(ind != 0){
            transportation = transportation + publicT[ind][ques.get(4).answer];
        }
        int[] shortFlight = {0,225,600,1200,1800};
        int[] longFlight = {0,825,2200,4400,6600};
        transportation = transportation + shortFlight[ques.get(5).answer];
        transportation = transportation + longFlight[ques.get(6).answer];
        return transportation/1000;
    }

    public double getFood(){
        food = 0;
        if(ques==null){
            return food;
        }
        if(ques.get(7).answer == 3){
//            8,9,10,11
            int[] beef = {2500,1900,1300,0};
            int[] pork = {1450,860,450,0};
            int[] chicken = {950,600,200,0};
            int[] fish = {800,500,150,0};
            food = food + beef[ques.get(8).answer] + pork[ques.get(9).answer] + chicken[ques.get(10).answer] + fish[ques.get(11).answer];
        }
        else{
            int[] veg = {1000,500,1500};
            food = food + veg[ques.get(7).answer];
        }

        double[] leftover = {0,23.4,70.2,140.4};
        food = food + leftover[ques.get(12).answer];

        return food/1000;
    }
    private double readExcel(int rowIndex,int colIndex) {
        try {
            // Load the .xlsx file from the raw folder
            InputStream inputStream = context.getResources().openRawResource(R.raw.formulas); // Replace 'your_file_name' with the name of your file (without extension)

            // Create a Workbook object
            Workbook workbook = new XSSFWorkbook(inputStream);

            // Get the 3 sheet (index 2)
            Sheet sheet = workbook.getSheetAt(2);
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                Cell cell = row.getCell(colIndex);
                if (cell != null) {
                    // Print the value of the cell
                    switch (cell.getCellType()) {
                        case STRING:
                            workbook.close();
                            inputStream.close();

                            return 0.0;
                        case NUMERIC:
                            workbook.close();
                            inputStream.close();
                            return cell.getNumericCellValue();

                        default:
                            workbook.close();
                            inputStream.close();
                            return 0.0;
                    }
                } else {
                    workbook.close();
                    inputStream.close();
                    return 0.0;
                }
            } else {
                workbook.close();
                inputStream.close();
                return 0.0;
            }

            // Close the workbook and input stream


        } catch (Exception e) {
            e.printStackTrace();

            return 0.0;
        }
    }
    public double getHousing(){
        housing = 0;
        int [][] home={{9,17,25},{33,41,49},{57,65,73},{81,90,98},{57,65,73}};
        int row = home[ques.get(13).answer][ques.get(15).answer] + ques.get(14).answer + 3;
        int[] heating = {2,7,12,17,22};

        if(ques.get(16).answer == 5){
            double num = 0;
            for(int i = 0 ; i < 5;i++){
                int col = heating[ques.get(17).answer] + i;
                num = num + readExcel(row,col);
            }
            housing= housing + num/5;

        }
        else{
            int col = heating[ques.get(17).answer] + ques.get(16).answer;
            housing = housing + readExcel(row,col);
        }
//        moving to water
        if(ques.get(18).answer == 4){
            housing = housing - 233;
        }
        else if(ques.get(18).answer != ques.get(16).answer){
            if(ques.get(18).answer == 1){
                housing = housing - 233;
            }
            else{
                housing = housing + 233;
            }
        }
        if(ques.get(19).answer == 0 ){
            housing = housing - 6000;
        }
        if(ques.get(19).answer == 1 ){
            housing = housing - 4000;
        }

        return housing/1000;
    }

    public double getConsumption() {
        consumption = 0;
        int[] newClothes = {360,120,100,5};
        if(ques.get(21).answer == 0){
            consumption = consumption + newClothes[ques.get(20).answer]*0.5;
        }
        else if(ques.get(21).answer == 1){
            consumption = consumption + newClothes[ques.get(20).answer]*0.7;
        }
        else{
            consumption = consumption + newClothes[ques.get(20).answer];
        }
        int[] devices = {0,300,600,1200};
        consumption = consumption + devices[ques.get(22).answer];

        if(ques.get(23).answer != 0){
            double[][] clothesrecycle = {{-54,-108,-180},{-18,-36,-60},{-15,-30,-50},{-0.75,-1.5,-2.5}};
            consumption = consumption + clothesrecycle[ques.get(20).answer][ques.get(23).answer-1];
            double[][] devicerecycle = {{0,0,0},{-45,-60,-90},{-60,-120,-180},{-120,-240,-360}};
            consumption = consumption + devicerecycle[ques.get(22).answer][ques.get(23).answer-1];
        }


        return consumption/1000;
    }

    public double getTotal() {
        Total= 0;
        Total = getTransportation() + getFood() + getHousing() + getConsumption();
        Total = Math.round(Total * 1000.0) / 1000.0;
        return Total;
    }





    private double readExcelCountry() {
        try {
            // Load the .xlsx file from the raw folder
            InputStream inputStream = context.getResources().openRawResource(R.raw.global); // Replace 'global' with the actual file name

            // Create a Workbook object
            Workbook workbook = new XSSFWorkbook(inputStream);

            // Get the first sheet (index 0)
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate through rows starting from row 3 (index 2)
            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    Cell countryCell = row.getCell(0); // Column A for country names
                    Cell emissionCell = row.getCell(1); // Column B for emissions

                    if (countryCell != null && countryCell.getCellType() == CellType.STRING) {
                        String countryName = countryCell.getStringCellValue().trim();

                        // Compare the country name with the global variable
                        if (countryName.equalsIgnoreCase(country)) {
                            if (emissionCell != null && emissionCell.getCellType() == CellType.NUMERIC) {
                                double emission = emissionCell.getNumericCellValue();

                                // Close resources
                                workbook.close();
                                inputStream.close();

                                return emission; // Return the population if found
                            }
                        }
                    }
                }
            }

            // Close resources if no match is found
            workbook.close();
            inputStream.close();
            return 0.0; // Return 0.0 if the country is not found
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0; // Return 0.0 if an exception occurs
        }
    }






    public double getCountryCompare() {
        countryCompare = readExcelCountry();
        double compareTo = getTotal();
        double percentage = (compareTo / countryCompare - 1) * 100;

        // Round to 2 decimal places using BigDecimal
        BigDecimal roundedPercentage = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP);
        return roundedPercentage.doubleValue();
    }

    public double getCountryEmission() {
        countryCompare = readExcelCountry();
        return countryCompare;
    }

    public double getGlobalCompare() {
        double compareTo = getTotal();
        double percentage = (compareTo / 2 - 1) * 100;

        // Round to 2 decimal places using BigDecimal
        BigDecimal roundedPercentage = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_UP);
        return roundedPercentage.doubleValue();
    }
}
