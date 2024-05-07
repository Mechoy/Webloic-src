package weblogic.auddi.uddi.datastructure;

import org.w3c.dom.Node;
import weblogic.auddi.uddi.UDDIException;

public abstract class UDDIXMLHandler {
   public abstract Object create(Node var1) throws UDDIException;
}
