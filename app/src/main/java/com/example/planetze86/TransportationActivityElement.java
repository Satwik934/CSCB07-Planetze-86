package com.example.planetze86;

public class TransportationActivityElement extends EmissionActivityElement {
    private String transportMode;
    private double distanceOrDurationOrFlightCount;
    private String additionalDetails;

    public TransportationActivityElement(String date, String transportMode, double distanceOrDurationOrFlightCount, String additionalDetails) {
        super(date, "Transportation"); // Set type field
        this.transportMode = transportMode;
        this.distanceOrDurationOrFlightCount = distanceOrDurationOrFlightCount;
        this.additionalDetails = additionalDetails;
    }

    public TransportationActivityElement() {
        super(); // Required for Firebase
    }

    // Getters, setters, and overrides remain unchanged



    // Getters and Setters
    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public double getDistanceOrDurationOrFlightCount() {
        return distanceOrDurationOrFlightCount;
    }

    public void setDistanceOrDurationOrFlightCount(double distanceOrDurationOrFlightCount) {
        this.distanceOrDurationOrFlightCount = distanceOrDurationOrFlightCount;
    }

    public String getAdditionalDetails() {
        return additionalDetails;
    }

    public void setAdditionalDetails(String additionalDetails) {
        this.additionalDetails = additionalDetails;
    }

    @Override
    public String getDetails() {
        String durDistNum = "Distance: ";
        String minKm = "km";
        switch (transportMode){
            case "Public Transportation": {durDistNum = "Duration: "; minKm = " min";break;}
            case "Flight": {durDistNum = "Count: "; minKm = ""; break;}
        }

        return "Mode: " + transportMode + ", " +
                durDistNum +
                distanceOrDurationOrFlightCount + minKm +
                ", Details: " + additionalDetails;
    }

    @Override
    public double getEmissions() {
        double emissions = 0.0; // Default value
        switch (transportMode) {
            case "Personal Vehicle": {
                switch (additionalDetails) {
                    case "Gasoline":
                        emissions = distanceOrDurationOrFlightCount * 0.24;
                        break;
                    case "Diesel":
                        emissions = distanceOrDurationOrFlightCount * 0.27;
                        break;
                    case "Hybrid":
                        emissions = distanceOrDurationOrFlightCount * 0.16;
                        break;
                    case "Electric":
                        emissions = distanceOrDurationOrFlightCount * 0.05;
                        break;
                    default:
                        emissions = 0.18; // Unknown vehicle type
                }
                break;
            }
            case "Public Transportation": {
                //Assuming ratio Bus:Train:Subway = 3:1.1:1.1
                double averageForPublicTransport = 1911.0/2.0/52.0/6.0;
                if (additionalDetails.equals("Bus")) {
                    emissions = distanceOrDurationOrFlightCount * averageForPublicTransport/5.2*3; // * 6timesaweek1hourinayear/52weeks/6daysperweek then scale
                } else if (additionalDetails.equals("Train")) {
                    emissions = distanceOrDurationOrFlightCount * averageForPublicTransport/5.2*1.1; // Placeholder value
                } else if (additionalDetails.equals("Subway")) {
                    emissions = distanceOrDurationOrFlightCount * averageForPublicTransport/5.2*1.1; // Placeholder value
                }
                break;
            }
            case "Cycling": {
                emissions = 0.0; // Zero emissions for cycling
                break;
            }
            case "Walking": {
                emissions = 0.0; // Zero emissions for walking
                break;
            }
            case "Flight": {
                if (additionalDetails.equals("Short-Haul")) {
                    emissions = distanceOrDurationOrFlightCount * 225/3/2; // 1.5 Flights a year is 225kg
                } else if (additionalDetails.equals("Long-Haul")) {
                    emissions = distanceOrDurationOrFlightCount * 825/3/2; // 1.5 Flights a year is 825kg
                }
                break;
            }
            default:
                emissions = 0.0; // Unknown mode of transportation
        }
        return emissions;
    }

}
