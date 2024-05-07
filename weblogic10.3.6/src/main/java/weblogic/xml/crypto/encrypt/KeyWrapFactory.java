package weblogic.xml.crypto.encrypt;

interface KeyWrapFactory extends WLEncryptionMethodFactory {
   String getKeyAlgorithm();

   KeyWrap newKeyWrap();
}
