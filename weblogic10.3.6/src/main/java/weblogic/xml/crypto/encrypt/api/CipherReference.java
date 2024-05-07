package weblogic.xml.crypto.encrypt.api;

import java.util.List;
import weblogic.xml.crypto.api.URIReference;

public interface CipherReference extends CipherData, URIReference {
   List getTransforms();
}
