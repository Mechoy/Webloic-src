package weblogic.management.descriptors.cmp20;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class OptimisticConcurrencyMBeanImpl extends XMLElementMBeanDelegate implements OptimisticConcurrencyMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_verifyFields = false;
   private String verifyFields;

   public String getVerifyFields() {
      return this.verifyFields;
   }

   public void setVerifyFields(String var1) {
      if (var1 != null && var1.trim().length() == 0) {
         var1 = null;
      }

      String var2 = this.verifyFields;
      this.verifyFields = var1;
      this.isSet_verifyFields = var1 != null;
      this.checkChange("verifyFields", var2, this.verifyFields);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<optimistic-concurrency");
      var2.append(">\n");
      if (null != this.getVerifyFields()) {
         var2.append(ToXML.indent(var1 + 2)).append("<verify-fields>").append(this.getVerifyFields()).append("</verify-fields>\n");
      }

      var2.append(ToXML.indent(var1)).append("</optimistic-concurrency>\n");
      return var2.toString();
   }
}
