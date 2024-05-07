package weblogic.wsee.security.wss.policy;

import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import org.w3c.dom.Node;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.dsig.api.DigestMethod;
import weblogic.xml.crypto.dsig.api.Reference;
import weblogic.xml.crypto.dsig.api.SignatureMethod;
import weblogic.xml.crypto.dsig.api.SignedInfo;
import weblogic.xml.crypto.dsig.api.XMLSignatureFactory;
import weblogic.xml.crypto.wss.WSSecurityException;

public interface SignaturePolicy {
   void addSignatureTokens(SecurityTokenType[] var1);

   void addSignatureToken(SecurityTokenType var1);

   void addSignatureToken(SecurityToken var1);

   void setValidSignatureTokens(List var1);

   List getValidSignatureTokens();

   void setSignatureMethod(String var1) throws SecurityPolicyArchitectureException;

   void setSignatureMethod(SignatureMethod var1);

   SignatureMethod getSignatureMethod();

   void setCanonicalizationMethod(String var1) throws SecurityPolicyArchitectureException;

   void setCanonicalizationMethod(CanonicalizationMethod var1);

   CanonicalizationMethod getCanonicalizationMethod();

   void setDigestMethod(String var1) throws SecurityPolicyArchitectureException;

   void setDigestMethod(DigestMethod var1);

   DigestMethod getDigestMethod();

   SignedInfo newSignedInfo(Reference var1);

   SignedInfo newSignedInfo(XMLSignatureFactory var1, Reference var2);

   SignedInfo getSignedInfo();

   void setTokenProtection(boolean var1);

   boolean signedSecurityTokens();

   void setIncludeSigningTokens(boolean var1);

   boolean isIncludeSigningTokens();

   void addSignatureNode(String var1, Node var2);

   void addSignatureReference(String var1, Reference var2);

   void addQNameExprNode(String var1, QNameExpr var2);

   Map getSigningNodeMap();

   void setSigningNodeMap(Map var1);

   void addReferences(List var1);

   List getReferences() throws SecurityPolicyArchitectureException, WSSecurityException;

   void setX509AuthConditional(boolean var1);

   boolean isX509AuthConditional();

   boolean isSignatureRequired();

   boolean hasSignatureToken();

   String getDerivedFromTokenType();

   void addSignatureNodeListToReference() throws SecurityPolicyArchitectureException, WSSecurityException;

   void addSignatureNodeListToReference(SOAPMessageContext var1) throws SecurityPolicyArchitectureException, WSSecurityException;

   void addSignatureNodeListToReference(List var1) throws SecurityPolicyArchitectureException, WSSecurityException;

   void setNewSignatureNodeListToReference(List var1) throws SecurityPolicyArchitectureException, WSSecurityException;

   void addXPathFilter2NodeList(String var1, List<XPath> var2);

   void addXPathNode(String var1, MessagePartsType var2);

   void addXPathNode(String var1, XPath var2);

   boolean isPolicyValid();
}
