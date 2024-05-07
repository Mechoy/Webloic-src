package weblogic.xml.crypto.api.dom;

import org.w3c.dom.traversal.NodeIterator;
import weblogic.xml.crypto.api.NodeSetData;

public interface DOMNodeSetData extends NodeSetData {
   NodeIterator nodeIterator();
}
