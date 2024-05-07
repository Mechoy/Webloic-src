package weblogic.net.http;

import java.net.ProtocolException;

public final class HttpUnauthorizedException extends ProtocolException {
   private static final long serialVersionUID = -8962090006498923363L;

   public HttpUnauthorizedException(String var1) {
      super(var1);
   }

   public HttpUnauthorizedException() {
   }
}
