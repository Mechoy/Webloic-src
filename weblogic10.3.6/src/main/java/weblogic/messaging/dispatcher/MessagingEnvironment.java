package weblogic.messaging.dispatcher;

public abstract class MessagingEnvironment {
   private static MessagingEnvironment singleton;

   public static MessagingEnvironment getMessagingEnvironment() {
      if (singleton == null) {
         try {
            singleton = (MessagingEnvironment)Class.forName("weblogic.messaging.dispatcher.WLSMessagingtEnvironmentImpl").newInstance();
         } catch (Exception var5) {
            try {
               singleton = (MessagingEnvironment)Class.forName("weblogic.messaging.dispatcher.CEMessagingEnvironmentImpl").newInstance();
            } catch (Exception var4) {
               try {
                  singleton = (MessagingEnvironment)Class.forName("weblogic.messaging.dispatcher.ClientMessagingEnvironmentImpl").newInstance();
               } catch (Exception var3) {
                  throw new IllegalArgumentException(var3.toString());
               }
            }
         }
      }

      return singleton;
   }

   static void setMessagingEnvironment(MessagingEnvironment var0) {
      singleton = var0;
   }

   public abstract boolean isServer();
}
