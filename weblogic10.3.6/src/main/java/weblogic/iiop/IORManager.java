package weblogic.iiop;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.naming.NamingException;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CosNaming.NamingContextHelper;
import weblogic.corba.j2ee.naming.NameParser;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.Kernel;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.concurrent.locks.Mutex;

public final class IORManager {
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");
   private static boolean isClient = true;
   private static final String TRUE_PROP = "true";
   private static final String FALSE_PROP = "false";
   private static final DebugCategory debugTransport = Debug.getCategory("weblogic.iiop.transport");
   private static final DebugLogger debugIIOPTransport = DebugLogger.getDebugLogger("DebugIIOPTransport");
   private static final Mutex bootstrapLock = new Mutex();

   public static IOR createIOR(String var0, String var1, int var2, String var3, int var4, int var5) {
      try {
         IORFactory var6 = (IORFactory)Class.forName("weblogic.factories." + var0 + "." + "IORFactoryImpl").newInstance();
         return var6.createIOR(var1, var2, var3, var4, var5);
      } catch (ClassNotFoundException var7) {
         throw new Error(var7.toString());
      } catch (InstantiationException var8) {
         throw new Error(var8.toString());
      } catch (IllegalAccessException var9) {
         throw new Error(var9.toString());
      }
   }

   public static void initialize() {
      isClient = false;
   }

   public static boolean isClient() {
      return isClient;
   }

   public static Object createInitialReference(String var0, long var1) throws NamingException {
      try {
         NameParser.URLInfo var3 = NameParser.parseURL(var0);
         if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
            p("createInitialReference: " + var3.getHost() + ":" + Integer.toString(var3.getPort()));
         }

         String var4;
         try {
            var4 = System.getProperty("weblogic.system.iiop.enableClient");
         } catch (SecurityException var10) {
            var4 = "false";
         }

         if (!IIOPClient.isEnabled() && "true".equals(var4)) {
            IIOPClient.initialize();
         }

         if (!isClient) {
            try {
               Class var5 = Class.forName("weblogic.rjvm.ServerURL");
               Constructor var6 = var5.getConstructor(String.class, String.class, Integer.TYPE, String.class);
               java.lang.Object var7 = var6.newInstance(var3.getProtocol(), var3.getHost(), new Integer(var3.getPort()), var3.name);
               Method var8 = var5.getDeclaredMethod("isHostedByLocalRJVM", (Class[])null);
               if ((Boolean)var8.invoke(var7, (java.lang.Object[])null)) {
                  if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                     p(var3.getURL() + " is local");
                  }

                  return InitialReferences.getInitialReferenceObject(var3.getService());
               }
            } catch (Throwable var9) {
            }
         }

         IOR var13 = createIOR(var3.getProtocol(), var3.getHost(), var3.getPort(), var3.serviceName, var3.getMajorVersion(), var3.getMinorVersion());
         if (var3.serviceName.equals("NameService")) {
            var13 = locateNameService(var13, var1);
         }

         return (Object)IIOPReplacer.resolveObject(var13);
      } catch (SystemException var11) {
         throw weblogic.corba.j2ee.naming.Utils.wrapNamingException(var11, var11.getMessage());
      } catch (IOException var12) {
         throw weblogic.corba.j2ee.naming.Utils.wrapNamingException(var12, var12.getMessage());
      }
   }

   public static IOR locateNameService(IOR var0, long var1) {
      java.lang.Object var3 = null;
      Message var4 = null;
      boolean var5 = IIOPClientService.reconnectOnBootstrap && var0.getProfile().getObjectKey().isNamingKey();

      try {
         EndPoint var6;
         if (var5) {
            bootstrapLock.lock();
            var6 = EndPointManager.findOrCreateEndPoint(var0, (String)null, true);
         } else {
            var6 = EndPointManager.findOrCreateEndPoint(var0);
         }

         if (var6.supportsForwarding()) {
            IOPProfile var7 = var0.getProfile();
            IOR var8;
            IOR var9;
            if (var7.getMinorVersion() == 0) {
               var8 = new IOR(NamingContextHelper.id(), var7.getHost(), var7.getPort(), ObjectKey.BOOTSTRAP_KEY, var7.getMajorVersion(), var7.getMinorVersion());
               var3 = new RequestMessage(var6, var8, "get", false);
               ((SequencedRequestMessage)var3).setTimeout(var1);
               ((SequencedRequestMessage)var3).flush();
               ((SequencedRequestMessage)var3).getOutputStream().write_string("NameService");
               var4 = var6.sendReceive((SequencedRequestMessage)var3);
               var9 = new IOR(var4.getInputStream(), true);
               return var9;
            }

            var3 = new LocateRequestMessage(var6, var0);
            ((SequencedRequestMessage)var3).setTimeout(var1);
            if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
               IIOPLogger.logDebugTransport("LOCATE_REQUEST(" + ((SequencedRequestMessage)var3).getRequestID() + ")");
            }

            var4 = var6.sendReceive((SequencedRequestMessage)var3);
            var8 = ((LocateReplyMessage)var4).needsForwarding();
            if (var8 != null) {
               var9 = var8;
               return var9;
            }
         }
      } catch (IOException var13) {
         throw Utils.mapToCORBAException(var13);
      } finally {
         if (var5) {
            bootstrapLock.unlock();
         }

         if (var3 != null) {
            ((SequencedRequestMessage)var3).getOutputStream().close();
         }

         if (var4 != null) {
            var4.getInputStream().close();
         }

      }

      return var0;
   }

   public static IOR locateInitialReference(IOR var0, String var1, long var2) {
      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("locateInitialReference(" + var0 + ", " + var1 + ")");
      }

      LocateRequestMessage var4 = null;
      Message var5 = null;

      try {
         EndPoint var6 = EndPointManager.findOrCreateEndPoint(var0, var1);
         if (var6.supportsForwarding()) {
            var4 = new LocateRequestMessage(var6, var0);
            var4.setTimeout(var2);
            if (debugTransport.isEnabled() || debugIIOPTransport.isDebugEnabled()) {
               IIOPLogger.logDebugTransport("LOCATE_REQUEST(" + var4.getRequestID() + ")");
            }

            var5 = var6.sendReceive(var4);
            IOR var7 = ((LocateReplyMessage)var5).needsForwarding();
            if (var7 != null) {
               IOR var8 = var7;
               return var8;
            }
         }
      } catch (IOException var12) {
         throw Utils.mapToCORBAException(var12);
      } finally {
         if (var4 != null) {
            var4.getOutputStream().close();
         }

         if (var5 != null) {
            var5.getInputStream().close();
         }

      }

      return var0;
   }

   static void p(String var0) {
      System.err.println("<IORManager> " + var0);
   }
}
