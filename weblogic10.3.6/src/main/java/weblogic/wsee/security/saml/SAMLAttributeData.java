package weblogic.wsee.security.saml;

import com.bea.security.saml2.providers.SAML2AttributeInfo;
import java.util.Collection;
import weblogic.security.providers.saml.SAMLAttributeInfo;

public interface SAMLAttributeData {
   String getAttributeName();

   String getAttributeNameFormat();

   String getAttributeFriendlyName();

   Collection<String> getAttributeValues();

   boolean isSAML20();

   void setAttributeName(String var1);

   void setAttributeNameFormat(String var1);

   void setAttributeFriendlyName(String var1);

   void setAttributeValues(Collection<String> var1);

   void addAttributeValue(String var1);

   String getAttributeNameSpace();

   void setAttributeNameSpace(String var1);

   SAML2AttributeInfo getSAML2AttributeInfo();

   SAMLAttributeInfo getSAMLAttributeInfo();

   boolean isEmpty();

   void addAttributeValues(Collection<String> var1);
}
