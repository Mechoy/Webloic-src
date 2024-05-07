package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import weblogic.auddi.uddi.UDDIException;

public class Names extends UDDIList implements Serializable {
   public Names() {
   }

   public Names(Names var1) throws UDDIException {
      super(var1);
   }

   public Name getFirst() {
      return (Name)super.getVFirst();
   }

   public Name getNext() {
      return (Name)super.getVNext();
   }

   public String toXML() {
      return super.toXML("");
   }
}
