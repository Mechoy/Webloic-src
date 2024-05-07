package weblogic.xml.crypto.dsig.api;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface SignatureProperties extends XMLStructure {
   String TYPE = "?";

   String getId();

   List getProperties();
}
