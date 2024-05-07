package weblogic.wsee.server;

public class WsLifeCycleEvent {
   public static final WsLifeCycleEvent WSRM_RECV_RST_BEFORE_SAVE = new WsLifeCycleEvent("WsrmRecvRstBeforeSave");
   public static final WsLifeCycleEvent WSRM_RECV_RST_AFTER_SAVE = new WsLifeCycleEvent("WsrmRecvRstAfterSave");
   public static final WsLifeCycleEvent WSRM_RECV_RST_AFTER_RSTR = new WsLifeCycleEvent("WsrmRecvRstAfterRstr");
   public static final WsLifeCycleEvent WSRM_RECV_CREATE_SEQ = new WsLifeCycleEvent("WsrmRecvCreateSeq");
   public static final WsLifeCycleEvent WSRM_RECV_AFTER_CREATE_SEQ_RES = new WsLifeCycleEvent("WsrmRecvAfterCreateSeqRes");
   public static final WsLifeCycleEvent WSRM_RECV_DURING_SEQ = new WsLifeCycleEvent("WsrmRecvDuringSeq");
   public static final WsLifeCycleEvent WSRM_RECV_BEFORE_SAF = new WsLifeCycleEvent("WsrmRecvBeforeSAF");
   public static final WsLifeCycleEvent WSRM_RECV_AFTER_ENQUEUE = new WsLifeCycleEvent("WsrmRecvAfterEnqueue");
   public static final WsLifeCycleEvent WSRM_RECV_BEFORE_RES_TO_SAF = new WsLifeCycleEvent("WsrmRecvBeforeResToSAF");
   public static final WsLifeCycleEvent WSRM_RECV_AFTER_ACK = new WsLifeCycleEvent("WsrmRecvAfterAck");
   public static final WsLifeCycleEvent WSRM_RECV_AFTER_RENEW = new WsLifeCycleEvent("WsrmRecvAfterRenew");
   public static final WsLifeCycleEvent WSRM_RECV_AFTER_RENEW_SAVE = new WsLifeCycleEvent("WsrmRecvAfterRenewSave");
   public static final WsLifeCycleEvent WSRM_SEND_RST_BEFORE_RSTR = new WsLifeCycleEvent("WsrmSendRstBeforeRstr");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_RSTR_BEFORE_CREATE_SEQ = new WsLifeCycleEvent("WsrmSendAfterRstrBeforeCreateSeq");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_CREATE_SEQ_BEFORE_RES = new WsLifeCycleEvent("WsrmSendAfterCreateSeqBeforeRes");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_CREATE_SEQ_RES_BEFORE_RM = new WsLifeCycleEvent("WsrmSendAfterCreateSeqResBeforeRM");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_RM_BEFORE_SEND = new WsLifeCycleEvent("WsrmSendAfterRMBeforeSend");
   public static final WsLifeCycleEvent WSRM_SEND_BEFORE_TERMINATE = new WsLifeCycleEvent("WsrmSendBeforeTerminate");
   public static final WsLifeCycleEvent WSRM_SEND_BEFORE_ACK = new WsLifeCycleEvent("WsrmSendBeforeAck");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_SEND_RENEW = new WsLifeCycleEvent("WsrmSendAfterSendRenew");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_RENEW_RES_BEFORE_SAVE = new WsLifeCycleEvent("WsrmSendAfterRenewResBeforeSave");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_RENEW_SAVE = new WsLifeCycleEvent("WsrmSendAfterRenewSave");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_VALIDATE_ACK_BEFORE_SAVE = new WsLifeCycleEvent("WsrmSendAfterValidateAckBeforeSave");
   public static final WsLifeCycleEvent WSRM_SEND_BEFORE_RECV_RES = new WsLifeCycleEvent("WsrmSendBeforeRecvRes");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_RES_SEC_VALIDATION = new WsLifeCycleEvent("WsrmSendAfterResSecValidation");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_RES_WSRM_SEC_VALIDATION = new WsLifeCycleEvent("WsrmSendAfterResWsrmSecValidation");
   public static final WsLifeCycleEvent WSRM_SEND_AFTER_RES_TO_SAF_BEFORE_QUEUE = new WsLifeCycleEvent("WsrmSendAfterResToSAFBeforeQueue");
   private String eventName;

   private WsLifeCycleEvent() {
   }

   private WsLifeCycleEvent(String var1) {
      this.eventName = var1;
   }

   public String getEventName() {
      return this.eventName;
   }
}
