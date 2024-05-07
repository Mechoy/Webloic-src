package weblogic.net.http;

public abstract class NETEnvironment {
   private static NETEnvironment singleton;

   public static NETEnvironment getNETEnvironment() {
      if (singleton == null) {
         try {
            singleton = (NETEnvironment)Class.forName("weblogic.net.http.WLSNETEnvironmentImpl").newInstance();
         } catch (Exception var3) {
            try {
               singleton = (NETEnvironment)Class.forName("weblogic.net.http.WLSClientNETEnvironmentImpl").newInstance();
            } catch (Exception var2) {
               throw new IllegalArgumentException(var2.toString());
            }
         }
      }

      return singleton;
   }

   static void setNETEnvironment(NETEnvironment var0) {
      singleton = var0;
   }

   public abstract boolean useSunHttpHandler();
}
