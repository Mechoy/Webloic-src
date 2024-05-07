package weblogic.deploy.service;

import java.io.Serializable;

public interface StatusRelayer {
   void relayStatus(String var1, Serializable var2);

   void relayStatus(long var1, String var3, Serializable var4);
}
