package weblogic.management.j2ee.internal;

import java.io.IOException;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerFactory;
import javax.management.MBeanServerNotification;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.OperationsException;
import javax.management.QueryExp;
import javax.management.ReflectionException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementLogger;
import weblogic.management.j2ee.ListenerRegistry;
import weblogic.management.jmx.ObjectNameManager;
import weblogic.management.jmx.ObjectSecurityManager;
import weblogic.management.jmx.modelmbean.WLSModelMBean;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.provider.ManagementService;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

public class JMOService implements ListenerRegistry {
   private final Map wlsToJ2EEObjectNames;
   private String defaultDomain;
   private MBeanServerConnection domainRuntimeMBeanServer;
   private static DebugLogger debug = DebugLogger.getDebugLogger("DebugJ2EEManagement");
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final ObjectName RUNTIME_QUERY;
   private final MBeanServer server;

   private JMOService() {
      this.wlsToJ2EEObjectNames = new HashMap(39);
      this.defaultDomain = null;
      this.defaultDomain = ManagementService.getRuntimeAccess(kernelId).getDomainName();
      this.server = MBeanServerFactory.createMBeanServer();
      MBeanServerConnectionProvider.initialize();
      this.domainRuntimeMBeanServer = MBeanServerConnectionProvider.getDomainMBeanServerConnection();
      this.registerDomainRuntimeListener();
      this.initializePreexistingBeans();
   }

   public static JMOService getJMOService() {
      return JMOService.SINGLETON.service;
   }

   public String getDefaultDomain() {
      return this.defaultDomain;
   }

   public Object getAttribute(ObjectName var1, String var2) throws InvalidObjectNameException, InstanceNotFoundException, ReflectionException, AttributeNotFoundException, MBeanException {
      this.validate(var1);
      return this.server.getAttribute(var1, var2);
   }

   public Set queryNames(ObjectName var1, QueryExp var2) {
      return this.server.queryNames(var1, var2);
   }

   public boolean isRegistered(ObjectName var1) {
      return this.server.isRegistered(var1);
   }

   public Integer getMBeanCount() {
      return this.server.getMBeanCount();
   }

   public MBeanInfo getMBeanInfo(ObjectName var1) throws IntrospectionException, InstanceNotFoundException, ReflectionException {
      return this.server.getMBeanInfo(var1);
   }

   public AttributeList getAttributes(ObjectName var1, String[] var2) throws InstanceNotFoundException, ReflectionException {
      return this.server.getAttributes(var1, var2);
   }

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, ReflectionException, MBeanException, InvalidObjectNameException {
      this.validate(var1);
      this.server.setAttribute(var1, var2);
   }

   public AttributeList setAttributes(ObjectName var1, AttributeList var2) throws InstanceNotFoundException, ReflectionException {
      return this.server.setAttributes(var1, var2);
   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, ReflectionException, MBeanException, InvalidObjectNameException {
      this.validate(var1);
      return this.server.invoke(var1, var2, var3, var4);
   }

   public void addListener(ObjectName var1, NotificationListener var2, NotificationFilter var3, Object var4) throws InstanceNotFoundException, RemoteException {
      this.server.addNotificationListener(var1, var2, var3, var4);
   }

   public void removeListener(ObjectName var1, NotificationListener var2) throws InstanceNotFoundException, ListenerNotFoundException, RemoteException {
      this.server.removeNotificationListener(var1, var2);
   }

   private void registerDomainRuntimeListener() {
      try {
         this.domainRuntimeMBeanServer.addNotificationListener(new ObjectName("JMImplementation:type=MBeanServerDelegate"), new MBeanServerListener(), new MBeanServerFilter(), (Object)null);
      } catch (MalformedObjectNameException var2) {
         throw new Error("Malformed ObjectName", var2);
      } catch (InstanceNotFoundException var3) {
         throw new Error("DomainRuntime MBeanServer not found", var3);
      } catch (IOException var4) {
         throw new Error("IOException while registering with DomainRuntime MBeanServer", var4);
      }
   }

   private void initializePreexistingBeans() {
      try {
         Set var1 = this.domainRuntimeMBeanServer.queryNames(RUNTIME_QUERY, (QueryExp)null);
         Iterator var2 = var1.iterator();

         while(var2.hasNext()) {
            try {
               ObjectName var3 = (ObjectName)var2.next();
               String var4 = var3.getKeyProperty("Type");
               if (var4 != null && Types.isValidWLSType(var4)) {
                  NotificationHandler var5 = new NotificationHandler(var3, this.defaultDomain);
                  if (!var5.registerThisObject()) {
                     if (debug.isDebugEnabled()) {
                        debug.debug("Skipping Bean !registerThisObject" + var3);
                     }
                  } else {
                     this.registerMBean(var5);
                  }
               } else if (debug.isDebugEnabled()) {
                  debug.debug("Skipping Bean: no type " + var3);
               }
            } catch (Throwable var6) {
               debug.debug("Unable to register a pre-existing mean" + var6);
               ManagementLogger.logCouldNotRegisterMBeanForJSR77(var6);
            }
         }
      } catch (IOException var7) {
         debug.debug("Failure to connect to domain runtime service", var7);
      }

   }

   MBeanServer getJMOMBeanServer() {
      return this.server;
   }

   private void validate(ObjectName var1) throws InvalidObjectNameException {
      JMOValidator var2 = new JMOValidator(var1);
      var2.validate();
   }

   private void registerMBean(NotificationHandler var1) {
      Map var2 = var1.getJ2EEObjectNameToImplMap();
      Set var3 = var2.keySet();
      Iterator var4 = var3.iterator();

      while(var4.hasNext()) {
         ObjectName var5 = (ObjectName)var4.next();

         try {
            Object var6 = var2.get(var5);
            WLSModelMBeanContext var7 = new WLSModelMBeanContext(this.server, (ObjectNameManager)null, (ObjectSecurityManager)null);
            WLSModelMBean var8 = new WLSModelMBean(var6, var7);
            if (debug.isDebugEnabled()) {
               debug.debug("Registering:    " + var5.toString());
            }

            this.server.registerMBean(var8, var5);
         } catch (NotCompliantMBeanException var9) {
            throw new AssertionError("Not a Compliant MBean" + var9);
         } catch (MBeanRegistrationException var10) {
            throw new AssertionError("MBean Registration failed" + var10);
         } catch (InstanceAlreadyExistsException var11) {
            if (debug.isDebugEnabled()) {
               debug.debug("Attempt to register an MBean again" + var11.toString());
            }
            continue;
         } catch (OperationsException var12) {
            throw new AssertionError("Failure in creating MBean Adaptor" + var12);
         }

         this.wlsToJ2EEObjectNames.put(var1.getWLSObjectName(), var3.toArray(new ObjectName[var3.size()]));
      }

   }

   private void unregisterMBean(ObjectName var1) {
      if (this.wlsToJ2EEObjectNames.containsKey(var1)) {
         ObjectName[] var2 = (ObjectName[])((ObjectName[])this.wlsToJ2EEObjectNames.remove(var1));

         for(int var3 = 0; var3 < var2.length; ++var3) {
            if (debug.isDebugEnabled()) {
               debug.debug("Un-Registering:    " + var2[var3]);
            }

            try {
               this.server.unregisterMBean(var2[var3]);
            } catch (MBeanRegistrationException var5) {
               throw new AssertionError("MBean Registration failed" + var5);
            } catch (OperationsException var6) {
               throw new AssertionError("Failure in creating MBean Adaptor" + var6);
            }
         }

      }
   }

   public String[] getQueriedNames(ObjectName var1) {
      Set var2 = this.server.queryNames(var1, (QueryExp)null);
      return toStringArray(var2);
   }

   public String[] getQueriedNames(QueryExp var1) {
      Set var2 = this.server.queryNames((ObjectName)null, var1);
      return toStringArray(var2);
   }

   public static String[] toStringArray(Set var0) {
      HashSet var1 = new HashSet();
      ObjectName var2 = null;
      Iterator var3 = var0.iterator();

      while(var3.hasNext()) {
         var2 = (ObjectName)var3.next();
         var1.add(var2.getCanonicalName());
      }

      return (String[])((String[])var1.toArray(new String[0]));
   }

   // $FF: synthetic method
   JMOService(Object var1) {
      this();
   }

   static {
      try {
         RUNTIME_QUERY = new ObjectName("*:*");
      } catch (MalformedObjectNameException var1) {
         throw new Error(var1);
      }
   }

   class MBeanServerListener implements NotificationListener {
      public void handleNotification(Notification var1, Object var2) {
         try {
            this._handleNotification(var1, var2);
         } catch (Throwable var4) {
            ManagementLogger.logCouldNotRegisterMBeanForJSR77(var4);
         }

      }

      private void _handleNotification(Notification var1, Object var2) {
         MBeanServerNotification var3 = null;
         ObjectName var4 = null;
         if (var1 instanceof MBeanServerNotification) {
            var3 = (MBeanServerNotification)var1;
            var4 = var3.getMBeanName();
            boolean var5 = var3.getType().equals("JMX.mbean.registered");
            if (var5) {
               NotificationHandler var6 = new NotificationHandler(var4, JMOService.this.defaultDomain);
               if (!var6.registerThisObject()) {
                  if (JMOService.debug.isDebugEnabled()) {
                     JMOService.debug.debug("Not Registering:    " + var3.getMBeanName().getCanonicalName());
                  }

                  return;
               }

               JMOService.this.registerMBean(var6);
            } else {
               JMOService.this.unregisterMBean(var4);
            }
         }

      }
   }

   private static class SINGLETON {
      static JMOService service = new JMOService();
   }
}
