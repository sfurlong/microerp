package dai.server.servlets;

import java.util.*;

public class trlBalanceObj2
{
	dai.shared.cmnSvcs.FinanceAcctsDataCache _financeCache = dai.shared.cmnSvcs.FinanceAcctsDataCache.getInstance();

	//CONSTRUCTORS
	public trlBalanceObj2()
	{
		ID="";
		acctName="";
		debit="";
		credit="";
		balance=0;
		isSub="N";
		subOf="";
                hasSub="N";
                prevDebit="";
                prevCredit="";
                prevBalance=0;
	}

        public trlBalanceObj2 (Vector account)
        {
        //account = ID, debit, credit, prevDebit, prevCredit
          String temp = "";
          temp += account.get(0);
          setID(temp);
          setName();
          temp = "" + account.get(1);
          setDebit(temp);
          temp = "" + account.get(2);
          setCredit(temp);
          temp = "" + account.get(3);
          setPrevDebit(temp);
          temp = "" + account.get(4);
          setPrevCredit(temp);
        }

	public trlBalanceObj2(String temp)
	//temp = "[ID,_Debit,_Credit,_prevDebit,_prevCredit]"
	{
		setID(temp.substring(1,(temp.indexOf(","))));
		temp=temp.substring(temp.indexOf(",") + 1);
		setName();
		setDebit(temp.substring(1,(temp.indexOf(","))));
		temp=temp.substring(temp.indexOf(",") + 1);
		setCredit(temp.substring(1, (temp.indexOf(","))));
                temp=temp.substring(temp.indexOf(",") + 1);
                setPrevDebit(temp.substring(1, (temp.indexOf(","))));
                temp=temp.substring(temp.indexOf(",") + 1);
                setPrevCredit(temp.substring(1, (temp.indexOf("]"))));
	}

	public trlBalanceObj2(int x, String temp)
	//temp = "ID,_Debit,_Credit,_IsSub,_SubOf,_prevDebit,_prevCredit"
	{
		setID(temp.substring(0,(temp.indexOf(","))));
		temp=temp.substring(temp.indexOf(",") + 1);
		setName();
		setDebit(temp.substring(1,(temp.indexOf(","))));
		temp=temp.substring(temp.indexOf(",") + 1);
		setCredit(temp.substring(1, (temp.indexOf(","))));
		temp=temp.substring(temp.indexOf(",") + 1);
		setIsSub(temp.substring(1, (temp.indexOf(","))));
		temp=temp.substring(temp.indexOf(",")+1);
		setSubOf(temp.substring(1, (temp.indexOf(","))));
                temp=temp.substring(temp.indexOf(",")+1);
                setPrevDebit(temp.substring(1, (temp.indexOf(","))));
                temp=temp.substring(temp.indexOf(",")+1);
                setPrevCredit(temp.substring(1, (temp.length())));
	}

	//DATA
	private String ID="";
	private String acctName="";
	private String debit="";
	private String credit="";
	private double balance=0;
	private String isSub="N";
	private String subOf="";
        private String hasSub="N";

	//DATA FROM THE PREVIOUS MONTH
        private String prevDebit="";
        private String prevCredit="";
        private double prevBalance=0;


	//SETs and GETs
	public void setID (String newID)
	{
		if (!(newID.equals("null")))
			ID=newID;
	}

	public void setName()
	//get acctName based on its ID
	{
		Vector nums = _financeCache.getAcctNums();
		for (int x=0; x < nums.size(); x++)
		{
			if (ID.equals(nums.get(x).toString()))
				acctName=_financeCache.getAcctName(x);
		}
	}

	public void setName (String newName)
	//set acctName to a specific string
	{
		if (!(newName.equals("null")))
			acctName=newName;
	}

	public void setDebit (String newDebit)
	{
		if (!(newDebit.equals("null")))
			debit=newDebit;
	}

	public void setCredit (String newCredit)
	{
		if (!(newCredit.equals("null")))
			credit=newCredit;
	}

	public void setBalance()
	//calculates balance based on debit - credit
	{
		if ((debit.trim().equals("null"))||(debit.trim().equals("")))
			debit="0";
		if ((credit.trim().equals("null"))||(credit.trim().equals("")))
			credit="0";
		balance = (Double.parseDouble(debit) - Double.parseDouble(credit));
	}

	public void setBalance(double newBalance)
	{
		balance=newBalance;
	}

	public void setIsSub (String newIs)
	{
		if ((newIs.equals("Y")) || (newIs.equals("N")))
			isSub = newIs;
	}

	public void setSubOf (String newSub)
	{
		if (!(newSub.equals("null")))
			subOf = newSub;
	}

	public void setHasSub (String newHas)
	{
		if ((newHas.equals("Y")) || (newHas.equals("N")))
			hasSub = newHas;
	}

	public void setPrevDebit (String newPrevDebit)
	{
		if (!(newPrevDebit.equals("null")))
			prevDebit=newPrevDebit;
	}

	public void setPrevCredit (String newPrevCredit)
	{
		if (!(newPrevCredit.equals("null")))
			prevCredit=newPrevCredit;
	}

	public void setPrevBalance()
	//calculates previous month's balance based on debit - credit
	{
		if ((prevDebit.trim().equals("null"))||(prevDebit.trim().equals("")))
			prevDebit="0";
		if ((prevCredit.trim().equals("null"))||(prevCredit.trim().equals("")))
			prevCredit="0";
		prevBalance = (Double.parseDouble(prevDebit) - Double.parseDouble(prevCredit));
	}

	public void setPrevBalance(double newPrevBalance)
	{
		prevBalance=newPrevBalance;
	}



	public String getID ()
	{
		return ID;
	}

	public String getName()
	{
		return acctName;
	}

	public String getDebit ()
	{
		return debit;
	}

	public String getCredit ()
	{
		return credit;
	}

	public double getBalance ()
	{
		return balance;
	}

	public String getIsSub ()
	{
		return isSub;
	}

	public String getSubOf ()
	{
		return subOf;
	}


	public String getHasSub ()
	{
		return hasSub;
	}

	public String getPrevDebit ()
	{
		return prevDebit;
	}

	public String getPrevCredit ()
	{
		return prevCredit;
	}

	public double getPrevBalance ()
	{
		return prevBalance;
	}
}