package weblogic.wsee.jaxws.cluster;

import com.sun.xml.ws.api.SOAPVersion;
import com.sun.xml.ws.api.addressing.AddressingVersion;
import com.sun.xml.ws.api.message.HeaderList;
import com.sun.xml.ws.api.message.Message;
import com.sun.xml.ws.api.message.Packet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weblogic.wsee.WseeCoreLogger;
import weblogic.wsee.jaxws.cluster.spi.AffinityBasedRoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfo;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinder;
import weblogic.wsee.jaxws.cluster.spi.RoutingInfoFinderRegistry;
import weblogic.wsee.monitoring.WseeClusterRoutingRuntimeMBeanImpl;

public abstract class BaseSOAPRouter<S, R extends BaseRoutables> {
   private static final Logger LOGGER = Logger.getLogger(BaseSOAPRouter.class.getName());
   private WseeClusterRoutingRuntimeMBeanImpl _clusterRouting;

   public void init(WseeClusterRoutingRuntimeMBeanImpl var1) {
      this._clusterRouting = var1;
   }

   protected abstract S getTargetServerForRouting(RoutingInfo var1) throws DeliveryException;

   protected abstract RoutingInfo getRoutingForTargetServer(S var1);

   protected abstract R deliverMessageToTargetServer(R var1, S var2) throws Exception;

   protected abstract R setAbstainedFinders(R var1, Map<AffinityBasedRoutingInfoFinder, RoutingInfo> var2);

   protected String getMessageId(Message var1) {
      AddressingVersion[] var2 = AddressingVersion.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AddressingVersion var5 = var2[var4];
         SOAPVersion[] var6 = SOAPVersion.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            SOAPVersion var9 = var6[var8];
            String var10 = var1.getHeaders().getMessageID(var5, var9);
            if (var10 != null) {
               return var10;
            }
         }
      }

      return null;
   }

   protected String getRelatesTo(Message var1) {
      AddressingVersion[] var2 = AddressingVersion.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         AddressingVersion var5 = var2[var4];
         SOAPVersion[] var6 = SOAPVersion.values();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            SOAPVersion var9 = var6[var8];
            String var10 = var1.getHeaders().getRelatesTo(var5, var9);
            if (var10 != null) {
               return var10;
            }
         }
      }

      return null;
   }

   public R route(R var1) throws Exception {
      Message var2 = var1.packet.getMessage();
      if (LOGGER.isLoggable(Level.FINE)) {
         String var3 = this.getMessageId(var2);
         String var4 = "";
         if (var3 != null) {
            var4 = "ID: " + var3;
         }

         LOGGER.fine(this.getClass().getSimpleName() + " handling inbound message " + var4);
      }

      boolean var18 = this.getRelatesTo(var2) != null;
      if (this._clusterRouting != null) {
         if (var18) {
            this._clusterRouting.incrementResponseCount();
         } else {
            this._clusterRouting.incrementRequestCount();
         }
      }

      RoutingInfoFinder[] var19 = RoutingInfoFinderRegistry.getInstance().getFinders();
      if (LOGGER.isLoggable(Level.FINE)) {
         StringBuffer var5 = new StringBuffer("RoutingInfoFinders: ");

         for(int var6 = 0; var6 < var19.length; ++var6) {
            var5.append(var19[var6].getClass().getSimpleName());
            if (var6 < var19.length - 1) {
               var5.append(", ");
            }
         }

         LOGGER.fine(var5.toString());
      }

      HashMap var20 = new HashMap();
      RoutingInfo var21 = null;
      HeaderList var7 = var2.getHeaders();
      RoutingInfoFinder[] var8 = var19;
      int var9 = var19.length;

      RoutingInfo var12;
      for(int var10 = 0; var10 < var9; ++var10) {
         RoutingInfoFinder var11 = var8[var10];

         try {
            if (var21 == null || var11 instanceof AffinityBasedRoutingInfoFinder) {
               var12 = var11.findRoutingInfo(var7);
               if (var12 != null) {
                  if (var12.getType() == RoutingInfo.Type.NEED_BODY) {
                     if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Finder " + var11 + " determined it could find routing info in the SOAP body, allowing it to parse the body now.");
                     }

                     var12 = var11.findRoutingInfoFromSoapBody(var12, var1.packet);
                     if (var12 == null) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                           LOGGER.fine("Finder " + var11 + " did not find routing info in the SOAP body, moving on to next finder");
                        }
                        continue;
                     }
                  }

                  if (var12.getType() == RoutingInfo.Type.ABSTAIN) {
                     if (var11 instanceof AffinityBasedRoutingInfoFinder) {
                        var20.put((AffinityBasedRoutingInfoFinder)var11, var12);
                     }
                  } else if (var21 == null) {
                     var21 = var12;
                  }
               }
            }
         } catch (Exception var17) {
            this.logRoutingFailure(var17);
            throw var17;
         }
      }

      BaseRoutables var22;
      if (var21 != null) {
         Object var23;
         try {
            var23 = this.getTargetServerForRouting(var21);
         } catch (DeliveryException var15) {
            String var25 = "Couldn't get target server for msg ID '" + this.getMessageId(var2) + "': " + var15.toString();
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine(var25);
            }

            this.logRoutingFailure(var15);
            throw new DeliveryException(var25, var15);
         }

         if (var23 != null) {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Final SOAP routing decision is " + var21.toString() + " for server: " + var23);
            }

            if (this._clusterRouting != null) {
               if (var18) {
                  this._clusterRouting.incrementRoutedResponseCount();
               } else {
                  this._clusterRouting.incrementRoutedRequestCount();
               }
            }

            Iterator var24 = var20.keySet().iterator();

            while(var24.hasNext()) {
               AffinityBasedRoutingInfoFinder var26 = (AffinityBasedRoutingInfoFinder)var24.next();
               var12 = (RoutingInfo)var20.get(var26);

               try {
                  var26.recordRoutingIDAffinity(var12, var21);
               } catch (Exception var14) {
                  this.logRoutingFailure(var14);
               }
            }

            try {
               var22 = this.deliverMessageToTargetServer(var1, var23);
            } catch (Exception var16) {
               if (LOGGER.isLoggable(Level.WARNING)) {
                  LOGGER.log(Level.WARNING, "Delivery Failure: " + var16.toString(), var16);
               }

               this.logRoutingFailure(var16);
               throw new DeliveryException(var16.toString(), var16);
            }
         } else {
            if (LOGGER.isLoggable(Level.FINE)) {
               LOGGER.fine("Couldn't get target server for final SOAP routing decision " + var21.toString());
            }

            var22 = null;
         }
      } else {
         var22 = null;
      }

      if (var22 == null) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("No SOAP routing decision for this message");
         }

         if (var20.size() > 0) {
            var22 = this.setAbstainedFinders(var1, var20);
         }
      }

      return var22;
   }

   public void notifyRoutingDecision(S var1, Map<AffinityBasedRoutingInfoFinder, RoutingInfo> var2) {
      RoutingInfo var3 = this.getRoutingForTargetServer(var1);
      Iterator var4 = var2.keySet().iterator();

      while(var4.hasNext()) {
         AffinityBasedRoutingInfoFinder var5 = (AffinityBasedRoutingInfoFinder)var4.next();
         RoutingInfo var6 = (RoutingInfo)var2.get(var5);

         try {
            var5.recordRoutingIDAffinity(var6, var3);
         } catch (Exception var8) {
            this.logRoutingFailure(var8);
         }
      }

   }

   protected void logRoutingFailure(Exception var1) {
      if (this._clusterRouting != null) {
         this._clusterRouting.incrementRoutingFailureCount();
         this._clusterRouting.setLastRoutingFailure(var1.toString());
         this._clusterRouting.setLastRoutingFailureTime(System.currentTimeMillis());
      }

      if (!(var1 instanceof DeliveryException)) {
         if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.log(Level.SEVERE, var1.toString(), var1);
         }

         WseeCoreLogger.logUnexpectedException(var1.toString(), var1);
      }

   }

   public static class BaseRoutables {
      public Packet packet;

      public BaseRoutables(Packet var1) {
         this.packet = var1;
      }
   }
}
