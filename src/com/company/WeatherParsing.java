package com.company;

import java.io.*;

import java.util.*;

public class WeatherParsing {
    private String formatDate(String date){
        String year = date.substring(0,4);
        String month = date.substring(4,6);
        String day = date.substring(6,8);
        String hour = date.substring(9,11);
        String minutes = date.substring(11,13);

        return day + "." + month + "." + year + " at " + hour + ":" + minutes;
    }

    public void csvWeatherParsing(String path){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path));
            ArrayList<String[]> dataLines = new ArrayList<>();
            String line;
            double averageTemperature = 0.0;
            int averageHumidity = 0;
            double averageWindSpeed = 0.0;
            double highestTemperature = Double.MIN_VALUE;
            double lowestHumidity = Double.MAX_VALUE;
            double highestWindSpeed = Double.MIN_VALUE;
            int countOfNorthWindDirection = 0;
            int countOfSouthWindDirection = 0;
            int countOfWestWindDirection = 0;
            int countOfEastWindDirection = 0;
            String dateAndTimeWithHighestTemperature = "";
            String dateAndTimeWithLowestHumidity = "";
            String dateAndTimeWithHighestWindSpeed = "";
            String mostFrequentWindDirection = "";
            int counterOfLines = 0;

            while((line = reader.readLine()) != null){
                dataLines.add(line.split("\n"));
            }
            reader.close();
            for (int i = 10; i < dataLines.size() ; i++) {
                String[] currLine = dataLines.get(i)[0].split(",");
                String currDate = currLine[0];
                double currWindDirection = Double.parseDouble(currLine[4]);
                double currTemperature = Double.parseDouble(currLine[1]);
                double currHumidity = Double.parseDouble(currLine[2]);
                double currWindSpeed = Double.parseDouble(currLine[3]);

                averageTemperature += currTemperature;
                averageHumidity += currHumidity;
                averageWindSpeed += currWindSpeed;
                if(currWindDirection >= 315 && currWindDirection < 45 ){
                    countOfNorthWindDirection +=1;
                } else if(currWindDirection >= 45 && currWindDirection < 135){
                    countOfEastWindDirection += 1;
                } else if(currWindDirection >= 135 && currWindDirection < 225){
                    countOfSouthWindDirection += 1;
                } else if(currWindDirection >= 225 && currWindDirection < 315){
                    countOfWestWindDirection += 1;
                }

                if(currTemperature > highestTemperature){
                    highestTemperature = currTemperature;
                    dateAndTimeWithHighestTemperature = currDate;
                }
                if(currHumidity < lowestHumidity){
                    lowestHumidity = currHumidity;
                    dateAndTimeWithLowestHumidity = currDate;
                }
                if(currWindSpeed > highestWindSpeed){
                    highestWindSpeed = currWindSpeed;
                    dateAndTimeWithHighestWindSpeed = currDate;
                }
                counterOfLines++;
            }

            HashMap<Integer, String> windDirectionMap = new HashMap<>();
            windDirectionMap.put(countOfEastWindDirection,"East");
            windDirectionMap.put(countOfNorthWindDirection,"North");
            windDirectionMap.put(countOfWestWindDirection,"West");
            windDirectionMap.put(countOfSouthWindDirection,"South");
            int keyWithMostFrequentWD = Collections.max(windDirectionMap.keySet());
            mostFrequentWindDirection = windDirectionMap.get(keyWithMostFrequentWD);
            averageTemperature /= counterOfLines;
            averageHumidity /= counterOfLines;
            averageWindSpeed /= counterOfLines;

            makeOutputFileWithAnalysedData(averageTemperature,averageHumidity,averageWindSpeed,dateAndTimeWithHighestTemperature,
                    dateAndTimeWithHighestWindSpeed, dateAndTimeWithLowestHumidity, mostFrequentWindDirection);
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    private void makeOutputFileWithAnalysedData(double averageTemperature, int averageHumidity, double averageWindSpeed,
                                   String dateAndTimeWithHighestTemperature, String dateAndTimeWithHighestWindSpeed,
                                   String dateAndTimeWithLowestHumidity, String mostFrequentWindDirection) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("analysedDataOutput.txt"));
            String output = "Analysed data...\n" +
                    "Average temperature is " + averageTemperature + " \n" +
                    "Average humidity is " + averageHumidity + " \n" +
                    "Average wind speed is " + averageWindSpeed + " \n" +
                    "Date with highest temperature: " + formatDate(dateAndTimeWithHighestTemperature) + "\n" +
                    "Date with lowest humidity: " + formatDate(dateAndTimeWithLowestHumidity) + "\n" +
                    "Date with highest wind speed: " + formatDate(dateAndTimeWithHighestWindSpeed) + "\n" +
                    "The most frequent wind direction: " + mostFrequentWindDirection;
            writer.write(output);
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
