package weblogic.management.internal;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import weblogic.management.configuration.RemoteEnumeration;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.OutboundResponse;

public final class RemoteEnumerationImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class array$Ljava$lang$Object;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      switch (var1) {
         case 0:
            ((RemoteEnumeration)var4).close();
            this.associateResponseData(var2, var3);
            break;
         case 1:
            Object[] var5 = ((RemoteEnumeration)var4).getNextBatch();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var5, array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
               break;
            } catch (IOException var7) {
               throw new MarshalException("error marshalling return", var7);
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
            ((RemoteEnumeration)var3).close();
            return null;
         case 1:
            return ((RemoteEnumeration)var3).getNextBatch();
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
