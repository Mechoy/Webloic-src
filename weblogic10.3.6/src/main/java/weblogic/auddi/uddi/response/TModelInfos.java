package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.datastructure.UDDIList;

public class TModelInfos extends UDDIList {
   public void add(TModelInfo var1) throws UDDIException {
      super.add(var1);
   }

   public TModelInfo getFirst() {
      return (TModelInfo)super.getVFirst();
   }

   public TModelInfo getNext() {
      return (TModelInfo)super.getVNext();
   }

   public String toXML() {
      return super.toXML("tModelInfos");
   }
}
