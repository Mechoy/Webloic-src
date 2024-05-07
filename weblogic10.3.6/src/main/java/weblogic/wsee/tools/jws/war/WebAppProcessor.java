package weblogic.wsee.tools.jws.war;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.AuthConstraintBean;
import weblogic.j2ee.descriptor.LoginConfigBean;
import weblogic.j2ee.descriptor.RunAsBean;
import weblogic.j2ee.descriptor.SecurityConstraintBean;
import weblogic.j2ee.descriptor.SecurityRoleBean;
import weblogic.j2ee.descriptor.SecurityRoleRefBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.ServletMappingBean;
import weblogic.j2ee.descriptor.UserDataConstraintBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebResourceCollectionBean;
import weblogic.jws.security.UserDataConstraint;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.JWSProcessor;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.WebServiceInfo;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSecurityDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;

public class WebAppProcessor implements JWSProcessor {
   private ModuleInfo moduleInfo = null;

   public void init(ModuleInfo var1) throws WsBuildException {
      this.moduleInfo = var1;
   }

   public void finish() throws WsBuildException {
   }

   public void process(WebServiceInfo var1) throws WsBuildException {
      if (!this.moduleInfo.isWsdlOnly()) {
         if (!var1.getWebService().isEjb()) {
            WebAppBean var2 = this.getWebAppBean(this.moduleInfo);
            WebServiceDecl var3 = var1.getWebService();
            String[] var4 = var2.getDisplayNames();
            if (var4 == null || var4.length == 0) {
               String[] var5 = new String[]{var3.getArtifactName() + "WebApp"};
               var2.setDisplayNames(var5);
            }

            ArrayList var9 = new ArrayList();
            Iterator var6 = var3.getDDPorts();

            while(var6.hasNext()) {
               PortDecl var7 = (PortDecl)var6.next();
               ServletBean var8 = createServlet(var2, var3, var7);
               createServletMappings(var2, var7, var8.getServletName());
               var9.add(var8);
            }

            if (var3 instanceof WebServiceSEIDecl) {
               WebServiceSEIDecl var10 = (WebServiceSEIDecl)var3;
               WebServiceSecurityDecl var11 = var10.getWebServiceSecurityDecl();
               if (var11.isSecurityRolesDefined()) {
                  createBasicAuth(var2);
                  createSecurityConstraints(var2, var10, var11);
                  createSecurityRoles(var2, var11);
                  createSecurityRoleRefs(var11, var9);
               } else if (var11.isUserDataConstraintDefined()) {
                  createSecurityConstraints(var2, var10, var11);
               }

               createRunAs(var2, var9, var11);
            }

         }
      }
   }

   private WebAppBean getWebAppBean(ModuleInfo var1) {
      WebAppBean var2 = var1.getWebAppBean();
      if (var2 == null) {
         EditableDescriptorManager var3 = new EditableDescriptorManager();
         var2 = (WebAppBean)var3.createDescriptorRoot(WebAppBean.class).getRootBean();
         var2.setVersion("2.5");
         var1.setWebAppBean(var2);
      }

      return var2;
   }

   private static ServletBean createServlet(WebAppBean var0, WebServiceDecl var1, PortDecl var2) {
      ServletBean var3 = var0.createServlet();
      var3.setServletName(var1.getDeployedName() + var2.getProtocol());
      var3.setServletClass(var1.getJClass().getQualifiedName());
      var3.setLoadOnStartup("0");
      return var3;
   }

   private static ServletMappingBean createServletMappings(WebAppBean var0, PortDecl var1, String var2) {
      ServletMappingBean var3 = var0.createServletMapping();
      var3.setServletName(var2);
      var3.setUrlPatterns(new String[]{var1.getServiceUri()});
      return var3;
   }

   private static void createBasicAuth(WebAppBean var0) {
      LoginConfigBean[] var1 = var0.getLoginConfigs();
      if (var1 == null || var1.length <= 0) {
         LoginConfigBean var2 = var0.createLoginConfig();
         var2.setAuthMethod("BASIC");
         var2.setRealmName("default");
      }
   }

   private static void createSecurityRoles(WebAppBean var0, WebServiceSecurityDecl var1) {
      Iterator var2 = var1.getSecurityRoles().iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         if (!hasRoleDefined(var0, var3)) {
            var0.createSecurityRole().setRoleName(var3);
         }
      }

   }

   private static boolean hasRoleDefined(WebAppBean var0, String var1) {
      SecurityRoleBean[] var2 = var0.getSecurityRoles();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         SecurityRoleBean var5 = var2[var4];
         if (var1.equals(var5.getRoleName())) {
            return true;
         }
      }

      return false;
   }

   private static void createSecurityRoleRefs(WebServiceSecurityDecl var0, List<ServletBean> var1) {
      Iterator var2 = var1.iterator();

      while(true) {
         ServletBean var3;
         do {
            if (!var2.hasNext()) {
               return;
            }

            var3 = (ServletBean)var2.next();
         } while(!var0.hasSecurityRoleRefs());

         HashMap var4 = var0.getSecurityRoleRefs();
         Iterator var5 = var4.entrySet().iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            SecurityRoleRefBean var7 = var3.createSecurityRoleRef();
            var7.setRoleName((String)var6.getKey());
            var7.setRoleLink((String)var6.getValue());
         }
      }
   }

   private static void createSecurityConstraints(WebAppBean var0, WebServiceSEIDecl var1, WebServiceSecurityDecl var2) {
      Iterator var3 = var1.getDDPorts();
      ArrayList var4 = new ArrayList();

      while(var3.hasNext()) {
         PortDecl var5 = (PortDecl)var3.next();
         var4.add(var5.getServiceUri());
      }

      SecurityConstraintBean var10 = var0.createSecurityConstraint();
      ArrayList var6 = var2.getSecurityRoles();
      if (var6.size() > 0) {
         AuthConstraintBean var7 = var10.createAuthConstraint();
         Iterator var8 = var6.iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            var7.addRoleName(var9);
         }
      }

      WebResourceCollectionBean var11 = var10.createWebResourceCollection();
      var11.setWebResourceName(var1.getArtifactName());
      var11.setUrlPatterns((String[])var4.toArray(new String[0]));
      UserDataConstraint.Transport var12 = var2.getTransport();
      UserDataConstraintBean var13;
      if (var12 == UserDataConstraint.Transport.INTEGRAL) {
         var13 = var10.createUserDataConstraint();
         var13.setTransportGuarantee("INTEGRAL");
      } else if (var12 == UserDataConstraint.Transport.CONFIDENTIAL) {
         var13 = var10.createUserDataConstraint();
         var13.setTransportGuarantee("CONFIDENTIAL");
      }

   }

   private static void createRunAs(WebAppBean var0, List<ServletBean> var1, WebServiceSecurityDecl var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         ServletBean var4 = (ServletBean)var3.next();
         if (var2.isRunAsEnabled()) {
            RunAsBean var5 = var4.createRunAs();
            var5.setRoleName(var2.getRunAsRole());
            if (!var2.isSecurityRolesDefined()) {
               createSecurityRoles(var0, var2);
            }
         }
      }

   }
}
