
//Title:        eCorp
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:
package dai.server.servlets;

import dai.shared.businessObjs.*;
import dai.shared.cmnSvcs.*;
import dai.shared.csAdapters.*;

import java.util.*;

public class HTMLGenerator
{

	public HTMLGenerator() {
	}

        public String[] getColumnHeadings (csSecurity security, String sqlStmt)
        {
          int count=0;
          int index=0;
          String temp=""+sqlStmt.trim();
          String temp2[]=new String[80];

          temp=temp.substring(temp.indexOf(" "), temp.indexOf("from")).trim();
          index=temp.indexOf(",");

          while (index!=-1)
          {
            temp2[count]=temp.substring(0, index);
            count++;
            temp=temp.substring(index+2, temp.length());
            index=temp.indexOf(",");
          }

          temp2[count]=temp.substring(0, temp.length());
          count++;

          if (temp2[0].trim().equals("*"))
          {
             temp=sqlStmt.substring(sqlStmt.indexOf("from"), sqlStmt.length());
             temp=temp.substring(temp.indexOf(" ")+1, temp.length());

             try {
               DBAttributes columnNames[]={};
               csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
	       csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
               columnNames= dbAdapter.getColumnNames(security, temp);
               String headings[]=new String[columnNames.length];

               for (int i=0; i<columnNames.length;i++)
               {
                 //headings[i]=columnNames[i].getName();
		 temp = "" + columnNames[i].getName().substring(0, 1);
                 temp = temp.toUpperCase() + columnNames[i].getName().substring(1).toLowerCase();
                 headings[i] = temp;
               }
               return headings;
             } catch (Exception e) {
               e.printStackTrace();
             }
          }
          else
          {
            String headings[]=new String[count];
            for (int i=0; i<count; i++)
            {
              //headings[i]=temp2[i];
	      temp = "" + temp2[i].substring(0, 1);
              temp = temp.toUpperCase() + temp2[i].substring(1).toLowerCase();
              headings[i]=temp;
            }
            return headings;
          }
          return null;
        }

	public String createSimpleTableFromVect(Vector rowsVect, String[] headings)
	{
		int numRows = rowsVect.size();
		int numCols = 0;
		Vector colsVect;

		String tableHtml = "<TABLE border=1 width=85%><TR bgcolor=darkblue>";

                for (int i=0; i < headings.length; i++)
                {
                    tableHtml +="<TD><FONT color=white><B><center><U>"+headings[i]+"</U></center></B></FONT></TD>";
                }
                tableHtml+="</TR>";

	    for (int i=0; i<numRows; i++)
		{
			colsVect = (Vector)rowsVect.elementAt(i);

			numCols = colsVect.size();

			//Print the Detail Row Columns
			tableHtml = tableHtml + "<TR>";

			for (int j=0; j<numCols; j++)
			{
				//Write table columns for this row.
				tableHtml = tableHtml +
							"<TD><FONT size=2>" + colsVect.elementAt(j) + "</FONT></TD>";
			}
			//End of table row.
			tableHtml = tableHtml + "</TR>";
		}

		tableHtml = tableHtml + "</TABLE>";

		return tableHtml;
	}

	public String createTable(Vector rowsVect, ParseXMLRptData xmlRptData)
	{
		String[] headings = xmlRptData.getColHeadings();
		String[] colFormatting = xmlRptData.getColFormatting();
		int[]    detailColNums = xmlRptData.getDetailColNums();
		int[]    groupColNums = xmlRptData.getGroupColNums();
		int[]    groupSumColNums = xmlRptData.getGroupSumColNums();
		double[] groupSumColValues = new double[groupSumColNums.length];
		int[]    rptSumColNums = xmlRptData.getRptSumColNums();
		double[] rptSumColValues = new double[rptSumColNums.length];
		int numRows = rowsVect.size();
		Vector colsVect;

		//String tableHtml = "<TABLE border=1 cellspacing=10>";
		String tableHtml = "<TABLE border=1 width=630>";

		//The first row of the table will be the headings.
		tableHtml = tableHtml + "<TR bgcolor=darkblue>";
		for (int i=0; i<headings.length; i++)
		{
			//Write table heading for this column
			tableHtml = tableHtml + "<TD><FONT color=white><B>" + headings[i] + "</B></TD>";
		}
		//End of table row.
		tableHtml = tableHtml + "</TR>";

		String lastGroupByVal = "";
		boolean printGroupFooter = false;
		for (int i=0; i<numRows; i++)
		{
			colsVect = (Vector)rowsVect.elementAt(i);

			//Check to see if there is a group.  If so print the
			//Group Header Columns.
			if (xmlRptData.rptUsesGrouping())
			{
				String newGroupByVal = (String)colsVect.elementAt(xmlRptData.getGroupByColNum());
				if (newGroupByVal == null) newGroupByVal = "-";
				//GROUP FOOTER LOGIC
				//Check the group by column.  If the current group by column value
				//is different than the previous, skip a couple of lines and
				//check if there is a totals column
				if (!lastGroupByVal.equals(newGroupByVal))
				{

					if (i != 0 && groupSumColNums.length > 0)
					{
						tableHtml = tableHtml + "<TR>";
						tableHtml = tableHtml + createColSkipTags(groupSumColNums[0]-1);
						tableHtml = tableHtml + "<TD align=right>Total:</TD>";
						for (int m=0; m<groupSumColNums.length; m++)
						{
							tableHtml = tableHtml +
										"<TD align=right><U>"+
										"<Font size=2>"+
										daiFormatUtil.doubleToCurrency(groupSumColValues[m], true)+
										"</U></TD>";
							groupSumColValues[m] = 0.0;
						}
						tableHtml = tableHtml + "</TR>";
					}

					lastGroupByVal = (String)colsVect.elementAt(xmlRptData.getGroupByColNum());
                    if (lastGroupByVal == null) lastGroupByVal = "-";

					tableHtml = tableHtml + "<TR>";
					for (int j=0; j<groupColNums.length; j++)
					{
						//Write table columns for this row.
						tableHtml = tableHtml +
									"<TD>" +
									"<Font size=2>"+
									colsVect.elementAt(groupColNums[j]) +
									"</TD>";
					}
					tableHtml = tableHtml + "</TR>";
					printGroupFooter = true;
				}
			}

			//BEGIN DETAIL COLS
			//Print the Detail Row Columns
			tableHtml = tableHtml + "<TR>";

			tableHtml = tableHtml + createColSkipTags(detailColNums[0]);
			for (int j=0; j<detailColNums.length; j++)
			{

				//Get the cell value.
				String colVal = (String)colsVect.elementAt(detailColNums[j]);
				String colDispVal;
				if (colVal == null)
				{
					colVal = "";
					colDispVal = "<center>-";
				} else
				{
					colDispVal = formatCol(colVal, colFormatting[detailColNums[j]]);
				}
				//Write the table value for this row and column.
				tableHtml = tableHtml +
							"<TD " + xmlRptData.getDetailCellTags(j) + " >" +
							"<Font size=2>"+
							colDispVal +
							"</TD>";

                //Accumulate the group sum columns
				for (int k=0; k<groupSumColNums.length; k++)
				{
					if (detailColNums[j] == groupSumColNums[k])
					{
						if (colVal.length() == 0) colVal = "0.00";
						groupSumColValues[k] = groupSumColValues[k] + Double.parseDouble(colVal);
					}
				}
                //Accumulate the rpt footer sum columns
				for (int k=0; k<rptSumColNums.length; k++)
				{
					if (detailColNums[j] == rptSumColNums[k])
					{
						if (colVal.length() == 0) colVal = "0.00";
						rptSumColValues[k] = rptSumColValues[k] + Double.parseDouble(colVal);
					}
				}
			}
			//End of table row.
			tableHtml = tableHtml + "</TR>";
			//END DETAIL COLS

		}

		//Print the group Footer for the last row only
		if (xmlRptData.rptUsesGrouping() && groupSumColNums.length > 0)
		{
			tableHtml = tableHtml + "<TR>";
			tableHtml = tableHtml + createColSkipTags(groupSumColNums[0]-1);
			tableHtml = tableHtml + "<TD align=right>Total:</TD>";
			for (int m=0; m<groupSumColNums.length; m++)
			{
				tableHtml = tableHtml +
							"<TD align=right><U>"+
							"<Font size=2>"+
							daiFormatUtil.doubleToCurrency(groupSumColValues[m], true)+
							"</U></TD>";
				groupSumColValues[m] = 0.0;
			}
			tableHtml = tableHtml + "</TR>";
		}

        //Print the footer sum totals
        if (rptSumColNums.length > 0 && rptSumColNums.length > 0) {
			tableHtml = tableHtml + "<TR><B>";
			tableHtml = tableHtml + createColSkipTags(rptSumColNums[0]-1);
			tableHtml = tableHtml + "<TD align=right>Grand Total</TD>";
			for (int m=0; m<rptSumColNums.length; m++)
			{
				tableHtml = tableHtml +
							"<TD align=right><U>"+
							"<Font size=2>"+
							daiFormatUtil.doubleToCurrency(rptSumColValues[m], true)+
							"</U></TD>";
				rptSumColValues[m] = 0.0;
			}
			tableHtml = tableHtml + "</B></TR>";
        }

		tableHtml = tableHtml + "</TABLE>";

		return tableHtml;
	}

	private String createColSkipTags(int numColsToSkip)
	{
		String ret = "";

		for (int i=0; i<numColsToSkip; i++)
		{
			ret = ret + "<TD></TD>";
		}
		return ret;
	}

    private String formatCol(String colVal, String colFormat) {
        String ret = colVal;
        if (colFormat != null && colFormat.equals("CURRENCY")) {
            ret = daiFormatUtil.doubleToCurrency(Double.parseDouble(colVal), false);
        }
        if (colFormat != null && colFormat.equals("DATE")) {
            ret = daiFormatUtil.adjustDate(colVal, 0);
        }
        return ret;
    }
}
