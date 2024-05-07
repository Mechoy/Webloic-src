package weblogic.wsee.wstx.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import weblogic.store.PersistentHandle;
import weblogic.transaction.XIDFactory;
import weblogic.wsee.wstx.wsat.WSATHelper;

public class BranchRecord implements Externalizable {
   private static final long serialVersionUID = -8663994789749988958L;
   private static final int VERSION = 1;
   private Xid globalXid;
   private List<RegisteredResource> registeredResources;
   private boolean isPrimaryBranchCompleted = false;
   private String branchAliasSuffix = "BI_WSATGatewayRM";
   private PersistentHandle storeHandle;
   private boolean logged;

   public BranchRecord() {
      this.registeredResources = new ArrayList();
   }

   BranchRecord(Xid var1) {
      this.globalXid = var1;
      this.registeredResources = new ArrayList();
   }

   public synchronized RegisteredResource addSubordinate(XAResource var1) {
      RegisteredResource var2 = new RegisteredResource(var1);
      this.registeredResources.add(var2);
      return var2;
   }

   synchronized String getBranchName(XAResource var1) {
      int var2 = this.getResourceIndex(var1);
      if (var2 == -1) {
         throw new IllegalStateException("WS-AT resource not associated with transaction branch " + this.globalXid);
      } else {
         return var2 + this.branchAliasSuffix;
      }
   }

   void setStoreHandle(PersistentHandle var1) {
      this.storeHandle = var1;
   }

   PersistentHandle getStoreHandle() {
      return this.storeHandle;
   }

   void setLogged(boolean var1) {
      this.logged = var1;
   }

   boolean isLogged() {
      return this.logged;
   }

   int prepare(Xid var1) throws XAException {
      if (this.isPrimaryBranch(var1)) {
         this.debug("prepare() xid=" + var1 + " returning XA_OK");
         return 0;
      } else {
         RegisteredResource var2 = this.getRegisteredResource(var1);
         int var3 = 0;

         try {
            var3 = var2.prepare(var1);
         } catch (XAException var5) {
            switch (var5.errorCode) {
               case -4:
                  JTAHelper.throwXAException(106, "Subordinate resource timeout.", var5);
                  break;
               case 100:
                  throw var5;
               default:
                  throw var5;
            }
         }

         return var3;
      }
   }

   void rollback(Xid var1) throws XAException {
      if (this.isPrimaryBranch(var1)) {
         this.debug("rollback() xid=" + var1 + " ignoring primary branch ");
      }

      RegisteredResource var2 = this.getRegisteredResource(var1);

      try {
         var2.rollback(var1);
      } catch (XAException var4) {
         switch (var4.errorCode) {
            case -4:
               break;
            case 5:
            case 7:
            case 8:
               throw var4;
            default:
               throw var4;
         }
      }

   }

   void commit(Xid var1, boolean var2) throws XAException {
      if (this.isPrimaryBranch(var1)) {
         this.debug("commit() xid=" + var1 + " setting primary branch as completed");
         this.isPrimaryBranchCompleted = true;
      } else {
         RegisteredResource var3 = this.getRegisteredResource(var1);

         try {
            var3.commit(var1, var2);
         } catch (XAException var5) {
            switch (var5.errorCode) {
               case -4:
                  break;
               case 5:
               case 6:
               case 8:
                  throw var5;
               default:
                  throw var5;
            }
         }

      }
   }

   private synchronized int getResourceIndex(XAResource var1) {
      for(int var2 = 0; var2 < this.registeredResources.size(); ++var2) {
         RegisteredResource var3 = (RegisteredResource)this.registeredResources.get(var2);
         if (var1.equals(var3.resource)) {
            return var2;
         }
      }

      return -1;
   }

   XAResource exists(XAResource var1) {
      int var2 = this.getResourceIndex(var1);
      return var2 == -1 ? null : ((RegisteredResource)this.registeredResources.get(var2)).resource;
   }

   boolean isPrimaryBranch(Xid var1) {
      return Arrays.equals(var1.getBranchQualifier(), this.globalXid.getBranchQualifier());
   }

   private int getResourceIndex(Xid var1) {
      String var2 = new String(var1.getBranchQualifier());
      int var3 = var2.indexOf(this.branchAliasSuffix);
      if (var3 == -1) {
         return -1;
      } else {
         String var4 = var2.substring(0, var3);
         int var5 = Integer.parseInt(var4);
         return var5;
      }
   }

   private synchronized RegisteredResource getRegisteredResource(Xid var1) throws XAException {
      int var2 = this.getResourceIndex(var1);
      if (var2 == -1) {
         JTAHelper.throwXAException(-4, "This may be expected during rollback if client and web service are collocated, Xid=" + var1);
      }

      RegisteredResource var3 = (RegisteredResource)this.registeredResources.get(var2);
      if (var3 == null) {
         JTAHelper.throwXAException(-4, "Xid=" + var1);
      }

      if (var3.getBranchXid() != null) {
         boolean var4 = Arrays.equals(var3.getBranchXid().getBranchQualifier(), var1.getBranchQualifier());
         if (!var4) {
            WSATHelper.getInstance();
            if (WSATHelper.isDebugEnabled()) {
               byte[] var5 = var3.getBranchXid().getBranchQualifier();
               if (var5 == null) {
                  var5 = new byte[0];
               }

               WSATHelper.getInstance().debug("WSAT Branch registered branchId:\t[" + new String(var5) + "] ");
               var5 = var1.getBranchQualifier();
               if (var5 == null) {
                  var5 = new byte[0];
               }

               WSATHelper.getInstance().debug("WSAT Branch branchId used to identify a registered resource:\t[" + new String(var5) + "] ");
            }

            this.debug("prepare() xid=" + var1 + " returning XA_RDONLY");
            JTAHelper.throwXAException(-4, "xid=" + var1);
         }
      }

      return var3;
   }

   Xid getXid() {
      return this.globalXid;
   }

   synchronized Collection<Xid> getAllXids() {
      ArrayList var1 = new ArrayList();

      for(int var2 = 0; var2 < this.registeredResources.size(); ++var2) {
         var1.add(((RegisteredResource)this.registeredResources.get(var2)).getBranchXid());
      }

      return var1;
   }

   void assignBranchXid(Xid var1) {
      int var2 = this.getResourceIndex(var1);
      if (var2 != -1) {
         RegisteredResource var3 = (RegisteredResource)this.registeredResources.get(var2);
         if (var3 != null) {
            if (var3.getBranchXid() == null) {
               var3.setBranchXid(var1);
            }

         }
      }
   }

   synchronized boolean allResourcesCompleted() {
      if (!this.isPrimaryBranchCompleted) {
         return false;
      } else {
         int var1 = 0;

         for(int var2 = this.registeredResources.size(); var1 < var2; ++var1) {
            RegisteredResource var3 = (RegisteredResource)this.registeredResources.get(var1);
            if (!var3.isCompleted()) {
               return false;
            }
         }

         return true;
      }
   }

   private void debug(String var1) {
      WSATHelper.getInstance();
      if (WSATHelper.isDebugEnabled()) {
         WSATHelper.getInstance().debug("[WSAT Branch " + this.globalXid + "] " + var1);
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeInt(1);
      var1.writeInt(this.globalXid.getFormatId());
      byte[] var2 = this.globalXid.getGlobalTransactionId();
      var1.writeByte((byte)var2.length);
      var1.write(var2);
      byte[] var3 = this.globalXid.getBranchQualifier();
      if (var3 == null) {
         var1.writeByte(-1);
      } else {
         var1.writeByte((byte)var3.length);
         var1.write(var3);
      }

      var1.writeInt(this.registeredResources.size());

      for(int var4 = 0; var4 < this.registeredResources.size(); ++var4) {
         RegisteredResource var5 = (RegisteredResource)this.registeredResources.get(var4);
         var5.writeExternal(var1);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      int var2 = var1.readInt();
      if (var2 != 1) {
         throw new IOException("invalid OTSBranch version " + var2);
      } else {
         int var3 = var1.readInt();
         byte var4 = var1.readByte();
         byte[] var5 = new byte[var4];
         var1.readFully(var5);
         var4 = var1.readByte();
         byte[] var6 = null;
         if (var4 > -1) {
            var6 = new byte[var4];
            var1.readFully(var6);
         }

         this.globalXid = XIDFactory.createXID(var3, var5, var6);
         int var7 = var1.readInt();

         for(int var8 = 0; var8 < var7; ++var8) {
            RegisteredResource var9 = new RegisteredResource();
            var9.readExternal(var1);
            var9.setPrepared();
            this.registeredResources.add(var9);
         }

         this.logged = true;
      }
   }

   public String toString() {
      return "BranchRecord:globalXid=" + this.globalXid;
   }

   class RegisteredResource implements Externalizable {
      private static final long serialVersionUID = 601688150453719976L;
      private static final int STATE_ACTIVE = 1;
      private static final int STATE_PREPARED = 2;
      private static final int STATE_READONLY = 3;
      private static final int STATE_COMPLETED = 4;
      private XAResource resource;
      private int vote = -1;
      private int state;
      private BranchXidImpl branchXid;

      RegisteredResource() {
      }

      RegisteredResource(XAResource var2) {
         this.resource = var2;
         this.state = 1;
      }

      private Xid getBranchXid() {
         return this.branchXid == null ? null : this.branchXid.getDelegate();
      }

      private void setBranchXid(Xid var1) {
         this.branchXid = new BranchXidImpl(var1);
      }

      private void setPrepared() {
         this.state = 2;
      }

      XAResource getResource() {
         return this.resource;
      }

      boolean isCompleted() {
         return this.state == 4 || this.state == 3;
      }

      int prepare(Xid var1) throws XAException {
         switch (this.state) {
            case 2:
            case 3:
               return this.vote;
            case 4:
               JTAHelper.throwXAException(-5, "Resource completed.");
            default:
               try {
                  this.vote = this.resource.prepare(var1);
                  switch (this.vote) {
                     case 0:
                        this.state = 2;
                        break;
                     case 3:
                        this.state = 3;
                  }
               } catch (XAException var3) {
                  switch (var3.errorCode) {
                     case -7:
                     case -6:
                     case -5:
                     case -4:
                     case -3:
                        throw var3;
                     case 100:
                     case 101:
                     case 102:
                     case 103:
                     case 104:
                     case 105:
                     case 106:
                     case 107:
                        this.state = 4;
                        throw var3;
                     default:
                        throw var3;
                  }
               }

               return this.vote;
         }
      }

      void commit(Xid var1, boolean var2) throws XAException {
         switch (this.state) {
            case 3:
            case 4:
               return;
            default:
               try {
                  this.resource.commit(var1, var2);
                  this.state = 4;
               } catch (XAException var4) {
                  switch (var4.errorCode) {
                     case -7:
                     case -6:
                     case -5:
                     case -3:
                        throw var4;
                     case -4:
                        this.state = 4;
                        break;
                     case 5:
                     case 6:
                     case 8:
                        this.state = 4;
                        throw var4;
                     case 7:
                        this.state = 4;
                        break;
                     case 100:
                     case 101:
                     case 102:
                     case 103:
                     case 104:
                     case 105:
                     case 106:
                     case 107:
                        if (var2) {
                           this.state = 4;
                           throw var4;
                        }

                        JTAHelper.throwXAException(8, "Invalid rollback error code thrown for 2PC commit. ", var4);
                        break;
                     default:
                        throw var4;
                  }
               }

         }
      }

      void rollback(Xid var1) throws XAException {
         switch (this.state) {
            case 3:
            case 4:
               return;
            default:
               try {
                  this.resource.rollback(var1);
                  this.state = 4;
               } catch (XAException var3) {
                  switch (var3.errorCode) {
                     case -7:
                     case -6:
                     case -5:
                     case -3:
                        throw var3;
                     case -4:
                        this.state = 4;
                        break;
                     case -2:
                     case -1:
                     case 0:
                     case 1:
                     case 2:
                     case 3:
                     case 4:
                     default:
                        throw var3;
                     case 5:
                     case 7:
                     case 8:
                        this.state = 4;
                        throw var3;
                     case 6:
                        this.state = 4;
                  }
               }

         }
      }

      public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
         this.branchXid = new BranchXidImpl();
         this.branchXid.readExternal(var1);
         this.resource = (XAResource)var1.readObject();
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         this.branchXid.writeExternal(var1);
         var1.writeObject(this.resource);
      }
   }
}
