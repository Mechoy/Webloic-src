package weblogic.wsee.addressing.policy.api;

import weblogic.wsee.wsa.wsaddressing.WSAVersion;

public interface UsingAddressingPolicyInfo {
   UsingAddressingVersionInfo getUsingAddressingWSAVersionInfo();

   boolean isValidWSAVersion(WSAVersion var1);
}
