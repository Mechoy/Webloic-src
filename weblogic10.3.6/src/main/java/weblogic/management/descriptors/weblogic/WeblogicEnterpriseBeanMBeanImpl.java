package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class WeblogicEnterpriseBeanMBeanImpl extends XMLElementMBeanDelegate implements WeblogicEnterpriseBeanMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_remoteClientTimeout = false;
   private int remoteClientTimeout = 0;
   private boolean isSet_transactionDescriptor = false;
   private TransactionDescriptorMBean transactionDescriptor;
   private boolean isSet_clientsOnSameServer = false;
   private boolean clientsOnSameServer = false;
   private boolean isSet_runAsIdentityPrincipal = false;
   private String runAsIdentityPrincipal;
   private boolean isSet_iiopSecurityDescriptor = false;
   private IIOPSecurityDescriptorMBean iiopSecurityDescriptor;
   private boolean isSet_createAsPrincipalName = false;
   private String createAsPrincipalName;
   private boolean isSet_referenceDescriptor = false;
   private ReferenceDescriptorMBean referenceDescriptor;
   private boolean isSet_messageDrivenDescriptor = false;
   private MessageDrivenDescriptorMBean messageDrivenDescriptor;
   private boolean isSet_dispatchPolicy = false;
   private String dispatchPolicy;
   private boolean isSet_passivateAsPrincipalName = false;
   private String passivateAsPrincipalName;
   private boolean isSet_entityDescriptor = false;
   private EntityDescriptorMBean entityDescriptor;
   private boolean isSet_jndiName = false;
   private String jndiName;
   private boolean isSet_ejbName = false;
   private String ejbName;
   private boolean isSet_statelessSessionDescriptor = false;
   private StatelessSessionDescriptorMBean statelessSessionDescriptor;
   private boolean isSet_stickToFirstServer = false;
   private boolean stickToFirstServer = false;
   private boolean isSet_enableCallByReference = false;
   private boolean enableCallByReference = false;
   private boolean isSet_statefulSessionDescriptor = false;
   private StatefulSessionDescriptorMBean statefulSessionDescriptor;
   private boolean isSet_removeAsPrincipalName = false;
   private String removeAsPrincipalName;
   private boolean isSet_localJNDIName = false;
   private String localJNDIName;

   public int getRemoteClientTimeout() {
      return this.remoteClientTimeout;
   }

   public void setRemoteClientTimeout(int var1) {
      int var2 = this.remoteClientTimeout;
      this.remoteClientTimeout = var1;
      this.isSet_remoteClientTimeout = var1 != -1;
      this.checkChange("remoteClientTimeout", var2, this.remoteClientTimeout);
   }

   public TransactionDescriptorMBean getTransactionDescriptor() {
      return this.transactionDescriptor;
   }

   public void setTransactionDescriptor(TransactionDescriptorMBean var1) {
      TransactionDescriptorMBean var2 = this.transactionDescriptor;
      this.transactionDescriptor = var1;
      this.isSet_transactionDescriptor = var1 != null;
      this.checkChange("transactionDescriptor", var2, this.transactionDescriptor);
   }

   public boolean getClientsOnSameServer() {
      return this.clientsOnSameServer;
   }

   public void setClientsOnSameServer(boolean var1) {
      boolean var2 = this.clientsOnSameServer;
      this.clientsOnSameServer = var1;
      this.isSet_clientsOnSameServer = true;
      this.checkChange("clientsOnSameServer", var2, this.clientsOnSameServer);
   }

   public String getRunAsIdentityPrincipal() {
      return this.runAsIdentityPrincipal;
   }

   public void setRunAsIdentityPrincipal(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.runAsIdentityPrincipal;
      this.runAsIdentityPrincipal = var1;
      this.isSet_runAsIdentityPrincipal = var1 != null;
      this.checkChange("runAsIdentityPrincipal", var2, this.runAsIdentityPrincipal);
   }

   public IIOPSecurityDescriptorMBean getIIOPSecurityDescriptor() {
      return this.iiopSecurityDescriptor;
   }

   public void setIIOPSecurityDescriptor(IIOPSecurityDescriptorMBean var1) {
      IIOPSecurityDescriptorMBean var2 = this.iiopSecurityDescriptor;
      this.iiopSecurityDescriptor = var1;
      this.isSet_iiopSecurityDescriptor = var1 != null;
      this.checkChange("iiopSecurityDescriptor", var2, this.iiopSecurityDescriptor);
   }

   public String getCreateAsPrincipalName() {
      return this.createAsPrincipalName;
   }

   public void setCreateAsPrincipalName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.createAsPrincipalName;
      this.createAsPrincipalName = var1;
      this.isSet_createAsPrincipalName = var1 != null;
      this.checkChange("createAsPrincipalName", var2, this.createAsPrincipalName);
   }

   public ReferenceDescriptorMBean getReferenceDescriptor() {
      return this.referenceDescriptor;
   }

   public void setReferenceDescriptor(ReferenceDescriptorMBean var1) {
      ReferenceDescriptorMBean var2 = this.referenceDescriptor;
      this.referenceDescriptor = var1;
      this.isSet_referenceDescriptor = var1 != null;
      this.checkChange("referenceDescriptor", var2, this.referenceDescriptor);
   }

   public MessageDrivenDescriptorMBean getMessageDrivenDescriptor() {
      return this.messageDrivenDescriptor;
   }

   public void setMessageDrivenDescriptor(MessageDrivenDescriptorMBean var1) {
      MessageDrivenDescriptorMBean var2 = this.messageDrivenDescriptor;
      this.messageDrivenDescriptor = var1;
      this.isSet_messageDrivenDescriptor = var1 != null;
      this.checkChange("messageDrivenDescriptor", var2, this.messageDrivenDescriptor);
   }

   public String getDispatchPolicy() {
      return this.dispatchPolicy;
   }

   public void setDispatchPolicy(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.dispatchPolicy;
      this.dispatchPolicy = var1;
      this.isSet_dispatchPolicy = var1 != null;
      this.checkChange("dispatchPolicy", var2, this.dispatchPolicy);
   }

   public String getPassivateAsPrincipalName() {
      return this.passivateAsPrincipalName;
   }

   public void setPassivateAsPrincipalName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.passivateAsPrincipalName;
      this.passivateAsPrincipalName = var1;
      this.isSet_passivateAsPrincipalName = var1 != null;
      this.checkChange("passivateAsPrincipalName", var2, this.passivateAsPrincipalName);
   }

   public EntityDescriptorMBean getEntityDescriptor() {
      return this.entityDescriptor;
   }

   public void setEntityDescriptor(EntityDescriptorMBean var1) {
      EntityDescriptorMBean var2 = this.entityDescriptor;
      this.entityDescriptor = var1;
      this.isSet_entityDescriptor = var1 != null;
      this.checkChange("entityDescriptor", var2, this.entityDescriptor);
   }

   public String getJNDIName() {
      return this.jndiName;
   }

   public void setJNDIName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.jndiName;
      this.jndiName = var1;
      this.isSet_jndiName = var1 != null;
      this.checkChange("jndiName", var2, this.jndiName);
   }

   public String getEJBName() {
      return this.ejbName;
   }

   public void setEJBName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.ejbName;
      this.ejbName = var1;
      this.isSet_ejbName = var1 != null;
      this.checkChange("ejbName", var2, this.ejbName);
   }

   public StatelessSessionDescriptorMBean getStatelessSessionDescriptor() {
      return this.statelessSessionDescriptor;
   }

   public void setStatelessSessionDescriptor(StatelessSessionDescriptorMBean var1) {
      StatelessSessionDescriptorMBean var2 = this.statelessSessionDescriptor;
      this.statelessSessionDescriptor = var1;
      this.isSet_statelessSessionDescriptor = var1 != null;
      this.checkChange("statelessSessionDescriptor", var2, this.statelessSessionDescriptor);
   }

   public boolean getStickToFirstServer() {
      return this.stickToFirstServer;
   }

   public void setStickToFirstServer(boolean var1) {
      boolean var2 = this.stickToFirstServer;
      this.stickToFirstServer = var1;
      this.isSet_stickToFirstServer = true;
      this.checkChange("stickToFirstServer", var2, this.stickToFirstServer);
   }

   public boolean getEnableCallByReference() {
      return this.enableCallByReference;
   }

   public void setEnableCallByReference(boolean var1) {
      boolean var2 = this.enableCallByReference;
      this.enableCallByReference = var1;
      this.isSet_enableCallByReference = true;
      this.checkChange("enableCallByReference", var2, this.enableCallByReference);
   }

   public StatefulSessionDescriptorMBean getStatefulSessionDescriptor() {
      return this.statefulSessionDescriptor;
   }

   public void setStatefulSessionDescriptor(StatefulSessionDescriptorMBean var1) {
      StatefulSessionDescriptorMBean var2 = this.statefulSessionDescriptor;
      this.statefulSessionDescriptor = var1;
      this.isSet_statefulSessionDescriptor = var1 != null;
      this.checkChange("statefulSessionDescriptor", var2, this.statefulSessionDescriptor);
   }

   public String getRemoveAsPrincipalName() {
      return this.removeAsPrincipalName;
   }

   public void setRemoveAsPrincipalName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.removeAsPrincipalName;
      this.removeAsPrincipalName = var1;
      this.isSet_removeAsPrincipalName = var1 != null;
      this.checkChange("removeAsPrincipalName", var2, this.removeAsPrincipalName);
   }

   public String getLocalJNDIName() {
      return this.localJNDIName;
   }

   public void setLocalJNDIName(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.localJNDIName;
      this.localJNDIName = var1;
      this.isSet_localJNDIName = var1 != null;
      this.checkChange("localJNDIName", var2, this.localJNDIName);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<weblogic-enterprise-bean");
      var2.append(">\n");
      if (null != this.getEJBName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<ejb-name>").append(this.getEJBName()).append("</ejb-name>\n");
      }

      if (null != this.getStatefulSessionDescriptor()) {
         var2.append(this.getStatefulSessionDescriptor().toXML(var1 + 2)).append("\n");
      } else if (null != this.getMessageDrivenDescriptor()) {
         var2.append(this.getMessageDrivenDescriptor().toXML(var1 + 2)).append("\n");
      } else if (null != this.getEntityDescriptor()) {
         var2.append(this.getEntityDescriptor().toXML(var1 + 2)).append("\n");
      } else if (null != this.getStatelessSessionDescriptor()) {
         var2.append(this.getStatelessSessionDescriptor().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getTransactionDescriptor()) {
         var2.append(this.getTransactionDescriptor().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getIIOPSecurityDescriptor()) {
         var2.append(this.getIIOPSecurityDescriptor().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getReferenceDescriptor()) {
         var2.append(this.getReferenceDescriptor().toXML(var1 + 2)).append("\n");
      }

      if (this.isSet_enableCallByReference || this.getEnableCallByReference()) {
         var2.append(ToXML.indent(var1 + 2)).append("<enable-call-by-reference>").append(ToXML.capitalize(Boolean.valueOf(this.getEnableCallByReference()).toString())).append("</enable-call-by-reference>\n");
      }

      if (this.isSet_clientsOnSameServer || this.getClientsOnSameServer()) {
         var2.append(ToXML.indent(var1 + 2)).append("<clients-on-same-server>").append(ToXML.capitalize(Boolean.valueOf(this.getClientsOnSameServer()).toString())).append("</clients-on-same-server>\n");
      }

      if (null != this.getRunAsIdentityPrincipal()) {
         var2.append(ToXML.indent(var1 + 2)).append("<run-as-identity-principal>").append(this.getRunAsIdentityPrincipal()).append("</run-as-identity-principal>\n");
      }

      if (null != this.getCreateAsPrincipalName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<create-as-principal-name>").append(this.getCreateAsPrincipalName()).append("</create-as-principal-name>\n");
      }

      if (null != this.getRemoveAsPrincipalName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<remove-as-principal-name>").append(this.getRemoveAsPrincipalName()).append("</remove-as-principal-name>\n");
      }

      if (null != this.getPassivateAsPrincipalName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<passivate-as-principal-name>").append(this.getPassivateAsPrincipalName()).append("</passivate-as-principal-name>\n");
      }

      if (null != this.getJNDIName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<jndi-name>").append(this.getJNDIName()).append("</jndi-name>\n");
      }

      if (null != this.getLocalJNDIName()) {
         var2.append(ToXML.indent(var1 + 2)).append("<local-jndi-name>").append(this.getLocalJNDIName()).append("</local-jndi-name>\n");
      }

      if (null != this.getDispatchPolicy()) {
         var2.append(ToXML.indent(var1 + 2)).append("<dispatch-policy>").append(this.getDispatchPolicy()).append("</dispatch-policy>\n");
      }

      if (this.isSet_remoteClientTimeout || 0 != this.getRemoteClientTimeout()) {
         var2.append(ToXML.indent(var1 + 2)).append("<remote-client-timeout>").append(this.getRemoteClientTimeout()).append("</remote-client-timeout>\n");
      }

      if (this.isSet_stickToFirstServer || this.getStickToFirstServer()) {
         var2.append(ToXML.indent(var1 + 2)).append("<stick-to-first-server>").append(ToXML.capitalize(Boolean.valueOf(this.getStickToFirstServer()).toString())).append("</stick-to-first-server>\n");
      }

      var2.append(ToXML.indent(var1)).append("</weblogic-enterprise-bean>\n");
      return var2.toString();
   }
}
