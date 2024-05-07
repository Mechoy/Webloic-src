package weblogic.management.mbeanservers.compatibility.internal;

import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.management.Descriptor;
import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.QueryExp;
import javax.management.modelmbean.ModelMBeanInfo;
import weblogic.management.MBeanCreationException;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementLogger;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.beaninfo.BeanInfoAccess;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class MBeanHomeImpl implements MBeanHome {
   Map proxyCache;
   RemoteMBeanServer mbeanServer;
   BeanInfoAccess beanInfoAccess;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public MBeanHomeImpl(RemoteMBeanServer var1) {
      this.mbeanServer = var1;
      this.proxyCache = Collections.synchronizedMap(new HashMap(256));
      this.beanInfoAccess = ManagementService.getBeanInfoAccess();
   }

   public RemoteMBeanServer getMBeanServer() {
      return this.mbeanServer;
   }

   public DomainMBean getActiveDomain() {
      try {
         return (DomainMBean)this.getMBean(this.getDomainName(), "DomainConfig");
      } catch (InstanceNotFoundException var2) {
         throw new Error(var2);
      }
   }

   public Object getProxy(ObjectName var1) throws InstanceNotFoundException {
      return this.getProxy(var1, false);
   }

   private Object getProxy(ObjectName var1, boolean var2) throws InstanceNotFoundException {
      if (!this.mbeanServer.isRegistered(var1)) {
         throw new InstanceNotFoundException(var1.toString());
      } else {
         Object var3 = this.proxyCache.get(var1);
         if (var3 != null) {
            return var3;
         } else {
            Class[] var4 = new Class[]{this.getInterface(var1, var2)};
            Object var5 = Proxy.newProxyInstance(var4[0].getClassLoader(), var4, new MBeanProxy(var1, this));
            this.proxyCache.put(var1, var5);
            return var5;
         }
      }
   }

   public WebLogicMBean getMBean(ObjectName var1) throws InstanceNotFoundException {
      Object var2 = this.getProxy(var1, true);
      if (!(var2 instanceof WebLogicMBean)) {
         String var3 = "";

         try {
            if (var2 != null) {
               Class var4 = var2.getClass();
               var3 = "mbean class = " + var4;
               var3 = var3 + " superclass = " + var4.getSuperclass();
               Class[] var5 = var2.getClass().getInterfaces();

               for(int var6 = 0; var5 != null && var6 < var5.length; ++var6) {
                  var3 = var3 + " intf[" + var6 + "] = " + var5[var6];
               }
            }
         } catch (Exception var7) {
         }

         throw new AssertionError("The MBean represented by " + var1 + " is not a WebLogicMBean. The MBean info is " + var3);
      } else {
         return (WebLogicMBean)var2;
      }
   }

   private Class getInterface(ObjectName var1, boolean var2) {
      MBeanInfo var3;
      try {
         var3 = this.mbeanServer.getMBeanInfo(var1);
      } catch (Exception var12) {
         throw new AssertionError(var12);
      }

      String var4 = var3.getClassName();
      if (!ObjectName.class.getName().equals(var4)) {
         try {
            Class var5 = this.classForName(var4);
            if (var5.isInterface()) {
               return var5;
            }
         } catch (ClassNotFoundException var11) {
         }
      }

      if (var3 instanceof ModelMBeanInfo) {
         String var14 = null;
         Descriptor var6 = null;
         ModelMBeanInfo var7 = null;

         try {
            var7 = (ModelMBeanInfo)var3;
            var6 = var7.getMBeanDescriptor();
            Object var8 = var6.getFieldValue("interfaceclassname");
            if (var8 == null) {
               var8 = var6.getFieldValue("com.bea.interfaceclassname");
            }

            if (var8 != null) {
               var14 = var8.toString();
            }

            return this.classForName(var14);
         } catch (Exception var13) {
            if (var2) {
               String var9 = "Error occurred while getting interfaceClassName. Model info = " + var7 + " desc = " + var6 + " InterfaceClassName = " + var14;
               RuntimeException var10 = new RuntimeException(var9);
               ManagementLogger.logExceptionInMBeanProxy(var10);
               ManagementLogger.logExceptionGettingClassForInterface(var14, var13);
            }
         }
      }

      return DynamicMBean.class;
   }

   private Class classForName(String var1) throws ClassNotFoundException {
      try {
         return Class.forName(var1);
      } catch (ClassNotFoundException var3) {
         return this.beanInfoAccess.getClassForName(var1);
      }
   }

   private WebLogicMBean queryMBean(String var1) throws InstanceNotFoundException {
      try {
         ObjectName var2 = new ObjectName(var1);
         Set var3 = this.mbeanServer.queryNames(var2, (QueryExp)null);
         if (var3.size() > 1) {
            throw new IllegalArgumentException("Query had multiple results " + var1);
         } else {
            Iterator var4 = var3.iterator();
            if (!var4.hasNext()) {
               throw new InstanceNotFoundException(var1);
            } else {
               ObjectName var5 = (ObjectName)var4.next();
               return this.getMBean(var5);
            }
         }
      } catch (MalformedObjectNameException var6) {
         throw new IllegalArgumentException(var1);
      }
   }

   public WebLogicMBean getMBean(String var1, String var2) throws InstanceNotFoundException {
      return this.getMBean(var1, var2, this.getDomainName());
   }

   public WebLogicMBean getMBean(String var1, String var2, String var3) throws InstanceNotFoundException {
      String var4 = var3 + ":Name=" + var1 + ",Type=" + var2 + ",*";
      return this.queryMBean(var4);
   }

   public WebLogicMBean getMBean(String var1, String var2, String var3, String var4) throws InstanceNotFoundException {
      String var5 = var3 + ":Name=" + var1 + ",Type=" + var2 + ",Location=" + var4 + ",*";
      return this.queryMBean(var5);
   }

   public Set getMBeansByType(String var1) {
      String var2 = "*:Type=" + var1 + ",*";
      HashSet var3 = new HashSet();

      try {
         ObjectName var4 = new ObjectName(var2);
         Set var5 = this.mbeanServer.queryNames(var4, (QueryExp)null);
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            var3.add(this.getMBean((ObjectName)var6.next()));
         }

         return var3;
      } catch (Exception var7) {
         throw new Error(var7);
      }
   }

   public WebLogicMBean getMBean(String var1, Class var2) throws InstanceNotFoundException {
      if (var1 == null) {
         throw new NullPointerException("null name");
      } else {
         Set var3 = this.getAllMBeans(this.getDomainName());
         Iterator var4 = var3.iterator();

         WebLogicMBean var5;
         do {
            if (!var4.hasNext()) {
               throw new InstanceNotFoundException(var1 + ", class: " + var2.getName() + " (" + ManagementService.getRuntimeAccess(kernelId).isAdminServer() + ")");
            }

            var5 = (WebLogicMBean)var4.next();
         } while(!var1.equals(var5.getName()) || !var2.isInstance(var5));

         return var5;
      }
   }

   public Set getAllMBeans(String var1) {
      Set var2;
      try {
         var2 = this.mbeanServer.queryNames(new ObjectName(var1 + ":*"), (QueryExp)null);
      } catch (MalformedObjectNameException var8) {
         throw new Error(var8);
      }

      HashSet var3 = new HashSet(var2.size());
      Iterator var4 = var2.iterator();

      while(var4.hasNext()) {
         ObjectName var5 = (ObjectName)var4.next();

         try {
            Object var6 = this.getProxy(var5);
            if (var6 instanceof WebLogicMBean) {
               var3.add(var6);
            }
         } catch (InstanceNotFoundException var7) {
         }
      }

      return var3;
   }

   public Set getAllMBeans() {
      return this.getAllMBeans("*");
   }

   public ConfigurationMBean getConfigurationMBean(String var1, String var2) throws InstanceNotFoundException {
      return (ConfigurationMBean)this.getMBean(var1, var2);
   }

   public RuntimeMBean getRuntimeMBean(String var1, String var2) throws InstanceNotFoundException {
      return (RuntimeMBean)this.getMBean(var1, var2);
   }

   public String toString() {
      return "MBeanHomeImpl for server " + this.getServerName();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (var1 instanceof MBeanHome) {
         MBeanHome var2 = (MBeanHome)var1;
         return this.mbeanServer.equals(var2.getMBeanServer());
      } else {
         return false;
      }
   }

   public ConfigurationMBean getAdminMBean(String var1, String var2) throws InstanceNotFoundException {
      try {
         String var3 = this.getDomainName() + ":" + "Name=" + var1 + "," + "Type=" + var2;
         ObjectName var4 = new ObjectName(var3);
         return (ConfigurationMBean)this.getMBean(var4);
      } catch (MalformedObjectNameException var5) {
         throw new Error(var5);
      }
   }

   public ConfigurationMBean getAdminMBean(String var1, String var2, String var3) throws InstanceNotFoundException {
      try {
         String var4 = var3 + ":" + "Name=" + var1 + "," + "Type=" + var2;
         ObjectName var5 = new ObjectName(var4);
         return (ConfigurationMBean)this.getMBean(var5);
      } catch (MalformedObjectNameException var6) {
         throw new Error(var6);
      }
   }

   public WebLogicMBean createAdminMBean(String var1, String var2) throws MBeanCreationException {
      throw new UnsupportedOperationException("Only supported on the Admin MBeanHome");
   }

   public WebLogicMBean createAdminMBean(String var1, String var2, String var3) throws MBeanCreationException {
      throw new UnsupportedOperationException("Only supported on the Admin MBeanHome");
   }

   public WebLogicMBean createAdminMBean(String var1, String var2, String var3, ConfigurationMBean var4) throws MBeanCreationException {
      throw new UnsupportedOperationException("Only supported on the Admin MBeanHome");
   }

   public ConfigurationMBean findOrCreateAdminMBean(String var1, String var2) throws MBeanCreationException {
      throw new UnsupportedOperationException("Only supported on the Admin MBeanHome");
   }

   public ConfigurationMBean findOrCreateAdminMBean(String var1, String var2, String var3) throws MBeanCreationException {
      throw new UnsupportedOperationException("Only supported on the Admin MBeanHome");
   }

   public ConfigurationMBean findOrCreateAdminMBean(String var1, String var2, String var3, ConfigurationMBean var4) throws MBeanCreationException {
      throw new UnsupportedOperationException("Only supported on the Admin MBeanHome");
   }

   public void addManagedHome(MBeanHome var1, String var2, String var3) {
      throw new Error("addManagedHome is supported only on AdminMbeanHome: " + var2 + "=" + var3);
   }

   public String getDomainName() {
      return ManagementService.getRuntimeAccess(kernelId).getDomainName();
   }

   public String getServerName() {
      return ManagementService.getRuntimeAccess(kernelId).getServerName();
   }

   public void deleteMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException {
      this.mbeanServer.unregisterMBean(var1);
   }

   public void deleteMBean(WebLogicMBean var1) throws InstanceNotFoundException, MBeanRegistrationException {
      this.deleteMBean((ObjectName)var1.getObjectName());
   }
}
