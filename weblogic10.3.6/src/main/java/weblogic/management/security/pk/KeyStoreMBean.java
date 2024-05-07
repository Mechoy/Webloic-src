package weblogic.management.security.pk;

import javax.management.InvalidAttributeValueException;
import weblogic.management.security.ProviderMBean;

/** @deprecated */
public interface KeyStoreMBean extends ProviderMBean {
   String getPrivateKeyStorePassPhrase();

   void setPrivateKeyStorePassPhrase(String var1) throws InvalidAttributeValueException;

   byte[] getPrivateKeyStorePassPhraseEncrypted();

   void setPrivateKeyStorePassPhraseEncrypted(byte[] var1) throws InvalidAttributeValueException;

   String getRootCAKeyStorePassPhrase();

   void setRootCAKeyStorePassPhrase(String var1) throws InvalidAttributeValueException;

   byte[] getRootCAKeyStorePassPhraseEncrypted();

   void setRootCAKeyStorePassPhraseEncrypted(byte[] var1) throws InvalidAttributeValueException;

   String getPrivateKeyStoreLocation();

   void setPrivateKeyStoreLocation(String var1) throws InvalidAttributeValueException;

   String getRootCAKeyStoreLocation();

   void setRootCAKeyStoreLocation(String var1) throws InvalidAttributeValueException;

   String getType();
}
