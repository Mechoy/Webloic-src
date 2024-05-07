package weblogic.security.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.StringTokenizer;
import weblogic.common.internal.VersionInfo;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.security.SecurityMessagesTextFormatter;

public class SecurityPre90UpgradeXSLTUtils {
   private static SecurityPre90UpgradeXSLTUtils This;
   private static final String SEPARATOR = "|";
   ArrayList realmInfo = new ArrayList();
   ArrayList childInfo = new ArrayList();
   private SecurityMessagesTextFormatter formatter = SecurityMessagesTextFormatter.getInstance();

   private static String trim(String var0) {
      return var0 != null ? var0.trim() : null;
   }

   private void validateObjectName(HashSet var1, String var2) {
      if (var2.length() < 1) {
         throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeMissingObjectNameProblem(), this.formatter.getSecurityPre90UpgradeNameSolution()));
      } else if (!var1.add(var2)) {
         throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeDuplicateObjectNameProblem(var2), this.formatter.getSecurityPre90UpgradeNameSolution()));
      }
   }

   private void validateObjectNames() {
      HashSet var1 = new HashSet();
      ListIterator var2 = this.realmInfo.listIterator();

      while(var2.hasNext()) {
         this.validateObjectName(var1, ((RealmInfo)var2.next()).objectName);
      }

      var2 = this.childInfo.listIterator();

      while(var2.hasNext()) {
         this.validateObjectName(var1, ((ChildInfo)var2.next()).objectName);
      }

   }

   private ChildInfo lookupChildInfo(String var1, String var2) {
      ListIterator var3 = this.childInfo.listIterator();

      ChildInfo var4;
      do {
         if (!var3.hasNext()) {
            throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeMissingRealmChildProblem(var1, var2), this.formatter.getSecurityPre90UpgradeNameSolution()));
         }

         var4 = (ChildInfo)var3.next();
      } while(!var4.objectName.equals(var2));

      return var4;
   }

   private void validateChildRefersToRealm(String var1, ChildInfo var2) {
      if (!var1.equals(var2.realmObjectName)) {
         if (var2.realmObjectName.length() >= 1) {
            throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeRealmChildRefersToAnotherRealmProblem(var1, var2.objectName, var2.realmObjectName), this.formatter.getSecurityPre90UpgradeNameSolution()));
         } else {
            throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeRealmChildRefersToNoRealmProblem(var1, var2.objectName), this.formatter.getSecurityPre90UpgradeNameSolution()));
         }
      }
   }

   private void validateRealmChildObjectName(String var1, ChildInfo var2) {
      String var3 = var1 + var2.displayName;
      if (!var3.equals(var2.objectName)) {
         throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeNonStandardRealmChildObjectNameProblem(var2.objectName, var3, var2.displayName, var2.realmObjectName), this.formatter.getSecurityPre90UpgradeNameSolution()));
      }
   }

   private void validateRealmChild(String var1, String var2, String var3) {
      if (var3.length() >= 1) {
         ChildInfo var4 = this.lookupChildInfo(var1, var3);
         this.validateChildRefersToRealm(var1, var4);
         this.validateRealmChildObjectName(var1, var4);
      }
   }

   private void validateRealmChildren(String var1, String var2, String[] var3) {
      HashSet var4 = new HashSet();
      HashSet var5 = new HashSet();

      for(int var6 = 0; var3 != null && var6 < var3.length; ++var6) {
         String var7 = var3[var6];
         ChildInfo var8 = this.lookupChildInfo(var1, var7);
         if (!var4.add(var8.objectName)) {
            throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeDuplicateRealmChildReferencesProblem(var1, var2, var8.objectName), this.formatter.getSecurityPre90UpgradeNameSolution()));
         }

         if (!var5.add(var8.displayName)) {
            throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeDuplicateRealmChildDisplayNameProblem(var1, var2, var8.displayName), this.formatter.getSecurityPre90UpgradeNameSolution()));
         }

         this.validateChildRefersToRealm(var1, var8);
         this.validateRealmChildObjectName(var1, var8);
      }

   }

   private void validateCertPathBuilderAmongCertPathProviders(RealmInfo var1) {
      if (var1.certPathBuilder.length() >= 1) {
         String[] var2 = var1.certPathProviders;

         for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
            String var4 = var2[var3];
            if (var4.equals(var1.certPathBuilder)) {
               return;
            }
         }

         throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeRealmCertPathBuilderNotChildProblem(var1.certPathBuilder, var1.objectName), this.formatter.getSecurityPre90UpgradeRealmCertPathBuilderNotChildSolution()));
      }
   }

   private void validateRealms() {
      String var1 = null;
      HashSet var2 = new HashSet();
      ListIterator var3 = this.realmInfo.listIterator();

      RealmInfo var4;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var4 = (RealmInfo)var3.next();
         if (var4.isDefault) {
            if (var1 != null) {
               throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeMultipleDefaultRealmsProblem(var1, var4.objectName), this.formatter.getSecurityPre90UpgradeMultipleDefaultRealmsSolution()));
            }

            var1 = var4.objectName;
         }

         if (!var2.add(var4.displayName)) {
            throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeDuplicateRealmDisplayNameProblem(var4.objectName), this.formatter.getSecurityPre90UpgradeNameSolution()));
         }

         String var5 = "Security:Name=" + var4.displayName;
         if (!var5.equals(var4.objectName)) {
            throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeNonStandardRealmObjectNameProblem(var4.objectName, var5, var4.displayName), this.formatter.getSecurityPre90UpgradeNameSolution()));
         }

         this.validateRealmChildren(var4.objectName, "Auditor", var4.auditors);
         this.validateRealmChildren(var4.objectName, "AuthenticationProvider", var4.authenticationProviders);
         this.validateRealmChildren(var4.objectName, "RoleMapper", var4.roleMappers);
         this.validateRealmChildren(var4.objectName, "Authorizer", var4.authorizers);
         this.validateRealmChildren(var4.objectName, "CredentialMapper", var4.credentialMappers);
         this.validateRealmChildren(var4.objectName, "CertPathProvider", var4.certPathProviders);
         this.validateRealmChildren(var4.objectName, "KeyStore", var4.keyStores);
         this.validateRealmChild(var4.objectName, "Adjudicator", var4.adjudicator);
         this.validateRealmChild(var4.objectName, "UserLockoutManager", var4.userLockoutManager);
         this.validateCertPathBuilderAmongCertPathProviders(var4);
         if (var4.userLockoutManager.length() < 1) {
            throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeMissingUserLockoutManagerProblem(var4.objectName), this.formatter.getSecurityPre90UpgradeMissingUserLockoutManagerSolution()));
         }
      } while(!var4.isUseDeprecatedWebResource);

      throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeUseDeprecatedWebResourceProblem(var4.objectName), this.formatter.getSecurityPre90UpgradeUseDeprecatedWebResourceSolution()));
   }

   private void addRealmChildren(HashSet var1, String[] var2) {
      for(int var3 = 0; var2 != null && var3 < var2.length; ++var3) {
         var1.add(var2[var3]);
      }

   }

   private void addRealmChild(HashSet var1, String var2) {
      if (var2.length() >= 1) {
         var1.add(var2);
      }

   }

   private void addRealm(HashSet var1, RealmInfo var2) {
      this.addRealmChildren(var1, var2.auditors);
      this.addRealmChildren(var1, var2.authenticationProviders);
      this.addRealmChildren(var1, var2.roleMappers);
      this.addRealmChildren(var1, var2.authorizers);
      this.addRealmChildren(var1, var2.credentialMappers);
      this.addRealmChildren(var1, var2.certPathProviders);
      this.addRealmChildren(var1, var2.keyStores);
      this.addRealmChild(var1, var2.adjudicator);
      this.addRealmChild(var1, var2.certPathBuilder);
      this.addRealmChild(var1, var2.userLockoutManager);
   }

   private HashSet getReferencedChildren() {
      HashSet var1 = new HashSet();
      ListIterator var2 = this.realmInfo.listIterator();

      while(var2.hasNext()) {
         RealmInfo var3 = (RealmInfo)var2.next();
         this.addRealm(var1, var3);
      }

      return var1;
   }

   private void validateOrphans() {
      HashSet var1 = this.getReferencedChildren();
      new HashSet();
      ListIterator var3 = this.childInfo.listIterator();

      ChildInfo var4;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var4 = (ChildInfo)var3.next();
      } while(var1.contains(var4.objectName));

      throw new ValidationException(this.formatter.getSecurityPre90UpgradeNotPossible(this.formatter.getSecurityPre90UpgradeUnreferencedRealmChildProblemProblem(var4.displayName), this.formatter.getSecurityPre90UpgradeNameSolution()));
   }

   void validate() {
      this.validateObjectNames();
      this.validateRealms();
      this.validateOrphans();
   }

   public static void startValidation() {
      This = new SecurityPre90UpgradeXSLTUtils();
   }

   public static void validateRealm(String var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, String var9, String var10, String var11, String var12, String var13, String var14) {
      RealmInfo var15 = new RealmInfo();
      var15.objectName = getObjectName(var0);
      var15.displayName = getDisplayName(var1, var2);
      var15.isDefault = getBooleanAttribute(var3);
      var15.auditors = getObjectNames(var4);
      var15.authenticationProviders = getObjectNames(var5);
      var15.roleMappers = getObjectNames(var6);
      var15.authorizers = getObjectNames(var7);
      var15.credentialMappers = getObjectNames(var9);
      var15.certPathProviders = getObjectNames(var10);
      var15.keyStores = getObjectNames(var12);
      var15.adjudicator = getObjectName(var8);
      var15.certPathBuilder = getObjectName(var11);
      var15.userLockoutManager = getObjectName(var13);
      var15.isUseDeprecatedWebResource = getBooleanAttribute(var14);
      This.realmInfo.add(var15);
   }

   public static void validateProviderOrULock(String var0, String var1, String var2, String var3) {
      ChildInfo var4 = new ChildInfo();
      var4.objectName = getObjectName(var0);
      var4.displayName = getDisplayName(var1, var2);
      var4.realmObjectName = getObjectName(var3);
      This.childInfo.add(var4);
   }

   public static void endValidation() {
      This.validate();
   }

   static boolean getBooleanAttribute(String var0) {
      return Boolean.valueOf(var0);
   }

   static String getBooleanAttributeAsString(String var0) {
      return getBooleanAttribute(var0) ? "true" : "false";
   }

   public static String getIsDefaultRealm(String var0) {
      return getBooleanAttributeAsString(var0);
   }

   public static String getObjectName(String var0) {
      return var0 != null ? var0.trim() : "";
   }

   private static String[] getObjectNames(String var0) {
      ArrayList var1 = new ArrayList();
      StringTokenizer var2 = new StringTokenizer(var0, "|");

      while(var2.hasMoreTokens()) {
         String var3 = var2.nextToken();
         String var4 = getObjectName(var3);
         if (var4.length() >= 1) {
            var1.add(var4);
         }
      }

      return (String[])((String[])var1.toArray(new String[var1.size()]));
   }

   public static String getFirstObjectName(String var0) {
      String[] var1 = getObjectNames(var0);
      return var1 != null && var1.length > 0 ? var1[0] : null;
   }

   public static String getRestOfObjectNames(String var0) {
      String var1 = null;
      String[] var2 = getObjectNames(var0);

      for(int var3 = 1; var2 != null && var3 < var2.length; ++var3) {
         if (var1 == null) {
            var1 = var2[var3];
         } else {
            var1 = var1 + "|" + var2[var3];
         }
      }

      return var1;
   }

   public static String getDisplayName(String var0, String var1) {
      if (var0 != null && var0.length() > 0) {
         return var0;
      } else if (var1 != null && var1.length() >= 1) {
         int var2 = var1.lastIndexOf(".");
         return var2 == -1 ? var1 : var1.substring(var2 + 1, var1.length());
      } else {
         throw new AssertionError("type is null or empty");
      }
   }

   public static String beforeVersion(String var0, int var1) {
      if (var0 != null && var0.length() >= 1) {
         VersionInfo var2 = VersionInfoFactory.getVersionInfo();
         VersionInfo var3 = new VersionInfo(var1 - 1, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
         VersionInfo var4 = new VersionInfo(var0);
         return var4.laterThan(var3) ? "false" : "true";
      } else {
         throw new AssertionError("SecurityPre90UpgradeXSLTUtils.beforeVersion was passed an empty or null domain version.");
      }
   }

   public static String getCompatibilityMode(String var0, String var1, String var2, String var3, int var4) {
      boolean var5 = Boolean.valueOf(var1);
      boolean var6 = Boolean.valueOf(var2);
      boolean var7 = var3 != null && var3.length() > 0;
      if (var4 <= 0 && !var5) {
         return !var7 && !"true".equals(beforeVersion(var0, 7)) ? "false" : "true";
      } else {
         return var6 ? "true" : "false";
      }
   }

   public static int getDefaultSecurityConfigVersion(String var0, String var1, String var2, String var3) {
      if ("true".equals(getCompatibilityMode(var0, var1, var2, var3, 0))) {
         return 6;
      } else {
         return "true".equals(beforeVersion(var0, 8)) ? 7 : 8;
      }
   }

   private class ValidationException extends RuntimeException {
      private static final long serialVersionUID = 8365130643577135804L;

      ValidationException(String var2) {
         super(var2);
      }
   }

   private static class ChildInfo {
      String objectName;
      String displayName;
      String realmObjectName;

      private ChildInfo() {
      }

      // $FF: synthetic method
      ChildInfo(Object var1) {
         this();
      }
   }

   private static class RealmInfo {
      String objectName;
      String displayName;
      boolean isDefault;
      String[] auditors;
      String[] authenticationProviders;
      String[] roleMappers;
      String[] authorizers;
      String[] credentialMappers;
      String[] certPathProviders;
      String[] keyStores;
      String adjudicator;
      String certPathBuilder;
      String userLockoutManager;
      boolean isUseDeprecatedWebResource;

      private RealmInfo() {
      }

      // $FF: synthetic method
      RealmInfo(Object var1) {
         this();
      }
   }
}
