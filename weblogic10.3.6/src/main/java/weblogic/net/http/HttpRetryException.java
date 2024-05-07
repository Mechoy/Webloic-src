package weblogic.net.http;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class HttpRetryException extends IOException {
   private static final long serialVersionUID = 2681042980966739659L;
   private int responseCode;
   private String location;
   private static Constructor javaCtor;

   public HttpRetryException(String var1, int var2) {
      super(var1);
      this.responseCode = var2;
   }

   public HttpRetryException(String var1, int var2, String var3) {
      super(var1);
      this.responseCode = var2;
      this.location = var3;
   }

   static IOException newInstance(String var0, int var1, String var2) {
      if (javaCtor != null) {
         try {
            return (IOException)javaCtor.newInstance(var0, new Integer(var1), var2);
         } catch (Exception var4) {
         }
      }

      return new HttpRetryException(var0, var1, var2);
   }

   public int responseCode() {
      return this.responseCode;
   }

   public String getReason() {
      return super.getMessage();
   }

   public String getLocation() {
      return this.location;
   }

   static {
      try {
         Class var0 = Class.forName("java.net.HttpRetryException");
         javaCtor = var0.getConstructor(String.class, Integer.TYPE, String.class);
      } catch (Exception var1) {
      }

   }
}
