package weblogic.jms.common;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.OpaqueReference;
import weblogic.jndi.internal.NamingNode;

public class WrappedDestinationImpl implements OpaqueReference, Externalizable, Aggregatable {
   private static DestinationImplObserver observer;
   private DestinationImpl destinationImpl;

   public WrappedDestinationImpl() {
   }

   public WrappedDestinationImpl(DestinationImpl var1) {
      this.destinationImpl = var1;
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      if (this.destinationImpl == null) {
         throw new NameNotFoundException("Name not found");
      } else {
         return this.destinationImpl;
      }
   }

   public String toString() {
      return this.destinationImpl == null ? null : this.destinationImpl.toString();
   }

   public void onBind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      DestinationImpl var4;
      if (var3 == null) {
         var4 = this.destinationImpl;
      } else {
         var4 = ((WrappedDestinationImpl)var3).destinationImpl;
      }

      if (observer != null) {
         observer.newDestination(this.getBoundName(var1, var2), var4);
      }

   }

   public void onRebind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      throw new NameAlreadyBoundException("Name already bound");
   }

   public boolean onUnbind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      if (observer != null) {
         observer.removeDestination(this.getBoundName(var1, var2), this.destinationImpl);
      }

      return true;
   }

   private String getBoundName(NamingNode var1, String var2) {
      String var3 = null;

      try {
         String var4 = var1.getNameInNamespace();
         if (var4 != null && var4.length() != 0) {
            var3 = var4 + '.' + var2;
         } else {
            var3 = var2;
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return var3;
   }

   public static void setObserver(DestinationImplObserver var0) {
      observer = var0;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.destinationImpl = new DestinationImpl();
      this.destinationImpl.readExternal(var1);
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      this.destinationImpl.writeExternal(var1);
   }
}
