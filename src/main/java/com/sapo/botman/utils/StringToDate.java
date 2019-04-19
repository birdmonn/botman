package com.sapo.botman.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StringToDate {
    private static StringToDate instance;

    public static StringToDate getInstance() {
        if (instance == null) {
            instance = new StringToDate();
        }
        return instance;
    }

    public List<Date> dateAToDateB(String dateA,String dateB) throws ParseException {
      try {
          List<Date> dateList = new ArrayList<>();
          Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse(dateA);
          dateList.add(dateStart);
          Date dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse(dateB);
          Calendar c = Calendar.getInstance();
          Date tempDate = dateStart;
          while (tempDate.compareTo(dateEnd) != 0) {
              c.setTime(tempDate);
              c.add(Calendar.DATE, 1);
              tempDate = c.getTime();
              dateList.add(tempDate);
          }
          return dateList;
      } catch (ParseException e){
          return new ArrayList<>();
      }
    }

    public List<Date> dateA(String dateA) throws ParseException {
        try {
            List<Date> dateList = new ArrayList<>();
            Date dateStart = new SimpleDateFormat("dd/MM/yyyy").parse(dateA);
            dateList.add(dateStart);
            return dateList;
        } catch (ParseException e){
            return new ArrayList<>();
        }
    }
}
