package weblogic.t3.srvr;

import java.security.AccessController;
import java.security.PrivilegedAction;
import weblogic.kernel.KernelStatus;
import weblogic.kernel.T3SrvrLogger;
import weblogic.management.configuration.MachineMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.configuration.UnixMachineMBean;
import weblogic.management.provider.ManagementService;
import weblogic.platform.OperatingSystem;
import weblogic.platform.Unix;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class SetUIDRendezvous {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private static final DebugCategory dbg = Debug.getCategory("weblogic.DebugSetUID");
   private static final SetUIDRendezvous singleton = new SetUIDRendezvous();
   private boolean canSwitchUsers;
   private final OperatingSystem os = OperatingSystem.getOS();
   private final String privilegedUser;
   private final String privilegedGroup;
   private final String unPrivilegedUser = getNonPrivUser();
   private final String unPrivilegedGroup = getNonPrivGroup();

   private SetUIDRendezvous() {
      String var1 = null;
      String var2 = null;
      if ((this.unPrivilegedUser != null || this.unPrivilegedGroup != null) && this.os instanceof Unix && KernelStatus.isServer()) {
         var1 = this.os.getUser();
         var2 = this.os.getGroup();
         if (var1 != null || var2 != null) {
            this.canSwitchUsers = true;
         }
      }

      this.privilegedUser = var1;
      this.privilegedGroup = var2;
   }

   public static synchronized void initialize() {
      if (dbg.isEnabled()) {
         T3SrvrLogger.logDebugSetUID("current user=" + singleton.privilegedUser + " current group=" + singleton.privilegedGroup + " target user=" + singleton.unPrivilegedUser + " target group=" + singleton.unPrivilegedGroup + " canSwithUsers=" + singleton.canSwitchUsers);
      }

      singleton.makeUnPrivileged();
   }

   public static synchronized void finish() {
      singleton.makePrivileged();
      singleton.makeUnPrivilegedFinal();
      if (dbg.isEnabled()) {
         T3SrvrLogger.logDebugSetUID("switching uid/gid done.");
      }

   }

   public static synchronized Throwable doPrivileged(PrivilegedAction var0) {
      Throwable var1;
      try {
         singleton.makePrivileged();
         if (dbg.isEnabled()) {
            T3SrvrLogger.logDebugSetUID("Running action " + var0);
         }

         var1 = (Throwable)var0.run();
      } finally {
         if (dbg.isEnabled()) {
            T3SrvrLogger.logDebugSetUID("Done action " + var0);
         }

         singleton.makeUnPrivileged();
      }

      return var1;
   }

   private void setUser(String var1) {
      if (var1 != null && var1.length() != 0) {
         try {
            this.os.setUser(var1);
         } catch (IllegalArgumentException var3) {
            T3SrvrLogger.logCantSwitchToUser(var1, var3);
            return;
         }

         T3SrvrLogger.logSwitchedToUser(var1);
      }
   }

   private void setGroup(String var1) {
      if (var1 != null && var1.length() != 0) {
         try {
            this.os.setGroup(var1);
         } catch (IllegalArgumentException var3) {
            T3SrvrLogger.logCantSwitchToGroup(var1, var3);
            return;
         }

         T3SrvrLogger.logSwitchedToGroup(var1);
      }
   }

   private void setEUser(String var1) {
      if (var1 != null && var1.length() != 0) {
         if (dbg.isEnabled()) {
            T3SrvrLogger.logDebugSetUID("Switching user to " + var1);
         }

         try {
            this.os.setEffectiveUser(var1);
         } catch (IllegalArgumentException var3) {
            T3SrvrLogger.logCantSwitchToUser(var1, var3);
            return;
         }

         if (dbg.isEnabled()) {
            T3SrvrLogger.logDebugSetUID("Switched user to " + var1);
         }

      }
   }

   private void setEGroup(String var1) {
      if (var1 != null && var1.length() != 0) {
         if (dbg.isEnabled()) {
            T3SrvrLogger.logDebugSetUID("Switching group to " + var1);
         }

         try {
            this.os.setEffectiveGroup(var1);
         } catch (IllegalArgumentException var3) {
            T3SrvrLogger.logCantSwitchToGroup(var1, var3);
            return;
         }

         if (dbg.isEnabled()) {
            T3SrvrLogger.logDebugSetUID("Switched group to " + var1);
         }

      }
   }

   private void makeUnPrivilegedFinal() {
      if (this.canSwitchUsers) {
         this.canSwitchUsers = false;
         this.setGroup(this.unPrivilegedGroup);
         this.setUser(this.unPrivilegedUser);
         this.verifyReal(this.unPrivilegedGroup, this.unPrivilegedUser);
      }
   }

   private void makeUnPrivileged() {
      if (this.canSwitchUsers) {
         this.setEGroup(this.unPrivilegedGroup);
         this.setEUser(this.unPrivilegedUser);
         this.verifyEffective(this.unPrivilegedGroup, this.unPrivilegedUser);
      }
   }

   private void makePrivileged() {
      if (this.canSwitchUsers) {
         this.setEGroup(this.privilegedGroup);
         this.setEUser(this.privilegedUser);
         this.verifyEffective(this.privilegedGroup, this.privilegedUser);
      }
   }

   private void verifyEffective(String var1, String var2) {
      String var3;
      if (var1 != null) {
         var3 = this.os.getEffectiveGroup();
         if (!var1.equals(var3)) {
            throw new AssertionError(var1 + "!=" + var3);
         }
      }

      if (var2 != null) {
         var3 = this.os.getEffectiveUser();
         if (!var2.equals(var3)) {
            throw new AssertionError(var2 + " != " + var3);
         }
      }

   }

   private void verifyReal(String var1, String var2) {
      String var3;
      if (var1 != null) {
         var3 = this.os.getGroup();
         if (!var1.equals(var3)) {
            throw new AssertionError(var1 + "!=" + var3);
         }
      }

      if (var2 != null) {
         var3 = this.os.getUser();
         if (!var2.equals(var3)) {
            throw new AssertionError(var2 + " != " + var3);
         }
      }

   }

   private static String getNonPrivUser() {
      if (!KernelStatus.isServer()) {
         return null;
      } else {
         String var0 = null;
         ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
         MachineMBean var2 = var1.getMachine();
         if (var2 != null && var2 instanceof UnixMachineMBean) {
            UnixMachineMBean var3 = (UnixMachineMBean)var2;
            if (var3.isPostBindUIDEnabled()) {
               var0 = var3.getPostBindUID();
            }
         }

         return var0;
      }
   }

   private static String getNonPrivGroup() {
      if (!KernelStatus.isServer()) {
         return null;
      } else {
         String var0 = null;
         ServerMBean var1 = ManagementService.getRuntimeAccess(kernelId).getServer();
         MachineMBean var2 = var1.getMachine();
         if (var2 != null && var2 instanceof UnixMachineMBean) {
            UnixMachineMBean var3 = (UnixMachineMBean)var2;
            if (var3.isPostBindGIDEnabled()) {
               var0 = var3.getPostBindGID();
            }
         }

         return var0;
      }
   }
}
