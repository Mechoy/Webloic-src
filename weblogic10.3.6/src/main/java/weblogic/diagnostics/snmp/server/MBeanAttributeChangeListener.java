package weblogic.diagnostics.snmp.server;

import java.util.Date;
import java.util.LinkedList;
import javax.management.AttributeChangeNotification;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.ObjectName;
import weblogic.diagnostics.snmp.agent.SNMPAgent;
import weblogic.diagnostics.snmp.agent.SNMPAgentToolkitException;
import weblogic.diagnostics.snmp.agent.SNMPNotificationManager;
import weblogic.diagnostics.snmp.i18n.SNMPLogger;

public class MBeanAttributeChangeListener extends JMXMonitorListener {
   private static final String ATTR_CHANGE_TRAP = "wlsAttributeChange";
   private static final String TRAP_ATTR_TYPE = "trapAttributeType";
   private static final String TRAP_ATTR_CHANGE_TYPE = "trapAttributeChangeType";
   private static final String TRAP_ATTR_OLD_VALUE = "trapAttributeOldVal";
   private static final String TRAP_ATTR_NEW_VALUE = "trapAttributeNewVal";

   public MBeanAttributeChangeListener(JMXMonitorLifecycle var1, SNMPAgent var2, String var3, String var4, String var5, String var6, String var7) throws MalformedObjectNameException {
      super(var1, var2, var3, var4, var5, var6, var7);
   }

   public void handleNotification(Notification var1, Object var2) {
      AttributeChangeNotification var3 = (AttributeChangeNotification)var1;
      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Got attribute change notification " + var3 + " from " + var3.getSource());
      }

      Object var4 = var3.getOldValue();
      Object var5 = var3.getNewValue();
      SNMPNotificationManager var6 = this.snmpAgent.getSNMPAgentToolkit().getSNMPNotificationManager();
      String var7 = this.snmpAgent.getNotifyGroup();
      LinkedList var8 = new LinkedList();
      String var9 = var3.getAttributeType();
      String var10 = "UPDATE";
      boolean var11 = false;
      if (var3.getNewValue() != null) {
         var11 = var3.getNewValue().getClass().isArray();
      }

      if (var3.getOldValue() != null) {
         var11 = var11 || var3.getOldValue().getClass().isArray();
      }

      if (var11) {
         Object[] var12 = (Object[])((Object[])var3.getOldValue());
         Object[] var13 = (Object[])((Object[])var3.getNewValue());
         int var14 = var12 == null ? 0 : var12.length;
         int var15 = var13 == null ? 0 : var13.length;
         if (var14 > var15) {
            var10 = "REMOVE";
         } else if (var14 < var15) {
            var10 = "ADD";
         }

         var4 = this.toString(var12);
         var5 = this.toString(var13);
      }

      Object var17 = var3.getSource();
      if (var17 == null) {
         var17 = "";
      }

      if (var17 instanceof ObjectName) {
         var17 = ((ObjectName)var17).getCanonicalName();
      }

      var8.add(new Object[]{"trapTime", (new Date()).toString()});
      var8.add(new Object[]{"trapServerName", this.serverName});
      var8.add(new Object[]{"trapMBeanName", var17.toString()});
      var8.add(new Object[]{"trapMBeanType", this.typeName});
      var8.add(new Object[]{"trapAttributeName", this.attributeName});
      var8.add(new Object[]{"trapAttributeType", var9});
      var8.add(new Object[]{"trapAttributeChangeType", var10});
      var8.add(new Object[]{"trapAttributeOldVal", "" + var4});
      var8.add(new Object[]{"trapAttributeNewVal", "" + var5});

      try {
         var6.sendNotification(var7, "wlsAttributeChange", var8);
         this.updateMonitorTrapCount();
      } catch (SNMPAgentToolkitException var16) {
         SNMPLogger.logMonitorNotificationError(this.serverName, this.typeName, this.mbeanName, var16);
      }

   }

   public boolean isNotificationEnabled(Notification var1) {
      if (var1 instanceof AttributeChangeNotification) {
         AttributeChangeNotification var2 = (AttributeChangeNotification)var1;
         if (var2.getAttributeName().equals(this.attributeName)) {
            return true;
         }
      }

      if (DEBUG.isDebugEnabled()) {
         DEBUG.debug("Filtering notification " + var1);
      }

      return false;
   }

   void updateMonitorTrapCount() {
      if (this.snmpStats != null) {
         this.snmpStats.incrementAttributeChangeTrapCount();
      }

   }

   private String toString(Object[] var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("{");
      if (var1 != null) {
         Object[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            var2.append("[");
            var2.append(var6 != null ? var6.toString() : "");
            var2.append("]");
         }
      }

      var2.append("}");
      return var2.toString();
   }
}
