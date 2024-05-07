package weblogic.deploy.service;

public interface StatusListenerManager {
   void registerStatusListener(String var1, StatusListener var2);

   void unregisterStatusListener(String var1);
}
