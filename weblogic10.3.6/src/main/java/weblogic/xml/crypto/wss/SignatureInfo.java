package weblogic.xml.crypto.wss;

import java.security.Key;
import java.util.List;
import org.w3c.dom.Node;

public interface SignatureInfo {
   Key getKey();

   List getReferences();

   String getC14NMethod();

   String getSignatureMethod();

   boolean containsNode(Node var1);

   public interface Reference {
      String getURI();

      List getTransformURIs();

      String getDigestURI();

      boolean containsNode(Node var1);
   }
}
