package weblogic.security.providers.saml;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.security.ApplicationVersionerMBean;
import weblogic.management.security.credentials.CredentialMapperMBean;

public interface SAMLCredentialMapperMBean extends StandardInterface, DescriptorBean, CredentialMapperMBean, ApplicationVersionerMBean {
   String getProviderClassName();

   String getDescription();

   String getVersion();

   String getIssuerURI();

   void setIssuerURI(String var1) throws InvalidAttributeValueException;

   String getNameMapperClassName();

   void setNameMapperClassName(String var1) throws InvalidAttributeValueException;

   String getNameQualifier();

   void setNameQualifier(String var1) throws InvalidAttributeValueException;

   int getDefaultTimeToLive();

   void setDefaultTimeToLive(int var1) throws InvalidAttributeValueException;

   int getDefaultTimeToLiveDelta();

   void setDefaultTimeToLiveDelta(int var1) throws InvalidAttributeValueException;

   String getSourceSiteURL();

   void setSourceSiteURL(String var1) throws InvalidAttributeValueException;

   String getSourceIdHex();

   String getSourceIdBase64();

   String[] getIntersiteTransferURIs();

   void setIntersiteTransferURIs(String[] var1) throws InvalidAttributeValueException;

   String[] getAssertionRetrievalURIs();

   void setAssertionRetrievalURIs(String[] var1) throws InvalidAttributeValueException;

   boolean isArtifactEnabled();

   void setArtifactEnabled(boolean var1) throws InvalidAttributeValueException;

   String getAssertionStoreClassName();

   void setAssertionStoreClassName(String var1) throws InvalidAttributeValueException;

   Properties getAssertionStoreProperties();

   void setAssertionStoreProperties(Properties var1) throws InvalidAttributeValueException;

   boolean isPostEnabled();

   void setPostEnabled(boolean var1) throws InvalidAttributeValueException;

   String getDefaultPostForm();

   void setDefaultPostForm(String var1) throws InvalidAttributeValueException;

   int getMinimumParserPoolSize();

   void setMinimumParserPoolSize(int var1) throws InvalidAttributeValueException;

   Properties getAssertionConfiguration();

   void setAssertionConfiguration(Properties var1) throws InvalidAttributeValueException;

   int getCredCacheSize();

   void setCredCacheSize(int var1) throws InvalidAttributeValueException;

   int getCredCacheMinViableTTL();

   void setCredCacheMinViableTTL(int var1) throws InvalidAttributeValueException;

   String getName();
}
