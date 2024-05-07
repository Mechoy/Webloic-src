package weblogic.security.spi;

import java.security.KeyStore;

public interface KeyStoreProvider extends SecurityProvider {
   KeyStore getPrivateKeyStore();

   String getPrivateKeyStoreLocation();

   String getPrivateKeyStorePassPhrase();

   KeyStore getRootCAKeyStore();

   String getRootCAKeyStoreLocation();

   String getRootCAKeyStorePassPhrase();
}
