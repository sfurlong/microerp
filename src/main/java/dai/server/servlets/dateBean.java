
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Your description

package dai.server.servlets;

import javax.swing.JPanel;
//import dai.shared.csAdapters.*;
//import dai.shared.cmnSvcs.*;

public class dateBean extends JPanel
{

	public dateBean()
	{
	}


        //returns last day of previous month as string "MM/DD/YYYY"
	public String prevMonthEnd (String endDate)
	{
	  int temp = 0;
          int numMonth = 0;
          int numYear = 0;
          String day = "";
          String month = "";
          String year = "";
          String prevEnd = "";

	  temp = endDate.indexOf("/",0);
          month = endDate.substring(0, temp);
          temp = endDate.lastIndexOf("/");
          year = endDate.substring ((temp+1));
          numMonth = Integer.parseInt(month);
          numMonth = numMonth - 1;
          numYear = Integer.parseInt(year);

          if (numMonth==0)    //find number of previous month
          {
            numMonth = 11;
            numYear = numYear - 1;
          }
          else
            numMonth = numMonth - 1;

          day = lastDay(numMonth, numYear);
          if (numMonth < 9)
            prevEnd = "0" + (numMonth+1) + "/" + day + "/" + numYear;
          else
            prevEnd = "" + (numMonth+1) + "/" + day + "/" + numYear;

          return prevEnd;
	}


        //returns the number of days in a given month
        public String lastDay(int month, int year) //**month is number from 0-11
        {
          String lastDays = "";
          String febDays = "";

          if ((((year%4)==0) && (year%100)!=0) || ((year%400)==0))
            febDays = "29";
          else
            febDays = "28";

          switch (month)
          {
            case 1 : lastDays = febDays; break;
            case 3 : lastDays = "30"; break;
            case 5 : lastDays = "30"; break;
            case 8 : lastDays = "30"; break;
            case 10: lastDays = "30"; break;
            default: lastDays = "31";
          }          

          return lastDays;
        }
}