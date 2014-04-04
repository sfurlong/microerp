<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://xmlns.oracle.com/adf/faces/rich" prefix="af"%>
<f:view>
  <af:document id="d1">
    <af:form id="f1">
      <af:panelSplitter orientation="vertical" splitterPosition="50" id="ps1">
        <f:facet name="first"/>
        <f:facet name="second">
          <af:panelSplitter orientation="vertical" positionedFromEnd="true"
                            splitterPosition="50" id="ps2">
            <f:facet name="first">
              <af:panelStretchLayout id="psl1">
                <f:facet name="bottom"/>
                <f:facet name="center">
                  <af:panelGroupLayout layout="scroll"
                                       xmlns:af="http://xmlns.oracle.com/adf/faces/rich"
                                       id="pgl1">
                    <af:panelFormLayout id="pfl1">
                      <f:facet name="footer"/>
                      <af:inputText label="User ID:" id="userId_it"
                                    binding="#{loginBackingBean.userIdProp}"/>
                      <af:inputText label="Password:" id="passwd_it"
                                    secret="true"
                                    binding="#{loginBackingBean.passwdProp}"/>
                      <af:commandButton text="Submit" id="submit_cb"
                                        action="#{loginBackingBean.submit_cb_action}"/>
                    </af:panelFormLayout>
                  </af:panelGroupLayout>
                </f:facet>
                <f:facet name="start"/>
                <f:facet name="end"/>
                <f:facet name="top"/>
              </af:panelStretchLayout>
              <!-- id="af_one_column_header_and_footer_stretched_with_splitter"   -->
            </f:facet>
            <f:facet name="second"/>
          </af:panelSplitter>
        </f:facet>
      </af:panelSplitter>
    </af:form>
  </af:document>
</f:view>
<%-- 
  oracle-jdev-comment:preferred-managed-bean-name:loginBackingBean
--%>