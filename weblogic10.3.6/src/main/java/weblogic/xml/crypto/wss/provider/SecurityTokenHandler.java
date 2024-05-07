package weblogic.xml.crypto.wss.provider;

import javax.security.auth.Subject;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.MessageContext;
import org.w3c.dom.Node;
import weblogic.security.service.ContextHandler;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.common.keyinfo.KeyProvider;
import weblogic.xml.crypto.wss.SecurityTokenValidateResult;
import weblogic.xml.crypto.wss.WSSecurityException;

/** @deprecated */
public interface SecurityTokenHandler {
   SecurityToken getSecurityToken(String var1, Object var2, ContextHandler var3) throws WSSecurityException;

   SecurityToken getSecurityToken(String var1, String var2, Purpose var3, ContextHandler var4) throws WSSecurityException;

   SecurityTokenReference getSTR(QName var1, String var2, SecurityToken var3) throws WSSecurityException;

   QName[] getQNames();

   String[] getValueTypes();

   SecurityToken newSecurityToken(Node var1) throws MarshalException;

   SecurityTokenReference newSecurityTokenReference(Node var1) throws weblogic.xml.dom.marshal.MarshalException;

   KeyProvider getKeyProvider(SecurityToken var1, MessageContext var2);

   SecurityToken getSecurityToken(SecurityTokenReference var1, MessageContext var2) throws WSSecurityException;

   SecurityTokenValidateResult validateUnmarshalled(SecurityToken var1, MessageContext var2) throws WSSecurityException;

   SecurityTokenValidateResult validateProcessed(SecurityToken var1, MessageContext var2);

   boolean matches(SecurityToken var1, String var2, String var3, ContextHandler var4, Purpose var5);

   Subject getSubject(SecurityToken var1, MessageContext var2) throws WSSecurityException;
}
