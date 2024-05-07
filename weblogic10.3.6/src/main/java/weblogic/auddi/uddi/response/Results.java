package weblogic.auddi.uddi.response;

import weblogic.auddi.uddi.datastructure.UDDIList;

public class Results extends UDDIList {
   public void add(Result var1) {
      super.add(var1);
   }

   public Result getFirst() {
      return (Result)super.getVFirst();
   }

   public Result getNext() {
      return (Result)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
