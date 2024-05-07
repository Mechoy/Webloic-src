package weblogic.wsee.callback.controls;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class GenericControlCallbackReferenceData implements ControlCallbackReferenceData, Serializable {
   private HashMap<String, String> keyValues = new HashMap();

   public Set<String> keySet() {
      return this.keyValues == null ? null : this.keyValues.keySet();
   }

   public String get(String var1) {
      return var1 == null ? null : (String)this.keyValues.get(var1);
   }

   public void put(String var1, String var2) {
      if (var1 != null) {
         if (var2 != null) {
            this.keyValues.put(var1, var2);
         }
      }
   }
}
