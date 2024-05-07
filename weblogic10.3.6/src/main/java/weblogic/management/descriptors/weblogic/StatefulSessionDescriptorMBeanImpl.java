package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class StatefulSessionDescriptorMBeanImpl extends XMLElementMBeanDelegate implements StatefulSessionDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_allowRemoveDuringTransaction = false;
   private boolean allowRemoveDuringTransaction = false;
   private boolean isSet_allowConcurrentCalls = false;
   private boolean allowConcurrentCalls = false;
   private boolean isSet_statefulSessionClustering = false;
   private StatefulSessionClusteringMBean statefulSessionClustering;
   private boolean isSet_statefulSessionCache = false;
   private StatefulSessionCacheMBean statefulSessionCache;
   private boolean isSet_persistentStoreDir = false;
   private String persistentStoreDir;

   public boolean getAllowRemoveDuringTransaction() {
      return this.allowRemoveDuringTransaction;
   }

   public void setAllowRemoveDuringTransaction(boolean var1) {
      boolean var2 = this.allowRemoveDuringTransaction;
      this.allowRemoveDuringTransaction = var1;
      this.isSet_allowRemoveDuringTransaction = true;
      this.checkChange("allowRemoveDuringTransaction", var2, this.allowRemoveDuringTransaction);
   }

   public boolean getAllowConcurrentCalls() {
      return this.allowConcurrentCalls;
   }

   public void setAllowConcurrentCalls(boolean var1) {
      boolean var2 = this.allowConcurrentCalls;
      this.allowConcurrentCalls = var1;
      this.isSet_allowConcurrentCalls = true;
      this.checkChange("allowConcurrentCalls", var2, this.allowConcurrentCalls);
   }

   public StatefulSessionClusteringMBean getStatefulSessionClustering() {
      return this.statefulSessionClustering;
   }

   public void setStatefulSessionClustering(StatefulSessionClusteringMBean var1) {
      StatefulSessionClusteringMBean var2 = this.statefulSessionClustering;
      this.statefulSessionClustering = var1;
      this.isSet_statefulSessionClustering = var1 != null;
      this.checkChange("statefulSessionClustering", var2, this.statefulSessionClustering);
   }

   public StatefulSessionCacheMBean getStatefulSessionCache() {
      return this.statefulSessionCache;
   }

   public void setStatefulSessionCache(StatefulSessionCacheMBean var1) {
      StatefulSessionCacheMBean var2 = this.statefulSessionCache;
      this.statefulSessionCache = var1;
      this.isSet_statefulSessionCache = var1 != null;
      this.checkChange("statefulSessionCache", var2, this.statefulSessionCache);
   }

   public String getPersistentStoreDir() {
      return this.persistentStoreDir;
   }

   public void setPersistentStoreDir(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.persistentStoreDir;
      this.persistentStoreDir = var1;
      this.isSet_persistentStoreDir = var1 != null;
      this.checkChange("persistentStoreDir", var2, this.persistentStoreDir);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<stateful-session-descriptor");
      var2.append(">\n");
      if (null != this.getStatefulSessionCache()) {
         var2.append(this.getStatefulSessionCache().toXML(var1 + 2)).append("\n");
      }

      if (null != this.getPersistentStoreDir()) {
         var2.append(ToXML.indent(var1 + 2)).append("<persistent-store-dir>").append(this.getPersistentStoreDir()).append("</persistent-store-dir>\n");
      }

      if (null != this.getStatefulSessionClustering()) {
         var2.append(this.getStatefulSessionClustering().toXML(var1 + 2)).append("\n");
      }

      if (this.isSet_allowConcurrentCalls || this.getAllowConcurrentCalls()) {
         var2.append(ToXML.indent(var1 + 2)).append("<allow-concurrent-calls>").append(ToXML.capitalize(Boolean.valueOf(this.getAllowConcurrentCalls()).toString())).append("</allow-concurrent-calls>\n");
      }

      if (this.isSet_allowRemoveDuringTransaction || this.getAllowRemoveDuringTransaction()) {
         var2.append(ToXML.indent(var1 + 2)).append("<allow-remove-during-transaction>").append(ToXML.capitalize(Boolean.valueOf(this.getAllowRemoveDuringTransaction()).toString())).append("</allow-remove-during-transaction>\n");
      }

      var2.append(ToXML.indent(var1)).append("</stateful-session-descriptor>\n");
      return var2.toString();
   }
}
