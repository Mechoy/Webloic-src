package weblogic.security.jacc.simpleprovider;

import java.security.Permission;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import weblogic.diagnostics.debug.DebugLogger;

class GrantGenerator {
   private static GrantGenerator SINGLETON = null;
   private static final String defaultPrincipalClassName;
   private static final String PRINCIPALCLASSNAMEPROP = "weblogic.jaccprovider.principalclass";
   private static String DEFAULTPRINCIPALCLASSNAME = "weblogic.security.jacc.simpleprovider.WLSJACCPrincipalImpl";
   private static String HEADER = "//Automatically generated\n//Do not edit!\n\n";
   private static DebugLogger jaccDebugLogger = DebugLogger.getDebugLogger("DebugSecurityJACCNonPolicy");
   private static String principalClassName = null;

   private GrantGenerator() {
   }

   protected static String generateHeader() {
      return HEADER;
   }

   protected static String generateUncheckedGrants(String var0, Permissions var1) {
      if (var1 != null && var1.elements().hasMoreElements()) {
         StringBuffer var2 = new StringBuffer("grant {\n\t//granted and unchecked resources - full access allowed\n");
         Enumeration var3 = var1.elements();

         while(var3.hasMoreElements()) {
            Permission var4 = (Permission)var3.nextElement();
            var2.append(permLine(var4));
         }

         var2.append("};\n\n");
         return var2.toString();
      } else {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("GrantGenerator:generateUncheckedGrants uncheckedPermissions is null or empty. Returning an empty String.");
         }

         return "";
      }
   }

   protected static String generateExcludedGrants(String var0, Permissions var1) {
      if (var1 != null && var1.elements().hasMoreElements()) {
         StringBuffer var2 = new StringBuffer("grant {\n\t//excluded resources\n");
         Enumeration var3 = var1.elements();

         while(var3.hasMoreElements()) {
            Permission var4 = (Permission)var3.nextElement();
            var2.append(permLine(var4));
         }

         var2.append("};\n\n");
         return var2.toString();
      } else {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("GrantGenerator:generateExcludedGrants excludedPermissions is null or empty. Returning an empty String.");
         }

         return "";
      }
   }

   private static String permLine(Permission var0) {
      StringBuffer var1 = new StringBuffer("\tpermission " + var0.getClass().getName());
      String var2 = var0.getName();
      if (var2 != null) {
         var1.append(" \"" + var2 + "\"");
      }

      String var3 = var0.getActions();
      if (var3 != null) {
         var1.append(" , \"" + var3 + "\"");
      }

      var1.append(";\n");
      return var1.toString();
   }

   protected static String generateRoleGrants(String var0, Map var1, Map var2) {
      if (var0 != null && var1 != null && !var1.isEmpty() && var2 != null && !var2.isEmpty()) {
         HashMap var3 = (HashMap)getUserToRoleMap(var1);
         if (var3.isEmpty()) {
            if (jaccDebugLogger.isDebugEnabled()) {
               jaccDebugLogger.debug("GrantGenerator:generateRoleGrants userToRoleMap is null or empty. Returning an empty String.");
            }

            return "";
         } else {
            Set var4 = var3.keySet();
            Iterator var5 = var4.iterator();
            StringBuffer var6 = new StringBuffer();

            while(true) {
               while(var5.hasNext()) {
                  String var7 = (String)var5.next();
                  if (var7 == null) {
                     if (jaccDebugLogger.isDebugEnabled()) {
                        jaccDebugLogger.debug("GrantGenerator:generateRoleGrants userName in userToRoleMap is null or empty. Ignoring and continuing.");
                     }
                  } else {
                     var6.append("grant principal " + principalClassName + " \"" + var7 + "\" {\n");
                     ArrayList var8 = (ArrayList)var3.get(var7);
                     if (var8 != null && !var8.isEmpty()) {
                        for(int var9 = 0; var9 < var8.size(); ++var9) {
                           String var10 = (String)var8.get(var9);
                           if (var10 == null) {
                              if (jaccDebugLogger.isDebugEnabled()) {
                                 jaccDebugLogger.debug("GrantGenerator:generateRoleGrants roleName in roleNameList(" + var9 + ") " + "is null. Ignoring and continuing.");
                              }
                           } else {
                              ArrayList var11 = (ArrayList)var2.get(var10);
                              if (var11 == null) {
                                 if (jaccDebugLogger.isDebugEnabled()) {
                                    jaccDebugLogger.debug("GrantGenerator:generateRoleGrants permList for roleName " + var10 + " in roleToPermissions is null. Ignoring and continuing.");
                                 }
                              } else {
                                 Iterator var12 = var11.iterator();

                                 while(var12.hasNext()) {
                                    Permission var13 = (Permission)var12.next();
                                    if (var13 == null) {
                                       if (jaccDebugLogger.isDebugEnabled()) {
                                          jaccDebugLogger.debug("GrantGenerator:generateRoleGrants perm in permList is null. Ignoring and continuing.");
                                       }
                                    } else {
                                       var6.append("\t// Mapping this permission for Role: " + var10 + "\n");
                                       var6.append(permLine(var13) + "\n");
                                    }
                                 }
                              }
                           }
                        }

                        var6.append("};\n\n");
                     } else if (jaccDebugLogger.isDebugEnabled()) {
                        jaccDebugLogger.debug("GrantGenerator:generateRoleGrants the RoleNameList for userName " + var7 + " in userToRoleMap is null or empty. Ignoring and continuing.");
                     }
                  }
               }

               return var6.toString();
            }
         }
      } else {
         if (jaccDebugLogger.isDebugEnabled()) {
            jaccDebugLogger.debug("GrantGenerator:generateRoleGrants appRolesToPrincipalNames and/or roleToPermissions is null or empty. Returning an empty String.");
         }

         return "";
      }
   }

   private static Map getUserToRoleMap(Map var0) {
      HashMap var1 = new HashMap();
      Set var2 = var0.keySet();
      Iterator var3 = var2.iterator();

      while(true) {
         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (var4 == null) {
               if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("GrantGenerator:getUserToRoleMap roleName in appRolesToPrincipalNames is null. Ignoring and continuing.");
               }
            } else {
               String[] var5 = (String[])((String[])var0.get(var4));
               if (var5 != null && var5.length != 0) {
                  for(int var6 = 0; var6 < var5.length; ++var6) {
                     String var7 = var5[var6];
                     if (var7 == null) {
                        if (jaccDebugLogger.isDebugEnabled()) {
                           jaccDebugLogger.debug("GrantGenerator:getUserToRoleMap userName in userGroupNames[" + var6 + "] " + "is null. Ignoring and continuing.");
                        }
                     } else {
                        ArrayList var8 = (ArrayList)var1.get(var7);
                        if (var8 == null) {
                           var8 = new ArrayList(1);
                        }

                        var8.add(var4);
                        if (jaccDebugLogger.isDebugEnabled()) {
                           jaccDebugLogger.debug("GrantGenerator:getUserToRoleMap for user " + var7 + " added role " + var4);
                        }

                        var1.put(var7, var8);
                     }
                  }
               } else if (jaccDebugLogger.isDebugEnabled()) {
                  jaccDebugLogger.debug("GrantGenerator:getUserToRoleMap userGroupNames for roleName " + var4 + "in appRolesToPrincipalNames " + "is null or empty. Ignoring and continuing.");
               }
            }
         }

         return var1;
      }
   }

   static {
      defaultPrincipalClassName = DEFAULTPRINCIPALCLASSNAME;
      principalClassName = System.getProperty("weblogic.jaccprovider.principalclass", defaultPrincipalClassName);
      if (jaccDebugLogger.isDebugEnabled()) {
         jaccDebugLogger.debug("GrantGenerator:PrincipalClassName: " + principalClassName);
      }

   }
}
