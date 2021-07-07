//Title:        Business Artifacts
//Version:
//Copyright:    Copyright (c) 1998
//Author:       Stephen P. Furlong
//Company:      Digital Artifacts Inc.
//Description:
package dai.client.clientAppRoot;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import dai.shared.cmnSvcs.SessionMetaData;

public class daiExplorerTree extends JTree {
	private SessionMetaData sessionMeta;

	private Vector vecLeafNodes = new Vector();

	public daiExplorerTree() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		sessionMeta = SessionMetaData.getInstance();
		populateTree();
		// Enable tool tips.
		ToolTipManager.sharedInstance().registerComponent(this);

		this.setCellRenderer(new MyRenderer());

	}

	private void populateTree() {
		// Create Tree Root Node
		daiExplorerNode rootNode = new daiExplorerNode("", "Business Explorer");

		// Load the Order Entry Module.
		rootNode.add(loadCustOrderModule());
		rootNode.add(loadShipmentsModule());
		rootNode.add(loadPurchasingModule());
		rootNode.add(loadInventoryModule());
		rootNode.add(loadAcctsPayableModule());
		rootNode.add(loadDecisionSupportModule());
		rootNode.add(loadSysAdminModule());

		// Complete tree creation.
		DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
		this.setModel(treeModel);
		this.setSelectionRow(0);
	}

	private daiExplorerNode loadCustOrderModule() {
		// !!The first parameter is used for security
		// !!which will be implemented later
		daiExplorerNode component = new daiExplorerNode("", "Customer Orders");
		daiExplorerNode documents = new daiExplorerNode("", "Documents");

		component.add(new daiExplorerNode("", "Orders",
				"dai.client.ui.businessTrans.order.OrderFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		component.add(new daiExplorerNode("", "Customers",
				"dai.client.ui.corpResources.customer.customerFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Quotations",
				"dai.client.ui.businessTrans.quote.QuoteFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		component.add(new daiExplorerNode("", "Prospects",
				"dai.client.ui.corpResources.prospect.ProspectFrame",
				sessionMeta.getImagesHome() + "new.gif"));

		// Documents.
		documents.add(new daiExplorerNode("", "Order Ack",
				"dai.client.ui.docGen.PrintOrderAckDoc", sessionMeta
						.getImagesHome()
						+ "print16.gif"));
		documents.add(new daiExplorerNode("", "Quotation",
				"dai.client.ui.docGen.PrintQuoteDoc", sessionMeta
						.getImagesHome()
						+ "print16.gif"));
		documents.add(new daiExplorerNode("", "Customer Statement",
				"dai.client.ui.docGen.PrintCustStmtDoc", sessionMeta
						.getImagesHome()
						+ "print16.gif"));
		documents.add(new daiExplorerNode("", "Prospect Labels",
				"dai.client.ui.docGen.PrintProspectLabelDoc", sessionMeta
						.getImagesHome()
						+ "print16.gif"));

		component.add(documents);

		return component;
	}

	private daiExplorerNode loadShipmentsModule() {
		// !!The first parameter is used for security
		// !!which will be implemented later
		daiExplorerNode component = new daiExplorerNode("",
				"Shipments/Acct Receivable");
		daiExplorerNode documents = new daiExplorerNode("", "Documents");

		component
				.add(new daiExplorerNode(
						"",
						"Create Shipment",
						"dai.client.ui.businessTrans.shipment.CreateShipmentWizardFrame",
						sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Shipments",
				"dai.client.ui.businessTrans.shipment.ShipmentFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Update Shipment Charges",
				"dai.client.ui.businessTrans.shipment.UpdateShipmentFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Receive Payment",
				"dai.client.ui.businessTrans.shipment.CashReceiptFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Receipt Entry/Update",
				"dai.client.ui.businessTrans.shipment.ReceiptFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Create Credit Memo",
				"dai.client.ui.businessTrans.shipment.CreateCreditMemoFrame",
				sessionMeta.getImagesHome() + "new.gif"));

		// Documents.
		documents.add(new daiExplorerNode("", "Sales Invoice",
				"dai.client.ui.docGen.PrintSalesInvoiceDoc", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		documents.add(new daiExplorerNode("", "Packing Slip",
				"dai.client.ui.docGen.PrintPackSlipDoc", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		documents.add(new daiExplorerNode("", "Credit Memo",
				"dai.client.ui.docGen.PrintCreditMemoDoc", sessionMeta
						.getImagesHome()
						+ "new.gif"));

		component.add(documents);

		return component;
	}

	private daiExplorerNode loadPurchasingModule() {
		// !!The first parameter is used for security
		// !!which will be implemented later
		daiExplorerNode component = new daiExplorerNode("", "Purchasing");
		daiExplorerNode documents = new daiExplorerNode("", "Documents");

		component.add(new daiExplorerNode("", "Vendors",
				"dai.client.ui.corpResources.vendor.vendorFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		component.add(new daiExplorerNode("", "Carriers",
				"dai.client.ui.corpResources.carrier.carrierFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		component.add(new daiExplorerNode("", "Purchase Orders",
				"dai.client.ui.businessTrans.purchOrder.PurchOrderFrame",
				sessionMeta.getImagesHome() + "new.gif"));

		// Documents.
		documents.add(new daiExplorerNode("", "Purchase Order",
				"dai.client.ui.docGen.PrintPurchOrderDoc", sessionMeta
						.getImagesHome()
						+ "new.gif"));

		component.add(documents);

		return component;
	}

	private daiExplorerNode loadInventoryModule() {
		// !!The first parameter is used for security
		// !!which will be implemented later
		daiExplorerNode component = new daiExplorerNode("", "Inventory");

		component.add(new daiExplorerNode("", "Items",
				"dai.client.ui.corpResources.item.ItemFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		component.add(new daiExplorerNode("", "Receive Inventory",
				"dai.client.ui.businessTrans.purchOrder.ReceiveInventoryFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Adjust Inventory",
				"dai.client.ui.corpResources.item.InventoryAdjustFrame",
				sessionMeta.getImagesHome() + "new.gif"));

		return component;
	}

	private daiExplorerNode loadAcctsPayableModule() {
		// !!The first parameter is used for security
		// !!which will be implemented later
		daiExplorerNode component = new daiExplorerNode("", "Accounts Payable");

		component.add(new daiExplorerNode("", "Receive Vendor Invoice",
				"dai.client.ui.businessTrans.purchOrder.InvoiceReceiptFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Receive Expense Bill",
				"dai.client.ui.businessTrans.purchOrder.BillReceiptFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Pay Bills",
				"dai.client.ui.businessTrans.purchOrder.PayBillsFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Review Payment Vouchers",
				"dai.client.ui.businessTrans.purchOrder.PayVoucherFrame",
				sessionMeta.getImagesHome() + "new.gif"));
		component.add(new daiExplorerNode("", "Print Checks",
				"dai.client.ui.docGen.CheckPrintRptFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));

		return component;
	}

	private daiExplorerNode loadDecisionSupportModule() {
		// !!The first parameter is used for security
		// !!which will be implemented later
		daiExplorerNode component = new daiExplorerNode("", "Decision Support");
		component.add(new daiExplorerNode("", "Web Reports",
				"dai.client.ui.docGen.daiWebRptFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));

		return component;
	}

	private daiExplorerNode loadSysAdminModule() {
		// !!The first parameter is used for security
		// !!which will be implemented later
		daiExplorerNode component = new daiExplorerNode("",
				"System Administration");

		component.add(new daiExplorerNode("", "Users",
				"dai.client.ui.sysAdmin.UserProfileFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		component.add(new daiExplorerNode("", "Corporate Locations",
				"dai.client.ui.sysAdmin.LocationFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		component.add(new daiExplorerNode("", "Chart Of Accounts",
				"dai.client.ui.sysAdmin.FinanceAcctsFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));
		component.add(new daiExplorerNode("", "Financial Account Defaults",
				"dai.client.ui.sysAdmin.DefaultAccountsFrame", sessionMeta
						.getImagesHome()
						+ "new.gif"));

		return component;
	}
}

class MyRenderer extends DefaultTreeCellRenderer {

	public MyRenderer() {
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		if (leaf) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			daiExplorerNode comp = (daiExplorerNode) node;
			setToolTipText(comp.getNodeDescription());
		} else {
			setToolTipText(null); // no tool tip
		}

		return this;
	}
}