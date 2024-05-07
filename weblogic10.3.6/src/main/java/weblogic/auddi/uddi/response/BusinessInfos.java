package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIList;

public class BusinessInfos extends UDDIList {
   public void add(BusinessInfo var1) throws UDDIException {
      super.add(var1);
   }

   public BusinessInfo getFirst() {
      return (BusinessInfo)super.getVFirst();
   }

   public BusinessInfo getNext() {
      return (BusinessInfo)super.getVNext();
   }

   public String toXML() {
      return super.toXML("businessInfos");
   }
}
