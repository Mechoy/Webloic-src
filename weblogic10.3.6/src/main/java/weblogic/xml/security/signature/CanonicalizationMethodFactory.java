package weblogic.xml.security.signature;

public interface CanonicalizationMethodFactory {
   String getURI();

   CanonicalizationMethod newCanonicalizationMethod();
}
