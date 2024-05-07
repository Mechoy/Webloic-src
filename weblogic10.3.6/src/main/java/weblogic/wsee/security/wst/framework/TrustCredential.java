package weblogic.wsee.security.wst.framework;

import java.util.Calendar;
import org.w3c.dom.Element;

public interface TrustCredential {
   String getIdentifier();

   String getAppliesTo();

   Element getAppliesToElement();

   Calendar getCreated();

   Calendar getExpires();

   boolean hasAttachedSecurityTokenReference();

   boolean hasUnattachedSecurityTokenReference();
}
