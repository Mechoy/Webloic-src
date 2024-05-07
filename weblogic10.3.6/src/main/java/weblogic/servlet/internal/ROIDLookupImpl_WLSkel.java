package weblogic.servlet.internal;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import weblogic.cluster.replication.ROID;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class ROIDLookupImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$weblogic$cluster$replication$ROID;
   // $FF: synthetic field
   private static Class array$Lweblogic$cluster$replication$ROID;
   // $FF: synthetic field
   private static Class array$Ljava$lang$Object;
   // $FF: synthetic field
   private static Class array$J;
   // $FF: synthetic field
   private static Class class$java$lang$String;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      switch (var1) {
         case 0:
            String var22;
            String var24;
            String var25;
            try {
               MsgInput var8 = var2.getMsgInput();
               var22 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var24 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var25 = (String)var8.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var19) {
               throw new UnmarshalException("error unmarshalling arguments", var19);
            } catch (ClassNotFoundException var20) {
               throw new UnmarshalException("error unmarshalling arguments", var20);
            }

            ROID var26 = ((ROIDLookup)var4).lookupROID(var22, var24, var25);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var26, class$weblogic$cluster$replication$ROID == null ? (class$weblogic$cluster$replication$ROID = class$("weblogic.cluster.replication.ROID")) : class$weblogic$cluster$replication$ROID);
               break;
            } catch (IOException var18) {
               throw new MarshalException("error marshalling return", var18);
            }
         case 1:
            ROID var21;
            Object[] var23;
            try {
               MsgInput var7 = var2.getMsgInput();
               var21 = (ROID)var7.readObject(class$weblogic$cluster$replication$ROID == null ? (class$weblogic$cluster$replication$ROID = class$("weblogic.cluster.replication.ROID")) : class$weblogic$cluster$replication$ROID);
               var23 = (Object[])var7.readObject(array$Ljava$lang$Object == null ? (array$Ljava$lang$Object = class$("[Ljava.lang.Object;")) : array$Ljava$lang$Object);
            } catch (IOException var16) {
               throw new UnmarshalException("error unmarshalling arguments", var16);
            } catch (ClassNotFoundException var17) {
               throw new UnmarshalException("error unmarshalling arguments", var17);
            }

            ((ROIDLookup)var4).unregister(var21, var23);
            this.associateResponseData(var2, var3);
            break;
         case 2:
            ROID[] var5;
            long[] var6;
            long var9;
            String var11;
            try {
               MsgInput var12 = var2.getMsgInput();
               var5 = (ROID[])var12.readObject(array$Lweblogic$cluster$replication$ROID == null ? (array$Lweblogic$cluster$replication$ROID = class$("[Lweblogic.cluster.replication.ROID;")) : array$Lweblogic$cluster$replication$ROID);
               var6 = (long[])var12.readObject(array$J == null ? (array$J = class$("[J")) : array$J);
               var9 = var12.readLong();
               var11 = (String)var12.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var14) {
               throw new UnmarshalException("error unmarshalling arguments", var14);
            } catch (ClassNotFoundException var15) {
               throw new UnmarshalException("error unmarshalling arguments", var15);
            }

            ((ROIDLookup)var4).updateLastAccessTimes(var5, var6, var9, var11);
            this.associateResponseData(var2, var3);
            break;
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
            return ((ROIDLookup)var3).lookupROID((String)var2[0], (String)var2[1], (String)var2[2]);
         case 1:
            ((ROIDLookup)var3).unregister((ROID)var2[0], (Object[])var2[1]);
            return null;
         case 2:
            ((ROIDLookup)var3).updateLastAccessTimes((ROID[])var2[0], (long[])var2[1], (Long)var2[2], (String)var2[3]);
            return null;
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
