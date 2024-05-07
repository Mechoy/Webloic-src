package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;

public class BusinessEntityExts extends UDDIList {
   public void add(BusinessEntityExt var1) throws UDDIException {
      super.add(var1);
   }

   public BusinessEntityExt getFirst() {
      return (BusinessEntityExt)super.getVFirst();
   }

   public BusinessEntityExt getNext() {
      return (BusinessEntityExt)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
