package weblogic.diagnostics.instrumentation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import weblogic.diagnostics.instrumentation.engine.InstrumentationEngineConfiguration;
import weblogic.diagnostics.instrumentation.rtsupport.InstrumentationSupportImpl;
import weblogic.utils.classloaders.ClassPreProcessor;
import weblogic.utils.classloaders.GenericClassLoader;

public class DiagnosticClassPreProcessor implements ClassPreProcessor {
   private Map classLoaderMap = new WeakHashMap();
   private InstrumentationManager manager = InstrumentationManager.getInstrumentationManager();
   private static final InstrumentationScopeBundle VOID_BUNDLE = new InstrumentationScopeBundle();
   private static final boolean DEBUG = false;

   public void initialize(Hashtable var1) {
   }

   public byte[] preProcess(String var1, byte[] var2) {
      if (!this.isEligibleClass(var1)) {
         return var2;
      } else {
         ClassLoader var3 = Thread.currentThread().getContextClassLoader();

         ClassLoader var4;
         for(var4 = var3; var4 != null && !(var4 instanceof GenericClassLoader); var4 = var4.getParent()) {
         }

         if (var4 == null) {
            return var2;
         } else {
            if (var4 instanceof GenericClassLoader) {
               GenericClassLoader var5 = (GenericClassLoader)var4;
               InstrumentationScope var6 = this.getInstrumentationScope(var5);
               if (var6 != null) {
                  byte[] var7 = var6.instrumentClass(var3, var1, var2);
                  if (var7 != null) {
                     var2 = var7;
                  }
               }
            }

            return var2;
         }
      }
   }

   public byte[] preProcess(GenericClassLoader var1, String var2, byte[] var3) {
      if (!this.isEligibleClass(var2)) {
         return var3;
      } else {
         InstrumentationScope var4 = this.getInstrumentationScope(var1);
         if (var4 != null) {
            byte[] var5 = var4.instrumentClass(var1, var2, var3);
            if (var5 != null) {
               var3 = var5;
            }
         }

         return var3;
      }
   }

   private boolean isEligibleClass(String var1) {
      if (!this.manager.isEnabled()) {
         return false;
      } else if (var1.startsWith("$Proxy")) {
         return false;
      } else if (var1.indexOf("_WLDF$INST_AUX_CLASS_") >= 0) {
         return false;
      } else {
         Iterator var2 = DiagnosticClassPreProcessor.ExcludedPackages.EXCLUDES.keySet().iterator();

         boolean var4;
         do {
            String var3;
            do {
               if (!var2.hasNext()) {
                  InstrumentationEngineConfiguration var8 = InstrumentationEngineConfiguration.getInstrumentationEngineConfiguration();
                  return var8.isEligibleClass(var1);
               }

               var3 = (String)var2.next();
            } while(!var1.startsWith(var3));

            var4 = false;
            Set var5 = (Set)DiagnosticClassPreProcessor.ExcludedPackages.EXCLUDES.get(var3);
            Iterator var6 = var5.iterator();

            while(var6.hasNext()) {
               String var7 = (String)var6.next();
               if (var1.startsWith(var7)) {
                  var4 = true;
               }
            }
         } while(var4);

         return false;
      }
   }

   private InstrumentationScope getInstrumentationScope(GenericClassLoader var1) {
      InstrumentationScopeBundle var2 = null;
      synchronized(this.classLoaderMap) {
         var2 = (InstrumentationScopeBundle)this.classLoaderMap.get(var1);
      }

      if (var2 == null) {
         InstrumentationScopeBundle var3 = this.createBundle(var1);
         synchronized(this.classLoaderMap) {
            var2 = (InstrumentationScopeBundle)this.classLoaderMap.get(var1);
            if (var2 == null) {
               var2 = var3;
               this.classLoaderMap.put(var1, var3);
            }
         }
      }

      return var2 == null ? null : var2.scope;
   }

   private InstrumentationScopeBundle createBundle(GenericClassLoader var1) {
      String var2 = InstrumentationSupportImpl.getInstrumentationScopeName(var1);
      if (var2 == null) {
         return VOID_BUNDLE;
      } else {
         InstrumentationScope var3 = this.manager.findInstrumentationScope(var2);
         if (var3 == null) {
            return null;
         } else {
            this.manager.associateClassloaderWithScope(var1, var3);
            InstrumentationScopeBundle var4 = new InstrumentationScopeBundle();
            var4.scope = var3;
            return var4;
         }
      }
   }

   private static final class ExcludedPackages {
      static final Map<String, Set> EXCLUDES = new HashMap<String, Set>() {
         {
            this.put("java.", new HashSet());
            this.put("javax.", new HashSet<String>() {
               {
                  this.add("javax.faces.");
               }
            });
            this.put("schema.", new HashSet());
            this.put("com.bea.xbean.", new HashSet());
            this.put("com.bea.xml.", new HashSet());
            this.put("weblogic.xml.", new HashSet());
         }
      };
   }

   private static class InstrumentationScopeBundle {
      InstrumentationScope scope;

      private InstrumentationScopeBundle() {
      }

      // $FF: synthetic method
      InstrumentationScopeBundle(Object var1) {
         this();
      }
   }
}
