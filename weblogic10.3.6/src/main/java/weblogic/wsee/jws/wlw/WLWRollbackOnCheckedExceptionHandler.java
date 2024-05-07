package weblogic.wsee.jws.wlw;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.util.Verbose;

public class WLWRollbackOnCheckedExceptionHandler extends GenericHandler {
   private static final boolean verbose = Verbose.isVerbose(WLWRollbackOnCheckedExceptionHandler.class);

   public boolean handleRequest(MessageContext var1) {
      try {
         var1.setProperty("weblogic.jws.wlw.rollback_on_checked_exception", "True");
      } catch (IllegalArgumentException var3) {
      }

      return true;
   }

   public boolean handleResponse(MessageContext var1) {
      return this.handleFault(var1);
   }

   public boolean handleFault(MessageContext var1) {
      return true;
   }

   public boolean handleClosure(MessageContext var1) {
      return this.handleFault(var1);
   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
