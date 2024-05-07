package weblogic.corba.idl;

import java.io.IOException;
import java.util.Iterator;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.OBJ_ADAPTER;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;
import weblogic.iiop.Utils;
import weblogic.iiop.spi.IORDelegate;
import weblogic.iiop.spi.MessageStream;
import weblogic.rmi.extensions.server.ActivatableServerReference;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.ServerReference;
import weblogic.utils.collections.NumericKeyHashMap;

class DelegateImpl extends Delegate implements IORDelegate {
   private static final boolean ENABLE_SERVANT_PREINVOKE = Boolean.getBoolean("weblogic.iiop.enableServantPreInvoke");
   private IOR ior;
   private NumericKeyHashMap policies;

   public DelegateImpl(IOR var1) {
      this.ior = var1;
   }

   public final IOR getIOR() {
      return this.ior;
   }

   public ORB orb(Object var1) {
      return weblogic.corba.orb.ORB.getInstance();
   }

   protected DelegateImpl(DelegateImpl var1) {
      this.ior = var1.ior;
      this.policies = var1.policies;
   }

   protected Delegate copy() {
      return new DelegateImpl(this);
   }

   public int hash(Object var1, int var2) {
      return this.ior.hashCode();
   }

   public Object duplicate(Object var1) {
      return new IIOPReplacer.AnonymousCORBAStub(this.ior.getTypeId().toString(), this.copy());
   }

   public void release(Object var1) {
   }

   public boolean is_a(Object var1, String var2) {
      if (this.ior.getTypeId().toString().equals(var2)) {
         return true;
      } else {
         String[] var3 = ((org.omg.CORBA.portable.ObjectImpl)var1)._ids();

         for(int var4 = 0; var4 < var3.length; ++var4) {
            if (var3[var4].equals(var2)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean is_local(Object var1) {
      return true;
   }

   public boolean non_existent(Object var1) {
      return false;
   }

   public boolean is_equivalent(Object var1, Object var2) {
      if (var2 == var1) {
         return true;
      } else {
         org.omg.CORBA.portable.ObjectImpl var3 = (org.omg.CORBA.portable.ObjectImpl)var2;
         return var3._get_delegate() instanceof DelegateImpl ? ((DelegateImpl)var3._get_delegate()).getIOR().equals(this.ior) : false;
      }
   }

   public Policy get_policy(Object var1, int var2) {
      Policy var3 = this.getPolicy(var2);
      if (var3 == null) {
         throw new BAD_PARAM("No associated policy");
      } else {
         return var3;
      }
   }

   protected Policy getPolicy(int var1) {
      return this.policies == null ? null : (Policy)this.policies.get((long)var1);
   }

   public Object set_policy_override(Object var1, Policy[] var2, SetOverrideType var3) {
      org.omg.CORBA.portable.ObjectImpl var4 = (org.omg.CORBA.portable.ObjectImpl)var1._duplicate();
      DelegateImpl var5 = (DelegateImpl)var4._get_delegate();
      var5.policies = new NumericKeyHashMap();
      if (var3.value() == 1 && this.policies != null) {
         Iterator var6 = this.policies.values().iterator();

         while(var6.hasNext()) {
            Policy var7 = (Policy)var6.next();
            var5.policies.put((long)var7.policy_type(), var7);
         }
      }

      for(int var8 = 0; var8 < var2.length; ++var8) {
         var5.policies.put((long)var2[var8].policy_type(), var2[var8]);
      }

      return var4;
   }

   public OutputStream request(Object var1, String var2, boolean var3) {
      try {
         ServerReference var4 = OIDManager.getInstance().getServerReference(this.ior.getProfile().getObjectKey().getObjectID());
         CollocatedRequest var5 = new CollocatedRequest(var4, var2, var3, this.ior.getProfile().getObjectKey().getActivationID());
         return var5.getOutputStream();
      } catch (IOException var6) {
         throw Utils.mapToCORBAException(var6);
      }
   }

   public void releaseReply(Object var1, InputStream var2) {
      if (var2 != null) {
         try {
            var2.close();
         } catch (IOException var4) {
            throw Utils.mapToCORBAException(var4);
         }
      }
   }

   public InputStream invoke(Object var1, OutputStream var2) throws ApplicationException, RemarshalException {
      try {
         CollocatedRequest var3 = (CollocatedRequest)((MessageStream)var2).getMessage();
         var3.invoke();
         return var3.responseExpected() ? var3.getInputStream() : null;
      } catch (IOException var4) {
         throw Utils.mapToCORBAException(var4);
      } catch (ApplicationException var5) {
         throw var5;
      } catch (RemarshalException var6) {
         throw var6;
      } catch (Exception var7) {
         throw (SystemException)(new OBJ_ADAPTER()).initCause(var7);
      }
   }

   public Request request(Object var1, String var2) {
      throw new NO_IMPLEMENT();
   }

   public Request create_request(Object var1, Context var2, String var3, NVList var4, NamedValue var5, ExceptionList var6, ContextList var7) {
      throw new NO_IMPLEMENT();
   }

   public Request create_request(Object var1, Context var2, String var3, NVList var4, NamedValue var5) {
      throw new NO_IMPLEMENT();
   }

   public Object get_interface_def(Object var1) {
      throw new NO_IMPLEMENT();
   }

   public String toString() {
      return "Delegate(" + System.identityHashCode(this) + ") [" + this.ior.toString() + "]";
   }

   public ServantObject servant_preinvoke(Object var1, String var2, Class var3) {
      if (!ENABLE_SERVANT_PREINVOKE) {
         return null;
      } else {
         try {
            ServerReference var4 = OIDManager.getInstance().getServerReference(this.ior.getProfile().getObjectKey().getObjectID());
            ServantObject var5 = new ServantObject();
            if (var4 instanceof ActivatableServerReference) {
               var5.servant = ((ActivatableServerReference)var4).getImplementation(this.ior.getProfile().getObjectKey().getActivationID());
            } else {
               var5.servant = var4.getImplementation();
            }

            return var5;
         } catch (IOException var6) {
            throw Utils.mapToCORBAException(var6);
         }
      }
   }
}
