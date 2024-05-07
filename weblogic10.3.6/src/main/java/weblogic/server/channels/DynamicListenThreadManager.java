package weblogic.server.channels;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CyclicBarrier;
import weblogic.management.provider.ManagementService;
import weblogic.platform.VM;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.SecurityLogger;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.utils.SSLSetup;
import weblogic.server.AbstractServerService;
import weblogic.server.ServerLogger;
import weblogic.server.ServiceFailureException;

public final class DynamicListenThreadManager extends AbstractServerService {
   private boolean magicThreadDumpsOn;
   private InetAddress magicAddress;
   private InetAddress localhost;
   private final HashMap licensedAddresses;
   private CyclicBarrier barrier;
   private final HashMap listeners;
   private boolean isJSSEEnabled;
   private static final AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
   private final ThreadGroup tg;
   private boolean initialBinding;

   private DynamicListenThreadManager() {
      this.licensedAddresses = new HashMap();
      this.listeners = new HashMap();
      this.isJSSEEnabled = false;
      this.tg = new ThreadGroup("WebLogicServer");
      this.initialBinding = false;
      this.isJSSEEnabled = SSLSetup.isJSSEEnabled();
   }

   public static final DynamicListenThreadManager getInstance() {
      return DynamicListenThreadManager.SingletonMaker.singleton;
   }

   final ThreadGroup getThreadGroup() {
      return this.tg;
   }

   private final synchronized void notifyWaiters() {
      this.initialBinding = true;
      this.notifyAll();
   }

   final void waitForBinding() {
      try {
         if (!this.initialBinding) {
            this.barrier.await();
         }
      } catch (Exception var7) {
         if (!this.initialBinding) {
            synchronized(this) {
               while(!this.initialBinding) {
                  try {
                     this.wait();
                  } catch (InterruptedException var5) {
                  }
               }
            }
         }
      }

   }

   private synchronized void pause(int var1) throws InterruptedException {
      this.wait((long)var1);
   }

   public void start() throws ServiceFailureException {
      this.initialize();
      ArrayList var1 = new ArrayList();
      Iterator var2 = ChannelService.getLocalServerChannels().values().iterator();

      while(true) {
         Iterator var4;
         while(var2.hasNext()) {
            ArrayList var3 = (ArrayList)var2.next();
            var4 = var3.iterator();

            while(var4.hasNext()) {
               ServerChannelImpl var5 = (ServerChannelImpl)var4.next();
               if (!var5.isImplicitChannel() && var5.getProtocol() != ProtocolHandlerAdmin.PROTOCOL_ADMIN) {
                  try {
                     DynamicListenThread var6 = this.createListener(var3);
                     var1.add(var6);
                  } catch (IOException var8) {
                     SecurityLogger.logNotListeningForSSLInfo(var8.toString());
                  }
                  break;
               }
            }
         }

         this.barrier = new CyclicBarrier(var1.size() + 1);
         var2 = var1.iterator();

         while(var2.hasNext()) {
            DynamicListenThread var11 = (DynamicListenThread)var2.next();
            var11.start(false, true, true);
         }

         try {
            this.barrier.await();
         } catch (Exception var9) {
            int var12 = 1000 * ManagementService.getRuntimeAccess(kernelId).getServer().getListenThreadStartDelaySecs();

            label72:
            while(var12 > 0) {
               var4 = var1.iterator();

               DynamicListenThread var15;
               do {
                  if (!var4.hasNext()) {
                     break label72;
                  }

                  var15 = (DynamicListenThread)var4.next();
               } while(var15.isBindingDone() || var15.isBindingFail());

               try {
                  this.pause(100);
                  var12 -= 100;
               } catch (InterruptedException var7) {
               }
            }
         }

         int var10 = 0;
         Iterator var13 = var1.iterator();

         while(var13.hasNext()) {
            DynamicListenThread var14 = (DynamicListenThread)var13.next();
            if (var14.isBindingDone() && !var14.isBindingFail()) {
               ++var10;
            }
         }

         if (var10 == 0 && !ChannelHelper.isLocalAdminChannelEnabled()) {
            SecurityLogger.logNotInitOnAnyPortInfo();
            throw new ServiceFailureException("Server failed to bind to any usable port. See preceeding log message for details.");
         }

         if (var10 < var1.size()) {
            ServerLogger.logListenPortsNotOpenTotally();
         }

         this.notifyWaiters();
         ServerLogger.logDynamicListenersEnabled();
         return;
      }
   }

   private void initialize() {
      this.magicThreadDumpsOn = ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug().isMagicThreadDumpEnabled();
      if (this.magicThreadDumpsOn) {
         try {
            String var1 = ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug().getMagicThreadDumpHost();
            this.magicAddress = InetAddress.getByName(var1);
         } catch (UnknownHostException var3) {
            this.magicThreadDumpsOn = false;
         }
      }

      try {
         this.localhost = InetAddress.getByName("127.0.0.1");
      } catch (UnknownHostException var2) {
      }

   }

   final boolean checkDumpThreads(Socket var1) {
      if (this.magicThreadDumpsOn && var1.getInetAddress().equals(this.magicAddress)) {
         dumpThreads(var1);
         if (ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug().getMagicThreadDumpBackToSocket()) {
            try {
               var1.close();
            } catch (IOException var3) {
               ServerLogger.logDebugThreadException("Problem dumping threads back to socket", var3);
            }

            return false;
         }
      }

      return true;
   }

   private static void dumpThreads(Socket var0) {
      if (ManagementService.getRuntimeAccess(kernelId).getServer().getServerDebug().getMagicThreadDumpBackToSocket()) {
         ServerLogger.logDebugThread("Dumping threads to " + var0);

         try {
            OutputStream var1 = var0.getOutputStream();
            OutputStreamWriter var2 = new OutputStreamWriter(var1);
            BufferedWriter var3 = new BufferedWriter(var2);
            PrintWriter var4 = new PrintWriter(var3);
            VM.getVM().threadDump(var4);
            var4.flush();
         } catch (IOException var5) {
            ServerLogger.logDebugThreadException("Problem dumping threads back to socket", var5);
         }
      }

   }

   public DynamicListenThread createListener(ArrayList var1) throws IOException {
      Collections.sort(var1);
      ServerChannel[] var2 = (ServerChannel[])((ServerChannel[])var1.toArray(new ServerChannel[0]));
      if (!var2[0].supportsTLS()) {
         return new DynamicListenThread(var2, this);
      } else {
         return (DynamicListenThread)(this.isJSSEEnabled ? new DynamicJSSEListenThread(var2, this) : new DynamicSSLListenThread(var2, this));
      }
   }

   public void stop() {
      this.stopListeners(this.listeners);
      this.initialBinding = false;
   }

   private static synchronized boolean isScaleLimitedLicenseEnabled() {
      return true;
   }

   private boolean checkConcurrentClients(int var1) {
      return true;
   }

   public void halt() throws ServiceFailureException {
   }

   public void enableListeners() {
      this.enableListeners(this.listeners);
   }

   void enableListeners(HashMap var1) {
      Iterator var2 = var1.values().iterator();

      while(var2.hasNext()) {
         DynamicListenThread var3 = (DynamicListenThread)var2.next();
         if (!var3.enable()) {
            var2.remove();
         }
      }

   }

   void addListener(DynamicListenThread var1) {
      this.listeners.put(var1.getKey(), var1);
   }

   void removeListener(DynamicListenThread var1) {
      this.listeners.remove(var1.getKey());
   }

   DynamicListenThread findListener(ServerChannel var1) {
      return (DynamicListenThread)this.listeners.get(var1.getListenerKey());
   }

   DynamicListenThread restartListener(ServerChannel var1, HashMap var2, boolean var3) throws IOException {
      DynamicListenThread var4 = (DynamicListenThread)var2.remove(var1.getListenerKey());
      if (var4 != null) {
         var4.stop();
      }

      ArrayList var5 = ChannelService.getDiscriminantSet(var1.getListenerKey());
      if (var5 != null && !var5.isEmpty()) {
         var4 = this.createListener(var5);
         var4.setAdminChannel(var3);
         var2.put(var1.getListenerKey(), var4);
         var4.start(true, !var3, false);
      } else {
         var4 = null;
      }

      return var4;
   }

   DynamicListenThread restartListener(ServerChannel var1) throws IOException {
      return this.restartListener(var1, this.listeners, false);
   }

   DynamicListenThread stopListener(ServerChannel var1, HashMap var2) {
      DynamicListenThread var3 = (DynamicListenThread)var2.remove(var1.getListenerKey());
      if (var3 != null) {
         var3.stop();
      }

      return var3;
   }

   DynamicListenThread stopListener(ServerChannel var1) {
      return this.stopListener(var1, this.listeners);
   }

   void stopListeners(HashMap var1) {
      Iterator var2 = var1.values().iterator();

      while(var2.hasNext()) {
         DynamicListenThread var3 = (DynamicListenThread)var2.next();
         var3.unmanage();
         var3.setAdminChannel(false);
         var3.stop();
      }

      var1.clear();
   }

   // $FF: synthetic method
   DynamicListenThreadManager(Object var1) {
      this();
   }

   private static class SingletonMaker {
      private static final DynamicListenThreadManager singleton = new DynamicListenThreadManager();
   }
}
