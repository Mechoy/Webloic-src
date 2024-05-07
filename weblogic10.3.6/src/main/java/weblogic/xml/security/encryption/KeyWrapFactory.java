package weblogic.xml.security.encryption;

public interface KeyWrapFactory extends EncryptionMethodFactory {
   String getAlgorithm();

   KeyWrap newKeyWrap();
}
