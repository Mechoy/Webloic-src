package weblogic.wsee.security.saml;

import com.bea.security.saml2.providers.SAML2AttributeStatementInfo;
import java.util.Collection;
import weblogic.security.providers.saml.SAMLAttributeStatementInfo;

public interface SAMLAttributeStatementData {
   Collection<SAMLAttributeData> getAttributeInfo();

   SAMLAttributeData getAttributeInfo(String var1);

   void addAttributeInfo(SAMLAttributeData var1);

   void addAttributeInfo(Collection<SAMLAttributeData> var1);

   Collection<SAML2AttributeStatementInfo> getCollectionsForSAML2AttributeStatementInfo();

   Collection<SAMLAttributeStatementInfo> getCollectionsForSAMLAttributeStatementInfo();

   boolean isAttributeOnlyRequest();

   void setAttributeOnlyRequest(boolean var1);

   boolean isEmpty();

   int size();

   boolean hasAttributeInfo(String var1);

   boolean hasAttributeValue(String var1, String var2);
}
