package weblogic.iiop;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.UtilDelegate;
import javax.rmi.CORBA.ValueHandler;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.spi.IORDelegate;
import weblogic.kernel.Kernel;

public final class UtilDelegateImpl implements UtilDelegate {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private static UtilDelegate delegate;
   static final String[] delegateClassNames = new String[]{"com.sun.corba.se.internal.POA.ShutdownUtilDelegate", "com.sun.corba.se.impl.javax.rmi.CORBA.Util", "com.sun.corba.se.internal.javax.rmi.CORBA.Util", "com.ibm.rmi.javax.rmi.CORBA.Util", "com.sun.corba.ee.internal.POA.ShutdownUtilDelegate", "com.sun.corba.ee.internal.javax.rmi.CORBA.Util", "com.sun.corba.ee.internal.POA.JavaxRmiCorbaUtil", "com.ibm.CORBA.iiop.Util", "com.ibm.CORBA.iiop.UtilDelegateImpl"};

   public RemoteException mapSystemException(SystemException var1) {
      return Utils.mapSystemException(var1);
   }

   public void writeAny(OutputStream var1, Object var2) {
      if (var1 instanceof IIOPOutputStream) {
         ((IIOPOutputStream)var1).writeAny(var2);
      } else {
         delegate.writeAny(var1, var2);
      }

   }

   public Object readAny(InputStream var1) {
      return var1 instanceof IIOPInputStream ? ((IIOPInputStream)var1).readAny() : delegate.readAny(var1);
   }

   public void writeRemoteObject(OutputStream var1, Object var2) {
      if (var1 instanceof IIOPOutputStream) {
         try {
            IIOPReplacer.getIIOPReplacer().replaceRemote(var2).write((IIOPOutputStream)var1);
         } catch (IOException var4) {
            throw new MARSHAL("IOException writing RemoteObject " + var4.getMessage());
         }
      } else {
         delegate.writeRemoteObject(var1, var2);
      }

   }

   public void writeAbstractObject(OutputStream var1, Object var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("writeAbstractObject(OutputStream, " + var2 + ")");
      }

      try {
         ((org.omg.CORBA_2_3.portable.OutputStream)var1).write_abstract_interface(IIOPReplacer.getIIOPReplacer().replaceObject(var2));
      } catch (IOException var4) {
         throw new MARSHAL("IOException writing AbstractObject " + var4.getMessage());
      }
   }

   public void registerTarget(Tie var1, Remote var2) {
   }

   public void unexportObject(Remote var1) {
   }

   public Tie getTie(Remote var1) {
      return null;
   }

   public ValueHandler createValueHandler() {
      return delegate.createValueHandler();
   }

   public String getCodebase(Class var1) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("getCodebase for " + var1);
      }

      return delegate.getCodebase(var1);
   }

   public Class loadClass(String var1, String var2, ClassLoader var3) throws ClassNotFoundException {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("className = " + var1 + " remoteCodebase = " + var2 + " loadingContext = " + var3);
      }

      return Utils.loadClass(var1, var2, var3);
   }

   public boolean isLocal(Stub var1) throws RemoteException {
      try {
         Delegate var2 = var1._get_delegate();
         return var2 instanceof IORDelegate ? ((IORDelegate)var2).getIOR().isLocal() : false;
      } catch (SystemException var3) {
         throw Util.mapSystemException(var3);
      }
   }

   public RemoteException wrapException(Throwable var1) {
      return delegate.wrapException(var1);
   }

   public Object[] copyObjects(Object[] var1, ORB var2) throws RemoteException {
      return delegate.copyObjects(var1, var2);
   }

   public Object copyObject(Object var1, ORB var2) throws RemoteException {
      return delegate.copyObject(var1, var2);
   }

   private static void p(String var0) {
      System.err.println("<UtilDelegateImpl>: " + var0);
   }

   static {
      Class var0 = null;

      for(int var1 = 0; var1 < delegateClassNames.length; ++var1) {
         try {
            if ((var0 = Class.forName(delegateClassNames[var1])) != null) {
               try {
                  delegate = (UtilDelegate)var0.newInstance();
                  if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                     p("delegating to " + var0.getName());
                  }
                  break;
               } catch (InstantiationException var3) {
               } catch (IllegalAccessException var4) {
               }
            }
         } catch (Exception var5) {
         }
      }

      if (delegate == null) {
         throw new RuntimeException("could not find or instantiate any UtilDelegate class");
      }
   }
}
