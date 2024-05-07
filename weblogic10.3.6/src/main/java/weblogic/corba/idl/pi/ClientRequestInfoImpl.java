package weblogic.corba.idl.pi;

import org.omg.CORBA.Any;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA_2_3.portable.ObjectImpl;
import org.omg.Dynamic.Parameter;
import org.omg.IOP.ServiceContext;
import org.omg.IOP.TaggedComponent;
import org.omg.IOP.TaggedProfile;
import org.omg.PortableInterceptor.ClientRequestInfo;
import org.omg.PortableInterceptor.InvalidSlot;
import weblogic.corba.idl.AnyImpl;
import weblogic.corba.idl.TypeCodeImpl;
import weblogic.iiop.ReplyMessage;
import weblogic.iiop.RequestMessage;

public class ClientRequestInfoImpl extends ObjectImpl implements ClientRequestInfo {
   private RequestMessage request;
   private ReplyMessage reply;

   public ClientRequestInfoImpl(RequestMessage var1) {
      this.request = var1;
   }

   public ClientRequestInfoImpl(ReplyMessage var1) {
      this.reply = var1;
   }

   public String received_exception_id() {
      return this.reply.getExceptionId().toString();
   }

   public Any received_exception() {
      AnyImpl var1 = new AnyImpl();
      TypeCode var2 = TypeCodeImpl.create_struct_tc(22, this.received_exception_id(), "Exception", new StructMember[0]);
      var1.type(var2);
      return var1;
   }

   public Object effective_target() {
      return null;
   }

   public Object target() {
      return null;
   }

   public Policy get_request_policy(int var1) {
      return null;
   }

   public void add_request_service_context(ServiceContext var1, boolean var2) {
      this.request.getServiceContexts().addServiceContext(new weblogic.iiop.ServiceContext(var1.context_id, var1.context_data));
   }

   public TaggedComponent get_effective_component(int var1) {
      return null;
   }

   public TaggedComponent[] get_effective_components(int var1) {
      return new TaggedComponent[0];
   }

   public TaggedProfile effective_profile() {
      return null;
   }

   public int request_id() {
      return this.request == null ? this.reply.getRequestID() : this.request.getRequestID();
   }

   public short reply_status() {
      return (short)this.reply.getReplyStatus();
   }

   public short sync_scope() {
      return 0;
   }

   public boolean response_expected() {
      return !this.request.isOneWay();
   }

   public String operation() {
      return this.request.getOperationName();
   }

   public String[] contexts() {
      throw new NO_IMPLEMENT("contexts()");
   }

   public String[] operation_context() {
      throw new NO_IMPLEMENT("operation_contexts()");
   }

   public Any result() {
      return null;
   }

   public Any get_slot(int var1) throws InvalidSlot {
      return null;
   }

   public Object forward_reference() {
      return null;
   }

   public TypeCode[] exceptions() {
      return new TypeCode[0];
   }

   public Parameter[] arguments() {
      return new Parameter[0];
   }

   public ServiceContext get_reply_service_context(int var1) {
      weblogic.iiop.ServiceContext var2 = this.reply.getServiceContext(var1);
      return new ServiceContext(var2.getContextId(), var2.getContextData());
   }

   public ServiceContext get_request_service_context(int var1) {
      weblogic.iiop.ServiceContext var2 = this.request.getServiceContext(var1);
      return new ServiceContext(var2.getContextId(), var2.getContextData());
   }

   public String[] _ids() {
      return new String[]{"IDL:omg.org/PortableInterceptor/ClientRequestInfo:1.0"};
   }
}
