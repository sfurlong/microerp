package com.altaprise.webrpts.view;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import oracle.adf.view.rich.component.rich.layout.RichPanelList;
import oracle.adf.view.rich.component.rich.layout.RichShowDetailItem;

import org.apache.myfaces.trinidad.event.DisclosureEvent;

public class TabsBackingBean {
  private RichShowDetailItem finsTabProp;
  private RichPanelList finsRptList;

  public TabsBackingBean() {
  }

  public void financials_sdi_disclosureListener(DisclosureEvent disclosureEvent) {
    // Add event code here...
   }

  public void setFinsTabProp(RichShowDetailItem finsTabProp) {
    this.finsTabProp = finsTabProp;
  }

  public RichShowDetailItem getFinsTabProp() {
    return finsTabProp;
  }

  public void setFinsRptList(RichPanelList finsRptList) {
    this.finsRptList = finsRptList;
  }

  public RichPanelList getFinsRptList() {
    return finsRptList;
  }
}
