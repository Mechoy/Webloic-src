package weblogic.ejb.container.internal;

import javax.security.jacc.PolicyConfiguration;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb20.interfaces.PrincipalNotFoundException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.jacc.RoleMapper;
import weblogic.utils.Debug;

public final class PoolHelper {
   private SecurityHelper helper;
   private String name;
   private String pid;
   private String src;
   private PolicyConfiguration cf;
   private RoleMapper rm;

   public PoolHelper(String var1, PolicyConfiguration var2, String var3, String var4, RoleMapper var5) {
      this.name = var1;
      this.pid = var3;
      this.src = var4;
      this.cf = var2;
      this.rm = var5;
   }

   public AuthenticatedSubject getFileDesc(String var1) throws PrincipalNotFoundException {
      this.initialize();
      return this.helper.getSubjectForPrincipal(var1);
   }

   public static boolean setFile(AuthenticatedSubject var0, AuthenticatedSubject var1) {
      return SecurityHelper.pushSpecificRunAsMaybe(var0, var1);
   }

   public static boolean setSegment(AuthenticatedSubject var0, AuthenticatedSubject var1) {
      return SecurityHelper.pushSpecificRunAsMaybe(var0, var1);
   }

   public static void resetFile() {
      SecurityHelper.popRunAsSubject();
   }

   public static void setDir() {
      SecurityHelper.pushCallerPrincipal();
   }

   public static void resetDir() {
      try {
         SecurityHelper.popCallerPrincipal();
      } catch (Exception var1) {
         EJBLogger.logErrorPoppingCallerPrincipal(var1);
      }

   }

   public static void setFile2(AuthenticatedSubject var0) {
      SecurityHelper.pushRunAsSubject(var0);
   }

   public static void resetFile2() {
      SecurityHelper.popRunAsSubject();
   }

   private void initialize() {
      if (this.helper == null) {
         try {
            this.helper = new SecurityHelper(this.name, this.cf, this.pid, this.src, this.rm);
         } catch (Throwable var2) {
            Debug.assertion(false, "could not create SecurityHelper: " + var2.getMessage());
         }
      }

   }
}
