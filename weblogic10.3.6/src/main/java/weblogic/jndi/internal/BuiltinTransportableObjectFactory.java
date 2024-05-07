package weblogic.jndi.internal;

import java.rmi.Remote;
import java.util.Hashtable;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchResult;
import weblogic.common.internal.PassivationUtils;
import weblogic.jndi.TransportableObjectFactory;
import weblogic.jndi.WLContext;
import weblogic.jndi.remote.AttributeWrapper;
import weblogic.jndi.remote.AttributesWrapper;
import weblogic.jndi.remote.ContextWrapper;
import weblogic.jndi.remote.DirContextWrapper;
import weblogic.jndi.remote.NamingEnumerationWrapper;

public final class BuiltinTransportableObjectFactory implements TransportableObjectFactory {
   public Object getObjectInstance(Object var1, Name var2, Context var3, Hashtable var4) throws Exception {
      Object var5 = var1;
      if (!this.isAlreadyTransportable(var1)) {
         if (var1 instanceof Context) {
            var5 = this.makeTransportable((Context)var1, var4);
         } else if (var1 instanceof Binding) {
            var5 = this.makeTransportable((Binding)var1, var4);
         } else if (var1 instanceof NamingEnumeration) {
            var5 = this.makeTransportable((NamingEnumeration)var1, var4);
         } else if (var1 instanceof Attributes) {
            var5 = this.makeTransportable((Attributes)var1, var4);
         } else if (var1 instanceof Attribute) {
            var5 = this.makeTransportable((Attribute)var1, var4);
         } else {
            var5 = null;
         }
      }

      if (NamingFactoriesDebugLogger.isDebugEnabled() && var5 != null && var5 != var1) {
         NamingFactoriesDebugLogger.debug("Wrapping " + var1.getClass().getName() + " with " + var5.getClass().getName() + " to make transportable");
      }

      return var5;
   }

   private final Object makeTransportable(Context var1, Hashtable var2) {
      return var1 instanceof DirContext ? new DirContextWrapper((DirContext)var1, var2) : new ContextWrapper(var1, var2);
   }

   private final Object makeTransportable(NamingEnumeration var1, Hashtable var2) {
      return new NamingEnumerationWrapper(var1, var2);
   }

   private final Object makeTransportable(Binding var1, Hashtable var2) throws NamingException {
      Object var4;
      if (var1 instanceof SearchResult) {
         SearchResult var8 = (SearchResult)var1;
         var4 = var8.getObject();
         Object var5 = WLNamingManager.getTransportableInstance(var4, (Name)null, (Context)null, var2);
         Attributes var6 = var8.getAttributes();
         Attributes var7 = (Attributes)WLNamingManager.getTransportableInstance(var6, (Name)null, (Context)null, var2);
         return var4 == var5 && var6 == var7 ? var1 : new SearchResult(var8.getName(), var5, var7);
      } else {
         Object var3 = var1.getObject();
         var4 = WLNamingManager.getTransportableInstance(var3, (Name)null, (Context)null, var2);
         return var3 != var4 ? new Binding(var1.getName(), var4) : var1;
      }
   }

   private final Object makeTransportable(Attributes var1, Hashtable var2) {
      return this.isSerializable(var1) ? var1 : new AttributesWrapper(var1, var2);
   }

   private final Object makeTransportable(Attribute var1, Hashtable var2) {
      return this.isSerializable(var1) ? var1 : new AttributeWrapper(var1, var2);
   }

   public boolean isAlreadyTransportable(Object var1) {
      return var1 instanceof WLContext || var1 instanceof Remote || var1 instanceof NameClassPairEnumeration;
   }

   public boolean isSerializable(Object var1) {
      return PassivationUtils.isSerializable(var1);
   }
}
