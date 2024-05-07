package weblogic.cacheprovider.coherence.management;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import weblogic.management.ManagementException;
import weblogic.management.runtime.ApplicationRuntimeMBean;
import weblogic.management.runtime.EJBComponentRuntimeMBean;
import weblogic.management.runtime.WebAppComponentRuntimeMBean;

public class CoherenceJMXBridge {
   private Map<ClassLoader, RuntimeMBeanEntry> runtimeMBeanByLoader = new ConcurrentHashMap();
   private ObjectName sysScopedClusterObjectName;
   private MBeanServer sysScopedMBeanServer;
   private boolean isSystemScoped;

   public void registerApplicationRuntimeMBean(ClassLoader var1, ApplicationRuntimeMBean var2) throws ManagementException {
      this.runtimeMBeanByLoader.put(var1, new RuntimeMBeanEntry(var2));
      if (this.isSystemScoped) {
         var2.setCoherenceClusterRuntime(new CoherenceClusterRuntimeMBeanImpl(this.sysScopedClusterObjectName, this.sysScopedMBeanServer, var2));
      }

   }

   public void unRegisterApplicationRuntimeMBean(ClassLoader var1, ApplicationRuntimeMBean var2) throws ManagementException {
      this.runtimeMBeanByLoader.remove(var1);
   }

   public void registerWebAppComponentRuntimeMBean(ClassLoader var1, WebAppComponentRuntimeMBean[] var2) throws ManagementException {
      this.runtimeMBeanByLoader.put(var1, new RuntimeMBeanEntry(var2));
      if (this.isSystemScoped) {
         WebAppComponentRuntimeMBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            WebAppComponentRuntimeMBean var6 = var3[var5];
            var6.setCoherenceClusterRuntime(new CoherenceClusterRuntimeMBeanImpl(this.sysScopedClusterObjectName, this.sysScopedMBeanServer, var6));
         }
      }

   }

   public void unRegisterWebAppComponentRuntimeMBean(ClassLoader var1, WebAppComponentRuntimeMBean[] var2) throws ManagementException {
      this.runtimeMBeanByLoader.remove(var1);
   }

   public void registerEJBComponentRuntimeMBean(ClassLoader var1, EJBComponentRuntimeMBean var2) throws ManagementException {
      this.runtimeMBeanByLoader.put(var1, new RuntimeMBeanEntry(var2));
      if (this.isSystemScoped) {
         var2.setCoherenceClusterRuntime(new CoherenceClusterRuntimeMBeanImpl(this.sysScopedClusterObjectName, this.sysScopedMBeanServer, var2));
      }

   }

   public void unRegisterEJBComponentRuntimeMBean(ClassLoader var1, EJBComponentRuntimeMBean var2) throws ManagementException {
      this.runtimeMBeanByLoader.remove(var1);
   }

   public void clusterStarted(ClassLoader var1, ObjectName var2, MBeanServer var3) throws ManagementException {
      RuntimeMBeanEntry var4 = (RuntimeMBeanEntry)this.runtimeMBeanByLoader.get(var1);
      if (var4 != null) {
         var4.setCoherenceClusterRuntimeMBean(var2, var3);
      } else {
         this.sysScopedClusterObjectName = var2;
         this.sysScopedMBeanServer = var3;
         this.isSystemScoped = true;
      }

   }

   public void shutdown(ClassLoader var1) {
      Iterator var2 = this.runtimeMBeanByLoader.keySet().iterator();

      while(var2.hasNext()) {
         ClassLoader var3 = (ClassLoader)var2.next();
         if (this.isChildOfLoader(var1, var3)) {
            var2.remove();
         }
      }

   }

   private boolean isChildOfLoader(ClassLoader var1, ClassLoader var2) {
      if (var2 == var1) {
         return true;
      } else {
         for(ClassLoader var3 = var2.getParent(); var3 != null; var3 = var3.getParent()) {
            if (var3 == var1) {
               return true;
            }
         }

         return false;
      }
   }

   private class RuntimeMBeanEntry {
      ApplicationRuntimeMBean appRuntimeMBean;
      WebAppComponentRuntimeMBean[] webAppRuntimeMBeans;
      EJBComponentRuntimeMBean ejbRuntimeMBean;

      private RuntimeMBeanEntry(ApplicationRuntimeMBean var2) {
         this.appRuntimeMBean = var2;
      }

      private RuntimeMBeanEntry(WebAppComponentRuntimeMBean[] var2) {
         this.webAppRuntimeMBeans = var2;
      }

      private RuntimeMBeanEntry(EJBComponentRuntimeMBean var2) {
         this.ejbRuntimeMBean = var2;
      }

      private void setCoherenceClusterRuntimeMBean(ObjectName var1, MBeanServer var2) throws ManagementException {
         CoherenceClusterRuntimeMBeanImpl var3 = null;
         if (this.appRuntimeMBean != null) {
            var3 = new CoherenceClusterRuntimeMBeanImpl(var1, var2, this.appRuntimeMBean);
            this.appRuntimeMBean.setCoherenceClusterRuntime(var3);
         } else if (this.webAppRuntimeMBeans != null) {
            WebAppComponentRuntimeMBean[] var4 = this.webAppRuntimeMBeans;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               WebAppComponentRuntimeMBean var7 = var4[var6];
               var3 = new CoherenceClusterRuntimeMBeanImpl(var1, var2, var7);
               var7.setCoherenceClusterRuntime(var3);
            }
         } else if (this.ejbRuntimeMBean != null) {
            var3 = new CoherenceClusterRuntimeMBeanImpl(var1, var2, this.ejbRuntimeMBean);
            this.ejbRuntimeMBean.setCoherenceClusterRuntime(var3);
         }

      }

      // $FF: synthetic method
      RuntimeMBeanEntry(ApplicationRuntimeMBean var2, Object var3) {
         this((ApplicationRuntimeMBean)var2);
      }

      // $FF: synthetic method
      RuntimeMBeanEntry(WebAppComponentRuntimeMBean[] var2, Object var3) {
         this((WebAppComponentRuntimeMBean[])var2);
      }

      // $FF: synthetic method
      RuntimeMBeanEntry(EJBComponentRuntimeMBean var2, Object var3) {
         this((EJBComponentRuntimeMBean)var2);
      }
   }
}
