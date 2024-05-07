package weblogic.management.mbeanservers.domainruntime.internal;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.diagnostics.debug.DebugLogger;

public class ManagedMBeanServerObjectNameManager {
   private String location;
   private Map objectNamesByScoped = new MyLinkedHashMap();
   private Map scopedByObjectName = new MyLinkedHashMap();
   private Map objectNameExceptions = new HashMap();
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJMXDomain");

   public synchronized void registerObject(ObjectName var1, ObjectName var2) {
      this.scopedByObjectName.put(var1, var2);
      this.objectNamesByScoped.put(var2, var1);
      if (var1 != null && var1.getKeyProperty("Location") != null) {
         this.objectNameExceptions.put(var2, var1);
      }

   }

   public synchronized ObjectName unregisterObjectInstance(ObjectName var1) {
      this.objectNameExceptions.remove(var1);
      ObjectName var2 = (ObjectName)this.objectNamesByScoped.get(var1);
      if (var2 == null) {
         return null;
      } else {
         this.unregisterObject(var2);
         return var2;
      }
   }

   public synchronized void unregisterObject(ObjectName var1) {
      Object var2 = this.scopedByObjectName.remove(var1.getCanonicalName());
      ObjectName var3 = (ObjectName)this.objectNamesByScoped.remove(var2);
   }

   public ManagedMBeanServerObjectNameManager(String var1) {
      this.location = var1;
   }

   public synchronized ObjectName lookupObjectName(ObjectName var1) {
      ObjectName var2 = (ObjectName)this.objectNamesByScoped.get(var1);
      if (var2 == null) {
         var2 = (ObjectName)this.objectNameExceptions.get(var1);
      }

      if (var2 == null) {
         var2 = this.removeLocation(var1);
         this.registerObject(var2, var1);
      }

      return var2;
   }

   private ObjectName removeLocation(ObjectName var1) {
      if (var1 == null) {
         return null;
      } else if (var1.getKeyProperty("Location") == null) {
         return var1;
      } else {
         Hashtable var2 = var1.getKeyPropertyList();

         try {
            StringBuffer var3 = new StringBuffer();
            if (var1.isDomainPattern()) {
               var3.append("*:");
            } else {
               var3.append(var1.getDomain() + ":");
            }

            Iterator var4 = var2.entrySet().iterator();
            boolean var5 = true;

            while(var4.hasNext()) {
               Map.Entry var6 = (Map.Entry)var4.next();
               if (!var6.getKey().equals("Location")) {
                  if (!var5) {
                     var3.append(",");
                  }

                  var3.append(var6.getKey() + "=" + var6.getValue());
                  var5 = false;
               }
            }

            if (var1.isPropertyPattern()) {
               if (!var5) {
                  var3.append(",");
               }

               var3.append("*");
            }

            return new ObjectName(var3.toString());
         } catch (MalformedObjectNameException var7) {
            throw new AssertionError(var7);
         }
      }
   }

   public synchronized ObjectName lookupScopedObjectName(ObjectName var1) {
      if (var1 == null) {
         return null;
      } else {
         ObjectName var2 = (ObjectName)this.scopedByObjectName.get(var1);
         if (var2 == null) {
            if (var1.getKeyProperty("Location") != null && this.objectNamesByScoped.get(var1) != null) {
               if (debug.isDebugEnabled()) {
                  debug.debug("ManagedMBeanServerObjectNameManager: lookupScopedObjectName existing Location entry found for " + var1);
               }

               return var1;
            }

            var2 = this.addLocation(var1);
            this.registerObject(var1, var2);
         }

         return var2;
      }
   }

   private ObjectName addLocation(ObjectName var1) {
      Hashtable var2 = var1.getKeyPropertyList();
      var2.put("Location", this.location);

      try {
         return new ObjectName(var1.getDomain(), var2);
      } catch (MalformedObjectNameException var4) {
         throw new AssertionError(var4);
      }
   }

   public static class MyLinkedHashMap extends LinkedHashMap {
      private static final long serialVersionUID = 1L;
      private static final int MAX_ENTRIES = Integer.parseInt(System.getProperty("weblogic.management.domainruntime.ObjectNameCacheSize", "100"));

      protected boolean removeEldestEntry(Map.Entry var1) {
         return this.size() > MAX_ENTRIES;
      }
   }
}
