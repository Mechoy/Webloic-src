package weblogic.cluster.replication;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.HostID;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class ReplicationManager_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$weblogic$cluster$replication$Replicatable;
   // $FF: synthetic field
   private static Class class$weblogic$cluster$replication$AsyncBatch;
   // $FF: synthetic field
   private static Class class$weblogic$cluster$replication$ROID;
   // $FF: synthetic field
   private static Class array$Lweblogic$cluster$replication$ROID;
   // $FF: synthetic field
   private static Class class$java$io$Serializable;
   // $FF: synthetic field
   private static Class class$weblogic$cluster$replication$ReplicationManager$ROObject;
   // $FF: synthetic field
   private static Class class$java$lang$Object;
   // $FF: synthetic field
   private static Class class$weblogic$rmi$spi$HostID;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      ROID var5;
      int var12;
      Serializable var13;
      Object var14;
      ROID[] var37;
      Object var42;
      switch (var1) {
         case 0:
            int var38;
            HostID var39;
            ROID var41;
            Replicatable var43;
            try {
               MsgInput var9 = var2.getMsgInput();
               var39 = (HostID)var9.readObject(class$weblogic$rmi$spi$HostID == null ? (class$weblogic$rmi$spi$HostID = class$("weblogic.rmi.spi.HostID")) : class$weblogic$rmi$spi$HostID);
               var38 = var9.readInt();
               var41 = (ROID)var9.readObject(class$weblogic$cluster$replication$ROID == null ? (class$weblogic$cluster$replication$ROID = class$("weblogic.cluster.replication.ROID")) : class$weblogic$cluster$replication$ROID);
               var43 = (Replicatable)var9.readObject(class$weblogic$cluster$replication$Replicatable == null ? (class$weblogic$cluster$replication$Replicatable = class$("weblogic.cluster.replication.Replicatable")) : class$weblogic$cluster$replication$Replicatable);
            } catch (IOException var34) {
               throw new UnmarshalException("error unmarshalling arguments", var34);
            } catch (ClassNotFoundException var35) {
               throw new UnmarshalException("error unmarshalling arguments", var35);
            }

            Object var44 = ((ReplicationServicesInternal)var4).create(var39, var38, var41, var43);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var44, class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
               break;
            } catch (IOException var33) {
               throw new MarshalException("error marshalling return", var33);
            }
         case 1:
            try {
               MsgInput var6 = var2.getMsgInput();
               var5 = (ROID)var6.readObject(class$weblogic$cluster$replication$ROID == null ? (class$weblogic$cluster$replication$ROID = class$("weblogic.cluster.replication.ROID")) : class$weblogic$cluster$replication$ROID);
            } catch (IOException var31) {
               throw new UnmarshalException("error unmarshalling arguments", var31);
            } catch (ClassNotFoundException var32) {
               throw new UnmarshalException("error unmarshalling arguments", var32);
            }

            ReplicationManager.ROObject var40 = ((ReplicationServicesInternal)var4).fetch(var5);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var40, class$weblogic$cluster$replication$ReplicationManager$ROObject == null ? (class$weblogic$cluster$replication$ReplicationManager$ROObject = class$("weblogic.cluster.replication.ReplicationManager$ROObject")) : class$weblogic$cluster$replication$ReplicationManager$ROObject);
               break;
            } catch (IOException var30) {
               throw new MarshalException("error marshalling return", var30);
            }
         case 2:
            try {
               MsgInput var7 = var2.getMsgInput();
               var37 = (ROID[])var7.readObject(array$Lweblogic$cluster$replication$ROID == null ? (array$Lweblogic$cluster$replication$ROID = class$("[Lweblogic.cluster.replication.ROID;")) : array$Lweblogic$cluster$replication$ROID);
            } catch (IOException var28) {
               throw new UnmarshalException("error unmarshalling arguments", var28);
            } catch (ClassNotFoundException var29) {
               throw new UnmarshalException("error unmarshalling arguments", var29);
            }

            ((ReplicationServicesInternal)var4).remove(var37);
            this.associateResponseData(var2, var3);
            break;
         case 3:
            try {
               MsgInput var10 = var2.getMsgInput();
               var37 = (ROID[])var10.readObject(array$Lweblogic$cluster$replication$ROID == null ? (array$Lweblogic$cluster$replication$ROID = class$("[Lweblogic.cluster.replication.ROID;")) : array$Lweblogic$cluster$replication$ROID);
               var42 = (Object)var10.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var26) {
               throw new UnmarshalException("error unmarshalling arguments", var26);
            } catch (ClassNotFoundException var27) {
               throw new UnmarshalException("error unmarshalling arguments", var27);
            }

            ((ReplicationServicesInternal)var4).remove(var37, var42);
            this.associateResponseData(var2, var3);
            break;
         case 4:
            try {
               MsgInput var11 = var2.getMsgInput();
               var37 = (ROID[])var11.readObject(array$Lweblogic$cluster$replication$ROID == null ? (array$Lweblogic$cluster$replication$ROID = class$("[Lweblogic.cluster.replication.ROID;")) : array$Lweblogic$cluster$replication$ROID);
               var42 = (Object)var11.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var24) {
               throw new UnmarshalException("error unmarshalling arguments", var24);
            } catch (ClassNotFoundException var25) {
               throw new UnmarshalException("error unmarshalling arguments", var25);
            }

            ((ReplicationServicesInternal)var4).removeOneWay(var37, var42);
            this.associateResponseData(var2, var3);
            break;
         case 5:
            AsyncBatch var36;
            try {
               MsgInput var8 = var2.getMsgInput();
               var36 = (AsyncBatch)var8.readObject(class$weblogic$cluster$replication$AsyncBatch == null ? (class$weblogic$cluster$replication$AsyncBatch = class$("weblogic.cluster.replication.AsyncBatch")) : class$weblogic$cluster$replication$AsyncBatch);
            } catch (IOException var22) {
               throw new UnmarshalException("error unmarshalling arguments", var22);
            } catch (ClassNotFoundException var23) {
               throw new UnmarshalException("error unmarshalling arguments", var23);
            }

            ((ReplicationServicesInternal)var4).update(var36);
            this.associateResponseData(var2, var3);
            break;
         case 6:
            try {
               MsgInput var15 = var2.getMsgInput();
               var5 = (ROID)var15.readObject(class$weblogic$cluster$replication$ROID == null ? (class$weblogic$cluster$replication$ROID = class$("weblogic.cluster.replication.ROID")) : class$weblogic$cluster$replication$ROID);
               var12 = var15.readInt();
               var13 = (Serializable)var15.readObject(class$java$io$Serializable == null ? (class$java$io$Serializable = class$("java.io.Serializable")) : class$java$io$Serializable);
               var14 = (Object)var15.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var20) {
               throw new UnmarshalException("error unmarshalling arguments", var20);
            } catch (ClassNotFoundException var21) {
               throw new UnmarshalException("error unmarshalling arguments", var21);
            }

            ((ReplicationServicesInternal)var4).update(var5, var12, var13, var14);
            this.associateResponseData(var2, var3);
            break;
         case 7:
            try {
               MsgInput var16 = var2.getMsgInput();
               var5 = (ROID)var16.readObject(class$weblogic$cluster$replication$ROID == null ? (class$weblogic$cluster$replication$ROID = class$("weblogic.cluster.replication.ROID")) : class$weblogic$cluster$replication$ROID);
               var12 = var16.readInt();
               var13 = (Serializable)var16.readObject(class$java$io$Serializable == null ? (class$java$io$Serializable = class$("java.io.Serializable")) : class$java$io$Serializable);
               var14 = (Object)var16.readObject(class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object);
            } catch (IOException var18) {
               throw new UnmarshalException("error unmarshalling arguments", var18);
            } catch (ClassNotFoundException var19) {
               throw new UnmarshalException("error unmarshalling arguments", var19);
            }

            ((ReplicationServicesInternal)var4).updateOneWay(var5, var12, var13, var14);
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
            return ((ReplicationServicesInternal)var3).create((HostID)var2[0], (Integer)var2[1], (ROID)var2[2], (Replicatable)var2[3]);
         case 1:
            return ((ReplicationServicesInternal)var3).fetch((ROID)var2[0]);
         case 2:
            ((ReplicationServicesInternal)var3).remove((ROID[])var2[0]);
            return null;
         case 3:
            ((ReplicationServicesInternal)var3).remove((ROID[])var2[0], (Object)var2[1]);
            return null;
         case 4:
            ((ReplicationServicesInternal)var3).removeOneWay((ROID[])var2[0], (Object)var2[1]);
            return null;
         case 5:
            ((ReplicationServicesInternal)var3).update((AsyncBatch)var2[0]);
            return null;
         case 6:
            ((ReplicationServicesInternal)var3).update((ROID)var2[0], (Integer)var2[1], (Serializable)var2[2], (Object)var2[3]);
            return null;
         case 7:
            ((ReplicationServicesInternal)var3).updateOneWay((ROID)var2[0], (Integer)var2[1], (Serializable)var2[2], (Object)var2[3]);
            return null;
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
