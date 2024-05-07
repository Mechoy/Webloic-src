package weblogic.wsee.security.wst.framework;

import weblogic.wsee.security.wst.faults.WSTFaultException;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public interface TrustTokenProvider {
   TrustToken issueTrustToken(WSTContext var1) throws WSTFaultException;

   TrustToken renewTrustToken(WSTContext var1, TrustToken var2) throws WSTFaultException;

   void cancelTrustToken(WSTContext var1, TrustToken var2) throws WSTFaultException;

   SecurityTokenReference createSecurityTokenReference(WSTContext var1, TrustToken var2) throws WSTFaultException;

   TrustToken resolveTrustToken(WSTContext var1, SecurityTokenReference var2) throws WSTFaultException;
}
