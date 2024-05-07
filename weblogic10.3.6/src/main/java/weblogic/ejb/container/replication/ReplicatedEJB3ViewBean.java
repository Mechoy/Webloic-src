package weblogic.ejb.container.replication;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.rmi.RemoteException;
import weblogic.cluster.replication.ApplicationUnavailableException;
import weblogic.cluster.replication.ROID;
import weblogic.ejb.container.EJBLogger;

public class ReplicatedEJB3ViewBean extends ReplicatedBean {
   private String ifaceClassName;

   public ReplicatedEJB3ViewBean() {
   }

   public ReplicatedEJB3ViewBean(String var1, Class var2) {
      super(var1);
      this.ifaceClassName = var2.getName();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.jndiName);
      var1.writeObject(this.ifaceClassName);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.jndiName = (String)var1.readObject();
      this.ifaceClassName = (String)var1.readObject();
   }

   public Object becomeSecondary(ROID var1) {
      try {
         return this.getHome().createSecondaryForBI(var1, this.ifaceClassName);
      } catch (RemoteException var3) {
         EJBLogger.logClustersNotHomogeneous(this.jndiName.toString());
         throw new ApplicationUnavailableException(var3.toString());
      }
   }
}
