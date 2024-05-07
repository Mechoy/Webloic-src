package weblogic.xml.crypto.api.dom;

import org.w3c.dom.Node;
import weblogic.xml.crypto.api.URIReference;

public interface DOMURIReference extends URIReference {
   Node getHere();
}
