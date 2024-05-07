package weblogic.xml.crypto.dsig.api.keyinfo;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface KeyInfo extends XMLStructure {
   List getContent();

   String getId();
}
