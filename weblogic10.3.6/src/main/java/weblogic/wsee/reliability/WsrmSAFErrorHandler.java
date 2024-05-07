package weblogic.wsee.reliability;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.messaging.saf.SAFErrorHandler;

public class WsrmSAFErrorHandler implements SAFErrorHandler {
   public short getType() {
      return 1;
   }

   public boolean isAlwaysForward() {
      return false;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
   }
}
