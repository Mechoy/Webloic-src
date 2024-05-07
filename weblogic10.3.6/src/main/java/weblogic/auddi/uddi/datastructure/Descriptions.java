package weblogic.auddi.uddi.datastructure;

import java.io.Serializable;
import java.util.HashMap;
import weblogic.auddi.uddi.LanguageErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;
import weblogic.auddi.util.Util;

public class Descriptions extends UDDIList implements Serializable {
   HashMap m_map = null;

   public Descriptions() {
   }

   public Descriptions(Descriptions var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(Description var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new Description(var2));
         }

      }
   }

   public void remove(Description var1) throws UDDIException {
      super.remove(var1);
      String var2 = var1.getLang().toString();
      this.m_map.remove(var2);
   }

   public void add(Description var1) throws UDDIException {
      if (this.m_map == null) {
         this.m_map = new HashMap();
      }

      String var2 = var1.getLang().toString();
      if (this.m_map.get(var2) != null) {
         throw new LanguageErrorException(UDDIMessages.get("error.languageError.description", var2));
      } else {
         super.add(var1);
         this.m_map.put(var2, var1);
      }
   }

   public Description getFirst() {
      return (Description)super.getVFirst();
   }

   public Description getNext() {
      return (Description)super.getVNext();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof Descriptions)) {
         return false;
      } else {
         Descriptions var2 = (Descriptions)var1;
         boolean var3 = true;
         var3 &= Util.isEqual((Object)this.m_map, (Object)var2.m_map);
         return var3;
      }
   }

   public String toXML() {
      return super.toXML("");
   }
}
