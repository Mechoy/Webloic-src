package weblogic.wsee.policy.deployment;

import java.io.IOException;
import java.security.AccessController;
import javax.enterprise.deploy.spi.exceptions.ConfigurationException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.runtime.DomainRuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.WseePolicySubjectManagerRuntimeMBean;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.policy.deployment.config.PolicyDescriptorUtils;
import weblogic.wsee.policy.deployment.config.PolicyManagementException;

public class WseePolicySubjectManagerRuntimeMBeanImpl extends RuntimeMBeanDelegate implements WseePolicySubjectManagerRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   static void initialize() throws ManagementException {
      if (ManagementService.getRuntimeAccess(kernelId).isAdminServer()) {
         new WseePolicySubjectManagerRuntimeMBeanImpl(getDomainRuntimeMBean());
      }

   }

   private WseePolicySubjectManagerRuntimeMBeanImpl(DomainRuntimeMBean var1) throws ManagementException {
      super(var1.getName(), var1);
      var1.setPolicySubjectManagerRuntime(this);
   }

   public TabularData getPolicyReferenceInfos(String var1) throws ManagementException {
      WseePolicyReferenceInfo[] var2 = new WseePolicyReferenceInfo[0];

      try {
         var2 = PolicyDescriptorUtils.getPolicyReferenceInfos(var1);
      } catch (PolicyManagementException var4) {
         throw new ManagementException(getMessage(var4));
      } catch (ConfigurationException var5) {
         throw new ManagementException(getMessage(var5));
      }

      return WseePolicyReferenceInfo.from(var2);
   }

   public void setPolicyReferenceInfos(String var1, TabularData var2) throws ManagementException {
      WseePolicyReferenceInfo[] var3 = WseePolicyReferenceInfo.from(var2);

      try {
         PolicyDescriptorUtils.setPolicyReferenceInfos(var1, var3);
      } catch (PolicyManagementException var5) {
         throw new ManagementException(getMessage(var5));
      } catch (ConfigurationException var6) {
         throw new ManagementException(getMessage(var6));
      } catch (IOException var7) {
         throw new ManagementException(getMessage(var7));
      }
   }

   public void setPolicyReferenceInfos(String var1, TabularData var2, String var3) throws ManagementException {
   }

   public void setPolicyReferenceInfo(String var1, CompositeData var2) throws ManagementException {
      try {
         PolicyDescriptorUtils.setPolicyReferenceInfo(var1, WseePolicyReferenceInfo.from(var2));
      } catch (PolicyManagementException var4) {
         throw new ManagementException(getMessage(var4));
      } catch (ConfigurationException var5) {
         throw new ManagementException(getMessage(var5));
      } catch (IOException var6) {
         throw new ManagementException(getMessage(var6));
      }
   }

   public void attachPolicyReference(String var1, CompositeData var2) throws ManagementException {
      try {
         PolicyDescriptorUtils.attachPolicyReference(var1, WseePolicyReferenceInfo.from(var2));
      } catch (PolicyManagementException var4) {
         throw new ManagementException(getMessage(var4));
      } catch (ConfigurationException var5) {
         throw new ManagementException(getMessage(var5));
      } catch (IOException var6) {
         throw new ManagementException(getMessage(var6));
      }
   }

   public void attachPolicyReference(String var1, CompositeData var2, String var3) throws ManagementException {
   }

   public void removePolicyReference(String var1, String var2) throws ManagementException {
      try {
         PolicyDescriptorUtils.removePolicyReference(var1, var2);
      } catch (PolicyManagementException var4) {
         throw new ManagementException(getMessage(var4));
      } catch (ConfigurationException var5) {
         throw new ManagementException(getMessage(var5));
      } catch (IOException var6) {
         throw new ManagementException(getMessage(var6));
      }
   }

   public void removePolicyReference(String var1, String var2, String var3) throws ManagementException {
   }

   public String getPolicyRefStatus(String var1, String var2) throws ManagementException {
      try {
         String var3 = null;
         var3 = PolicyDescriptorUtils.getPolicyRefStatus(var1, var2);
         return var3;
      } catch (PolicyManagementException var4) {
         throw new ManagementException(getMessage(var4));
      } catch (ConfigurationException var5) {
         throw new ManagementException(getMessage(var5));
      }
   }

   public void setPolicyRefStatus(String var1, String var2, Boolean var3) throws ManagementException {
      try {
         PolicyDescriptorUtils.setPolicyRefStatus(var1, var2, var3);
      } catch (PolicyManagementException var5) {
         throw new ManagementException(getMessage(var5));
      } catch (ConfigurationException var6) {
         throw new ManagementException(getMessage(var6));
      } catch (IOException var7) {
         throw new ManagementException(getMessage(var7));
      }
   }

   public void setPolicyRefStatus(String var1, String var2, boolean var3, String var4) throws ManagementException {
   }

   public boolean isOWSMAttachable(String var1) throws ManagementException {
      try {
         return PolicyDescriptorUtils.isOWSMAttachable(var1);
      } catch (PolicyManagementException var3) {
         throw new ManagementException(getMessage(var3));
      } catch (ConfigurationException var4) {
         throw new ManagementException(getMessage(var4));
      }
   }

   private static DomainRuntimeMBean getDomainRuntimeMBean() {
      return ManagementService.getDomainAccess(kernelId).getDomainRuntime();
   }

   private static String getMessage(Throwable var0) {
      return var0 == null ? "" : var0.getMessage();
   }
}
