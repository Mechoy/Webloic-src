package weblogic.messaging.dispatcher;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import weblogic.rmi.extensions.AsyncResult;
import weblogic.rmi.extensions.server.FutureResponse;
import weblogic.rmi.internal.AsyncResultImpl;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class FastDispatcherImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$weblogic$messaging$dispatcher$Response;
   // $FF: synthetic field
   private static Class class$weblogic$messaging$dispatcher$Request;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      Request var5;
      AsyncResultImpl var34;
      switch (var1) {
         case 0:
            try {
               MsgInput var7 = var2.getMsgInput();
               var5 = (Request)var7.readObject(class$weblogic$messaging$dispatcher$Request == null ? (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")) : class$weblogic$messaging$dispatcher$Request);
               var34 = new AsyncResultImpl(var2, var3);
            } catch (IOException var32) {
               throw new UnmarshalException("error unmarshalling arguments", var32);
            } catch (ClassNotFoundException var33) {
               throw new UnmarshalException("error unmarshalling arguments", var33);
            }

            ((FastDispatcherImpl)var4).dispatchAsyncFuture(var5, var34, (FutureResponse)var3);
            break;
         case 1:
            try {
               MsgInput var8 = var2.getMsgInput();
               var5 = (Request)var8.readObject(class$weblogic$messaging$dispatcher$Request == null ? (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")) : class$weblogic$messaging$dispatcher$Request);
               var34 = new AsyncResultImpl(var2, var3);
            } catch (IOException var30) {
               throw new UnmarshalException("error unmarshalling arguments", var30);
            } catch (ClassNotFoundException var31) {
               throw new UnmarshalException("error unmarshalling arguments", var31);
            }

            ((FastDispatcherImpl)var4).dispatchAsyncTranFuture(var5, var34, (FutureResponse)var3);
            break;
         case 2:
            try {
               MsgInput var6 = var2.getMsgInput();
               var5 = (Request)var6.readObject(class$weblogic$messaging$dispatcher$Request == null ? (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")) : class$weblogic$messaging$dispatcher$Request);
            } catch (IOException var28) {
               throw new UnmarshalException("error unmarshalling arguments", var28);
            } catch (ClassNotFoundException var29) {
               throw new UnmarshalException("error unmarshalling arguments", var29);
            }

            ((DispatcherOneWay)var4).dispatchOneWay(var5);
            this.associateResponseData(var2, var3);
            break;
         case 3:
            int var35;
            try {
               MsgInput var10 = var2.getMsgInput();
               var5 = (Request)var10.readObject(class$weblogic$messaging$dispatcher$Request == null ? (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")) : class$weblogic$messaging$dispatcher$Request);
               var35 = var10.readInt();
            } catch (IOException var26) {
               throw new UnmarshalException("error unmarshalling arguments", var26);
            } catch (ClassNotFoundException var27) {
               throw new UnmarshalException("error unmarshalling arguments", var27);
            }

            ((DispatcherOneWay)var4).dispatchOneWayWithId(var5, var35);
            this.associateResponseData(var2, var3);
            break;
         case 4:
            try {
               MsgInput var9 = var2.getMsgInput();
               var5 = (Request)var9.readObject(class$weblogic$messaging$dispatcher$Request == null ? (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")) : class$weblogic$messaging$dispatcher$Request);
            } catch (IOException var24) {
               throw new UnmarshalException("error unmarshalling arguments", var24);
            } catch (ClassNotFoundException var25) {
               throw new UnmarshalException("error unmarshalling arguments", var25);
            }

            ((FastDispatcherImpl)var4).dispatchSyncFuture(var5, (FutureResponse)var3);
            break;
         case 5:
            try {
               MsgInput var11 = var2.getMsgInput();
               var5 = (Request)var11.readObject(class$weblogic$messaging$dispatcher$Request == null ? (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")) : class$weblogic$messaging$dispatcher$Request);
            } catch (IOException var22) {
               throw new UnmarshalException("error unmarshalling arguments", var22);
            } catch (ClassNotFoundException var23) {
               throw new UnmarshalException("error unmarshalling arguments", var23);
            }

            ((FastDispatcherImpl)var4).dispatchSyncNoTranFuture(var5, (FutureResponse)var3);
            break;
         case 6:
            try {
               MsgInput var12 = var2.getMsgInput();
               var5 = (Request)var12.readObject(class$weblogic$messaging$dispatcher$Request == null ? (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")) : class$weblogic$messaging$dispatcher$Request);
            } catch (IOException var20) {
               throw new UnmarshalException("error unmarshalling arguments", var20);
            } catch (ClassNotFoundException var21) {
               throw new UnmarshalException("error unmarshalling arguments", var21);
            }

            ((FastDispatcherImpl)var4).dispatchSyncTranFuture(var5, (FutureResponse)var3);
            break;
         case 7:
            int var13;
            try {
               MsgInput var14 = var2.getMsgInput();
               var5 = (Request)var14.readObject(class$weblogic$messaging$dispatcher$Request == null ? (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")) : class$weblogic$messaging$dispatcher$Request);
               var13 = var14.readInt();
            } catch (IOException var18) {
               throw new UnmarshalException("error unmarshalling arguments", var18);
            } catch (ClassNotFoundException var19) {
               throw new UnmarshalException("error unmarshalling arguments", var19);
            }

            Response var15 = ((DispatcherRemote)var4).dispatchSyncTranFutureWithId(var5, var13);
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var15, class$weblogic$messaging$dispatcher$Response == null ? (class$weblogic$messaging$dispatcher$Response = class$("weblogic.messaging.dispatcher.Response")) : class$weblogic$messaging$dispatcher$Response);
               break;
            } catch (IOException var17) {
               throw new MarshalException("error marshalling return", var17);
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
            ((DispatcherRemote)var3).dispatchAsyncFuture((Request)var2[0], (AsyncResult)var2[1]);
            return null;
         case 1:
            ((DispatcherRemote)var3).dispatchAsyncTranFuture((Request)var2[0], (AsyncResult)var2[1]);
            return null;
         case 2:
            ((DispatcherOneWay)var3).dispatchOneWay((Request)var2[0]);
            return null;
         case 3:
            ((DispatcherOneWay)var3).dispatchOneWayWithId((Request)var2[0], (Integer)var2[1]);
            return null;
         case 4:
            return ((DispatcherRemote)var3).dispatchSyncFuture((Request)var2[0]);
         case 5:
            return ((DispatcherRemote)var3).dispatchSyncNoTranFuture((Request)var2[0]);
         case 6:
            return ((DispatcherRemote)var3).dispatchSyncTranFuture((Request)var2[0]);
         case 7:
            return ((DispatcherRemote)var3).dispatchSyncTranFutureWithId((Request)var2[0], (Integer)var2[1]);
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
