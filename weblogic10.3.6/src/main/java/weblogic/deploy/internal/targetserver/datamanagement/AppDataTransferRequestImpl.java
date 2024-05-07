package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import weblogic.deploy.service.AppDataTransferRequest;

public class AppDataTransferRequestImpl extends DataTransferRequestImpl implements AppDataTransferRequest {
   private static final long serialVersionUID = 2968297419700820168L;
   private String appName;
   private String appVersionIdentifier;
   private boolean planUpdate;

   public AppDataTransferRequestImpl() {
   }

   public AppDataTransferRequestImpl(String var1, String var2, long var3, List var5, String var6, boolean var7) {
      super(var3, var5, var6);
      this.appName = var1;
      this.appVersionIdentifier = var2;
      this.planUpdate = var7;
   }

   public String getAppName() {
      return this.appName;
   }

   public String getAppVersionIdentifier() {
      return this.appVersionIdentifier;
   }

   public boolean isPlanUpdate() {
      return this.planUpdate;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeObject(this.appName);
      var1.writeObject(this.appVersionIdentifier);
      var1.writeBoolean(this.planUpdate);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.appName = (String)var1.readObject();
      this.appVersionIdentifier = (String)var1.readObject();
      this.planUpdate = var1.readBoolean();
   }
}
