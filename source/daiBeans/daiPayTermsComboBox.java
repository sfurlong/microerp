package daiBeans;

import dai.shared.businessObjs.cust_orderObj;

public class daiPayTermsComboBox extends daiComboBox
{
    public static final int NET30_INDEX = 0;
    
	public daiPayTermsComboBox()
	{
		super();
        try {
    		jbInit();
        } catch (Exception e) {
            System.out.println(e);
        }
	}

	private void jbInit() throws Exception
	{
        //Populate the pay terms combo box
        this.addItem(cust_orderObj.PAY_TERMS_NET30);
        this.addItem(cust_orderObj.PAY_TERMS_NET45);
        this.addItem(cust_orderObj.PAY_TERMS_COD);
        this.addItem(cust_orderObj.PAY_TERMS_ON_RECEIPT);
        this.addItem(cust_orderObj.PAY_TERMS_IN_ADVANCE);
        this.addItem(cust_orderObj.PAY_TERMS_CHARGE_AMX);
        this.addItem(cust_orderObj.PAY_TERMS_CHARGE_VISA);
        this.addItem(cust_orderObj.PAY_TERMS_CHARGE_MC);
        this.addItem(cust_orderObj.PAY_TERMS_CHARGE_DISC);
        this.addItem(cust_orderObj.PAY_TERMS_NET30_2_10);
        this.addItem(cust_orderObj.PAY_TERMS_PENDING_CREDIT);
	}

}
