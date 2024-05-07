package weblogic.security.internal;

import java.security.AccessController;
import java.util.Arrays;
import weblogic.descriptor.DescriptorUpdateEvent;
import weblogic.descriptor.DescriptorUpdateListener;
import weblogic.descriptor.DescriptorUpdateRejectedException;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.ServerMBean;
import weblogic.management.provider.ManagementService;
import weblogic.management.security.RealmMBean;
import weblogic.management.utils.ErrorCollectionException;
import weblogic.nodemanager.server.NMEncryptionHelper;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;

public final class SecurityConfigurationValidator extends AbstractServerService implements DescriptorUpdateListener {
   private static SecurityConfigurationValidator singleton = null;
   private static final boolean DEBUG = false;
   private String currentNMUser;
   private byte[] currentNMPass;
   private String proposedNMUser;
   private byte[] proposedNMPass;

   private SecurityConfigurationValidator() {
   }

   public static synchronized SecurityConfigurationValidator getInstance() {
      if (singleton == null) {
         singleton = new SecurityConfigurationValidator();
      }

      return singleton;
   }

   private static void p(String var0) {
   }

   public synchronized void start() {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      ManagementService.getRuntimeAccess(var1).getDomain().getDescriptor().addUpdateListener(this);
   }

   public void prepareUpdate(DescriptorUpdateEvent var1) throws DescriptorUpdateRejectedException {
      DomainMBean var2 = (DomainMBean)var1.getProposedDescriptor().getRootBean();
      RealmMBean var3 = var2.getSecurityConfiguration().getDefaultRealm();
      if (var3 == null) {
         throw new DescriptorUpdateRejectedException(SecurityLogger.getCannotActivateChangesNoDefaultRealmError());
      } else {
         try {
            var3.validate();
         } catch (ErrorCollectionException var7) {
            throw new DescriptorUpdateRejectedException(SecurityLogger.getCannotActivateChangesImproperlyConfiguredDefaultRealmError(), var7);
         }

         DomainMBean var4 = (DomainMBean)var1.getProposedDescriptor().getRootBean();
         this.proposedNMUser = var4.getSecurityConfiguration().getNodeManagerUsername();
         this.proposedNMPass = var4.getSecurityConfiguration().getNodeManagerPassword().getBytes();
         AuthenticatedSubject var5 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         DomainMBean var6 = ManagementService.getRuntimeAccess(var5).getDomain();
         this.currentNMUser = var6.getSecurityConfiguration().getNodeManagerUsername();
         this.currentNMPass = var6.getSecurityConfiguration().getNodeManagerPassword().getBytes();
         if (this.currentNMUser == null) {
            this.currentNMUser = "";
         }

         if (this.currentNMPass == null) {
            this.currentNMPass = "".getBytes();
         }

      }
   }

   public void activateUpdate(DescriptorUpdateEvent var1) {
      AuthenticatedSubject var2 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (this.proposedNMUser != null || this.proposedNMPass != null) {
         if (this.proposedNMUser == null) {
            this.proposedNMUser = this.currentNMUser;
         }

         if (this.proposedNMPass == null) {
            this.proposedNMPass = this.currentNMPass;
         }

         if (this.proposedNMUser.equals(this.currentNMUser) && Arrays.equals(this.proposedNMPass, this.currentNMPass)) {
            return;
         }

         ServerMBean var3 = ManagementService.getRuntimeAccess(var2).getServer();
         new String(this.proposedNMPass);
         NMEncryptionHelper.updateNMHash(var3.getRootDirectory(), this.proposedNMUser, this.proposedNMPass);
      }

      this.currentNMUser = null;
      this.proposedNMUser = null;
      this.currentNMPass = null;
      this.proposedNMPass = null;
   }

   public void rollbackUpdate(DescriptorUpdateEvent var1) {
      this.currentNMUser = null;
      this.proposedNMUser = null;
      this.currentNMPass = null;
      this.proposedNMPass = null;
   }
}
