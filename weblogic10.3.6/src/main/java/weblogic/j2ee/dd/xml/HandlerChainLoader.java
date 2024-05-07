package weblogic.j2ee.dd.xml;

import com.bea.xml.XmlException;
import com.sun.java.xml.ns.javaee.HandlerChainType;
import com.sun.java.xml.ns.javaee.HandlerChainsDocument;
import com.sun.java.xml.ns.javaee.HandlerChainsDocument.Factory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.jws.HandlerChain;

public class HandlerChainLoader {
   private final HandlerChainType[] handlerChains;

   public HandlerChainLoader(HandlerChain var1, Class var2) throws XmlException, IOException {
      this.handlerChains = this.buildHandlerChains(var1, var2);
   }

   private HandlerChainType[] buildHandlerChains(HandlerChain var1, Class var2) throws XmlException, IOException {
      HandlerChainType[] var3 = null;
      if (var1 != null && var1.file() != null && var1.file().length() > 0) {
         URL var4 = this.getURL(var1.file(), var2);
         if (var4 != null) {
            HandlerChainsDocument var5 = Factory.parse(var4);
            var5.validate();
            var3 = var5.getHandlerChains().getHandlerChainArray();
         }
      }

      return var3;
   }

   private boolean isRelativeUrl(String var1) {
      if (var1.indexOf(58) > 0) {
         return false;
      } else {
         return !var1.startsWith("/");
      }
   }

   private URL getURL(String var1, Class var2) throws MalformedURLException {
      URL var3 = null;
      if (this.isRelativeUrl(var1)) {
         var3 = var2.getResource(var1);
      } else {
         var3 = new URL(var1);
      }

      return var3;
   }

   public HandlerChainType[] getHandlerChains() {
      return this.handlerChains;
   }
}
