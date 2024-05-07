package weblogic.security.acl;

import java.io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import weblogic.cluster.ClusterService;
import weblogic.cluster.ClusterServices;
import weblogic.cluster.MulticastSession;
import weblogic.cluster.RecoverListener;
import weblogic.management.configuration.PasswordPolicyMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.shared.LoggerWrapper;
import weblogic.utils.AssertionError;

/** @deprecated */
public final class PasswordGuessing {
   private static LoggerWrapper log = LoggerWrapper.getInstance("SecurityPasswordPolicy");
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static Hashtable master_invalid_login = new Hashtable();
   private static Vector unused_cache = new Vector();
   private int unused_cache_size;
   private long timestamp_of_current_check;
   private boolean lockout_enabled = false;
   private int lockout_threshold;
   private long lockout_duration;
   private int lockout_duration_min;
   private long lockout_reset_duration;
   private int lockout_gc_threshold;
   private static int sequence_number;
   private static int failure_sequence_number;
   private static int unlock_sequence_number;
   private static String this_server_name;
   private ClusterServices clusterServices = null;
   private MulticastSession multicastSession = null;

   public PasswordGuessing() {
      this.init();
   }

   void init() {
      ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
      if (var1 != null) {
         this_server_name = var1.getName();
      }

      PasswordPolicyMBean var2 = ManagementService.getRuntimeAccess(kernelId).getDomain().getSecurity().getPasswordPolicy();
      this.lockout_enabled = var2.isLockoutEnabled();
      this.lockout_threshold = var2.getLockoutThreshold();
      this.lockout_duration_min = var2.getLockoutDuration();
      this.lockout_duration = (long)this.lockout_duration_min * 60L * 1000L;
      this.lockout_reset_duration = (long)var2.getLockoutResetDuration() * 60L * 1000L;
      this.lockout_gc_threshold = var2.getLockoutGCThreshold();
      this.unused_cache_size = var2.getLockoutCacheSize();
      if (log.isDebugEnabled()) {
         log.debug("PasswordPolicy settings LockoutEnabled=" + this.lockout_enabled + " LockoutThreshold=" + this.lockout_threshold + " LockoutDuration=" + this.lockout_duration_min + " LockoutResetDuration=" + this.lockout_reset_duration / 60L / 1000L + " LockoutGCThreshold=" + this.lockout_gc_threshold + " LockoutCacheSize=" + this.unused_cache_size);
      }

      this.createMulticastSession();
   }

   public boolean isLocked(String var1) {
      if (this.lockout_enabled && master_invalid_login.size() != 0) {
         this.setTimestampOfCurrentCheck();
         if (master_invalid_login.containsKey(var1)) {
            InvalidLogin var2 = (InvalidLogin)master_invalid_login.get(var1);
            long var3 = var2.getLockedTimestamp();
            if (var3 == 0L) {
               if (log.isDebugEnabled()) {
                  log.debug("User " + var1 + " is not yet locked");
               }

               return false;
            } else {
               Security.incrementLoginAttemptsWhileLockedTotalCount();
               Security.incrementInvalidLoginAttemptsTotalCount();
               long var5 = var3 + this.lockout_duration;
               if (this.getTimestampOfCurrentCheck() < var5) {
                  if (log.isDebugEnabled()) {
                     log.debug("User " + var1 + " is still locked");
                  }

                  return true;
               } else {
                  this.clearInvalidLoginRecord(var2);
                  Security.incrementUnlockedUsersTotalCount();
                  Security.decrementLockedUsersCurrentCount();
                  SecurityLogger.logLockoutExpiredInfo(var1);
                  return false;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   void logFailure(String var1) {
      ++failure_sequence_number;
      LoginFailureRecord var2 = this.logFailure(this_server_name, failure_sequence_number, this.getTimestampOfCurrentCheck(), var1);
      if (var2 != null) {
         ++sequence_number;
         SecurityMessage var3 = new SecurityMessage(sequence_number, var2);
         if (log.isDebugEnabled()) {
            log.debug("About to multicast login failure for user " + var1 + " " + var3.toString());
         }

         try {
            if (this.createMulticastSession()) {
               this.multicastSession.send(var3);
               if (log.isDebugEnabled()) {
                  log.debug("Sent multicast for login failure");
               }
            }
         } catch (IOException var5) {
            SecurityLogger.logSendingLoginFailure(var5.toString());
         }
      }

   }

   LoginFailureRecord logFailure(String var1, int var2, long var3, String var5) {
      LoginFailureRecord var6 = null;
      if (!this.lockout_enabled) {
         return var6;
      } else {
         if (log.isDebugEnabled()) {
            log.debug("Login failure for user " + var5);
         }

         if (var1.equals(this_server_name)) {
            Security.incrementInvalidLoginAttemptsTotalCount();
         }

         InvalidLogin var7 = null;
         if (!master_invalid_login.containsKey(var5)) {
            if (unused_cache.size() > 0) {
               if (log.isDebugEnabled()) {
                  log.debug("Retrieving unused invalid login from the cache");
               }

               var7 = (InvalidLogin)unused_cache.elementAt(0);
               unused_cache.removeElementAt(0);
               var7.setName(var5);
            } else {
               var7 = new InvalidLogin(var5);
            }

            master_invalid_login.put(var7.getName(), var7);
         } else {
            var7 = (InvalidLogin)master_invalid_login.get(var5);
         }

         if ((long)master_invalid_login.size() > Security.getInvalidLoginUsersHighCount()) {
            Security.setInvalidLoginUsersHighCount((long)master_invalid_login.size());
         }

         var6 = new LoginFailureRecord(var1, var2, var3, var5);
         var7.addFailure(var6);
         this.cleanOutStaleFailureRecords(var7);
         if (log.isDebugEnabled()) {
            log.debug("User " + var5 + " has " + var7.getFailureCount() + " failures");
         }

         if (var7.getFailureCount() >= this.lockout_threshold) {
            SecurityLogger.logLockingUser(var5, var7.getFailureCount(), this.lockout_duration_min);
            Security.incrementUserLockoutTotalCount();
            Security.incrementLockedUsersCurrentCount();
            var7.setLockedTimestamp(var3);
         }

         this.garbageCollectInvalidLoginRecords();
         return var6;
      }
   }

   void logSuccess(String var1) {
      if (this.unlockLocal(var1)) {
         ++unlock_sequence_number;
         UnlockUserRecord var2 = new UnlockUserRecord(this_server_name, unlock_sequence_number, this.getTimestampOfCurrentCheck(), var1);
         ++sequence_number;
         SecurityMessage var3 = new SecurityMessage(sequence_number, var2);
         if (log.isDebugEnabled()) {
            log.debug("About to multicast unlock user: " + var1 + " " + var3.toString());
         }

         try {
            if (this.createMulticastSession()) {
               this.multicastSession.send(var3);
               if (log.isDebugEnabled()) {
                  log.debug("Sent multicast for unlock user");
               }
            }
         } catch (IOException var5) {
            if (var1 == null) {
               SecurityLogger.logBroadcastUnlockUserFailure("null", var5.toString());
            } else {
               SecurityLogger.logBroadcastUnlockUserFailure(var1, var5.toString());
            }
         }
      }

   }

   private boolean unlockLocal(String var1) {
      if (this.lockout_enabled && master_invalid_login.size() != 0) {
         if (var1 == null) {
            throw new AssertionError("Received a null user name");
         } else if (!master_invalid_login.containsKey(var1)) {
            return false;
         } else {
            InvalidLogin var2 = (InvalidLogin)master_invalid_login.get(var1);
            if (var2 == null) {
               throw new AssertionError("Hashtable has the key but can't get the entry");
            } else {
               long var3 = var2.getLockedTimestamp();
               if (log.isDebugEnabled()) {
                  log.debug("Unlocked user or successful login" + var1 + " cleaning out old invalid login record");
               }

               InvalidLogin var5 = (InvalidLogin)master_invalid_login.remove(var1);
               var5.erase();
               if (unused_cache.size() < this.unused_cache_size) {
                  if (log.isDebugEnabled()) {
                     log.debug("Putting unused invalid login record in cache");
                  }

                  unused_cache.addElement(var5);
               } else {
                  var5 = null;
               }

               if (var3 != 0L) {
                  Security.incrementUnlockedUsersTotalCount();
                  Security.decrementLockedUsersCurrentCount();
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   private void cleanOutStaleFailureRecords(InvalidLogin var1) {
      if (var1 != null) {
         Vector var2 = var1.getFailures();
         if (var2 == null) {
            throw new AssertionError("Inconsistent InvalidLogin record");
         } else if (var2.size() != 0) {
            for(int var3 = 0; var3 < var2.size(); ++var3) {
               LoginFailureRecord var4 = (LoginFailureRecord)var2.elementAt(var3);
               if (this.getTimestampOfCurrentCheck() - var4.timestamp <= this.lockout_reset_duration) {
                  break;
               }

               if (log.isDebugEnabled()) {
                  log.debug("Discarding stale login failure record");
               }

               var2.removeElementAt(var3);
               var4 = null;
            }

         }
      }
   }

   private void garbageCollectInvalidLoginRecords() {
      long var1 = System.currentTimeMillis();
      InvalidLogin var3 = null;
      LoginFailureRecord var4 = null;
      int var5 = master_invalid_login.size();
      if (var5 != 0 && var5 >= this.lockout_gc_threshold) {
         Enumeration var6 = master_invalid_login.elements();

         while(var6.hasMoreElements()) {
            var3 = (InvalidLogin)var6.nextElement();
            if (var3 == null) {
               throw new AssertionError("Enumerator returned a null element for a key");
            }

            long var7 = var3.getLockedTimestamp();
            if (var7 == 0L) {
               var4 = (LoginFailureRecord)var3.getLatestFailure();
               if (var4 != null && var4.eventTime() < var1 - this.lockout_reset_duration) {
                  if (log.isDebugEnabled()) {
                     log.debug("Garbage collecting InvalidLogin record for user: " + var3.getName());
                  }

                  this.clearInvalidLoginRecord(var3);
               }
            }
         }

         if (log.isDebugEnabled()) {
            log.debug("InvalidLogin Record GC done: " + (var5 - master_invalid_login.size()) + " records garbage collected");
         }

      } else {
         if (log.isDebugEnabled()) {
            log.debug("InvalidLogin Record GC not needed");
         }

      }
   }

   private void clearInvalidLoginRecord(InvalidLogin var1) {
      String var2 = null;
      var2 = var1.getName();
      InvalidLogin var3 = (InvalidLogin)master_invalid_login.remove(var2);
      var3.erase();
      if (unused_cache.size() < this.unused_cache_size) {
         if (log.isDebugEnabled()) {
            log.debug("Putting unused invalid login record in cache");
         }

         unused_cache.addElement(var3);
      } else {
         var3 = null;
      }

   }

   private long getTimestampOfCurrentCheck() {
      if (this.timestamp_of_current_check == 0L) {
         this.setTimestampOfCurrentCheck();
      }

      return this.timestamp_of_current_check;
   }

   private void setTimestampOfCurrentCheck() {
      this.timestamp_of_current_check = System.currentTimeMillis();
   }

   public void processSecurityMessage(int var1, SecurityMulticastRecord var2) {
      if (var2 instanceof LoginFailureRecord || var2 instanceof UnlockUserRecord) {
         if (!var2.eventOrigin().equals(this_server_name)) {
            if (var2 instanceof LoginFailureRecord) {
               LoginFailureRecord var3 = (LoginFailureRecord)var2;
               if (log.isDebugEnabled()) {
                  log.debug("Received a LoginFailureRecord: " + var3.toString());
               }

               this.logFailure(var3.eventOrigin(), var3.eventSequenceNumber(), var3.eventTime(), var3.userName());
            } else if (var2 instanceof UnlockUserRecord) {
               UnlockUserRecord var4 = (UnlockUserRecord)var2;
               if (log.isDebugEnabled()) {
                  log.debug("Received an UnlockUserRecord: " + var4.toString());
               }

               if (this.unlockLocal(var4.userName()) && log.isDebugEnabled()) {
                  log.debug("Locked user has now been unlocked locally");
               }
            }

         }
      }
   }

   public void runtimeClearLockout(String var1) {
      if (var1 == null) {
         throw new AssertionError("Received a null user name");
      } else if (var1.equals("")) {
         if (log.isDebugEnabled()) {
            log.debug("clearLockout was passed an empty user name");
         }

      } else if (this.lockout_enabled) {
         String var2 = "weblogic.passwordpolicy";
         User var3 = Security.getCurrentUser();
         if (!Security.hasPermission(var3, var2, (Permission)Security.getRealm().getPermission("unlockuser"), '.')) {
            Security.checkPermission((Principal)var3, var2, Security.getRealm().getPermission("unlockuser"), '.');
         }

         this.logSuccess(var1);
         SecurityLogger.logExplicitUserUnlockInfo(var1);
      }
   }

   public long getLastLoginFailure(String var1) {
      if (!this.lockout_enabled) {
         return 0L;
      } else if (var1 == null) {
         throw new AssertionError("Received a null user name");
      } else if (var1.equals("")) {
         if (log.isDebugEnabled()) {
            log.debug("getLastLoginFailure was passed a null or empty user name");
         }

         return 0L;
      } else {
         InvalidLogin var2 = null;
         if (!master_invalid_login.containsKey(var1)) {
            return 0L;
         } else {
            var2 = (InvalidLogin)master_invalid_login.get(var1);
            if (var2 == null) {
               throw new AssertionError("Inconsistent hashtable - key exists but not value");
            } else {
               Vector var3 = var2.getFailures();
               if (var3 == null) {
                  throw new AssertionError("Inconsistent InvalidLogin record");
               } else if (var3.size() == 0) {
                  return 0L;
               } else {
                  LoginFailureRecord var4 = (LoginFailureRecord)var3.lastElement();
                  return var4 != null ? var4.timestamp : 0L;
               }
            }
         }
      }
   }

   public int getLoginFailureCount(String var1) {
      if (!this.lockout_enabled) {
         return 0;
      } else if (var1 == null) {
         throw new AssertionError("Received a null user name");
      } else if (var1.equals("")) {
         if (log.isDebugEnabled()) {
            log.debug("getLoginFailureCount was passed a null or empty user name");
         }

         return 0;
      } else {
         InvalidLogin var2 = null;
         if (!master_invalid_login.containsKey(var1)) {
            return 0;
         } else {
            var2 = (InvalidLogin)master_invalid_login.get(var1);
            if (var2 == null) {
               throw new AssertionError("Inconsistent hashtable - key exists but not value");
            } else {
               Vector var3 = var2.getFailures();
               return var3 == null ? 0 : var3.size();
            }
         }
      }
   }

   private boolean createMulticastSession() {
      if (this.multicastSession != null) {
         return true;
      } else {
         this.clusterServices = ClusterService.getServices();
         if (this.clusterServices != null) {
            this.multicastSession = this.clusterServices.createMulticastSession((RecoverListener)null, -1);
            if (this.multicastSession != null) {
               return true;
            } else {
               if (log.isDebugEnabled()) {
                  log.debug("Can't create multicastSession even though ClusterServices are available");
               }

               return false;
            }
         } else {
            if (log.isDebugEnabled()) {
               log.debug("Can't create multicastSession because ClusterServices are unavailable");
            }

            return false;
         }
      }
   }

   public boolean runtimeIsLocked(String var1) {
      if (this.lockout_enabled && master_invalid_login.size() != 0) {
         this.setTimestampOfCurrentCheck();
         if (master_invalid_login.containsKey(var1)) {
            InvalidLogin var2 = (InvalidLogin)master_invalid_login.get(var1);
            long var3 = var2.getLockedTimestamp();
            if (var3 != 0L) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }
}
