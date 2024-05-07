package weblogic.j2ee;

import com.bea.xbean.common.SystemCache;
import com.bea.xml.SchemaTypeLoader;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;

public class ApplicationCache extends SystemCache {
   private static ApplicationCache singleton = new ApplicationCache();
   private WeakHashMap cl2SchemaTypeLoader;

   private ApplicationCache() {
      SystemCache.set(this);
      this.cl2SchemaTypeLoader = new WeakHashMap();
   }

   public static ApplicationCache getApplicationCache() {
      return singleton;
   }

   public SchemaTypeLoader getFromTypeLoaderCache(ClassLoader var1) {
      ApplicationContextInternal var2 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
      if (var2 != null) {
         return (SchemaTypeLoader)var2.getSchemaTypeLoader(var1);
      } else {
         WeakReference var3 = (WeakReference)this.cl2SchemaTypeLoader.get(var1);
         return var3 != null && var3.get() != null ? (SchemaTypeLoader)var3.get() : null;
      }
   }

   public void addToTypeLoaderCache(SchemaTypeLoader var1, ClassLoader var2) {
      ApplicationContextInternal var3 = ApplicationAccess.getApplicationAccess().getCurrentApplicationContext();
      if (var3 != null) {
         var3.setSchemaTypeLoader(var2, var1);
      } else {
         this.cl2SchemaTypeLoader.put(var2, new WeakReference(var1));
      }

   }
}
