package weblogic.application.config;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import weblogic.application.ModuleException;
import weblogic.descriptor.DescriptorManager;
import weblogic.utils.Debug;
import weblogic.utils.classloaders.GenericClassLoader;

public final class ConfigDescriptorManagerCache {
   private final boolean debug = Debug.getCategory("weblogic.application.config.ConfigDescriptorManagerCache").isEnabled();
   private volatile Map _cache = new WeakHashMap();
   public static final ConfigDescriptorManagerCache SINGLETON = new ConfigDescriptorManagerCache();

   private ConfigDescriptorManagerCache() {
   }

   public DescriptorManager getEntry(GenericClassLoader var1) throws ModuleException {
      synchronized(this._cache) {
         WeakReference var3 = (WeakReference)this._cache.get(var1);
         DescriptorManager var4 = null;
         if (var3 != null) {
            var4 = (DescriptorManager)var3.get();
         }

         if (var4 == null) {
            var4 = new DescriptorManager(var1);
            this._cache.put(var1, new WeakReference(var4));
            if (this.debug) {
               Debug.say("Creating entry using key " + var1 + ". New size: " + this._cache.size());
            }
         } else if (this.debug) {
            Debug.say("Got existing entry using key " + var1 + ". Current size: " + this._cache.size());
         }

         return var4;
      }
   }

   public void removeEntry(GenericClassLoader var1) {
      synchronized(this._cache) {
         WeakReference var3 = (WeakReference)this._cache.remove(var1);
         if (this.debug) {
            DescriptorManager var4 = null;
            if (var3 != null) {
               var4 = (DescriptorManager)var3.get();
            }

            if (var4 != null) {
               Debug.say("Removing entry for key " + var1 + ". New size: " + this._cache.size());
            } else {
               Debug.say("Trying to remove non-existant entry for " + var1 + ". Current size: " + this._cache.size());
            }
         }

      }
   }
}
