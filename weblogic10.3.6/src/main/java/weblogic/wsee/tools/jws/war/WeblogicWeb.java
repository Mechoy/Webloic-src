package weblogic.wsee.tools.jws.war;

import java.util.Iterator;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.RunAsRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.SecurityRoleAssignmentBean;
import weblogic.j2ee.descriptor.wl.WeblogicWebAppBean;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.ModuleInfo;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSecurityDecl;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;

public class WeblogicWeb extends JAXRPCProcessor {
   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (!this.moduleInfo.isWsdlOnly()) {
         if (!var1.getWebService().isEjb()) {
            WeblogicWebAppBean var2 = this.getWeblogicWebAppBean(this.moduleInfo);
            WebServiceSEIDecl var3 = var1.getWebService();
            Iterator var4 = var3.getDDPorts();

            assert var4.hasNext();

            var2.setContextRoots(new String[]{((PortDecl)var4.next()).getContextPath()});
            WebServiceSecurityDecl var5 = var3.getWebServiceSecurityDecl();
            if (!var5.delegateToPolicyRoleConsumer()) {
               addSecurityRoleAssignments(var2, var5);
               addRunAsRoleAssignment(var2, var5);
            }

         }
      }
   }

   private WeblogicWebAppBean getWeblogicWebAppBean(ModuleInfo var1) {
      WeblogicWebAppBean var2 = var1.getWeblogicWebAppBean();
      if (var2 == null) {
         EditableDescriptorManager var3 = new EditableDescriptorManager();
         var2 = (WeblogicWebAppBean)var3.createDescriptorRoot(WeblogicWebAppBean.class).getRootBean();
         var1.setWeblogicWebAppBean(var2);
      }

      return var2;
   }

   private static void addSecurityRoleAssignments(WeblogicWebAppBean var0, WebServiceSecurityDecl var1) {
      if (var1.isSecurityRolesDefined()) {
         Iterator var2 = var1.getSecurityRoles().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (needToCreateSecurityRoleAssignments(var0, var3)) {
               String[] var4 = var1.getPrincipals(var3);
               SecurityRoleAssignmentBean var5 = var0.createSecurityRoleAssignment();
               var5.setRoleName(var3);
               if (var4 == null) {
                  var5.createExternallyDefined();
               } else {
                  var5.setPrincipalNames(var4);
               }
            }
         }
      }

   }

   private static boolean needToCreateSecurityRoleAssignments(WeblogicWebAppBean var0, String var1) {
      SecurityRoleAssignmentBean[] var2 = var0.getSecurityRoleAssignments();
      SecurityRoleAssignmentBean[] var3 = var2;
      int var4 = var2.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         SecurityRoleAssignmentBean var6 = var3[var5];
         if (var6.getRoleName().equals(var1)) {
            return false;
         }
      }

      return true;
   }

   private static void addRunAsRoleAssignment(WeblogicWebAppBean var0, WebServiceSecurityDecl var1) {
      if (var1.isRunAsEnabled()) {
         RunAsRoleAssignmentBean var2 = var0.createRunAsRoleAssignment();
         var2.setRoleName(var1.getRunAsRole());
         var2.setRunAsPrincipalName(var1.getRunAsPrincipal());
      }

   }
}
