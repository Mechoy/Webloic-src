package weblogic.net.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.URL;

/** @deprecated */
public final class SOAPHttpURLConnection extends HttpURLConnection {
   public SOAPHttpURLConnection(URL var1) {
      this(var1, (Proxy)null);
   }

   public SOAPHttpURLConnection(URL var1, Proxy var2) {
      super(var1, var2);
   }

   public InputStream getInputStream() throws IOException {
      try {
         return super.getInputStream();
      } catch (FileNotFoundException var2) {
         if (this.getResponseCode() == 500) {
            return this.http.getInputStream();
         } else {
            throw var2;
         }
      }
   }
}
