package com.example.roommate.values.forms;

public record SearchTimeForm (String datum,String startUhrzeit, String endUhrzeit){

    public SearchTimeForm(String datum, String startUhrzeit, String endUhrzeit) {
        if (datum == null) datum = "2024-01-01";
        if (startUhrzeit == null) startUhrzeit = "08:00";
        if (endUhrzeit == null) endUhrzeit = "16:00";

        this.datum = datum;
        this.startUhrzeit = startUhrzeit;
        this.endUhrzeit = endUhrzeit;
    }
   /* public SearchTimeForm(){}*/
//    public void setDatum(String datum){
//        this.datum = datum;
//    }
//
//    public void setStartUhrzeit(String startUhrzeit){
//        this.startUhrzeit = startUhrzeit;
//    }
//
//    public void setEndUhrzeit(String endUhrzeit){
//        this.endUhrzeit = endUhrzeit;
//    }


//    public String getDatum() {
//        return datum;
//    }
//
//    public String getStartUhrzeit() {
//        return startUhrzeit;
//    }
//
//    public String getEndUhrzeit() {
//        return endUhrzeit;
//    }
}
