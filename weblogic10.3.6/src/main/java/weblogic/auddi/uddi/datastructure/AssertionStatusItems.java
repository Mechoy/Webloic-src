package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;

public class AssertionStatusItems extends UDDIList {
   public void add(AssertionStatusItem var1) throws UDDIException {
      super.add(var1);
   }

   public AssertionStatusItem getFirst() {
      return (AssertionStatusItem)super.getVFirst();
   }

   public AssertionStatusItem getNext() {
      return (AssertionStatusItem)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
