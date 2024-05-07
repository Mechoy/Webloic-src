package weblogic.xml.crypto.dsig.api;

import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;

public interface Manifest extends XMLStructure {
   String TYPE = "?";

   String getId();

   List getReferences();

   boolean validate(XMLValidateContext var1);
}
