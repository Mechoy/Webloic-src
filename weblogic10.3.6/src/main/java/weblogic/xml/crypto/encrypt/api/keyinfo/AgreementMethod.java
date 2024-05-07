package weblogic.xml.crypto.encrypt.api.keyinfo;

import java.util.List;
import weblogic.xml.crypto.api.AlgorithmMethod;
import weblogic.xml.crypto.api.XMLStructure;

public interface AgreementMethod extends AlgorithmMethod {
   List getContent();

   byte[] getKAContent();

   XMLStructure getOriginatorKeyInfo();

   XMLStructure getRecipeintKeyInfo();
}
