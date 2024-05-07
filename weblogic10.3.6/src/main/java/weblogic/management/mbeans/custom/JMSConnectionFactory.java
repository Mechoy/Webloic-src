package weblogic.management.mbeans.custom;

import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.JMSConnectionFactoryBean;
import weblogic.jms.JMSLogger;
import weblogic.management.DistributedManagementException;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.SubDeploymentMBean;
import weblogic.management.configuration.TargetMBean;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;
import weblogic.management.provider.custom.ConfigurationMBeanCustomizer;

public class JMSConnectionFactory extends ConfigurationMBeanCustomizer {
   private static final String TARGETS = "Targets";
   private transient JMSConnectionFactoryBean delegate;
   private transient SubDeploymentMBean subDeployment;

   public JMSConnectionFactory(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void useDelegates(JMSConnectionFactoryBean var1, SubDeploymentMBean var2) {
      this.delegate = var1;
      this.subDeployment = var2;
   }

   public TargetMBean[] getTargets() {
      if (this.subDeployment == null) {
         Object var1 = this.getValue("Targets");
         if (var1 == null) {
            return new TargetMBean[0];
         } else {
            if (!(var1 instanceof TargetMBean)) {
               if (!(var1 instanceof WebLogicMBean[])) {
                  return new TargetMBean[0];
               }

               WebLogicMBean[] var2 = (WebLogicMBean[])((WebLogicMBean[])var1);
               TargetMBean[] var3 = new TargetMBean[var2.length];

               for(int var4 = 0; var4 < var2.length; ++var4) {
                  WebLogicMBean var5 = var2[var4];
                  if (!(var5 instanceof TargetMBean)) {
                     return new TargetMBean[0];
                  }

                  var3[var4] = (TargetMBean)var5;
               }

               var1 = var3;
            }

            return (TargetMBean[])((TargetMBean[])var1);
         }
      } else {
         return this.subDeployment.getTargets();
      }
   }

   public void setTargets(TargetMBean[] var1) throws InvalidAttributeValueException, DistributedManagementException {
      if (this.subDeployment == null) {
         this.putValueNotify("Targets", var1);
      } else {
         this.subDeployment.setTargets(var1);
      }

   }

   public String getJNDIName() {
      if (this.delegate == null) {
         Object var1 = this.getValue("JNDIName");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getJNDIName();
      }
   }

   public void setJNDIName(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("JNDIName", var1);
      } else {
         this.delegate.setJNDIName(var1);
      }

   }

   public String getClientId() {
      if (this.delegate == null) {
         Object var1 = this.getValue("ClientId");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getClientParams().getClientId();
      }
   }

   public void setClientId(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("ClientId", var1);
      } else {
         this.delegate.getClientParams().setClientId(var1);
      }

   }

   public String getAcknowledgePolicy() {
      if (this.delegate == null) {
         Object var1 = this.getValue("AcknowledgePolicy");
         return var1 != null && var1 instanceof String ? (String)var1 : "All";
      } else {
         return this.delegate.getClientParams().getAcknowledgePolicy();
      }
   }

   public void setAcknowledgePolicy(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("AcknowledgePolicy", var1);
      } else {
         this.delegate.getClientParams().setAcknowledgePolicy(var1);
      }

   }

   public boolean getAllowCloseInOnMessage() {
      if (this.delegate == null) {
         Object var1 = this.getValue("AllowCloseInOnMessage");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : false;
      } else {
         return this.delegate.getClientParams().isAllowCloseInOnMessage();
      }
   }

   public void setAllowCloseInOnMessage(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("AllowCloseInOnMessage", new Boolean(var1));
      } else {
         this.delegate.getClientParams().setAllowCloseInOnMessage(var1);
      }

   }

   public int getMessagesMaximum() {
      if (this.delegate == null) {
         Object var1 = this.getValue("MessagesMaximum");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 10;
      } else {
         return this.delegate.getClientParams().getMessagesMaximum();
      }
   }

   public void setMessagesMaximum(int var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("MessagesMaximum", new Integer(var1));
      } else {
         this.delegate.getClientParams().setMessagesMaximum(var1);
      }

   }

   public String getOverrunPolicy() {
      if (this.delegate == null) {
         Object var1 = this.getValue("OverrunPolicy");
         return var1 != null && var1 instanceof String ? (String)var1 : "KeepOld";
      } else {
         return this.delegate.getClientParams().getMulticastOverrunPolicy();
      }
   }

   public void setOverrunPolicy(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("OverrunPolicy", var1);
      } else {
         this.delegate.getClientParams().setMulticastOverrunPolicy(var1);
      }

   }

   public int getDefaultPriority() {
      if (this.delegate == null) {
         Object var1 = this.getValue("DefaultPriority");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 4;
      } else {
         return this.delegate.getDefaultDeliveryParams().getDefaultPriority();
      }
   }

   public void setDefaultPriority(int var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("DefaultPriority", new Integer(var1));
      } else {
         this.delegate.getDefaultDeliveryParams().setDefaultPriority(var1);
      }

   }

   public long getDefaultTimeToDeliver() {
      if (this.delegate == null) {
         Object var2 = this.getValue("DefaultTimeToDeliver");
         return var2 != null && var2 instanceof Long ? (Long)var2 : 0L;
      } else {
         Long var1 = new Long(this.delegate.getDefaultDeliveryParams().getDefaultTimeToDeliver());
         return var1;
      }
   }

   public void setDefaultTimeToDeliver(long var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("DefaultTimeToDeliver", new Long(var1));
      } else {
         this.delegate.getDefaultDeliveryParams().setDefaultTimeToDeliver("" + var1);
      }

   }

   public long getDefaultTimeToLive() {
      if (this.delegate == null) {
         Object var1 = this.getValue("DefaultTimeToLive");
         return var1 != null && var1 instanceof Long ? (Long)var1 : 0L;
      } else {
         return this.delegate.getDefaultDeliveryParams().getDefaultTimeToLive();
      }
   }

   public void setDefaultTimeToLive(long var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("DefaultTimeToLive", new Long(var1));
      } else {
         this.delegate.getDefaultDeliveryParams().setDefaultTimeToLive(var1);
      }

   }

   public long getSendTimeout() {
      if (this.delegate == null) {
         Object var1 = this.getValue("SendTimeout");
         return var1 != null && var1 instanceof Long ? (Long)var1 : 10L;
      } else {
         return this.delegate.getDefaultDeliveryParams().getSendTimeout();
      }
   }

   public void setSendTimeout(long var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("SendTimeout", new Long(var1));
      } else {
         this.delegate.getDefaultDeliveryParams().setSendTimeout(var1);
      }

   }

   public String getDefaultDeliveryMode() {
      if (this.delegate == null) {
         Object var1 = this.getValue("DefaultDeliveryMode");
         return var1 != null && var1 instanceof String ? (String)var1 : "Persistent";
      } else {
         return this.delegate.getDefaultDeliveryParams().getDefaultDeliveryMode();
      }
   }

   public void setDefaultDeliveryMode(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("DefaultDeliveryMode", var1);
      } else {
         this.delegate.getDefaultDeliveryParams().setDefaultDeliveryMode(var1);
      }

   }

   public long getDefaultRedeliveryDelay() {
      if (this.delegate == null) {
         Object var1 = this.getValue("DefaultRedeliveryDelay");
         return var1 != null && var1 instanceof Long ? (Long)var1 : 0L;
      } else {
         return this.delegate.getDefaultDeliveryParams().getDefaultRedeliveryDelay();
      }
   }

   public void setDefaultRedeliveryDelay(long var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("DefaultRedeliveryDelay", new Long(var1));
      } else {
         this.delegate.getDefaultDeliveryParams().setDefaultRedeliveryDelay(var1);
      }

   }

   public long getTransactionTimeout() {
      if (this.delegate == null) {
         Object var1 = this.getValue("TransactionTimeout");
         return var1 != null && var1 instanceof Long ? (Long)var1 : 3600L;
      } else {
         return this.delegate.getTransactionParams().getTransactionTimeout();
      }
   }

   public void setTransactionTimeout(long var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("TransactionTimeout", new Long(var1));
      } else {
         this.delegate.getTransactionParams().setTransactionTimeout(var1);
      }

   }

   public boolean isUserTransactionsEnabled() {
      if (this.delegate == null) {
         Object var1 = this.getValue("UserTransactionsEnabled");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : false;
      } else {
         return this.delegate.getTransactionParams().isXAConnectionFactoryEnabled();
      }
   }

   public void setUserTransactionsEnabled(boolean var1) throws InvalidAttributeValueException {
      if (var1) {
         JMSLogger.logUserTransactionsEnabledDeprecated(this.getMbean().getName());
      }

      if (this.delegate == null) {
         this.putValue("UserTransactionsEnabled", new Boolean(var1));
      } else {
         this.delegate.getTransactionParams().setXAConnectionFactoryEnabled(var1);
      }

   }

   public boolean isXAConnectionFactoryEnabled() {
      if (this.delegate == null) {
         Object var1 = this.getValue("XAConnectionFactoryEnabled");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : false;
      } else {
         return this.delegate.getTransactionParams().isXAConnectionFactoryEnabled();
      }
   }

   public void setXAConnectionFactoryEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("XAConnectionFactoryEnabled", new Boolean(var1));
      } else {
         this.delegate.getTransactionParams().setXAConnectionFactoryEnabled(var1);
      }

   }

   public boolean isXAServerEnabled() {
      if (this.delegate == null) {
         Object var1 = this.getValue("XAServerEnabled");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : false;
      } else {
         return this.delegate.getTransactionParams().isXAConnectionFactoryEnabled();
      }
   }

   public void setXAServerEnabled(boolean var1) throws InvalidAttributeValueException {
      if (var1) {
         JMSLogger.logXAServerEnabledDeprecated(this.getMbean().getName());
      }

      if (this.delegate == null) {
         this.putValue("XAServerEnabled", new Boolean(var1));
      } else {
         this.delegate.getTransactionParams().setXAConnectionFactoryEnabled(var1);
      }

   }

   public boolean getLoadBalancingEnabled() {
      if (this.delegate == null) {
         Object var1 = this.getValue("LoadBalancingEnabled");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : true;
      } else {
         return this.delegate.getLoadBalancingParams().isLoadBalancingEnabled();
      }
   }

   public void setLoadBalancingEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("LoadBalancingEnabled", new Boolean(var1));
      } else {
         this.delegate.getLoadBalancingParams().setLoadBalancingEnabled(var1);
      }

   }

   public boolean getServerAffinityEnabled() {
      if (this.delegate == null) {
         Object var1 = this.getValue("ServerAffinityEnabled");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : true;
      } else {
         return this.delegate.getLoadBalancingParams().isServerAffinityEnabled();
      }
   }

   public void setServerAffinityEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("ServerAffinityEnabled", new Boolean(var1));
      } else {
         this.delegate.getLoadBalancingParams().setServerAffinityEnabled(var1);
      }

   }

   public boolean getAttachJMSXUserID() {
      if (this.delegate == null) {
         Object var1 = this.getValue("AttachJMSXUserID");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : false;
      } else {
         return this.delegate.getSecurityParams().isAttachJMSXUserId();
      }
   }

   public void setAttachJMSXUserID(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("AttachJMSXUserID", new Boolean(var1));
      } else {
         this.delegate.getSecurityParams().setAttachJMSXUserId(var1);
      }

   }

   public int getFlowMinimum() {
      if (this.delegate == null) {
         Object var1 = this.getValue("FlowMinimum");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 50;
      } else {
         return this.delegate.getFlowControlParams().getFlowMinimum();
      }
   }

   public void setFlowMinimum(int var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("FlowMinimum", new Integer(var1));
      } else {
         this.delegate.getFlowControlParams().setFlowMinimum(var1);
      }

   }

   public int getFlowMaximum() {
      if (this.delegate == null) {
         Object var1 = this.getValue("FlowMaximum");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 500;
      } else {
         return this.delegate.getFlowControlParams().getFlowMaximum();
      }
   }

   public void setFlowMaximum(int var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("FlowMaximum", new Integer(var1));
      } else {
         this.delegate.getFlowControlParams().setFlowMaximum(var1);
      }

   }

   public int getFlowSteps() {
      if (this.delegate == null) {
         Object var1 = this.getValue("FlowSteps");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 10;
      } else {
         return this.delegate.getFlowControlParams().getFlowSteps();
      }
   }

   public void setFlowSteps(int var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("FlowSteps", new Integer(var1));
      } else {
         this.delegate.getFlowControlParams().setFlowSteps(var1);
      }

   }

   public int getFlowInterval() {
      if (this.delegate == null) {
         Object var1 = this.getValue("FlowInterval");
         return var1 != null && var1 instanceof Integer ? (Integer)var1 : 60;
      } else {
         return this.delegate.getFlowControlParams().getFlowInterval();
      }
   }

   public void setFlowInterval(int var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("FlowInterval", new Integer(var1));
      } else {
         this.delegate.getFlowControlParams().setFlowInterval(var1);
      }

   }

   public boolean getFlowControlEnabled() {
      if (this.delegate == null) {
         Object var1 = this.getValue("FlowControlEnabled");
         return var1 != null && var1 instanceof Boolean ? (Boolean)var1 : true;
      } else {
         return this.delegate.getFlowControlParams().isFlowControlEnabled();
      }
   }

   public void setFlowControlEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("FlowControlEnabled", new Boolean(var1));
      } else {
         this.delegate.getFlowControlParams().setFlowControlEnabled(var1);
      }

   }

   public String getNotes() {
      if (this.delegate == null) {
         Object var1 = this.getValue("Notes");
         return var1 != null && var1 instanceof String ? (String)var1 : null;
      } else {
         return this.delegate.getNotes();
      }
   }

   public void setNotes(String var1) throws InvalidAttributeValueException {
      if (this.delegate == null) {
         this.putValue("Notes", var1);
      } else {
         this.delegate.setNotes(var1);
      }

   }
}
