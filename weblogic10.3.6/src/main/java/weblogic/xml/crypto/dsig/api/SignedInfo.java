package weblogic.xml.crypto.dsig.api;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface SignedInfo extends XMLStructure {
   CanonicalizationMethod getCanonicalizationMethod();

   String getId();

   List getReferences();

   SignatureMethod getSignatureMethod();

   void addReference(Reference var1);
}
