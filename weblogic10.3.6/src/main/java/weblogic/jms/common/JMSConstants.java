package weblogic.jms.common;

public final class JMSConstants {
   public static final String ACKNOWLEDGE_MODE_AUTO = "Auto";
   public static final String ACKNOWLEDGE_MODE_CLIENT = "Client";
   public static final String ACKNOWLEDGE_MODE_DUPS_OK = "Dups-Ok";
   public static final String ACKNOWLEDGE_MODE_NONE = "None";
   public static final int ACKNOWLEDGE_ALL = 1;
   public static final int ACKNOWLEDGE_PREVIOUS = 2;
   public static final int ACKNOWLEDGE_ONE = 3;
   public static final int ACKNOWLEDGE_DEFAULT = 1;
   public static final String JMS_BEA_STATE_VISIBLE = "visible";
   public static final String JMS_BEA_STATE_PENDING_ORDERED = "pending-ordered";
   public static final String JMS_BEA_STATE_PENDING_TRANSACTIONAL_RECEIVE = "pending-transactional_receive";
   public static final String JMS_BEA_STATE_PENDING_TRANSACTIONAL_SEND = "pending-transactional_send";
   public static final String JMS_BEA_STATE_PENDING_UNACKNOWLEDGED = "pending-unacknowledged";
   public static final String JMS_BEA_STATE_PENDING_SCHEDULED = "pending-scheduled";
   public static final String JMS_BEA_STATE_PENDING_LOCKED = "pending-locked";
   public static final String JMS_BEA_STATE_PENDING_INSERTION_PAUSED = "pending-insertion-paused";
   public static final String RECONNECT_POLICY_NONE = "none".intern();
   public static final String RECONNECT_POLICY_PRODUCER = "producer".intern();
   public static final String RECONNECT_POLICY_ALL = "all".intern();
   public static final long DEFAULT_RECONNECT_BLOCKING_MILLIS = 60000L;
   public static final long DEFAULT_TOTAL_RECONNECT_PERIOD = -1L;
   public static final int MESSAGE_EXPIRED = 0;
   public static final int WORK_EXPIRED = 1;
   public static final int DELIVERY_LIMIT_REACHED = 2;
   public static final int CLIENT_ID_POLICY_RESTRICTED = 0;
   public static final int CLIENT_ID_POLICY_UNRESTRICTED = 1;
   public static final String CLIENT_ID_POLICY_RESTRICTED_STRING = "Restricted".intern();
   public static final String CLIENT_ID_POLICY_UNRESTRICTED_STRING = "Unrestricted".intern();
   public static final int CLIENT_ID_POLICY_DEFAULT = 0;
   public static final String SUBSCRIPTION_EXCLUSIVE = "Exclusive".intern();
   public static final int SUBSCRIPTION_EXCLUSIVE_INT = 0;
   public static final String SUBSCRIPTION_SHARABLE = "Sharable".intern();
   public static final int SUBSCRIPTION_SHARABLE_INT = 1;
}
