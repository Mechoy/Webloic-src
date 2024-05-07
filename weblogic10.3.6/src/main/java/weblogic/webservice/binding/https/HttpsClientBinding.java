package weblogic.webservice.binding.https;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import javax.xml.soap.SOAPException;
import weblogic.webservice.binding.BindingInfo;
import weblogic.webservice.binding.soap.HttpClientBinding;

/** @deprecated */
public class HttpsClientBinding extends HttpClientBinding {
   private HttpsBindingInfo bindingInfo;
   private static final String HTTPS_SOCKET_SHARING_TIMEOUT = "https.sharedsocket.timeout";

   public HttpsClientBinding() throws SOAPException {
   }

   public void init(BindingInfo var1) throws IOException {
      super.init(var1);
      if (!(var1 instanceof HttpsBindingInfo)) {
         throw new IllegalArgumentException("info should be HttpsBindingInfo");
      } else {
         this.bindingInfo = (HttpsBindingInfo)var1;
         String var2 = System.getProperty("https.sharedsocket.timeout");
         if (var2 != null) {
            this.bindingInfo.setSharedSocketTimeout((long)Integer.parseInt(var2));
         }

      }
   }

   protected Socket createSocket(String var1, int var2) throws IOException {
      Socket var3 = this.bindingInfo.getSSLAdapter().createSocket(var1, var2);
      var3.setTcpNoDelay(true);
      int var4 = this.getBindingInfo().getTimeout();
      if (var4 > -1) {
         var3.setSoTimeout(var4 * 1000);
      }

      if (!this.bindingInfo.getSSLSocketPooling()) {
         this.bindingInfo.setSSLSecureSocket(var3, var1, var2);
      }

      return var3;
   }

   protected Socket createSocket(URL var1) throws IOException {
      if (this.bindingInfo.getSSLSocketPooling()) {
         return super.createSocket(var1);
      } else {
         Socket var2 = this.bindingInfo.getSSLSecureSocket(var1.getHost(), this.getPort(var1));
         return var2 != null && this.bindingInfo.getSocketSharing() ? var2 : this.createSocket(var1.getHost(), this.getPort(var1));
      }
   }

   protected void releaseSocket() {
      if (this.bindingInfo.getSocketSharing()) {
         this.bindingInfo.setSSLSecureSocketTimeOut();
      } else {
         if (this.bindingInfo.getSSLSocketPooling()) {
            super.releaseSocket();
         } else {
            this.bindingInfo.closeSharedSocket();
         }

      }
   }

   private int getPort(URL var1) {
      return var1.getPort() == -1 ? 443 : var1.getPort();
   }
}
