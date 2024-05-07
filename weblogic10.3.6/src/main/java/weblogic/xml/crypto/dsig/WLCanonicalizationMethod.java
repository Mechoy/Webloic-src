package weblogic.xml.crypto.dsig;

import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.stream.XMLOutputStream;

public interface WLCanonicalizationMethod extends CanonicalizationMethod {
   XMLOutputStream canonicalize(OutputStream var1, Map var2);

   void setAugmentedElementTracks(Set<String> var1);
}
