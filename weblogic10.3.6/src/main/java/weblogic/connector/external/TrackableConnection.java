package weblogic.connector.external;

public interface TrackableConnection {
   boolean isLocalTransactionInProgress();

   void connectionClosed();
}
