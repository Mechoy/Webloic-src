package weblogic.corba.idl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.ConnectException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.HashMap;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;
import weblogic.corba.utils.RemoteInfo;
import weblogic.corba.utils.RepositoryId;
import weblogic.iiop.ClusterComponent;
import weblogic.iiop.IIOPRemoteRef;
import weblogic.iiop.IOR;
import weblogic.iiop.ObjectKey;
import weblogic.iiop.ProtocolHandlerIIOP;
import weblogic.iiop.Utils;
import weblogic.iiop.spi.IORDelegate;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.extensions.server.RuntimeMethodDescriptor;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.BasicServerRef;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.StubInfo;
import weblogic.rmi.spi.InboundRequest;
import weblogic.rmi.spi.OutboundResponse;
import weblogic.rmi.utils.Utilities;

public final class CorbaServerRef extends BasicServerRef implements IORDelegate, IDLEntity {
   private static final long serialVersionUID = 6604979373837293000L;
   private static final String OPERATIONS = "Operations";
   private static final String TIE_CLASS_SUFFIX = "_Tie";
   private static HashMap objectMethods = new HashMap();
   public static final ResponseHandler NULL_RESPONSE;
   private InvokeHandler delegate;
   private StubReference stub;
   private RemoteInfo rinfo;

   public CorbaServerRef(Object var1) throws RemoteException {
      this(OIDManager.getInstance().getNextObjectID(), var1);
   }

   public CorbaServerRef(int var1, Object var2) throws RemoteException {
      super(var1, getTie(var2));
      this.initialize((InvokeHandler)this.getImplementation());
   }

   private void initialize(InvokeHandler var1) {
      this.delegate = var1;
      this.incrementRefCount();
      org.omg.CORBA.portable.ObjectImpl var2 = (org.omg.CORBA.portable.ObjectImpl)var1;
      this.rinfo = setDelegate(var2, this.getObjectID());
   }

   public static RemoteInfo setDelegate(org.omg.CORBA.portable.ObjectImpl var0, int var1) {
      String var2 = var0._ids()[0];
      ServerChannel var3 = ServerChannelManager.findLocalServerChannel(ProtocolHandlerIIOP.PROTOCOL_IIOP);
      String var4 = "localhost";
      int var5 = -1;
      if (var3 != null) {
         var4 = var3.getPublicAddress();
         var5 = var3.getPublicPort();
      }

      ObjectKey var6 = new ObjectKey(var2, var1, LocalServerIdentity.getIdentity());
      RepositoryId var7 = new RepositoryId(var2);
      RemoteInfo var8 = RemoteInfo.findRemoteInfo(var7, var0.getClass());
      IOR var9 = new IOR(var2, var4, var5, var6, Utilities.getAnnotationString(var0), (ClusterComponent)null, var8.getDescriptor());
      var0._set_delegate(new DelegateImpl(var9));
      return var8;
   }

   private static InvokeHandler getTie(Object var0) throws NoSuchObjectException {
      if (var0 instanceof InvokeHandler) {
         return (InvokeHandler)var0;
      } else {
         Class var1 = getOperationsClass(var0);
         if (var1 == null) {
            throw new NoSuchObjectException("Couldn't find Tie for class: " + var0.getClass().getName());
         } else {
            String var2 = var1.getName();
            String var3 = var2.substring(0, var2.length() - "Operations".length()) + "_Tie";

            try {
               Class var4 = Utils.loadClass(var3, (String)null, var0.getClass().getClassLoader());
               Constructor var5 = var4.getDeclaredConstructor(var1);
               return (InvokeHandler)var5.newInstance(var0);
            } catch (NoSuchMethodException var6) {
               throw (NoSuchObjectException)(new NoSuchObjectException(var6.getMessage())).initCause(var6);
            } catch (InstantiationException var7) {
               throw (NoSuchObjectException)(new NoSuchObjectException(var7.getMessage())).initCause(var7);
            } catch (InvocationTargetException var8) {
               throw (NoSuchObjectException)(new NoSuchObjectException(var8.getTargetException().getMessage())).initCause(var8);
            } catch (IllegalAccessException var9) {
               throw (NoSuchObjectException)(new NoSuchObjectException(var9.getMessage())).initCause(var9);
            } catch (ClassNotFoundException var10) {
               throw new NoSuchObjectException("Couldn't load Tie class: " + var3);
            }
         }
      }
   }

   public IOR getIOR() {
      return ((IORDelegate)((IORDelegate)((org.omg.CORBA.portable.ObjectImpl)this.delegate)._get_delegate())).getIOR();
   }

   public StubReference getStubReference() {
      if (this.stub == null) {
         this.stub = new StubInfo(new IIOPRemoteRef(this.getIOR(), this.rinfo), this.getDescriptor().getClientRuntimeDescriptor((String)null), (String)null, CorbaStub.class.getName());
      }

      return this.stub;
   }

   public void invoke(RuntimeMethodDescriptor var1, InboundRequest var2, OutboundResponse var3) throws Exception {
      try {
         weblogic.iiop.InboundRequest var4 = (weblogic.iiop.InboundRequest)var2;
         if (!var4.isCollocated() && var4.getEndPoint().isDead()) {
            throw new ConnectException("Connection is already shutdown for " + var2);
         } else {
            Integer var5 = (Integer)objectMethods.get(var4.getMethod());
            ResponseHandler var6;
            if (var3 == null) {
               var6 = NULL_RESPONSE;
            } else {
               var6 = ((weblogic.iiop.OutboundResponse)var3).createResponseHandler(var4);
            }

            if (var5 != null) {
               this.invokeObjectMethod(var5, var4.getInputStream(), var6);
            } else {
               this.delegate._invoke(var4.getMethod(), var4.getInputStream(), var6);
            }

            if (var3 != null) {
               var3.transferThreadLocalContext(var2);
            }

         }
      } catch (ClassCastException var7) {
         throw new NoSuchObjectException("CORBA ties are only supported with IIOP");
      }
   }

   protected OutputStream invokeObjectMethod(Integer var1, InputStream var2, ResponseHandler var3) {
      Object var4 = null;
      org.omg.CORBA.portable.ObjectImpl var5 = (org.omg.CORBA.portable.ObjectImpl)this.delegate;
      boolean var7;
      switch (var1) {
         case 0:
            var7 = var5._is_a(var2.read_string());
            var3.createReply().write_boolean(var7);
            break;
         case 1:
            var7 = var5._is_equivalent(var2.read_Object());
            var3.createReply().write_boolean(var7);
            break;
         case 2:
            var7 = var5._non_existent();
            var3.createReply().write_boolean(var7);
            break;
         case 3:
            org.omg.CORBA.Object var6 = var5._get_interface_def();
            var3.createReply().write_Object(var6);
            break;
         case 4:
            var5._release();
            break;
         default:
            throw new BAD_OPERATION(0, CompletionStatus.COMPLETED_MAYBE);
      }

      return (OutputStream)var4;
   }

   public static Class getOperationsClass(Object var0) {
      for(Class var1 = var0.getClass(); var1 != null; var1 = var1.getSuperclass()) {
         Class[] var2 = var1.getInterfaces();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            String var4 = var2[var3].getName();
            if (var4.endsWith("Operations")) {
               return var2[var3];
            }
         }
      }

      return null;
   }

   static {
      objectMethods.put("_is_a", new Integer(0));
      objectMethods.put("_is_equivalent", new Integer(1));
      objectMethods.put("_non_existent", new Integer(2));
      objectMethods.put("_interface", new Integer(3));
      objectMethods.put("_release", new Integer(4));
      NULL_RESPONSE = new ResponseHandler() {
         public OutputStream createReply() {
            return null;
         }

         public OutputStream createExceptionReply() {
            return null;
         }
      };
   }
}
