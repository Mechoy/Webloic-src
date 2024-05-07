package weblogic.deploy.common;

import java.io.IOException;
import java.io.InputStream;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.common.internal.WLObjectInputStream;
import weblogic.rmi.utils.io.RemoteObjectReplacer;

public class DeploymentObjectInputStream extends WLObjectInputStream implements PeerInfoable {
   final PeerInfo peerInfo;

   public DeploymentObjectInputStream(InputStream var1, String var2) throws IOException {
      super(var1);
      this.peerInfo = var2 != null && var2.length() != 0 ? PeerInfo.getPeerInfo(var2) : null;
      if (Debug.isServiceTransportDebugEnabled()) {
         Debug.serviceTransportDebug("PeerInfo on '" + this + "' is: " + this.peerInfo);
      }

      this.setReplacer(RemoteObjectReplacer.getReplacer());
   }

   public PeerInfo getPeerInfo() {
      return this.peerInfo;
   }
}
