package weblogic.management.descriptors.ejb20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class MessageDrivenDestinationMBeanImpl extends XMLElementMBeanDelegate implements MessageDrivenDestinationMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_subscriptionDurability = false;
   private String subscriptionDurability;
   private boolean isSet_destinationType = false;
   private String destinationType;

   public String getSubscriptionDurability() {
      return this.subscriptionDurability;
   }

   public void setSubscriptionDurability(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.subscriptionDurability;
      this.subscriptionDurability = var1;
      this.isSet_subscriptionDurability = var1 != null;
      this.checkChange("subscriptionDurability", var2, this.subscriptionDurability);
   }

   public String getDestinationType() {
      return this.destinationType;
   }

   public void setDestinationType(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.destinationType;
      this.destinationType = var1;
      this.isSet_destinationType = var1 != null;
      this.checkChange("destinationType", var2, this.destinationType);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<message-driven-destination");
      var2.append(">\n");
      if (null != this.getDestinationType()) {
         var2.append(ToXML.indent(var1 + 2)).append("<destination-type>").append(this.getDestinationType()).append("</destination-type>\n");
      }

      if (null != this.getSubscriptionDurability()) {
         var2.append(ToXML.indent(var1 + 2)).append("<subscription-durability>").append(this.getSubscriptionDurability()).append("</subscription-durability>\n");
      }

      var2.append(ToXML.indent(var1)).append("</message-driven-destination>\n");
      return var2.toString();
   }
}
