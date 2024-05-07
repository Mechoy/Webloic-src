package weblogic.xml.crypto.wss.api;

import java.util.Calendar;
import java.util.List;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.dom.marshal.WLDOMStructure;

public interface Timestamp extends WLDOMStructure, XMLStructure {
   String getId();

   Calendar getCreated();

   Calendar getExpires();

   List getContent();
}
