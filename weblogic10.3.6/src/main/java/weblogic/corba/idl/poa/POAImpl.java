package weblogic.corba.idl.poa;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.naming.NamingException;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.OBJ_ADAPTER;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA_2_3.portable.ObjectImpl;
import org.omg.PortableInterceptor.PolicyFactory;
import org.omg.PortableServer.AdapterActivator;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.IdAssignmentPolicy;
import org.omg.PortableServer.IdAssignmentPolicyValue;
import org.omg.PortableServer.IdUniquenessPolicy;
import org.omg.PortableServer.IdUniquenessPolicyValue;
import org.omg.PortableServer.ImplicitActivationPolicy;
import org.omg.PortableServer.ImplicitActivationPolicyValue;
import org.omg.PortableServer.LifespanPolicy;
import org.omg.PortableServer.LifespanPolicyValue;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManager;
import org.omg.PortableServer.RequestProcessingPolicy;
import org.omg.PortableServer.RequestProcessingPolicyValue;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantActivator;
import org.omg.PortableServer.ServantLocator;
import org.omg.PortableServer.ServantManager;
import org.omg.PortableServer.ServantRetentionPolicy;
import org.omg.PortableServer.ServantRetentionPolicyValue;
import org.omg.PortableServer.ThreadPolicy;
import org.omg.PortableServer.ThreadPolicyValue;
import org.omg.PortableServer.POAPackage.AdapterAlreadyExists;
import org.omg.PortableServer.POAPackage.AdapterNonExistent;
import org.omg.PortableServer.POAPackage.InvalidPolicy;
import org.omg.PortableServer.POAPackage.NoServant;
import org.omg.PortableServer.POAPackage.ObjectAlreadyActive;
import org.omg.PortableServer.POAPackage.ObjectNotActive;
import org.omg.PortableServer.POAPackage.ServantAlreadyActive;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongAdapter;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import weblogic.corba.idl.DelegateFactory;
import weblogic.corba.utils.RemoteInfo;
import weblogic.corba.utils.RepositoryId;
import weblogic.iiop.ClusterComponent;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;
import weblogic.iiop.ObjectKey;
import weblogic.iiop.ProtocolHandlerIIOP;
import weblogic.iiop.spi.IORDelegate;
import weblogic.jndi.Environment;
import weblogic.kernel.KernelStatus;
import weblogic.protocol.ServerChannel;
import weblogic.protocol.ServerChannelManager;
import weblogic.rmi.cluster.ClusterableActivatableServerRef;
import weblogic.rmi.extensions.server.ActivatableServerReference;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.ServerReference;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.security.service.SecurityManager;
import weblogic.utils.Debug;
import weblogic.utils.collections.NumericKeyHashMap;

public class POAImpl extends ObjectImpl implements POA, PolicyFactory {
   private static final boolean DEBUG = false;
   private static int nextOID = 0;
   private static final int POFF = 16;
   private final String name;
   private final int[] policyValues = new int[8];
   private final HashMap children = new HashMap();
   private final HashMap aom = new HashMap();
   private final HashMap asm = new HashMap();
   private final NumericKeyHashMap policies = new NumericKeyHashMap();
   private final POA parent;
   private final POAManager manager;
   private Servant defaultServant;
   private ServantManager servantManager;
   private int objectId = -1;
   private String applicationName;
   private static AuthenticatedSubject kernelId = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());

   public POAImpl(String var1) {
      this.name = var1;
      this.parent = null;
      this.manager = new POAManagerImpl(this.composeName());
      this.setDefaultPolicies();
   }

   public POAImpl(String var1, Policy[] var2, POA var3, POAManager var4) throws InvalidPolicy {
      this.name = var1;
      this.parent = var3;
      if (var4 == null) {
         this.manager = new POAManagerImpl(this.composeName());
      } else {
         this.manager = var4;
      }

      this.setDefaultPolicies();

      for(int var5 = 0; var5 < var2.length; ++var5) {
         PolicyImpl var6 = (PolicyImpl)var2[var5];
         if (var6.policy_type() >= 16 && var6.policy_type() <= 24) {
            this.setPolicy(var6.policy_type(), var6.policy_value());
         } else {
            this.policies.put((long)var6.policy_type(), var6);
         }
      }

   }

   private void setPolicy(int var1, int var2) throws InvalidPolicy {
      if (var1 >= 16 && var1 <= 24) {
         this.policyValues[var1 - 16] = var2;
      } else {
         throw new InvalidPolicy();
      }
   }

   private int getPolicy(int var1) {
      Debug.assertion(var1 >= 16 && var1 < 24);
      return this.policyValues[var1 - 16];
   }

   private void setDefaultPolicies() {
      try {
         this.setPolicy(16, 0);
         this.setPolicy(17, 0);
         this.setPolicy(18, 0);
         this.setPolicy(19, 1);
         this.setPolicy(21, 0);
         this.setPolicy(22, 0);
         this.setPolicy(20, 0);
      } catch (InvalidPolicy var2) {
      }

   }

   public byte[] id() {
      return POAHelper.id().getBytes();
   }

   public synchronized void destroy(boolean var1, boolean var2) {
      synchronized(this.children) {
         HashMap var4 = (HashMap)this.children.clone();
         Iterator var5 = var4.values().iterator();

         while(true) {
            if (!var5.hasNext()) {
               break;
            }

            ((POA)var5.next()).destroy(var1, var2);
         }
      }

      ((POAImpl)this.parent).destroyChild(this.name);
      ServerReference var3 = OIDManager.getInstance().getServerReference(this);
      OIDManager.getInstance().removeServerReference(var3);
      if (KernelStatus.isServer() && var3.getDescriptor().isClusterable()) {
         try {
            SecurityManager.runAs(kernelId, kernelId, new PrivilegedExceptionAction() {
               public Object run() throws NamingException {
                  Environment var1 = new Environment();
                  var1.setCreateIntermediateContexts(true);
                  var1.getInitialContext().unbind(POAImpl.this.composeName() + "/thePOA");
                  return null;
               }
            });
         } catch (PrivilegedActionException var7) {
            throw (BAD_PARAM)(new BAD_PARAM(var7.getException().getMessage(), 1330446346, CompletionStatus.COMPLETED_NO)).initCause(var7.getException());
         }
      }

      if (this.servantManager instanceof ServantActivator) {
         Iterator var9 = this.aom.entrySet().iterator();

         while(var9.hasNext()) {
            Map.Entry var10 = (Map.Entry)var9.next();
            ((ServantActivator)this.servantManager).etherealize(((String)var10.getKey()).getBytes(), this, (Servant)var10.getValue(), var1, false);
         }
      }

   }

   private void destroyChild(String var1) {
      synchronized(this.children) {
         if (this.children.remove(var1) == null) {
            throw new BAD_PARAM(var1);
         }
      }
   }

   public void deactivate_object(byte[] var1) throws ObjectNotActive, WrongPolicy {
      synchronized(this.aom) {
         Servant var3 = (Servant)this.aom.remove(new String(var1, 0));
         if (var3 == null) {
            throw new ObjectNotActive();
         } else {
            this.asm.remove(var3);
            if (this.servantManager instanceof ServantActivator) {
               ((ServantActivator)this.servantManager).etherealize(var1, this, var3, false, false);
            }

         }
      }
   }

   public String the_name() {
      return this.name;
   }

   public byte[] reference_to_id(org.omg.CORBA.Object var1) throws WrongAdapter, WrongPolicy {
      if (!(var1 instanceof ObjectImpl)) {
         throw new WrongAdapter("Not an ObjectImpl");
      } else {
         ObjectImpl var2 = (ObjectImpl)var1;
         Delegate var3 = var2._get_delegate();
         if (!(var3 instanceof IORDelegate)) {
            throw new WrongAdapter("Not an IORDelegate");
         } else {
            ObjectKey var4 = ((IORDelegate)var3).getIOR().getProfile().getObjectKey();
            if (var4.getObjectID() == this.objectId && var4.getActivationID() != null) {
               return (byte[])((byte[])var4.getActivationID());
            } else {
               throw new WrongAdapter("Wrong Object Key");
            }
         }
      }
   }

   public org.omg.CORBA.Object id_to_reference(byte[] var1) throws ObjectNotActive, WrongPolicy {
      synchronized(this.aom) {
         Servant var3 = (Servant)this.aom.get(new String(var1, 0));
         if (var3 == null) {
            throw new ObjectNotActive("No active object: " + new String(var1, 0));
         } else {
            org.omg.CORBA.Object var10000;
            try {
               var10000 = IIOPReplacer.makeInvocationHandler((IOR)IIOPReplacer.getReplacer().replaceObject(var3));
            } catch (IOException var6) {
               throw (INTERNAL)(new INTERNAL("id_to_reference()")).initCause(var6);
            }

            return var10000;
         }
      }
   }

   public AdapterActivator the_activator() {
      throw new NO_IMPLEMENT("the_activator");
   }

   public void the_activator(AdapterActivator var1) {
      throw new NO_IMPLEMENT("the_activator");
   }

   public POAManager the_POAManager() {
      return this.manager;
   }

   public Servant get_servant() throws NoServant, WrongPolicy {
      if (this.defaultServant == null) {
         throw new NoServant();
      } else if (this.getPolicy(22) != 1) {
         throw new WrongPolicy("RequestProcessingPolicyValue: " + this.getPolicy(22));
      } else {
         return this.defaultServant;
      }
   }

   public void set_servant(Servant var1) throws WrongPolicy {
      if (this.getPolicy(22) != 1) {
         throw new WrongPolicy("RequestProcessingPolicyValue: " + this.getPolicy(22));
      } else {
         this.defaultServant = var1;
      }
   }

   public byte[] activate_object(Servant var1) throws ServantAlreadyActive, WrongPolicy {
      synchronized(this.aom) {
         if (this.getPolicy(18) == 0 && this.aom.containsValue(var1)) {
            throw new ServantAlreadyActive();
         } else if (this.getPolicy(19) == 1 && this.getPolicy(21) == 0) {
            String var3 = Integer.toString(this.getNextObjectID());
            var1._set_delegate(this.createDelegate(var1, var3.getBytes()));
            this.aom.put(var3, var1);
            this.asm.put(var1, var3);
            return var3.getBytes();
         } else {
            throw new WrongPolicy();
         }
      }
   }

   public synchronized int getNextObjectID() {
      return ++nextOID;
   }

   public byte[] servant_to_id(Servant var1) throws ServantNotActive, WrongPolicy {
      if (this.getPolicy(22) != 1 && (this.getPolicy(21) != 0 || this.getPolicy(18) != 0 && this.getPolicy(20) != 0)) {
         throw new WrongPolicy("servant_to_id()");
      } else {
         synchronized(this.aom) {
            String var3;
            if (this.getPolicy(18) == 0 && (var3 = (String)this.asm.get(var1)) != null) {
               return var3.getBytes();
            } else if (this.getPolicy(20) != 0 || this.getPolicy(18) != 1 && this.asm.get(var1) != null) {
               if (this.getPolicy(22) == 1 && this.defaultServant != null && var1.equals(this.defaultServant)) {
                  throw new NO_IMPLEMENT("servant_to_id()");
               } else {
                  throw new ServantNotActive("servant_to_id: " + var1.toString());
               }
            } else {
               byte[] var10000;
               try {
                  var10000 = this.activate_object(var1);
               } catch (ServantAlreadyActive var6) {
                  throw (AssertionError)(new AssertionError()).initCause(var6);
               }

               return var10000;
            }
         }
      }
   }

   public Servant id_to_servant(byte[] var1) throws ObjectNotActive, WrongPolicy {
      if (this.getPolicy(21) == 0) {
         synchronized(this.aom) {
            Servant var3 = (Servant)this.aom.get(new String(var1, 0));
            if (var3 == null) {
               if (this.getPolicy(22) != 2) {
                  throw new ObjectNotActive(new String(var1, 0));
               }

               try {
                  var3 = ((ServantActivator)this.servantManager).incarnate(var1, this);
               } catch (ForwardRequest var6) {
                  throw new NO_IMPLEMENT("ForwardRequest");
               }
            }

            return var3;
         }
      } else if (this.getPolicy(22) == 1) {
         if (this.defaultServant == null) {
            throw new ObjectNotActive("No default Servant registered");
         } else {
            return this.defaultServant;
         }
      } else {
         throw new WrongPolicy("Neither RETAIN or USE_DEFAULT_SERVANT were specified");
      }
   }

   public void activate_object_with_id(byte[] var1, Servant var2) throws ServantAlreadyActive, ObjectAlreadyActive, WrongPolicy {
      synchronized(this.aom) {
         String var4 = new String(var1, 0);
         if (this.getPolicy(18) == 0 && this.aom.get(var4) != null) {
            throw new ServantAlreadyActive();
         } else if (this.aom.containsValue(var2)) {
            throw new ObjectAlreadyActive();
         } else if (this.getPolicy(21) != 0) {
            throw new WrongPolicy();
         } else {
            var2._set_delegate(this.createDelegate(var2, var1));
            this.aom.put(var4, var2);
            this.asm.put(var2, var4);
         }
      }
   }

   private org.omg.PortableServer.portable.Delegate createDelegate(Servant var1, byte[] var2) {
      String var3 = var1._all_interfaces(this, var2)[0];
      ServerChannel var4 = ServerChannelManager.findLocalServerChannel(ProtocolHandlerIIOP.PROTOCOL_IIOP);
      String var5 = var4.getPublicAddress();
      int var6 = var4.getPublicPort();
      ObjectKey var7 = new ObjectKey(var3, this.objectId, var2);
      RepositoryId var8 = new RepositoryId(var3);
      RemoteInfo var9 = RemoteInfo.findRemoteInfo(var8, var1.getClass());
      return new POADelegateImpl(this, var9.getDescriptor() != null ? new IOR(var3, var5, var6, var7, (String)null, (ClusterComponent)null, var9.getDescriptor()) : new IOR(var3, var5, var6, var7, (String)null, (ClusterComponent)null, this.policies), var9, var2);
   }

   private IOR createIOR(String var1, byte[] var2) {
      ServerChannel var3 = ServerChannelManager.findLocalServerChannel(ProtocolHandlerIIOP.PROTOCOL_IIOP);
      String var4 = var3.getPublicAddress();
      int var5 = var3.getPublicPort();
      ObjectKey var6 = new ObjectKey(var1, this.objectId, var2);
      return new IOR(var1, var4, var5, var6, (String)null, (ClusterComponent)null, this.policies);
   }

   public ServantManager get_servant_manager() throws WrongPolicy {
      if (this.getPolicy(22) != 2) {
         throw new WrongPolicy("RequestProcessingPolicyValue: " + this.getPolicy(22));
      } else {
         return this.servantManager;
      }
   }

   public void set_servant_manager(ServantManager var1) throws WrongPolicy {
      if (this.servantManager != null) {
         throw new BAD_INV_ORDER("ServantManager already set");
      } else if (var1 == null || this.getPolicy(21) == 0 && !(var1 instanceof ServantActivator) || this.getPolicy(21) != 0 && !(var1 instanceof ServantLocator)) {
         throw new OBJ_ADAPTER("Wrong ServantManager");
      } else if (this.getPolicy(22) != 2) {
         throw new WrongPolicy("RequestProcessingPolicyValue: " + this.getPolicy(22));
      } else {
         this.servantManager = var1;
      }
   }

   public ServantManager getServantManager() {
      return this.servantManager;
   }

   public org.omg.CORBA.Object create_reference(String var1) throws WrongPolicy {
      if (this.getPolicy(19) != 1) {
         throw new WrongPolicy("create_reference() requires SYSTEM_ID");
      } else {
         byte[] var2 = Integer.toString(this.getNextObjectID()).getBytes();
         IOR var3 = this.createIOR(var1, var2);
         return new IIOPReplacer.AnonymousCORBAStub(var1, DelegateFactory.createDelegate(var3));
      }
   }

   public org.omg.CORBA.Object create_reference_with_id(byte[] var1, String var2) {
      if (this.getPolicy(19) == 1) {
         try {
            Integer.parseInt(new String(var1, 0));
         } catch (NumberFormatException var4) {
            throw new BAD_PARAM("create_reference_with_id() requires system id for SYSTEM_ID");
         }
      }

      IOR var3 = this.createIOR(var2, var1);
      return new IIOPReplacer.AnonymousCORBAStub(var2, DelegateFactory.createDelegate(var3));
   }

   public org.omg.CORBA.Object servant_to_reference(Servant var1) throws ServantNotActive, WrongPolicy {
      if (this.getPolicy(21) == 0 && (this.getPolicy(18) == 0 || this.getPolicy(20) == 0)) {
         synchronized(this.aom) {
            if (this.getPolicy(18) == 0 && this.asm.get(var1) != null) {
               return var1._this_object();
            } else if (this.getPolicy(20) == 0 && (this.getPolicy(18) == 1 || this.asm.get(var1) == null)) {
               org.omg.CORBA.Object var10000;
               try {
                  this.activate_object(var1);
                  var10000 = var1._this_object();
               } catch (ServantAlreadyActive var5) {
                  throw (AssertionError)(new AssertionError()).initCause(var5);
               }

               return var10000;
            } else {
               throw new ServantNotActive("servant_to_reference: " + var1.toString());
            }
         }
      } else {
         throw new WrongPolicy("servant_to_reference()");
      }
   }

   public Servant reference_to_servant(org.omg.CORBA.Object var1) throws ObjectNotActive, WrongPolicy, WrongAdapter {
      if (!(var1 instanceof ObjectImpl)) {
         throw new WrongAdapter("Not an ObjectImpl");
      } else {
         ObjectImpl var2 = (ObjectImpl)var1;
         Delegate var3 = var2._get_delegate();
         if (!(var3 instanceof IORDelegate)) {
            throw new WrongAdapter("Not an IORDelegate");
         } else {
            ObjectKey var4 = ((IORDelegate)var3).getIOR().getProfile().getObjectKey();
            if (var4.getObjectID() == this.objectId && var4.getActivationID() != null) {
               String var5 = new String((byte[])((byte[])var4.getActivationID()), 0);
               if (this.getPolicy(21) == 0) {
                  synchronized(this.aom) {
                     Servant var7 = (Servant)this.aom.get(var5);
                     if (var7 == null) {
                        throw new ObjectNotActive(var5);
                     } else {
                        return var7;
                     }
                  }
               } else if (this.getPolicy(22) == 1) {
                  if (this.defaultServant == null) {
                     throw new ObjectNotActive("No default Servant registered");
                  } else {
                     return this.defaultServant;
                  }
               } else {
                  throw new WrongPolicy("Neither RETAIN or USE_DEFAULT_SERVANT were specified");
               }
            } else {
               throw new WrongAdapter("Wrong Object Key");
            }
         }
      }
   }

   public POA the_parent() {
      return this.parent;
   }

   public POA[] the_children() {
      synchronized(this.children) {
         return (POA[])((POA[])this.children.values().toArray(new POA[this.children.size()]));
      }
   }

   public POA create_POA(String var1, POAManager var2, Policy[] var3) throws AdapterAlreadyExists, InvalidPolicy {
      try {
         synchronized(this.children) {
            if (this.children.get(var1) != null) {
               throw new AdapterAlreadyExists(var1);
            } else {
               POAImpl var5 = new POAImpl(var1, var3, this, var2);
               this.children.put(var1, var5);
               return var5.export();
            }
         }
      } catch (IOException var8) {
         throw (BAD_PARAM)(new BAD_PARAM(var8.getMessage(), 1330446346, CompletionStatus.COMPLETED_NO)).initCause(var8);
      }
   }

   public POA export() throws IOException {
      if (this.objectId < 0) {
         synchronized(this) {
            if (this.objectId < 0) {
               Object var2 = new POAServerRef(this);
               if (((ActivatableServerReference)var2).getDescriptor().isClusterable()) {
                  var2 = new ClusterableActivatableServerRef((ActivatableServerReference)var2);
               }

               this.objectId = ((ActivatableServerReference)var2).exportObject().getObjectID();
               if (KernelStatus.isServer() && ((ActivatableServerReference)var2).getDescriptor().isClusterable()) {
                  final String var3 = this.composeName() + "/thePOA";

                  try {
                     SecurityManager.runAs(kernelId, kernelId, new PrivilegedExceptionAction() {
                        public Object run() throws NamingException {
                           Environment var1 = new Environment();
                           var1.setCreateIntermediateContexts(true);
                           var1.getInitialContext().bind(var3, this);
                           return null;
                        }
                     });
                  } catch (PrivilegedActionException var6) {
                     throw (IOException)(new IOException(var6.getException().getMessage())).initCause(var6.getException());
                  }

                  ((ClusterableActivatableServerRef)var2).initialize(var3);
               }
            }
         }
      }

      return this;
   }

   private String composeName() {
      return this.parent == null ? "weblogic/corba/" + this.name : ((POAImpl)this.parent).composeName() + "/" + this.name;
   }

   public POA find_POA(String var1, boolean var2) throws AdapterNonExistent {
      synchronized(this.children) {
         POA var4 = (POA)this.children.get(var1);
         if (var4 == null) {
            throw new AdapterNonExistent();
         } else {
            return var4;
         }
      }
   }

   public IdAssignmentPolicy create_id_assignment_policy(IdAssignmentPolicyValue var1) {
      return new IdAssignmentPolicyImpl(var1.value());
   }

   public IdUniquenessPolicy create_id_uniqueness_policy(IdUniquenessPolicyValue var1) {
      return new IdUniquenessPolicyImpl(var1.value());
   }

   public ImplicitActivationPolicy create_implicit_activation_policy(ImplicitActivationPolicyValue var1) {
      return new ImplicitActivationPolicyImpl(var1.value());
   }

   public LifespanPolicy create_lifespan_policy(LifespanPolicyValue var1) {
      return new LifespanPolicyImpl(var1.value());
   }

   public RequestProcessingPolicy create_request_processing_policy(RequestProcessingPolicyValue var1) {
      return new RequestProcessingPolicyImpl(var1.value());
   }

   public ServantRetentionPolicy create_servant_retention_policy(ServantRetentionPolicyValue var1) {
      return new ServantRetentionPolicyImpl(var1.value());
   }

   public ThreadPolicy create_thread_policy(ThreadPolicyValue var1) {
      return new ThreadPolicyImpl(var1.value());
   }

   public Policy create_policy(int var1, Any var2) throws PolicyError {
      switch (var1) {
         case 16:
         case 17:
         case 18:
         case 19:
         case 20:
         case 21:
         case 22:
            throw new PolicyError("POA policies are only supported via poa functions", (short)1);
         default:
            return null;
      }
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof POAImpl)) {
         return false;
      } else {
         POAImpl var2 = (POAImpl)var1;
         return this.name.equals(var2.name);
      }
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public String toString() {
      return "POA[" + this.name + ", " + this.objectId + "]";
   }

   public String[] _ids() {
      return new String[]{POAHelper.id()};
   }

   protected static void p(String var0) {
      System.out.println("<POAImpl> " + var0);
   }
}
