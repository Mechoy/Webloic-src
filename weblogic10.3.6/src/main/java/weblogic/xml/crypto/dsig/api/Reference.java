package weblogic.xml.crypto.dsig.api;

import java.util.List;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.URIReference;
import weblogic.xml.crypto.api.XMLStructure;

public interface Reference extends URIReference, XMLStructure {
   DigestMethod getDigestMethod();

   DigestValue getDigestValue();

   String getId();

   List getTransforms();

   ValidateResult validate(XMLValidateContext var1) throws XMLSignatureException;

   public interface ValidateResult {
      DigestValue getDigestValue();

      String getReferenceURI();

      boolean status();
   }

   public interface DigestValue extends XMLStructure {
      Data getDereferencedData();

      byte[] getDigest();

      byte[] getDigestInput();
   }
}
