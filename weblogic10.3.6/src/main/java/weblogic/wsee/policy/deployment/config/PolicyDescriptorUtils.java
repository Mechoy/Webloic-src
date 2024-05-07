package weblogic.wsee.policy.deployment.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.enterprise.deploy.model.exceptions.DDBeanCreateException;
import javax.enterprise.deploy.spi.TargetModuleID;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceProvider;
import weblogic.deploy.api.model.WebLogicDeployableObject;
import weblogic.deploy.api.spi.DeploymentOptions;
import weblogic.deploy.api.spi.WebLogicDeploymentConfiguration;
import weblogic.deploy.api.spi.WebLogicDeploymentManager;
import weblogic.deploy.api.tools.DeployableObjectInfo;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.ServletBean;
import weblogic.j2ee.descriptor.WebAppBean;
import weblogic.j2ee.descriptor.WebservicesBean;
import weblogic.j2ee.descriptor.wl.OperationPolicyBean;
import weblogic.j2ee.descriptor.wl.OwsmPolicyBean;
import weblogic.j2ee.descriptor.wl.OwsmSecurityPolicyBean;
import weblogic.j2ee.descriptor.wl.PortInfoBean;
import weblogic.j2ee.descriptor.wl.PortPolicyBean;
import weblogic.j2ee.descriptor.wl.PropertyNamevalueBean;
import weblogic.j2ee.descriptor.wl.ServiceReferenceDescriptionBean;
import weblogic.j2ee.descriptor.wl.WebservicePolicyRefBean;
import weblogic.j2ee.descriptor.wl.WsPolicyBean;
import weblogic.jws.Policies;
import weblogic.jws.Policy;
import weblogic.management.ManagementException;
import weblogic.management.configuration.AppDeploymentMBean;
import weblogic.management.mbeanservers.edit.ActivationTaskMBean;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.runtime.AppRuntimeStateRuntimeMBean;
import weblogic.management.runtime.DeployerRuntimeMBean;
import weblogic.management.runtime.DeploymentTaskRuntimeMBean;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.ServerRuntimeMBean;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicies;
import weblogic.wsee.jws.jaxws.owsm.SecurityPolicy;
import weblogic.wsee.policy.deployment.PolicyDeployUtils;
import weblogic.wsee.policy.deployment.WseePolicyReferenceInfo;
import weblogic.wsee.util.StringUtil;

public class PolicyDescriptorUtils {
   String host;
   String port;
   private static final String BOTH = "both";
   private static final String IN = "inbound";
   private static final String OUT = "outbound";
   private static final String ENABLED = "enabled";
   private static final String DISABLED = "disabled";
   private static final String DELETED = "deleted";
   private static final String OWSM = "owsm-security";
   private static final String WLS = "ws-policy";
   private static final Object configLock = new Object();
   private static Map<String, Lock> usedLocks = new HashMap();
   private static List<Lock> unusedLocks = new ArrayList();

   public static WseePolicyReferenceInfo[] getPolicyReferenceInfos(String var0) throws ManagementException, PolicyManagementException, ConfigurationException {
      PolicySubject var1 = parsePolicySubject(var0);
      Map var2 = null;
      Object var3 = getLock(getAppName(var1));
      synchronized(var3) {
         if (isClientType(var1)) {
            var2 = getClientPolicyReferenceInfos(var1);
         } else {
            var2 = getServicePolicyReferenceInfos(var1);
         }
      }

      releaseLock(getAppName(var1), var3);
      return (WseePolicyReferenceInfo[])var2.values().toArray(new WseePolicyReferenceInfo[var2.size()]);
   }

   private static Map<String, WseePolicyReferenceInfo> getClientPolicyReferenceInfos(PolicySubject var0) throws PolicyManagementException, ManagementException {
      if (isOperationType(var0)) {
         throw new PolicyManagementException("Operation level policy management is not supported for clients.");
      } else {
         DeploymentConfigurationHelper var1 = getDCH(getAppName(var0));
         ServiceReferenceDescriptionBean var2 = getServiceRefDescriptor(getModuleName(var0), getEJBName(var0), getServiceRefName(var0), var1);
         return getPolicies(getSubjectName(var0), var2);
      }
   }

   private static Map<String, WseePolicyReferenceInfo> getServicePolicyReferenceInfos(PolicySubject var0) throws ManagementException, PolicyManagementException, ConfigurationException {
      DeploymentConfigurationHelper var1 = getDCH(getAppName(var0));
      Class var2 = getServiceClass(getModuleName(var0), getServiceName(var0), getPortName(var0), var1);
      WebservicePolicyRefBean var3 = getPolicyDescriptor(getModuleName(var0), var1);
      return getPolicies(getSubjectName(var0), isOperationType(var0), var2, var3);
   }

   public static void setPolicyReferenceInfos(String var0, WseePolicyReferenceInfo[] var1) throws ManagementException, PolicyManagementException, ConfigurationException, IOException {
      if (var1 != null && var1.length != 0) {
         PolicySubject var2 = parsePolicySubject(var0);
         Object var3 = getLock(getAppName(var2));
         synchronized(var3) {
            DeploymentConfigurationHelper var5 = getDCH(getAppName(var2));
            if (isClientType(var2)) {
               setClientPolicyReferenceInfos(var2, var1, var5);
            } else {
               setServicePolicyReferenceInfos(var2, var1, var5);
            }

            save(getAppName(var2), var5);
         }

         releaseLock(getAppName(var2), var3);
      }
   }

   private static void setClientPolicyReferenceInfos(PolicySubject var0, WseePolicyReferenceInfo[] var1, DeploymentConfigurationHelper var2) throws PolicyManagementException, ManagementException {
      if (isOperationType(var0)) {
         throw new PolicyManagementException("Operation level policy management is not supported for clients.");
      } else {
         ServiceReferenceDescriptionBean var3 = getServiceRefDescriptor(getModuleName(var0), getEJBName(var0), getServiceRefName(var0), var2);
         Map var4 = getPolicies(getSubjectName(var0), var3);
         Iterator var5 = var4.keySet().iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            WseePolicyReferenceInfo var7 = (WseePolicyReferenceInfo)var4.get(var6);
            if (!var7.getStatus().equals("deleted")) {
               remove(var6, var4, var3, getSubjectName(var0));
            }
         }

         for(int var8 = 0; var8 < var1.length; ++var8) {
            attach(var1[var8], var4, var3, getSubjectName(var0));
         }

      }
   }

   private static void setServicePolicyReferenceInfos(PolicySubject var0, WseePolicyReferenceInfo[] var1, DeploymentConfigurationHelper var2) throws ManagementException, PolicyManagementException, ConfigurationException {
      Class var3 = getServiceClass(getModuleName(var0), getServiceName(var0), getPortName(var0), var2);
      WebservicePolicyRefBean var4 = getPolicyDescriptor(getModuleName(var0), var2);
      Map var5 = getPolicies(getSubjectName(var0), isOperationType(var0), var3, var4);
      String var6 = null;
      var6 = getServiceLink(var0, var2);
      Iterator var7 = var5.keySet().iterator();

      while(var7.hasNext()) {
         String var8 = (String)var7.next();
         WseePolicyReferenceInfo var9 = (WseePolicyReferenceInfo)var5.get(var8);
         if (!var9.getStatus().equals("deleted")) {
            remove(var8, var5, var4, var3, getSubjectName(var0), isOperationType(var0), var6);
         }
      }

      for(int var10 = 0; var10 < var1.length; ++var10) {
         attach(var1[var10], var5, getPolicies(getSubjectName(var0), isOperationType(var0), var3), var4, getSubjectName(var0), isOperationType(var0), var6);
      }

   }

   public void setPolicyReferenceInfos(String var1, WseePolicyReferenceInfo[] var2, String var3) {
   }

   public static void setPolicyReferenceInfo(String var0, WseePolicyReferenceInfo var1) throws ManagementException, PolicyManagementException, ConfigurationException, IOException {
      PolicySubject var2 = parsePolicySubject(var0);
      Object var3 = getLock(getAppName(var2));
      synchronized(var3) {
         DeploymentConfigurationHelper var5 = getDCH(getAppName(var2));
         if (isClientType(var2)) {
            removeClientPolicyReference(var1.getUri(), var2, var5);
            attachClientPolicyReference(var2, var1, var5);
         } else {
            removeServicePolicyReference(var1.getUri(), var2, var5);
            attachServicePolicyReference(var1, var2, var5);
         }

         save(getAppName(var2), var5);
      }

      releaseLock(getAppName(var2), var3);
   }

   public static void attachPolicyReference(String var0, WseePolicyReferenceInfo var1) throws ManagementException, PolicyManagementException, ConfigurationException, IOException {
      PolicySubject var2 = parsePolicySubject(var0);
      Object var3 = getLock(getAppName(var2));
      synchronized(var3) {
         DeploymentConfigurationHelper var5 = getDCH(getAppName(var2));
         if (isClientType(var2)) {
            attachClientPolicyReference(var2, var1, var5);
         } else {
            attachServicePolicyReference(var1, var2, var5);
         }

         save(getAppName(var2), var5);
      }

      releaseLock(getAppName(var2), var3);
   }

   private static void attachClientPolicyReference(PolicySubject var0, WseePolicyReferenceInfo var1, DeploymentConfigurationHelper var2) throws PolicyManagementException, ManagementException {
      if (isOperationType(var0)) {
         throw new PolicyManagementException("Operation level policy management is not supported for clients.");
      } else {
         ServiceReferenceDescriptionBean var3 = getServiceRefDescriptor(getModuleName(var0), getEJBName(var0), getServiceRefName(var0), var2);
         Map var4 = getPolicies(getSubjectName(var0), var3);
         attach(var1, var4, var3, getSubjectName(var0));
      }
   }

   private static void attachServicePolicyReference(WseePolicyReferenceInfo var0, PolicySubject var1, DeploymentConfigurationHelper var2) throws ManagementException, PolicyManagementException, ConfigurationException {
      Class var3 = getServiceClass(getModuleName(var1), getServiceName(var1), getPortName(var1), var2);
      WebservicePolicyRefBean var4 = getPolicyDescriptor(getModuleName(var1), var2);
      Map var5 = getPolicies(getSubjectName(var1), isOperationType(var1), var3, var4);
      attach(var0, var5, getPolicies(getSubjectName(var1), isOperationType(var1), var3), var4, getSubjectName(var1), isOperationType(var1), getServiceLink(var1, var2));
   }

   private static void save(String var0, DeploymentConfigurationHelper var1) throws ManagementException, IOException, ConfigurationException, PolicyManagementException {
      AppDeploymentMBean var2 = DeploymentConfigurationHelper.getAppDeploymentMBean(var0);
      ConfigurationManagerMBean var3 = MBeanUtils.getConfigurationManagerMBean();
      synchronized(configLock) {
         int var5 = !var3.haveUnactivatedChanges() ? 10000 : -1;
         MBeanUtils.getConfigurationManagerMBean().startEdit(-1, var5);
         String var6 = var1.getPlanPath();
         if (var1.isNewPlan()) {
            String var7 = var1.getSourcePath();

            File var8;
            for(var8 = new File(var7); var8.getParent() != null && noSelectableContents(var8); var8 = var8.getParentFile()) {
            }

            var6 = var8.getPath() + File.separator + "Plan.xml";
            var1.setPlanPath(var6);
         }

         var1.save();
         updateConfig(var2, var6);
      }
   }

   private static void updateConfig(AppDeploymentMBean var0, String var1) throws ManagementException, PolicyManagementException {
      redeployPlan(var0, var1);
      MBeanUtils.getConfigurationManagerMBean().save();
      boolean var2 = false;
      long var3 = var2 ? 0L : -1L;
      ActivationTaskMBean var5 = MBeanUtils.getConfigurationManagerMBean().activate(var3);
      Exception var6 = var5.getError();
      if (var6 != null) {
         throw new PolicyManagementException("Could not activate plan: " + var6.getMessage());
      }
   }

   private static boolean noSelectableContents(File var0) {
      if (var0.isDirectory() && !var0.isHidden()) {
         File[] var1 = var0.listFiles();
         if (var1 == null) {
            return true;
         }

         for(int var2 = 0; var2 < var1.length; ++var2) {
            File var3 = var1[var2];
            if (var3.isDirectory() && !var3.isHidden()) {
               return false;
            }
         }
      }

      return true;
   }

   public void attachPolicyReference(String var1, WseePolicyReferenceInfo var2, String var3) {
   }

   public static void removePolicyReference(String var0, String var1) throws ManagementException, PolicyManagementException, ConfigurationException, IOException {
      PolicySubject var2 = parsePolicySubject(var0);
      Object var3 = getLock(getAppName(var2));
      synchronized(var3) {
         DeploymentConfigurationHelper var5 = getDCH(getAppName(var2));
         if (isClientType(var2)) {
            removeClientPolicyReference(var1, var2, var5);
         } else {
            removeServicePolicyReference(var1, var2, var5);
         }

         save(getAppName(var2), var5);
      }

      releaseLock(getAppName(var2), var3);
   }

   private static void removeClientPolicyReference(String var0, PolicySubject var1, DeploymentConfigurationHelper var2) throws PolicyManagementException, ManagementException {
      if (isOperationType(var1)) {
         throw new PolicyManagementException("Operation level policy management is not supported for clients.");
      } else {
         ServiceReferenceDescriptionBean var3 = getServiceRefDescriptor(getModuleName(var1), getEJBName(var1), getServiceRefName(var1), var2);
         Map var4 = getPolicies(getSubjectName(var1), var3);
         remove(var0, var4, var3, getSubjectName(var1));
      }
   }

   private static void removeServicePolicyReference(String var0, PolicySubject var1, DeploymentConfigurationHelper var2) throws ManagementException, PolicyManagementException, ConfigurationException {
      Class var3 = getServiceClass(getModuleName(var1), getServiceName(var1), getPortName(var1), var2);
      WebservicePolicyRefBean var4 = getPolicyDescriptor(getModuleName(var1), var2);
      Map var5 = getPolicies(getSubjectName(var1), isOperationType(var1), var3, var4);
      remove(var0, var5, var4, var3, getSubjectName(var1), isOperationType(var1), getServiceLink(var1, var2));
   }

   public void removePolicyReference(String var1, String var2, String var3) {
   }

   public static String getPolicyRefStatus(String var0, String var1) throws ManagementException, PolicyManagementException, ConfigurationException {
      PolicySubject var2 = parsePolicySubject(var0);
      WseePolicyReferenceInfo var3 = null;
      Object var4 = getLock(getAppName(var2));
      synchronized(var4) {
         Map var6 = null;
         if (isClientType(var2)) {
            var6 = getClientPolicyReferenceInfos(var2);
         } else {
            var6 = getServicePolicyReferenceInfos(var2);
         }

         var3 = (WseePolicyReferenceInfo)var6.get(var1);
      }

      releaseLock(getAppName(var2), var4);
      return var3 == null ? null : var3.getStatus();
   }

   public static void setPolicyRefStatus(String var0, String var1, boolean var2) throws ManagementException, PolicyManagementException, ConfigurationException, IOException {
      PolicySubject var3 = parsePolicySubject(var0);
      Object var4 = getLock(getAppName(var3));
      synchronized(var4) {
         DeploymentConfigurationHelper var6 = getDCH(getAppName(var3));
         if (isClientType(var3)) {
            setClientPolicyRefStatus(var1, var2, var3, var6);
         } else {
            setServicePolicyRefStatus(var1, var2, var3, var6);
         }

         save(getAppName(var3), var6);
      }

      releaseLock(getAppName(var3), var4);
   }

   private static void setClientPolicyRefStatus(String var0, boolean var1, PolicySubject var2, DeploymentConfigurationHelper var3) throws PolicyManagementException, ManagementException {
      if (isOperationType(var2)) {
         throw new PolicyManagementException("Operation level policy management is not supported for clients.");
      } else {
         ServiceReferenceDescriptionBean var4 = getServiceRefDescriptor(getModuleName(var2), getEJBName(var2), getServiceRefName(var2), var3);
         Map var5 = getPolicies(getSubjectName(var2), var4);
         setStatus(var0, var5, var4, getSubjectName(var2), var1);
      }
   }

   private static void setServicePolicyRefStatus(String var0, boolean var1, PolicySubject var2, DeploymentConfigurationHelper var3) throws ManagementException, PolicyManagementException, ConfigurationException {
      Class var4 = getServiceClass(getModuleName(var2), getServiceName(var2), getPortName(var2), var3);
      WebservicePolicyRefBean var5 = getPolicyDescriptor(getModuleName(var2), var3);
      Map var6 = getPolicies(getSubjectName(var2), isOperationType(var2), var4, var5);
      setStatus(var0, var6, var5, var4, getSubjectName(var2), isOperationType(var2), getServiceLink(var2, var3), var1);
   }

   private static String getServiceLink(PolicySubject var0, DeploymentConfigurationHelper var1) throws PolicyManagementException, ManagementException {
      return isOperationType(var0) ? getServiceLink(getModuleName(var0), getServiceName(var0), getPortName(var0), var1) : null;
   }

   public void setPolicyRefStatus(String var1, String var2, boolean var3, String var4) {
   }

   public static boolean isOWSMAttachable(String var0) throws ManagementException, PolicyManagementException, ConfigurationException {
      PolicySubject var1 = parsePolicySubject(var0);
      String var2 = null;
      Object var3 = getLock(getAppName(var1));
      synchronized(var3) {
         Map var5 = null;
         if (isClientType(var1)) {
            var5 = getClientPolicyReferenceInfos(var1);
         } else {
            var5 = getServicePolicyReferenceInfos(var1);
         }

         var2 = effectiveCategory(var5);
      }

      releaseLock(getAppName(var1), var3);
      return var2 == null ? true : var2.equals("owsm-security");
   }

   private static PolicySubject parsePolicySubject(String var0) {
      return new PolicySubject(var0);
   }

   private static String getAppName(PolicySubject var0) {
      return var0.getAppName();
   }

   private static String getModuleName(PolicySubject var0) {
      return var0.getModuleName();
   }

   private static String getEJBName(PolicySubject var0) {
      return var0.getEJBName();
   }

   private static String getServiceRefName(PolicySubject var0) {
      return var0.getServiceRefName();
   }

   private static String getServiceName(PolicySubject var0) {
      return var0.getServiceName();
   }

   private static String getSubjectName(PolicySubject var0) {
      return var0.getSubjectName();
   }

   private static String getPortName(PolicySubject var0) {
      return var0.getPortName();
   }

   private static boolean isOperationType(PolicySubject var0) {
      return var0.isOperationType();
   }

   private static boolean isClientType(PolicySubject var0) {
      return var0.isClientType();
   }

   private static Map<String, WseePolicyReferenceInfo> getPolicies(String var0, boolean var1, Class var2, WebservicePolicyRefBean var3) {
      Map var4 = getPolicies(var0, var1, var2);
      int var7;
      int var8;
      if (var1) {
         OperationPolicyBean[] var5 = var3.getOperationPolicy();
         if (var5 != null) {
            OperationPolicyBean[] var6 = var5;
            var7 = var5.length;

            for(var8 = 0; var8 < var7; ++var8) {
               OperationPolicyBean var9 = var6[var8];
               if (var9.getOperationName().equals(var0)) {
                  updatePolicies(var4, var9.getWsPolicy());
               }
            }
         }
      } else {
         PortPolicyBean[] var10 = var3.getPortPolicy();
         if (var10 != null) {
            PortPolicyBean[] var11 = var10;
            var7 = var10.length;

            for(var8 = 0; var8 < var7; ++var8) {
               PortPolicyBean var12 = var11[var8];
               if (var12.getPortName().equals(var0)) {
                  updatePolicies(var4, var12.getOwsmSecurityPolicy());
                  updatePolicies(var4, var12.getWsPolicy());
               }
            }
         }
      }

      return var4;
   }

   private static Map<String, WseePolicyReferenceInfo> getPolicies(String var0, boolean var1, Class var2) {
      ArrayList var3 = null;
      if (var1) {
         Method var4 = PolicyDeployUtils.findWebMethodFromService(var2, var0);
         var3 = getPoliciesFromAnnotatedElement(var4);
      } else {
         var3 = getPoliciesFromAnnotatedElement(var2);
      }

      Map var5 = getPolicyRefInfoMap(var3);
      return var5;
   }

   private static Map<String, WseePolicyReferenceInfo> getPolicies(String var0, ServiceReferenceDescriptionBean var1) {
      HashMap var2 = new HashMap();
      if (var1 != null) {
         PortInfoBean[] var3 = var1.getPortInfos();
         if (var3 != null) {
            PortInfoBean[] var4 = var3;
            int var5 = var3.length;

            for(int var6 = 0; var6 < var5; ++var6) {
               PortInfoBean var7 = var4[var6];
               if (var7.getPortName().equals(var0)) {
                  OwsmPolicyBean[] var8 = var7.getOwsmPolicy();
                  if (var8 != null) {
                     OwsmPolicyBean[] var9 = var8;
                     int var10 = var8.length;

                     for(int var11 = 0; var11 < var10; ++var11) {
                        OwsmPolicyBean var12 = var9[var11];
                        var2.put(var12.getUri(), new WseePolicyReferenceInfo("owsm-security", var12.getUri(), var12.getStatus(), getOverrides(var12)));
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   private static Map<String, String> getOverrides(OwsmPolicyBean var0) {
      HashMap var1 = new HashMap();
      PropertyNamevalueBean[] var2 = var0.getSecurityConfigurationProperties();
      if (var2 != null) {
         PropertyNamevalueBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PropertyNamevalueBean var6 = var3[var5];
            var1.put(var6.getName(), var6.getValue());
         }
      }

      return var1;
   }

   private static Map<String, WseePolicyReferenceInfo> getPolicyRefInfoMap(ArrayList<WseePolicyReferenceInfo> var0) {
      if (var0 == null) {
         var0 = new ArrayList();
      }

      HashMap var1 = new HashMap();
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         WseePolicyReferenceInfo var3 = (WseePolicyReferenceInfo)var2.next();
         var1.put(var3.getUri(), var3);
      }

      return var1;
   }

   private static void updatePolicies(Map<String, WseePolicyReferenceInfo> var0, OwsmSecurityPolicyBean[] var1) {
      OwsmSecurityPolicyBean[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         OwsmSecurityPolicyBean var5 = var2[var4];
         if (!var0.containsKey(var5.getUri())) {
            addPolicy(var0, var5);
         } else {
            WseePolicyReferenceInfo var6 = (WseePolicyReferenceInfo)var0.get(var5.getUri());
            if (var5.getStatus().equals("deleted")) {
               var0.remove(var6.getUri());
            } else {
               var6.setStatus(var5.getStatus());
            }
         }
      }

   }

   private static void updatePolicies(Map<String, WseePolicyReferenceInfo> var0, WsPolicyBean[] var1) {
      WsPolicyBean[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WsPolicyBean var5 = var2[var4];
         if (!var0.containsKey(var5.getUri())) {
            addPolicy(var0, var5);
         } else {
            String var6 = var5.getUri();
            String var7 = var5.getDirection();
            if (var7 == null) {
               var7 = "both";
            }

            String var8 = var5.getStatus();
            WseePolicyReferenceInfo var9 = (WseePolicyReferenceInfo)var0.get(var6);
            if (var8.equals("enabled")) {
               if (!var7.equals(var9.getDirection())) {
                  var9.setDirection(var7);
               }
            } else if (var8.equals("deleted")) {
               if (!var7.equals("both") && !var7.equals(var9.getDirection())) {
                  if (var9.getDirection().equals("both")) {
                     switchDirection(var9, var7);
                  }
               } else {
                  var0.remove(var5.getUri());
               }
            } else if (var8.equals("disabled")) {
               var9.setStatus(var8);
               var9.setDirection("both");
            }
         }
      }

   }

   private static void switchDirection(WseePolicyReferenceInfo var0, String var1) {
      if (var1.equals("inbound")) {
         var0.setDirection("outbound");
      } else {
         var0.setDirection("inbound");
      }

   }

   private static void addPolicy(Map<String, WseePolicyReferenceInfo> var0, WsPolicyBean var1) {
      var0.put(var1.getUri(), new WseePolicyReferenceInfo("ws-policy", var1.getUri(), var1.getStatus(), var1.getDirection()));
   }

   private static void addPolicy(Map<String, WseePolicyReferenceInfo> var0, OwsmSecurityPolicyBean var1) {
      var0.put(var1.getUri(), new WseePolicyReferenceInfo("owsm-security", var1.getUri(), var1.getStatus()));
   }

   private static String effectiveCategory(Map<String, WseePolicyReferenceInfo> var0) {
      String var1 = null;
      Iterator var2 = var0.values().iterator();

      while(var2.hasNext()) {
         WseePolicyReferenceInfo var3 = (WseePolicyReferenceInfo)var2.next();
         if (var3.getStatus().equals("enabled")) {
            var1 = var3.getCategory();
            break;
         }
      }

      return var1;
   }

   private static void attach(WseePolicyReferenceInfo var0, Map<String, WseePolicyReferenceInfo> var1, Map<String, WseePolicyReferenceInfo> var2, WebservicePolicyRefBean var3, String var4, boolean var5, String var6) {
      if (var0.getStatus().equals("deleted")) {
         throw new IllegalArgumentException("Status deleted is invalid for this method");
      } else {
         if (var0.getStatus().equals("enabled")) {
            String var7 = effectiveCategory(var1);
            if (var7 != null && !var7.equals(var0.getCategory())) {
               throw new IllegalArgumentException("Invalid category " + var7 + " to attach to existing policies.");
            }
         }

         if (var1.containsKey(var0.getUri())) {
            WsPolicyBean var8;
            if (var5) {
               var8 = getOperationPolicyBean(var0, var3, var4);
               if (var8 != null) {
                  var8.setStatus(var0.getStatus());
                  var8.setDirection(var0.getDirection());
               } else {
                  PolicyDeployUtils.addPolicyToOperation(var3, var4, var0.getUri(), var0.getDirection(), var6);
               }
            } else if (var0.getCategory().equals("owsm-security")) {
               OwsmSecurityPolicyBean var9 = getOWSMPolicyBean(var0, var3, var4);
               if (var9 != null) {
                  var9.setStatus(var0.getStatus());
               } else {
                  PolicyDeployUtils.addOwsmSecurityPolicyToPort(var3, var4, var0.getUri(), var0.getStatus());
               }
            } else {
               var8 = getPortPolicyBean(var0, var3, var4);
               if (var8 != null) {
                  var8.setStatus(var0.getStatus());
                  var8.setDirection(var0.getDirection());
               } else {
                  PolicyDeployUtils.addPolicyToPort(var3, var4, var0.getUri(), var0.getDirection(), var0.getStatus());
               }
            }
         } else if (var0.getCategory().equals("owsm-security")) {
            if (var2.containsValue(var0)) {
               PolicyDeployUtils.removeOwsmSecurityPolicyFromPort(var3, var4, var0.getUri());
            } else {
               PolicyDeployUtils.addOwsmSecurityPolicyToPort(var3, var4, var0.getUri(), var0.getStatus());
            }
         } else if (var5) {
            if (var2.containsValue(var0)) {
               PolicyDeployUtils.removePolicyFromOperation(var3, var4, var0.getUri());
            } else {
               PolicyDeployUtils.addPolicyToOperation(var3, var4, var0.getUri(), var0.getDirection(), var6);
            }
         } else if (var2.containsValue(var0)) {
            PolicyDeployUtils.removePolicyFromPort(var3, var4, var0.getUri());
         } else {
            PolicyDeployUtils.addPolicyToPort(var3, var4, var0.getUri(), var0.getDirection(), var0.getStatus());
         }

      }
   }

   private static void attach(WseePolicyReferenceInfo var0, Map<String, WseePolicyReferenceInfo> var1, ServiceReferenceDescriptionBean var2, String var3) {
      if (var0.getStatus().equals("deleted")) {
         throw new IllegalArgumentException("Status deleted is invalid for this method");
      } else if (var0.getStatus().equals("enabled") && var0.getCategory().equals("ws-policy")) {
         throw new IllegalArgumentException("Invalid policy category ws-policy to attach to client.");
      } else {
         if (var1.containsKey(var0.getUri())) {
            OwsmPolicyBean var4 = getOWSMPolicyBean(var0, var2, var3);
            if (var4 != null) {
               var4.setStatus(var0.getStatus());
               setOverrides(var0, var4);
            } else {
               addOwsmSecurityPolicyToPort(var2, var3, var0);
            }
         } else {
            addOwsmSecurityPolicyToPort(var2, var3, var0);
         }

      }
   }

   private static void setOverrides(WseePolicyReferenceInfo var0, OwsmPolicyBean var1) {
      PropertyNamevalueBean[] var2 = var1.getSecurityConfigurationProperties();
      PropertyNamevalueBean var6;
      if (var2 != null) {
         PropertyNamevalueBean[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            var6 = var3[var5];
            var1.destroySecurityConfigurationProperty(var6);
         }
      }

      Map var7 = var0.getOverrides();
      if (var7 != null) {
         Iterator var8 = var7.keySet().iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            var6 = var1.createSecurityConfigurationProperty();
            var6.setName(var9);
            var6.setValue((String)var7.get(var9));
         }
      }

   }

   private static void addOwsmSecurityPolicyToPort(ServiceReferenceDescriptionBean var0, String var1, WseePolicyReferenceInfo var2) {
      PortInfoBean[] var3 = var0.getPortInfos();
      if (var3 != null) {
         PortInfoBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PortInfoBean var7 = var4[var6];
            if (var7.getPortName().equals(var1)) {
               OwsmPolicyBean var8 = createOwsmPolicy(var2.getUri(), var2.getStatus(), var7);
               setOverrides(var2, var8);
               return;
            }
         }
      }

      PortInfoBean var9 = var0.createPortInfo();
      var9.setPortName(var1);
      OwsmPolicyBean var10 = createOwsmPolicy(var2.getUri(), var2.getStatus(), var9);
      setOverrides(var2, var10);
   }

   private static OwsmPolicyBean createOwsmPolicy(String var0, String var1, PortInfoBean var2) {
      OwsmPolicyBean var3 = var2.createOwsmPolicy();
      var3.setUri(var0);
      var3.setCategory("owsm-security");
      var3.setStatus(var1);
      return var3;
   }

   private static void remove(String var0, Map<String, WseePolicyReferenceInfo> var1, WebservicePolicyRefBean var2, Class var3, String var4, boolean var5, String var6) {
      if (var1.containsKey(var0)) {
         WseePolicyReferenceInfo var7 = (WseePolicyReferenceInfo)var1.get(var0);
         if (var5) {
            PolicyDeployUtils.removePolicyFromOperation(var2, var4, var0, "both", var6);
         } else {
            String var8 = var7.getCategory();
            if ("owsm-security".equals(var8)) {
               PolicyDeployUtils.removeOwsmSecurityPolicyFromPort(var2, var4, var0);
            } else {
               PolicyDeployUtils.removePolicyFromPort(var2, var4, var0);
            }
         }

      }
   }

   private static void remove(String var0, Map<String, WseePolicyReferenceInfo> var1, ServiceReferenceDescriptionBean var2, String var3) throws PolicyManagementException {
      if (var1.containsKey(var0)) {
         removeOwsmSecurityPolicyFromPort(var2, var3, var0);
      }
   }

   private static void removeOwsmSecurityPolicyFromPort(ServiceReferenceDescriptionBean var0, String var1, String var2) {
      PortInfoBean[] var3 = var0.getPortInfos();
      if (var3 != null) {
         PortInfoBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PortInfoBean var7 = var4[var6];
            if (var7.getPortName().equals(var1)) {
               OwsmPolicyBean[] var8 = var7.getOwsmPolicy();
               if (var8 != null) {
                  OwsmPolicyBean[] var9 = var8;
                  int var10 = var8.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     OwsmPolicyBean var12 = var9[var11];
                     if (var12.getUri().equals(var2)) {
                        var7.destroyOwsmPolicy(var12);
                     }
                  }
               }
            }
         }
      }

   }

   private static void setStatus(String var0, Map<String, WseePolicyReferenceInfo> var1, ServiceReferenceDescriptionBean var2, String var3, boolean var4) {
      WseePolicyReferenceInfo var5 = (WseePolicyReferenceInfo)var1.get(var0);
      if (var5 == null) {
         throw new IllegalArgumentException("Policy " + var0 + " is not attached, status can not be set.");
      } else {
         boolean var6 = var5.getStatus().equals("enabled");
         if (var4 ^ var6) {
            OwsmPolicyBean var7 = getOWSMPolicyBean(var0, var2, var3);
            var7.setStatus(var4 ? "enabled" : "disabled");
         }
      }
   }

   private static void setStatus(String var0, Map<String, WseePolicyReferenceInfo> var1, WebservicePolicyRefBean var2, Class var3, String var4, boolean var5, String var6, boolean var7) {
      WseePolicyReferenceInfo var8 = (WseePolicyReferenceInfo)var1.get(var0);
      if (var8 == null) {
         throw new IllegalArgumentException("Policy " + var0 + " is not attached, status can not be set.");
      } else {
         boolean var9 = var8.getStatus().equals("enabled");
         if (var7 ^ var9) {
            boolean var10 = var8.getCategory().equals("owsm-security");
            if (var10) {
               setStatus(var2, var4, var0, var7);
            } else {
               setStatus(var2, var3, var4, var0, var5, var6, var7);
            }

         }
      }
   }

   private static void setStatus(WebservicePolicyRefBean var0, String var1, String var2, boolean var3) {
      OwsmSecurityPolicyBean var4 = getOWSMPolicyBean(var2, var0, var1);
      if (var4 == null) {
         PolicyDeployUtils.addOwsmSecurityPolicyToPort(var0, var1, var2, var3 ? "enabled" : "disabled");
      } else {
         var4.setStatus(var3 ? "enabled" : "disabled");
      }

   }

   private static void setStatus(WebservicePolicyRefBean var0, Class var1, String var2, String var3, boolean var4, String var5, boolean var6) {
      String var7 = var6 ? "enabled" : "disabled";
      WsPolicyBean var8 = null;
      if (var4) {
         var8 = getOperationPolicyBean(var3, var0, var2);
      } else {
         var8 = getPortPolicyBean(var3, var0, var2);
      }

      if (var8 == null) {
         if (var4) {
            PolicyDeployUtils.addPolicyToOperation(var0, var2, var3, "both", var5);
         } else {
            PolicyDeployUtils.addPolicyToPort(var0, var2, var3, "both", var7);
         }
      } else {
         var8.setStatus(var6 ? "enabled" : "disabled");
      }

   }

   private static WsPolicyBean getOperationPolicyBean(WseePolicyReferenceInfo var0, WebservicePolicyRefBean var1, String var2) {
      return getOperationPolicyBean(var0.getUri(), var1, var2);
   }

   private static WsPolicyBean getOperationPolicyBean(String var0, WebservicePolicyRefBean var1, String var2) {
      OperationPolicyBean[] var3 = var1.getOperationPolicy();
      if (var3 != null) {
         OperationPolicyBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            OperationPolicyBean var7 = var4[var6];
            if (var7.getOperationName().equals(var2)) {
               WsPolicyBean[] var8 = var7.getWsPolicy();
               WsPolicyBean[] var9 = var8;
               int var10 = var8.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  WsPolicyBean var12 = var9[var11];
                  if (var12.getUri().equals(var0)) {
                     return var12;
                  }
               }
            }
         }
      }

      return null;
   }

   private static OwsmSecurityPolicyBean getOWSMPolicyBean(WseePolicyReferenceInfo var0, WebservicePolicyRefBean var1, String var2) {
      return getOWSMPolicyBean(var0.getUri(), var1, var2);
   }

   private static OwsmPolicyBean getOWSMPolicyBean(WseePolicyReferenceInfo var0, ServiceReferenceDescriptionBean var1, String var2) {
      return getOWSMPolicyBean(var0.getUri(), var1, var2);
   }

   private static OwsmSecurityPolicyBean getOWSMPolicyBean(String var0, WebservicePolicyRefBean var1, String var2) {
      PortPolicyBean[] var3 = var1.getPortPolicy();
      if (var3 != null) {
         PortPolicyBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PortPolicyBean var7 = var4[var6];
            if (var7.getPortName().equals(var2)) {
               OwsmSecurityPolicyBean[] var8 = var7.getOwsmSecurityPolicy();
               OwsmSecurityPolicyBean[] var9 = var8;
               int var10 = var8.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  OwsmSecurityPolicyBean var12 = var9[var11];
                  if (var12.getUri().equals(var0)) {
                     return var12;
                  }
               }
            }
         }
      }

      return null;
   }

   private static OwsmPolicyBean getOWSMPolicyBean(String var0, ServiceReferenceDescriptionBean var1, String var2) {
      PortInfoBean[] var3 = var1.getPortInfos();
      if (var3 != null) {
         PortInfoBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PortInfoBean var7 = var4[var6];
            if (var7.getPortName().equals(var2)) {
               OwsmPolicyBean[] var8 = var7.getOwsmPolicy();
               OwsmPolicyBean[] var9 = var8;
               int var10 = var8.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  OwsmPolicyBean var12 = var9[var11];
                  if (var12.getUri().equals(var0)) {
                     return var12;
                  }
               }
            }
         }
      }

      return null;
   }

   private static WsPolicyBean getPortPolicyBean(WseePolicyReferenceInfo var0, WebservicePolicyRefBean var1, String var2) {
      return getPortPolicyBean(var0.getUri(), var1, var2);
   }

   private static WsPolicyBean getPortPolicyBean(String var0, WebservicePolicyRefBean var1, String var2) {
      PortPolicyBean[] var3 = var1.getPortPolicy();
      if (var3 != null) {
         PortPolicyBean[] var4 = var3;
         int var5 = var3.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            PortPolicyBean var7 = var4[var6];
            if (var7.getPortName().equals(var2)) {
               WsPolicyBean[] var8 = var7.getWsPolicy();
               WsPolicyBean[] var9 = var8;
               int var10 = var8.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  WsPolicyBean var12 = var9[var11];
                  if (var12.getUri().equals(var0)) {
                     return var12;
                  }
               }
            }
         }
      }

      return null;
   }

   private static ArrayList<WseePolicyReferenceInfo> getPoliciesFromAnnotatedElement(AnnotatedElement var0) {
      ArrayList var1 = new ArrayList();
      if (!getWLSPolicies(var0, var1)) {
         getOWSMPolicies(var0, var1);
      }

      return var1;
   }

   private static boolean getWLSPolicies(AnnotatedElement var0, ArrayList<WseePolicyReferenceInfo> var1) {
      boolean var2 = false;
      Policies var3 = (Policies)var0.getAnnotation(Policies.class);
      if (var3 != null) {
         Policy[] var4 = var3.value();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Policy var7 = var4[var6];
            addPolicyRefInfo(var1, var7);
         }

         var2 = true;
      } else {
         Policy var8 = (Policy)var0.getAnnotation(Policy.class);
         if (var8 != null) {
            addPolicyRefInfo(var1, var8);
            var2 = true;
         }
      }

      return var2;
   }

   private static boolean getOWSMPolicies(AnnotatedElement var0, ArrayList<WseePolicyReferenceInfo> var1) {
      boolean var2 = false;
      SecurityPolicies var3 = (SecurityPolicies)var0.getAnnotation(SecurityPolicies.class);
      if (var3 != null) {
         SecurityPolicy[] var4 = var3.value();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            SecurityPolicy var7 = var4[var6];
            addPolicyRefInfo(var1, var7);
         }

         var2 = true;
      } else {
         SecurityPolicy var8 = (SecurityPolicy)var0.getAnnotation(SecurityPolicy.class);
         if (var8 != null) {
            addPolicyRefInfo(var1, var8);
            var2 = true;
         }
      }

      return var2;
   }

   private static void addPolicyRefInfo(ArrayList<WseePolicyReferenceInfo> var0, Policy var1) {
      var0.add(new WseePolicyReferenceInfo("ws-policy", var1.uri(), "enabled", var1.direction().toString()));
   }

   private static void addPolicyRefInfo(ArrayList<WseePolicyReferenceInfo> var0, SecurityPolicy var1) {
      var0.add(new WseePolicyReferenceInfo("owsm-security", var1.uri()));
   }

   private static Class getServiceClass(String var0, String var1, String var2, DeploymentConfigurationHelper var3) throws ManagementException, PolicyManagementException, ConfigurationException {
      return loadServiceClass(var3, var0, var1, new QName(var2));
   }

   private static Class loadServiceClass(DeploymentConfigurationHelper var0, String var1, String var2) {
      WebLogicDeployableObject var3 = var0.getDeployableObject(var1);
      return var3.getClassFromScope(var2);
   }

   private static String getServiceLink(String var0, String var1, String var2, DeploymentConfigurationHelper var3) throws PolicyManagementException, ManagementException {
      WebAppBean var4 = WebServiceBeanUtils.getWebAppBean(var3, var0);
      boolean var5 = var4 != null;
      WebservicesBean var6 = WebServiceBeanUtils.getWebservicesBean(var3, var0, var5);
      return WebServiceBeanUtils.getLinkName(var6, var1, new QName(var2), var5);
   }

   private static Class loadServiceClass(DeploymentConfigurationHelper var0, String var1, String var2, QName var3) throws PolicyManagementException, ConfigurationException {
      WebAppBean var4 = WebServiceBeanUtils.getWebAppBean(var0, var1);
      EjbJarBean var5 = null;
      boolean var6 = true;
      if (var4 == null) {
         var6 = false;
         var5 = WebServiceBeanUtils.getEjbBean(var0, var1);
      }

      WebservicesBean var7 = WebServiceBeanUtils.getWebservicesBean(var0, var1, var6);
      if (var7 == null) {
         throw new PolicyManagementException("There was an error getting the webservice bean");
      } else {
         String var8 = WebServiceBeanUtils.getServiceClassName(var2, var7, var3, var6, var4, var5);
         Class var9 = null;
         if (var8 == null && var6) {
            ServletBean[] var10 = var4.getServlets();

            for(int var11 = 0; var11 < var10.length; ++var11) {
               String var12 = var10[var11].getServletClass();
               Class var13 = WebServiceBeanUtils.loadServiceClass(var0, var1, var12);
               if (var13 != null) {
                  String var14 = null;
                  String var15 = null;
                  WebService var16 = (WebService)var13.getAnnotation(WebService.class);
                  if (var16 != null) {
                     var14 = var16.serviceName();
                     var15 = var16.portName();
                  } else {
                     WebServiceProvider var17 = (WebServiceProvider)var13.getAnnotation(WebServiceProvider.class);
                     if (var17 != null) {
                        var14 = var17.serviceName();
                        var15 = var17.portName();
                     }
                  }

                  if (var14 == null || var14.length() == 0) {
                     var14 = var13.getSimpleName() + "Service";
                  }

                  if (var15 == null || var15.length() == 0) {
                     var15 = (var16 != null && var16.name() != null && var16.name().length() > 0 ? var16.name() : var13.getSimpleName()) + "Port";
                  }

                  if (var2.equals(var14) && var3.equals(var15)) {
                     var8 = var12;
                     var9 = var13;
                     break;
                  }
               }
            }

            if (var8 == null) {
               throw new PolicyManagementException("webservice.webservicepoliciestable.unabletogetclassname");
            }
         }

         if (var9 == null && var8 != null) {
            var9 = loadServiceClass(var0, var1, var8);
         }

         if (var8 != null && var9 == null) {
            throw new PolicyManagementException("webservice.webservicepoliciestable.unabletoloadclass" + var8);
         } else {
            return var9;
         }
      }
   }

   private static WebservicePolicyRefBean loadPolicyDescriptor(DeploymentConfigurationHelper var0, String var1) throws ManagementException {
      try {
         return var0.getWebservicePolicies(var1);
      } catch (FileNotFoundException var3) {
         throw new ManagementException(var3);
      } catch (DDBeanCreateException var4) {
         throw new ManagementException(var4);
      } catch (ConfigurationException var5) {
         throw new ManagementException(var5);
      }
   }

   private static ServiceReferenceDescriptionBean loadServiceRefDescriptor(DeploymentConfigurationHelper var0, String var1, String var2, String var3) throws ManagementException {
      try {
         return var0.getServiceRefPolicies(var1, var2, var3);
      } catch (FileNotFoundException var5) {
         throw new ManagementException(var5);
      } catch (DDBeanCreateException var6) {
         throw new ManagementException(var6);
      } catch (ConfigurationException var7) {
         throw new ManagementException(var7);
      }
   }

   private static WebservicePolicyRefBean getPolicyDescriptor(String var0, DeploymentConfigurationHelper var1) throws ManagementException {
      return loadPolicyDescriptor(var1, var0);
   }

   private static ServiceReferenceDescriptionBean getServiceRefDescriptor(String var0, String var1, String var2, DeploymentConfigurationHelper var3) throws ManagementException {
      return loadServiceRefDescriptor(var3, var0, var1, var2);
   }

   private static DeploymentConfigurationHelper getDCH(String var0) throws ManagementException {
      AppDeploymentMBean var1 = DeploymentConfigurationHelper.getAppDeploymentMBean(var0);
      return getDCH(var1);
   }

   private static DeploymentConfigurationHelper getDCH(AppDeploymentMBean var0) throws ManagementException {
      synchronized(configLock) {
         return DeploymentConfigurationManager.getInstance().getDeploymentConfiguration(var0);
      }
   }

   public static void redeployPlan(AppDeploymentMBean var0, String var1) throws ManagementException {
      try {
         File var2 = null;
         if (!StringUtil.isEmpty(var1)) {
            var2 = new File(var1);
         }

         WebLogicDeploymentManager var3 = DeploymentConfigurationHelper.getTmpDeploymentManager();
         TargetModuleID[] var4 = var3.getModules(var0);
         File var5 = new File(var0.getAbsoluteSourcePath());
         if (isDeploymentActive(var0)) {
            var3.redeploy(var4, var5, var2, createDeploymentOptions());
         } else if (isDeployPending(var0)) {
            var3.deploy(var4, var5, var2, createDeploymentOptions());
         } else {
            var3.distribute(var4, var5, var2, createDeploymentOptions());
         }
      } catch (Exception var6) {
      }

   }

   public static DeploymentOptions createDeploymentOptions() {
      DeploymentOptions var0 = new DeploymentOptions();
      var0.setUseExpiredLock(true);
      return var0;
   }

   public static boolean isDeploymentActive(AppDeploymentMBean var0) {
      try {
         DomainRuntimeMBean var1 = MBeanUtils.getDomainRuntimeMBean();
         AppRuntimeStateRuntimeMBean var2 = var1.getAppRuntimeStateRuntime();
         ServerRuntimeMBean[] var3 = MBeanUtils.getServerRuntimeMBeans();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            String var5 = var2.getCurrentState(var0.getName(), var3[var4].getName());
            if (var5 != null && (var5.equals("STATE_ADMIN") || var5.equals("STATE_ACTIVE") || var5.equals("STATE_PREPARED"))) {
               return true;
            }
         }
      } catch (ManagementException var6) {
      }

      return false;
   }

   public static boolean isDeployPending(AppDeploymentMBean var0) throws ManagementException {
      DeploymentTaskRuntimeMBean var1 = getLastDeploymentTaskRuntimeMBean(var0);
      if (var1 != null && var1.isRunning()) {
         String var2 = var1.getStatus();
         if (var2.indexOf("deploy Initializing") >= 0) {
            return true;
         }
      }

      return false;
   }

   public static DeploymentTaskRuntimeMBean getLastDeploymentTaskRuntimeMBean(AppDeploymentMBean var0) throws ManagementException {
      DeploymentTaskRuntimeMBean var1 = null;
      String var2 = var0.getApplicationName();
      String var3 = var0.getVersionIdentifier();
      DeployerRuntimeMBean var4 = MBeanUtils.getDomainRuntimeMBean().getDeployerRuntime();
      DeploymentTaskRuntimeMBean[] var5 = var4.getDeploymentTaskRuntimes();
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         DeploymentTaskRuntimeMBean var8 = var5[var7];

         try {
            if (var8 != null && var8.getApplicationName().equals(var2)) {
               String var9 = var8.getApplicationVersionIdentifier();
               if (StringUtil.isEmpty(var3) || var9 != null && var9.equals(var3)) {
                  if (var8.getBeginTime() == 0L && var8.getEndTime() == 0L) {
                     return var8;
                  }

                  if (var1 == null || var1.getBeginTime() < var8.getBeginTime()) {
                     var1 = var8;
                  }
               }
            }
         } catch (Exception var10) {
         }
      }

      return var1;
   }

   static Object getLock(String var0) {
      Lock var1 = null;
      synchronized(usedLocks) {
         synchronized(unusedLocks) {
            if (usedLocks.containsKey(var0)) {
               var1 = (Lock)usedLocks.get(var0);
               var1.add();
               Lock var10000 = var1;
               return var10000;
            }

            int var4 = unusedLocks.size();
            if (var4 == 0) {
               unusedLocks.add(new Lock());
            }

            if (unusedLocks.size() > 16) {
               unusedLocks.removeAll(unusedLocks.subList(16, var4 - 1));
            }

            var4 = unusedLocks.size();
            var1 = (Lock)unusedLocks.remove(var4 - 1);
            usedLocks.put(var0, var1);
            var1.add();
         }

         return var1;
      }
   }

   static void releaseLock(String var0, Object var1) {
      synchronized(usedLocks) {
         synchronized(unusedLocks) {
            Lock var4 = (Lock)var1;
            var4.remove();
            if (var4.isEmpty()) {
               usedLocks.remove(var0);
               unusedLocks.add(var4);
            }
         }

      }
   }

   private static class Lock {
      int counter;

      private Lock() {
         this.counter = 0;
      }

      public void add() {
         ++this.counter;
      }

      public void remove() {
         --this.counter;
      }

      public boolean isEmpty() {
         return this.counter == 0;
      }

      // $FF: synthetic method
      Lock(Object var1) {
         this();
      }
   }

   public class DeployableObjectInfoWrapper extends DeployableObjectInfo {
      public DeployableObjectInfoWrapper(WebLogicDeployableObject var2, WebLogicDeploymentConfiguration var3, String var4) throws IOException, ConfigurationException {
         super(var2, var3, var4);
      }
   }
}
