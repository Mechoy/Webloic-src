package weblogic.wsee.callback.controls;

import java.util.Set;

public interface ControlCallbackReferenceData {
   String get(String var1);

   void put(String var1, String var2);

   Set<String> keySet();
}
