package weblogic.security.providers.saml;

import java.util.Properties;
import javax.management.InvalidAttributeValueException;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.commo.StandardInterface;
import weblogic.management.security.authentication.IdentityAsserterMBean;
import weblogic.management.security.authentication.ServletAuthenticationFilterMBean;
import weblogic.security.providers.utils.CertRegManagerMBean;

public interface SAMLIdentityAsserterMBean extends StandardInterface, DescriptorBean, IdentityAsserterMBean, CertRegManagerMBean, ServletAuthenticationFilterMBean {
   String getProviderClassName();

   String getDescription();

   String getVersion();

   String[] getSupportedTypes();

   String[] getActiveTypes();

   boolean getBase64DecodingRequired();

   String[] getAssertionConsumerURIs();

   void setAssertionConsumerURIs(String[] var1) throws InvalidAttributeValueException;

   boolean isPostEnabled();

   void setPostEnabled(boolean var1) throws InvalidAttributeValueException;

   boolean isArtifactEnabled();

   void setArtifactEnabled(boolean var1) throws InvalidAttributeValueException;

   int getMinimumParserPoolSize();

   void setMinimumParserPoolSize(int var1) throws InvalidAttributeValueException;

   boolean isRecipientCheckEnabled();

   void setRecipientCheckEnabled(boolean var1) throws InvalidAttributeValueException;

   String getUsedAssertionCacheClassName();

   void setUsedAssertionCacheClassName(String var1) throws InvalidAttributeValueException;

   Properties getUsedAssertionCacheProperties();

   void setUsedAssertionCacheProperties(Properties var1) throws InvalidAttributeValueException;

   boolean isEnforceOneUsePolicy();

   void setEnforceOneUsePolicy(boolean var1) throws InvalidAttributeValueException;

   String getNameMapperClassName();

   void setNameMapperClassName(String var1) throws InvalidAttributeValueException;

   Properties getAssertionConfiguration();

   void setAssertionConfiguration(Properties var1) throws InvalidAttributeValueException;

   Properties getSourceSiteRedirects();

   void setSourceSiteRedirects(Properties var1) throws InvalidAttributeValueException;

   String getName();
}
