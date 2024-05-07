package weblogic.wsee.mtom.internal;

import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import weblogic.utils.Debug;

public class MtomXopClientHandler extends MtomXopHandler {
   public static final String VERBOSE_PROPERTY = "weblogic.wsee.mtom.internal.MtomXopClientHandler";
   public static final boolean VERBOSE = Boolean.getBoolean("weblogic.wsee.mtom.internal.MtomXopClientHandler");

   public boolean handleRequest(MessageContext var1) {
      if (VERBOSE) {
         Debug.say(this.getClass() + ".handleRequest");
      }

      if (!(var1 instanceof SOAPMessageContext)) {
         return true;
      } else {
         SOAPMessageContext var2 = (SOAPMessageContext)var1;
         if (!this.isMtomEnable(var2)) {
            return true;
         } else {
            try {
               if (var2.getProperty("weblogic.wsee.xop.normal") == null) {
                  var2.setProperty("weblogic.wsee.xop.normal", "encrypt");
               }

               this.processXOP(var2);
               return true;
            } catch (Exception var4) {
               throw new JAXRPCException(var4);
            }
         }
      }
   }

   public boolean handleResponse(MessageContext var1) {
      return true;
   }
}
