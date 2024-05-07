package weblogic.wsee.tools.jws.decl;

import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import java.util.ArrayList;
import java.util.HashMap;
import weblogic.jws.security.RolesAllowed;
import weblogic.jws.security.RolesReferenced;
import weblogic.jws.security.RunAs;
import weblogic.jws.security.UserDataConstraint;
import weblogic.wsee.util.JamUtil;

public class WebServiceSecurityDecl {
   private boolean delegate;
   private ArrayList<String> roles = new ArrayList();
   private HashMap<String, String[]> rolesMap = new HashMap();
   private boolean securityRolesDefined = false;
   private HashMap<String, String> roleRefsMap = new HashMap();
   private UserDataConstraint.Transport transport;
   private boolean userDataConstraintDefined = false;
   private boolean isRunAsEnabled = false;
   private String runAsRole;
   private String runAsPrincipal;

   WebServiceSecurityDecl(JClass var1) {
      this.processUserDataConstraint(var1);
      this.setDelegate(var1);
      if (!this.delegate) {
         this.processSecurityRoles(var1);
         this.processSecurityRoleRefs(var1);
         this.processRunAs(var1);
      }
   }

   private void setDelegate(JClass var1) {
      this.delegate = var1.getAnnotation(RolesReferenced.class) == null && var1.getAnnotation(RunAs.class) == null;
      this.securityRolesDefined = var1.getAnnotation(RolesAllowed.class) != null;
   }

   private void processUserDataConstraint(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(UserDataConstraint.class);
      if (var2 != null) {
         this.transport = (UserDataConstraint.Transport)JamUtil.getAnnotationEnumValue(var2, "transport", UserDataConstraint.Transport.class, UserDataConstraint.Transport.NONE);
         this.userDataConstraintDefined = true;
      }

   }

   private void processSecurityRoleRefs(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(RolesReferenced.class);
      if (var2 != null) {
         JAnnotationValue[] var3 = var2.getValues();
         if (var3 != null) {
            JAnnotation[] var4 = var3[0].asAnnotationArray();
            processSecurityRoleRefs(var4, this.roleRefsMap, this.roles);
            this.securityRolesDefined = true;
         }
      }

   }

   private void processSecurityRoles(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(RolesAllowed.class);
      if (var2 != null) {
         JAnnotationValue[] var3 = var2.getValues();
         if (var3 != null) {
            JAnnotation[] var4 = var3[0].asAnnotationArray();
            processSecurityRoles(var4, this.roles, this.rolesMap);
            this.securityRolesDefined = true;
         }
      }

   }

   private void processRunAs(JClass var1) {
      JAnnotation var2 = var1.getAnnotation(RunAs.class);
      if (var2 != null) {
         JAnnotationValue var3 = var2.getValue("role");
         JAnnotationValue var4 = var2.getValue("mapToPrincipal");
         if (var3 != null && var4 != null) {
            this.isRunAsEnabled = true;
            this.runAsRole = var3.asString();
            this.runAsPrincipal = var4.asString();
            if (this.runAsPrincipal.length() == 0) {
               this.runAsPrincipal = this.runAsRole;
            }

            if (!this.roles.contains(this.runAsRole)) {
               this.roles.add(this.runAsRole);
            }
         }
      }

   }

   private static void processSecurityRoles(JAnnotation[] var0, ArrayList<String> var1, HashMap<String, String[]> var2) {
      JAnnotation[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         JAnnotation var6 = var3[var5];
         JAnnotationValue var7 = var6.getValue("role");
         if (var7 == null) {
            throw new IllegalArgumentException("role must be specified in SecurityRole annotation");
         }

         String var8 = var7.asString();
         JAnnotationValue var9 = var6.getValue("mapToPrincipals");
         String[] var10 = var9 == null ? null : var9.asStringArray();
         boolean var11 = false;
         if (var2.containsKey(var8)) {
            var11 = true;
         } else {
            var1.add(var8);
         }

         if (var10 != null && var10.length > 0) {
            if (var11) {
               String[] var12 = (String[])var2.get(var8);
               String[] var13 = new String[var10.length + var12.length];
               System.arraycopy(var12, 0, var13, 0, var12.length);
               System.arraycopy(var10, 0, var13, var12.length, var10.length);
               var10 = var13;
            }

            var2.put(var8, var10);
         }
      }

   }

   private static void processSecurityRoleRefs(JAnnotation[] var0, HashMap<String, String> var1, ArrayList<String> var2) {
      JAnnotation[] var3 = var0;
      int var4 = var0.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         JAnnotation var6 = var3[var5];
         JAnnotationValue var7 = var6.getValue("role");
         JAnnotationValue var8 = var6.getValue("link");
         if (var7 != null && var8 != null) {
            String var9 = var7.asString();
            String var10 = var8.asString();
            if (var10.length() == 0) {
               var10 = var9;
            }

            var1.put(var9, var10);
            if (!var2.contains(var10)) {
               var2.add(var10);
            }
         }
      }

   }

   public ArrayList<String> processSecurityRolesOnMethod(JMethod var1) {
      ArrayList var2 = new ArrayList();
      JAnnotation var3 = var1.getAnnotation(RolesAllowed.class);
      if (var3 != null) {
         JAnnotationValue[] var4 = var3.getValues();
         if (var4 != null) {
            JAnnotation[] var5 = var4[0].asAnnotationArray();
            processSecurityRoles(var5, var2, this.rolesMap);
         }
      } else {
         var2 = this.roles;
      }

      return var2;
   }

   public boolean isUserDataConstraintDefined() {
      return this.userDataConstraintDefined;
   }

   public boolean isSecurityRolesDefined() {
      return this.securityRolesDefined;
   }

   public String[] getPrincipals(String var1) {
      return (String[])this.rolesMap.get(var1);
   }

   public ArrayList<String> getSecurityRoles() {
      return this.roles;
   }

   public UserDataConstraint.Transport getTransport() {
      return this.transport;
   }

   public boolean hasSecurityRoleRefs() {
      return this.roleRefsMap.size() > 0;
   }

   public HashMap<String, String> getSecurityRoleRefs() {
      return this.roleRefsMap;
   }

   public boolean isRunAsEnabled() {
      return this.isRunAsEnabled;
   }

   public String getRunAsRole() {
      return this.runAsRole;
   }

   public String getRunAsPrincipal() {
      return this.runAsPrincipal;
   }

   public boolean delegateToPolicyRoleConsumer() {
      return this.delegate;
   }
}
