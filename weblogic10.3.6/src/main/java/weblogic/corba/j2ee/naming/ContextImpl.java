package weblogic.corba.j2ee.naming;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.Hashtable;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosTransactions.TransactionFactory;
import weblogic.corba.client.naming.ReferenceHelperImpl;
import weblogic.corba.client.spi.ServiceManager;
import weblogic.corba.cos.naming.NamingContextAny;
import weblogic.corba.cos.naming.NamingContextAnyPackage.WNameComponent;
import weblogic.rmi.extensions.server.ReferenceHelper;
import weblogic.transaction.TransactionHelper;

public final class ContextImpl implements Context {
   private ORBInfo orbinfo;
   private Hashtable env;
   private NamingContext ctx;
   private static final boolean DEBUG = false;
   private transient Thread loginThread;

   private static void p(String var0) {
      System.err.println("<ContextImpl> " + var0);
   }

   public ContextImpl(Hashtable var1) throws NamingException {
      this.loginThread = null;
      this.env = var1;
      if (this.env != null && !this.env.containsKey("java.naming.security.principal") && !this.env.containsKey("java.naming.security.credentials") && this.env.containsKey("jmx.remote.credentials")) {
         Object var2 = this.env.get("jmx.remote.credentials");
         if (var2 != null && var2 instanceof String[]) {
            String[] var3 = (String[])((String[])var2);
            if (var3.length == 2) {
               this.env.put("java.naming.security.principal", var3[0]);
               this.env.put("java.naming.security.credentials", var3[1]);
            }
         }
      }

   }

   public ContextImpl(Hashtable var1, ORBInfo var2, NamingContext var3) throws NamingException {
      this(var1);
      this.orbinfo = var2;
      this.ctx = var3;
   }

   public ContextImpl(ContextImpl var1, NamingContext var2) throws NamingException {
      this(var1.env, var1.orbinfo, var2);
   }

   public NamingContext getContext() {
      return this.ctx;
   }

   private NamingContext getContext(String var1) throws NamingException {
      if (this.ctx != null) {
         if (this.orbinfo != null) {
            ORBHelper.getORBHelper().setCurrent(this.orbinfo);
         }

         return this.ctx;
      } else {
         String var2 = NameParser.getProtocolString(var1);
         if (var2 == null) {
            throw new InvalidNameException("No useable protocol specified in: " + var1);
         } else {
            org.omg.CORBA.Object var3 = ORBHelper.getORBHelper().getORBReference(var2, this.env, "NameService");
            this.orbinfo = ORBHelper.getORBHelper().getCurrent();
            this.ctx = Utils.narrowContext(var3);
            ServiceManager.getSecurityManager().pushSubject(this.env, this);
            ORBHelper.getORBHelper().pushTransactionHelper();
            return this.ctx;
         }
      }
   }

   public Object lookup(Name var1) throws NamingException {
      return this.lookup(var1.toString());
   }

   public Object lookup(String var1) throws NamingException {
      NamingContext var2 = this.getContext(var1);
      if (var2 instanceof Context) {
         return ((Context)var2).lookup(NameParser.getNameString(var1));
      } else {
         return var2 instanceof NamingContextAny ? this.lookup((NamingContextAny)var2, Utils.stringToWNameComponent(var1)) : this.lookup((NamingContext)var2, Utils.stringToNameComponent(var1));
      }
   }

   Object lookup(NamingContext var1, NameComponent[] var2) throws NamingException {
      try {
         Object var3 = var1.resolve(var2);
         if (var3 instanceof NamingContext) {
            var3 = new ContextImpl(this, (NamingContext)var3);
         } else if (var3 instanceof TransactionFactory) {
            var3 = TransactionHelper.getTransactionHelper().getUserTransaction();
         }

         return var3;
      } catch (UserException var4) {
         throw Utils.wrapNamingException(var4, "Exception in lookup.");
      } catch (Exception var5) {
         throw Utils.wrapNamingException(var5, "Unhandled exception in lookup");
      }
   }

   Object lookup(NamingContextAny var1, WNameComponent[] var2) throws NamingException {
      try {
         Object var3 = null;
         Any var4 = var1.resolve_any(var2);
         switch (var4.type().kind().value()) {
            case 14:
               var3 = var4.extract_Object();
               break;
            case 29:
            case 30:
            case 32:
               var3 = var4.extract_Value();
         }

         if (var3 instanceof NamingContext) {
            var3 = new ContextImpl(this, (NamingContext)var3);
         } else if (var3 instanceof TransactionFactory) {
            var3 = TransactionHelper.getTransactionHelper().getUserTransaction();
         }

         return var3;
      } catch (UserException var5) {
         throw Utils.wrapNamingException(var5, "Exception in lookup.");
      } catch (Exception var6) {
         throw Utils.wrapNamingException(var6, "Unhandled exception in lookup");
      }
   }

   public void bind(String var1, Object var2) throws NamingException {
      this.bind(Utils.stringToName(var1), var2);
   }

   public void bind(Name var1, Object var2) throws NamingException {
      try {
         NamingContext var3 = this.getContext(var1.toString());
         if (!ReferenceHelper.exists()) {
            ReferenceHelper.setReferenceHelper(new ReferenceHelperImpl());
         }

         Object var4 = ReferenceHelper.getReferenceHelper().replaceObject(var2);
         var4 = NamingManager.getStateToBind(var4, var1, this, this.env);
         if (!(var4 instanceof org.omg.CORBA.Object)) {
            try {
               NamingContextAny var5 = (NamingContextAny)var3;
               ORB var6 = ORB.init();
               Any var7 = var6.create_any();
               TypeCode var8 = var6.get_primitive_tc(TCKind.tk_value);
               var7.insert_Value((Serializable)var4, var8);
               var5.bind_any(Utils.nameToWName(var1), var7);
            } catch (ClassCastException var9) {
               throw new IllegalArgumentException("Object must be a CORBA object: " + var4);
            }
         } else {
            var3.bind(Utils.nameToName(var1), (org.omg.CORBA.Object)var4);
         }

      } catch (UserException var10) {
         throw Utils.wrapNamingException(var10, "Exception in bind()");
      } catch (IOException var11) {
         throw new CommunicationException();
      } catch (Exception var12) {
         throw Utils.wrapNamingException(var12, "Unhandled exception in bind()");
      }
   }

   public void rebind(String var1, Object var2) throws NamingException {
      this.rebind(Utils.stringToName(var1), var2);
   }

   public void rebind(Name var1, Object var2) throws NamingException {
      try {
         NamingContext var3 = this.getContext(var1.toString());
         if (!ReferenceHelper.exists()) {
            ReferenceHelper.setReferenceHelper(new ReferenceHelperImpl());
         }

         Object var4 = ReferenceHelper.getReferenceHelper().replaceObject(var2);
         var4 = NamingManager.getStateToBind(var4, var1, this, this.env);
         if (!(var4 instanceof org.omg.CORBA.Object)) {
            try {
               NamingContextAny var5 = (NamingContextAny)var3;
               ORB var6 = ORB.init();
               Any var7 = var6.create_any();
               TypeCode var8 = var6.get_primitive_tc(TCKind.tk_value);
               var7.insert_Value((Serializable)var4, var8);
               var5.rebind_any(Utils.nameToWName(var1), var7);
            } catch (ClassCastException var9) {
               throw new IllegalArgumentException("Object must be a CORBA object: " + var4);
            }
         } else {
            var3.rebind(Utils.nameToName(var1), (org.omg.CORBA.Object)var4);
         }

      } catch (UserException var10) {
         throw Utils.wrapNamingException(var10, "Exception in rebind()");
      } catch (IOException var11) {
         throw new CommunicationException();
      } catch (Exception var12) {
         throw Utils.wrapNamingException(var12, "Unhandled exception in rebind()");
      }
   }

   public void unbind(Name var1) throws NamingException {
      this.unbind(var1.toString());
   }

   public void unbind(String var1) throws NamingException {
      try {
         NamingContext var2 = this.getContext(var1);
         var2.unbind(Utils.stringToNameComponent(var1));
      } catch (UserException var3) {
         throw Utils.wrapNamingException(var3, "Exception in unbind()");
      } catch (Exception var4) {
         throw Utils.wrapNamingException(var4, "Unhandled exception in unbind()");
      }
   }

   public void rename(Name var1, Name var2) throws NamingException {
      this.rename(var1.toString(), var2.toString());
   }

   public void rename(String var1, String var2) throws NamingException {
      Object var3 = this.lookup(var1);
      this.bind(var2, var3);
      this.unbind(var1);
   }

   public NamingEnumeration list(Name var1) throws NamingException {
      return this.listBindings(var1);
   }

   public NamingEnumeration list(String var1) throws NamingException {
      return this.listBindings(var1);
   }

   public NamingEnumeration listBindings(Name var1) throws NamingException {
      return this.listBindings(var1.toString());
   }

   public NamingEnumeration listBindings(String var1) throws NamingException {
      try {
         NamingContext var2 = this.getContext(var1);
         if (var1.length() > 0) {
            NameComponent[] var3 = Utils.stringToNameComponent(var1);
            var2 = Utils.narrowContext(var2.resolve(var3));
         }

         BindingIteratorHolder var5 = new BindingIteratorHolder();
         var2.list(0, new BindingListHolder(new Binding[0]), var5);
         return new NamingEnumerationImpl(var5.value, var2, this);
      } catch (Exception var4) {
         throw Utils.wrapNamingException(var4, "Exception in listBindings");
      }
   }

   public void destroySubcontext(Name var1) throws NamingException {
      this.destroySubcontext(var1.toString());
   }

   public void destroySubcontext(String var1) throws NamingException {
      try {
         int var2 = var1.lastIndexOf(47);
         String var4 = "";
         if (var2 >= 0) {
            var1.substring(var2 + 1);
            var4 = var1.substring(0, var2);
         }

         this.getContext(var4);
         NameComponent[] var6 = Utils.stringToNameComponent(var1);
         NamingContext var7 = Utils.narrowContext(this.getContext(var1).resolve(var6));
         var7.destroy();
      } catch (Exception var8) {
         throw Utils.wrapNamingException(var8, "Exception in destroySubcontext()");
      }
   }

   public Context createSubcontext(Name var1) throws NamingException {
      return this.createSubcontext(var1.toString());
   }

   public Context createSubcontext(String var1) throws NamingException {
      try {
         int var2 = var1.lastIndexOf(47);
         String var4 = "";
         if (var2 >= 0) {
            var1.substring(var2 + 1);
            var4 = var1.substring(0, var2);
         }

         NamingContext var5 = this.getContext(var4);
         return new ContextImpl(this, Utils.narrowContext(var5.bind_new_context(Utils.stringToNameComponent(var1))));
      } catch (UserException var6) {
         throw Utils.wrapNamingException(var6, "CosNaming exception");
      } catch (Exception var7) {
         throw Utils.wrapNamingException(var7, "Unhandled error in createSubcontext");
      }
   }

   public Object lookupLink(Name var1) throws NamingException {
      throw new UnsupportedOperationException("naming operation using Name");
   }

   public Object lookupLink(String var1) throws NamingException {
      return this.lookup(var1);
   }

   public String getNameInNamespace() throws NamingException {
      throw new UnsupportedOperationException("naming operation using Name");
   }

   public javax.naming.NameParser getNameParser(Name var1) throws NamingException {
      throw new UnsupportedOperationException("naming operation using Name");
   }

   public javax.naming.NameParser getNameParser(String var1) throws NamingException {
      return new NameParser();
   }

   public Name composeName(Name var1, Name var2) throws NamingException {
      throw new UnsupportedOperationException("naming operation using Name");
   }

   public String composeName(String var1, String var2) throws NamingException {
      throw new UnsupportedOperationException("naming operation using Name");
   }

   public Object addToEnvironment(String var1, Object var2) throws NamingException {
      Object var3 = this.env.get(var1);
      this.env.put(var1, var2);
      return var3;
   }

   public Object removeFromEnvironment(String var1) throws NamingException {
      return this.env.remove(var1);
   }

   public Hashtable getEnvironment() throws NamingException {
      return this.env;
   }

   public void close() {
      if (this.loginThread != null) {
         if (this.loginThread == Thread.currentThread()) {
            ServiceManager.getSecurityManager().popSubject();
         }

         this.loginThread = null;
      }

      ORBHelper.getORBHelper().popTransactionHelper();
   }

   public Object writeReplace() throws ObjectStreamException {
      return this.getContext() != null ? this.getContext() : this;
   }

   public void enableLogoutOnClose() {
      this.loginThread = Thread.currentThread();
   }

   static class UnsupportedOperationException extends NamingException {
      private static final long serialVersionUID = -4020884966249797871L;

      UnsupportedOperationException(String var1) {
         super(var1);
      }
   }
}
