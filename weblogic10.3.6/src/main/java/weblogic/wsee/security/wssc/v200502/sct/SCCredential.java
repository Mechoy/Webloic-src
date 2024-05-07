package weblogic.wsee.security.wssc.v200502.sct;

import java.io.Externalizable;
import weblogic.wsee.security.wst.framework.TrustCredential;
import weblogic.wsee.server.StateExpiration;

public class SCCredential extends weblogic.wsee.security.wssc.sct.SCCredential implements TrustCredential, Externalizable, StateExpiration {
   private static final long serialVersionUID = -9178912632369049253L;
}
