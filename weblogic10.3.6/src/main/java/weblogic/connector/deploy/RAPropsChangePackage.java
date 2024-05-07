package weblogic.connector.deploy;

import java.util.Map;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.exception.RAException;
import weblogic.connector.external.ConfigPropInfo;

class RAPropsChangePackage implements ChangePackage {
   private RAInstanceManager raIM;
   private Map<String, ConfigPropInfo> changedProperties;
   private String newJNDIName;

   public void setNewJNDIName(String var1) {
      this.newJNDIName = var1;
   }

   protected RAPropsChangePackage(RAInstanceManager var1, Map<String, ConfigPropInfo> var2) {
      this.raIM = var1;
      this.changedProperties = var2;
   }

   public void rollback() throws RAException {
   }

   public void prepare() throws RAException {
      if (this.newJNDIName != null) {
         String var1 = JNDIHandler.getJndiNameAndVersion(this.newJNDIName, this.raIM.getVersionId());
         if (JNDIHandler.verifyJNDIName(var1)) {
            String var2 = Debug.getExceptionJndiNameAlreadyBound(this.newJNDIName);
            throw new RAException(var2);
         }
      }

   }

   public void activate() throws RAException {
      if (this.newJNDIName != null) {
         this.raIM.rebindRA(this.newJNDIName);
      }

   }
}
