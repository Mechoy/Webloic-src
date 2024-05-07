package weblogic.webservice.context;

import java.util.Iterator;
import javax.xml.rpc.JAXRPCException;

/** @deprecated */
public interface WebServiceSession {
   Object getUnderlyingSession() throws JAXRPCException;

   Object getRequest() throws JAXRPCException;

   Object getAttribute(String var1);

   void setAttribute(String var1, Object var2);

   Iterator getAttributeNames();

   void removeAttribute(String var1);

   void invalidate();
}
