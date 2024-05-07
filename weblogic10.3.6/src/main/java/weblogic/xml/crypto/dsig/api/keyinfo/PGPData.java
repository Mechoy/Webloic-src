package weblogic.xml.crypto.dsig.api.keyinfo;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface PGPData extends XMLStructure {
   String TYPE = " http://www.w3.org/2000/09/xmldsig#PGPData";

   List getExternalElements();

   byte[] getKeyId();

   byte[] getKeyPacket();
}
