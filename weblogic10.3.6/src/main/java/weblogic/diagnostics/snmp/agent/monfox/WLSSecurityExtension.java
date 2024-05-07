package weblogic.diagnostics.snmp.agent.monfox;

import java.security.AccessController;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.resource.spi.security.PasswordCredential;
import monfox.toolkit.snmp.engine.SnmpEngineID;
import monfox.toolkit.snmp.v3.usm.ext.UsmUserSecurityExtension;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.CredentialManager;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.RemoteResource;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public class WLSSecurityExtension implements UsmUserSecurityExtension, TimerListener {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugSNMPAgent");
   private static final AuthenticatedSubject KERNEL_ID = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final long DEFAULT_CACHE_FLUSH_PERIOD = 300000L;
   private static WLSSecurityExtension SINGLETON;
   private CredentialManager credentialManager;
   private SnmpResource authResource = SnmpResource.getAuthenticationResource();
   private SnmpResource privResource = SnmpResource.getPrivacyResource();
   private Timer cacheFlushTimer;
   private long cacheFlushPeriod;
   private RemoteResource testResource = new RemoteResource((String)null, (String)null, (String)null, (String)null, "SNMP_AUTH");
   private ConcurrentHashMap userInfos = new ConcurrentHashMap();
   private int securityLevel = 0;
   private int authProtocol = 0;
   private int privProtocol = 3;

   public static synchronized WLSSecurityExtension getInstance() {
      if (SINGLETON == null) {
         SINGLETON = new WLSSecurityExtension();
      }

      return SINGLETON;
   }

   private WLSSecurityExtension() {
      this.credentialManager = (CredentialManager)SecurityServiceManager.getSecurityService(KERNEL_ID, SecurityServiceManager.getDefaultRealmName(), ServiceType.CREDENTIALMANAGER);
      this.setLocalizedKeyCacheInvalidationInterval(300000L);
   }

   private byte[] getPwd(String var1, boolean var2) {
      SnmpResource var3 = var2 ? this.authResource : this.privResource;
      Object[] var4 = this.credentialManager.getCredentials(KERNEL_ID, var1, var3, (ContextHandler)null, "weblogic.UserPassword");
      int var5 = var4 != null ? var4.length : 0;

      for(int var6 = 0; var6 < var5; ++var6) {
         if (var4[var6] instanceof PasswordCredential) {
            String var7 = new String(((PasswordCredential)var4[var6]).getPassword());
            return var7.getBytes();
         }
      }

      return null;
   }

   private boolean isValidUserInfo(WLSUserInfo var1) {
      return this.getMonfoxSecurityLevel() == var1.getSecLevel() && this.getMonfoxAuthProtocol() == var1.getAuthProtocol() && this.getMonfoxPrivProtocol() == var1.getPrivProtocol();
   }

   private WLSUserInfo getCachedUserInfo(String var1, SnmpEngineID var2) {
      WLSUserInfo var3 = null;
      synchronized(this.userInfos) {
         if (this.userInfos.containsKey(var1)) {
            HashMap var5 = (HashMap)this.userInfos.get(var1);
            if (var5 != null && var5.containsKey(var2)) {
               if (DEBUG_LOGGER.isDebugEnabled()) {
                  DEBUG_LOGGER.debug("Found cached UserInfo for user " + var1 + ", engineID: " + var2);
               }

               var3 = (WLSUserInfo)var5.get(var2);
               if (!this.isValidUserInfo(var3)) {
                  if (DEBUG_LOGGER.isDebugEnabled()) {
                     DEBUG_LOGGER.debug("Cached UserInfo for user " + var1 + ", engineID " + var2 + " is invalid, removing from cache");
                  }

                  var5.remove(var2);
                  if (var5.size() == 0) {
                     this.userInfos.remove(var1);
                  }

                  var3 = null;
               }
            }
         }

         return var3;
      }
   }

   private WLSUserInfo addUserInfoToCache(String var1, SnmpEngineID var2) {
      WLSUserInfo var3 = null;

      try {
         var3 = this.createUserInfo(var1, var2);
      } catch (NoSuchAlgorithmException var8) {
         throw new RuntimeException(var8);
      }

      HashMap var4 = null;
      synchronized(this.userInfos) {
         if (this.userInfos.containsKey(var1)) {
            var4 = (HashMap)this.userInfos.get(var1);
         } else {
            var4 = new HashMap();
            this.userInfos.put(var1, var4);
         }

         var4.put(var2, var3);
         return var3;
      }
   }

   public UsmUserSecurityExtension.UserInfo getUserInfo(String var1, SnmpEngineID var2) {
      WLSUserInfo var3 = this.getCachedUserInfo(var1, var2);
      if (var3 == null) {
         var3 = this.addUserInfoToCache(var1, var2);
      }

      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Returning UserInfo for " + var1 + ", engineID + " + var2.toString() + ", nonExistentUser=" + var3.isNonExistentUser());
      }

      if (var3.isNonExistentUser()) {
         var3 = null;
      }

      return var3;
   }

   private WLSUserInfo createUserInfo(String var1, SnmpEngineID var2) throws NoSuchAlgorithmException {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("Creating UserInfo for " + var1);
      }

      byte[] var3 = this.getPwd(var1, true);
      byte[] var4 = this.getPwd(var1, false);
      boolean var5 = var3 == null;
      WLSUserInfo var6 = new WLSUserInfo(var2, var1, this.getMonfoxSecurityLevel(), this.getMonfoxAuthProtocol(), this.getMonfoxPrivProtocol(), var3, var4, var5);
      this.clearBuffer(var3);
      this.clearBuffer(var4);
      return var6;
   }

   private void clearBuffer(byte[] var1) {
      int var2 = var1 != null ? var1.length : 0;

      for(int var3 = 0; var3 < var2; ++var3) {
         var1[var3] = 0;
      }

   }

   int getAuthProtocol() {
      return this.authProtocol;
   }

   void setAuthProtocol(int var1) {
      this.authProtocol = var1;
   }

   int getPrivProtocol() {
      return this.privProtocol;
   }

   void setPrivProtocol(int var1) {
      this.privProtocol = var1;
   }

   int getSecurityLevel() {
      return this.securityLevel;
   }

   void setSecurityLevel(int var1) {
      this.securityLevel = var1;
   }

   private int getMonfoxSecurityLevel() {
      return SecurityUtil.convertSNMPAgentToolkitSecurityLevel(this.securityLevel);
   }

   private int getMonfoxAuthProtocol() {
      switch (this.authProtocol) {
         case 0:
            return 0;
         case 1:
            return 1;
         default:
            throw new IllegalArgumentException();
      }
   }

   private int getMonfoxPrivProtocol() {
      switch (this.privProtocol) {
         case 2:
            return 2;
         case 3:
            return 4;
         default:
            throw new IllegalArgumentException();
      }
   }

   public synchronized void setLocalizedKeyCacheInvalidationInterval(long var1) {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("WLSSecurityExtension: Setting userInfo cache flush period = " + var1);
      }

      if (var1 > 0L) {
         if (this.cacheFlushTimer != null) {
            this.cacheFlushTimer.cancel();
         }

         TimerManagerFactory var3 = TimerManagerFactory.getTimerManagerFactory();
         TimerManager var4 = var3.getDefaultTimerManager();
         this.cacheFlushTimer = var4.scheduleAtFixedRate(this, 0L, var1);
         this.cacheFlushPeriod = var1;
      }
   }

   public long getLocalizedKeyCacheInvalidationInterval() {
      return this.cacheFlushPeriod;
   }

   public void timerExpired(Timer var1) {
      this.clearUserInfos();
   }

   public void clearUserInfos() {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("WLSSecurityExtension: Flushing user info cache");
      }

      this.userInfos.clear();
   }

   public void invalidateLocalizedKeyCache(String var1) {
      if (DEBUG_LOGGER.isDebugEnabled()) {
         DEBUG_LOGGER.debug("WLSSecurityExtension: Flushing user info cache for " + var1);
      }

      synchronized(this.userInfos) {
         this.userInfos.remove(var1);
      }
   }
}
