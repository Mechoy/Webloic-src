package weblogic.management.provider.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.util.Date;
import java.util.LinkedList;
import java.util.Properties;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.DomainDir;
import weblogic.management.ManagementLogger;
import weblogic.management.provider.EditWaitTimedOutException;
import weblogic.security.SubjectUtils;
import weblogic.security.WLSPrincipals;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.internal.SerializedSystemIni;
import weblogic.security.internal.encryption.ClearOrEncryptedService;
import weblogic.security.internal.encryption.EncryptionServiceException;
import weblogic.security.service.ContextHandler;
import weblogic.security.service.PrincipalAuthenticator;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public class EditLockManager {
   private static final String EDIT_LOCK_FILENAME = "edit.lok";
   private static final String OWNER = "owner";
   private static final String ACQUIRED = "acquired";
   private static final String EXPIRES = "expires";
   private static final String EXCLUSIVE = "exclusive";
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugConfigurationEdit");
   private static final AuthenticatedSubject kernelIdentity = obtainKernelIdentity();
   private Object owner;
   private long lockAcquisitionTime;
   private long lockExpirationTime;
   private boolean lockExclusiveFlag;
   private LinkedList waiters = new LinkedList();
   private ClearOrEncryptedService encryptionService = null;

   EditLockManager() {
      FileInputStream var1 = null;

      try {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Getting encryption service");
         }

         this.encryptionService = new ClearOrEncryptedService(SerializedSystemIni.getEncryptionService());
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Reading edit lock info from lock file");
         }

         Properties var2 = new Properties();
         var1 = new FileInputStream(this.getEditLockFilename());
         var2.load(var1);
         String var3 = var2.getProperty("owner");
         String var4 = this.decryptUser(var3);
         if (WLSPrincipals.isKernelUsername(var4)) {
            this.owner = kernelIdentity;
         } else if (WLSPrincipals.isAnonymousUsername(var4)) {
            this.owner = SubjectUtils.getAnonymousSubject();
         } else if (var4 != null) {
            PrincipalAuthenticator var5 = (PrincipalAuthenticator)SecurityServiceManager.getSecurityService(kernelIdentity, SecurityServiceManager.getDefaultRealmName(), ServiceType.AUTHENTICATION);
            this.owner = var5.impersonateIdentity(var4, (ContextHandler)null);
         }

         String var21 = var2.getProperty("expires");
         if (var21 != null) {
            this.lockExpirationTime = Long.parseLong(var21);
         }

         String var6 = var2.getProperty("acquired");
         if (var6 != null) {
            this.lockAcquisitionTime = Long.parseLong(var6);
         }

         String var7 = var2.getProperty("exclusive");
         if (var7 != null) {
            this.lockExclusiveFlag = Boolean.valueOf(var7);
         }
      } catch (FileNotFoundException var18) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Edit lock file was not found, ignoring");
         }
      } catch (Exception var19) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception occurred reading edit lock file", var19);
         }

         ManagementLogger.logReadEditLockFileFailed(var19);
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var17) {
            }
         }

      }

      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Created edit lock manager " + this);
      }

   }

   synchronized boolean getEditLock(Object var1, int var2, int var3, boolean var4) throws EditWaitTimedOutException {
      boolean var5 = false;
      Lock var6 = null;
      long var7 = -1L;
      long var9 = -1L;
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Attempting to get edit lock for owner " + var1);
      }

      if (var1 == null) {
         throw new IllegalArgumentException("owner can not be null");
      } else {
         if (var2 != -1) {
            var9 = System.currentTimeMillis() + (long)var2;
         }

         if (var3 != -1) {
            var7 = (long)var3;
         }

         if (this.owner == null) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("No current owner - getting lock");
            }

            this.setOwner(var1);
            this.setExpirationTime(var7);
            this.setExclusive(var4);
            this.persistLock();
            return var5;
         } else {
            while((!this.ownersEqual(var1, this.owner) || this.isLockExclusive() || var4) && (var6 == null || !var6.isRemoved())) {
               long var11 = System.currentTimeMillis();
               if (this.lockExpirationTime > 0L && var11 >= this.lockExpirationTime) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Getting lock - edit lock has expired at " + new Date(this.lockExpirationTime));
                  }

                  var5 = true;
                  this.clearOwner();
                  this.setOwner(var1);
                  if (var6 != null) {
                     this.waiters.remove(var6);
                  }

                  this.setExpirationTime(var7);
                  this.setExclusive(var4);
                  this.persistLock();
                  return var5;
               }

               if (var9 != -1L && var11 >= var9) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Giving up, give up time was " + new Date(var9));
                  }

                  if (var6 != null) {
                     this.waiters.remove(var6);
                  }

                  throw new EditWaitTimedOutException("Waited " + var2 + " milliseconds");
               }

               if (var6 == null) {
                  var6 = new Lock(var1, var4, var7);
                  this.waiters.add(var6);
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Adding owner to waiters list " + var1);
                  }
               }

               long var13 = -1L;
               if (this.lockExpirationTime > 0L) {
                  var13 = this.lockExpirationTime - var11;
               }

               if (var9 != -1L && (var9 < this.lockExpirationTime || this.lockExpirationTime <= 0L)) {
                  var13 = var9 - var11;
               }

               try {
                  if (var13 == -1L) {
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("Waiting until notified");
                     }

                     this.wait();
                  } else {
                     if (debugLogger.isDebugEnabled()) {
                        debugLogger.debug("Waiting for " + var13 + " milliseconds");
                     }

                     this.wait(var13);
                  }
               } catch (InterruptedException var16) {
               }
            }

            if (var6 != null && !var6.isRemoved()) {
               this.waiters.remove(var6);
            }

            this.setExpirationTime(var7);
            this.setExclusive(var4);
            return var5;
         }
      }
   }

   synchronized void releaseEditLock(Object var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("owner can not be null");
      } else if (this.owner != null) {
         if (!this.ownersEqual(this.owner, var1)) {
            throw new IllegalStateException("not owner, owner is " + this.owner);
         } else {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Release edit lock, owner is " + this.owner);
            }

            this.clearOwner();
            this.persistLock();
            if (!this.waiters.isEmpty()) {
               Lock var2 = (Lock)this.waiters.removeFirst();
               var2.setRemoved(true);
               this.setOwner(var2.getLockOwner());
               this.setExclusive(var2.isExclusive());
               this.setExpirationTime(var2.getExpirationTime());
               this.persistLock();
               this.notifyAll();
            }
         }
      }
   }

   synchronized void cancelEditLock(Object var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Canceling edit lock, setting new owner " + var1);
      }

      if (var1 == null) {
         throw new IllegalArgumentException("owner can not be null");
      } else {
         this.clearOwner();
         this.setOwner(var1);
         this.persistLock();
      }
   }

   synchronized Object getLockOwner() {
      return this.owner;
   }

   synchronized long getLockAcquisitionTime() {
      return this.lockAcquisitionTime;
   }

   synchronized boolean isLockExclusive() {
      return this.lockExclusiveFlag;
   }

   synchronized long getLockExpirationTime() {
      return this.lockExpirationTime;
   }

   synchronized boolean isLockOwner(Object var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Checking lock owner for " + var1);
         debugLogger.debug("Current owner is " + this.owner);
      }

      return this.ownersEqual(this.owner, var1);
   }

   private void setOwner(Object var1) {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Setting owner to " + var1);
      }

      this.owner = var1;
      this.lockAcquisitionTime = System.currentTimeMillis();
   }

   private void setExpirationTime(long var1) {
      if (var1 == -1L) {
         this.lockExpirationTime = 0L;
      } else {
         this.lockExpirationTime = System.currentTimeMillis() + var1;
      }

   }

   private void setExclusive(boolean var1) {
      this.lockExclusiveFlag = var1;
   }

   private void clearOwner() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Clearing owner and times");
      }

      this.owner = null;
      this.lockAcquisitionTime = 0L;
      this.lockExpirationTime = 0L;
      this.lockExclusiveFlag = false;
   }

   private void persistLock() {
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Persisting lock");
      }

      FileOutputStream var1 = null;

      try {
         Properties var2 = new Properties();
         if (this.owner != null) {
            if (this.owner instanceof AuthenticatedSubject) {
               var2.setProperty("owner", this.encryptUser(SubjectUtils.getUsername((AuthenticatedSubject)this.owner)));
            } else {
               var2.setProperty("owner", this.encryptUser(this.owner.toString()));
            }

            var2.setProperty("acquired", "" + this.lockAcquisitionTime);
            var2.setProperty("expires", "" + this.lockExpirationTime);
            var2.setProperty("exclusive", "" + this.lockExclusiveFlag);
         }

         File var3 = new File(this.getEditLockFilename());
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Writing edit lock to " + var3);
         }

         var1 = new FileOutputStream(var3);
         var2.store(var1, "");
      } catch (Exception var12) {
         ManagementLogger.logWriteEditLockFileFailed(var12);
      } finally {
         if (var1 != null) {
            try {
               var1.close();
            } catch (IOException var11) {
            }
         }

      }

   }

   private boolean ownersEqual(Object var1, Object var2) {
      if (var1 != null && var2 != null) {
         return var1 instanceof AuthenticatedSubject && var2 instanceof AuthenticatedSubject ? SubjectUtils.getUsername((AuthenticatedSubject)var1).equals(SubjectUtils.getUsername((AuthenticatedSubject)var2)) : var1.equals(var2);
      } else {
         return var1 == var2;
      }
   }

   private String encryptUser(String var1) {
      return this.encryptionService.encrypt(var1);
   }

   private String decryptUser(String var1) {
      if (var1 == null) {
         return var1;
      } else {
         try {
            return this.encryptionService.decrypt(var1);
         } catch (EncryptionServiceException var3) {
            ManagementLogger.logEditLockPropertyDecryptionFailure(this.getEditLockFilename(), "owner", var1, var3.toString());
            return null;
         } catch (Exception var4) {
            ManagementLogger.logEditLockDecryptionFailure(this.getEditLockFilename(), var4.toString());
            return null;
         }
      }
   }

   private String getEditLockFilename() {
      return DomainDir.getPathRelativeRootDir("edit.lok");
   }

   private static AuthenticatedSubject obtainKernelIdentity() {
      AuthenticatedSubject var0 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      return var0;
   }

   private class Lock {
      private Object lockOwner;
      private boolean exclusive;
      private boolean removed;
      private long expirationTime;

      private Lock(Object var2, boolean var3, long var4) {
         this.lockOwner = var2;
         this.exclusive = var3;
         this.expirationTime = var4;
      }

      public Object getLockOwner() {
         return this.lockOwner;
      }

      public boolean isExclusive() {
         return this.exclusive;
      }

      public long getExpirationTime() {
         return this.expirationTime;
      }

      public void setRemoved(boolean var1) {
         this.removed = var1;
      }

      public boolean isRemoved() {
         return this.removed;
      }

      // $FF: synthetic method
      Lock(Object var2, boolean var3, long var4, Object var6) {
         this(var2, var3, var4);
      }
   }
}
