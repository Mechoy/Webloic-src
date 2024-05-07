package weblogic.iiop;

import java.io.IOException;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Principal;
import org.omg.CORBA.SystemException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.iiop.csi.CompoundSecMechList;
import weblogic.kernel.Kernel;
import weblogic.security.subject.AbstractSubject;

public final class RequestMessage extends SequencedRequestMessage implements MessageTypeConstants {
   private boolean response_expected;
   private IOR ior;
   private TargetAddress target;
   private ObjectKey object_key;
   private String operation;
   private Principal requesting_principal;
   private CompoundSecMechList mechList;
   private boolean isForeign;
   private static final Object NULL = new Object();
   private Object subject;
   private Object cachedTxContext;
   private static final DebugLogger debugIIOPDetail = DebugLogger.getDebugLogger("DebugIIOPDetail");

   public RequestMessage(EndPoint var1, MessageHeader var2, IIOPInputStream var3) {
      this.subject = NULL;
      this.msgHdr = var2;
      this.endPoint = var1;
      this.inputStream = var3;
      if (var1 != null) {
         var3.setCodeSets(var1.getCharCodeSet(), var1.getWcharCodeSet());
      }

      if (!this.isFragmented()) {
         this.flush();
      } else {
         this.request_id = var3.peek_long();
      }

   }

   public RequestMessage(EndPoint var1, IOR var2, String var3, boolean var4) {
      this.subject = NULL;
      this.msgHdr = new MessageHeader(0, var1.getMinorVersion());
      this.endPoint = var1;
      this.ior = var2;
      boolean var5 = true;
      switch (var2.getProfile().getMinorVersion()) {
         case 2:
            if (!var1.getFlag(8)) {
               this.addServiceContext(BiDirIIOPContextImpl.getContext());
               this.flags |= 8;
            }
         case 0:
         case 1:
            if (!var1.getFlag(4)) {
               this.addServiceContext(VendorInfo.VENDOR_INFO);
               this.flags |= 4;
            }

            if (!var1.getFlag(1) && var2.isRemote()) {
               if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
                  p("Using negotiated char codeset = " + Integer.toHexString(var1.getCharCodeSet()) + ", negotiated wchar codeset = " + Integer.toHexString(var1.getWcharCodeSet()));
               }

               this.addServiceContext(new CodeSet(var1.getCharCodeSet(), var1.getWcharCodeSet()));
               this.flags |= 1;
            }

            if (!var1.getFlag(2)) {
               this.addServiceContext(SendingContextRunTime.getSendingContextRuntime());
               var1.setFlag(2);
            }
         default:
            this.request_id = var1.getNextRequestID();
            this.response_expected = !var4;
            this.object_key = var2.getProfile().getObjectKey();
            this.target = var2.getProfile().getTargetAddress();
            this.operation = var3;
            this.requesting_principal = null;
            this.mechList = (CompoundSecMechList)var2.getProfile().getComponent(33);
            this.isForeign = var2.isRemote();
            this.setMaxStreamFormatVersion(var2.getProfile().getMaxStreamFormatVersion());
      }
   }

   public final ServiceContextList getOutboundServiceContexts() {
      return this.serviceContexts.generateOutboundContexts();
   }

   public final ObjectKey getObjectKey() {
      return this.object_key;
   }

   public final IOR getIOR() {
      return this.ior;
   }

   public final String getOperationName() {
      return this.operation;
   }

   public final Principal getPrincipal() {
      return this.requesting_principal;
   }

   public boolean isOneWay() {
      return !this.response_expected;
   }

   public void setOneWay() {
      this.response_expected = false;
   }

   public AbstractSubject getSubject() {
      if (this.subject == NULL) {
         this.subject = this.endPoint.getSubject(this);
      }

      return (AbstractSubject)this.subject;
   }

   public void write(IIOPOutputStream var1) throws SystemException {
      this.msgHdr.write(var1);
      switch (this.getMinorVersion()) {
         case 0:
         case 1:
            this.writeServiceContexts(var1);
            var1.write_ulong(this.request_id);
            var1.write_boolean(this.response_expected);
            if (this.getMinorVersion() == 1) {
               this.produceMinorVersion1Padding(var1);
            }

            this.object_key.write(var1);
            var1.write_string(this.operation);
            this.writeRequestingPrincipal(var1);
            break;
         case 2:
            var1.write_ulong(this.request_id);
            if (this.response_expected) {
               var1.write_octet((byte)3);
            } else {
               var1.write_octet((byte)0);
            }

            this.produceMinorVersion1Padding(var1);
            this.target.write(var1);
            var1.write_string(this.operation);
            this.writeServiceContexts(var1);
      }

      this.alignOnEightByteBoundry(var1);
   }

   private void produceMinorVersion1Padding(IIOPOutputStream var1) {
      var1.write_octet((byte)0);
      var1.write_octet((byte)0);
      var1.write_octet((byte)0);
   }

   private void writeRequestingPrincipal(IIOPOutputStream var1) {
      if (this.requesting_principal != null) {
         var1.write_Principal(this.requesting_principal);
      } else {
         var1.write_long(0);
      }

   }

   public void read(IIOPInputStream var1) throws SystemException {
      switch (this.getMinorVersion()) {
         case 0:
            this.readServiceContexts(var1);
            this.request_id = var1.read_ulong();
            this.response_expected = var1.read_boolean();
            this.object_key = new ObjectKey(var1);
            this.operation = var1.read_string();
            this.requesting_principal = var1.read_Principal();
            break;
         case 1:
            this.readServiceContexts(var1);
            this.request_id = var1.read_ulong();
            this.response_expected = var1.read_boolean();
            var1.read_octet();
            var1.read_octet();
            var1.read_octet();
            this.object_key = new ObjectKey(var1);
            this.operation = var1.read_string();
            this.requesting_principal = var1.read_Principal();
            break;
         case 2:
            this.request_id = var1.read_ulong();
            byte var2 = var1.read_octet();
            switch (var2) {
               case 0:
                  this.response_expected = false;
                  break;
               case 3:
                  this.response_expected = true;
                  break;
               default:
                  throw new MARSHAL("Unknown response_expected flags: " + var2);
            }

            var1.read_octet();
            var1.read_octet();
            var1.read_octet();
            this.target = new TargetAddress(var1);
            this.object_key = this.target.object_key;
            this.operation = var1.read_string();
            if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
               p("operation= " + this.operation);
            }

            this.readServiceContexts(var1);
      }

      if (Kernel.DEBUG && debugIIOPDetail.isDebugEnabled()) {
         p("request_id = " + this.request_id + " response_expected = " + this.response_expected + " operation = " + this.operation);
      }

      this.alignOnEightByteBoundry(var1);
   }

   public CompoundSecMechList getMechanismListForRequest() {
      return this.mechList;
   }

   public boolean isForeign() {
      return this.isForeign;
   }

   public final Object getCachedTxContext() {
      return this.cachedTxContext;
   }

   public final void setCachedTxContext(Object var1) {
      this.cachedTxContext = var1;
   }

   public void redirect(IOR var1) throws IOException {
      this.endPoint = EndPointManager.findOrCreateEndPoint(var1);
      this.object_key = var1.getProfile().getObjectKey();
   }

   public final void close() {
      super.close();
      this.response_expected = false;
      this.target.reset();
      this.object_key = null;
      this.operation = null;
      this.requesting_principal = null;
      this.mechList = null;
      this.isForeign = false;
      this.cachedTxContext = null;
   }

   protected static void p(String var0) {
      System.err.println("<RequestMessage> " + var0);
   }
}
