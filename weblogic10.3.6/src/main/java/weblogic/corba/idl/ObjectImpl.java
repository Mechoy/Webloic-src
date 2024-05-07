package weblogic.corba.idl;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.Remote;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DomainManager;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.Request;
import org.omg.CORBA.SetOverrideType;
import weblogic.corba.utils.RemoteInfo;
import weblogic.corba.utils.RepositoryId;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;

public class ObjectImpl implements Object, Remote, InvocationHandler {
   private static final boolean DEBUG = false;
   private RepositoryId typeid;
   private Remote delegate;
   private IOR ior;

   public ObjectImpl(IOR var1) {
      this.ior = var1;
      this.delegate = null;
      this.typeid = var1.getTypeId();
   }

   protected ObjectImpl() {
      this.delegate = this;
      this.typeid = RepositoryId.createFromRemote(this.getClass());
   }

   protected ObjectImpl(String var1) {
      this.delegate = this;
      this.typeid = new RepositoryId(var1);
   }

   public ObjectImpl(Remote var1) {
      this.delegate = var1;
      if (var1 != null) {
         this.typeid = RepositoryId.createFromRemote(var1.getClass());
      } else {
         this.typeid = RepositoryId.OBJECT;
      }

   }

   public RepositoryId getTypeId() {
      return this.typeid;
   }

   public IOR getIOR() throws IOException {
      if (this.ior != null) {
         return this.ior;
      } else {
         return this.delegate != null && !(this.delegate instanceof ObjectImpl) ? IIOPReplacer.getIIOPReplacer().replaceRemote(this.delegate) : null;
      }
   }

   public java.lang.Object getRemote() throws IOException {
      if (this.delegate != null) {
         return this.delegate;
      } else if (this.ior != null) {
         IIOPReplacer.getIIOPReplacer();
         return IIOPReplacer.resolveObject(this.ior);
      } else {
         return null;
      }
   }

   public int hashCode() {
      if (this.ior != null) {
         return this.ior.hashCode();
      } else {
         return this.delegate != this ? this.delegate.hashCode() : this.typeid.hashCode();
      }
   }

   public boolean equals(java.lang.Object var1) {
      if (!(var1 instanceof ObjectImpl)) {
         return false;
      } else {
         ObjectImpl var2 = (ObjectImpl)var1;
         return this.typeid.equals(var2.typeid) && (this.ior == var2.ior || (this.ior == null || this.ior.equals(var2.ior)) && this.ior != null) && (this.delegate == this || this.delegate == var2.delegate || this.delegate.equals(var2.delegate));
      }
   }

   public boolean _is_a(String var1) {
      boolean var2 = false;
      if (this.typeid.toString().equals(var1)) {
         var2 = true;
      } else if (var1 != null && var1.length() != 0) {
         RemoteInfo var3 = RemoteInfo.findRemoteInfo(new RepositoryId(var1));
         var2 = var3 != null && (var3.getTheClass().isAssignableFrom(this.getClass()) || this.delegate != null && var3.getTheClass().isAssignableFrom(this.delegate.getClass()));
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean _is_equivalent(Object var1) {
      return var1 instanceof ObjectImpl ? ((ObjectImpl)var1).equals(this) : false;
   }

   public boolean _non_existent() {
      return this.delegate == null;
   }

   public int _hash(int var1) {
      return this.delegate != null ? this.delegate.hashCode() : this.hashCode();
   }

   public void _release() {
   }

   public Object _duplicate() {
      throw new NO_IMPLEMENT();
   }

   public Object _get_interface_def() {
      throw new NO_IMPLEMENT();
   }

   public Request _request(String var1) {
      throw new NO_IMPLEMENT();
   }

   public Request _create_request(Context var1, String var2, NVList var3, NamedValue var4) {
      throw new NO_IMPLEMENT();
   }

   public Request _create_request(Context var1, String var2, NVList var3, NamedValue var4, ExceptionList var5, ContextList var6) {
      throw new NO_IMPLEMENT();
   }

   public Policy _get_policy(int var1) {
      throw new NO_IMPLEMENT();
   }

   public DomainManager[] _get_domain_managers() {
      throw new NO_IMPLEMENT();
   }

   public Object _set_policy_override(Policy[] var1, SetOverrideType var2) {
      throw new NO_IMPLEMENT();
   }

   public String toString() {
      return super.toString() + " delegate: " + (this.delegate == null ? "<null>" : this.delegate.getClass().getName()) + ", typeid: " + this.typeid;
   }

   public java.lang.Object invoke(java.lang.Object var1, Method var2, java.lang.Object[] var3) throws Throwable {
      Class var4 = var2.getDeclaringClass();
      if (var4 != Object.class && var4 != java.lang.Object.class) {
         if (var4.isAssignableFrom(this.delegate.getClass())) {
            try {
               return var2.invoke(this.delegate, var3);
            } catch (InvocationTargetException var6) {
               throw var6.getTargetException();
            }
         }
      } else {
         var2.invoke(this, var3);
      }

      throw new InternalError("unexpected method: " + var2);
   }

   protected static void p(String var0) {
      System.err.println("<ObjectImpl> " + var0);
   }
}
