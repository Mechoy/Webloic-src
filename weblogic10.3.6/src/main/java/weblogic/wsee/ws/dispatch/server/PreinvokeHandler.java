package weblogic.wsee.ws.dispatch.server;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.component.Component;
import weblogic.wsee.component.ComponentException;
import weblogic.wsee.handler.InvocationException;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.ws.WsSkel;
import weblogic.wsee.ws.dispatch.Dispatcher;

public class PreinvokeHandler extends GenericHandler implements WLHandler {
   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Dispatcher var3 = var2.getDispatcher();
      QName var4 = var3.getWsMethod().getOperationName();
      Component var5 = ((WsSkel)var3.getWsPort().getEndpoint()).getComponent();
      SecurityHelper.setSubject(var1);

      try {
         var5.preinvoke(var4.getLocalPart(), var1);
      } catch (ComponentException var7) {
         throw new InvocationException("Failed to preinvoke end component " + var7, var7);
      }

      var2.setProperty("weblogic.wsee.preinvoked", "true");
      return true;
   }

   public boolean handleResponse(MessageContext var1) {
      boolean var2 = this.finish(var1);
      SecurityHelper.resetSubject(var1);
      return var2;
   }

   public boolean handleClosure(MessageContext var1) {
      boolean var2 = this.finish(var1);
      SecurityHelper.resetSubject(var1);
      return var2;
   }

   private boolean finish(MessageContext var1) {
      if (var1.getProperty("weblogic.wsee.preinvoked") != null) {
         WlMessageContext var2 = WlMessageContext.narrow(var1);
         Dispatcher var3 = var2.getDispatcher();
         QName var4 = var3.getWsMethod().getOperationName();
         Component var5 = ((WsSkel)var3.getWsPort().getEndpoint()).getComponent();

         try {
            var5.postinvoke(var4.getLocalPart(), var1);
         } catch (ComponentException var7) {
            throw new InvocationException("Failed to postinvoke end component " + var7, var7);
         }
      }

      return true;
   }

   public boolean handleFault(MessageContext var1) {
      boolean var2 = this.finish(var1);
      SecurityHelper.resetSubject(var1);
      return var2;
   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
