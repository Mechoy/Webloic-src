package weblogic.servlet.internal;

public final class MalformedCookieHeaderException extends Exception {
   private static final long serialVersionUID = -2466048250134275216L;

   public MalformedCookieHeaderException() {
   }

   public MalformedCookieHeaderException(String var1) {
      super(var1);
   }

   public String getHeader() {
      return this.getMessage();
   }
}
