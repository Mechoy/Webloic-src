package weblogic.security;

import com.bea.common.security.service.SAML2PublishException;
import com.bea.common.security.service.SAML2Service;
import java.security.AccessController;
import weblogic.management.ManagementException;
import weblogic.management.provider.ManagementService;
import weblogic.management.provider.RuntimeAccess;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SingleSignOnServicesRuntimeMBean;
import weblogic.management.utils.AlreadyExistsException;
import weblogic.management.utils.CreateException;
import weblogic.management.utils.InvalidParameterException;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityServiceManager;
import weblogic.security.service.SecurityService.ServiceType;

public final class SingleSignOnServicesRuntime extends RuntimeMBeanDelegate implements SingleSignOnServicesRuntimeMBean {
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   private static RuntimeAccess getRuntimeAccess() {
      return ManagementService.getRuntimeAccess(kernelId);
   }

   public SingleSignOnServicesRuntime() throws ManagementException {
      super(getRuntimeAccess().getServerName(), getRuntimeAccess().getServerRuntime(), true, getRuntimeAccess().getServer().getSingleSignOnServices());
      getRuntimeAccess().getServerRuntime().setSingleSignOnServicesRuntime(this);
   }

   public void publish(String var1) throws InvalidParameterException {
      try {
         this.publish(var1, false);
      } catch (InvalidParameterException var3) {
         throw var3;
      } catch (Exception var4) {
         throw new InvalidParameterException(var4.getMessage(), var4);
      }
   }

   public void publish(String var1, boolean var2) throws InvalidParameterException, CreateException, AlreadyExistsException {
      try {
         SAML2Service var3 = (SAML2Service)SecurityServiceManager.getSecurityService(kernelId, getRuntimeAccess().getDomain().getSecurityConfiguration().getDefaultRealm().getName(), ServiceType.SAML2_SSO);
         if (var3 == null) {
            throw new CreateException("SAML2Service Unavailable");
         } else {
            var3.publish(var1, var2);
         }
      } catch (IllegalArgumentException var4) {
         throw new InvalidParameterException(var4.getMessage(), var4);
      } catch (SAML2PublishException var5) {
         if (var5 instanceof SAML2PublishException.OverwriteProhibitedException) {
            throw new AlreadyExistsException(var5.getMessage(), var5);
         } else if (var5 instanceof SAML2PublishException.FileCreateException) {
            throw new InvalidParameterException(var5.getMessage(), var5);
         } else {
            throw new CreateException(var5.getMessage(), var5);
         }
      }
   }
}
