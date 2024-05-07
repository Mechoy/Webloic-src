package weblogic.corba.orb;

import java.applet.Applet;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import javax.naming.NamingException;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.Current;
import org.omg.CORBA.DynAny;
import org.omg.CORBA.DynArray;
import org.omg.CORBA.DynEnum;
import org.omg.CORBA.DynSequence;
import org.omg.CORBA.DynStruct;
import org.omg.CORBA.DynUnion;
import org.omg.CORBA.Environment;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.CORBA.Request;
import org.omg.CORBA.ServiceInformationHolder;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UnionMember;
import org.omg.CORBA.UserException;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.WrongTransaction;
import org.omg.CORBA.ORBPackage.InconsistentTypeCode;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.portable.OutputStream;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.TimeBase.TimeTHelper;
import org.omg.TimeBase.UtcTHelper;
import weblogic.corba.cos.transactions.InvocationPolicyImpl;
import weblogic.corba.cos.transactions.OTSPolicyImpl;
import weblogic.corba.idl.AnyImpl;
import weblogic.corba.idl.EnvironmentImpl;
import weblogic.corba.idl.ExceptionListImpl;
import weblogic.corba.idl.NVListImpl;
import weblogic.corba.idl.NamedValueImpl;
import weblogic.corba.idl.TypeCodeImpl;
import weblogic.corba.idl.poa.POAImpl;
import weblogic.corba.idl.poa.RelativeRequestTimeoutPolicyImpl;
import weblogic.corba.idl.poa.RelativeRoundtripTimeoutPolicyImpl;
import weblogic.corba.idl.poa.ReplyEndTimePolicyImpl;
import weblogic.corba.idl.poa.RequestEndTimePolicyImpl;
import weblogic.corba.j2ee.naming.NameParser;
import weblogic.corba.j2ee.naming.Utils;
import weblogic.corba.utils.RepositoryId;
import weblogic.iiop.IIOPClient;
import weblogic.iiop.IIOPClientService;
import weblogic.iiop.IIOPOutputStream;
import weblogic.iiop.IIOPReplacer;
import weblogic.iiop.IOR;
import weblogic.iiop.IORManager;
import weblogic.iiop.InitialReferences;
import weblogic.rmi.extensions.server.StubReference;
import weblogic.rmi.internal.OIDManager;
import weblogic.rmi.internal.ServerReference;

public class ORB extends org.omg.CORBA_2_3.ORB {
   private String host;
   private String name;
   private int port;
   private Hashtable initialRefs;
   private ArrayList deferred;
   private String defaultInitialRef;
   private String protocol;
   private String channelName;
   private long timeout;
   private static final boolean DEBUG = false;
   private static final String ROOT_POA = "RootPOA";
   private POA rootPOA;

   public ORB() {
      this.port = -1;
      this.initialRefs = new Hashtable();
      this.deferred = new ArrayList();
      this.timeout = 0L;
      if (!IIOPClient.isEnabled()) {
         IIOPClient.initialize();
      }

   }

   private ORB(boolean var1) {
      this();
      this.set_parameters((String[])(new String[0]), (Properties)null);
   }

   public static final ORB getInstance() {
      return ORB.SingletonMaker.ORB;
   }

   public void set_delegate(Object var1) {
      try {
         ((POAImpl)this.getRootPOA()).activate_object((Servant)var1);
      } catch (Exception var3) {
      }

   }

   protected void set_parameters(String[] var1, Properties var2) {
      this.initialRefs.put("RootPOA", "RootPOA");
      String var6;
      if (var2 != null) {
         try {
            this.host = var2.getProperty("org.omg.CORBA.ORBInitialHost");
            this.protocol = var2.getProperty("weblogic.corba.orb.ORBProtocol", "iiop");
            this.name = var2.getProperty("weblogic.corba.orb.ORBName", Long.toHexString((long)this.hashCode()));
            this.channelName = var2.getProperty("weblogic.jndi.provider.channel");
            this.port = Integer.parseInt(var2.getProperty("org.omg.CORBA.ORBInitialPort", "-1"));
            this.timeout = Long.parseLong(var2.getProperty("weblogic.jndi.requestTimeout", "0"));
            String var3 = var2.getProperty("org.omg.CORBA.ORBInitRef");
            if (var3 != null) {
               int var4 = var3.indexOf(61);
               String var5 = var3.substring(0, var4);
               var6 = var3.substring(var4 + 1);
               this.initialRefs.put(var5, var6);
            }

            this.defaultInitialRef = var2.getProperty("org.omg.CORBA.ORBDefaultInitRef");
         } catch (IndexOutOfBoundsException var8) {
            throw new BAD_PARAM(var8.getMessage(), 1330446343, CompletionStatus.COMPLETED_NO);
         } catch (NumberFormatException var9) {
            throw new BAD_PARAM(var9.getMessage(), 1330446343, CompletionStatus.COMPLETED_NO);
         }
      } else {
         this.name = Long.toHexString((long)this.hashCode());
      }

      if (var1 != null) {
         for(int var10 = 0; var10 < var1.length; ++var10) {
            if (var1[var10].equals("-ORBDefaultInitRef")) {
               ++var10;
               this.defaultInitialRef = var1[var10];
            } else if (var1[var10].equals("-ORBInitRef")) {
               ++var10;
               String var11 = var1[var10];
               int var12 = var11.indexOf(61);
               var6 = var11.substring(0, var12);
               String var7 = var11.substring(var12 + 1);
               this.initialRefs.put(var6, var7);
            }
         }
      }

   }

   protected void set_parameters(Applet var1, Properties var2) {
      this.set_parameters((String[])null, var2);
   }

   public org.omg.CORBA.Object resolve_initial_references(String var1) throws InvalidName {
      Object var2 = this.initialRefs.get(var1);
      if (var2 == "RootPOA") {
         return this.getRootPOA();
      } else {
         org.omg.CORBA.Object var3 = null;
         if (var2 instanceof org.omg.CORBA.Object) {
            var3 = (org.omg.CORBA.Object)var2;
         } else if (var2 instanceof String) {
            var3 = this.string_to_object((String)var2);
            this.cacheInitialReference(var1, var3);
         } else if (this.defaultInitialRef != null) {
            var3 = this.string_to_object(this.defaultInitialRef + "/" + var1);
            this.cacheInitialReference(var1, var3);
         } else if (this.host != null && this.port >= 0) {
            try {
               IOR var4 = IORManager.createIOR(this.protocol, this.host, this.port, var1, 1, 2);
               if (var1.equals("NameService")) {
                  var4 = IORManager.locateNameService(var4, this.timeout);
               } else {
                  var4 = IORManager.locateInitialReference(var4, this.channelName, this.timeout);
               }

               var3 = (org.omg.CORBA.Object)IIOPReplacer.resolveObject(var4);
               this.cacheInitialReference(var1, var3);
            } catch (IOException var5) {
               throw new InvalidName();
            }
         } else {
            var3 = InitialReferences.getInitialReferenceObject(var1);
         }

         if (var3 == null) {
            throw new InvalidName();
         } else {
            return var3;
         }
      }
   }

   private void cacheInitialReference(String var1, org.omg.CORBA.Object var2) {
      if (!var1.equals("NameService") || !IIOPClientService.reconnectOnBootstrap) {
         this.initialRefs.put(var1, var2);
      }

   }

   public String[] list_initial_services() {
      return InitialReferences.getServiceList();
   }

   public String object_to_string(org.omg.CORBA.Object var1) {
      try {
         Object var2 = IIOPReplacer.getIIOPReplacer().replaceObject(var1);
         return var2 instanceof IOR ? ((IOR)var2).stringify() : null;
      } catch (IOException var3) {
         throw new BAD_PARAM(var3.getMessage(), 1330446346, CompletionStatus.COMPLETED_NO);
      }
   }

   public org.omg.CORBA.Object string_to_object(String var1) {
      try {
         if (var1.startsWith("IOR:")) {
            IOR var7 = IOR.destringify(var1);
            if ("iiops".equals(this.protocol)) {
               var7.getProfile().makeSecure();
            }

            Object var8 = IIOPReplacer.resolveObject(var7);
            if (!(var8 instanceof org.omg.CORBA.Object)) {
               var8 = IIOPReplacer.makeInvocationHandler(var7);
            }

            return (org.omg.CORBA.Object)var8;
         } else if (NameParser.isGIOPProtocol(var1)) {
            org.omg.CORBA.Object var2 = IORManager.createInitialReference(var1, this.timeout);
            String var3 = NameParser.getNameString(var1);
            return var3.length() > 0 ? Utils.narrowContext(var2).resolve(Utils.stringToNameComponent(var3)) : var2;
         } else {
            throw new NamingException("Bad stringified object reference: " + var1);
         }
      } catch (NamingException var4) {
         throw Utils.unwrapNamingException(new BAD_PARAM(var4.getMessage(), 1330446343, CompletionStatus.COMPLETED_NO), var4);
      } catch (UserException var5) {
         throw Utils.initCORBAExceptionWithCause(new BAD_PARAM(var5.getMessage(), 1330446343, CompletionStatus.COMPLETED_NO), var5);
      } catch (IOException var6) {
         throw Utils.initCORBAExceptionWithCause(new BAD_PARAM(var6.getMessage(), 1330446346, CompletionStatus.COMPLETED_NO), var6);
      }
   }

   public void connect(org.omg.CORBA.Object var1) {
      try {
         StubReference var2 = OIDManager.getInstance().getReplacement(var1);
      } catch (ClassCastException var3) {
         throw new BAD_PARAM("Couldn't connect Object to the ORB", 1330446346, CompletionStatus.COMPLETED_NO);
      } catch (RemoteException var4) {
         throw (BAD_PARAM)(new BAD_PARAM(var4.getMessage(), 1330446346, CompletionStatus.COMPLETED_NO)).initCause(var4);
      }
   }

   public void disconnect(org.omg.CORBA.Object var1) {
      ServerReference var2 = OIDManager.getInstance().getServerReference(var1);
      if (var2 != null) {
         OIDManager.getInstance().removeServerReference(var2);
      }

   }

   public void destroy() {
   }

   public NVList create_list(int var1) {
      return new NVListImpl(var1);
   }

   public NVList create_operation_list(org.omg.CORBA.Object var1) {
      throw new NO_IMPLEMENT();
   }

   public NamedValue create_named_value(String var1, Any var2, int var3) {
      return new NamedValueImpl(var1, var2, var3);
   }

   public ExceptionList create_exception_list() {
      return new ExceptionListImpl();
   }

   public ContextList create_context_list() {
      throw new NO_IMPLEMENT();
   }

   public Context get_default_context() {
      throw new NO_IMPLEMENT();
   }

   public Environment create_environment() {
      return new EnvironmentImpl();
   }

   public OutputStream create_output_stream() {
      return new IIOPOutputStream(this);
   }

   public void send_multiple_requests_oneway(Request[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].send_oneway();
      }

   }

   public void send_multiple_requests_deferred(Request[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].send_deferred();
         this.deferred.add(var1[var2]);
      }

   }

   public synchronized boolean poll_next_response() {
      Iterator var1 = this.deferred.iterator();

      do {
         if (!var1.hasNext()) {
            return false;
         }
      } while(!((Request)var1.next()).poll_response());

      return true;
   }

   public synchronized Request get_next_response() throws WrongTransaction {
      Iterator var1 = this.deferred.iterator();

      Request var2;
      do {
         if (!var1.hasNext()) {
            throw new BAD_INV_ORDER("No ready response");
         }

         var2 = (Request)var1.next();
      } while(!var2.poll_response());

      var1.remove();
      var2.get_response();
      return var2;
   }

   public void run() {
   }

   public void shutdown(boolean var1) {
   }

   public boolean work_pending() {
      throw new NO_IMPLEMENT();
   }

   public void perform_work() {
      throw new NO_IMPLEMENT();
   }

   public TypeCode get_primitive_tc(TCKind var1) {
      return TypeCodeImpl.get_primitive_tc(var1);
   }

   public TypeCode create_struct_tc(String var1, String var2, StructMember[] var3) {
      return TypeCodeImpl.create_struct_tc(15, var1, var2, var3);
   }

   public TypeCode create_union_tc(String var1, String var2, TypeCode var3, UnionMember[] var4) {
      return TypeCodeImpl.create_union_tc(var1, var2, var3, var4);
   }

   public TypeCode create_enum_tc(String var1, String var2, String[] var3) {
      return TypeCodeImpl.create_enum_tc(var1, var2, var3);
   }

   public TypeCode create_alias_tc(String var1, String var2, TypeCode var3) {
      return new TypeCodeImpl(21, new RepositoryId(var1), var2, var3);
   }

   public TypeCode create_exception_tc(String var1, String var2, StructMember[] var3) {
      return TypeCodeImpl.create_struct_tc(22, var1, var2, var3);
   }

   public TypeCode create_interface_tc(String var1, String var2) {
      return new TypeCodeImpl(14, new RepositoryId(var1), var2);
   }

   public TypeCode create_string_tc(int var1) {
      return TypeCodeImpl.create_string_tc(var1);
   }

   public TypeCode create_wstring_tc(int var1) {
      return TypeCodeImpl.create_wstring_tc(var1);
   }

   public TypeCode create_sequence_tc(int var1, TypeCode var2) {
      return TypeCodeImpl.create_sequence_tc(var1, var2);
   }

   public TypeCode create_recursive_sequence_tc(int var1, int var2) {
      throw new NO_IMPLEMENT();
   }

   public TypeCode create_array_tc(int var1, TypeCode var2) {
      return TypeCodeImpl.create_array_tc(var1, var2);
   }

   public TypeCode create_native_tc(String var1, String var2) {
      return new TypeCodeImpl(31, new RepositoryId(var1), var2);
   }

   public TypeCode create_abstract_interface_tc(String var1, String var2) {
      return new TypeCodeImpl(32, new RepositoryId(var1), var2);
   }

   public TypeCode create_fixed_tc(short var1, short var2) {
      return TypeCodeImpl.create_fixed_tc(var1, var2);
   }

   public TypeCode create_value_tc(String var1, String var2, short var3, TypeCode var4, ValueMember[] var5) {
      return TypeCodeImpl.create_value_tc(var1, var2, var3, var4, var5);
   }

   public TypeCode create_recursive_tc(String var1) {
      return TypeCodeImpl.get_primitive_tc(1);
   }

   public TypeCode create_value_box_tc(String var1, String var2, TypeCode var3) {
      return new TypeCodeImpl(30, new RepositoryId(var1), var2, var3);
   }

   public Any create_any() {
      return new AnyImpl();
   }

   public Policy create_policy(int var1, Any var2) throws PolicyError {
      Policy var3 = ((POAImpl)this.getRootPOA()).create_policy(var1, var2);
      if (var3 != null) {
         return var3;
      } else {
         switch (var1) {
            case 28:
               return new RequestEndTimePolicyImpl(UtcTHelper.extract(var2));
            case 30:
               return new ReplyEndTimePolicyImpl(UtcTHelper.extract(var2));
            case 31:
               return new RelativeRequestTimeoutPolicyImpl(TimeTHelper.extract(var2));
            case 32:
               return new RelativeRoundtripTimeoutPolicyImpl(TimeTHelper.extract(var2));
            case 55:
               return new InvocationPolicyImpl(var2.extract_ushort());
            case 56:
               return new OTSPolicyImpl(var2.extract_ushort());
            default:
               throw new PolicyError("create_policy()", (short)1);
         }
      }
   }

   public Current get_current() {
      throw new NO_IMPLEMENT();
   }

   public boolean get_service_information(short var1, ServiceInformationHolder var2) {
      throw new NO_IMPLEMENT();
   }

   public DynAny create_dyn_any(Any var1) {
      throw new NO_IMPLEMENT();
   }

   public DynAny create_basic_dyn_any(TypeCode var1) throws InconsistentTypeCode {
      throw new NO_IMPLEMENT();
   }

   public DynStruct create_dyn_struct(TypeCode var1) throws InconsistentTypeCode {
      throw new NO_IMPLEMENT();
   }

   public DynSequence create_dyn_sequence(TypeCode var1) throws InconsistentTypeCode {
      throw new NO_IMPLEMENT();
   }

   public DynArray create_dyn_array(TypeCode var1) throws InconsistentTypeCode {
      throw new NO_IMPLEMENT();
   }

   public DynUnion create_dyn_union(TypeCode var1) throws InconsistentTypeCode {
      throw new NO_IMPLEMENT();
   }

   public DynEnum create_dyn_enum(TypeCode var1) throws InconsistentTypeCode {
      throw new NO_IMPLEMENT();
   }

   protected static void p(String var0) {
      System.out.println("<ORB> " + var0);
   }

   private static final POA createRootPOA(String var0) {
      try {
         return var0 == null ? (new POAImpl("RootPOA")).export() : (new POAImpl(var0 + "/" + "RootPOA")).export();
      } catch (IOException var2) {
         throw (BAD_PARAM)(new BAD_PARAM(var2.getMessage(), 1330446346, CompletionStatus.COMPLETED_NO)).initCause(var2);
      }
   }

   private final synchronized POA getRootPOA() {
      if (this.rootPOA == null) {
         this.rootPOA = createRootPOA(this.name);
         this.initialRefs.put("RootPOA", this.rootPOA);
      }

      return this.rootPOA;
   }

   // $FF: synthetic method
   ORB(boolean var1, Object var2) {
      this(var1);
   }

   private static final class SingletonMaker {
      private static final ORB ORB = new ORB(true);
   }
}
