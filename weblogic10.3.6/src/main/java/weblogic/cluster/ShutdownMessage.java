package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.rmi.spi.HostID;

public class ShutdownMessage implements GroupMessage, Externalizable {
   public void execute(HostID var1) {
      MemberManager.theOne().shutdown(var1);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
   }
}
