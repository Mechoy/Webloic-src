package weblogic.connector.deploy;

import java.security.AccessController;
import java.util.Map;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.exception.RAException;
import weblogic.connector.external.AdminObjInfo;
import weblogic.connector.external.ConfigPropInfo;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;

class AdminObjectChangePackage implements ChangePackage {
   private RAInstanceManager raIM = null;
   private ConnectorModuleChangePackage.ChangeType changeType;
   private AdminObjInfo adminInfo = null;

   protected AdminObjectChangePackage(RAInstanceManager var1, AdminObjInfo var2, ConnectorModuleChangePackage.ChangeType var3, Map<String, ConfigPropInfo> var4) {
      this.raIM = var1;
      this.adminInfo = var2;
      this.changeType = var3;
   }

   public void rollback() throws RAException {
      if (ConnectorModuleChangePackage.ChangeType.NEW.equals(this.changeType)) {
         this.raIM.removeAdminObject(this.adminInfo);
      }

   }

   public void prepare() throws RAException {
      if (ConnectorModuleChangePackage.ChangeType.NEW.equals(this.changeType)) {
         AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         this.raIM.createAdminObject(this.adminInfo, var1);
      }

   }

   public void activate() throws RAException {
      if (ConnectorModuleChangePackage.ChangeType.NEW.equals(this.changeType)) {
         this.raIM.bindAdminObject(this.adminInfo.getKey());
         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Binding new Admin Object " + this.adminInfo.getJndiName());
         }
      } else if (ConnectorModuleChangePackage.ChangeType.REMOVE.equals(this.changeType)) {
         this.raIM.removeAdminObject(this.adminInfo);
         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Unbinding Admin Object " + this.adminInfo.getJndiName());
         }
      }

   }

   public String toString() {
      return this.changeType.toString() + " " + this.adminInfo.getJndiName();
   }
}
