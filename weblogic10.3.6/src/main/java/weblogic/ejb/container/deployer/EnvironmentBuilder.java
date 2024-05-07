package weblogic.ejb.container.deployer;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.ejb.EJBContext;
import javax.ejb.TimerService;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;
import javax.xml.ws.WebServiceContext;
import org.omg.CORBA.ORB;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.cacheprovider.coherence.jndi.CoherenceOpaqueReference;
import weblogic.deployment.BaseEnvironmentBuilder;
import weblogic.deployment.EnvironmentException;
import weblogic.deployment.JMSConnFactoryOpaqueReferenceImpl;
import weblogic.deployment.URLOpaqueReferenceImpl;
import weblogic.deployment.jms.PooledConnectionFactory;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.interfaces.BeanInfo;
import weblogic.j2ee.J2EELogger;
import weblogic.j2ee.descriptor.EjbLocalRefBean;
import weblogic.j2ee.descriptor.EjbRefBean;
import weblogic.j2ee.descriptor.EnvEntryBean;
import weblogic.j2ee.descriptor.ResourceEnvRefBean;
import weblogic.j2ee.descriptor.ResourceRefBean;
import weblogic.logging.Loggable;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.AssertionError;
import weblogic.utils.Debug;
import weblogic.utils.StackTraceUtils;
import weblogic.work.ShutdownCallback;
import weblogic.work.j2ee.J2EEWorkManager;

public final class EnvironmentBuilder extends BaseEnvironmentBuilder {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private BeanInfo bi;
   protected static final String EJB_CONTEXT_BINDING = "comp/EJBContext";
   protected static final String TIMERSERVICE_BINDING = "comp/TimerService";
   protected static final String WEB_SERVICE_CONTEXT_BINDING = "comp/WebServiceContext";

   public EnvironmentBuilder(Context var1, String var2, String var3, String var4, BeanInfo var5) throws NamingException {
      super(var1, var2, var3, var4);
      this.bi = var5;
   }

   protected String transformJNDIName(String var1) {
      return transformJNDIName(var1, this.applicationName);
   }

   private Object getValue(EnvEntryBean var1) throws EnvironmentException {
      String var2 = var1.getEnvEntryType();
      String var3 = var1.getEnvEntryValue();
      if (DEBUG) {
         HashSet var4 = new HashSet(Arrays.asList((Object[])(new String[]{"java.lang.String", "java.lang.Boolean", "java.lang.Integer", "java.lang.Double", "java.lang.Float", "java.lang.Short", "java.lang.Long", "java.lang.Byte", "java.lang.Character"})));
         Debug.assertion(var4.contains(var2));
      }

      if ("java.lang.String".equals(var2)) {
         return var3 == null ? "" : var3;
      } else if ("java.lang.Character".equals(var2)) {
         if (var3 != null && var3.length() > 0) {
            return new Character(var3.charAt(0));
         } else {
            Loggable var8 = EJBLogger.logcharEnvEntryHasLengthZeroLoggable();
            throw new EnvironmentException(var8.getMessage());
         }
      } else {
         try {
            Class var7 = ClassLoader.getSystemClassLoader().loadClass(var2);
            Constructor var5 = var7.getConstructor(String.class);
            return var5.newInstance(var3);
         } catch (Exception var6) {
            throw new AssertionError(var6);
         }
      }
   }

   protected void bindEnvEntry(Object var1) throws NamingException, EnvironmentException {
      EnvEntryBean var2 = (EnvEntryBean)var1;
      if (var2.getEnvEntryValue() != null) {
         this.envCtx.bind(var2.getEnvEntryName(), this.getValue(var2));
      }

   }

   protected void unbindEnvEntry(Object var1) {
      EnvEntryBean var2 = (EnvEntryBean)var1;

      try {
         this.envCtx.unbind(var2.getEnvEntryName());
      } catch (NamingException var4) {
         if (DEBUG) {
            var4.printStackTrace();
         }
      }

   }

   public void removeResourceReferences(Collection var1) {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.iterator();

      while(true) {
         while(var2.hasNext()) {
            ResourceRefBean var3 = (ResourceRefBean)var2.next();
            String var4 = var3.getResType();
            if ("javax.sql.DataSource".equals(var4)) {
               this.removeDataSourceRef(var3);
            } else if (this.isJMSConnectionFactory(var4)) {
               this.removeJMSConnectionFactoryRef(var3);
            } else if ("java.net.URL".equals(var4)) {
               this.removeURLRef(var3);
            } else if ("commonj.work.WorkManager".equals(var4)) {
               this.removeWorkManagerRef(var3);
            } else if ("commonj.timers.TimerManager".equals(var4)) {
               this.removeTimerManagerRef(var3.getResRefName());
            } else if (!"com.tangosol.net.NamedCache".equals(var4) && !"com.tangosol.net.Service".equals(var4)) {
               this.removeConnectorRef(var3);
            } else {
               this.removeCoherenceEntityRef(var3, var4);
            }
         }

         return;
      }
   }

   private boolean isJMSConnectionFactory(String var1) {
      return "javax.jms.QueueConnectionFactory".equals(var1) || "javax.jms.TopicConnectionFactory".equals(var1) || "javax.jms.XAQueueConnectionFactory".equals(var1) || "javax.jms.XATopicConnectionFactory".equals(var1) || "javax.jms.ConnectionFactory".equals(var1) || "javax.jms.XAConnectionFactory".equals(var1);
   }

   public void addResourceReferences(Collection var1, Map var2, BeanInfo var3) throws NamingException, EnvironmentException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var4 = var1.iterator();

      while(true) {
         while(var4.hasNext()) {
            ResourceRefBean var5 = (ResourceRefBean)var4.next();
            String var6 = var5.getResType();
            String var7 = var5.getResRefName();
            String var8 = (String)var2.get(var7);
            if ((var8 == null || var8.length() == 0) && var5.getMappedName() != null) {
               var2.put(var7, var5.getMappedName());
            }

            if ("javax.sql.DataSource".equals(var6)) {
               this.addDataSourceRef(var5, var2);
            } else if (this.isJMSConnectionFactory(var6)) {
               this.addJMSConnectionFactoryRef(var5, var2, var3);
            } else if ("java.net.URL".equals(var6)) {
               this.addURLRef(var5, var2);
            } else if ("commonj.work.WorkManager".equals(var6)) {
               this.addJ2EEWorkManager(var5);
            } else if ("commonj.timers.TimerManager".equals(var6)) {
               this.addTimerManager(var5.getResRefName());
            } else if (!"org.omg.CORBA.ORB".equals(var6) && !"org.omg.CORBA_2_3.ORB".equals(var6)) {
               if (!"com.tangosol.net.NamedCache".equals(var6) && !"com.tangosol.net.Service".equals(var6)) {
                  this.addConnectorRef(var5, var2);
               } else {
                  this.addCoherenceEntityRef(var5, var6);
               }
            } else {
               this.envCtx.bind(var5.getResRefName(), new LinkRef("java:comp/ORB"));
            }
         }

         return;
      }
   }

   private void addCoherenceEntityRef(ResourceRefBean var1, String var2) throws NamingException, EnvironmentException {
      String var3 = var1.getResRefName();
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

   private void removeCoherenceEntityRef(ResourceRefBean var1, String var2) {
      try {
         String var3 = var1.getResRefName();
         this.envCtx.unbind(var3);
      } catch (NamingException var4) {
         J2EELogger.logDebug("Cannot unbind " + var1.getResRefName());
         if (DEBUG) {
            var4.printStackTrace();
         }
      }

   }

   private boolean isResourceShareable(ResourceRefBean var1) {
      boolean var2 = true;
      if (var1.getResSharingScope() != null && var1.getResSharingScope().equalsIgnoreCase("Unshareable")) {
         var2 = false;
      }

      return var2;
   }

   private void addURLRef(ResourceRefBean var1, Map var2) throws NamingException, EnvironmentException {
      String var3 = (String)var2.get(var1.getResRefName());
      if (var3 == null || var3.length() == 0) {
         var3 = this.getJNDIFromRefName(var1, var3);
      }

      boolean var4 = this.isResourceShareable(var1);

      try {
         URL var5 = new URL(var3);
         if (!var4) {
            URLOpaqueReferenceImpl var6 = new URLOpaqueReferenceImpl(var3, this.applicationName);
            this.envCtx.bind(var1.getResRefName(), var6);
         } else {
            this.envCtx.bind(var1.getResRefName(), var5);
         }
      } catch (MalformedURLException var7) {
         this.envCtx.bind(var1.getResRefName(), this.createLinkRef(var3));
      }

      this.addToResCtx(var1, var3);
   }

   private void removeURLRef(ResourceRefBean var1) {
      try {
         this.envCtx.unbind(var1.getResRefName());
      } catch (NamingException var3) {
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   private void removeWorkManagerRef(ResourceRefBean var1) {
      try {
         J2EEWorkManager var2 = (J2EEWorkManager)this.envCtx.lookup(var1.getResRefName());
         var2.shutdown((ShutdownCallback)null);
         this.envCtx.unbind(var1.getResRefName());
      } catch (NamingException var3) {
         J2EELogger.logDebug("Cannot unbind " + var1.getResRefName());
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   private void addJ2EEWorkManager(ResourceRefBean var1) throws NamingException {
      this.envCtx.bind(var1.getResRefName(), J2EEWorkManager.get(this.applicationName, this.moduleName, var1.getResRefName()));
   }

   private void removeConnectorRef(ResourceRefBean var1) {
      try {
         this.envCtx.unbind(var1.getResRefName());
      } catch (NamingException var3) {
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   protected Map createJMSPoolProperties(String var1, AuthenticatedSubject var2) throws NamingException {
      Context var3 = null;

      try {
         var3 = (Context)this.rootCtx.lookup("app/jms");
      } catch (NameNotFoundException var6) {
      }

      HashMap var4 = new HashMap();
      var4.put("JNDIName", var1);
      var4.put("ApplicationName", this.applicationName);
      var4.put("ComponentName", this.componentName);
      var4.put("ComponentType", "EJB");
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

   private void addConnectorRef(ResourceRefBean var1, Map var2) throws NamingException, EnvironmentException {
      String var3 = (String)var2.get(var1.getResRefName());
      if (var3 == null || var3.length() == 0) {
         var3 = this.getJNDIFromRefName(var1, var3);
      }

      String var4 = var1.getResType();
      boolean var5 = this.isResourceShareable(var1);
      boolean var6 = false;
      if (var1.getResAuth() != null && var1.getResAuth().equalsIgnoreCase("Container")) {
         var6 = true;
      }

      this.bindResRef(var4, var5, var6, var3, var1.getResRefName(), 1);
      this.addToResCtx(var1, var3);
   }

   private void addToResCtx(ResourceRefBean var1, String var2) throws NamingException {
      this.resCtx.bind(var1.getResRefName() + "JNDI", var2);
      this.resCtx.bind(var1.getResRefName() + "Auth", var1.getResAuth());
      this.resCtx.bind(var1.getResRefName() + "SharingScope", var1.getResSharingScope());
   }

   private void removeFromResCtx(ResourceRefBean var1) {
      try {
         this.resCtx.unbind(var1.getResRefName() + "JNDI");
      } catch (NamingException var5) {
         if (DEBUG) {
            var5.printStackTrace();
         }
      }

      try {
         this.resCtx.unbind(var1.getResRefName() + "Auth");
      } catch (NamingException var4) {
         if (DEBUG) {
            var4.printStackTrace();
         }
      }

      try {
         this.resCtx.unbind(var1.getResRefName() + "SharingScope");
      } catch (NamingException var3) {
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

   }

   private void removeDataSourceRef(ResourceRefBean var1) {
      try {
         this.envCtx.unbind(var1.getResRefName());
      } catch (NamingException var3) {
         if (DEBUG) {
            var3.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   private void addDataSourceRef(ResourceRefBean var1, Map var2) throws NamingException, EnvironmentException {
      String var3 = (String)var2.get(var1.getResRefName());
      String var4 = var1.getResRefName();
      if (var4 != null && var4.length() != 0) {
         if (var3 == null || var3.length() == 0) {
            var3 = var4;
         }

         Object var5 = null;

         Context var6;
         try {
            var6 = (Context)this.rootCtx.lookup("app");
            var5 = var6.lookup(var3);
         } catch (Throwable var9) {
         }

         if (var5 == null) {
            try {
               var6 = (Context)this.rootCtx.lookup("app");
               var5 = var6.lookup("jdbc/" + var3);
            } catch (Throwable var8) {
            }
         }

         if (var5 == null) {
            try {
               var5 = (new InitialContext()).lookup(var3);
            } catch (Throwable var7) {
            }
         }

         if (var5 != null) {
            this.envCtx.bind(var4, var5);
         } else {
            this.envCtx.bind(var4, this.createLinkRef(var3));
            J2EELogger.logDebug("Cannot find JDBC DataSource at " + var3);
         }

      } else {
         throw new EnvironmentException("ResourceReference has no name set");
      }
   }

   private void addJMSConnectionFactoryRef(ResourceRefBean var1, Map var2, BeanInfo var3) throws NamingException, EnvironmentException {
      String var4 = (String)var2.get(var1.getResRefName());
      if (var4 == null || var4.length() == 0) {
         var4 = this.getJNDIFromRefName(var1, var4);
      }

      boolean var5 = this.isResourceShareable(var1);
      String var6;
      if (!var5) {
         var6 = this.applicationName + "-" + this.componentName + "-" + var4;
      } else {
         var6 = var4;
      }

      boolean var7 = false;
      if (var1.getResAuth() != null && var1.getResAuth().equalsIgnoreCase("Container")) {
         var7 = true;
      }

      AuthenticatedSubject var9 = null;

      try {
         var9 = var3.getRunAsSubject();
      } catch (Exception var13) {
      }

      PooledConnectionFactory var8;
      try {
         var8 = new PooledConnectionFactory(var6, 1, var7, this.createJMSPoolProperties(var4, var9));
      } catch (JMSException var12) {
         Loggable var11 = EJBLogger.logunableToCreateJMSConnectionFactoryLoggable(StackTraceUtils.throwable2StackTrace(var12));
         throw new EnvironmentException(var11.getMessage());
      }

      if (!var5) {
         JMSConnFactoryOpaqueReferenceImpl var10 = new JMSConnFactoryOpaqueReferenceImpl(var6, 1, var7, this.createJMSPoolProperties(var4, var9));
         this.envCtx.bind(var1.getResRefName(), var10);
      } else {
         this.envCtx.bind(var1.getResRefName(), var8);
      }

      this.addToResCtx(var1, var4);
   }

   private void removeJMSConnectionFactoryRef(ResourceRefBean var1) {
      try {
         PooledConnectionFactory var2 = (PooledConnectionFactory)this.envCtx.lookup(var1.getResRefName());
         if (var2 != null) {
            ServerRuntimeMBean var3 = ManagementService.getRuntimeAccess(kernelId).getServerRuntime();
            boolean var4 = var3.getStateVal() == 18;
            var2.close(var4);
         }

         this.envCtx.unbind(var1.getResRefName());
      } catch (Throwable var5) {
         if (DEBUG) {
            var5.printStackTrace();
         }
      }

      this.removeFromResCtx(var1);
   }

   public void removeResourceEnvReferences(Collection var1) {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ResourceEnvRefBean var3 = (ResourceEnvRefBean)var2.next();

         try {
            this.envCtx.unbind(var3.getResourceEnvRefName());
         } catch (NamingException var5) {
            if (DEBUG) {
               var5.printStackTrace();
            }
         }
      }

   }

   public void addResourceEnvReferences(Collection var1, Map var2) throws NamingException, EnvironmentException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var3 = var1.iterator();

      while(true) {
         ResourceEnvRefBean var4;
         String var5;
         while(true) {
            if (!var3.hasNext()) {
               return;
            }

            var4 = (ResourceEnvRefBean)var3.next();
            var5 = (String)var2.get(var4.getResourceEnvRefName());
            if ((var5 == null || var5.length() == 0) && var4.getMappedName() != null) {
               var5 = var4.getMappedName();
            }

            if (var5 != null) {
               break;
            }

            try {
               Class var6 = this.bi.getModuleClassLoader().loadClass(var4.getResourceEnvRefType());
               String var7 = null;
               if (EJBContext.class.isAssignableFrom(var6)) {
                  var7 = "comp/EJBContext";
               } else if (TimerService.class == var6) {
                  var7 = "comp/TimerService";
               } else if (UserTransaction.class == var6) {
                  var7 = "comp/UserTransaction";
               } else if (ORB.class.isAssignableFrom(var6)) {
                  var7 = "comp/ORB";
               } else if (WebServiceContext.class == var6) {
                  var7 = "comp/WebServiceContext";
               }

               if (var7 == null) {
                  break;
               }

               this.envCtx.bind(var4.getResourceEnvRefName(), new LinkRef("java:" + var7));
            } catch (ClassNotFoundException var8) {
               throw new EnvironmentException("Unable to load class: " + var4.getResourceEnvRefType(), var8);
            }
         }

         if (var5 == null || var5.length() == 0) {
            Loggable var9 = EJBLogger.lognoJNDIForResourceEnvRefLoggable(var4.getResourceEnvRefName());
            throw new EnvironmentException(var9.getMessage());
         }

         this.envCtx.bind(var4.getResourceEnvRefName(), this.createLinkRef(var5));
      }
   }

   public void addEJBReferences(Collection var1, Map var2, String var3) throws NamingException, EnvironmentException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var4 = var1.iterator();

      while(true) {
         while(var4.hasNext()) {
            EjbRefBean var5 = (EjbRefBean)var4.next();
            String var6 = (String)var2.get(var5.getEjbRefName());
            if (var6 != null && var6.length() > 0) {
               this.envCtx.rebind(var5.getEjbRefName(), this.createLinkRef(var6));
            } else if (var5.getEjbLink() != null) {
               this.addEJBLinkRef(var5.getEjbRefName(), var5.getEjbLink(), var5.getHome(), var5.getRemote(), var3, false);
            } else if (var5.getMappedName() != null) {
               if (var5.getMappedName().startsWith("weblogic-jndi:")) {
                  var6 = var5.getMappedName().substring("weblogic-jndi:".length(), var5.getMappedName().length());
               } else {
                  var6 = var5.getMappedName() + "#" + var5.getRemote();
               }

               this.envCtx.rebind(var5.getEjbRefName(), this.createLinkRef(var6));
            } else {
               this.autowireEJBRef(var5.getEjbRefName(), var5.getHome(), var5.getRemote());
            }
         }

         return;
      }
   }

   public void removeEJBReferences(Collection var1) throws NamingException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var2 = var1.iterator();

      while(true) {
         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof EjbRefBean) {
               EjbRefBean var8 = (EjbRefBean)var3;

               try {
                  this.envCtx.unbind(var8.getEjbRefName());
               } catch (NamingException var7) {
                  if (DEBUG) {
                     var7.printStackTrace();
                  }
               }
            } else if (var3 instanceof EjbLocalRefBean) {
               EjbLocalRefBean var4 = (EjbLocalRefBean)var3;

               try {
                  this.envCtx.unbind(var4.getEjbRefName());
               } catch (NamingException var6) {
                  if (DEBUG) {
                     var6.printStackTrace();
                  }
               }
            }
         }

         return;
      }
   }

   public void addEJBLocalReferences(Collection var1, Map var2, String var3) throws NamingException, EnvironmentException {
      if (DEBUG) {
         Debug.assertion(var1 != null);
      }

      Iterator var4 = var1.iterator();

      while(true) {
         while(var4.hasNext()) {
            EjbLocalRefBean var5 = (EjbLocalRefBean)var4.next();
            String var6 = (String)var2.get(var5.getEjbRefName());
            if (var6 != null && var6.length() > 0) {
               this.envCtx.bind(var5.getEjbRefName(), this.createLinkRef(var6));
            } else if (var5.getEjbLink() != null) {
               this.addEJBLinkRef(var5.getEjbRefName(), var5.getEjbLink(), var5.getLocalHome(), var5.getLocal(), var3, true);
            } else if (var5.getMappedName() != null) {
               if (var5.getMappedName().startsWith("weblogic-jndi:")) {
                  var6 = var5.getMappedName().substring("weblogic-jndi:".length(), var5.getMappedName().length());
               } else {
                  var6 = var5.getMappedName() + "#" + var5.getLocal();
               }

               this.envCtx.bind(var5.getEjbRefName(), this.createLinkRef(var6));
            } else {
               this.autowireEJBRef(var5.getEjbRefName(), var5.getLocalHome(), var5.getLocal());
            }
         }

         return;
      }
   }

   public void addTimerServiceBinding() throws NamingException {
      if (this.bi.isEJB30()) {
         this.rootCtx.bind("comp/TimerService", new TimerServiceProxyImpl(this.bi));
      }

   }

   public void removeTimerServiceBinding() {
      if (this.bi.isEJB30()) {
         try {
            this.rootCtx.unbind("comp/TimerService");
         } catch (NamingException var2) {
            if (DEBUG) {
               var2.printStackTrace();
            }
         }
      }

   }

   private String getJNDIFromRefName(ResourceRefBean var1, String var2) {
      if (var2 == null || var2.length() == 0) {
         var2 = var1.getResRefName();
      }

      return var2;
   }
}
