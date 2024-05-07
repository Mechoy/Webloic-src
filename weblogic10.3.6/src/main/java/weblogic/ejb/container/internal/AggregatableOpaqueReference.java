package weblogic.ejb.container.internal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import weblogic.ejb.container.EJBLogger;
import weblogic.jndi.Aggregatable;
import weblogic.jndi.OpaqueReference;
import weblogic.jndi.internal.NamingNode;
import weblogic.logging.Loggable;
import weblogic.rjvm.JVMID;

public class AggregatableOpaqueReference implements OpaqueReference, Aggregatable {
   private static final long serialVersionUID = 464050940789719368L;
   private final String uniqueEJBIdentifier;
   private final String interfaceName;
   private final Object referent;
   private final Set serverIDs = new HashSet();
   private Object serverID;
   private transient Map serverIdToReferent;
   private static final Object localServerID = JVMID.localID();

   public AggregatableOpaqueReference(String var1, String var2, Object var3) {
      this.uniqueEJBIdentifier = var1;
      this.interfaceName = var2;
      this.referent = var3;
      this.serverID = localServerID;
   }

   public String getInterfaceName() {
      return this.interfaceName;
   }

   public String getUniqueEJBIdentifier() {
      return this.uniqueEJBIdentifier;
   }

   public Object getServerID() {
      return this.serverID;
   }

   public Object getReferentInstance() {
      return this.referent;
   }

   public void onBind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      if (var3 != null) {
         this.validateAdditionalBinding(var2, var3);
      }

      AggregatableOpaqueReference var4 = (AggregatableOpaqueReference)var3;
      if (var4 == null) {
         var4 = this;
      }

      synchronized(this) {
         if (this.serverIdToReferent == null) {
            this.serverIdToReferent = new HashMap();
         }

         this.serverIdToReferent.put(var4.getServerID(), var4.getReferentInstance());
         this.serverIDs.add(var4.getServerID());
      }
   }

   public void onRebind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      this.onBind(var1, var2, var3);
   }

   public boolean onUnbind(NamingNode var1, String var2, Aggregatable var3) throws NamingException {
      Object var4 = null;
      if (var3 == null) {
         var4 = localServerID;
      } else {
         var4 = ((AggregatableOpaqueReference)var3).getServerID();
      }

      synchronized(this) {
         this.serverIdToReferent.remove(var4);
         this.serverIDs.remove(var4);
      }

      return this.serverIDs.isEmpty();
   }

   public Object getReferent(Name var1, Context var2) throws NamingException {
      if (this.serverIdToReferent == null) {
         this.serverIdToReferent = new HashMap();
      }

      Object var3 = this.serverIdToReferent.get(localServerID);
      if (var3 == null) {
         Iterator var4 = this.serverIdToReferent.values().iterator();
         if (var4.hasNext()) {
            Object var5 = var4.next();
            var3 = var5;
         }

         if (var3 == null) {
            var3 = this.referent;
         }
      }

      return var3 instanceof OpaqueReference ? ((OpaqueReference)var3).getReferent(var1, var2) : var3;
   }

   public String toString() {
      return "AggregatableOpaqueReference for interface " + this.interfaceName + " of EJB " + this.uniqueEJBIdentifier;
   }

   private void validateAdditionalBinding(String var1, Object var2) throws NameAlreadyBoundException {
      if (!(var2 instanceof AggregatableOpaqueReference)) {
         throw new NameAlreadyBoundException();
      } else {
         AggregatableOpaqueReference var3 = (AggregatableOpaqueReference)var2;
         Loggable var4;
         if (!this.uniqueEJBIdentifier.equals(var3.getUniqueEJBIdentifier())) {
            var4 = EJBLogger.logAlreadyBindInterfaceWithNameLoggable(var3.getInterfaceName(), var1);
            throw new NameAlreadyBoundException(var4.getMessage());
         } else if (!this.interfaceName.equals(var3.getInterfaceName())) {
            var4 = EJBLogger.logAnotherInterfaceBindWithNameLoggable(var3.getInterfaceName(), var1);
            throw new NameAlreadyBoundException(var4.getMessage());
         }
      }
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      synchronized(this) {
         var1.defaultWriteObject();
      }
   }
}
