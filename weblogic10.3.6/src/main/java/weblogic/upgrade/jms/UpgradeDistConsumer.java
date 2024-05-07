package weblogic.upgrade.jms;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.jms.common.JMSUtilities;

public class UpgradeDistConsumer extends UpgradeConsumer {
   static final long serialVersionUID = 8321229324831192400L;
   private static final int EXTVERSION = 1;
   private static final int HAS_NAME = 2;
   private static final int VERSION_MASK = 13;
   private String remotePhysicalTopicName;

   public final String getRemotePhysicalTopicName() {
      return this.remotePhysicalTopicName;
   }

   public void writeExternal(ObjectOutput var1) {
      throw new AssertionError("writeExternal must not be called on upgrade objects");
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      int var2 = var1.readInt();
      int var3 = var2 & 13;
      if (var3 != 1) {
         throw JMSUtilities.versionIOException(var3, 1, 1);
      } else {
         if ((var2 & 2) != 0) {
            this.remotePhysicalTopicName = var1.readUTF();
         }

      }
   }
}
