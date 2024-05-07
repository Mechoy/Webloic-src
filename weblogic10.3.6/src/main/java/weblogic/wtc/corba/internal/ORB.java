package weblogic.wtc.corba.internal;

import com.bea.core.jatmi.common.ntrace;
import java.applet.Applet;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
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
import org.omg.CORBA.INTERNAL;
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
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.WrongTransaction;
import org.omg.CORBA.ORBPackage.InconsistentTypeCode;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.portable.OutputStream;
import weblogic.corba.cos.naming.RootNamingContextImpl;
import weblogic.iiop.IOR;

public class ORB extends org.omg.CORBA_2_3.ORB {
   private static final String ORBClassProperty = "org.omg.CORBA.ORBClass";
   private static final String ORBClassValue = "weblogic.wtc.corba.internal.DelegatedSunORB";
   private static final String ORBSocketFactoryProperty = "org.omg.CORBA.ORBSocketFactoryClass";
   private static final String ORBSocketFactoryValue = "weblogic.wtc.corba.internal.ORBSocketFactory";
   private static final String ORBWtcSocketFactoryValue = "weblogic.wtc.corba.internal.ORBWtcSocketFactory";
   private static final String ORBInitialHost = "org.omg.CORBA.ORBInitialHost";
   private static final String ORBInitialPort = "org.omg.CORBA.ORBInitialPort";
   private org.omg.CORBA.ORB delegate;
   private String defaultInitialRef;
   private Hashtable initialRefs = new Hashtable();
   private boolean useInitialHost = false;
   private ORBInitialRef orbInitialRef = new ORBInitialRef(this);

   protected void set_parameters(String[] var1, Properties var2) {
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORB/set_parameters/" + var1 + "/" + var2);
      }

      Object var4 = null;
      this.ProcessInitialReferences(var1);
      Properties var5 = new Properties();
      var5.setProperty("org.omg.CORBA.ORBClass", "weblogic.wtc.corba.internal.DelegatedSunORB");
      if (System.getProperty("java.specification.version").equals("1.4")) {
         var5.setProperty("org.omg.CORBA.ORBSocketFactoryClass", "weblogic.wtc.corba.internal.ORBWtcSocketFactory");
      } else {
         var5.setProperty("org.omg.CORBA.ORBSocketFactoryClass", "weblogic.wtc.corba.internal.ORBSocketFactory");
      }

      if (var2 != null) {
         String var6 = var2.getProperty("org.omg.CORBA.ORBInitialHost");
         if (var6 != null) {
            var5.setProperty("org.omg.CORBA.ORBInitialHost", var6);
            this.useInitialHost = true;
         }

         var6 = var2.getProperty("org.omg.CORBA.ORBInitialPort");
         if (var6 != null) {
            var5.setProperty("org.omg.CORBA.ORBInitialPort", var6);
            this.useInitialHost = true;
         }
      }

      this.delegate = org.omg.CORBA_2_3.ORB.init((String[])var4, var5);
      if (var3) {
         ntrace.doTrace("]/ORB/set_parameters/50");
      }

   }

   protected void set_parameters(Applet var1, Properties var2) {
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORB/set_parameters/" + var1 + "/" + var2);
      }

      Properties var4 = new Properties();
      var4.setProperty("org.omg.CORBA.ORBClass", "weblogic.wtc.corba.internal.DelegatedSunORB");
      this.delegate = org.omg.CORBA_2_3.ORB.init(var1, var4);
      if (var3) {
         ntrace.doTrace("]/ORB/set_parameters/50");
      }

   }

   public org.omg.CORBA.Object resolve_initial_references(String var1) throws InvalidName {
      org.omg.CORBA.Object var2 = null;
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORB/resolve_initial_references/" + var1);
      }

      String var4 = (String)this.initialRefs.get(var1);
      if (var4 == null && this.defaultInitialRef != null) {
         var4 = this.defaultInitialRef + "/" + var1;
      } else if (var4 == null) {
         var4 = var1;
      }

      if (var3) {
         ntrace.doTrace("/ORB/resolve_initial_references/20/" + var4);
      }

      if (var4 != null && (var4.startsWith("corbaloc:tgiop:") || var4.startsWith("corbaname:tgiop:"))) {
         var2 = this.orbInitialRef.convertTGIOPURLToObject(var4);
      } else if (var4 != null && var4.equals("NameService") && !this.useInitialHost) {
         try {
            IOR var5 = RootNamingContextImpl.getRootNamingContext().getIOR();
            var2 = this.delegate.string_to_object(var5.stringify());
         } catch (IOException var6) {
            throw new INTERNAL();
         }
      } else {
         var2 = this.delegate.resolve_initial_references(var1);
      }

      if (var3) {
         ntrace.doTrace("]/ORB/resolve_initial_references/50/" + var2);
      }

      return var2;
   }

   private void ProcessInitialReferences(String[] var1) {
      if (var1 != null) {
         boolean var2 = ntrace.isTraceEnabled(8);
         if (var2) {
            ntrace.doTrace("[/ORB/ProcessInitialReferences/" + var1);
         }

         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            if (var2) {
               ntrace.doTrace("]/ORB/ProcessInitialReferences/10/" + var1[var4]);
            }

            if (var1[var4] != null && var1[var4].startsWith("-ORBDefaultInitRef") && var4 + 1 < var3 && var1[var4 + 1] != null) {
               this.defaultInitialRef = var1[var4 + 1];
               ++var4;
               if (var2) {
                  ntrace.doTrace("]/ORB/ProcessInitialReferences/20/" + this.defaultInitialRef);
               }
            }

            if (var1[var4] != null && var1[var4].startsWith("-ORBInitRef") && var4 + 1 < var3 && var1[var4 + 1] != null) {
               String var5 = var1[var4 + 1];
               ++var4;
               if (var5.indexOf(61) != -1) {
                  String var6 = var5.substring(var5.indexOf(61) + 1).trim();
                  String var7 = var5.substring(0, var5.indexOf(61)).trim();
                  this.initialRefs.put(var7, var6);
                  if (var2) {
                     ntrace.doTrace("]/ORB/ProcessInitialReferences/30/" + var7 + "/" + var6);
                  }
               }
            }
         }

         if (var2) {
            ntrace.doTrace("]/ORB/ProcessInitialReferences/100");
         }

      }
   }

   public void connect(org.omg.CORBA.Object var1) {
      this.delegate.connect(var1);
   }

   public void destroy() {
      this.delegate.destroy();
   }

   public void disconnect(org.omg.CORBA.Object var1) {
      this.delegate.disconnect(var1);
   }

   public String[] list_initial_services() {
      return this.delegate.list_initial_services();
   }

   public String object_to_string(org.omg.CORBA.Object var1) {
      return this.delegate.object_to_string(var1);
   }

   public org.omg.CORBA.Object string_to_object(String var1) {
      org.omg.CORBA.Object var2 = null;
      boolean var3 = ntrace.isTraceEnabled(8);
      if (var3) {
         ntrace.doTrace("[/ORB/string_to_object/" + var1);
      }

      String var4;
      if (var1 != null && var1.startsWith("corbaloc:rir:")) {
         var4 = var1.substring("corbaloc:rir:".length());
         if (var4.length() == 0) {
            var4 = "NameService";
         } else {
            if (!var4.startsWith("/")) {
               if (var3) {
                  ntrace.doTrace("*]/ORB/string_to_object/20");
               }

               throw new BAD_PARAM();
            }

            var4 = var4.substring(1);
         }

         try {
            var2 = this.resolve_initial_references(var4);
         } catch (InvalidName var9) {
            if (var3) {
               ntrace.doTrace("*]/ORB/string_to_object/25/" + var9);
            }

            throw new BAD_PARAM();
         }

         if (var3) {
            ntrace.doTrace("]/ORB/string_to_object/30/" + var2);
         }

         return var2;
      } else if (var1 != null && var1.startsWith("corbaloc:tgiop:")) {
         try {
            var2 = this.orbInitialRef.convertTGIOPURLToObject(var1);
         } catch (InvalidName var10) {
            if (var3) {
               ntrace.doTrace("*]/ORB/string_to_object/35/" + var10);
            }

            throw new BAD_PARAM();
         }

         if (var3) {
            ntrace.doTrace("]/ORB/string_to_object/40/" + var2);
         }

         return var2;
      } else if (var1 != null && var1.startsWith("corbaname:rir:")) {
         var4 = var1.substring("corbaname:rir:".length());
         if (var4.startsWith("#")) {
            var4 = var4.substring(1);
            org.omg.CORBA.Object var5 = null;

            try {
               var5 = this.resolve_initial_references("NameService");
            } catch (InvalidName var8) {
               if (var3) {
                  ntrace.doTrace("*]/ORB/string_to_object/50/" + var8);
               }

               throw new BAD_PARAM();
            }

            try {
               var2 = this.orbInitialRef.resolvePath(var5, var4);
            } catch (InvalidName var7) {
               throw new BAD_PARAM();
            }

            if (var3) {
               ntrace.doTrace("]/ORB/string_to_object/55/" + var2);
            }

            return var2;
         } else {
            if (var3) {
               ntrace.doTrace("*]/ORB/string_to_object/45");
            }

            throw new BAD_PARAM();
         }
      } else if (var1 != null && var1.startsWith("corbaname:tgiop:")) {
         try {
            var2 = this.orbInitialRef.convertTGIOPURLToObject(var1);
         } catch (InvalidName var11) {
            if (var3) {
               ntrace.doTrace("*]/ORB/string_to_object/60/" + var11);
            }

            throw new BAD_PARAM();
         }

         if (var3) {
            ntrace.doTrace("]/ORB/string_to_object/65/" + var2);
         }

         return var2;
      } else if (var1 != null && (var1.startsWith("corbaloc") || var1.startsWith("corbaname"))) {
         if (var3) {
            ntrace.doTrace("*]/ORB/string_to_object/70");
         }

         throw new NO_IMPLEMENT();
      } else {
         var2 = this.delegate.string_to_object(var1);
         if (var3) {
            ntrace.doTrace("]/ORB/string_to_object/75/" + var2);
         }

         return var2;
      }
   }

   public NVList create_list(int var1) {
      return this.delegate.create_list(var1);
   }

   public NVList create_operation_list(org.omg.CORBA.Object var1) {
      return this.delegate.create_operation_list(var1);
   }

   public NamedValue create_named_value(String var1, Any var2, int var3) {
      return this.delegate.create_named_value(var1, var2, var3);
   }

   public ExceptionList create_exception_list() {
      return this.delegate.create_exception_list();
   }

   public ContextList create_context_list() {
      return this.delegate.create_context_list();
   }

   public Context get_default_context() {
      return this.delegate.get_default_context();
   }

   public Environment create_environment() {
      return this.delegate.create_environment();
   }

   public OutputStream create_output_stream() {
      return this.delegate.create_output_stream();
   }

   public void send_multiple_requests_oneway(Request[] var1) {
      this.delegate.send_multiple_requests_oneway(var1);
   }

   public void send_multiple_requests_deferred(Request[] var1) {
      this.delegate.send_multiple_requests_deferred(var1);
   }

   public boolean poll_next_response() {
      return this.delegate.poll_next_response();
   }

   public Request get_next_response() throws WrongTransaction {
      return this.delegate.get_next_response();
   }

   public TypeCode get_primitive_tc(TCKind var1) {
      return this.delegate.get_primitive_tc(var1);
   }

   public TypeCode create_struct_tc(String var1, String var2, StructMember[] var3) {
      return this.delegate.create_struct_tc(var1, var2, var3);
   }

   public TypeCode create_union_tc(String var1, String var2, TypeCode var3, UnionMember[] var4) {
      return this.delegate.create_union_tc(var1, var2, var3, var4);
   }

   public TypeCode create_enum_tc(String var1, String var2, String[] var3) {
      return this.delegate.create_enum_tc(var1, var2, var3);
   }

   public TypeCode create_alias_tc(String var1, String var2, TypeCode var3) {
      return this.delegate.create_alias_tc(var1, var2, var3);
   }

   public TypeCode create_exception_tc(String var1, String var2, StructMember[] var3) {
      return this.delegate.create_exception_tc(var1, var2, var3);
   }

   public TypeCode create_interface_tc(String var1, String var2) {
      return this.delegate.create_interface_tc(var1, var2);
   }

   public TypeCode create_string_tc(int var1) {
      return this.delegate.create_string_tc(var1);
   }

   public TypeCode create_wstring_tc(int var1) {
      return this.delegate.create_wstring_tc(var1);
   }

   public TypeCode create_sequence_tc(int var1, TypeCode var2) {
      return this.delegate.create_sequence_tc(var1, var2);
   }

   public TypeCode create_recursive_sequence_tc(int var1, int var2) {
      return this.delegate.create_recursive_sequence_tc(var1, var2);
   }

   public TypeCode create_array_tc(int var1, TypeCode var2) {
      return this.delegate.create_array_tc(var1, var2);
   }

   public TypeCode create_native_tc(String var1, String var2) {
      return this.delegate.create_native_tc(var1, var2);
   }

   public TypeCode create_abstract_interface_tc(String var1, String var2) {
      return this.delegate.create_abstract_interface_tc(var1, var2);
   }

   public TypeCode create_fixed_tc(short var1, short var2) {
      return this.delegate.create_fixed_tc(var1, var2);
   }

   public TypeCode create_value_tc(String var1, String var2, short var3, TypeCode var4, ValueMember[] var5) {
      return this.delegate.create_value_tc(var1, var2, var3, var4, var5);
   }

   public TypeCode create_recursive_tc(String var1) {
      return this.delegate.create_recursive_tc(var1);
   }

   public TypeCode create_value_box_tc(String var1, String var2, TypeCode var3) {
      return this.delegate.create_value_box_tc(var1, var2, var3);
   }

   public Any create_any() {
      return this.delegate.create_any();
   }

   public Current get_current() {
      return this.delegate.get_current();
   }

   public void run() {
      this.delegate.run();
   }

   public void shutdown(boolean var1) {
      this.delegate.shutdown(var1);
   }

   public boolean work_pending() {
      return this.delegate.work_pending();
   }

   public void perform_work() {
      this.delegate.perform_work();
   }

   public boolean get_service_information(short var1, ServiceInformationHolder var2) {
      return this.delegate.get_service_information(var1, var2);
   }

   public DynAny create_dyn_any(Any var1) {
      return this.delegate.create_dyn_any(var1);
   }

   public DynAny create_basic_dyn_any(TypeCode var1) throws InconsistentTypeCode {
      return this.delegate.create_basic_dyn_any(var1);
   }

   public DynStruct create_dyn_struct(TypeCode var1) throws InconsistentTypeCode {
      return this.delegate.create_dyn_struct(var1);
   }

   public DynSequence create_dyn_sequence(TypeCode var1) throws InconsistentTypeCode {
      return this.delegate.create_dyn_sequence(var1);
   }

   public DynArray create_dyn_array(TypeCode var1) throws InconsistentTypeCode {
      return this.delegate.create_dyn_array(var1);
   }

   public DynUnion create_dyn_union(TypeCode var1) throws InconsistentTypeCode {
      return this.delegate.create_dyn_union(var1);
   }

   public DynEnum create_dyn_enum(TypeCode var1) throws InconsistentTypeCode {
      return this.delegate.create_dyn_enum(var1);
   }

   public Policy create_policy(int var1, Any var2) throws PolicyError {
      return this.delegate.create_policy(var1, var2);
   }
}
