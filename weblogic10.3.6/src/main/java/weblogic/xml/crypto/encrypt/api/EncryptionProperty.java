package weblogic.xml.crypto.encrypt.api;

import java.util.List;
import java.util.Map;

public interface EncryptionProperty {
   Map getAttributes();

   List getContent();

   String getId();

   String getTarget();
}
