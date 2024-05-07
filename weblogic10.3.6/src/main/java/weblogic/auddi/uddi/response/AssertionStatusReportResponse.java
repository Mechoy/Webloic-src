package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.AssertionStatusItem;
import weblogic.auddi.uddi.datastructure.AssertionStatusItems;

public class AssertionStatusReportResponse extends UDDIResponse {
   private AssertionStatusItems assertionStatusItems;

   public void addAssertionStatusItem(AssertionStatusItem var1) throws UDDIException {
      if (this.assertionStatusItems == null) {
         this.assertionStatusItems = new AssertionStatusItems();
      }

      this.assertionStatusItems.add(var1);
   }

   public void setAssertionStatusItems(AssertionStatusItems var1) {
      this.assertionStatusItems = var1;
   }

   public AssertionStatusItems getAssertionStatusItems() {
      if (this.assertionStatusItems == null) {
         this.assertionStatusItems = new AssertionStatusItems();
      }

      return this.assertionStatusItems;
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof AssertionStatusReportResponse)) {
         return false;
      } else {
         AssertionStatusReportResponse var2 = (AssertionStatusReportResponse)var1;
         boolean var3 = true;
         var3 &= this.assertionStatusItems == null ? var2.assertionStatusItems == null : this.assertionStatusItems.hasEqualContent(var2.assertionStatusItems);
         return var3;
      }
   }

   public String toXML() {
      StringBuffer var1 = new StringBuffer();
      var1.append("<assertionStatusReport" + super.toXML() + ">");
      if (this.assertionStatusItems != null) {
         var1.append(this.assertionStatusItems.toXML());
      }

      var1.append("</assertionStatusReport>");
      return var1.toString();
   }
}
