package weblogic.wsee.mc.cluster;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.addressing.WSEndpointReference;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.SOAPException;
import weblogic.wsee.WseeMCLogger;
import weblogic.wsee.config.WebServiceMBeanFactory;
import weblogic.wsee.jaxws.cluster.spi.AffinityBasedRoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinderRegistry;
import weblogic.wsee.mc.messages.McMsg;
import weblogic.wsee.mc.tube.McTubeUtils;
import weblogic.wsee.mc.utils.McConstants;

public class McAnonIDRoutingInfoFinder implements AffinityBasedRoutingInfoFinder {
   private static final Logger LOGGER = Logger.getLogger(McAnonIDRoutingInfoFinder.class.getName());
   private static boolean _didRegister = false;
   private final McAffinityStore _store;
   private final ReentrantReadWriteLock _affinityLock;

   public static void registerIfNeeded() {
      Class var0 = McAnonIDRoutingInfoFinder.class;
      synchronized(McAnonIDRoutingInfoFinder.class) {
         if (!_didRegister) {
            _didRegister = true;
            McAnonIDRoutingInfoFinder var1 = new McAnonIDRoutingInfoFinder();
            RoutingInfoFinderRegistry.getInstance().addFinder(var1);
         }

      }
   }

   public McAnonIDRoutingInfoFinder() {
      String var1 = WebServiceMBeanFactory.getInstance().getWebServicePersistence().getDefaultLogicalStoreName();

      try {
         this._store = McAffinityStore.getStore(var1);
         this._affinityLock = new ReentrantReadWriteLock();
      } catch (Exception var3) {
         throw new RuntimeException(var3.toString(), var3);
      }
   }

   public void setUsageMode(RoutingInfoFinder.UsageMode var1) {
   }

   public int getFinderPriority() {
      return 200;
   }

   public RoutingInfo findRoutingInfo(HeaderList var1) throws Exception {
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Searching headers for routing info");
      }

      AddressingVersion[] var2 = AddressingVersion.values();
      int var3 = var2.length;

      int var4;
      int var8;
      for(var4 = 0; var4 < var3; ++var4) {
         AddressingVersion var5 = var2[var4];
         SOAPVersion[] var6 = SOAPVersion.values();
         int var7 = var6.length;

         for(var8 = 0; var8 < var7; ++var8) {
            SOAPVersion var9 = var6[var8];
            String var10 = var1.getAction(var5, var9);
            if (var10 != null && McConstants.Action.MC.matchesAnyMCVersion(var10)) {
               return new RoutingInfo(var10, RoutingInfo.Type.NEED_BODY);
            }
         }
      }

      WSEndpointReference var13 = null;
      AddressingVersion[] var14 = AddressingVersion.values();
      var4 = var14.length;

      int var17;
      int var23;
      for(var17 = 0; var17 < var4; ++var17) {
         AddressingVersion var18 = var14[var17];
         SOAPVersion[] var20 = SOAPVersion.values();
         var8 = var20.length;

         for(var23 = 0; var23 < var8; ++var23) {
            SOAPVersion var24 = var20[var23];
            var13 = var1.getReplyTo(var18, var24);
            if (var13 != null) {
               if (McTubeUtils.isMcAnonURI(var13)) {
                  String var11 = McTubeUtils.getUUID(var13);
                  return this.lookupRoutingInfo(var11);
               }
               break;
            }
         }

         if (var13 != null) {
            break;
         }
      }

      WSEndpointReference var15 = null;
      AddressingVersion[] var16 = AddressingVersion.values();
      var17 = var16.length;

      for(int var19 = 0; var19 < var17; ++var19) {
         AddressingVersion var21 = var16[var19];
         SOAPVersion[] var22 = SOAPVersion.values();
         var23 = var22.length;

         for(int var25 = 0; var25 < var23; ++var25) {
            SOAPVersion var26 = var22[var25];
            var15 = var1.getFaultTo(var21, var26);
            if (var15 != null) {
               if (McTubeUtils.isMcAnonURI(var15)) {
                  String var12 = McTubeUtils.getUUID(var15);
                  return this.lookupRoutingInfo(var12);
               }
               break;
            }
         }

         if (var15 != null) {
            break;
         }
      }

      return new RoutingInfo((String)null, RoutingInfo.Type.ABSTAIN);
   }

   public RoutingInfo findRoutingInfoFromSoapBody(RoutingInfo var1, Packet var2) throws Exception {
      Message var3 = var2.getMessage();
      if (LOGGER.isLoggable(Level.FINE)) {
         LOGGER.fine("Finding MC routing info from MC msg within SOAP body");
      }

      McMsg var4 = new McMsg();

      try {
         var4.readFromSOAPMsg(var3.readAsSOAPMessage());
      } catch (SOAPException var14) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "McAnonIDRoutingInfoFinder failed: exception reading SOAP message: " + var14);
         }

         WseeMCLogger.logUnexpectedException(var14.toString(), var14);
         throw var14;
      }

      String var5 = var4.getAddress();
      if (var5 == null) {
         return new RoutingInfo((String)null, RoutingInfo.Type.ABSTAIN);
      } else {
         boolean var6 = McTubeUtils.isMcAnonURI(var5);
         if (!var6) {
            return new RoutingInfo((String)null, RoutingInfo.Type.ABSTAIN);
         } else {
            String var7 = McTubeUtils.getUUID(var5);

            RoutingInfo var8;
            try {
               this._affinityLock.readLock().lock();
               var8 = (RoutingInfo)this._store.get(var7);
            } finally {
               this._affinityLock.readLock().unlock();
            }

            if (var8 == null) {
               var8 = new RoutingInfo((String)null, RoutingInfo.Type.ABSTAIN);
            }

            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.log(Level.FINE, "McAnonIDRoutingInfoFinder: returning routing info: " + var8.toString());
            }

            return var8;
         }
      }
   }

   public void recordRoutingIDAffinity(RoutingInfo var1, RoutingInfo var2) {
      if (var2 != null) {
         String var3 = var1.getName();
         if (var3 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("MC finder associating anon URI '" + var3 + "' with routing info: " + var2);
            }

            try {
               this._affinityLock.writeLock().lock();
               RoutingInfo var4 = (RoutingInfo)this._store.remove(var3);
               this._store.put(var3, var2);
               if (var4 != null && var4.getType() == RoutingInfo.Type.ABSTAIN) {
                  synchronized(var4) {
                     var4.notifyAll();
                  }
               }
            } finally {
               this._affinityLock.writeLock().unlock();
            }
         }
      }

   }

   private RoutingInfo lookupRoutingInfo(String var1) {
      try {
         this._affinityLock.writeLock().lock();
         RoutingInfo var2 = (RoutingInfo)this._store.get(var1);
         RoutingInfo var3;
         if (var2 == null) {
            var2 = new RoutingInfo(var1, RoutingInfo.Type.ABSTAIN);
            this._store.put(var1, var2);
            var3 = var2;
            return var3;
         } else {
            for(; var2.getType() == RoutingInfo.Type.ABSTAIN; var2 = (RoutingInfo)this._store.get(var1)) {
               synchronized(var2) {
                  try {
                     this._affinityLock.writeLock().unlock();
                     var2.wait();
                     this._affinityLock.writeLock().lock();
                  } catch (InterruptedException var11) {
                     this._affinityLock.writeLock().lock();
                  }
               }
            }

            var3 = var2;
            return var3;
         }
      } finally {
         this._affinityLock.writeLock().unlock();
      }
   }
}
