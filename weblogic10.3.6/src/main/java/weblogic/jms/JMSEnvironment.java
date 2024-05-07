package weblogic.jms;

public abstract class JMSEnvironment {
   private static JMSEnvironment singleton;

   public static JMSEnvironment getJMSEnvironment() {
      if (singleton == null) {
         try {
            singleton = (JMSEnvironment)Class.forName("weblogic.jms.WLSJMSEnvironmentImpl").newInstance();
         } catch (Exception var7) {
            try {
               singleton = (JMSEnvironment)Class.forName("weblogic.jms.CEJMSEnvironmentImpl").newInstance();
            } catch (Exception var6) {
               try {
                  singleton = (JMSEnvironment)Class.forName("weblogic.jms.ClientJMSEnvironmentImpl").newInstance();
               } catch (Exception var5) {
                  try {
                     singleton = (JMSEnvironment)Class.forName("weblogic.jms.IIOPJMSEnvironmentImpl").newInstance();
                  } catch (Exception var4) {
                     throw new IllegalArgumentException(var4.toString());
                  }
               }
            }
         }
      }

      return singleton;
   }

   static void setJMSEnvironment(JMSEnvironment var0) {
      singleton = var0;
   }

   public abstract boolean isThinClient();

   public abstract boolean isServer();
}
