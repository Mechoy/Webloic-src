package weblogic.management.mbeanservers.compatibility.internal;

import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeMBeanException;
import javax.naming.Context;
import javax.naming.NamingException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.jndi.Environment;
import weblogic.management.MBeanCreationException;
import weblogic.management.MBeanHome;
import weblogic.management.ManagementException;
import weblogic.management.RemoteMBeanServer;
import weblogic.management.WebLogicMBean;
import weblogic.management.WebLogicObjectName;
import weblogic.management.configuration.ConfigurationMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.rmi.extensions.RemoteRuntimeException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public final class AdminMBeanHomeImpl implements MBeanHome {
   private static DebugLogger debug = DebugLogger.getDebugLogger("CompatabilityMBeanServer");
   private final MBeanHome mbeanHome;
   private final RuntimeAccess runtimeAccess;
   private final ObjectName domainObjectName;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private Map managedHomes = Collections.synchronizedMap(new HashMap(32));
   private Context localJNDIContext;
   private static final String[] CREATOR_SIGNATURE = new String[]{String.class.getName()};

   public AdminMBeanHomeImpl(MBeanHome var1) {
      this.runtimeAccess = ManagementService.getRuntimeAccess(kernelId);
      this.mbeanHome = var1;
      this.managedHomes.put(this.runtimeAccess.getServerName(), var1);

      try {
         this.domainObjectName = new ObjectName(this.runtimeAccess.getDomainName() + ":" + "Name=" + this.runtimeAccess.getDomainName() + ",Type=Domain");
      } catch (MalformedObjectNameException var4) {
         throw new Error(var4);
      }

      try {
         Environment var2 = this.getBaseEnvironment();
         this.localJNDIContext = var2.getInitialContext();
      } catch (NamingException var3) {
         throw new Error(var3);
      }
   }

   private Environment getBaseEnvironment() {
      Environment var1 = new Environment();
      var1.setReplicateBindings(false);
      var1.setCreateIntermediateContexts(true);
      return var1;
   }

   private static boolean isAdminMBean(String var0) {
      return !var0.endsWith("Config") && !var0.endsWith("Runtime");
   }

   public RemoteMBeanServer getMBeanServer() {
      return this.mbeanHome.getMBeanServer();
   }

   private MBeanHome getMBeanHomeForLocation(String var1) {
      return var1 == null ? this.mbeanHome : (MBeanHome)this.managedHomes.get(var1);
   }

   public Object getProxy(ObjectName var1) throws InstanceNotFoundException {
      String var2 = var1.getKeyProperty("Location");
      MBeanHome var3 = this.getMBeanHomeForLocation(var2);
      if (var3 == null) {
         throw new InstanceNotFoundException(var1.toString());
      } else {
         try {
            return var3.getProxy(var1);
         } catch (RemoteRuntimeException var5) {
            this.managedHomes.remove(var3);
            throw new InstanceNotFoundException(var1.toString());
         }
      }
   }

   public WebLogicMBean getMBean(ObjectName var1) throws InstanceNotFoundException {
      Object var2 = this.getProxy(var1);
      if (var2 instanceof WebLogicMBean) {
         return (WebLogicMBean)var2;
      } else {
         throw new InstanceNotFoundException("getMBean can be used only for weblogic mbeans");
      }
   }

   public WebLogicMBean getMBean(String var1, String var2, String var3) throws InstanceNotFoundException {
      if (this.managedHomes.size() != 1 && !isAdminMBean(var2)) {
         try {
            return this.mbeanHome.getMBean(var1, var2, var3);
         } catch (InstanceNotFoundException var12) {
            synchronized(this.managedHomes) {
               Iterator var5 = this.managedHomes.values().iterator();

               while(var5.hasNext()) {
                  MBeanHome var6 = null;

                  try {
                     var6 = (MBeanHome)var5.next();
                     if (!this.mbeanHome.equals(var6)) {
                        WebLogicMBean var10000 = var6.getMBean(var1, var2, var3);
                        return var10000;
                     }
                  } catch (InstanceNotFoundException var9) {
                     if (debug.isDebugEnabled()) {
                        debug.debug("Error getting MBean ", var9);
                     }
                  } catch (RemoteRuntimeException var10) {
                     var5.remove();
                     if (debug.isDebugEnabled()) {
                        debug.debug("Error getting MBean ", var10);
                     }
                  }
               }

               throw new InstanceNotFoundException(var1);
            }
         }
      } else {
         return this.mbeanHome.getMBean(var1, var2, var3);
      }
   }

   public WebLogicMBean getMBean(String var1, String var2) throws InstanceNotFoundException {
      if (this.managedHomes.size() != 1 && !isAdminMBean(var2)) {
         synchronized(this.managedHomes) {
            Iterator var4 = this.managedHomes.values().iterator();

            while(var4.hasNext()) {
               MBeanHome var5 = null;

               try {
                  var5 = (MBeanHome)var4.next();
                  WebLogicMBean var10000 = var5.getMBean(var1, var2);
                  return var10000;
               } catch (InstanceNotFoundException var8) {
                  if (debug.isDebugEnabled()) {
                     debug.debug("Error getting MBean ", var8);
                  }
               } catch (RemoteRuntimeException var9) {
                  var4.remove();
                  if (debug.isDebugEnabled()) {
                     debug.debug("Error getting MBean ", var9);
                  }
               }
            }

            throw new InstanceNotFoundException(var1);
         }
      } else {
         return this.mbeanHome.getMBean(var1, var2);
      }
   }

   public WebLogicMBean getMBean(String var1, String var2, String var3, String var4) throws InstanceNotFoundException {
      if (this.managedHomes.containsKey(var4)) {
         return ((MBeanHome)this.managedHomes.get(var4)).getMBean(var1, var2, var3, var4);
      } else {
         String var5 = "ObjectName:name=" + var1 + ",type=" + var2 + ",domain=" + var3 + ", location: " + var4;
         throw new InstanceNotFoundException(var5);
      }
   }

   public WebLogicMBean getMBean(String var1, Class var2) throws InstanceNotFoundException {
      synchronized(this.managedHomes) {
         Iterator var4 = this.managedHomes.values().iterator();

         while(var4.hasNext()) {
            MBeanHome var5 = null;

            WebLogicMBean var10000;
            try {
               var5 = (MBeanHome)var4.next();
               var10000 = var5.getMBean(var1, var2);
            } catch (InstanceNotFoundException var8) {
               if (debug.isDebugEnabled()) {
                  debug.debug("Error getting MBean ", var8);
               }
               continue;
            } catch (RemoteRuntimeException var9) {
               var4.remove();
               if (debug.isDebugEnabled()) {
                  debug.debug("Error getting MBean ", var9);
               }
               continue;
            }

            return var10000;
         }
      }

      throw new InstanceNotFoundException(var1);
   }

   public Set getMBeansByType(String var1) {
      if (this.managedHomes.size() != 1 && !isAdminMBean(var1)) {
         HashSet var2 = new HashSet();
         synchronized(this.managedHomes) {
            Iterator var4 = this.managedHomes.values().iterator();

            while(var4.hasNext()) {
               MBeanHome var5 = null;

               try {
                  var5 = (MBeanHome)var4.next();
                  var2.addAll(var5.getMBeansByType(var1));
               } catch (RemoteRuntimeException var8) {
                  var4.remove();
                  if (debug.isDebugEnabled()) {
                     debug.debug("Error getting MBeans by type ", var8);
                  }
               }
            }

            return var2;
         }
      } else {
         return this.mbeanHome.getMBeansByType(var1);
      }
   }

   public Set getAllMBeans(String var1) {
      HashSet var2 = new HashSet();
      synchronized(this.managedHomes) {
         Iterator var4 = this.managedHomes.values().iterator();

         while(var4.hasNext()) {
            MBeanHome var5 = null;

            try {
               var5 = (MBeanHome)var4.next();
               var2.addAll(var5.getAllMBeans(var1));
            } catch (RemoteRuntimeException var8) {
               var4.remove();
               if (debug.isDebugEnabled()) {
                  debug.debug("Error getting all MBeans ", var8);
               }
            }
         }

         return var2;
      }
   }

   public Set getAllMBeans() {
      HashSet var1 = new HashSet();
      synchronized(this.managedHomes) {
         Iterator var3 = this.managedHomes.values().iterator();

         while(var3.hasNext()) {
            MBeanHome var4 = null;

            try {
               var4 = (MBeanHome)var3.next();
               var1.addAll(var4.getAllMBeans());
            } catch (RemoteRuntimeException var7) {
               var3.remove();
               if (debug.isDebugEnabled()) {
                  debug.debug("Error getting all MBeans ", var7);
               }
            }
         }

         return var1;
      }
   }

   public ConfigurationMBean getAdminMBean(String var1, String var2) throws InstanceNotFoundException {
      return this.mbeanHome.getAdminMBean(var1, var2);
   }

   public ConfigurationMBean getAdminMBean(String var1, String var2, String var3) throws InstanceNotFoundException {
      return this.mbeanHome.getAdminMBean(var1, var2, var3);
   }

   public ConfigurationMBean getConfigurationMBean(String var1, String var2) throws InstanceNotFoundException {
      return this.mbeanHome.getConfigurationMBean(var1, var2);
   }

   public RuntimeMBean getRuntimeMBean(String var1, String var2) throws InstanceNotFoundException {
      return this.mbeanHome.getRuntimeMBean(var1, var2);
   }

   public DomainMBean getActiveDomain() {
      try {
         return (DomainMBean)this.mbeanHome.getMBean(this.getDomainName(), "Domain");
      } catch (InstanceNotFoundException var2) {
         throw new AssertionError(var2);
      }
   }

   public String getDomainName() {
      return ManagementService.getRuntimeAccess(kernelId).getDomainName();
   }

   public void addManagedHome(MBeanHome var1, String var2, String var3) {
      try {
         Environment var4 = this.getBaseEnvironment();
         var4.setProviderUrl(var3);
         Context var5 = var4.getInitialContext();
         var5.rebind("weblogic.management.adminhome", this);
      } catch (NamingException var6) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to bind in to the remote mbean home for " + var2, var6);
         }

         return;
      }

      try {
         this.localJNDIContext.bind("weblogic.management.home." + var2, var1);
      } catch (NamingException var7) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to connect to bind managed MBeanHome", var7);
         }
      }

      this.managedHomes.put(var2, var1);
      if (debug.isDebugEnabled()) {
         debug.debug("Added managed home,  server name " + var2 + ", server URL " + var3);
      }

   }

   void removeManagedHome(String var1) {
      try {
         this.localJNDIContext.unbind("weblogic.management.home." + var1);
      } catch (NamingException var3) {
         if (debug.isDebugEnabled()) {
            debug.debug("Unable to connect to " + var1, var3);
         }
      }

      this.managedHomes.remove(var1);
      if (debug.isDebugEnabled()) {
         debug.debug("Removed managed home,  server name " + var1);
      }

   }

   public WebLogicMBean createAdminMBean(String var1, String var2, String var3, ConfigurationMBean var4) throws MBeanCreationException {
      Object var5;
      if (var4 != null) {
         var5 = var4.getObjectName();
      } else {
         var5 = this.domainObjectName;
      }

      String var6 = "create" + var2;
      String[] var7 = new String[]{var1};
      ObjectName var8 = null;

      try {
         var8 = (ObjectName)this.getMBeanServer().invoke((ObjectName)var5, var6, var7, CREATOR_SIGNATURE);
      } catch (InstanceNotFoundException var11) {
         throw new MBeanCreationException("Unable to create new bean of type " + var2 + " for " + var5 + ":parent not found", var11);
      } catch (MBeanException var12) {
         throw new MBeanCreationException("Unable to create new bean of type " + var2 + " for " + var5 + ":unexpected exception", var12);
      } catch (RuntimeMBeanException var13) {
         throw new MBeanCreationException("Unable to create new bean of type " + var2 + " for " + var5 + ":unexpected exception", var13);
      } catch (ReflectionException var14) {
         if (var2 != null && var2.endsWith("Runtime")) {
            throw new MBeanCreationException("Explicit create of a RuntimeMBean is not allowed");
         }

         throw new MBeanCreationException("Unable to create new bean of type " + var2 + " for " + var5 + ":missing create method", var14);
      }

      try {
         return this.getMBean(var8);
      } catch (InstanceNotFoundException var10) {
         throw new MBeanCreationException("Unable to acquire proxy for " + var8);
      }
   }

   public WebLogicMBean createAdminMBean(String var1, String var2, String var3) throws MBeanCreationException {
      return this.createAdminMBean(var1, var2, var3, (ConfigurationMBean)null);
   }

   public WebLogicMBean createAdminMBean(String var1, String var2) throws MBeanCreationException {
      return this.createAdminMBean(var1, var2, (String)null, (ConfigurationMBean)null);
   }

   public ConfigurationMBean findOrCreateAdminMBean(String var1, String var2) throws MBeanCreationException {
      try {
         return this.getAdminMBean(var1, var2);
      } catch (InstanceNotFoundException var4) {
         return (ConfigurationMBean)this.createAdminMBean(var1, var2, (String)null, (ConfigurationMBean)null);
      }
   }

   public ConfigurationMBean findOrCreateAdminMBean(String var1, String var2, String var3) throws MBeanCreationException {
      try {
         return this.getAdminMBean(var1, var2, var3);
      } catch (InstanceNotFoundException var5) {
         return (ConfigurationMBean)this.createAdminMBean(var1, var2, var3, (ConfigurationMBean)null);
      }
   }

   public ConfigurationMBean findOrCreateAdminMBean(String var1, String var2, String var3, ConfigurationMBean var4) throws MBeanCreationException {
      try {
         return this.getAdminMBean(var1, var2, var3);
      } catch (InstanceNotFoundException var6) {
         return (ConfigurationMBean)this.createAdminMBean(var1, var2, var3, var4);
      }
   }

   public void deleteMBean(ObjectName var1) throws InstanceNotFoundException, MBeanRegistrationException {
      ObjectName var2 = null;
      if (var1 instanceof WebLogicObjectName) {
         var2 = this.domainObjectName;
      } else {
         var2 = this.domainObjectName;
      }

      this.deleteMBean(var1, var2);
   }

   private void deleteMBean(ObjectName var1, ObjectName var2) throws InstanceNotFoundException, MBeanRegistrationException {
      String var3 = var1.getKeyProperty("Type");
      String var4 = "destroy" + var3;
      Object[] var5 = new Object[]{var1};
      String[] var6 = new String[]{ObjectName.class.getName()};

      try {
         this.getMBeanServer().invoke(var2, var4, var5, var6);
      } catch (ReflectionException var11) {
         Throwable var8 = var11.getCause();
         if (var3 != null && var3.endsWith("Runtime") && var8 != null && var8 instanceof NoSuchMethodException) {
            String var13 = "Explicit delete of a RuntimeMBean of type : " + var3 + "  is not allowed";
            ManagementException var10 = new ManagementException(var13);
            throw new MBeanRegistrationException(var10);
         } else {
            InstanceNotFoundException var9 = new InstanceNotFoundException("Unable to delete an MBean " + var11);
            var9.initCause(var11);
            throw var9;
         }
      } catch (MBeanException var12) {
         throw new MBeanRegistrationException(var12);
      }
   }

   public void deleteMBean(WebLogicMBean var1) throws InstanceNotFoundException, MBeanRegistrationException {
      WebLogicMBean var2 = var1.getParent();
      if (var2 == null) {
         this.deleteMBean((ObjectName)var1.getObjectName());
      } else {
         this.deleteMBean(var1.getObjectName(), var2.getObjectName());
      }

   }
}
