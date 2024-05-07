package weblogic.cluster.leasing.databaseless;

import java.util.Set;
import weblogic.cluster.ClusterService;
import weblogic.cluster.messaging.internal.BaseClusterMessage;
import weblogic.cluster.messaging.internal.ServerInformation;
import weblogic.cluster.messaging.internal.ServerInformationImpl;

class LeaseMessage extends BaseClusterMessage {
   static final long serialVersionUID = 4300768677385372050L;
   static final String ACQUIRE = "acquire";
   static final String RELEASE = "release";
   static final String FIND_OWNER = "find_owner";
   static final String FIND_PREVIOUS_OWNER = "find_previous_owner";
   static final String EXPIRED_LEASES = "expired";
   static final String RENEW_ALL = "renew_all";
   static final String RENEW_LEASES = "renew_leases";
   private final String leaseName;
   private final String owner;
   private final int leaseTimeout;
   private final int healthCheckPeriod;
   private String requestType;
   private final int gracePeriod;
   private final Set leasesToRenew;

   LeaseMessage(String var1, String var2, int var3) {
      super(getLocalInformation(), 4);
      this.leaseName = var1;
      this.owner = var2;
      this.leaseTimeout = var3;
      this.healthCheckPeriod = -1;
      this.requestType = "acquire";
      this.gracePeriod = -1;
      this.leasesToRenew = null;
   }

   LeaseMessage(String var1, String var2) {
      super(getLocalInformation(), 4);
      this.leaseName = var1;
      this.owner = var2;
      this.leaseTimeout = -1;
      this.healthCheckPeriod = -1;
      this.requestType = "release";
      this.gracePeriod = -1;
      this.leasesToRenew = null;
   }

   public LeaseMessage(String var1) {
      super(getLocalInformation(), 4);
      this.leaseName = var1;
      this.owner = null;
      this.leaseTimeout = -1;
      this.healthCheckPeriod = -1;
      this.requestType = "find_owner";
      this.gracePeriod = -1;
      this.leasesToRenew = null;
   }

   public LeaseMessage(int var1) {
      super(getLocalInformation(), 4);
      this.leaseName = null;
      this.owner = null;
      this.leaseTimeout = -1;
      this.healthCheckPeriod = -1;
      this.requestType = "expired";
      this.gracePeriod = var1;
      this.leasesToRenew = null;
   }

   LeaseMessage(int var1, String var2) {
      super(getLocalInformation(), 4);
      this.leaseName = null;
      this.owner = var2;
      this.leaseTimeout = -1;
      this.healthCheckPeriod = var1;
      this.gracePeriod = -1;
      this.requestType = "renew_all";
      this.leasesToRenew = null;
   }

   LeaseMessage(String var1, Set var2, int var3) {
      super(getLocalInformation(), 4);
      this.leaseName = null;
      this.owner = var1;
      this.leaseTimeout = -1;
      this.healthCheckPeriod = var3;
      this.gracePeriod = -1;
      this.requestType = "renew_leases";
      this.leasesToRenew = var2;
   }

   static LeaseMessage createFindPreviousOwnerMessage(String var0) {
      LeaseMessage var1 = new LeaseMessage(var0);
      var1.requestType = "find_previous_owner";
      return var1;
   }

   static LeaseMessage findExpiredLeasesMessage(int var0) {
      LeaseMessage var1 = new LeaseMessage(var0);
      return var1;
   }

   public String getRequestType() {
      return this.requestType;
   }

   public String getLeaseName() {
      return this.leaseName;
   }

   public String getOwner() {
      return this.owner;
   }

   public int getLeaseTimeout() {
      return this.leaseTimeout;
   }

   public int getHealthCheckPeriod() {
      return this.healthCheckPeriod;
   }

   public int getGracePeriod() {
      return this.gracePeriod;
   }

   public Set getLeasesToRenew() {
      return this.leasesToRenew;
   }

   public String toString() {
      String var1 = this.requestType.equalsIgnoreCase("renew_leases") ? this.leasesToRenew.toString() : this.leaseName;
      return "[LeaseMessage " + this.requestType + ", leaseNames " + var1 + ", owner " + this.owner + ", leaseTimeout " + this.leaseTimeout + ", healthCheckPeriod " + this.healthCheckPeriod + ", gracePeriod " + this.gracePeriod + "]";
   }

   static ServerInformation getLocalInformation() {
      return new ServerInformationImpl(ClusterService.getClusterService().getLocalMember());
   }
}
