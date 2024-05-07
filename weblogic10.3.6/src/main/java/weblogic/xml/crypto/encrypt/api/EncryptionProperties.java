package weblogic.xml.crypto.encrypt.api;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface EncryptionProperties extends XMLStructure {
   String TYPE = "http://www.w3.org/2001/04/xmlenc#EncryptionProperties";

   String getId();

   List getProperties();
}
