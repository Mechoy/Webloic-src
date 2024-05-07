package weblogic.messaging.dispatcher;

import java.rmi.Remote;
import javax.rmi.CORBA.Tie;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.CORBA.portable.UnknownException;
import org.omg.CORBA_2_3.portable.ObjectImpl;
import weblogic.rmi.extensions.AsyncResult;

public class _DispatcherImpl_Tie extends ObjectImpl implements Tie {
   private DispatcherImpl target = null;
   private static final String[] _type_ids = new String[]{"RMI:weblogic.messaging.dispatcher.DispatcherImpl:0000000000000000", "RMI:weblogic.messaging.dispatcher.DispatcherRemote:0000000000000000", "RMI:weblogic.messaging.dispatcher.DispatcherOneWay:0000000000000000"};
   // $FF: synthetic field
   static Class class$weblogic$messaging$dispatcher$Request;
   // $FF: synthetic field
   static Class class$weblogic$messaging$dispatcher$Response;
   // $FF: synthetic field
   static Class class$weblogic$rmi$extensions$AsyncResult;

   public String[] _ids() {
      return _type_ids;
   }

   public OutputStream _invoke(String var1, InputStream var2, ResponseHandler var3) throws SystemException {
      try {
         org.omg.CORBA_2_3.portable.InputStream var4 = (org.omg.CORBA_2_3.portable.InputStream)var2;
         Request var5;
         int var6;
         Response var11;
         AsyncResult var12;
         org.omg.CORBA_2_3.portable.OutputStream var13;
         OutputStream var14;
         switch (var1.length()) {
            case 14:
               if (var1.equals("dispatchOneWay")) {
                  var5 = (Request)var4.read_value(class$weblogic$messaging$dispatcher$Request != null ? class$weblogic$messaging$dispatcher$Request : (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")));
                  this.target.dispatchOneWay(var5);
                  OutputStream var15 = var3.createReply();
                  return var15;
               }
            case 18:
               if (var1.equals("dispatchSyncFuture")) {
                  var5 = (Request)var4.read_value(class$weblogic$messaging$dispatcher$Request != null ? class$weblogic$messaging$dispatcher$Request : (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")));
                  var11 = this.target.dispatchSyncFuture(var5);
                  var13 = (org.omg.CORBA_2_3.portable.OutputStream)var3.createReply();
                  var13.write_value(var11, class$weblogic$messaging$dispatcher$Response != null ? class$weblogic$messaging$dispatcher$Response : (class$weblogic$messaging$dispatcher$Response = class$("weblogic.messaging.dispatcher.Response")));
                  return var13;
               }
            case 19:
               if (var1.equals("dispatchAsyncFuture")) {
                  var5 = (Request)var4.read_value(class$weblogic$messaging$dispatcher$Request != null ? class$weblogic$messaging$dispatcher$Request : (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")));
                  var12 = (AsyncResult)var4.read_value(class$weblogic$rmi$extensions$AsyncResult != null ? class$weblogic$rmi$extensions$AsyncResult : (class$weblogic$rmi$extensions$AsyncResult = class$("weblogic.rmi.extensions.AsyncResult")));
                  this.target.dispatchAsyncFuture(var5, var12);
                  var14 = var3.createReply();
                  return var14;
               }
            case 20:
               if (var1.equals("dispatchOneWayWithId")) {
                  var5 = (Request)var4.read_value(class$weblogic$messaging$dispatcher$Request != null ? class$weblogic$messaging$dispatcher$Request : (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")));
                  var6 = var4.read_long();
                  this.target.dispatchOneWayWithId(var5, var6);
                  var14 = var3.createReply();
                  return var14;
               }
            case 22:
               if (var1.equals("dispatchSyncTranFuture")) {
                  var5 = (Request)var4.read_value(class$weblogic$messaging$dispatcher$Request != null ? class$weblogic$messaging$dispatcher$Request : (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")));
                  var11 = this.target.dispatchSyncTranFuture(var5);
                  var13 = (org.omg.CORBA_2_3.portable.OutputStream)var3.createReply();
                  var13.write_value(var11, class$weblogic$messaging$dispatcher$Response != null ? class$weblogic$messaging$dispatcher$Response : (class$weblogic$messaging$dispatcher$Response = class$("weblogic.messaging.dispatcher.Response")));
                  return var13;
               }
            case 23:
               if (var1.equals("dispatchAsyncTranFuture")) {
                  var5 = (Request)var4.read_value(class$weblogic$messaging$dispatcher$Request != null ? class$weblogic$messaging$dispatcher$Request : (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")));
                  var12 = (AsyncResult)var4.read_value(class$weblogic$rmi$extensions$AsyncResult != null ? class$weblogic$rmi$extensions$AsyncResult : (class$weblogic$rmi$extensions$AsyncResult = class$("weblogic.rmi.extensions.AsyncResult")));
                  this.target.dispatchAsyncTranFuture(var5, var12);
                  var14 = var3.createReply();
                  return var14;
               }
            case 24:
               if (var1.equals("dispatchSyncNoTranFuture")) {
                  var5 = (Request)var4.read_value(class$weblogic$messaging$dispatcher$Request != null ? class$weblogic$messaging$dispatcher$Request : (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")));
                  var11 = this.target.dispatchSyncNoTranFuture(var5);
                  var13 = (org.omg.CORBA_2_3.portable.OutputStream)var3.createReply();
                  var13.write_value(var11, class$weblogic$messaging$dispatcher$Response != null ? class$weblogic$messaging$dispatcher$Response : (class$weblogic$messaging$dispatcher$Response = class$("weblogic.messaging.dispatcher.Response")));
                  return var13;
               }
            case 28:
               if (var1.equals("dispatchSyncTranFutureWithId")) {
                  var5 = (Request)var4.read_value(class$weblogic$messaging$dispatcher$Request != null ? class$weblogic$messaging$dispatcher$Request : (class$weblogic$messaging$dispatcher$Request = class$("weblogic.messaging.dispatcher.Request")));
                  var6 = var4.read_long();
                  Response var7 = this.target.dispatchSyncTranFutureWithId(var5, var6);
                  org.omg.CORBA_2_3.portable.OutputStream var8 = (org.omg.CORBA_2_3.portable.OutputStream)var3.createReply();
                  var8.write_value(var7, class$weblogic$messaging$dispatcher$Response != null ? class$weblogic$messaging$dispatcher$Response : (class$weblogic$messaging$dispatcher$Response = class$("weblogic.messaging.dispatcher.Response")));
                  return var8;
               }
            case 15:
            case 16:
            case 17:
            case 21:
            case 25:
            case 26:
            case 27:
            default:
               throw new BAD_OPERATION();
         }
      } catch (SystemException var9) {
         throw var9;
      } catch (Throwable var10) {
         throw new UnknownException(var10);
      }
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         return Class.forName(var0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   public void deactivate() {
      this._orb().disconnect(this);
      this._set_delegate((Delegate)null);
      this.target = null;
   }

   public Remote getTarget() {
      return this.target;
   }

   public ORB orb() {
      return this._orb();
   }

   public void orb(ORB var1) {
      var1.connect(this);
   }

   public void setTarget(Remote var1) {
      this.target = (DispatcherImpl)var1;
   }

   public Object thisObject() {
      return this;
   }
}
