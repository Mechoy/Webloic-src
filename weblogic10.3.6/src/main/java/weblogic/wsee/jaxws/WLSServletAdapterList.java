package weblogic.wsee.jaxws;

import com.sun.xml.ws.api.server.PortAddressResolver;
import com.sun.xml.ws.api.server.WSEndpoint;
import com.sun.xml.ws.transport.http.servlet.ServletAdapter;
import com.sun.xml.ws.transport.http.servlet.ServletAdapterList;
import javax.xml.namespace.QName;

public class WLSServletAdapterList extends ServletAdapterList {
   private static final ThreadLocal<AddressPair> par = new ThreadLocal();

   protected ServletAdapter createHttpAdapter(String var1, String var2, WSEndpoint<?> var3) {
      return new WLSServletAdapter(var1, var2, var3, this);
   }

   public PortAddressResolver createPortAddressResolver(String var1) {
      AddressPair var2 = (AddressPair)par.get();
      return var2 != null && var1.equals(var2.clearAddress) ? new WLSPortAddressResolver(super.createPortAddressResolver(var1), super.createPortAddressResolver(var2.sslAddress)) : new WLSPortAddressResolver(super.createPortAddressResolver(var1));
   }

   public void registerPortAddressResolver(String var1, String var2) {
      par.set(new AddressPair(var1, var2));
   }

   public void clearPortAddressResolver() {
      par.remove();
   }

   static class WLSPortAddressResolver extends PortAddressResolver {
      private final PortAddressResolver docDelegate;
      private final PortAddressResolver serviceDelegate;

      public WLSPortAddressResolver(PortAddressResolver var1, PortAddressResolver var2) {
         this.docDelegate = var1;
         this.serviceDelegate = var2;
      }

      public WLSPortAddressResolver(PortAddressResolver var1) {
         this.docDelegate = this.serviceDelegate = var1;
      }

      public String getAddressFor(QName var1, String var2) {
         return this.serviceDelegate.getAddressFor(var1, var2);
      }

      public PortAddressResolver getDocumentPortAddressResolver() {
         return this.docDelegate;
      }
   }

   private static class AddressPair {
      public String clearAddress;
      public String sslAddress;

      public AddressPair(String var1, String var2) {
         this.clearAddress = var1;
         this.sslAddress = var2;
      }
   }
}
