package weblogic.wsee.message;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import weblogic.wsee.addressing.ActionHeader;
import weblogic.wsee.addressing.AddressingHeader;
import weblogic.wsee.addressing.CallbackToHeader;
import weblogic.wsee.addressing.EmbeddedWsdlHeader;
import weblogic.wsee.addressing.FaultDetailHeader;
import weblogic.wsee.addressing.FaultToHeader;
import weblogic.wsee.addressing.FromHeader;
import weblogic.wsee.addressing.InterfaceNameHeader;
import weblogic.wsee.addressing.MessageIdHeader;
import weblogic.wsee.addressing.RelatesToHeader;
import weblogic.wsee.addressing.ReplyToHeader;
import weblogic.wsee.addressing.ServiceNameHeader;
import weblogic.wsee.addressing.SetCookieHeader;
import weblogic.wsee.addressing.TimestampHeader;
import weblogic.wsee.addressing.ToHeader;
import weblogic.wsee.callback.CallbackInfoHeader;
import weblogic.wsee.callback.Wlw81CallbackHeader;
import weblogic.wsee.callback.controls.ControlCallbackInfoHeader;
import weblogic.wsee.cluster.CorrelationHeader;
import weblogic.wsee.cluster.ServiceIdentityHeader;
import weblogic.wsee.conversation.ContinueHeader;
import weblogic.wsee.conversation.StartHeader;
import weblogic.wsee.reliability.SAFServerHeader;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.headers.TestSequenceSSLHeader;
import weblogic.wsee.reliability.headers.UsesSequenceSSLHeader;
import weblogic.wsee.reliability.headers.UsesSequenceSTRHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;
import weblogic.wsee.workarea.WorkAreaHeader;
import weblogic.wsee.wsa.wsaddressing.WSAddressingConstants;

public class StandardMsgHeaderFactory implements MsgHeaderFactoryIntf {
   private Map headerClasses = new HashMap();
   private static Set needFillQNameHeaders = new HashSet();

   public MsgHeader createMsgHeader(QName var1) throws MsgHeaderException {
      try {
         Class var2 = (Class)this.headerClasses.get(var1);
         if (var2 != null) {
            MsgHeader var3 = (MsgHeader)var2.newInstance();
            if (needFillQNameHeaders.contains(var2)) {
               if (var3 instanceof AddressingHeader) {
                  ((AddressingHeader)var3).setName(var1);
               } else if (var3 instanceof WsrmHeader) {
                  ((WsrmHeader)var3).setNamespaceUri(var1.getNamespaceURI());
               }
            }

            return var3;
         } else {
            return null;
         }
      } catch (MsgHeaderException var4) {
         throw var4;
      } catch (IllegalAccessException var5) {
         throw new MsgHeaderException("Could not build header for " + var1, var5);
      } catch (InstantiationException var6) {
         throw new MsgHeaderException("Could not build header for " + var1, var6);
      }
   }

   void addMsgHeaderClass(QName var1, Class var2) {
      this.headerClasses.put(var1, var2);
   }

   StandardMsgHeaderFactory() {
      needFillQNameHeaders.add(ToHeader.class);
      needFillQNameHeaders.add(FromHeader.class);
      needFillQNameHeaders.add(MessageIdHeader.class);
      needFillQNameHeaders.add(ReplyToHeader.class);
      needFillQNameHeaders.add(FaultToHeader.class);
      needFillQNameHeaders.add(ActionHeader.class);
      needFillQNameHeaders.add(RelatesToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_TO_10, ToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_TO, ToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_SOURCE_10, FromHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_SOURCE, FromHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_MESSAGE_ID_10, MessageIdHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_MESSAGE_ID, MessageIdHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_REPLY_TO_10, ReplyToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_REPLY_TO, ReplyToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_FAULT_TO_10, FaultToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_FAULT_TO, FaultToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_ACTION_10, ActionHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_ACTION, ActionHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_RELATES_TO_10, RelatesToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_RELATES_TO, RelatesToHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_FAULT_DETAIL, FaultDetailHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_FAULT_DETAIL_10, FaultDetailHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_METADATA_INTERFACE_NAME_10, InterfaceNameHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_HEADER_METADATA_SERVICE_NAME_10, ServiceNameHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_METADATA_EMBEDDED_WSDL_NAME_11, EmbeddedWsdlHeader.class);
      this.addMsgHeaderClass(WSAddressingConstants.WSA_METADATA_EMBEDDED_WSDL_NAME_20, EmbeddedWsdlHeader.class);
      this.addMsgHeaderClass(SetCookieHeader.NAME, SetCookieHeader.class);
      this.addMsgHeaderClass(TimestampHeader.NAME, TimestampHeader.class);
      this.addMsgHeaderClass(StartHeader.NAME, StartHeader.class);
      this.addMsgHeaderClass(ContinueHeader.NAME, ContinueHeader.class);
      this.addMsgHeaderClass(CallbackToHeader.NAME, CallbackToHeader.class);
      WsrmConstants.RMVersion[] var1 = WsrmConstants.RMVersion.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         WsrmConstants.RMVersion var4 = var1[var3];
         this.addMsgHeaderClass(WsrmHeader.getQName(AcknowledgementHeader.class, var4), AcknowledgementHeader.class);
         this.addMsgHeaderClass(WsrmHeader.getQName(AckRequestedHeader.class, var4), AckRequestedHeader.class);
         this.addMsgHeaderClass(WsrmHeader.getQName(SequenceHeader.class, var4), SequenceHeader.class);
         needFillQNameHeaders.add(AcknowledgementHeader.class);
         needFillQNameHeaders.add(AckRequestedHeader.class);
         needFillQNameHeaders.add(SequenceHeader.class);
         if (var4.isLaterThanOrEqualTo(WsrmConstants.RMVersion.RM_11)) {
            this.addMsgHeaderClass(WsrmHeader.getQName(UsesSequenceSTRHeader.class, var4), UsesSequenceSTRHeader.class);
            this.addMsgHeaderClass(WsrmHeader.getQName(UsesSequenceSSLHeader.class, var4), UsesSequenceSSLHeader.class);
            this.addMsgHeaderClass(WsrmHeader.getQName(TestSequenceSSLHeader.class, var4), TestSequenceSSLHeader.class);
            needFillQNameHeaders.add(UsesSequenceSTRHeader.class);
            needFillQNameHeaders.add(UsesSequenceSSLHeader.class);
            needFillQNameHeaders.add(TestSequenceSSLHeader.class);
         }
      }

      this.addMsgHeaderClass(WorkAreaHeader.NAME, WorkAreaHeader.class);
      this.addMsgHeaderClass(CorrelationHeader.NAME, CorrelationHeader.class);
      this.addMsgHeaderClass(ServiceIdentityHeader.NAME, ServiceIdentityHeader.class);
      this.addMsgHeaderClass(SAFServerHeader.NAME, SAFServerHeader.class);
      this.addMsgHeaderClass(CallbackInfoHeader.NAME, CallbackInfoHeader.class);
      this.addMsgHeaderClass(ControlCallbackInfoHeader.NAME, ControlCallbackInfoHeader.class);
      this.addMsgHeaderClass(Wlw81CallbackHeader.NAME, Wlw81CallbackHeader.class);
   }
}
