package weblogic.deploy.internal.targetserver.datamanagement;

import weblogic.deploy.service.DataTransferRequest;

public class ModuleRedeployDataUpdate extends AppDataUpdate {
   public ModuleRedeployDataUpdate(Data var1, DataUpdateRequestInfo var2) {
      super(var1, var2);
   }

   protected DataTransferRequest createDataTransferRequest() {
      AppData var1 = this.getLocalAppData();
      return new ModuleRedeployDataTransferRequestImpl(var1.getAppName(), var1.getAppVersionIdentifier(), this.getRequestId(), this.getRequestedFiles(), var1.getLockPath(), false);
   }
}
