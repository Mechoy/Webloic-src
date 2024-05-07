package weblogic.webservice.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;

/** @deprecated */
public abstract class WebServiceContext {
   private static Map contexts = Collections.synchronizedMap(new HashMap());

   WebServiceContext() {
   }

   public static WebServiceContext currentContext() throws ContextNotFoundException {
      WebServiceContext var0 = (WebServiceContext)contexts.get(Thread.currentThread());
      if (var0 == null) {
         throw new ContextNotFoundException("unable to find context for current thread");
      } else {
         return var0;
      }
   }

   public static void register(WebServiceContext var0) {
      if (var0 == null) {
         contexts.remove(Thread.currentThread());
      } else {
         contexts.put(Thread.currentThread(), var0);
      }

   }

   public abstract WebServiceHeader getHeader();

   public abstract WebServiceSession getSession();

   public abstract SOAPMessageContext getLastMessageContext();
}
