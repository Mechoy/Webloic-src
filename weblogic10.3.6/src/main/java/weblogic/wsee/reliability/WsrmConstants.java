package weblogic.wsee.reliability;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import weblogic.webservice.core.soap.NameImpl;

public class WsrmConstants {
   public static final String BASE_RETRANSMISSION_INTERVAL = "weblogic.wsee.wsrm.BaseRetransmissionInterval";
   public static final String RETRANSMISSION_EXPONENTIAL_BACKOFF = "weblogic.wsee.wsrm.RetransmissionExponentialBackoff";
   public static final String NON_BUFFERED_SOURCE = "weblogic.wsee.wsrm.NonBufferedSource";
   public static final String RETRY_COUNT = "weblogic.wsee.wsrm.RetryCount";
   public static final String RETRY_DELAY = "weblogic.wsee.wsrm.RetryDelay";
   public static final String ACKNOWLEDGEMENT_INTERVAL = "weblogic.wsee.wsrm.AcknowledgementInterval";
   public static final String NON_BUFFERED_DESTINATION = "weblogic.wsee.wsrm.NonBufferedDestination";
   public static final String BUFFER_QUEUE_JNDI_NAME = "weblogic.wsee.wsrm.BufferQueueJndiName";
   public static final String BUFFER_QUEUE_MDB_RUNAS_PRINCIPAL_NAME = "weblogic.wsee.wsrm.BufferQueueMdbRunAsPrincipalName";
   public static final String INACTIVITY_TIMEOUT = "weblogic.wsee.wsrm.InactivityTimeout";
   public static final String SEQUENCE_EXPIRATION = "weblogic.wsee.wsrm.SequenceExpiration";
   public static final String TEST_SEQUENCE_SSL = "weblogic.wsee.reliability.TestSequenceSSL";
   public static final String ASYNC_FAULT = "weblogic.wsee.reliability.asyncfault";
   public static final String TEMP_ID__For_New_Sequence = "New";
   public static final String[] PROP_NAMES_FOR_RM_SOURCE_ONLY = new String[]{"weblogic.wsee.wsrm.BaseRetransmissionInterval", "weblogic.wsee.wsrm.RetransmissionExponentialBackoff"};
   public static final String[] PROP_NAMES_FOR_RM_DESTINATION_ONLY = new String[]{"weblogic.wsee.wsrm.RetryCount", "weblogic.wsee.wsrm.RetryDelay"};
   public static final String[] PROP_NAMES_FOR_RM_SOURCE_OR_DESTINATION = new String[]{"weblogic.wsee.wsrm.InactivityTimeout", "weblogic.wsee.wsrm.AcknowledgementInterval", "weblogic.wsee.wsrm.SequenceExpiration"};
   public static final String[] PROP_NAMES_FOR_RM_SOURCE;
   public static final String[] PROP_NAMES_FOR_RM_DESTINATION;
   public static final String RM_VERSION = "weblogic.wsee.wsrm.RMVersion";
   public static final String FORCE_WSRM_1_0_CLIENT = "weblogic.wsee.reliability.forceWSRM10Client";
   public static final String SEQUENCE_STR = "weblogic.wsee.wsrm.SequenceSTR";
   public static final String SEQUENCE_TRANSPORT_SECURITY = "weblogic.wsee.wsrm.SequenceTransportSecurity";

   static {
      ArrayList var0 = new ArrayList();
      var0.addAll(Arrays.asList(PROP_NAMES_FOR_RM_SOURCE_ONLY));
      var0.addAll(Arrays.asList(PROP_NAMES_FOR_RM_SOURCE_OR_DESTINATION));
      PROP_NAMES_FOR_RM_SOURCE = (String[])var0.toArray(new String[var0.size()]);
      ArrayList var1 = new ArrayList();
      var1.addAll(Arrays.asList(PROP_NAMES_FOR_RM_DESTINATION_ONLY));
      var1.addAll(Arrays.asList(PROP_NAMES_FOR_RM_SOURCE_OR_DESTINATION));
      PROP_NAMES_FOR_RM_DESTINATION = (String[])var1.toArray(new String[var1.size()]);
   }

   public static enum DeliveryQOS {
      AtLeastOnce,
      AtMostOnce,
      ExactlyOnce;
   }

   public static enum IncompleteSequenceBehavior {
      DiscardEntireSequence,
      DiscardFollowingFirstGap,
      NoDiscard;
   }

   public static enum FaultCode {
      SENDER("Client", "Sender"),
      RECEIVER("Server", "Receiver");

      Map<SOAPVersion, String> versionToCodeMap = new HashMap();

      private FaultCode(String var3, String var4) {
         this.versionToCodeMap.put(WsrmConstants.SOAPVersion.SOAP_11, var3);
         this.versionToCodeMap.put(WsrmConstants.SOAPVersion.SOAP_12, var4);
      }

      public String getCodeLocalName(SOAPVersion var1) {
         String var2 = (String)this.versionToCodeMap.get(var1);
         return var2;
      }

      public String getCodeQualifiedName(SOAPVersion var1) {
         String var2 = (String)this.versionToCodeMap.get(var1);
         return var1.getPrefix() + ":" + var2;
      }

      public QName getCodeQName(SOAPVersion var1) {
         String var2 = (String)this.versionToCodeMap.get(var1);
         return new QName(var1.getNamespaceUri(), var2);
      }

      public Name getCodeName(SOAPVersion var1) {
         String var2 = (String)this.versionToCodeMap.get(var1);
         return new NameImpl(var2, var1.getPrefix(), var1.getNamespaceUri());
      }
   }

   public static enum HeaderElement {
      SEQUENCE(WsrmConstants.Element.SEQUENCE),
      ACK(WsrmConstants.Element.ACK),
      ACK_REQUESTED(WsrmConstants.Element.ACK_REQUESTED),
      CREATE_SEQUENCE(WsrmConstants.Element.CREATE_SEQUENCE),
      CREATE_SEQUENCE_RESPONSE(WsrmConstants.Element.CREATE_SEQUENCE_RESPONSE),
      TERMINATE_SEQUENCE(WsrmConstants.Element.TERMINATE_SEQUENCE),
      TERMINATE_SEQUENCE_RESPONSE(WsrmConstants.Element.TERMINATE_SEQUENCE_RESPONSE),
      CLOSE_SEQUENCE(WsrmConstants.Element.CLOSE_SEQUENCE),
      CLOSE_SEQUENCE_RESPONSE(WsrmConstants.Element.CLOSE_SEQUENCE_RESPONSE),
      FAULT_CODE(WsrmConstants.Element.FAULT_CODE);

      private Element elem;
      private static Set<QName> headerElementQNameSet = getHeaderElementQNameSet();

      private HeaderElement(Element var3) {
         this.elem = var3;
      }

      public QName getQualifiedName(RMVersion var1) {
         return this.elem.getQName(var1);
      }

      public static Set getHeaderElementQNameSet() {
         if (headerElementQNameSet == null) {
            headerElementQNameSet = new HashSet();
            RMVersion[] var0 = WsrmConstants.RMVersion.values();
            int var1 = var0.length;

            for(int var2 = 0; var2 < var1; ++var2) {
               RMVersion var3 = var0[var2];
               List var4 = getHeaderQNameListForRMVersion(var3);
               headerElementQNameSet.addAll(var4);
            }
         }

         return headerElementQNameSet;
      }

      private static List getHeaderQNameListForRMVersion(RMVersion var0) {
         ArrayList var1 = new ArrayList();
         HeaderElement[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            HeaderElement var5 = var2[var4];
            var1.add(var5.getQualifiedName(var0));
         }

         return var1;
      }

      static {
         headerElementQNameSet.add(WsrmConstants.Element.USES_SEQUENCE_STR.getQName(WsrmConstants.RMVersion.RM_11));
         headerElementQNameSet.add(WsrmConstants.Element.USES_SEQUENCE_SSL.getQName(WsrmConstants.RMVersion.RM_11));
      }
   }

   public static enum Element {
      SEQUENCE("Sequence"),
      ACK("SequenceAcknowledgement"),
      ACK_REQUESTED("AckRequested"),
      CREATE_SEQUENCE("CreateSequence"),
      CREATE_SEQUENCE_RESPONSE("CreateSequenceResponse"),
      TERMINATE_SEQUENCE("TerminateSequence"),
      TERMINATE_SEQUENCE_RESPONSE("TerminateSequenceResponse"),
      CLOSE_SEQUENCE("CloseSequence"),
      CLOSE_SEQUENCE_RESPONSE("CloseSequenceResponse"),
      ACKS_TO("AcksTo"),
      EXPIRES("Expires"),
      OFFER("Offer"),
      ACCEPT("Accept"),
      IDENTIFIER("Identifier"),
      ACK_RANGE("AcknowledgementRange"),
      MESSAGE_NUMBER("MessageNumber"),
      LAST_MESSAGE("LastMessage"),
      NACK("Nack"),
      LOWER("Lower"),
      UPPER("Upper"),
      SEQUENCE_FAULT("SequenceFault"),
      FAULT_CODE("FaultCode"),
      ENDPOINT("Endpoint"),
      INCOMPLETE_SEQUENCE_BEHAVIOR("IncompleteSequenceBehavior"),
      NONE("None"),
      FINAL("Final"),
      LAST_MSG_NUMBER("LastMsgNumber"),
      USES_SEQUENCE_STR("UsesSequenceSTR"),
      USES_SEQUENCE_SSL("UsesSequenceSSL"),
      TEST_SEQUENCE_SSL("TestSequenceSSL"),
      TEST_SEQUENCE_SSL_SESSION_ID("SSLSessionID");

      private String elementName;

      private Element(String var3) {
         this.elementName = var3;
      }

      public String getElementName() {
         return this.elementName;
      }

      public String getQualifiedName(RMVersion var1) {
         return var1.getPrefix() + ":" + this.elementName;
      }

      public QName getQName(RMVersion var1) {
         return new QName(var1.getNamespaceUri(), this.elementName, var1.getPrefix());
      }
   }

   public static enum Action {
      ACK("SequenceAcknowledgement"),
      ACK_REQUESTED("AckRequested"),
      CREATE_SEQUENCE("CreateSequence"),
      CREATE_SEQUENCE_RESPONSE("CreateSequenceResponse"),
      CLOSE_SEQUENCE("CloseSequence"),
      CLOSE_SEQUENCE_RESPONSE("CloseSequenceResponse"),
      TERMINATE_SEQUENCE("TerminateSequence"),
      TERMINATE_SEQUENCE_RESPONSE("TerminateSequenceResponse"),
      LAST_MESSAGE("LastMessage");

      String elementName;

      private Action(String var3) {
         this.elementName = var3;
      }

      public String getElementName() {
         return this.elementName;
      }

      public String getActionURI(RMVersion var1) {
         return var1.getNamespaceUri() + "/" + this.elementName;
      }

      public boolean matchesAnyRMVersion(String var1) {
         RMVersion[] var2 = WsrmConstants.RMVersion.values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            RMVersion var5 = var2[var4];
            if (this.getActionURI(var5).equals(var1)) {
               return true;
            }
         }

         return false;
      }

      public static boolean matchesAnyActionAndRMVersion(String var0) {
         List var1 = getAllActionsForAllRMVersions();
         return var1.contains(var0);
      }

      public static List<String> getAllActionsForAllRMVersions() {
         ArrayList var0 = new ArrayList();
         Action[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Action var4 = var1[var3];
            RMVersion[] var5 = WsrmConstants.RMVersion.values();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               RMVersion var8 = var5[var7];
               StringBuffer var9 = new StringBuffer(var8.getNamespaceUri());
               var9.append("/");
               var9.append(var4.elementName);
               var0.add(var9.toString());
            }
         }

         return var0;
      }

      public static String dumpAllActionsForAllRMVersions() {
         StringBuffer var0 = new StringBuffer();
         List var1 = getAllActionsForAllRMVersions();

         for(int var2 = 0; var2 < var1.size(); ++var2) {
            var0.append((String)var1.get(var2));
            if (var2 < var1.size() - 1) {
               var0.append(", ");
            }
         }

         return var0.toString();
      }

      public static Action valueOfElementName(String var0) {
         Action[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Action var4 = var1[var3];
            if (var4.getElementName().equals(var0)) {
               return var4;
            }
         }

         throw new IllegalArgumentException("No enum in " + Action.class.getName() + " with elementName: " + var0);
      }

      public static VersionInfo getVersionInfo(String var0) {
         VersionInfo var1 = new VersionInfo();
         Action[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Action var5 = var2[var4];
            RMVersion[] var6 = WsrmConstants.RMVersion.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               RMVersion var9 = var6[var8];
               if (var5.getActionURI(var9).equals(var0)) {
                  var1.action = var5;
                  var1.rmVersion = var9;
                  break;
               }
            }
         }

         if (var1.action != null) {
            return var1;
         } else {
            return null;
         }
      }

      public static class VersionInfo {
         public Action action;
         public RMVersion rmVersion;
      }
   }

   public static enum SOAPVersion {
      SOAP_11("soap", "http://schemas.xmlsoap.org/soap/envelope/"),
      SOAP_12("soap12", "http://www.w3.org/2003/05/soap-envelope");

      String prefix;
      String namespaceUri;

      private SOAPVersion(String var3, String var4) {
         this.prefix = var3;
         this.namespaceUri = var4;
      }

      public String getPrefix() {
         return this.prefix;
      }

      public String getNamespaceUri() {
         return this.namespaceUri;
      }
   }

   public static enum RMVersion {
      RM_10("wsrm", "http://schemas.xmlsoap.org/ws/2005/02/rm", "wsrmp", "http://schemas.xmlsoap.org/ws/2005/02/rm/policy"),
      RM_11("wsrm11", "http://docs.oasis-open.org/ws-rx/wsrm/200702", "wsrmp11", "http://docs.oasis-open.org/ws-rx/wsrmp/200702");

      String prefix;
      String namespaceUri;
      String policyPrefix;
      String policyNamespaceUri;

      private RMVersion(String var3, String var4, String var5, String var6) {
         this.prefix = var3;
         this.namespaceUri = var4;
         this.policyPrefix = var5;
         this.policyNamespaceUri = var6;
      }

      public static RMVersion latest() {
         return RM_11;
      }

      public static RMVersion forNamespaceUri(String var0) {
         RMVersion[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            RMVersion var4 = var1[var3];
            if (var4.getNamespaceUri().equals(var0)) {
               return var4;
            }
         }

         return latest();
      }

      public static RMVersion forPolicyNamespaceUri(String var0) {
         RMVersion[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            RMVersion var4 = var1[var3];
            if (var4.getPolicyNamespaceUri().equals(var0)) {
               return var4;
            }
         }

         return latest();
      }

      public String getPrefix() {
         return this.prefix;
      }

      public String getNamespaceUri() {
         return this.namespaceUri;
      }

      public String getPolicyNamespaceUri() {
         return this.policyNamespaceUri;
      }

      public String getPolicyPrefix() {
         return this.policyPrefix;
      }

      public boolean isLaterThan(RMVersion var1) {
         return var1.ordinal() < this.ordinal();
      }

      public boolean isLaterThanOrEqualTo(RMVersion var1) {
         return var1.ordinal() <= this.ordinal();
      }

      public boolean isBefore(RMVersion var1) {
         return var1.ordinal() > this.ordinal();
      }
   }
}
