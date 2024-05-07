package weblogic.server;

import java.io.IOException;
import java.rmi.MarshalException;
import java.rmi.UnmarshalException;
import weblogic.rmi.internal.Skeleton;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.MsgInput;
import weblogic.rmi.spi.OutboundResponse;

public final class RemoteLifeCycleOperationsImpl_WLSkel extends Skeleton {
   // $FF: synthetic field
   private static Class class$java$lang$String;

   public OutboundResponse invoke(int var1, InboundRequest var2, OutboundResponse var3, Object var4) throws Exception {
      int var5;
      boolean var6;
      String var18;
      switch (var1) {
         case 0:
            ((RemoteLifeCycleOperations)var4).forceShutdown();
            this.associateResponseData(var2, var3);
            break;
         case 1:
            ((RemoteLifeCycleOperations)var4).forceSuspend();
            this.associateResponseData(var2, var3);
            break;
         case 2:
            var18 = ((RemoteLifeCycleOperations)var4).getMiddlewareHome();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var18, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var17) {
               throw new MarshalException("error marshalling return", var17);
            }
         case 3:
            var18 = ((RemoteLifeCycleOperations)var4).getState();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var18, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var16) {
               throw new MarshalException("error marshalling return", var16);
            }
         case 4:
            var18 = ((RemoteLifeCycleOperations)var4).getWeblogicHome();
            this.associateResponseData(var2, var3);

            try {
               var3.getMsgOutput().writeObject(var18, class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               break;
            } catch (IOException var15) {
               throw new MarshalException("error marshalling return", var15);
            }
         case 5:
            ((RemoteLifeCycleOperations)var4).resume();
            this.associateResponseData(var2, var3);
            break;
         case 6:
            String var19;
            try {
               MsgInput var7 = var2.getMsgInput();
               var18 = (String)var7.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
               var19 = (String)var7.readObject(class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String);
            } catch (IOException var13) {
               throw new UnmarshalException("error unmarshalling arguments", var13);
            } catch (ClassNotFoundException var14) {
               throw new UnmarshalException("error unmarshalling arguments", var14);
            }

            ((RemoteLifeCycleOperations)var4).setState(var18, var19);
            this.associateResponseData(var2, var3);
            break;
         case 7:
            ((RemoteLifeCycleOperations)var4).shutdown();
            this.associateResponseData(var2, var3);
            break;
         case 8:
            try {
               MsgInput var8 = var2.getMsgInput();
               var5 = var8.readInt();
               var6 = var8.readBoolean();
            } catch (IOException var12) {
               throw new UnmarshalException("error unmarshalling arguments", var12);
            }

            ((RemoteLifeCycleOperations)var4).shutdown(var5, var6);
            this.associateResponseData(var2, var3);
            break;
         case 9:
            ((RemoteLifeCycleOperations)var4).suspend();
            this.associateResponseData(var2, var3);
            break;
         case 10:
            try {
               MsgInput var9 = var2.getMsgInput();
               var5 = var9.readInt();
               var6 = var9.readBoolean();
            } catch (IOException var11) {
               throw new UnmarshalException("error unmarshalling arguments", var11);
            }

            ((RemoteLifeCycleOperations)var4).suspend(var5, var6);
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
            ((RemoteLifeCycleOperations)var3).forceShutdown();
            return null;
         case 1:
            ((RemoteLifeCycleOperations)var3).forceSuspend();
            return null;
         case 2:
            return ((RemoteLifeCycleOperations)var3).getMiddlewareHome();
         case 3:
            return ((RemoteLifeCycleOperations)var3).getState();
         case 4:
            return ((RemoteLifeCycleOperations)var3).getWeblogicHome();
         case 5:
            ((RemoteLifeCycleOperations)var3).resume();
            return null;
         case 6:
            ((RemoteLifeCycleOperations)var3).setState((String)var2[0], (String)var2[1]);
            return null;
         case 7:
            ((RemoteLifeCycleOperations)var3).shutdown();
            return null;
         case 8:
            ((RemoteLifeCycleOperations)var3).shutdown((Integer)var2[0], (Boolean)var2[1]);
            return null;
         case 9:
            ((RemoteLifeCycleOperations)var3).suspend();
            return null;
         case 10:
            ((RemoteLifeCycleOperations)var3).suspend((Integer)var2[0], (Boolean)var2[1]);
            return null;
         default:
            throw new UnmarshalException("Method identifier [" + var1 + "] out of range");
      }
   }
}
