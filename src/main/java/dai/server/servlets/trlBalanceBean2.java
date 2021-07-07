
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

public class trlBalanceBean2
{

	csDBAdapterFactory dbAdapterFactory = csDBAdapterFactory.getInstance();
	csDBAdapter dbAdapter = dbAdapterFactory.getDBAdapter();
	SessionMetaData _sessionMeta = SessionMetaData.getInstance();

	public trlBalanceBean2()
	{
	}

	public String getFinalResults (String masterID[], Vector results, String dateClause, csSecurity security)
	{
		trlBalanceObj2 acctObjects[] = new trlBalanceObj2[results.size()];
		trlBalanceObj2 finalResults[] = new trlBalanceObj2[results.size()*2];
		Vector subResults = new Vector();
		Vector tempVect = new Vector();
                Vector tempVect2 = new Vector();
		String temp ="";
		String sqlStatement="";
		String isSub="";
		String subOf="";
		String display="";
		int index=0;
		int finalCount=0;
		double diff=0;
		double balanceRight=0;
		double balanceLeft=0;
		double groupBalance=0;
		double prevGroupBalance=0;
                double prevDiff=0;
		double debitTot = 0;
		double creditTot = 0;
		boolean found = false;

		for (int i=0; i < finalResults.length; i++)
		{
			finalResults[i]=new trlBalanceObj2();
		}

		for (int i=0; i < results.size(); i++)
		{
			tempVect2 =(Vector) results.get(i);
			trlBalanceObj2 tempObj = new trlBalanceObj2(tempVect2);
			tempObj.setBalance();
                        tempObj.setPrevBalance();

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
			tempVect2 = (Vector) subResults.get(0);
                        isSub = "" + tempVect2.get(0);
			tempObj.setIsSub(isSub);
                        subOf = "" + tempVect2.get(1);
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
						trlBalanceObj2 tempObj2 = new trlBalanceObj2();

						if (index==-1)	 //master acct not yet in *finalResults*
						{
							for (int x=i; x <results.size(); x++)  //retrieve master account from *results*
							{
								tempVect2 = (Vector) results.get(x);
								tempObj2 = new trlBalanceObj2(tempVect2);

								if (tempObj2.getID().equals(masterID[j]))
								{
									finalResults[finalCount] = tempObj2;
									finalCount++;
									found=true;

									//calculate totals
									tempObj2.setHasSub("Y");
									tempObj2.setBalance();
									tempObj2.setPrevBalance();
									balanceRight+=tempObj2.getBalance();
									groupBalance = tempObj2.getBalance()+tempObj.getBalance();
									prevGroupBalance = tempObj2.getPrevBalance()+tempObj.getPrevBalance();
									tempObj2.setBalance(groupBalance);
									tempObj2.setPrevBalance(prevGroupBalance);
								}
							}

							if (!found)	  //get master from database
							{
								sqlStatement="select ID, sum(DEBIT), sum(CREDIT) from ACCOUNT_DETAIL group by ID"
											 + " having ID='" + masterID[j] + "' and " + dateClause;
								try
								{
									tempVect = dbAdapter.getDynamicSQLResults(security, sqlStatement);
								} catch (Exception e)
								{
									e.printStackTrace();
								}
								if (tempVect.size()>0)
								{
									tempVect2 = (Vector) tempVect.get(0);
									tempObj2 = new trlBalanceObj2(tempVect2);
								} else
								{
									tempObj2 = new trlBalanceObj2();
									//tempObj2.setName("<b>Chart of Accounts error:</b> Master account does not exist for subacct <i>" + tempObj.getID() + "</i>");
									tempObj2.setID(masterID[j]);
									tempObj2.setName();

								}
								finalResults[finalCount] = tempObj2;
								finalCount++;

								//calculate totals
								tempObj2.setHasSub("Y");
								tempObj2.setBalance();
								tempObj2.setPrevBalance();
								balanceRight+=tempObj2.getBalance();
								groupBalance = tempObj2.getBalance()+tempObj.getBalance();
								prevGroupBalance = tempObj2.getPrevBalance()+tempObj.getPrevBalance();
								tempObj2.setBalance(groupBalance);
								tempObj2.setPrevBalance(prevGroupBalance);
							}

//*							finalResults[finalCount] = tempObj;
//*							finalCount++;
					                debitTot  += Double.parseDouble(tempObj.getDebit());
							creditTot += Double.parseDouble(tempObj.getCredit());
							balanceLeft += tempObj.getBalance();
						} else	//master acct already in *finalResults*; add subacct
						{
							finalResults[index].setHasSub("Y");
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
							prevGroupBalance = finalResults[index].getPrevBalance() + tempObj.getPrevBalance();
							finalResults[index].setBalance(groupBalance);
                                                        finalResults[index].setPrevBalance(prevGroupBalance);
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
			if (finalResults[i].getPrevDebit().trim().equals("null"))
			{
				finalResults[i].setPrevDebit("0");
			}
			if (finalResults[i].getPrevCredit().trim().equals("null"))
			{
				finalResults[i].setPrevCredit("0");
			}

            debitTot  += Double.parseDouble(finalResults[i].getDebit());
            creditTot += Double.parseDouble(finalResults[i].getCredit());
			display += "<tr>";
                        if (finalResults[i].getHasSub().equals("Y"))
                        {
                          display +="<td align=left><FONT size=2><B><a href='trlBalance3.jsp?masterId=" + finalResults[i].getID() + "'>" + finalResults[i].getID() + "</a></B></FONT></td>";
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

			prevDiff = finalResults[i].getBalance() - finalResults[i].getPrevBalance();
			display += "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(finalResults[i].getPrevBalance(), true) + "</FONT></td>"
				+ "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(prevDiff, true) + "</FONT></td>"
				+ "</tr>";
		} //end for loop

		display += "<tr><td></td>"
				   + "<td align=right><FONT size=2>Grand Totals:</FONT></td>"
				   + "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(debitTot, true) + "</FONT></td>"
				   + "<td align=right><FONT size=2>" + daiFormatUtil.doubleToCurrency(creditTot, true) + "</FONT></td>"
				   + "</tr>";

		return display;
	}
}