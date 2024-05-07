package weblogic.common.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.RemoteException;
import javax.naming.NamingException;
import weblogic.common.AdminServicesDef;
import weblogic.common.LogServicesDef;
import weblogic.common.NameServicesDef;
import weblogic.common.T3ServicesDef;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.io.common.IOServicesDef;
import weblogic.jdbc.common.JdbcServicesDef;
import weblogic.jndi.Environment;
import weblogic.rjvm.JVMID;
import weblogic.rjvm.RJVM;
import weblogic.rjvm.RJVMManager;
import weblogic.time.common.TimeServicesDef;
import weblogic.utils.AssertionError;

public final class T3BindableServices implements T3ServicesDef, Externalizable {
   private static final long serialVersionUID = -9045561245277800956L;
   private JVMID hostID;
   private T3ServicesDef services;

   public T3BindableServices(JVMID var1) {
      try {
         this.setHostID(var1);
      } catch (RemoteException var3) {
         throw new AssertionError("Unexpected exception: " + var3);
      }
   }

   public T3BindableServices() {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      if (var1 instanceof WLObjectOutput) {
         ((WLObjectOutput)var1).writeObjectWL(this.hostID);
      } else {
         var1.writeObject(this.hostID);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      JVMID var2;
      if (var1 instanceof WLObjectInput) {
         var2 = (JVMID)((WLObjectInput)var1).readObjectWL();
      } else {
         var2 = (JVMID)var1.readObject();
      }

      this.setHostID(var2);
   }

   private void setHostID(JVMID var1) throws RemoteException {
      if (var1.equals(JVMID.localID())) {
         try {
            this.services = (T3ServicesDef)Class.forName("weblogic.t3.srvr.T3ServerServices").newInstance();
         } catch (ClassNotFoundException var3) {
            throw new AssertionError(var3);
         } catch (InstantiationException var4) {
            throw new AssertionError(var4);
         } catch (IllegalAccessException var5) {
            throw new AssertionError(var5);
         }
      } else {
         RJVM var2 = RJVMManager.getRJVMManager().findOrCreate(var1);
         this.services = (T3ServicesDef)var2.getColocatedServices();
      }

      this.hostID = var1;
   }

   public AdminServicesDef admin() {
      return this.services.admin();
   }

   /** @deprecated */
   public JdbcServicesDef jdbc() {
      return null;
   }

   public LogServicesDef log() {
      return this.services.log();
   }

   public NameServicesDef name() {
      return this.services.name();
   }

   public IOServicesDef io() {
      return this.services.io();
   }

   public TimeServicesDef time() {
      return this.services.time();
   }

   public static void initialize() {
      try {
         Environment var0 = new Environment();
         var0.setCreateIntermediateContexts(true);
         var0.setReplicateBindings(false);
         var0.getInitialContext().bind("weblogic.common.T3Services", new T3BindableServices(JVMID.localID()));
      } catch (NamingException var1) {
         throw new AssertionError(var1);
      }
   }
}
