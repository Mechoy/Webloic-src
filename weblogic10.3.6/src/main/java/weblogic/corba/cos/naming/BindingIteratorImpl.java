package weblogic.corba.cos.naming;

import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;

public final class BindingIteratorImpl extends _BindingIteratorImplBase {
   private NamingEnumeration nenum;

   BindingIteratorImpl(NamingEnumeration var1) {
      this.nenum = var1;
   }

   public boolean next_one(BindingHolder var1) {
      var1.value = new Binding(new NameComponent[0], BindingType.nobject);

      try {
         if (!this.nenum.hasMore()) {
            return false;
         } else {
            javax.naming.Binding var2 = (javax.naming.Binding)this.nenum.next();
            var2.setRelative(true);
            if (var2.getObject() instanceof Context) {
               var1.value.binding_type = BindingType.ncontext;
            } else {
               var1.value.binding_type = BindingType.nobject;
            }

            var1.value.binding_name = new NameComponent[]{new NameComponent(var2.getName(), "")};
            return true;
         }
      } catch (NamingException var3) {
         return false;
      }
   }

   public boolean next_n(int var1, BindingListHolder var2) {
      try {
         return getBindings(this.nenum, var1, var2);
      } catch (NamingException var4) {
         return false;
      }
   }

   static boolean getBindings(NamingEnumeration var0, int var1, BindingListHolder var2) throws NamingException {
      if (!var0.hasMore()) {
         var2.value = new Binding[0];
         return false;
      } else {
         ArrayList var3 = new ArrayList(var1);

         int var4;
         for(var4 = 0; var4 < var1 && var0.hasMore(); ++var4) {
            javax.naming.Binding var5 = (javax.naming.Binding)var0.next();
            var5.setRelative(true);
            Binding var6 = new Binding();
            if (var5.getObject() instanceof Context) {
               var6.binding_type = BindingType.ncontext;
            } else {
               var6.binding_type = BindingType.nobject;
            }

            var6.binding_name = new NameComponent[]{new NameComponent(var5.getName(), "")};
            var3.add(var6);
         }

         var2.value = (Binding[])((Binding[])var3.toArray(new Binding[var4]));
         return true;
      }
   }

   public void destroy() {
      try {
         this.nenum.close();
      } catch (NamingException var2) {
      }

   }
}
