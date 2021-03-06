package com.github.frederik174.simulation;


import java.time.Instant;
import java.time.ZoneId;

public class Vehicle {
    private String PIN;
    private String VIN;

    public Vehicle(int prodNumber){
        this.PIN = generatePin(prodNumber);
        this.VIN = generateVin(prodNumber);
    }

    private String generatePin(int prodNumber){
        // production year
        Integer productionYear = Instant.now().atZone(ZoneId.of("Europe/Berlin")).getYear();

        // calendar week
        String productionWeek = String.format("%02d", ((Instant.now().atZone(ZoneId.of("Europe/Berlin")).getDayOfYear() + 1) / 7 +1));
        // production number (ongoing)
        String productionNumber = String.format("%04d", prodNumber);

        String pin = "12" + productionYear.toString() + "3" + productionWeek + productionNumber;

        // security number
        Integer securityNumber = calculateSecurityNumber(pin);
        pin = pin + securityNumber.toString();

        return pin;
    }

    private Integer calculateSecurityNumber(String pin){
        // replace first digit
        Character[] letters = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        String[] numbers = {"1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8"};

        for(int idx = 0; idx < letters.length - 1; idx++){
            if(pin.charAt(0) == letters[idx]){
                pin = numbers[idx] + pin.substring(1, pin.length());
                break;
            }
        }

        // calculate security number
        Integer sumEvenNumbers = 0;
        Integer sumOddNumbers = 0;

        for(int idx = 0; idx < pin.length(); idx++){
            if(idx % 2 == 0){
                sumOddNumbers += Integer.parseInt(String.valueOf(pin.charAt(idx))) * 3;
            }else{
                sumEvenNumbers += Integer.parseInt(String.valueOf(pin.charAt(idx)));
            }
        }
        Integer securityNumber = (sumEvenNumbers + sumOddNumbers) % 10;
        if (securityNumber != 0){
            securityNumber = 10 - securityNumber;
        }
        return securityNumber;
    }

    private String generateVin(int prodNumber){
        // WMI code (VW2 for Volkswagen Commercial Vehicles with models including T6)
        // Z is a placeholder
        // Model code
        // Model year "L" indicates 2020
        // plant code "H" indicates Hannover
        // 7HA -> "Kasten" 7HB -> Kombi/Multivan
        String productionNumber = String.format("%05d", prodNumber);
        String vin = "WV2" + "ZZZ" + "7HB" + "ZLH" + productionNumber;
        return vin;
    }

    public String getPin(){return this.PIN;}
    public String getVin(){return this.VIN;}
}
