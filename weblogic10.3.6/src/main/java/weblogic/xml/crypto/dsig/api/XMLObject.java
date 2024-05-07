package weblogic.xml.crypto.dsig.api;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface XMLObject extends XMLStructure {
   String TYPE = "?";

   List getContent();

   String getEncoding();

   String getId();

   String getMimeType();
}
