package weblogic.xml.security.encryption;

public interface EncryptionMethodFactory {
   String getURI();

   EncryptionMethod newEncryptionMethod();
}
