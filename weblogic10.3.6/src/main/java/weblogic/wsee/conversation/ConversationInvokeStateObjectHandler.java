package weblogic.wsee.conversation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.store.ObjectHandler;
import weblogic.wsee.addressing.EndpointReference;
import weblogic.wsee.util.Verbose;

public class ConversationInvokeStateObjectHandler implements ObjectHandler {
   private static final boolean verbose = Verbose.isVerbose(ConversationInvokeStateObjectHandler.class);

   public void writeObject(ObjectOutput var1, Object var2) throws IOException {
      var1.writeUTF("9.2");
      if (var2 instanceof String) {
         var1.writeInt(0);
         var1.writeUTF((String)var2);
      } else if (var2 instanceof ConversationInvokeState) {
         var1.writeInt(1);
         ConversationInvokeState var3 = (ConversationInvokeState)var2;
         var1.writeLong(var3.getAbsTimeout());
         var1.writeBoolean(var3.isRmState());
         if (var3.isRmState()) {
            var1.writeUTF(var3.getSeqId());
         }

         EndpointReference var4 = var3.getEpr();
         if (var4 != null) {
            var1.writeInt(1);
            var1.writeObject(var4);
         } else {
            var1.writeInt(0);
         }

         String var5 = var3.getAppVersion();
         if (var5 != null) {
            var1.writeInt(1);
            var1.writeUTF(var5);
         } else {
            var1.writeInt(0);
         }

         String var6 = var3.getStoreConfig();
         if (var6 != null) {
            var1.writeInt(1);
            var1.writeUTF(var6);
         } else {
            var1.writeInt(0);
         }

         String var7 = var3.getAppName();
         if (var7 != null) {
            var1.writeInt(1);
            var1.writeUTF(var7);
         } else {
            var1.writeInt(0);
         }

      }
   }

   public Object readObject(ObjectInput var1) throws ClassNotFoundException, IOException {
      String var2 = var1.readUTF();
      if (!"9.2".equals(var2)) {
         throw new IOException("Wrong version, expected: 9.2 actual: " + var2);
      } else {
         int var3 = var1.readInt();
         if (var3 == 0) {
            return var1.readUTF();
         } else {
            ConversationInvokeState var4 = new ConversationInvokeState();
            long var5 = var1.readLong();
            var4.setAbsTimeout(var5);
            boolean var7 = var1.readBoolean();
            var4.setRmState(var7);
            if (var7) {
               String var8 = var1.readUTF();
               var4.setSeqId(var8);
            }

            int var15 = var1.readInt();
            if (var15 == 1) {
               EndpointReference var9 = (EndpointReference)var1.readObject();
               var4.setEpr(var9);
            }

            int var16 = var1.readInt();
            if (var16 == 1) {
               String var10 = var1.readUTF();
               var4.setAppVersion(var10);
            }

            int var13 = var1.readInt();
            if (var13 == 1) {
               String var11 = var1.readUTF();
               var4.setStoreConfig(var11);
            }

            int var14 = var1.readInt();
            if (var14 == 1) {
               String var12 = var1.readUTF();
               var4.setAppName(var12);
            }

            return var4;
         }
      }
   }
}
