package weblogic.wsee.tools.jws.ejb;

import com.bea.util.jam.JAnnotatedElement;
import com.bea.util.jam.JAnnotation;
import com.bea.util.jam.JAnnotationValue;
import com.bea.util.jam.JClass;
import com.bea.util.jam.JMethod;
import com.bea.util.jam.mutable.MAnnotatedElement;
import com.bea.util.jam.mutable.MAnnotation;
import com.bea.wls.ejbgen.EJBGen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import weblogic.ejbgen.Constants;
import weblogic.ejbgen.RoleMapping;
import weblogic.ejbgen.RoleMappings;
import weblogic.ejbgen.SecurityRoleRef;
import weblogic.ejbgen.SecurityRoleRefs;
import weblogic.ejbgen.ServiceEndpointMethod;
import weblogic.ejbgen.Session;
import weblogic.ejbgen.Constants.Bool;
import weblogic.ejbgen.Constants.TransactionAttribute;
import weblogic.jws.Transactional;
import weblogic.jws.security.SecurityRoles;
import weblogic.utils.StringUtils;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSecurityDecl;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.util.JamUtil;

public class EjbGenInvoker {
   private Set<String> rolesToMapSet = new HashSet();

   public void process(ModuleInfo var1) throws WsBuildException {
      String[] var2 = new String[]{"-d", var1.getOutputDir().getAbsolutePath(), "-ignorePackage", "-wls9", "-source", "1.5"};
      var1.getJwsBuildContext().getLogger().log(EventLevel.VERBOSE, "Going to call ejbgen with args " + this.toString(var2));
      JClass[] var3 = new JClass[var1.getWebServices().size()];
      int var4 = 0;

      WebServiceDecl var6;
      for(Iterator var5 = var1.getWebServices().iterator(); var5.hasNext(); var3[var4++] = var6.getJClass()) {
         var6 = (WebServiceDecl)var5.next();

         assert var6 instanceof WebServiceSEIDecl : "Only JAX-RPC with EJB2 is supported";

         this.preprocess((WebServiceSEIDecl)var6);
      }

      try {
         EJBGen.main(var2, var3);
      } catch (Error var7) {
         throw new WsBuildException("Failed to run EJB Gen tool using the following args " + this.toString(var2) + " due to -- ", var7);
      }
   }

   private void preprocess(WebServiceSEIDecl var1) {
      this.mapRunAs(var1);
      this.mapTransaction(var1);
      if (!this.processRolesAllowed(var1)) {
         this.mapRoles(var1);
         this.createWLRoleMappings(var1.getJClass());
      }

   }

   private boolean processRolesAllowed(WebServiceSEIDecl var1) {
      boolean var2 = false;
      WebServiceSecurityDecl var3 = var1.getWebServiceSecurityDecl();
      if (var3.delegateToPolicyRoleConsumer()) {
         return false;
      } else {
         JClass var4 = var1.getJClass();
         Iterator var5 = var1.getWebMethods();
         ArrayList var6 = new ArrayList(var3.getSecurityRoles());

         String var11;
         while(var5.hasNext()) {
            WebMethodDecl var7 = (WebMethodDecl)var5.next();
            JMethod var8 = var7.getJMethod();
            ArrayList var9 = var3.processSecurityRolesOnMethod(var8);
            if (var9.size() > 0) {
               MAnnotation var10 = (MAnnotation)var8.getAnnotation(ServiceEndpointMethod.class);
               var11 = StringUtils.join((String[])var9.toArray(new String[0]), ",");
               var10.setSimpleValue("roles", var11, var4.forName(String.class.getName()));
               if (var9 != var3.getSecurityRoles()) {
                  addRoles(var6, var9);
               }

               var2 = true;
            }
         }

         if (var2) {
            MAnnotation var14 = ((MAnnotatedElement)var4).addLiteralAnnotation(RoleMappings.class.getName());
            MAnnotation[] var16 = var14.createNestedValueArray("value", RoleMapping.class.getName(), var6.size());
            int var18 = 0;

            for(Iterator var20 = var6.iterator(); var20.hasNext(); ++var18) {
               var11 = (String)var20.next();
               var16[var18].setSimpleValue("roleName", var11, var4.forName(String.class.getName()));
               String[] var12 = var3.getPrincipals(var11);
               if (var12 == null) {
                  var16[var18].setSimpleValue("externallyDefined", Bool.TRUE, var4.forName(Constants.Bool.class.getName()));
               } else {
                  var16[var18].setSimpleValue("principals", StringUtils.join(var12, ","), var4.forName(String.class.getName()));
               }
            }
         }

         if (var3.hasSecurityRoleRefs()) {
            HashMap var15 = var3.getSecurityRoleRefs();
            MAnnotation var17 = ((MAnnotatedElement)var4).addLiteralAnnotation(SecurityRoleRefs.class.getName());
            MAnnotation[] var19 = var17.createNestedValueArray("value", SecurityRoleRef.class.getName(), var15.size());
            int var21 = 0;
            Set var22 = var15.entrySet();

            for(Iterator var23 = var22.iterator(); var23.hasNext(); ++var21) {
               Map.Entry var13 = (Map.Entry)var23.next();
               var19[var21].setSimpleValue("roleName", var13.getKey(), var4.forName(String.class.getName()));
               var19[var21].setSimpleValue("roleLink", var13.getValue(), var4.forName(String.class.getName()));
            }

            var2 = true;
         }

         return var2;
      }
   }

   private static void addRoles(ArrayList<String> var0, ArrayList<String> var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!var0.contains(var4)) {
            var2.add(var4);
         }
      }

      var0.addAll(var2);
   }

   private void createWLRoleMappings(JClass var1) {
      MAnnotation var2 = ((MAnnotatedElement)var1).addLiteralAnnotation(RoleMappings.class.getName());
      MAnnotation[] var3 = var2.createNestedValueArray("value", RoleMapping.class.getName(), this.rolesToMapSet.size());
      int var4 = 0;

      for(Iterator var5 = this.rolesToMapSet.iterator(); var5.hasNext(); ++var4) {
         String var6 = (String)var5.next();
         var3[var4].setSimpleValue("roleName", var6, var1.forName(String.class.getName()));
         var3[var4].setSimpleValue("externallyDefined", Bool.TRUE, var1.forName(Constants.Bool.class.getName()));
      }

   }

   private void mapRoles(WebServiceSEIDecl var1) {
      HashSet var2 = new HashSet();
      HashSet var3 = new HashSet();
      JAnnotation var4 = var1.getJClass().getAnnotation(SecurityRoles.class);
      if (var4 != null) {
         JAnnotationValue var5 = var4.getValue("rolesAllowed");
         if (var5 != null) {
            String[] var6 = var5.asStringArray();

            for(int var7 = 0; var7 < var6.length; ++var7) {
               var2.add(var6[var7]);
            }
         }

         JAnnotationValue var15 = var4.getValue("rolesReferenced");
         if (var15 != null) {
            String[] var17 = var15.asStringArray();

            for(int var8 = 0; var8 < var17.length; ++var8) {
               var3.add(var17[var8]);
            }
         }
      }

      Iterator var14 = var1.getWebMethods();

      while(true) {
         JAnnotationValue var20;
         do {
            WebMethodDecl var16;
            JAnnotation var18;
            do {
               if (!var14.hasNext()) {
                  if (var3.size() > 0) {
                     this.addRefs(var1.getJClass(), var3);
                  }

                  return;
               }

               var16 = (WebMethodDecl)var14.next();
               var18 = var16.getJMethod().getAnnotation(SecurityRoles.class);
            } while(var18 == null);

            JAnnotationValue var19 = var18.getValue("rolesAllowed");
            if (var19 != null) {
               String[] var9 = var19.asStringArray();
               MAnnotation var10 = (MAnnotation)var16.getJMethod().getAnnotation(ServiceEndpointMethod.class);
               JAnnotationValue var11 = var10.getValue("roles");
               String var12 = "";
               if (var11 != null) {
                  var12 = var11.asString();
               }

               String var13 = this.mergeRoles(var2, var9, var12);
               var10.setSimpleValue("roles", var13, var1.getJClass().forName(String.class.getName()));
            }

            var20 = var18.getValue("rolesReferenced");
         } while(var20 == null);

         String[] var21 = var20.asStringArray();

         for(int var22 = 0; var22 < var21.length; ++var22) {
            var2.add(var21[var22]);
            var3.add(var21[var22]);
         }
      }
   }

   private void addRefs(JClass var1, Set<String> var2) {
      MAnnotation[] var3 = new MAnnotation[0];
      MAnnotation var4 = (MAnnotation)var1.getAnnotation(SecurityRoleRefs.class);
      if (var4 == null) {
         var4 = ((MAnnotatedElement)var1).addLiteralAnnotation(SecurityRoleRefs.class.getName());
      } else {
         JAnnotationValue var5 = var4.getValue("value");
         if (var5 != null) {
            var3 = (MAnnotation[])((MAnnotation[])var5.asAnnotationArray());
            if (var3 == null) {
               var3 = new MAnnotation[0];
            }
         }
      }

      MAnnotation[] var9 = var4.createNestedValueArray("value", SecurityRoleRef.class.getName(), var3.length + var2.size());

      int var6;
      for(var6 = 0; var6 < var3.length; ++var6) {
         var9[var6].setSimpleValue("roleName", var3[var6].getValue("roleName"), var1.forName(String.class.getName()));
         var9[var6].setSimpleValue("roleLink", var3[var6].getValue("roleLink"), var1.forName(String.class.getName()));
      }

      for(Iterator var7 = var2.iterator(); var7.hasNext(); ++var6) {
         String var8 = (String)var7.next();
         var9[var6].setSimpleValue("roleName", var8, var1.forName(String.class.getName()));
         var9[var6].setSimpleValue("roleLink", var8, var1.forName(String.class.getName()));
      }

      this.rolesToMapSet.addAll(var2);
   }

   private String mergeRoles(Set var1, String[] var2, String var3) {
      HashSet var4 = new HashSet(var1);

      for(int var5 = 0; var5 < var2.length; ++var5) {
         var4.add(var2[var5]);
      }

      if (!var3.equals("UNSPECIFIED")) {
         StringTokenizer var8 = new StringTokenizer(var3, ",");

         while(var8.hasMoreTokens()) {
            var4.add(var8.nextToken());
         }
      }

      this.rolesToMapSet.addAll(var4);
      String var9 = "UNSPECIFIED";
      if (var4.size() > 0) {
         StringBuffer var6 = new StringBuffer();
         Iterator var7 = var4.iterator();

         while(var7.hasNext()) {
            var6.append((String)var7.next());
            if (var7.hasNext()) {
               var6.append(',');
            }
         }

         var9 = var6.toString();
      }

      return var9;
   }

   private void mapTransaction(WebServiceSEIDecl var1) {
      boolean var2 = false;
      JAnnotation var3 = var1.getJClass().getAnnotation(Transactional.class);
      if (var3 != null) {
         var2 = JamUtil.getAnnotationBooleanValue(var3, "value", false);
      }

      Iterator var4 = var1.getWebMethods();

      while(var4.hasNext()) {
         WebMethodDecl var5 = (WebMethodDecl)var4.next();
         boolean var6 = var2;
         var3 = var5.getJMethod().getAnnotation(Transactional.class);
         if (var3 != null) {
            var6 = JamUtil.getAnnotationBooleanValue(var3, "value", var2);
         }

         MAnnotation var7 = this.findAnnotation(var5.getJMethod(), ServiceEndpointMethod.class);
         Constants.TransactionAttribute var8 = var6 ? TransactionAttribute.REQUIRED : TransactionAttribute.SUPPORTS;
         Constants.TransactionAttribute var9 = (Constants.TransactionAttribute)JamUtil.getAnnotationEnumValue(var7, "transactionAttribute", Constants.TransactionAttribute.class, var8);
         var7.setSimpleValue("transactionAttribute", var9, var1.getJClass().forName(Constants.TransactionAttribute.class.getName()));
      }

   }

   private MAnnotation findAnnotation(JAnnotatedElement var1, Class var2) {
      MAnnotation var3 = (MAnnotation)var1.getAnnotation(var2);
      if (var3 == null) {
         var3 = ((MAnnotatedElement)var1).addLiteralAnnotation(var2.getName());
      }

      return var3;
   }

   private void mapRunAs(WebServiceSEIDecl var1) {
      WebServiceSecurityDecl var2 = var1.getWebServiceSecurityDecl();
      if (var2.isRunAsEnabled()) {
         JClass var3 = var1.getJClass();
         MAnnotation var4 = this.findAnnotation(var3, Session.class);
         ((MAnnotation)var4).setSimpleValue("runAs", var2.getRunAsRole(), var3.forName("java.lang.String"));
         ((MAnnotation)var4).setSimpleValue("runAsIdentityPrincipal", var2.getRunAsPrincipal(), var3.forName("java.lang.String"));
      }

   }

   private String toString(String[] var1) {
      StringBuffer var2 = new StringBuffer();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2.append("([");
         var2.append(var3);
         var2.append("]");
         var2.append(var1[var3]);
         var2.append(")");
      }

      return var2.toString();
   }
}
