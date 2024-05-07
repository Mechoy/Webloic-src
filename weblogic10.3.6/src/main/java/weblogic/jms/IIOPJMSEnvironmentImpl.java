package weblogic.jms;

public class IIOPJMSEnvironmentImpl extends JMSEnvironment {
   public boolean isThinClient() {
      return true;
   }

   public boolean isServer() {
      return false;
   }
}
