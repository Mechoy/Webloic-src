package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;

public class TModels extends UDDIList {
   public void add(TModel var1) throws UDDIException {
      super.add(var1);
   }

   public TModel getFirst() {
      return (TModel)super.getVFirst();
   }

   public TModel getNext() {
      return (TModel)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
