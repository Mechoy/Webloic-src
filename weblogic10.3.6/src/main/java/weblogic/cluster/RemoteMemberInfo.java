package weblogic.cluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.common.internal.VersionInfo;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.rmi.spi.HostID;
import weblogic.server.ServiceFailureException;
import weblogic.work.WorkAdapter;
import weblogic.work.WorkManagerFactory;

final class RemoteMemberInfo {
   private JVMID memberID;
   private MemberServices services;
   private MemberAttributes attributes;
   private ArrayList receiverList;
   private int checksLeft;
   int numUnprocessedMessages;
   private RJVM rjvm;
   private long localJoinTime;
   private int srvrRuntimeState = 9;

   RemoteMemberInfo(HostID var1, long var2) {
      this.memberID = (JVMID)var1;
      this.services = new MemberServices(var1);
      this.attributes = null;
      this.receiverList = new ArrayList();
      this.checksLeft = MemberManager.theOne().getIdlePeriodsUntilTimeout();
      this.rjvm = RJVMManager.getRJVMManager().find(this.memberID);
      if (this.rjvm != null) {
         this.rjvm.addPeerGoneListener(MemberManager.theOne());
      }

      this.localJoinTime = var2;
   }

   MemberAttributes getAttributes() {
      return this.attributes;
   }

   RJVM getRJVM() {
      if (this.rjvm == null) {
         this.rjvm = RJVMManager.getRJVMManager().find(this.memberID);
         if (this.rjvm != null) {
            this.rjvm.addPeerGoneListener(MemberManager.theOne());
         }
      }

      return this.rjvm;
   }

   void resetTimeout() {
      this.checksLeft = MemberManager.theOne().getIdlePeriodsUntilTimeout();
      RJVM var1 = this.getRJVM();
      if (var1 != null) {
         this.rjvm.messageReceived();
      }

      AlternateLivelinessChecker.getInstance().reachable(this.memberID);
   }

   boolean checkTimeout() {
      if (this.checksLeft > 0) {
         --this.checksLeft;
         return false;
      } else {
         MulticastReceiver var1 = this.findOrCreateReceiver(2, true);
         return var1 == null ? true : AlternateLivelinessChecker.getInstance().isUnreachable(var1.getCurrentSeqNum(), this.memberID);
      }
   }

   MemberServices getMemberServices() {
      return this.services;
   }

   void forceTimeout() {
      this.checksLeft = 0;
   }

   void shutdown() {
      this.updateRuntimeState(9);
   }

   private void shutdownInternal() {
      synchronized(this.receiverList) {
         Iterator var2 = this.receiverList.iterator();

         while(var2.hasNext()) {
            MulticastReceiver var3 = (MulticastReceiver)var2.next();
            if (var3 != null) {
               var3.shutdown();
            }
         }

         this.receiverList.clear();
      }
   }

   private void suspend() {
      if (this.attributes != null) {
         MemberManager.theOne().fireClusterMembersChangeEvent(this.attributes, 1);
      }

      synchronized(this.services) {
         this.services.retractAllOffers(false);
      }

      MulticastReceiver var1 = this.findOrCreateReceiver(2, true);
      var1.clear();
   }

   MulticastReceiver findOrCreateReceiver(int var1, boolean var2) {
      synchronized(this.receiverList) {
         while(this.receiverList.size() <= var1) {
            this.receiverList.add((Object)null);
         }

         Object var4 = (MulticastReceiver)this.receiverList.get(var1);
         if (var4 == null) {
            if (var2) {
               var4 = new HybridMulticastReceiver(this.memberID, var1, WorkManagerFactory.getInstance().getSystem());
            } else {
               var4 = new MulticastReceiver(this.memberID, var1, ClusterService.MULTICAST_WORKMANAGER);
            }

            this.receiverList.set(var1, var4);
         }

         return (MulticastReceiver)var4;
      }
   }

   List<MulticastReceiver> getReceivers() {
      ArrayList var1 = new ArrayList();
      synchronized(this.receiverList) {
         Iterator var3 = this.receiverList.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();
            if (var4 instanceof MulticastReceiver) {
               var1.add((MulticastReceiver)var4);
            }
         }

         return var1;
      }
   }

   synchronized void processAttributes(MemberAttributes var1) {
      if (this.attributes == null) {
         if (!VersionInfo.theOne().compatible(var1.version())) {
            ClusterLogger.logIncompatibleVersionsError(VersionInfo.theOne().getReleaseVersion(), var1.serverName(), var1.version());
            if (var1.joinTime() <= this.localJoinTime) {
               ClusterLogger.logIncompatibleServerLeavingCluster();
               WorkAdapter var2 = new WorkAdapter() {
                  public void run() {
                     try {
                        ClusterService.getClusterService().getActivator().stop();
                     } catch (ServiceFailureException var2) {
                        var2.printStackTrace();
                     }

                  }
               };
               WorkManagerFactory.getInstance().getSystem().schedule(var2);
            }
         } else {
            ClusterMemberInfo var3 = ClusterService.getServices().getLocalMember();
            if (!var3.domainName().equals(var1.domainName())) {
               ClusterLogger.logMultipleDomainsCannotUseSameMulticastAddress(var3.domainName(), var1.domainName());
            } else if (!var3.clusterName().equals(var1.clusterName())) {
               ClusterLogger.logMultipleClustersCannotUseSameMulticastAddress(var3.clusterName(), var1.clusterName());
            } else {
               this.attributes = var1;
               this.updateRuntimeState(2);
            }
         }
      } else {
         MemberManager.theOne().fireClusterMembersChangeEvent(var1, 2);
      }

   }

   void processAnnouncement(Collection var1) {
      synchronized(this.services) {
         Iterator var3 = var1.iterator();

         while(var3.hasNext()) {
            Object var4 = var3.next();

            try {
               ServiceRetract var10 = (ServiceRetract)var4;
               if (!var10.ignoreRetract()) {
                  this.services.processRetract(var10, false);
               }
            } catch (ClassCastException var8) {
               try {
                  ServiceOffer var5 = (ServiceOffer)var4;
                  if (var5.getOldID() != -1) {
                     this.services.processUpdate(var5, false, var5.getOldID());
                  } else {
                     this.services.processOffer(var5, false);
                  }
               } catch (ClassCastException var7) {
               }
            }
         }

      }
   }

   void processStateDump(Collection var1, int var2, long var3) {
      synchronized(this.services) {
         this.services.retractAllOffers(false);
         synchronized(var1) {
            Iterator var7 = var1.iterator();

            while(true) {
               if (!var7.hasNext()) {
                  break;
               }

               ServiceOffer var8 = (ServiceOffer)var7.next();
               this.services.processOffer(var8, false);
            }
         }

         MulticastReceiver var6 = this.findOrCreateReceiver(var2, true);
         var6.setInSync(var3);
      }
   }

   public String toString() {
      return this.memberID.getHostAddress().toString();
   }

   void updateRuntimeState(int var1) {
      switch (this.srvrRuntimeState) {
         case 2:
            if (var1 == 17) {
               this.suspend();
            } else if (var1 == 9) {
               this.suspend();
               this.shutdownInternal();
            }
            break;
         case 9:
            if (var1 == 2) {
               this.add();
            } else if (var1 == 17) {
               this.discover();
            } else if (var1 == 9) {
               this.suspend();
               this.shutdownInternal();
            }
            break;
         case 17:
            if (var1 == 2) {
               this.add();
            } else if (var1 == 9) {
               this.shutdownInternal();
            }
      }

      this.srvrRuntimeState = var1;
   }

   boolean isRunning() {
      return this.srvrRuntimeState == 2;
   }

   boolean isSuspended() {
      return this.srvrRuntimeState == 17;
   }

   private void add() {
      MemberManager.theOne().fireClusterMembersChangeEvent(this.attributes, 0);
      ClusterLogger.logAddingServer(this.attributes.serverName(), this.attributes.clusterName(), this.attributes.identity().toString());
   }

   private void discover() {
      MemberManager.theOne().fireClusterMembersChangeEvent(this.attributes, 3);
   }

   void dumpDiagnosticImageData(XMLStreamWriter var1) throws XMLStreamException, IOException {
      var1.writeStartElement("RemoteMemberInfo");
      var1.writeAttribute("HostAddress", this.toString());
      Iterator var2 = this.getReceivers().iterator();

      while(var2.hasNext()) {
         MulticastReceiver var3 = (MulticastReceiver)var2.next();
         var3.dumpDiagnosticImageData(var1);
      }

      var1.writeEndElement();
   }
}
