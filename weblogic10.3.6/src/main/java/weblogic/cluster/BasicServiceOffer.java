package weblogic.cluster;

import java.io.EOFException;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.naming.Context;
import javax.naming.ContextNotEmptyException;
import javax.naming.NamingException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.jndi.WLContext;
import weblogic.rmi.cluster.ClusterableRemoteObject;
import weblogic.rmi.spi.HostID;

public final class BasicServiceOffer implements ServiceOffer, Externalizable {
   private static final long serialVersionUID = 2651365883729651106L;
   private int id;
   private String name;
   private Object service;
   private HostID memID;
   private long creationTime;
   private int oldID;
   private String appId;

   public BasicServiceOffer(int var1, String var2, String var3, Object var4) {
      this.oldID = -1;
      this.id = var1;
      this.name = var2;
      this.appId = var3;
      this.service = var4;
      this.creationTime = System.currentTimeMillis();
   }

   public BasicServiceOffer(int var1, String var2, String var3, Object var4, int var5) {
      this(var1, var2, var3, var4);
      this.oldID = var5;
   }

   public int id() {
      return this.id;
   }

   public String name() {
      return this.name;
   }

   public String appID() {
      return this.appId;
   }

   public String serviceName() {
      return " from " + this.memID.toString();
   }

   public boolean isClusterable() {
      return this.service instanceof ClusterableRemoteObject;
   }

   public long approximateAge() {
      return System.currentTimeMillis() - this.creationTime;
   }

   public int getOldID() {
      return this.oldID;
   }

   public void setServer(HostID var1) {
      this.memID = var1;
   }

   public HostID getServerID() {
      return this.memID;
   }

   public String toString() {
      return this.name + "@" + this.appId + ":" + (this.service != null ? this.service.getClass().getName() : "Subcontext") + " (from " + this.memID + ")";
   }

   public void install(Context var1) throws NamingException {
      if (this.service != null) {
         try {
            if (this.appId != null) {
               ApplicationVersionUtils.setBindApplicationId(this.appId);
            }

            var1.bind(this.name, this.service);
         } finally {
            if (this.appId != null) {
               ApplicationVersionUtils.unsetBindApplicationId();
            }

         }
      } else {
         var1.createSubcontext(this.name);
      }

   }

   public void retract(Context var1) throws NamingException {
      if (this.service != null) {
         try {
            if (this.appId != null) {
               ApplicationVersionUtils.setBindApplicationId(this.appId);
            }

            ((WLContext)var1).unbind(this.name, this.service);
         } finally {
            if (this.appId != null) {
               ApplicationVersionUtils.unsetBindApplicationId();
            }

         }
      } else {
         try {
            var1.destroySubcontext(this.name);
         } catch (ContextNotEmptyException var6) {
         }
      }

   }

   public void update(Context var1) throws NamingException {
      if (this.service != null) {
         try {
            if (this.appId != null) {
               ApplicationVersionUtils.setBindApplicationId(this.appId);
            }

            ((WLContext)var1).rebind(this.name, this.service);
         } finally {
            if (this.appId != null) {
               ApplicationVersionUtils.unsetBindApplicationId();
            }

         }
      } else {
         try {
            var1.destroySubcontext(this.name);
         } catch (ContextNotEmptyException var6) {
         }
      }

   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      WLObjectOutput var2 = (WLObjectOutput)var1;
      var2.writeInt(this.id);
      var2.writeString(this.name);
      var2.writeObjectWL(this.service);
      var2.writeLong(System.currentTimeMillis() - this.creationTime);
      var2.writeInt(this.oldID);
      var2.writeString(this.appId);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      WLObjectInput var2 = (WLObjectInput)var1;
      this.id = var2.readInt();
      this.name = var2.readString();
      this.service = var2.readObjectWL();
      this.creationTime = System.currentTimeMillis() - var2.readLong();
      this.oldID = var2.readInt();

      try {
         this.appId = var2.readString();
      } catch (EOFException var4) {
      }

   }

   public BasicServiceOffer() {
      this.oldID = -1;
   }
}
