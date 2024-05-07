package weblogic.jms.extensions;

import java.lang.reflect.Method;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.jms.common.CDS;
import weblogic.jms.common.CDSSecurityHandle;
import weblogic.jms.common.CrossDomainSecurityManager;
import weblogic.jms.common.DDMemberInformation;
import weblogic.jms.common.DDMembershipChangeEventImpl;
import weblogic.jms.common.DDMembershipChangeListener;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.kernel.KernelStatus;
import weblogic.security.subject.AbstractSubject;
import weblogic.security.subject.SubjectManager;
import weblogic.work.InheritableThreadContext;

public class JMSDestinationAvailabilityHelper {
   private static final int REGISTRATION_MODE_DEFAULT = 0;
   private static final int REGISTRATION_MODE_LOCAL_ONLY = 1;
   private static final int REGISTRATION_MODE_ALL = 2;
   private static JMSDestinationAvailabilityHelper INSTANCE = new JMSDestinationAvailabilityHelper();
   private String currentServerName = null;
   private String currentClusterName = null;
   private String currentDomainName = null;
   private boolean isServer;
   private static boolean coreEngine = false;
   private static boolean initialized = false;
   private static final String DDHM_NAME = "weblogic.jms.common.JMSDestinationAvailabilityHelperManager";

   private JMSDestinationAvailabilityHelper() {
   }

   private void initialize() {
      try {
         if (KernelStatus.isServer() && !coreEngine) {
            Class var1 = Class.forName("weblogic.jms.common.JMSManagementHelper");
            Object var2 = var1.newInstance();
            Method var3 = var1.getMethod("getServerName");
            this.currentServerName = (String)var3.invoke(var2);
            var3 = var1.getMethod("getClusterName");
            this.currentClusterName = (String)var3.invoke(var2);
            var3 = var1.getMethod("getDomainName");
            this.currentDomainName = (String)var3.invoke(var2);
         } else {
            this.currentServerName = "";
            CrossDomainSecurityManager.ensureSubjectManagerInitialized();
         }
      } catch (Exception var4) {
         throw new AssertionError(var4);
      }

      initialized = true;
   }

   public static synchronized JMSDestinationAvailabilityHelper getInstance() {
      if (!initialized) {
         INSTANCE.initialize();
      }

      return INSTANCE;
   }

   public RegistrationHandle register(Hashtable var1, String var2, DestinationAvailabilityListener var3) {
      return this.register((ContextFactory)(new PropertiesContextFactory(var1)), var2, var3, 2);
   }

   private RegistrationHandle register(Hashtable var1, String var2, DestinationAvailabilityListener var3, int var4) {
      return this.register((ContextFactory)(new PropertiesContextFactory(var1)), var2, var3, var4);
   }

   private RegistrationHandle register(ContextFactory var1, String var2, DestinationAvailabilityListener var3, int var4) {
      if (var2 == null) {
         throw new AssertionError("register(): destJNDIName cannot be null");
      } else if (var4 != 0 && var4 != 1 && var4 != 2) {
         throw new AssertionError("register(): Invalid registration mode " + var4);
      } else {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Registering the listener " + var3 + " for destJNDIName=" + var2);
         }

         DestinationAvailabilityListenerWrapper var5 = new DestinationAvailabilityListenerWrapper(var1, var2, var3, var4);
         CDSSecurityHandle var6 = CDS.getCDS().registerForDDMembershipInformation(var5);
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Successfully registered the listener " + var3 + " for destJNDIName=" + var2);
         }

         var1.setSecurityHandle(var6);
         return var5;
      }
   }

   private boolean isLocalWLSServer(DDMemberInformation var1) {
      return this.currentDomainName != null && this.currentDomainName.equals(var1.getDDMemberDomainName()) && this.currentServerName != null && this.currentServerName.equals(var1.getDDMemberServerName());
   }

   private boolean isLocalCluster(DDMemberInformation var1) {
      return this.currentDomainName != null && this.currentDomainName.equals(var1.getDDMemberDomainName()) && this.currentClusterName != null && this.currentClusterName.equals(var1.getDDMemberClusterName());
   }

   private boolean isLocalServerDDMember(DDMemberInformation var1) {
      return this.currentServerName != null && this.currentServerName.equals(var1.getDDMemberServerName()) && this.currentClusterName.equals(var1.getDDMemberClusterName()) && this.currentDomainName.equals(var1.getDDMemberDomainName());
   }

   private boolean isRemoteClusterDDMember(DDMemberInformation var1) {
      return this.currentDomainName == null || this.currentClusterName == null || !this.currentClusterName.equals(var1.getDDMemberClusterName()) || !this.currentDomainName.equals(var1.getDDMemberDomainName());
   }

   private DDMemberInformation[] getLocalServerDDMembers(DDMemberInformation[] var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (this.isLocalServerDDMember(var1[var3])) {
               var2.add(var1[var3]);
            }
         }
      }

      return var2.size() != 0 ? (DDMemberInformation[])((DDMemberInformation[])var2.toArray(new DDMemberInformation[var2.size()])) : null;
   }

   private DDMemberInformation[] getRemoteClusterDDMembers(DDMemberInformation[] var1) {
      ArrayList var2 = new ArrayList();
      if (var1 != null) {
         for(int var3 = 0; var3 < var1.length; ++var3) {
            if (this.isRemoteClusterDDMember(var1[var3])) {
               var2.add(var1[var3]);
            }
         }
      }

      return var2.size() != 0 ? (DDMemberInformation[])((DDMemberInformation[])var2.toArray(new DDMemberInformation[var2.size()])) : null;
   }

   private DDMemberInformation[] filterDDMembers(DestinationAvailabilityListenerWrapper var1, DDMemberInformation[] var2) {
      DDMemberInformation[] var3 = null;

      assert var1 != null;

      if (!this.isServer) {
         return var2;
      } else {
         int var4 = var1.getMode();
         if (var2 != null) {
            DDMemberInformation[] var5;
            if (var4 == 0) {
               var5 = this.getLocalServerDDMembers(var2);
               if (var5 != null) {
                  var3 = var5;
               } else {
                  var3 = this.getRemoteClusterDDMembers(var2);
               }
            } else if (var4 == 1) {
               var5 = this.getLocalServerDDMembers(var2);
               if (var5 != null) {
                  var3 = var5;
               }
            } else {
               var3 = var2;
            }
         }

         return var3;
      }
   }

   private int getDestinationTypeAsInt(DDMemberInformation var1) {
      boolean var2 = var1.isDD();
      String var3 = var1.getDDType();
      if (!var3.equals("javax.jms.Queue") && !var3.equals("javax.jms.Topic")) {
         throw new AssertionError("Unknown Destination Type");
      } else {
         boolean var4 = var3.equals("javax.jms.Queue");
         if (var1.getDestination() != null) {
            if (var2) {
               if (var4) {
                  return 4;
               } else {
                  return var1.getForwardingPolicy() == 0 ? 6 : 5;
               }
            } else {
               return var4 ? 0 : 1;
            }
         } else {
            return var4 ? 2 : 3;
         }
      }
   }

   private DestinationDetail createDestinationDetailFromDDMemberInformation(DDMemberInformation var1) {
      int var2 = this.getDestinationTypeAsInt(var1);
      if (var2 != 2 && var2 != 3) {
         DestinationImpl var3 = (DestinationImpl)var1.getDestination();
         return new DestinationDetailImpl(var1.getDDConfigName(), var1.getDDJNDIName(), this.getDestinationTypeAsInt(var1), var1.getMemberName(), var1.getDDMemberJndiName(), var1.getDDMemberLocalJndiName(), var3.getCreateDestinationArgument(), var3.isPre90() ? null : var3.getServerName(), var3.getPersistentStoreName(), var3, var1.getDDMemberServerName(), var1.getDDMemberMigratableTargetName(), this.isLocalWLSServer(var1), this.isLocalCluster(var1), var1.isAdvancedTopicSupported());
      } else {
         return new DestinationDetailImpl(var1.getDDConfigName(), var1.getDDJNDIName(), this.getDestinationTypeAsInt(var1), var1.getMemberName(), var1.getDDMemberJndiName(), var1.getDDMemberLocalJndiName(), (String)null, (String)null, (String)null, (Destination)null, var1.getDDMemberServerName(), var1.getDDMemberMigratableTargetName(), this.isLocalWLSServer(var1), this.isLocalCluster(var1), var1.isAdvancedTopicSupported());
      }
   }

   private void printDDMemberInfo(DDMemberInformation[] var1) {
      if (var1 != null) {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            System.out.println("member[" + var2 + "]:" + var1[var2].toString());
         }

      }
   }

   static {
      try {
         Class.forName("com.bea.core.encryption.EncryptionService");
         coreEngine = true;
      } catch (Throwable var1) {
         coreEngine = false;
      }

   }

   private interface ContextFactory {
      Context getJNDIContext() throws NamingException;

      AbstractSubject getRegistrationSubject();

      void close();

      String getProviderURL();

      void refreshCtx() throws NamingException;

      void setSecurityHandle(CDSSecurityHandle var1);

      Object lookup(String var1) throws NamingException;

      AbstractSubject getRightJNDISubject();

      AbstractSubject getRightJMSSubject();

      AbstractSubject getOrigSubject();

      boolean isOrigSubDowngraded();
   }

   private final class RefreshWaitLock {
      int waiters;
      NamingException ne;

      private RefreshWaitLock() {
      }

      private synchronized void complete(NamingException var1) {
         this.ne = var1;
         if (this.waiters > 0) {
            this.notifyAll();
         }

      }

      private synchronized boolean waitUntilComplete() throws NamingException {
         if (this.waiters == 0) {
            return false;
         } else {
            ++this.waiters;

            try {
               this.wait();
            } catch (InterruptedException var2) {
            }

            --this.waiters;
            if (this.ne != null) {
               throw this.ne;
            } else {
               return true;
            }
         }
      }

      // $FF: synthetic method
      RefreshWaitLock(Object var2) {
         this();
      }
   }

   private class PropertiesContextFactory implements ContextFactory {
      final Hashtable properties;
      AbstractSubject origSubject;
      AbstractSubject registrationSubject;
      Context ctx;
      CDSSecurityHandle secHandle;
      boolean origSubDowngraded = false;
      RefreshWaitLock refreshWaitLock = JMSDestinationAvailabilityHelper.this.new RefreshWaitLock();
      InheritableThreadContext originalContext;

      PropertiesContextFactory(Hashtable var2) {
         this.properties = var2 == null ? new Hashtable() : new Hashtable(var2);
         this.properties.put("weblogic.jndi.disableLoggingOfWarningMsg", "true");
         this.origSubject = CrossDomainSecurityManager.getCurrentSubject();
         if (this.origSubject == null || JMSDestinationAvailabilityHelper.this.isServer && CrossDomainSecurityManager.getCrossDomainSecurityUtil().isKernelIdentity(this.origSubject)) {
            this.origSubject = SubjectManager.getSubjectManager().getAnonymousSubject();
            this.origSubDowngraded = true;
         }

         this.originalContext = InheritableThreadContext.getContext();
      }

      public AbstractSubject getOrigSubject() {
         return this.origSubject;
      }

      public boolean isOrigSubDowngraded() {
         return this.origSubDowngraded;
      }

      public Context getJNDIContext() throws NamingException {
         this.refreshCtx();
         synchronized(this) {
            return this.ctx;
         }
      }

      public void refreshCtx() throws NamingException {
         ClassLoader var1 = this.originalContext.pushMultiThread();

         try {
            if (this.refreshWaitLock.waitUntilComplete()) {
               return;
            }

            NamingException var2 = null;
            InitialContext var3 = null;
            AbstractSubject var4 = null;

            try {
               var3 = this.properties == null ? new InitialContext() : new InitialContext(this.properties);
               var4 = CrossDomainSecurityManager.getCurrentSubject();
            } catch (NamingException var16) {
               var2 = var16;
            } catch (Throwable var17) {
               var2 = new NamingException(var17.getMessage());
               var2.setRootCause(var17);
            }

            synchronized(this) {
               if (this.ctx != null) {
                  try {
                     this.ctx.close();
                  } catch (NamingException var15) {
                  }
               }

               this.ctx = var3;
               if (var2 == null) {
                  this.registrationSubject = var4;
               }
            }

            this.refreshWaitLock.complete(var2);
            if (var2 != null) {
               throw var2;
            }
         } finally {
            this.originalContext.popMultiThread(var1);
         }

      }

      public synchronized AbstractSubject getRegistrationSubject() {
         return this.registrationSubject;
      }

      public synchronized void close() {
         try {
            if (this.ctx != null) {
               this.ctx.close();
            }

            if (this.secHandle != null) {
               this.secHandle.close();
            }
         } catch (NamingException var2) {
         }

      }

      public String getProviderURL() {
         return this.properties == null ? null : (String)this.properties.get("java.naming.provider.url");
      }

      public void setSecurityHandle(CDSSecurityHandle var1) {
         this.secHandle = var1;
      }

      private boolean hasRegistrationCredentials() {
         if (this.properties == null) {
            return false;
         } else {
            String var1 = (String)this.properties.get("java.naming.security.principal");
            return var1 != null;
         }
      }

      public AbstractSubject getRightJNDISubject() {
         if (this.hasRegistrationCredentials()) {
            return this.getRegistrationSubject();
         } else {
            return JMSDestinationAvailabilityHelper.this.isServer && this.secHandle.isRemoteDomain() ? SubjectManager.getSubjectManager().getAnonymousSubject() : this.origSubject;
         }
      }

      public AbstractSubject getRightJMSSubject() {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Getting right jms subject: properties= " + this.properties);
         }

         AbstractSubject var1 = this.getForeignSubject();
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("foreign subject= " + var1);
         }

         if (var1 != null) {
            return var1;
         } else if (!this.hasRegistrationCredentials()) {
            return this.origSubject;
         } else {
            return JMSDestinationAvailabilityHelper.this.isServer && (this.secHandle.isRemoteDomain() || CrossDomainSecurityManager.getCrossDomainSecurityUtil().isKernelIdentity(this.registrationSubject)) ? SubjectManager.getSubjectManager().getAnonymousSubject() : this.registrationSubject;
         }
      }

      private AbstractSubject getForeignSubject() {
         if (JMSDebug.JMSCDS.isDebugEnabled()) {
            JMSDebug.JMSCDS.debug("Getting foreign subject isforeign= " + this.secHandle.isForeignJMSServer());
         }

         return this.secHandle.isForeignJMSServer() ? this.secHandle.getForeignSubject() : null;
      }

      public Object lookup(final String var1) throws NamingException {
         try {
            AbstractSubject var2 = this.getRightJNDISubject();
            return CrossDomainSecurityManager.runAs(var2, new PrivilegedExceptionAction() {
               public Object run() throws NamingException {
                  try {
                     return PropertiesContextFactory.this.ctx.lookup(var1);
                  } catch (NamingException var2) {
                     if (var2 instanceof NameNotFoundException) {
                        throw var2;
                     } else {
                        PropertiesContextFactory.this.refreshCtx();
                        return PropertiesContextFactory.this.ctx.lookup(var1);
                     }
                  }
               }
            });
         } catch (PrivilegedActionException var5) {
            Exception var3 = var5.getException();
            if (var3 instanceof NamingException) {
               throw (NamingException)var3;
            } else {
               NamingException var4 = new NamingException(var3.getMessage());
               throw var4;
            }
         }
      }
   }

   private final class DestinationAvailabilityListenerWrapper implements DDMembershipChangeListener, RegistrationHandle {
      private ContextFactory ctxFactory;
      private String destJNDIName;
      private DestinationAvailabilityListener listener;
      private int mode;
      InheritableThreadContext originalContext;

      public DestinationAvailabilityListenerWrapper(ContextFactory var2, String var3, DestinationAvailabilityListener var4, int var5) {
         this.ctxFactory = var2;
         this.destJNDIName = var3;
         this.listener = var4;
         this.mode = var5;
         this.originalContext = InheritableThreadContext.getContext();
      }

      public String getDDJNDIName() {
         return this.destJNDIName;
      }

      public DestinationAvailabilityListener getDestinationAvailabilityListener() {
         return this.listener;
      }

      public int getMode() {
         return this.mode;
      }

      public String getDestinationName() {
         return this.destJNDIName;
      }

      public String getProviderURL() {
         return this.ctxFactory.getProviderURL();
      }

      public InitialContext getInitialContext() throws NamingException {
         return (InitialContext)this.ctxFactory.getJNDIContext();
      }

      public AbstractSubject getSubject() {
         return this.ctxFactory.getRegistrationSubject();
      }

      public void onDDMembershipChange(DDMembershipChangeEventImpl var1) {
         if (this.listener != null) {
            DDMemberInformation[] var2 = JMSDestinationAvailabilityHelper.this.filterDDMembers(this, var1.getRemovedDDMemberInformation());
            if (var2 != null) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("Valid Removed DDMembers info:");
                  JMSDestinationAvailabilityHelper.this.printDDMemberInfo(var2);
               }

               int var3 = var2.length;
               final DestinationDetailImpl[] var4 = new DestinationDetailImpl[var3];

               for(int var5 = 0; var5 < var3; ++var5) {
                  var4[var5] = JMSDestinationAvailabilityHelper.this.createDestinationDetailFromDDMemberInformation(var2[var5]);
               }

               this.callOutListener(new PrivilegedAction() {
                  public Object run() {
                     DestinationAvailabilityListenerWrapper.this.listener.onDestinationsUnavailable(DestinationAvailabilityListenerWrapper.this.destJNDIName, Arrays.asList(var4));
                     return null;
                  }
               });
            }

            DDMemberInformation[] var7 = JMSDestinationAvailabilityHelper.this.filterDDMembers(this, var1.getAddedDDMemberInformation());
            if (var7 != null) {
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("Valid Added DDMembers info:");
                  JMSDestinationAvailabilityHelper.this.printDDMemberInfo(var7);
               }

               int var8 = var7.length;
               final DestinationDetailImpl[] var9 = new DestinationDetailImpl[var8];

               for(int var6 = 0; var6 < var8; ++var6) {
                  var9[var6] = JMSDestinationAvailabilityHelper.this.createDestinationDetailFromDDMemberInformation(var7[var6]);
               }

               this.callOutListener(new PrivilegedAction() {
                  public Object run() {
                     DestinationAvailabilityListenerWrapper.this.listener.onDestinationsAvailable(DestinationAvailabilityListenerWrapper.this.destJNDIName, Arrays.asList(var9));
                     return null;
                  }
               });
            }

         }
      }

      private void callOutListener(PrivilegedAction var1) {
         ClassLoader var2 = this.originalContext.pushMultiThread();

         try {
            if (this.ctxFactory.isOrigSubDowngraded()) {
               CrossDomainSecurityManager.runAs(this.ctxFactory.getOrigSubject(), var1);
            } else {
               var1.run();
            }
         } finally {
            this.originalContext.popMultiThread(var2);
         }

      }

      public void unregister() {
         synchronized(this) {
            if (this.listener != null) {
               CDS.getCDS().unregisterDDMembershipChangeListener(this);
               if (JMSDebug.JMSCDS.isDebugEnabled()) {
                  JMSDebug.JMSCDS.debug("Successfully unregistered the listener " + this.listener + " for destJNDIName=" + this.destJNDIName);
               }

               this.listener = null;
               this.ctxFactory.close();
            }
         }
      }

      public void onFailure(final String var1, final Exception var2) {
         if (this.listener != null) {
            this.callOutListener(new PrivilegedAction() {
               public Object run() {
                  DestinationAvailabilityListenerWrapper.this.listener.onFailure(var1, var2);
                  return null;
               }
            });
         }
      }

      public final ConnectionFactory lookupConnectionFactory(String var1) throws NamingException {
         ClassLoader var2 = this.originalContext.pushMultiThread();

         ConnectionFactory var3;
         try {
            var3 = (ConnectionFactory)this.ctxFactory.lookup(var1);
         } finally {
            this.originalContext.popMultiThread(var2);
         }

         return var3;
      }

      public final Destination lookupDestination(String var1) throws NamingException {
         ClassLoader var2 = this.originalContext.pushMultiThread();

         Destination var3;
         try {
            var3 = (Destination)this.ctxFactory.lookup(var1);
         } finally {
            this.originalContext.popMultiThread(var2);
         }

         return var3;
      }

      public final Object runAs(PrivilegedExceptionAction var1) throws PrivilegedActionException {
         ClassLoader var2 = this.originalContext.pushMultiThread();

         Object var3;
         try {
            var3 = CrossDomainSecurityManager.runAs(this.ctxFactory.getRightJMSSubject(), var1);
         } finally {
            this.originalContext.popMultiThread(var2);
         }

         return var3;
      }
   }
}
