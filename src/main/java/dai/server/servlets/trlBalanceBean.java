
//Title:        Your Product Name
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Stephen Furlong
//Company:      DAI
//Description:  Your description

package dai.server.servlets;

import java.util.*;
import dai.shared.csAdapters.*;
import dai.shared.cmnSvcs.*;

public class trlBalanceBean
{

	csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
	csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
	SessionMetaData _sessionMeta = SessionMetaData.getInstance();

	public trlBalanceBean()
	{
	}


	public String getFinalResults (String masterID[], Vector results, String whereClause, csSecurity security)
	{
		trlBalanceObj acctObjects[] = new trlBalanceObj[results.size()];
		trlBalanceObj finalResults[] = new trlBalanceObj[results.size()*2];
		Vector subResults = new Vector();
		Vector tempVect = new Vector();
		String temp ="";
		String sqlStatement="";
		String isSub="";
		String subOf="";
		String display="";
		int index=0;
		int finalCount=0;
		double diff=0;
		double debitTot=0;
		double creditTot=0;
		double balanceRight=0;
		double balanceLeft=0;
		double groupBalance=0;
		boolean found = false;

		for (int i=0; i < finalResults.length; i++)
		{
			finalResults[i]=new trlBalanceObj();
		}

		for (int i=0; i < results.size(); i++)
		{
			temp=results.get(i).toString();
			trlBalanceObj tempObj = new trlBalanceObj(temp);
			tempObj.setBalance();

			//get info about subaccounts
			sqlStatement = "select IS_SUBACCOUNT, SUBACCOUNT from ACCOUNT where ID='"
						   + tempObj.getID() + "'";
			try
			{
				subResults = dbAdapter.getDynamicSQLResults(security, sqlStatement);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			temp = subResults.get(0).toString();
			isSub = temp.substring(1,(temp.indexOf(",")));
			tempObj.setIsSub(isSub);
			temp = temp.substring(temp.indexOf(",") + 1);
			subOf = temp.substring(1, (temp.indexOf("]")));
			tempObj.setSubOf(subOf);
			diff = tempObj.getBalance();

			/******GROUP SUBACCOUNTS WITH MASTER ACCOUNTS*****/
			index=-1;
			for (int j=0; j<masterID.length; j++)
			{
				if (tempObj.getID().equals(masterID[j]))  //current acct is master
				{
					for (int x=0; x < finalResults.length; x++)	 //make sure acct hasn't been added
					{
						if (finalResults[x].getID().equals(tempObj.getID()))
						{
							index=x;
							break;
						}
					}

					if (index==-1)	 //add if not already added
					{
						finalResults[finalCount] = tempObj;
						finalCount++;
						balanceRight+=tempObj.getBalance();
					} else
					{}
				} else
				{
					if (tempObj.getSubOf().equals(masterID[j]))	  //current acct is subacct of masterID[j]
					{
						for (int x=0; x < finalResults.length; x++)
						{
							if (masterID[j].equals(finalResults[x].getID()))
							{
								index = x;
								break;
							}
						}
						trlBalanceObj tempObj2 = new trlBalanceObj();

						if (index==-1)	 //master acct not yet in *finalResults*
						{
							for (int x=i; x <results.size(); x++)  //retrieve master account from *results*
							{
								temp=results.get(x).toString();
								tempObj2 = new trlBalanceObj(temp);

								if (tempObj2.getID().equals(masterID[j]))
								{
									finalResults[finalCount] = tempObj2;
									finalCount++;
									found=true;

									//calculate totals
									tempObj2.setHasSub("Y");
									tempObj2.setBalance();
									balanceRight+=tempObj2.getBalance();
									groupBalance = tempObj2.getBalance()+tempObj.getBalance();
									tempObj2.setBalance(groupBalance);
								}
							}

							if (!found)	  //get master from database
							{
								sqlStatement="select ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL group by ID"
											 + " having ID='" + masterID[j] + "' and " + whereClause;
								try
								{
									tempVect = dbAdapter.getDynamicSQLResults(security, sqlStatement);
								} catch (Exception e)
								{
									e.printStackTrace();
								}
								if (tempVect.size()>0)
								{
									tempObj2 = new trlBalanceObj(tempVect.get(0).toString());
								} else
								{
									tempObj2 = new trlBalanceObj();
									//tempObj2.setName("<b>Chart of Accounts error:</b> Master account does not exist for subacct <i>" + tempObj.getID() + "</i>");
									tempObj2.setID(masterID[j]);
									tempObj2.setName();

								}
								finalResults[finalCount] = tempObj2;
								finalCount++;

								//calculate totals
								tempObj2.setHasSub("Y");
								tempObj2.setBalance();
								balanceRight+=tempObj2.getBalance();
								groupBalance = tempObj2.getBalance()+tempObj.getBalance();
								tempObj2.setBalance(groupBalance);
							}

//*							finalResults[finalCount] = tempObj;
//*							finalCount++;
					                debitTot  += Double.parseDouble(tempObj.getDebit());
							creditTot += Double.parseDouble(tempObj.getCredit());
							balanceLeft += tempObj.getBalance();
						} else	//master acct already in *finalResults*; add subacct
						{
							//create hole for subacct
//							for (int x=finalCount; x >index+1; x--)
//							{
//								finalResults[x]=finalResults[x-1];
//							}

//*							finalResults[index+1]=tempObj;
//*							finalCount++;
					                debitTot  += Double.parseDouble(tempObj.getDebit());
							creditTot += Double.parseDouble(tempObj.getCredit());
							balanceLeft+=tempObj.getBalance();
							groupBalance = finalResults[index].getBalance() + tempObj.getBalance();
							finalResults[index].setBalance(groupBalance);
						}
					}
				}
			}
		}

		for (int i=0; i < finalCount; i++)
		{
			if (finalResults[i].getDebit().trim().equals("null"))
			{
				finalResults[i].setDebit("0");
			}
			if (finalResults[i].getCredit().trim().equals("null"))
			{
				finalResults[i].setCredit("0");
			}
            debitTot  += Double.parseDouble(finalResults[i].getDebit());
            creditTot += Double.parseDouble(finalResults[i].getCredit());
			display += "<tr>";
                        if (finalResults[i].getHasSub().equals("Y"))
                        {
                          display +="<td align=left><FONT size=2><B><a href='trlBalance1B.jsp?masterId=" + finalResults[i].getID() + "'>" + finalResults[i].getID() + "</a></B></FONT></td>";
                        }
                        else
                        {
			  display +="<td align=left><FONT size=2><a href='transaction.jsp?acctId=" + finalResults[i].getID() + "'>" + finalResults[i].getID() + "</a></FONT></td>";
			  //display +="<td align=left><FONT size=2>" + finalResults[i].getID() + "</FONT></td>";
			}

			display+= "<td align=left><FONT size=2>" + finalResults[i].getName() + "</FONT></td>"
					   + "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(Double.parseDouble(finalResults[i].getDebit()), false) + "</FONT></td>"
					   + "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(Double.parseDouble(finalResults[i].getCredit()), false) + "</FONT></td>";

			//show subacct balance in different column
                        display += "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(finalResults[i].getBalance(), true) + "</FONT></td>";
			/*if (finalResults[i].getIsSub().trim().equals("Y"))
			{
				display += "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(finalResults[i].getBalance(), true) + "</FONT></td>"
						   + "<td align=center>-----</td>";
			} else
			{
				display += "<td align=center>-----</td>"
						   +"<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(finalResults[i].getBalance(), true) + "</FONT></td>";
			}*/

			display += "</tr>";
		} //end for loop

		display += "<tr><td></td>"
				   + "<td align=right><FONT size=2>Grand Totals:</FONT></td>"
				   + "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(debitTot, true) + "</FONT></td>"
				   + "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(creditTot, true) + "</FONT></td>"
				   + "</tr>";

		return display;
	}
}