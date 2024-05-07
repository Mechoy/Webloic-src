package weblogic.wsee.reliability;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class WsrmClientPayloadContext extends WsrmPayloadContext {
   private static long serialVersionUID = 3541533640324550007L;

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
   }
}
