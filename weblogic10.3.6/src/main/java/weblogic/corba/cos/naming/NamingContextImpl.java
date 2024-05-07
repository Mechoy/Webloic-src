package weblogic.corba.cos.naming;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.rmi.Remote;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NoPermissionException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import org.omg.CORBA.Any;
import org.omg.CORBA.NO_PERMISSION;
import org.omg.CORBA.Object;
import org.omg.CORBA.UNKNOWN;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.omg.CosNaming.NamingContextPackage.NotFoundReason;
import weblogic.corba.cos.naming.NamingContextAnyPackage.TypeNotSupported;
import weblogic.corba.cos.naming.NamingContextAnyPackage.WNameComponent;
import weblogic.corba.cos.transactions.TransactionFactoryImpl;
import weblogic.corba.idl.AnyImpl;
import weblogic.corba.idl.ObjectImpl;
import weblogic.corba.j2ee.naming.Utils;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.IIOPLogger;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;
import weblogic.iiop.spi.IORDelegate;
import weblogic.rjvm.JVMID;
import weblogic.rmi.internal.InitialReferenceConstants;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public class NamingContextImpl extends _NamingContextAnyImplBase implements InitialReferenceConstants {
   public static final String TYPE_ID = NamingContextAnyHelper.id();
   private static final boolean DEBUG = false;
   private Context ctx;
   private static final DebugCategory debugNaming = Debug.getCategory("weblogic.iiop.naming");
   private static final DebugLogger debugIIOPNaming = DebugLogger.getDebugLogger("DebugIIOPNaming");

   protected static void p(String var0) {
      System.err.println("<NamingContextImpl> " + var0);
   }

   public static IOR getBootstrapIOR(JVMID var0) {
      return new IOR(TYPE_ID, 8);
   }

   protected NamingContextImpl() {
   }

   private NamingContextImpl(Context var1) {
      this.ctx = var1;
   }

   public IOR getIOR() throws IOException {
      return ((IORDelegate)this._get_delegate()).getIOR();
   }

   private Context getContext() throws NamingException {
      if (this.ctx == null) {
         this.ctx = new InitialContext();
      }

      return this.ctx;
   }

   public void bind(NameComponent[] var1, Object var2) throws NotFound, CannotProceed, InvalidName, AlreadyBound {
      String var3 = Utils.nameComponentToString(var1);

      try {
         if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
            IIOPLogger.logDebugNaming("bind(" + var3 + ") from " + this.getContext());
         }

         this.getContext().bind(var3, IIOPReplacer.getReplacer().resolveObject(var2));
         return;
      } catch (IOException var5) {
         this.throwNamingBindException(var5, var1);
      } catch (NamingException var6) {
         this.throwNamingBindException(var6, var1);
      }

   }

   public void bind_context(NameComponent[] var1, NamingContext var2) throws NotFound, CannotProceed, InvalidName, AlreadyBound {
      this.bind(var1, var2);
   }

   public void rebind(NameComponent[] var1, Object var2) throws NotFound, CannotProceed, InvalidName {
      String var3 = Utils.nameComponentToString(var1);

      try {
         if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
            IIOPLogger.logDebugNaming("rebind(" + var3 + ") from " + this.getContext());
         }

         this.getContext().rebind(var3, IIOPReplacer.getReplacer().resolveObject(var2));
         return;
      } catch (IOException var5) {
         this.throwNamingException(var5, (NameComponent[])var1);
      } catch (NamingException var6) {
         this.throwNamingException(var6, (NameComponent[])var1);
      }

   }

   public void rebind_context(NameComponent[] var1, NamingContext var2) throws NotFound, CannotProceed, InvalidName {
      this.rebind(var1, var2);
   }

   public Object resolve(NameComponent[] var1) throws NotFound, CannotProceed, InvalidName {
      String var2 = Utils.nameComponentToString(var1);
      java.lang.Object var3 = null;

      try {
         if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
            IIOPLogger.logDebugNaming("resolve(" + var2 + ")");
         }

         var3 = this.resolveObject(var2);
      } catch (NamingException var7) {
         this.throwNamingException(var7, (NameComponent[])var1);
      }

      if (!(var3 instanceof Object)) {
         try {
            return IIOPReplacer.makeInvocationHandler((IOR)IIOPReplacer.getReplacer().replaceObject(var3));
         } catch (IOException var5) {
            throw (CannotProceed)(new CannotProceed(this, var1)).initCause(var5);
         } catch (ClassCastException var6) {
            throw (NotFound)(new NotFound(NotFoundReason.not_object, var1)).initCause(var6);
         }
      } else {
         return (Object)var3;
      }
   }

   public void unbind(NameComponent[] var1) throws NotFound, CannotProceed, InvalidName {
      String var2 = Utils.nameComponentToString(var1);
      if (!var2.equals("")) {
         try {
            if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
               IIOPLogger.logDebugNaming("unbind(" + var2 + ") from " + this.getContext());
            }

            this.getContext().unbind(var2);

            try {
               this.getContext().lookup(var2);
               p("error unbound name still exists");
            } catch (NameNotFoundException var4) {
            }

         } catch (NamingException var5) {
            this.throwNamingException(var5, (NameComponent[])var1);
         }
      }
   }

   public void list(int var1, BindingListHolder var2, org.omg.CosNaming.BindingIteratorHolder var3) {
      try {
         NamingEnumeration var4 = (new InitialContext()).listBindings(this.getContext().getNameInNamespace());
         BindingIteratorImpl.getBindings(var4, var1, var2);
         if (var4.hasMore()) {
            var3.value = new BindingIteratorImpl(var4);
         } else {
            var3.value = null;
         }
      } catch (NamingException var5) {
         this.throwUncheckedNamingException(var5);
      }

   }

   public NamingContext new_context() {
      return null;
   }

   public NamingContext bind_new_context(NameComponent[] var1) throws NotFound, AlreadyBound, CannotProceed, InvalidName {
      try {
         Context var2 = this.getContext().createSubcontext(Utils.nameComponentToString(var1));
         return new NamingContextImpl(var2);
      } catch (NamingException var3) {
         this.throwNamingBindException(var3, var1);
         return null;
      }
   }

   public void destroy() throws NotEmpty {
      try {
         String var1 = this.getContext().getNameInNamespace();
         String var2 = null;
         boolean var3 = false;
         int var6;
         if ((var6 = var1.lastIndexOf(47)) >= 0) {
            var2 = var1.substring(0, var6);
            var1 = var1.substring(var6 + 1);
         } else {
            if ((var6 = var1.lastIndexOf(46)) < 0) {
               throw new NotEmpty();
            }

            var2 = var1.substring(0, var6);
            var1 = var1.substring(var6 + 1);
         }

         ((Context)(new InitialContext()).lookup(var2)).destroySubcontext(var1);
      } catch (ContextNotEmptyException var4) {
         throw (NotEmpty)(new NotEmpty()).initCause(var4);
      } catch (NamingException var5) {
         this.throwUncheckedNamingException(var5);
      }

   }

   public void bind_any(WNameComponent[] var1, Any var2) throws weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound, weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed, InvalidName, AlreadyBound, TypeNotSupported {
      String var3 = Utils.nameComponentToString(var1);

      try {
         if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
            IIOPLogger.logDebugNaming("bind_any(" + var3 + ")");
         }

         this.getContext().bind(var3, resolveAny(var2));
      } catch (NameAlreadyBoundException var5) {
         throw (AlreadyBound)(new AlreadyBound()).initCause(var5);
      } catch (NamingException var6) {
         this.throwNamingException(var6, (WNameComponent[])var1);
      } catch (IOException var7) {
         this.throwNamingException(var7, (WNameComponent[])var1);
      }

   }

   public void rebind_any(WNameComponent[] var1, Any var2) throws weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound, weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed, InvalidName, TypeNotSupported {
      String var3 = Utils.nameComponentToString(var1);

      try {
         if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
            IIOPLogger.logDebugNaming("rebind_any(" + var3 + ")");
         }

         this.getContext().rebind(var3, resolveAny(var2));
      } catch (NamingException var5) {
         this.throwNamingException(var5, (WNameComponent[])var1);
      } catch (IOException var6) {
         this.throwNamingException(var6, (WNameComponent[])var1);
      }

   }

   public Any resolve_any(WNameComponent[] var1) throws weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound, weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed, InvalidName {
      AnyImpl var2 = new AnyImpl();
      String var3 = Utils.nameComponentToString(var1);
      if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
         IIOPLogger.logDebugNaming("resolve_any(" + var3 + ")");
      }

      try {
         java.lang.Object var4 = this.resolveObject(var3);
         if (var4 instanceof Object) {
            var2.insert_Object((Object)var4);
         } else if (var4 instanceof Serializable) {
            var2.insert_Value((Serializable)var4);
         }
      } catch (NamingException var6) {
         this.throwNamingException(var6, (WNameComponent[])var1);
      }

      return var2;
   }

   public Any resolve_str_any(String var1) throws weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound, weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed, InvalidName {
      try {
         return this.resolve_any(Utils.stringToWNameComponent(var1));
      } catch (NamingException var3) {
         throw (InvalidName)(new InvalidName()).initCause(var3);
      }
   }

   public String to_string(NameComponent[] var1) throws InvalidName {
      return Utils.nameComponentToString(var1);
   }

   public NameComponent[] to_name(String var1) throws InvalidName {
      try {
         return Utils.stringToNameComponent(var1);
      } catch (NamingException var3) {
         throw (InvalidName)(new InvalidName()).initCause(var3);
      }
   }

   public String to_url(String var1, String var2) throws InvalidName, InvalidAddress {
      return "corbaname:iiop:" + var1 + "#" + var2;
   }

   public Object resolve_str(String var1) throws NotFound, CannotProceed, InvalidName {
      NameComponent[] var2 = null;

      try {
         var2 = Utils.stringToNameComponent(var1);
         return this.resolve(var2);
      } catch (NamingException var4) {
         this.throwNamingException(var4, (NameComponent[])var2);
         return null;
      }
   }

   public static java.lang.Object resolveAny(Any var0) throws TypeNotSupported, IOException {
      java.lang.Object var1 = null;
      switch (var0.type().kind().value()) {
         case 14:
            var1 = IIOPReplacer.getReplacer().resolveObject(var0.extract_Object());
            break;
         case 29:
         case 30:
         case 32:
            var1 = var0.extract_Value();
            break;
         default:
            throw new TypeNotSupported();
      }

      return var1;
   }

   private java.lang.Object resolveObject(String var1) throws NamingException {
      if (var1.equals("")) {
         return this;
      } else {
         java.lang.Object var2 = this.getContext().lookup(var1);
         Thread var3 = Thread.currentThread();
         ClassLoader var4 = var3.getContextClassLoader();

         java.lang.Object var5;
         try {
            var3.setContextClassLoader(var2.getClass().getClassLoader());
            if (var2 instanceof Proxy) {
               var5 = var2;
               return var5;
            }

            if (var2 instanceof Remote) {
               if (!(var2 instanceof Object)) {
                  var2 = new ObjectImpl((Remote)var2);
               }
            } else if (var2 instanceof Context) {
               var2 = new NamingContextImpl((Context)var2);
            } else if (var2 instanceof UserTransaction) {
               var2 = TransactionFactoryImpl.getTransactionFactory();
            } else if (var2 instanceof TransactionManager) {
               var2 = TransactionFactoryImpl.getTransactionFactory();
            }

            var5 = var2;
         } finally {
            var3.setContextClassLoader(var4);
         }

         return var5;
      }
   }

   private void throwNamingBindException(Exception var1, NameComponent[] var2) throws AlreadyBound, InvalidName, NotFound, CannotProceed {
      if (!(var1 instanceof NameAlreadyBoundException)) {
         this.throwNamingException(var1, var2);
      } else {
         if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
            IIOPLogger.logNamingException(var1);
         }

         throw (AlreadyBound)(new AlreadyBound()).initCause(var1);
      }
   }

   private void throwNamingException(Exception var1, NameComponent[] var2) throws InvalidName, NotFound, CannotProceed {
      if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
         IIOPLogger.logNamingException(var1);
      }

      if (var1 instanceof NameNotFoundException) {
         throw (NotFound)(new NotFound(NotFoundReason.missing_node, var2)).initCause(var1);
      } else if (var1 instanceof NoPermissionException) {
         throw (NO_PERMISSION)(new NO_PERMISSION(var1.getMessage())).initCause(var1);
      } else if (var1 instanceof AuthenticationException) {
         throw (NO_PERMISSION)(new NO_PERMISSION(var1.getMessage())).initCause(var1);
      } else if (var1 instanceof IOException) {
         throw (CannotProceed)(new CannotProceed(this, var2)).initCause(var1);
      } else {
         throw (InvalidName)(new InvalidName()).initCause(var1);
      }
   }

   private void throwUncheckedNamingException(NamingException var1) {
      if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
         IIOPLogger.logNamingException(var1);
      }

      if (var1 instanceof NoPermissionException) {
         throw (NO_PERMISSION)(new NO_PERMISSION(var1.getMessage())).initCause(var1);
      } else if (var1 instanceof AuthenticationException) {
         throw (NO_PERMISSION)(new NO_PERMISSION(var1.getMessage())).initCause(var1);
      } else {
         throw (UNKNOWN)(new UNKNOWN(var1.getMessage())).initCause(var1);
      }
   }

   private void throwNamingException(Exception var1, WNameComponent[] var2) throws InvalidName, weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound, weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed {
      if (debugNaming.isEnabled() || debugIIOPNaming.isDebugEnabled()) {
         IIOPLogger.logNamingException(var1);
      }

      if (var1 instanceof NameNotFoundException) {
         throw (weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound)(new weblogic.corba.cos.naming.NamingContextAnyPackage.NotFound(NotFoundReason.missing_node, var2)).initCause(var1);
      } else if (var1 instanceof NoPermissionException) {
         throw (NO_PERMISSION)(new NO_PERMISSION(var1.getMessage())).initCause(var1);
      } else if (var1 instanceof AuthenticationException) {
         throw (NO_PERMISSION)(new NO_PERMISSION(var1.getMessage())).initCause(var1);
      } else if (var1 instanceof IOException) {
         throw (weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed)(new weblogic.corba.cos.naming.NamingContextAnyPackage.CannotProceed(this, var2)).initCause(var1);
      } else {
         throw (InvalidName)(new InvalidName()).initCause(var1);
      }
   }
}
