package weblogic.j2ee.extensions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public enum ExtensionManager {
   instance;

   private List<ExtensionData> diExtensions = new ArrayList();

   public void addInjectionExtension(String var1, String var2, InjectionExtension var3) {
      this.diExtensions.add(new ExtensionData(var1, var2, var3));
   }

   public InjectionExtension getFirstMatchingExtension(String var1, String var2) {
      Iterator var3 = this.diExtensions.iterator();

      ExtensionData var4;
      do {
         do {
            if (!var3.hasNext()) {
               return null;
            }

            var4 = (ExtensionData)var3.next();
         } while(!var1.matches(var4.typeToMatch));
      } while(var2 != null && var2.length() != 0 && !var2.matches(var4.nameToMatch));

      return var4.e;
   }

   private class ExtensionData {
      private final String typeToMatch;
      private final String nameToMatch;
      private final InjectionExtension e;

      private ExtensionData(String var2, String var3, InjectionExtension var4) {
         this.typeToMatch = var2;
         this.nameToMatch = var3;
         this.e = var4;
      }

      // $FF: synthetic method
      ExtensionData(String var2, String var3, InjectionExtension var4, Object var5) {
         this(var2, var3, var4);
      }
   }
}
