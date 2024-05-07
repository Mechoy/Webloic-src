package weblogic.jndi.remote;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import weblogic.jndi.internal.WLNamingManager;
import weblogic.rmi.utils.enumerations.BatchingEnumerationBase;
import weblogic.utils.NestedRuntimeException;

public final class NamingEnumerationWrapper extends BatchingEnumerationBase implements NamingEnumeration {
   private NamingEnumeration delegate;
   private Hashtable env;

   public NamingEnumerationWrapper(NamingEnumeration var1, Hashtable var2) {
      this.delegate = var1;
      this.env = var2;
   }

   public void close() throws NamingException {
   }

   public Object getSmartStub(Object var1) {
      return new NamingEnumerationStub(var1);
   }

   public boolean hasMoreElements() {
      try {
         return this.hasMore();
      } catch (NamingException var2) {
         throw new NestedRuntimeException(var2);
      }
   }

   public boolean hasMore() throws NamingException {
      try {
         return this.delegate.hasMore();
      } catch (NullPointerException var2) {
         return false;
      }
   }

   public Object nextElement() {
      try {
         return this.next();
      } catch (NamingException var2) {
         throw new NestedRuntimeException(var2);
      }
   }

   public Object next() throws NamingException {
      return WLNamingManager.getTransportableInstance(this.delegate.next(), (Name)null, (Context)null, this.env);
   }
}
