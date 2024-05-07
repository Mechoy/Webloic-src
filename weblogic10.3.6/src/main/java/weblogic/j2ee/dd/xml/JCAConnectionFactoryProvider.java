package weblogic.j2ee.dd.xml;

public abstract class JCAConnectionFactoryProvider {
   public static JCAConnectionFactoryProvider provider = null;

   public static boolean isAdapterConnectionFactoryClass(String var0) {
      return provider != null ? provider.isAdapterConnectionFactory(var0) : false;
   }

   public static void set(JCAConnectionFactoryProvider var0) {
      provider = var0;
   }

   public abstract boolean isAdapterConnectionFactory(String var1);
}
