package weblogic.security.internal;

import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import weblogic.security.SecurityLogger;
import weblogic.security.service.InvalidParameterException;
import weblogic.security.service.SecurityServiceException;

public class ParsePolicies {
   private static final String PERMISSION_SYNTAX = "(permission[\\s|\\S]+?)\\s*;";
   private static final String OR = "|\\s*";
   private static final String LINE_END = "\\s*;";
   private static final String GRANT_BEGIN = "^\\s*grant\\s*\\{\\s*(\\s*";
   private static final String PERMISSION_ONLY = "permission\\s+(\\S+)";
   private static final String PERMISSION_AND_TARGET = "permission\\s+(\\S+)\\s+\"([\\S&&[^,\"]]+)\"";
   private static final String PERMISSION_AND_TARGET_AND_ACTIONS = "permission\\s+(\\S+)\\s+\"([\\S||[ ]]+)\"\\s*,\\s*\"([\\S|\\s]+)\"";
   private static final String GRANT_END = "\\s*)*\\s*}\\s*;\\s*$";
   private static final String GRANT_SYNTAX = "^\\s*grant\\s*\\{\\s*(\\s*permission\\s+(\\S+)\\s*;|\\s*permission\\s+(\\S+)\\s+\"([\\S&&[^,\"]]+)\"\\s*;|\\s*permission\\s+(\\S+)\\s+\"([\\S||[ ]]+)\"\\s*,\\s*\"([\\S|\\s]+)\"\\s*;\\s*)*\\s*}\\s*;\\s*$";

   public static PermissionCollection parseGrantStatement(String var0) {
      Permissions var1 = new Permissions();
      if (!Pattern.matches("^\\s*grant\\s*\\{\\s*(\\s*permission\\s+(\\S+)\\s*;|\\s*permission\\s+(\\S+)\\s+\"([\\S&&[^,\"]]+)\"\\s*;|\\s*permission\\s+(\\S+)\\s+\"([\\S||[ ]]+)\"\\s*,\\s*\"([\\S|\\s]+)\"\\s*;\\s*)*\\s*}\\s*;\\s*$", var0)) {
         throw new InvalidParameterException(SecurityLogger.getInvalidGrantSyntax(var0));
      } else {
         Matcher var2 = Pattern.compile("(permission[\\s|\\S]+?)\\s*;").matcher(var0);

         while(var2.find()) {
            String var3 = var2.group(1);
            Permission var4 = null;

            try {
               var4 = parsePermission(var3);
            } catch (SecurityServiceException var6) {
               throw new InvalidParameterException(var6);
            }

            if (var4 != null) {
               var1.add(var4);
            }
         }

         return var1;
      }
   }

   private static Permission parsePermission(String var0) throws SecurityServiceException {
      Matcher var1 = null;
      String var2 = null;
      Permission var3 = null;
      if (Pattern.matches("permission\\s+(\\S+)", var0)) {
         var2 = "permission\\s+(\\S+)";
      } else if (Pattern.matches("permission\\s+(\\S+)\\s+\"([\\S&&[^,\"]]+)\"", var0)) {
         var2 = "permission\\s+(\\S+)\\s+\"([\\S&&[^,\"]]+)\"";
      } else if (Pattern.matches("permission\\s+(\\S+)\\s+\"([\\S||[ ]]+)\"\\s*,\\s*\"([\\S|\\s]+)\"", var0)) {
         var2 = "permission\\s+(\\S+)\\s+\"([\\S||[ ]]+)\"\\s*,\\s*\"([\\S|\\s]+)\"";
      }

      if (var2 == null) {
         throw new InvalidParameterException(SecurityLogger.getInvalidPermissionSyntax(var0));
      } else {
         var1 = Pattern.compile(var2).matcher(var0);
         if (var1.find()) {
            int var4 = var1.groupCount();
            String var5 = null;
            String var6 = null;
            String var7 = null;
            switch (var4) {
               case 3:
                  var5 = var1.group(3);
               case 2:
                  var6 = var1.group(2);
               case 1:
                  var7 = var1.group(1);
               default:
                  var3 = MakePermission.makePermission(var7, var6, var5);
            }
         }

         return var3;
      }
   }
}
