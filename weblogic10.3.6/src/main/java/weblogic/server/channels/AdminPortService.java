package weblogic.server.channels;

import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import weblogic.management.provider.ManagementService;
import weblogic.protocol.ProtocolHandlerAdmin;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.configuration.ChannelHelper;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.server.AbstractServerService;
import weblogic.server.ServiceFailureException;
import weblogic.utils.Debug;

public class AdminPortService extends AbstractServerService {
   private static boolean DEBUG = false;
   private HashMap adminListeners = new HashMap(11);
   private static AdminPortService singleton;
   private boolean listenersBound = false;

   public AdminPortService() {
      singleton = this;
   }

   public static AdminPortService getInstance() {
      Debug.assertion(singleton != null);
      return singleton;
   }

   public void start() throws ServiceFailureException {
      AuthenticatedSubject var1 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
      if (ManagementService.getRuntimeAccess(var1).getServer().getListenersBindEarly()) {
         this.bindListeners();
      }

   }

   synchronized void bindListeners() throws ServiceFailureException {
      if (!this.listenersBound) {
         if (ChannelHelper.isLocalAdminChannelEnabled()) {
            if (DEBUG) {
               p("start()");
            }

            try {
               if (DEBUG) {
                  ChannelService.dumpTables();
               }

               DynamicListenThreadManager var1 = DynamicListenThreadManager.getInstance();
               ArrayList var2 = ChannelService.findInboundServerChannels(ProtocolHandlerAdmin.PROTOCOL_ADMIN);
               if (DEBUG) {
                  p("admin channels: " + var2);
               }

               Iterator var3 = var2.iterator();

               while(var3.hasNext()) {
                  ServerChannel var4 = (ServerChannel)var3.next();
                  ArrayList var5 = ChannelService.getDiscriminantSet(var4.getListenerKey());
                  if (this.adminListeners.get(var4.getListenerKey()) == null) {
                     DynamicListenThread var6 = var1.createListener(var5);
                     var6.setAdminChannel(true);
                     this.adminListeners.put(var6.getKey(), var6);
                     if (!var6.start(true, false, true)) {
                        throw new ServiceFailureException("Failed to start admin channel " + var4.getChannelName());
                     }
                  }
               }
            } catch (IOException var7) {
               throw new ServiceFailureException(var7);
            }

            this.listenersBound = true;
         } else if (DEBUG) {
            p("start() skipped - no admin channels");
         }

      }
   }

   synchronized void addListener(DynamicListenThread var1) {
      if (var1 != null) {
         this.adminListeners.put(var1.getKey(), var1);
      }
   }

   synchronized DynamicListenThread findListener(ServerChannel var1) {
      return (DynamicListenThread)this.adminListeners.get(var1.getListenerKey());
   }

   synchronized void removeListener(DynamicListenThread var1) {
      this.adminListeners.remove(var1.getKey());
   }

   DynamicListenThread stopListener(ServerChannel var1) throws IOException {
      return DynamicListenThreadManager.getInstance().stopListener(var1, this.adminListeners);
   }

   DynamicListenThread restartListener(ServerChannel var1) throws IOException {
      return DynamicListenThreadManager.getInstance().restartListener(var1, this.adminListeners, true);
   }

   public void enable() {
      if (DEBUG) {
         p("enable()");
      }

      DynamicListenThreadManager.getInstance().enableListeners(this.adminListeners);
   }

   public void stop() {
      this.halt();
   }

   public synchronized void halt() {
      if (DEBUG) {
         p("halt()");
      }

      DynamicListenThreadManager.getInstance().stopListeners(this.adminListeners);
      this.listenersBound = false;
   }

   public synchronized boolean listenersBound() {
      return this.listenersBound;
   }

   private static void p(String var0) {
      System.out.println("<AdminPortService>: " + var0);
   }
}
