package weblogic.wtc.jatmi;

import java.rmi.RemoteException;
import java.util.HashMap;

public class InvokeSvcDef implements InvokeSvc {
   private HashMap st = new HashMap();

   public void invoke(InvokeInfo var1, gwatmi var2) throws TPException {
      Reply var4 = null;
      TuxedoService var8 = (TuxedoService)this.st.get(var1.getServiceName());
      if (var8 == null) {
         throw new TPException(6);
      } else {
         TypedBuffer var5;
         int var6;
         int var7;
         try {
            var4 = var8.service(var1);
            var5 = var4.getReplyBuffer();
            var7 = 0;
            var6 = var4.gettpurcode();
         } catch (RemoteException var10) {
            throw new TPException(12, "Remote exception: " + var10);
         } catch (TPReplyException var11) {
            var5 = var11.getExceptionReply().getReplyBuffer();
            var7 = var11.gettperrno();
            var6 = var11.getExceptionReply().gettpurcode();
         } catch (TPException var12) {
            throw var12;
         }

         tfmh var3;
         if (var5 == null) {
            var3 = new tfmh(1);
         } else {
            tcm var9 = new tcm((short)0, new UserTcb(var5));
            var3 = new tfmh(var5.getHintIndex(), var9, 1);
         }

         var2.send_success_return(var1.getReqid(), var3, var7, var6, -1);
      }
   }

   public void advertise(String var1, TuxedoService var2) throws TPException {
      if (var1 == null) {
         throw new TPException(4, "null service name encounterd");
      } else if (var2 == null) {
         throw new TPException(4, "null function object encountered");
      } else {
         this.st.put(var1, var2);
      }
   }

   public void unadvertise(String var1) throws TPException {
      if (var1 == null) {
         throw new TPException(4, "null service name encountered");
      } else {
         this.st.remove(var1);
      }
   }

   public void shutdown() {
   }
}
