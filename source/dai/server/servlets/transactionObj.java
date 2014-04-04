package dai.server.servlets;

import java.util.*;

public class transactionObj
{
	dai.shared.cmnSvcs.FinanceAcctsDataCache _financeCache = dai.shared.cmnSvcs.FinanceAcctsDataCache.getInstance();

	//CONSTRUCTORS
	public transactionObj()
	{
		ID="";
		acctName="";
		date="";
		trans_Ref="";
		type="";
		debit="0";
		credit="0";
		balance=0;
		note="";
	}

        public transactionObj (Vector account)
        {
        //account = ID, trans_date, trans_ref, trans_type, debit, credit, note
          String temp = "";
          temp += account.get(0);
          setID(temp);
          setName();
          temp = "" + account.get(1);
          setDate(temp);
          temp = "" + account.get(2);
          setTrans_Ref(temp);
          temp = "" + account.get(3);
	  setType(temp);
          temp = "" + account.get(4);
	  setDebit(temp);
          temp = "" + account.get(5);
	  setCredit(temp);
          setBalance();
          temp = "" + account.get(6);
	  setNote(temp);
        }


	//DATA
	private String ID="";
	private String acctName="";
	private String date="";
	private String trans_Ref="";
	private String type="";
	private String debit="0";
	private String credit="0";
	private double balance=0;
	private String note="";



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

	public void setDate (String newDate)
	{
		if (!(newDate.equals("null")))
			date=newDate;
	}

	public void setTrans_Ref (String newTrans_Ref)
	{
		if (!(newTrans_Ref.equals("null")))
			trans_Ref=newTrans_Ref;
	}

	public void setType (String newType)
	{
		if (!(newType.equals("null")))
			type=newType;
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

	public void setNote (String newNote)
	{
		if (!(newNote.equals("null")))
			note=newNote;
	}


	public String getID ()
	{
		return ID;
	}

	public String getName()
	{
		return acctName;
	}

	public String getDate ()
	{
		return date;
	}

	public String getTrans_Ref ()
	{
		return trans_Ref;
	}

	public String getType ()
	{
		return type;
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

	public String getNote ()
	{
		return note;
	}
}