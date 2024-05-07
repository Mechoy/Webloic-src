package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;
import weblogic.deploy.service.ConfigDataTransferRequest;

public class ConfigDataTransferRequestImpl extends DataTransferRequestImpl implements ConfigDataTransferRequest {
   private static final long serialVersionUID = 2517073255669231968L;

   public ConfigDataTransferRequestImpl() {
   }

   public ConfigDataTransferRequestImpl(long var1, List var3, String var4) {
      super(var1, var3, var4);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
   }
}
