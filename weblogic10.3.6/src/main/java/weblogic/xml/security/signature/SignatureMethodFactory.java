package weblogic.xml.security.signature;

public interface SignatureMethodFactory {
   String getURI();

   SignatureMethod newSignatureMethod();
}
