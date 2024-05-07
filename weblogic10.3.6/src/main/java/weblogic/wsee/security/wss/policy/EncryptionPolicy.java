package weblogic.wsee.security.wss.policy;

import java.util.List;
import java.util.Map;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import org.w3c.dom.Node;
import weblogic.wsee.security.policy.EncryptionTarget;
import weblogic.wsee.security.policy.SecurityToken;
import weblogic.wsee.security.policy.assertions.xbeans.MessagePartsType;
import weblogic.wsee.security.policy.assertions.xbeans.SecurityTokenType;
import weblogic.wsee.security.policy12.assertions.XPath;
import weblogic.wsee.security.wssp.QNameExpr;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.encrypt.api.EncryptionMethod;
import weblogic.xml.crypto.wss.WSSecurityException;

public interface EncryptionPolicy {
   List<EncryptionTarget> getEncryptionTargets();

   void addEncryptionTarget(List var1, boolean var2) throws WSSecurityException;

   void addEncryptionTarget(EncryptionMethod var1, List var2, boolean var3) throws WSSecurityException;

   EncryptionMethod getEncryptionMethod();

   void setEncryptionMethod(EncryptionMethod var1);

   void setEncryptionMethod(String var1) throws SecurityPolicyArchitectureException;

   void setKeyWrapMethod(String var1) throws SecurityPolicyArchitectureException;

   void setKeyWrapMethod(EncryptionMethod var1);

   EncryptionMethod getKeyWrapMethod();

   void setCanonicalizationMethod() throws SecurityPolicyArchitectureException;

   void setCanonicalizationMethod(String var1) throws SecurityPolicyArchitectureException;

   void setCanonicalizationMethod(CanonicalizationMethod var1);

   CanonicalizationMethod getCanonicalizationMethod();

   void addEncryptionTokens(SecurityTokenType[] var1);

   void addEncryptionToken(SecurityTokenType var1);

   void addEncryptionToken(SecurityToken var1);

   void setValidEncryptionTokens(List var1);

   List getValidEncryptionTokens();

   boolean hasEncryptionToken();

   void addNode(String var1, Node var2);

   void addQNameExprNode(String var1, QNameExpr var2);

   Map getNodeMap();

   void setNodeMap(Map var1);

   boolean isEncryptionRequired();

   void addEncryptionNodeList(SOAPMessageContext var1) throws WSSecurityException, SecurityPolicyArchitectureException;

   void addXPathNode(String var1, MessagePartsType var2);

   void addXPathNode(String var1, XPath var2);

   boolean isPolicyValid();
}
