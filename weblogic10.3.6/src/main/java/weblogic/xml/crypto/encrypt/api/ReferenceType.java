package weblogic.xml.crypto.encrypt.api;

import java.util.List;
import weblogic.xml.crypto.api.URIReference;
import weblogic.xml.crypto.api.XMLStructure;

public interface ReferenceType extends XMLStructure, URIReference {
   List getContent();
}
