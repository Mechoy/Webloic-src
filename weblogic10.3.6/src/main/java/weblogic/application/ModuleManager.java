package weblogic.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ModuleManager {
   private Module[] modules = new Module[0];
   private final Map idMap = new HashMap(0);
   private Map additionalModuleUris;

   public ModuleManager() {
      this.additionalModuleUris = Collections.EMPTY_MAP;
   }

   public void setAdditionalModuleUris(Map var1) {
      this.additionalModuleUris = var1;
   }

   public synchronized void setModules(Module[] var1) {
      this.idMap.clear();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.idMap.put(var1[var2].getId(), var1[var2]);
      }

      this.modules = var1;
   }

   public synchronized Module findModuleWithId(String var1) {
      return (Module)this.idMap.get(var1);
   }

   public synchronized List findModulesWithIds(String[] var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (this.idMap.containsKey(var1[var3])) {
            var2.add(this.idMap.get(var1[var3]));
         }
      }

      return var2;
   }

   public synchronized Module[] getModules() {
      return this.modules;
   }

   public synchronized boolean validateModuleIds(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (!this.isValidModuleId(var1[var2])) {
            return false;
         }
      }

      return true;
   }

   public synchronized String[] getValidModuleIds(String[] var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (this.isValidModuleId(var1[var3])) {
            var2.add(var1[var3]);
         }
      }

      return (String[])((String[])var2.toArray(new String[var2.size()]));
   }

   public synchronized String[] getInvalidModuleIds(String[] var1) {
      ArrayList var2 = new ArrayList();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if (!this.isValidModuleId(var1[var3])) {
            var2.add(var1[var3]);
         }
      }

      return (String[])((String[])var2.toArray(new String[var2.size()]));
   }

   public synchronized Map getModuleIdToUriMapping(String[] var1) {
      HashMap var2 = new HashMap(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Module var4 = this.findModuleWithId(var1[var3]);
         if (var4 == null) {
            var2.put(var1[var3], this.additionalModuleUris.get(var1[var3]));
         } else {
            if (var4 instanceof ModuleWrapper) {
               var4 = ((ModuleWrapper)var4).unwrap();
            }

            if (var4 instanceof ModuleLocationInfo) {
               var2.put(var1[var3], ((ModuleLocationInfo)var4).getModuleURI());
            } else {
               var2.put(var1[var3], var1[var3]);
            }
         }
      }

      return var2;
   }

   public boolean areNewUris(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (this.idMap.containsKey(var1[var2])) {
            return false;
         }
      }

      return true;
   }

   private boolean isValidModuleId(String var1) {
      return this.idMap.containsKey(var1) || this.additionalModuleUris.containsKey(var1);
   }

   public boolean isNewUri(String var1) {
      return !this.idMap.containsKey(var1);
   }
}
