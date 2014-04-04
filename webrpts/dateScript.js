//calculates the last day of a given month - # from 0 to 11
function lastDay(month, year)
{
  var febDays;
  var lastDays;

  if ((((year%4)==0) && (year%100)!=0) || ((year%400)==0))
    febDays = 29;
  else
    febDays = 28;

  switch (month)
  {
    case 1 : lastDays = febDays; break;
    case 3 : lastDays = 30; break;
    case 5 : lastDays = 30; break;
    case 8 : lastDays = 30; break;
    case 10: lastDays = 30; break;
    default: lastDays = 31;
  }
  return lastDays;
}//end lastDay



//places the date range selected in the text fields
function showDates(dateRange, parm0, parm1)
{
  var range = dateRange.value;
  var from = getFromDate(range);
  var to = getToDate(range);

  parm0.value=from;
  parm1.value=to;
}//end ShowDates



//calculates the starting date of selected date range
function getFromDate(range)
{
  //set today's date
  var now   = new Date();
  var day   = now.getDate();
  var month = now.getMonth();
  var year  = now.getYear();
  var lmonth;
  var lyear;
    
  if (year < 100)
    year += 1900; 

  if (month == 0)
  {
    lmonth = 11;
    lyear = (year-1);
  }
  else
  {
    lmonth = (month - 1);
    lyear = year;
  }

  switch (range)
  {
    case "jan": return "01/01/" + year;
    case "feb": return "02/01/" + year;
    case "mar": return "03/01/" + year;
    case "apr": return "04/01/" + year;
    case "may": return "05/01/" + year;
    case "jun": return "06/01/" + year;
    case "jul": return "07/01/" + year;
    case "aug": return "08/01/" + year;
    case "sep": return "09/01/" + year;
    case "oct": return "10/01/" + year;
    case "nov": return "11/01/" + year;
    case "dec": return "12/01/" + year;
    case "lastmonth" : return ""+ (lmonth+1) + "/01/" + lyear;
    case "curryear" : return "01/01/" + year;
    case "currmonth" : return "" + (month+1) + "/01/" + year;
    default : return "" + (month+1) +"/" + day + "/" + year;
  }//end switch
}//end getFromDate



//calcultes the end date of selected date range
function getToDate(range)
{
  // set today's date
  var now   = new Date();
  var day   = now.getDate();
  var month = now.getMonth();
  var year  = now.getYear();
  var lmonth;

  //determine # of days in each month
  if (year < 100)
  {
    year += 1900;
  }

  if (month == 0)
  {
    lmonth = 11;
    lyear = (year-1);
  }
  else
  {
    lmonth = (month - 1);
    lyear=year;
  }
 
  switch (range)
  {
    case "jan": return "01/31/" + year;
    case "feb": return "02/" + lastDay(1, year) +"/" + year;
    case "mar": return "03/31/" + year;
    case "apr": return "04/30/" + year;
    case "may": return "05/31/" + year;
    case "jun": return "06/30/" + year;
    case "jul": return "07/31/" + year;
    case "aug": return "08/31/" + year;
    case "sep": return "09/30/" + year;
    case "oct": return "10/31/" + year;
    case "nov": return "11/30/" + year;
    case "dec": return "12/31/" + year;
    case "lastmonth" : return ""+ (lmonth+1) + "/" + lastDay(lmonth, lyear) + "/" + lyear;
    default : return "" + (month+1) + "/" + day + "/" + year;
  }//end range switch
}//end getToDate