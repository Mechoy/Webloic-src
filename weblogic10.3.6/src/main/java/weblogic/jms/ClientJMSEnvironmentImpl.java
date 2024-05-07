package weblogic.jms;

public class ClientJMSEnvironmentImpl extends JMSEnvironment {
   public boolean isThinClient() {
      return false;
   }

   public boolean isServer() {
      return false;
   }
}
