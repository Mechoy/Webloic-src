package weblogic.wsee.context;

import java.util.HashMap;
import java.util.Iterator;
import javax.xml.rpc.JAXRPCException;

public class WebServiceSessionImpl implements WebServiceSession {
   private HashMap attributes = new HashMap();

   public Object getUnderlyingSession() throws JAXRPCException {
      return this;
   }

   public Object getAttribute(String var1) {
      return this.attributes.get(var1);
   }

   public void setAttribute(String var1, Object var2) {
      this.attributes.put(var1, var2);
   }

   public Iterator getAttributeNames() {
      return this.attributes.keySet().iterator();
   }

   public void removeAttribute(String var1) {
      this.attributes.remove(var1);
   }

   public void invalidate() {
      this.attributes.clear();
   }
}
