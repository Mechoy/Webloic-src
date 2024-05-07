package weblogic.management.descriptors.webservice;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class ReliableDeliveryMBeanImpl extends XMLElementMBeanDelegate implements ReliableDeliveryMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_persistDuration = false;
   private int persistDuration = 600;
   private boolean isSet_inOrderDelivery = false;
   private boolean inOrderDelivery = false;
   private boolean isSet_retryInterval = false;
   private int retryInterval = 10;
   private boolean isSet_duplicateElimination = false;
   private boolean duplicateElimination = true;
   private boolean isSet_retries = false;
   private int retries = 60;

   public int getPersistDuration() {
      return this.persistDuration;
   }

   public void setPersistDuration(int var1) {
      int var2 = this.persistDuration;
      this.persistDuration = var1;
      this.isSet_persistDuration = var1 != -1;
      this.checkChange("persistDuration", var2, this.persistDuration);
   }

   public boolean isInOrderDelivery() {
      return this.inOrderDelivery;
   }

   public void setInOrderDelivery(boolean var1) {
      boolean var2 = this.inOrderDelivery;
      this.inOrderDelivery = var1;
      this.isSet_inOrderDelivery = true;
      this.checkChange("inOrderDelivery", var2, this.inOrderDelivery);
   }

   public int getRetryInterval() {
      return this.retryInterval;
   }

   public void setRetryInterval(int var1) {
      int var2 = this.retryInterval;
      this.retryInterval = var1;
      this.isSet_retryInterval = var1 != -1;
      this.checkChange("retryInterval", var2, this.retryInterval);
   }

   public boolean isDuplicateElimination() {
      return this.duplicateElimination;
   }

   public void setDuplicateElimination(boolean var1) {
      boolean var2 = this.duplicateElimination;
      this.duplicateElimination = var1;
      this.isSet_duplicateElimination = true;
      this.checkChange("duplicateElimination", var2, this.duplicateElimination);
   }

   public int getRetries() {
      return this.retries;
   }

   public void setRetries(int var1) {
      int var2 = this.retries;
      this.retries = var1;
      this.isSet_retries = var1 != -1;
      this.checkChange("retries", var2, this.retries);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<reliable-delivery");
      if (this.isSet_persistDuration) {
         var2.append(" persist-duration=\"").append(String.valueOf(this.getPersistDuration())).append("\"");
      }

      if (this.isSet_duplicateElimination) {
         var2.append(" duplicate-elimination=\"").append(String.valueOf(this.isDuplicateElimination())).append("\"");
      }

      if (this.isSet_inOrderDelivery) {
         var2.append(" in-order-delivery=\"").append(String.valueOf(this.isInOrderDelivery())).append("\"");
      }

      if (this.isSet_retryInterval) {
         var2.append(" retry-interval=\"").append(String.valueOf(this.getRetryInterval())).append("\"");
      }

      if (this.isSet_retries) {
         var2.append(" retries=\"").append(String.valueOf(this.getRetries())).append("\"");
      }

      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</reliable-delivery>\n");
      return var2.toString();
   }
}
