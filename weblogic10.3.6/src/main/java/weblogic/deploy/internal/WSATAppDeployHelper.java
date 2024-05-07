package weblogic.deploy.internal;

import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.ModuleDescriptorBean;
import weblogic.j2ee.descriptor.wl.ModuleOverrideBean;
import weblogic.j2ee.descriptor.wl.VariableAssignmentBean;
import weblogic.j2ee.descriptor.wl.VariableBean;
import weblogic.j2ee.descriptor.wl.VariableDefinitionBean;
import weblogic.management.configuration.JTAMBean;

public class WSATAppDeployHelper {
   private static final String WAR = ".war";
   private static final String BUILTIN_POLICY_HOME = "weblogic/wsee/policy/runtime/";
   private static final String POLICY_HOME = "policies/";
   private static final String WSAT_POLICY = "policy:wls_internal/wsat/wsat-registration-policy.xml";
   private static final String WSAT11_POLICY = "policy:wls_internal/wsat/wsat11-registration-policy.xml";
   private static final String WSAT_INBOUND_POLICY = "policy:wls_internal/wsat/wsat-registration-inbound-policy.xml";
   private static final String WSAT11_INBOUND_POLICY = "policy:wls_internal/wsat/wsat11-registration-inbound-policy.xml";
   private static final String WSAT_OUTBOUND_POLICY = "policy:wls_internal/wsat/wsat-registration-outbound-policy.xml";
   private static final String WSAT11_OUTBOUND_POLICY = "policy:wls_internal/wsat/wsat11-registration-outbound-policy.xml";
   private static final String WSAT_HTTPS_POLICY = "policy:wls_internal/wsat/wsat-https-policy.xml";
   private static final String WSAT_HTTPSCLIENTCERT_POLICY = "policy:wls_internal/wsat/wsat-httpsclientcert-policy.xml";
   private static final String WSAT_REGISTRATION_PORT = "RegistrationPortTypeRPCPort";
   private static final String WSAT11_REGISTRATION_PORT = "RegistrationPort";
   private static final String WSAT_REGISTRATIONREQUESTER_PORT = "RegistrationRequesterPortTypePort";
   private static final String WSAT11_REGISTRATIONREQUESTER_PORT = "RegistrationRequesterPort";
   private static final String[] PORTS = new String[]{"RegistrationPortTypeRPCPort", "RegistrationPort", "RegistrationRequesterPortTypePort", "RegistrationRequesterPort", "ParticipantPortTypePort", "CoordinatorPortTypePort", "ParticipantPort", "CoordinatorPort"};

   public static DeploymentPlanBean buildWSATAppDeploymentPlan(String var0, JTAMBean var1) {
      if (var1 == null) {
         return null;
      } else {
         boolean var2 = var1.isWSATIssuedTokenEnabled();
         String var3 = var1.getWSATTransportSecurityMode();
         return !var2 && "SSLNotRequired".equals(var3) ? null : buildWSATAppDeploymentPlan(var0, var2, var3);
      }
   }

   public static DeploymentPlanBean buildWSATAppDeploymentPlan(String var0, boolean var1, String var2) {
      EditableDescriptorManager var3 = new EditableDescriptorManager();
      DeploymentPlanBean var4 = (DeploymentPlanBean)var3.createDescriptorRoot(DeploymentPlanBean.class, "UTF-8").getRootBean();
      var4.setApplicationName(var0);
      ModuleDescriptorBean var5 = createModuleOverrideBean(var0, var4);
      VariableDefinitionBean var6 = var4.getVariableDefinition();
      if (var1) {
         createVDBean(var6, "policy:wls_internal/wsat/wsat-registration-policy.xml", "both");
         createVDBean(var6, "policy:wls_internal/wsat/wsat11-registration-policy.xml", "both");
         createVDBean(var6, "policy:wls_internal/wsat/wsat-registration-inbound-policy.xml", "inbound");
         createVDBean(var6, "policy:wls_internal/wsat/wsat-registration-outbound-policy.xml", "outbound");
         createVDBean(var6, "RegistrationRequesterPortTypePort", "inbound");
         createVDBean(var6, "policy:wls_internal/wsat/wsat11-registration-inbound-policy.xml", "inbound");
         createVDBean(var6, "policy:wls_internal/wsat/wsat11-registration-outbound-policy.xml", "outbound");
         createVDBean(var6, "RegistrationRequesterPort", "inbound");
         attachPolicyToPort(var5, "RegistrationPortTypeRPCPort", "policy:wls_internal/wsat/wsat-registration-policy.xml");
         attachPolicyToPort(var5, "RegistrationPortTypeRPCPort", "policy:wls_internal/wsat/wsat-registration-inbound-policy.xml", true);
         attachPolicyToPort(var5, "RegistrationPortTypeRPCPort", "policy:wls_internal/wsat/wsat-registration-outbound-policy.xml", true);
         attachPolicyToPort(var5, "RegistrationRequesterPortTypePort", "policy:wls_internal/wsat/wsat-registration-policy.xml");
         attachPolicyToPort(var5, "RegistrationRequesterPortTypePort", "RegistrationRequesterPortTypePort", "policy:wls_internal/wsat/wsat-registration-outbound-policy.xml", true);
         attachPolicyToPort(var5, "RegistrationPort", "policy:wls_internal/wsat/wsat11-registration-policy.xml");
         attachPolicyToPort(var5, "RegistrationPort", "policy:wls_internal/wsat/wsat11-registration-inbound-policy.xml", true);
         attachPolicyToPort(var5, "RegistrationPort", "policy:wls_internal/wsat/wsat11-registration-outbound-policy.xml", true);
         attachPolicyToPort(var5, "RegistrationRequesterPort", "policy:wls_internal/wsat/wsat11-registration-policy.xml");
         attachPolicyToPort(var5, "RegistrationRequesterPort", "RegistrationRequesterPort", "policy:wls_internal/wsat/wsat11-registration-outbound-policy.xml", true);
      }

      String[] var7;
      int var8;
      int var9;
      String var10;
      if ("SSLRequired".equals(var2)) {
         createVDBean(var6, "policy:wls_internal/wsat/wsat-https-policy.xml", "both");
         var7 = PORTS;
         var8 = var7.length;

         for(var9 = 0; var9 < var8; ++var9) {
            var10 = var7[var9];
            attachPolicyToPort(var5, var10, "policy:wls_internal/wsat/wsat-https-policy.xml");
         }
      }

      if ("ClientCertRequired".equals(var2)) {
         createVDBean(var6, "policy:wls_internal/wsat/wsat-httpsclientcert-policy.xml", "both");
         var7 = PORTS;
         var8 = var7.length;

         for(var9 = 0; var9 < var8; ++var9) {
            var10 = var7[var9];
            attachPolicyToPort(var5, var10, "policy:wls_internal/wsat/wsat-httpsclientcert-policy.xml");
         }
      }

      return var4;
   }

   private static void createVDBean(VariableDefinitionBean var0, String var1, String var2) {
      VariableBean var3 = var0.createVariable();
      var3.setName(var1);
      var3.setValue(var2);
   }

   private static ModuleDescriptorBean createModuleOverrideBean(String var0, DeploymentPlanBean var1) {
      ModuleOverrideBean var2 = var1.createModuleOverride();
      var2.setModuleName(var0 + ".war");
      var2.setModuleType("war");
      ModuleDescriptorBean var3 = var2.createModuleDescriptor();
      var3.setRootElement("webservice-policy-ref");
      var3.setUri("WEB-INF/weblogic-webservices-policy.xml");
      return var3;
   }

   private static void attachPolicyToPort(ModuleDescriptorBean var0, String var1, String var2) {
      attachPolicyToPort(var0, var1, var2, false);
   }

   private static void attachPolicyToPort(ModuleDescriptorBean var0, String var1, String var2, boolean var3) {
      attachPolicyToPort(var0, var1, var2, var2, var3);
   }

   private static void attachPolicyToPort(ModuleDescriptorBean var0, String var1, String var2, String var3, boolean var4) {
      if (!var4 || isExisted(var3)) {
         VariableAssignmentBean var5 = var0.createVariableAssignment();
         var5.setName(var2);
         var5.setXpath("/webservice-policy-ref/port-policy/[port-name=\"" + var1 + "\"]/ws-policy/[uri=\"" + var3 + "\"]/direction");
      }
   }

   private static boolean isExisted(String var0) {
      String var1 = var0.substring(7);
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      return var2.getResourceAsStream("policies/" + var1) != null || var2.getResourceAsStream("weblogic/wsee/policy/runtime/" + var1) != null;
   }
}
