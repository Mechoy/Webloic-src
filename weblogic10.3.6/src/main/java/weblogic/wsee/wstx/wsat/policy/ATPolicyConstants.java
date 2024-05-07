package weblogic.wsee.wstx.wsat.policy;

import javax.xml.namespace.QName;

public interface ATPolicyConstants {
   String AT12_NS = "http://docs.oasis-open.org/ws-tx/wsat/2006/06";
   String AT10_NS = "http://schemas.xmlsoap.org/ws/2004/10/wsat";
   String AT10_PREFIX = "wsat";
   String AT_PREFIX = "wsat12";
   String AT_LOCAL_NAME = "ATAssertion";
   QName AT10_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/10/wsat", "ATAssertion", "wsat");
   QName AT12_QNAME = new QName("http://docs.oasis-open.org/ws-tx/wsat/2006/06", "ATAssertion", "wsat12");
   QName DEFAULT_AT_QNAME = AT10_QNAME;
   QName Required_QName = new QName("", "required");
}
