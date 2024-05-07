package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;

public class BusinessEntities extends UDDIList {
   public void add(BusinessEntity var1) throws UDDIException {
      super.add(var1);
   }

   public BusinessEntity getFirst() {
      return (BusinessEntity)super.getVFirst();
   }

   public BusinessEntity getNext() {
      return (BusinessEntity)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
