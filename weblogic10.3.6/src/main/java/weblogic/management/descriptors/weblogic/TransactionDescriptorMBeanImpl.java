package weblogic.management.descriptors.weblogic;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class TransactionDescriptorMBeanImpl extends XMLElementMBeanDelegate implements TransactionDescriptorMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_transTimeoutSeconds = false;
   private int transTimeoutSeconds = 0;

   public int getTransTimeoutSeconds() {
      return this.transTimeoutSeconds;
   }

   public void setTransTimeoutSeconds(int var1) {
      int var2 = this.transTimeoutSeconds;
      this.transTimeoutSeconds = var1;
      this.isSet_transTimeoutSeconds = var1 != -1;
      this.checkChange("transTimeoutSeconds", var2, this.transTimeoutSeconds);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<transaction-descriptor");
      var2.append(">\n");
      if (this.isSet_transTimeoutSeconds || 0 != this.getTransTimeoutSeconds()) {
         var2.append(ToXML.indent(var1 + 2)).append("<trans-timeout-seconds>").append(this.getTransTimeoutSeconds()).append("</trans-timeout-seconds>\n");
      }

      var2.append(ToXML.indent(var1)).append("</transaction-descriptor>\n");
      return var2.toString();
   }
}
