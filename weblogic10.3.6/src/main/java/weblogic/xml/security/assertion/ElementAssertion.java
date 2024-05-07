package weblogic.xml.security.assertion;

import javax.xml.namespace.QName;
import weblogic.xml.security.specs.ElementIdentifier;

/** @deprecated */
public interface ElementAssertion {
   String BODY = "body";
   String HEADER = "header";

   String getRestriction();

   QName getElementName();

   String getElementLocalName();

   String getElementNamespace();

   boolean satisfies(ElementIdentifier var1);
}
