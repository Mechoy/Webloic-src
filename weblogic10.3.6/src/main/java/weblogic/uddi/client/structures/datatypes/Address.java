package weblogic.uddi.client.structures.datatypes;

import java.util.Vector;

public class Address {
   private String useType = null;
   private String sortCode = null;
   private Vector addressLineVector = new Vector();

   public String getUseType() {
      return this.useType;
   }

   public void setUseType(String var1) {
      this.useType = new String(var1);
   }

   public String getSortCode() {
      return this.sortCode;
   }

   public void setSortCode(String var1) {
      this.sortCode = new String(var1);
   }

   public Vector getAddressLineVector() {
      return this.addressLineVector;
   }

   public void setAddressLineVector(Vector var1) {
      this.addressLineVector = var1;
   }

   public void addAddressLine(String var1) {
      AddressLine var2 = new AddressLine(var1);
      this.addressLineVector.add(var2);
   }
}
