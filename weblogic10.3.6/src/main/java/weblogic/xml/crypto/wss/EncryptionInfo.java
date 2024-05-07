package weblogic.xml.crypto.wss;

import java.security.Key;
import java.util.List;

public interface EncryptionInfo {
   Key getKey();

   List getNodes();
}
