package weblogic.connector.monitoring.inbound;

import java.util.HashSet;
import java.util.Set;
import weblogic.connector.inbound.RAInboundManager;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ConnectorInboundRuntimeMBean;
import weblogic.management.runtime.MessageDrivenEJBRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public class ConnectorInboundRuntimeMBeanImpl extends RuntimeMBeanDelegate implements ConnectorInboundRuntimeMBean {
   private String msgListenerType;
   private String activationSpecClass;
   private Set mdbRuntimes;
   private RAInboundManager raInboundManager;

   public ConnectorInboundRuntimeMBeanImpl(String var1, String var2, String var3, RuntimeMBean var4, RAInboundManager var5) throws ManagementException {
      super(var1, var4, false);
      this.msgListenerType = var2;
      this.activationSpecClass = var3;
      this.mdbRuntimes = new HashSet();
      this.raInboundManager = var5;
      this.register();
   }

   public String getMsgListenerType() {
      return this.msgListenerType;
   }

   public String getActivationSpecClass() {
      return this.activationSpecClass;
   }

   public MessageDrivenEJBRuntimeMBean[] getMDBRuntimes() {
      return (MessageDrivenEJBRuntimeMBean[])((MessageDrivenEJBRuntimeMBean[])this.mdbRuntimes.toArray(new MessageDrivenEJBRuntimeMBean[this.mdbRuntimes.size()]));
   }

   public boolean addMDBRuntime(MessageDrivenEJBRuntimeMBean var1) {
      boolean var2 = false;
      if (var1 != null) {
         var2 = this.mdbRuntimes.add(var1);
      }

      return var2;
   }

   public boolean removeMDBRuntime(MessageDrivenEJBRuntimeMBean var1) {
      boolean var2 = false;
      if (var1 != null) {
         var2 = this.mdbRuntimes.remove(var1);
      }

      return var2;
   }

   public void removeAllMDBRuntimes() {
      this.mdbRuntimes.clear();
   }

   public String getState() {
      return this.raInboundManager.getState();
   }
}
