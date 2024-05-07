package weblogic.auddi.uddi.datastructure;

import java.util.HashMap;
import weblogic.auddi.uddi.LanguageErrorException;
import weblogic.auddi.uddi.UDDIException;
import weblogic.auddi.uddi.UDDIMessages;

public class UniqueNames extends Names {
   HashMap m_map = null;

   public UniqueNames() {
   }

   public UniqueNames(UniqueNames var1) throws UDDIException {
      if (var1 == null) {
         throw new IllegalArgumentException(UDDIMessages.get("error.runtime.constructor.null"));
      } else {
         for(Name var2 = var1.getFirst(); var2 != null; var2 = var1.getNext()) {
            this.add(new Name(var2));
         }

      }
   }

   public void remove(Name var1) throws UDDIException {
      super.remove(var1);
      String var2 = var1.getLang().toString();
      this.m_map.remove(var2);
   }

   public void add(Name var1) throws UDDIException {
      if (this.m_map == null) {
         this.m_map = new HashMap();
      }

      Language var2 = var1.getLang();
      String var3 = null;
      if (var2 != null) {
         var3 = var2.toString();
      }

      if (this.m_map.get(var3) != null) {
         throw new LanguageErrorException(UDDIMessages.get("error.languageError.name", var3));
      } else {
         super.add(var1);
         this.m_map.put(var3, var1);
      }
   }
}
