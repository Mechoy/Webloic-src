package weblogic.deployment;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.cacheprovider.coherence.jndi.CoherenceOpaqueReference;
import weblogic.deployment.descriptors.EJBReference;
import weblogic.deployment.descriptors.EnvironmentEntry;
import weblogic.deployment.descriptors.IllegalTypeException;
import weblogic.deployment.descriptors.IllegalValueException;
import weblogic.deployment.descriptors.ResourceReference;
import weblogic.deployment.jms.PooledConnectionFactory;
import weblogic.j2ee.J2EELogger;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.work.ShutdownCallback;
import weblogic.work.j2ee.J2EEWorkManager;

public final class EnvironmentBuilder extends BaseEnvironmentBuilder {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public EnvironmentBuilder(Context var1, String var2, String var3) throws NamingException {
      super(var1, var2, var3);
   }

   protected void unbindEnvEntry(Object var1) {
      EnvironmentEntry var2 = (EnvironmentEntry)var1;

      try {
         this.envCtx.unbind(var2.getName());
      } catch (NamingException var4) {
         J2EELogger.logDebug("Cannot remove " + var2.getName());
         if (DEBUG) {
            var4.printStackTrace();
         }
      }

   }

   protected void bindEnvEntry(Object var1) throws NamingException, EnvironmentException {
      EnvironmentEntry var2 = (EnvironmentEntry)var1;

      try {
         String var3 = var2.getName();
         if (var3 != null && var3.length() != 0) {
            this.envCtx.bind(var3, var2.getValue());
            if (DEBUG) {
               Debug.say("Bound env entry " + this.envCtx.getNameInNamespace() + "/" + var2.getName() + " to " + var2.getValue().getClass().getName());
            }

         } else {
            throw new EnvironmentException("EnvironmentEntry has no name set");
         }
      } catch (IllegalValueException var4) {
         throw new EnvironmentException(var4);
      } catch (IllegalTypeException var5) {
         throw new EnvironmentException(var5);
      }
   }

   public void removeResourceReferences(Collection var1) {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ResourceReference var3 = (ResourceReference)var2.next();
         String var4 = null;

         try {
            var4 = var3.getResourceType();
         } catch (IllegalValueException var6) {
            J2EELogger.logDebug("Cannot remove: " + var4);
            continue;
         }

         if ("javax.sql.DataSource".equals(var4)) {
            this.removeDataSourceRef(var3);
         } else if (this.isJMSConnectionFactory(var4)) {
            this.removeJMSConnectionFactoryRef(var3);
         } else if ("java.net.URL".equals(var4)) {
            this.removeURLRef(var3);
         } else if ("commonj.work.WorkManager".equals(var4)) {
            this.removeWorkManagerRef(var3);
         } else if ("commonj.timers.TimerManager".equals(var4)) {
            this.removeTimerManagerRef(var3.getName());
         } else if (!"com.tangosol.net.NamedCache".equals(var4) && !"com.tangosol.net.Service".equals(var4)) {
            this.removeConnectorRef(var3);
         } else {
            this.removeCoherenceEntityRef(var3, var4);
         }
      }

   }

   private boolean isJMSConnectionFactory(String var1) {
      return "javax.jms.QueueConnectionFactory".equals(var1) || "javax.jms.TopicConnectionFactory".equals(var1) || "javax.jms.XAQueueConnectionFactory".equals(var1) || "javax.jms.XATopicConnectionFactory".equals(var1) || "javax.jms.ConnectionFactory".equals(var1) || "javax.jms.XAConnectionFactory".equals(var1);
   }

   public void addResourceReferences(Collection var1) throws NamingException, EnvironmentException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ResourceReference var3 = (ResourceReference)var2.next();
         String var4 = null;

         try {
            var4 = var3.getResourceType();
         } catch (IllegalValueException var6) {
            throw new EnvironmentException(var6.toString());
         }

         if ("javax.sql.DataSource".equals(var4)) {
            this.addDataSourceRef(var3);
         } else if (this.isJMSConnectionFactory(var4)) {
            this.addJMSConnectionFactoryRef(var3);
         } else if ("java.net.URL".equals(var4)) {
            this.addURLRef(var3);
         } else if ("commonj.work.WorkManager".equals(var4)) {
            this.addJ2EEWorkManager(var3);
         } else if ("commonj.timers.TimerManager".equals(var4)) {
            this.addTimerManager(var3.getName());
         } else if (!"com.tangosol.net.NamedCache".equals(var4) && !"com.tangosol.net.Service".equals(var4)) {
            this.addConnectorRef(var3);
         } else {
            this.addCoherenceEntityRef(var3, var4);
         }
      }

   }

   private void addCoherenceEntityRef(ResourceReference var1, String var2) throws NamingException, EnvironmentException {
      String var3 = var1.getName();
      if (var3 != null && var3.length() != 0) {
         String var4 = var1.getMappedName();
         if (var4 != null && var4.length() != 0) {
            CoherenceOpaqueReference var5 = new CoherenceOpaqueReference(var4, var2, Thread.currentThread().getContextClassLoader());
            this.envCtx.bind(var3, var5);
         } else {
            throw new EnvironmentException("ResourceReference has no mappedName set");
         }
      } else {
         throw new EnvironmentException("ResourceReference has no name set");
      }
   }

   private void removeCoherenceEntityRef(ResourceReference var1, String var2) {
      try {
         String var3 = var1.getName();
         this.envCtx.unbind(var3);
      } catch (NamingException var4) {
         J2EELogger.logDebug("Cannot unbind " + var1.getName());
         if (DEBUG) {
            var4.printStackTrace();
         }
      }

   }

   private void addJ2EEWorkManager(ResourceReference var1) throws NamingException, EnvironmentException {
      String var2 = var1.getName();
      if (var2 != null && var2.length() != 0) {
         this.envCtx.bind(var2, J2EEWorkManager.get(this.applicationName, this.moduleName, var2));
      } else {
         throw new EnvironmentException("ResourceReference has no name set");
      }
   }

   private boolean isResourceShareable(ResourceReference var1) {
      boolean var2 = true;
      if (var1.getResourceSharingScope() != null && var1.getResourceSharingScope().equalsIgnoreCase("Unshareable")) {
         var2 = false;
      }

      return var2;
   }

   protected Map createJMSPoolProperties(String var1, AuthenticatedSubject var2) throws NamingException {
      Context var3 = null;

      try {
         var3 = (Context)this.rootCtx.lookup("app/jms");
      } catch (NameNotFoundException var6) {
      }

      HashMap var4 = new HashMap(1);
      var4.put("JNDIName", var1);
      var4.put("JmsApplicationContext", var3);
      if (var2 != null) {
         var4.put("RunAsSubject", var2);
      }

      String var5 = ApplicationVersionUtils.getBindApplicationId();
      if (var5 != null) {
         var4.put("weblogic.jndi.lookupApplicationId", var5);
      }

      return var4;
   }

   private void addConnectorRef(ResourceReference var1) throws NamingException, EnvironmentException {
      if (var1.getName() != null && var1.getName().length() != 0) {
         String var5 = var1.getJNDINameString();
         if (var5 == null || var5.length() == 0) {
            var5 = var1.getName();
         }

         try {
            String var6 = var1.getResourceType();
            boolean var7 = this.isResourceShareable(var1);
            boolean var8 = false;
            if (var1.getResourceAuthModeString() != null && var1.getResourceAuthModeString().equalsIgnoreCase("Container")) {
               var8 = true;
            }

            super.bindResRef(var6, var7, var8, var5, var1.getName(), 2);
         } catch (IllegalValueException var9) {
            throw new EnvironmentException(var9);
         }

         String var2 = var1.getName() + "JNDI";
         String var3 = var1.getName() + "Auth";
         String var4 = var1.getName() + "SharingScope";
         this.resCtx.bind(var2, var1.getJNDIName());
         this.resCtx.bind(var3, var1.getResourceAuthModeString());
         if (var1.getResourceSharingScope() != null) {
            this.resCtx.bind(var4, var1.getResourceSharingScope());
         }

      } else {
         throw new EnvironmentException("ResourceReference has no name set");
      }
   }

   private void removeConnectorRef(ResourceReference var1) {
      try {
         this.envCtx.unbind(var1.getName());
      } catch (NamingException var3) {
         J2EELogger.logDebug("Cannot unbind " + var1.getName());
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   private void removeWorkManagerRef(ResourceReference var1) {
      try {
         J2EEWorkManager var2 = (J2EEWorkManager)this.envCtx.lookup(var1.getName());
         var2.shutdown((ShutdownCallback)null);
         this.envCtx.unbind(var1.getName());
      } catch (NamingException var3) {
         J2EELogger.logDebug("Cannot unbind " + var1.getName());
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   private void removeDataSourceRef(ResourceReference var1) {
      try {
         this.envCtx.unbind(var1.getName());
      } catch (NamingException var3) {
         J2EELogger.logDebug("Cannot unbind " + var1.getName());
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   private void addDataSourceRef(ResourceReference var1) throws NamingException, EnvironmentException {
      String var2 = var1.getJNDINameString();
      String var3 = var1.getName();
      if (var3 != null && var3.length() != 0) {
         if (var2 == null || var2.length() == 0) {
            var2 = var3;
         }

         Object var4 = null;

         Context var5;
         try {
            var5 = (Context)this.rootCtx.lookup("app");
            var4 = var5.lookup(var2);
         } catch (Throwable var8) {
         }

         if (var4 == null) {
            try {
               var5 = (Context)this.rootCtx.lookup("app");
               var4 = var5.lookup("jdbc/" + var2);
            } catch (Throwable var7) {
            }
         }

         if (var4 == null) {
            try {
               var4 = (new InitialContext()).lookup(var2);
            } catch (Throwable var6) {
            }
         }

         if (var4 != null) {
            this.envCtx.bind(var3, var4);
         } else {
            this.envCtx.bind(var3, new LinkRef(var2));
            J2EELogger.logDebug("Cannot find JDBC DataSource at " + var2);
         }

      } else {
         throw new EnvironmentException("ResourceReference has no name set");
      }
   }

   private void addJMSConnectionFactoryRef(ResourceReference var1) throws NamingException, EnvironmentException {
      String var2 = var1.getJNDINameString();
      String var3 = var1.getName();
      if (var3 != null && var3.length() != 0) {
         if (var2 == null || var2.length() == 0) {
            var2 = var3;
         }

         boolean var4 = false;
         if (var1.getResourceAuthModeString() != null && var1.getResourceAuthModeString().equalsIgnoreCase("Container")) {
            var4 = true;
         }

         boolean var5 = this.isResourceShareable(var1);
         String var6;
         if (!var5) {
            var6 = this.applicationName + "-" + this.componentName + "-" + var2;
         } else {
            var6 = var2;
         }

         if (var5) {
            PooledConnectionFactory var7;
            try {
               var7 = new PooledConnectionFactory(var6, 2, var4, this.createJMSPoolProperties(var2, (AuthenticatedSubject)null));
            } catch (JMSException var9) {
               throw new EnvironmentException("Could not create a new JMS ConnectionFactory for the application: " + var9);
            }

            this.envCtx.bind(var3, var7);
         } else {
            JMSConnFactoryOpaqueReferenceImpl var8 = new JMSConnFactoryOpaqueReferenceImpl(var6, 1, var4, this.createJMSPoolProperties(var2, (AuthenticatedSubject)null));
            this.envCtx.bind(var3, var8);
         }

      } else {
         throw new EnvironmentException("ResourceReference has no name set");
      }
   }

   public void removeResourceEnvReferences(Map var1) {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();

         try {
            this.envCtx.unbind((String)var3.getKey());
         } catch (NamingException var5) {
            J2EELogger.logDebug("Cannot remove " + (String)var3.getKey());
            if (DEBUG) {
               var5.printStackTrace();
            }
         }
      }

   }

   private void removeJMSConnectionFactoryRef(ResourceReference var1) {
      try {
         PooledConnectionFactory var2 = (PooledConnectionFactory)this.envCtx.lookup(var1.getName());
         if (var2 != null) {
            ServerRuntimeMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
            boolean var4 = var3.getStateVal() == 18;
            var2.close(var4);
         }

         this.envCtx.unbind(var1.getName());
      } catch (Throwable var5) {
         J2EELogger.logDebug("Cannot unbind " + var1.getName());
         if (DEBUG) {
            var5.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   public void addResourceEnvReferences(Map var1) throws NamingException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry var3 = (Map.Entry)var2.next();
         this.envCtx.bind((String)var3.getKey(), new LinkRef((String)var3.getValue()));
      }

   }

   public void addResourceReference(ResourceReference var1) throws NamingException, EnvironmentException {
      this.addResourceReferences(Arrays.asList((Object[])(new ResourceReference[]{var1})));
   }

   public void addEJBReferences(Collection var1, String var2) throws NamingException, EnvironmentException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         EJBReference var4 = (EJBReference)var3.next();
         if (var4.getName() == null || var4.getName().length() == 0) {
            throw new EnvironmentException("EJB Reference has no name set");
         }

         if (var4.getLinkedEjbName() != null && var4.getLinkedEjbName().length() > 0) {
            this.addEJBLinkRef(var4.getName(), var4.getLinkedEjbName(), var4.getHomeInterfaceName(), var4.getRemoteInterfaceName(), var2, var4.isLocalLink());
         } else if (var4.getJNDIName() != null && var4.getJNDIName().length() > 0) {
            this.envCtx.rebind(var4.getName(), this.createLinkRef(var4.getJNDIName()));
         } else if (var4.getMappedName() != null && var4.getMappedName().length() > 0) {
            String var5 = null;
            if (var4.getMappedName().startsWith("weblogic-jndi:")) {
               var5 = var4.getMappedName().substring("weblogic-jndi:".length(), var4.getMappedName().length());
            } else {
               var5 = var4.getMappedName() + "#" + var4.getRemoteInterfaceName();
            }

            this.envCtx.rebind(var4.getName(), this.createLinkRef(var5));
         } else {
            this.autowireEJBRef(var4.getName(), var4.getHomeInterfaceName(), var4.getRemoteInterfaceName());
         }
      }

   }

   public void removeEJBReferences(Collection var1) {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         EJBReference var3 = (EJBReference)var2.next();

         try {
            this.envCtx.unbind(var3.getName());
         } catch (NamingException var5) {
            J2EELogger.logDebug("Cannot remove " + var3.getName());
            if (DEBUG) {
               var5.printStackTrace();
            }
         }
      }

   }

   public void addEJBReference(EJBReference var1, String var2) throws NamingException, EnvironmentException {
      this.addEJBReferences(Arrays.asList((Object[])(new EJBReference[]{var1})), var2);
   }

   private void removeFromResCtx(ResourceReference var1) {
      try {
         this.resCtx.unbind(var1.getName() + "JNDI");
      } catch (NamingException var4) {
         J2EELogger.logDebug("Cannot remove " + var1.getName() + "JNDI");
         if (DEBUG) {
            var4.printStackTrace();
         }
      }

      try {
         this.resCtx.unbind(var1.getName() + "Auth");
      } catch (NamingException var3) {
         J2EELogger.logDebug("Cannot remove " + var1.getName() + "Auth");
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

   }

   private void addURLRef(ResourceReference var1) throws NamingException, EnvironmentException {
      if (var1.getName() != null && var1.getName().length() != 0) {
         String var5 = var1.getJNDINameString();
         if (var5 == null || var5.length() == 0) {
            var5 = var1.getName();
         }

         try {
            this.envCtx.bind(var1.getName(), new URL(var5));
         } catch (MalformedURLException var7) {
            this.envCtx.bind(var1.getName(), this.createLinkRef(var5));
         }

         String var2 = var1.getName() + "JNDI";
         String var3 = var1.getName() + "Auth";
         String var4 = var1.getName() + "SharingScope";
         this.resCtx.bind(var2, var1.getJNDIName());
         this.resCtx.bind(var3, var1.getResourceAuthModeString());
         if (var1.getResourceSharingScope() != null) {
            this.resCtx.bind(var4, var1.getResourceSharingScope());
         }

      } else {
         throw new EnvironmentException("ResourceReference \"" + var1.getName() + "\" has no name set");
      }
   }

   private void removeURLRef(ResourceReference var1) {
      try {
         this.envCtx.unbind(var1.getName());
      } catch (NamingException var3) {
         J2EELogger.logDebug("Cannot unbind " + var1.getName());
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   protected String transformJNDIName(String var1) {
      return var1;
   }
}
