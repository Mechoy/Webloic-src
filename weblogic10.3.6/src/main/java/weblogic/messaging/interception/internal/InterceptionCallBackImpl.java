package weblogic.messaging.interception.internal;

import javax.xml.rpc.handler.MessageContext;
import weblogic.messaging.interception.exceptions.InterceptionProcessorException;
import weblogic.messaging.interception.interfaces.CarrierCallBack;
import weblogic.messaging.interception.interfaces.InterceptionCallBack;

public class InterceptionCallBackImpl implements InterceptionCallBack {
   private CarrierCallBack callBack;
   private Association association;
   private boolean processOnly;
   private MessageContext context;

   InterceptionCallBackImpl(CarrierCallBack var1, Association var2, boolean var3, MessageContext var4) {
      this.callBack = var1;
      this.association = var2;
      this.processOnly = var3;
      this.context = var4;
   }

   public final void onCallBack(boolean var1) {
      if (this.processOnly) {
         this.association.updateAsyncMeessagesCount(true);
         this.callBack.onCallBack(true);
      } else {
         this.association.updateAsyncMeessagesCount(var1);
         this.callBack.onCallBack(var1);
      }

   }

   public final void onException(InterceptionProcessorException var1) {
      this.association.updateAsyncMeessagesCount(false);
      this.callBack.onException(var1);
   }
}
