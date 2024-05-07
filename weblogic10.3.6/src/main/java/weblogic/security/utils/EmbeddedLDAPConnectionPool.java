package weblogic.security.utils;

import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import netscape.ldap.LDAPCache;
import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPException;
import weblogic.ldap.EmbeddedLDAP;
import weblogic.ldap.EmbeddedLDAPConnection;
import weblogic.management.configuration.EmbeddedLDAPMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.shared.LoggerWrapper;

public final class EmbeddedLDAPConnectionPool {
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final int LDAP_VERSION = 3;
   private LDAPServerInfo serverInfo;
   private Pool pool;
   private static final int POOL_SIZE = 6;
   private static final String OBJECTCLASS_ATTR = "objectclass";
   private boolean ignoreCertPathValidators;
   private static LoggerWrapper log = LoggerWrapper.getInstance("DebugEmbeddedLDAP");

   private boolean isDebug() {
      return log.isDebugEnabled();
   }

   private void _debug(String var1) {
      if (log.isDebugEnabled()) {
         log.debug(var1);
      }

   }

   private void debug(String var1, String var2) {
      this._debug("EmbeddedLDAPConnectdionPool." + var1 + ": " + var2);
   }

   public EmbeddedLDAPConnectionPool(LoggerWrapper var1) {
      this(var1, false);
   }

   public EmbeddedLDAPConnectionPool(LoggerWrapper var1, boolean var2) {
      this.ignoreCertPathValidators = false;
      String var3 = "constructor";
      this.ignoreCertPathValidators = var2;
      this.initializeServerInfo();
      this.initializePool();
      if (var1 != null) {
         log = var1;
      }

      if (this.isDebug()) {
         this.debug(var3, "succeeded.  Pool = " + this);
      }

   }

   private void initializeServerInfo() {
      final EmbeddedLDAP var1 = EmbeddedLDAP.getEmbeddedLDAP();
      final EmbeddedLDAPMBean var2 = var1.getEmbeddedLDAPMBean();
      SecurityServiceManager.runAs(kernelId, kernelId, new PrivilegedAction() {
         public Object run() {
            EmbeddedLDAPConnectionPool var10000 = EmbeddedLDAPConnectionPool.this;
            EmbeddedLDAP var10004 = var1;
            String var2x = EmbeddedLDAP.getEmbeddedLDAPHost();
            EmbeddedLDAP var10005 = var1;
            int var1x = EmbeddedLDAP.getEmbeddedLDAPPort();
            EmbeddedLDAP var10006 = var1;
            var10000.serverInfo = new LDAPServerInfo(true, var2x, var1x, EmbeddedLDAP.getEmbeddedLDAPUseSSL(), "cn=Admin", 1, var2);
            return null;
         }
      });
   }

   private void initializePool() {
      int var1 = 6;

      try {
         String var2 = System.getProperty("weblogic.security.providers.utils.EmbeddedLDAPDelegatePoolSize");
         if (var2 != null && var2.length() > 0) {
            var1 = new Integer(var2);
         }
      } catch (Exception var3) {
      }

      this.pool = new Pool(new MyLDAPFactory(), var1);
   }

   public LDAPConnectionHelper getReadOnlyConnection() {
      return new LDAPConnectionHelper(this.pool, false, log);
   }

   public LDAPConnectionHelper getReadWriteConnection() {
      return new LDAPConnectionHelper(this.pool, true, log);
   }

   private class MyLDAPFactory implements Factory {
      private MyLDAPFactory() {
      }

      private void debug(String var1, String var2) {
         EmbeddedLDAPConnectionPool.this._debug("EmbeddedLDAPDelegate.MyLDAPFactory" + var1 + ": " + var2);
      }

      public Object newInstance() throws InvocationTargetException {
         String var1 = "newInstance";

         try {
            EmbeddedLDAPConnection var2 = new EmbeddedLDAPConnection(false, false, EmbeddedLDAPConnectionPool.this.ignoreCertPathValidators);
            if (EmbeddedLDAPConnectionPool.this.isDebug()) {
               this.debug(var1, "created new LDAP connection " + var2);
            }

            if (EmbeddedLDAPConnectionPool.this.isDebug()) {
               var2.setProperty("com.netscape.ldap.trace", "+ldap_trace.log");
            }

            var2.connect(EmbeddedLDAPConnectionPool.this.serverInfo.getHost(), EmbeddedLDAPConnectionPool.this.serverInfo.getPort());
            var2.bind(3, EmbeddedLDAPConnectionPool.this.serverInfo.getPrincipal(), EmbeddedLDAPConnectionPool.this.serverInfo.getCredential());
            if (EmbeddedLDAPConnectionPool.this.serverInfo.getCacheEnabled() && EmbeddedLDAPConnectionPool.this.serverInfo.getCacheTTL() > 0 && EmbeddedLDAPConnectionPool.this.serverInfo.getCacheSize() > 0) {
               var2.setCache(new LDAPCache((long)EmbeddedLDAPConnectionPool.this.serverInfo.getCacheTTL(), (long)(EmbeddedLDAPConnectionPool.this.serverInfo.getCacheSize() * 1024)));
            }

            if (EmbeddedLDAPConnectionPool.this.isDebug()) {
               this.debug(var1, "connection succeeded");
            }

            return var2;
         } catch (LDAPException var3) {
            if (EmbeddedLDAPConnectionPool.this.isDebug()) {
               this.debug(var1, "connection failed " + var3);
            }

            throw new InvocationTargetException(var3);
         }
      }

      public void destroyInstance(Object var1) {
         String var2 = "destroyInstance";

         try {
            if (EmbeddedLDAPConnectionPool.this.isDebug()) {
               this.debug(var2, "destroy LDAP connection " + var1);
            }

            ((LDAPConnection)var1).disconnect();
         } catch (LDAPException var4) {
         }

      }

      // $FF: synthetic method
      MyLDAPFactory(Object var2) {
         this();
      }
   }
}
