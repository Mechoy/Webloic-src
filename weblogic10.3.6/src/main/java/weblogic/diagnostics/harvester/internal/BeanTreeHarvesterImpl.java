package weblogic.diagnostics.harvester.internal;

import com.bea.adaptive.harvester.WatchedValues;
import com.bea.adaptive.harvester.jmx.BaseHarvesterImpl;
import com.bea.adaptive.harvester.jmx.MetricInfoManager;
import java.io.IOException;
import java.lang.reflect.Method;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import weblogic.diagnostics.harvester.HarvesterException;
import weblogic.diagnostics.harvester.I18NConstants;
import weblogic.management.mbeanservers.Service;
import weblogic.management.runtime.RuntimeMBean;

public class BeanTreeHarvesterImpl extends BaseHarvesterImpl {
   public static BeanTreeHarvesterImpl getInstance() {
      return BeanTreeHarvesterImpl.BeanTreeHarvesterImplFactory.getInstance();
   }

   private BeanTreeHarvesterImpl(String var1) throws Exception {
      this.setUpDebugFlags("LocJMXHarvester");
      this.setName(var1);
      this.regMan = new BeanTreeRegistrationManager(this);
      this.metricMan = new MetricInfoManager(this);
      this.useCount = 1;
      this.setNamespace("ServerRuntime");
      harvestersByName.put(var1, this);
   }

   public void deallocate() {
   }

   public String findTypeName(String var1) throws Exception {
      if (var1 == null) {
         throw new HarvesterException.NullName(I18NConstants.TYPE_I18N);
      } else {
         return TreeBeanHarvestableDataProviderHelper.getTypeNameForInstance(var1);
      }
   }

   protected Object getAttribute(Object var1, String var2) throws Exception {
      String var3;
      if (var1 instanceof ObjectName) {
         var3 = ((ObjectName)var1).getCanonicalName();
         var1 = TreeBeanHarvestableDataProviderHelper.getInstanceForObjectIdentifier(var3);
      } else {
         var3 = TreeBeanHarvestableDataProviderHelper.getObjectNameForBean(var1);
      }

      String var4 = TreeBeanHarvestableDataProviderHelper.getTypeNameForInstance(var3);
      Method var5 = ((BeanTreeRegistrationManager)this.regMan).getReadMethod(var4, var2);
      if (var5 == null) {
         return null;
      } else {
         Object var6 = var5.invoke(var1);
         if (var6 != null) {
            if (!(var6 instanceof RuntimeMBean) && !(var6 instanceof Service)) {
               if (var6.getClass().isArray()) {
                  Class var10 = var6.getClass().getComponentType();
                  if (RuntimeMBean.class.isAssignableFrom(var10) || Service.class.isAssignableFrom(var10)) {
                     var6 = this.normalizeArrayResult((Object[])((Object[])var6));
                  }
               }
            } else {
               String var7 = TreeBeanHarvestableDataProviderHelper.getObjectNameForBean(var1);

               try {
                  var6 = new ObjectName(var7);
               } catch (MalformedObjectNameException var9) {
                  return var7;
               }
            }
         }

         return var6;
      }
   }

   private Object normalizeArrayResult(Object[] var1) {
      ObjectName[] var2 = new ObjectName[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         Object var4 = var1[var3];
         if (var4 != null && (var4 instanceof RuntimeMBean || var4 instanceof Service)) {
            String var5 = TreeBeanHarvestableDataProviderHelper.getObjectNameForBean(var4);

            try {
               var2[var3] = new ObjectName(var5);
            } catch (MalformedObjectNameException var7) {
               var2[var3] = null;
            }
         }
      }

      return var2;
   }

   protected boolean instanceNameIsValid(String var1) {
      return true;
   }

   protected void validateInstanceName(String var1) throws IllegalArgumentException {
   }

   protected int resolveByType(WatchedValues.Values var1) {
      return var1.getTypeName() == null ? -1 : super.resolveByType(var1);
   }

   public int isTypeHandled(String var1) {
      if (var1 == null) {
         throw new HarvesterException.NullName(I18NConstants.TYPE_I18N);
      } else {
         byte var2 = -1;

         String[][] var3;
         try {
            var3 = this.regMan.getKnownHarvestableTypes(var1);
         } catch (IOException var10) {
            throw new RuntimeException(var10);
         }

         if (var3 != null && var3.length > 0) {
            String[][] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               String[] var7 = var4[var6];
               String var8 = var7[0];
               if (var8 != null) {
                  Class var9 = getTypeClass(var8);
                  if (var9 != null) {
                     var2 = 2;
                     break;
                  }
               }
            }
         }

         return var2;
      }
   }

   private static Class getTypeClass(String var0) {
      Class var1;
      try {
         var1 = Class.forName(var0);
      } catch (ClassNotFoundException var3) {
         return null;
      }

      return !RuntimeMBean.class.isAssignableFrom(var1) && !Service.class.isAssignableFrom(var1) ? null : var1;
   }

   protected Object getInstance(String var1) {
      return TreeBeanHarvestableDataProviderHelper.getInstanceForObjectIdentifier(var1);
   }

   // $FF: synthetic method
   BeanTreeHarvesterImpl(String var1, Object var2) throws Exception {
      this(var1);
   }

   private static class BeanTreeHarvesterImplFactory {
      private static BeanTreeHarvesterImpl SINGLETON;

      private static BeanTreeHarvesterImpl createBeanTreeHarvesterImpl() {
         try {
            return new BeanTreeHarvesterImpl("WLSBeanTreeHarvester");
         } catch (Exception var1) {
            throw new RuntimeException(var1);
         }
      }

      static synchronized BeanTreeHarvesterImpl getInstance() {
         if (SINGLETON == null) {
            SINGLETON = createBeanTreeHarvesterImpl();
         }

         return SINGLETON;
      }
   }
}
