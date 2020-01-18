package com.github.frederik174.vls.line_simulation;


import java.time.Instant;
import java.time.ZoneId;

public class Vehicle {
    private String pin;

    Vehicle(Integer productionNumber, String plantID, String assemblySegment){
        this.pin = generatePin(productionNumber, plantID, assemblySegment);
    }

    private String generatePin(Integer productionNumber, String plantID, String assemblySegment){
        // plant id (2 digits)

        // production year
        Integer productionYear = Instant.now().atZone(ZoneId.of("Europe/Berlin")).getYear();
        // assembly segment

        // week
        Integer productionWeek = (Instant.now().atZone(ZoneId.of("Europe/Berlin")).getDayOfYear() + 1) / 7 +1;
        // production number (ongoing)
        String prodNumber = String.format("%04d", productionNumber);

        pin = plantID + productionYear.toString() + assemblySegment + productionWeek.toString() + prodNumber;

        // add a security number
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

    public String getPin(){return this.pin;}
}
