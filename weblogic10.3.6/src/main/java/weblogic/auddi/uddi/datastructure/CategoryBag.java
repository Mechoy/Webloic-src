package weblogic.auddi.uddi.datastructure;

import weblogic.auddi.uddi.UDDIException;

public class CategoryBag extends KRBag {
   public CategoryBag() {
   }

   public CategoryBag(CategoryBag var1) throws UDDIException {
      super(var1);
   }

   public String toXML() {
      return super.toXML("categoryBag");
   }
}
