package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIList;

public class RelatedBusinessInfos extends UDDIList {
   public void add(RelatedBusinessInfo var1) throws UDDIException {
      super.add(var1);
   }

   public RelatedBusinessInfo getFirst() {
      return (RelatedBusinessInfo)super.getVFirst();
   }

   public RelatedBusinessInfo getNext() {
      return (RelatedBusinessInfo)super.getVNext();
   }

   public String toXML() {
      return super.toXML("relatedBusinessInfos");
   }
}
