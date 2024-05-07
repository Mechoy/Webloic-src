package weblogic.auddi.uddi.datastructure;

public class TModelBag extends UDDIList {
   public void add(TModelKey var1) {
      super.add(var1);
   }

   public TModelKey getFirst() {
      return (TModelKey)super.getVFirst();
   }

   public TModelKey getNext() {
      return (TModelKey)super.getVNext();
   }

   public String toXML() {
      return super.toXML("tModelBag");
   }
}
