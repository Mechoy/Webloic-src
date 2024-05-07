package weblogic.connector.deploy;

import java.util.Map;
import java.util.Properties;
import weblogic.connector.common.Debug;
import weblogic.connector.common.RAInstanceManager;
import weblogic.connector.exception.RAException;
import weblogic.connector.external.ConfigPropInfo;
import weblogic.connector.external.OutboundInfo;
import weblogic.connector.outbound.ConnectionPool;

class ConnectionPoolChangePackage implements ChangePackage {
   private RAInstanceManager raIM;
   private OutboundInfo outboundInfo;
   private Properties poolParamProperties;
   private Properties loggingProperties;
   private ConnectorModuleChangePackage.ChangeType changeType;
   private ConnectionPool pool = null;

   protected ConnectionPoolChangePackage(RAInstanceManager var1, OutboundInfo var2, Properties var3, Properties var4, Map<String, ConfigPropInfo> var5, ConnectorModuleChangePackage.ChangeType var6) {
      this.raIM = var1;
      this.outboundInfo = var2;
      this.poolParamProperties = var3;
      this.loggingProperties = var4;
      this.changeType = var6;
   }

   public void prepare() throws RAException {
      if (ConnectorModuleChangePackage.ChangeType.NEW.equals(this.changeType)) {
         this.raIM.getRAOutboundManager().createConnectionFactory(this.outboundInfo);
         String var1 = JNDIHandler.getJndiNameAndVersion(this.outboundInfo.getKey(), this.raIM.getVersionId());
         this.raIM.getRAOutboundManager().preparePool(var1);
      }

   }

   public void activate() throws RAException {
      String var1;
      if (ConnectorModuleChangePackage.ChangeType.NEW.equals(this.changeType)) {
         var1 = JNDIHandler.getJndiNameAndVersion(this.outboundInfo.getKey(), this.raIM.getVersionId());
         this.raIM.getRAOutboundManager().activatePool(var1);
         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Active Connection Pool " + var1);
         }
      } else if (this.changeType.equals(ConnectorModuleChangePackage.ChangeType.REMOVE)) {
         var1 = JNDIHandler.getJndiNameAndVersion(this.outboundInfo.getKey(), this.raIM.getVersionId());
         this.raIM.getRAOutboundManager().deactivatePool(var1);
         this.raIM.getRAOutboundManager().shutdownPool(var1);
         if (Debug.isDeploymentEnabled()) {
            Debug.deployment("Shutdown Connection Pool " + var1);
         }
      } else {
         this.pool = this.raIM.getRAOutboundManager().getConnectionPool(this.outboundInfo.getKey());
         this.pool.applyPoolParamChanges(this.poolParamProperties);
         this.pool.applyLoggingChanges(this.loggingProperties, this.outboundInfo);
         this.raIM.getRAOutboundManager().updateOutBoundInfo(this.outboundInfo.getKey(), this.outboundInfo);
      }

   }

   public void rollback() throws RAException {
      if (ConnectorModuleChangePackage.ChangeType.NEW.equals(this.changeType)) {
         String var1 = JNDIHandler.getJndiNameAndVersion(this.outboundInfo.getKey(), this.raIM.getVersionId());
         this.raIM.getRAOutboundManager().shutdownPool(var1);
      }

   }

   public String toString() {
      return this.changeType.toString() + " " + this.outboundInfo.getJndiName();
   }
}
