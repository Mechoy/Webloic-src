package weblogic.xml.crypto.dsig.api;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface SignatureProperty extends XMLStructure {
   List getContent();

   String getId();

   String getTarget();
}
