package weblogic.cluster.migration.example;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class MigratableVariableImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$java$io$Serializable;
   // $FF: synthetic field
   private static Class class$java$lang$String;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      Serializable var12;
      switch (var1) {
         case 0:
            var12 = ((MigratableVariable)var4).get();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var12, class$java$io$Serializable == null ? (class$java$io$Serializable = class$("java.io.Serializable")) : class$java$io$Serializable);
               break;
            } catch (IOException var11) {
               throw new MarshalException("error marshalling return", var11);
            }
         case 1:
            try {
               MsgInput var6 = var2.getMsgInput();
               var12 = (Serializable)var6.readObject(class$java$io$Serializable == null ? (class$java$io$Serializable = class$("java.io.Serializable")) : class$java$io$Serializable);
            } catch (IOException var9) {
               throw new UnmarshalException("error unmarshalling arguments", var9);
            } catch (ClassNotFoundException var10) {
               throw new UnmarshalException("error unmarshalling arguments", var10);
            }

            ((MigratableVariable)var4).set(var12);
            this.associateResponseData(var2, var3);
            break;
         case 2:
            String var5 = ((MigratableVariable)var4).whereAmI();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var5, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var8) {
               throw new MarshalException("error marshalling return", var8);
            }
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }

      return var3;
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public Object invoke(int var1, Object[] var2, Object var3) throws Exception {
      switch (var1) {
         case 0:
            return ((MigratableVariable)var3).get();
         case 1:
            ((MigratableVariable)var3).set((Serializable)var2[0]);
            return null;
         case 2:
            return ((MigratableVariable)var3).whereAmI();
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
