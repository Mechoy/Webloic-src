package weblogic.corba.idl.poa;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.HashMap;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.OBJECT_NOT_EXIST;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantLocator;
import org.omg.PortableServer.POAManagerPackage.State;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;
import weblogic.corba.idl.CorbaServerRef;
import weblogic.iiop.Utils;
import weblogic.rmi.extensions.activation.Activator;
import weblogic.rmi.extensions.server.ActivatableServerReference;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.OutboundResponse;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.work.WorkManager;

public final class POAServerRef extends BasicServerRef implements ActivatableServerReference {
   private static final String OPERATIONS = "Operations";
   private static final String TIE_CLASS_SUFFIX = "POATie";
   private static HashMap objectMethods = new HashMap();
   private POAImpl poa;

   public POAServerRef(POAImpl var1) throws RemoteException {
      super(var1);
      this.incrementRefCount();
      this.poa = var1;
      CorbaServerRef.setDelegate(var1, this.getObjectID());
   }

   public static Servant getTie(Object var0, POAImpl var1) throws NoSuchObjectException {
      if (var0 instanceof Servant) {
         return (Servant)var0;
      } else {
         Class var2 = CorbaServerRef.getOperationsClass(var0);
         if (var2 == null) {
            throw new NoSuchObjectException("Couldn't find Tie for class: " + var0.getClass().getName());
         } else {
            String var3 = var2.getName();
            String var4 = var3.substring(0, var3.length() - "Operations".length()) + "POATie";

            try {
               Class var5 = Utils.loadClass(var4, (String)null, var0.getClass().getClassLoader());
               Constructor var6 = var5.getDeclaredConstructor(var2, POA.class);
               return (Servant)var6.newInstance(var0, var1);
            } catch (NoSuchMethodException var7) {
               throw (NoSuchObjectException)(new NoSuchObjectException(var7.getMessage())).initCause(var7);
            } catch (InstantiationException var8) {
               throw (NoSuchObjectException)(new NoSuchObjectException(var8.getMessage())).initCause(var8);
            } catch (InvocationTargetException var9) {
               throw (NoSuchObjectException)(new NoSuchObjectException(var9.getTargetException().getMessage())).initCause(var9);
            } catch (IllegalAccessException var10) {
               throw (NoSuchObjectException)(new NoSuchObjectException(var10.getMessage())).initCause(var10);
            } catch (ClassNotFoundException var11) {
               throw new NoSuchObjectException("Couldn't load Tie class: " + var4);
            }
         }
      }
   }

   public StubReference getStubReference() {
      throw new UnsupportedOperationException("getStubReference()");
   }

   public void invoke(RuntimeMethodDescriptor var1, InboundRequest var2, OutboundResponse var3) throws Exception {
      try {
         weblogic.iiop.InboundRequest var4 = (weblogic.iiop.InboundRequest)var2;
         ResponseHandler var5;
         if (var3 == null) {
            var5 = CorbaServerRef.NULL_RESPONSE;
         } else {
            var5 = ((weblogic.iiop.OutboundResponse)var3).createResponseHandler(var4);
         }

         State var6 = this.poa.the_POAManager().get_state();
         if (!var6.equals(State.DISCARDING)) {
            if (var6.equals(State.INACTIVE)) {
               throw new OBJECT_NOT_EXIST("POAManager not active");
            } else {
               byte[] var7 = (byte[])((byte[])var2.getActivationID());
               Object var9 = null;
               Servant var8;
               if (this.poa.getServantManager() instanceof ServantLocator) {
                  CookieHolder var10 = new CookieHolder();

                  try {
                     var8 = ((ServantLocator)this.poa.getServantManager()).preinvoke(var7, this.poa, var4.getMethod(), var10);
                  } catch (ForwardRequest var12) {
                     throw new NO_IMPLEMENT("ForwardRequest()");
                  }

                  var9 = var10.value;
               } else {
                  var8 = this.poa.id_to_servant(var7);
               }

               Integer var14 = (Integer)objectMethods.get(var4.getMethod());
               if (var14 != null) {
                  if (var14 == 4) {
                     this.poa.deactivate_object(var7);
                  } else {
                     invokeObjectMethod(var14, var4.getInputStream(), var5, var8);
                  }
               } else {
                  ((InvokeHandler)var8)._invoke(var4.getMethod(), var4.getInputStream(), var5);
               }

               if (this.poa.getServantManager() instanceof ServantLocator) {
                  ((ServantLocator)this.poa.getServantManager()).postinvoke(var7, this.poa, var4.getMethod(), var9, var8);
               }

            }
         }
      } catch (ClassCastException var13) {
         throw new NoSuchObjectException("CORBA ties are only supported with IIOP");
      }
   }

   protected WorkManager getWorkManager(RuntimeMethodDescriptor var1, AuthenticatedSubject var2) {
      return ((POAManagerImpl)this.poa.the_POAManager()).getWorkManager();
   }

   protected static OutputStream invokeObjectMethod(Integer var0, InputStream var1, ResponseHandler var2, Servant var3) {
      Object var4 = null;
      boolean var6;
      switch (var0) {
         case 0:
            var6 = var3._is_a(var1.read_string());
            var2.createReply().write_boolean(var6);
            break;
         case 1:
         default:
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
         case 2:
            var6 = var3._non_existent();
            var2.createReply().write_boolean(var6);
            break;
         case 3:
            org.omg.CORBA.Object var5 = var3._get_interface_def();
            var2.createReply().write_Object(var5);
      }

      return (OutputStream)var4;
   }

   public Object getImplementation(Object var1) throws RemoteException {
      if (var1 == null) {
         return this.getImplementation();
      } else {
         try {
            return this.poa.id_to_servant((byte[])((byte[])var1));
         } catch (ObjectNotActive var3) {
            throw new RemoteException(var3.getMessage(), var3);
         } catch (WrongPolicy var4) {
            throw new RemoteException(var4.getMessage(), var4);
         }
      }
   }

   public StubReference getStubReference(Object var1) {
      throw new UnsupportedOperationException("getStubReference()");
   }

   public Activator getActivator() {
      throw new UnsupportedOperationException("getActivator()");
   }

   static {
      objectMethods.put("_is_a", new Integer(0));
      objectMethods.put("_is_equivalent", new Integer(1));
      objectMethods.put("_non_existent", new Integer(2));
      objectMethods.put("_interface", new Integer(3));
      objectMethods.put("_release", new Integer(4));
   }
}
