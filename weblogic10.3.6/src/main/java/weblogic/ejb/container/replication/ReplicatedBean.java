package weblogic.ejb.container.replication;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.cluster.replication.ApplicationUnavailableException;
import weblogic.cluster.replication.ROID;
import weblogic.cluster.replication.Replicatable;
import weblogic.ejb.container.EJBLogger;
import weblogic.rmi.extensions.PortableRemoteObject;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;
import weblogic.utils.StackTraceUtils;

public class ReplicatedBean implements Replicatable, Externalizable {
   private static final long serialVersionUID = 3890747734666769086L;
   private static final DebugCategory DEBUG_APP_VERSION = Debug.getCategory("weblogic.AppVersion");
   private static final boolean debug;
   protected String jndiName;
   private transient ReplicatedHome replicatedHome;

   public ReplicatedBean() {
   }

   public ReplicatedBean(String var1) {
      this.jndiName = var1;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.jndiName);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.jndiName = (String)var1.readObject();
   }

   protected ReplicatedHome getHome() {
      if (this.replicatedHome == null) {
         try {
            InitialContext var1 = new InitialContext();
            Object var2 = PortableRemoteObject.narrow(var1.lookup(this.jndiName), ReplicatedHome.class);
            this.replicatedHome = (ReplicatedHome)var2;
         } catch (NamingException var3) {
            EJBLogger.logClustersNotHomogeneous(this.jndiName);
            throw new EJBException(var3);
         }
      }

      return this.replicatedHome;
   }

   public void becomePrimary(ROID var1) {
      if (debug) {
         this.debug("** becomePrimary on :" + var1);
      }

      try {
         this.getHome().becomePrimary(var1);
      } catch (RemoteException var3) {
         EJBLogger.logFailureInReplication(StackTraceUtils.throwable2StackTrace(var3));
      }

   }

   public Object becomeSecondary(ROID var1) {
      if (debug) {
         this.debug("** becomeSecondary " + var1);
      }

      try {
         return this.getHome().createSecondary(var1);
      } catch (RemoteException var3) {
         EJBLogger.logClustersNotHomogeneous(this.jndiName.toString());
         throw new ApplicationUnavailableException(var3.toString());
      }
   }

   public void becomeUnregistered(ROID var1) {
      try {
         this.getHome().removeSecondary(var1);
      } catch (RemoteException var3) {
      }

   }

   public void update(ROID var1, Serializable var2) {
      if (debug) {
         this.debug("*** Received update for key: " + var1);
      }

      try {
         this.getHome().updateSecondary(var1, var2);
      } catch (RemoteException var4) {
         EJBLogger.logFailedToUpdateSecondaryDuringReplication(this.jndiName, StackTraceUtils.throwable2StackTrace(var4));
      }

   }

   public Object getKey() {
      return DEFAULT_KEY;
   }

   private void debug(String var1) {
      HashMap var2 = ApplicationVersionUtils.getDebugWorkContexts();
      Debug.say(var1 + (var2 == null ? "" : ", workCtxs=" + var2));
   }

   static {
      debug = DEBUG_APP_VERSION.isEnabled();
   }
}
