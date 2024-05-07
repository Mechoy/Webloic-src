package weblogic.corba.j2ee.naming;

import java.util.NoSuchElementException;
import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.BindingHolder;
import org.omg.CosNaming.BindingIterator;
import org.omg.CosNaming.NamingContext;
import weblogic.corba.cos.naming.NamingContextAny;

public class NamingEnumerationImpl implements NamingEnumeration {
   private BindingIterator biter;
   private NamingContext ctx;
   private ContextImpl rootCtx;
   private Binding next_one;

   NamingEnumerationImpl(BindingIterator var1, NamingContext var2, ContextImpl var3) {
      this.biter = var1;
      this.ctx = var2;
      this.rootCtx = var3;
      this.next_one = null;
   }

   public void close() throws NamingException {
      this.biter.destroy();
   }

   public Object next() throws NamingException {
      if (!this.hasMore()) {
         throw new NoSuchElementException("No more elements");
      } else {
         Binding var1 = this.next_one;
         this.next_one = null;
         return var1;
      }
   }

   public boolean hasMore() throws NamingException {
      if (this.biter == null) {
         return false;
      } else {
         if (this.next_one == null) {
            BindingHolder var1 = new BindingHolder();

            try {
               if (this.biter.next_one(var1)) {
                  if (this.ctx instanceof NamingContextAny) {
                     this.next_one = new Binding(var1.value.binding_name[0].id, this.rootCtx.lookup((NamingContextAny)this.ctx, Utils.nameToWName(var1.value.binding_name)));
                  } else {
                     this.next_one = new Binding(var1.value.binding_name[0].id, this.rootCtx.lookup(this.ctx, var1.value.binding_name));
                  }
               }
            } catch (SystemException var4) {
               NamingException var3 = new NamingException("Unhandled error in hasMore()");
               var3.setRootCause(var4);
               throw var3;
            }
         }

         return this.next_one != null;
      }
   }

   public Object nextElement() {
      try {
         return this.next();
      } catch (NamingException var2) {
         throw new NoSuchElementException(var2.getMessage());
      }
   }

   public boolean hasMoreElements() {
      try {
         return this.hasMore();
      } catch (NamingException var2) {
         throw new NoSuchElementException(var2.getMessage());
      }
   }
}
