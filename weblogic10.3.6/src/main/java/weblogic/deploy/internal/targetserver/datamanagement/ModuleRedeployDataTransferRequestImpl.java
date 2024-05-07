package weblogic.deploy.internal.targetserver.datamanagement;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

public class ModuleRedeployDataTransferRequestImpl extends AppDataTransferRequestImpl {
   private static final long serialVersionUID = 4480914022040417421L;

   public ModuleRedeployDataTransferRequestImpl() {
   }

   public ModuleRedeployDataTransferRequestImpl(String var1, String var2, long var3, List var5, String var6, boolean var7) {
      super(var1, var2, var3, var5, var6, var7);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
   }
}
