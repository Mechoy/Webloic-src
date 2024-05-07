package weblogic.wsee.reliability2.api;

import java.util.SortedSet;
import weblogic.wsee.reliability.MessageRange;
import weblogic.wsee.reliability2.exception.WsrmException;
import weblogic.wsee.reliability2.sequence.SourceMessageInfo;
import weblogic.wsee.reliability2.sequence.UnknownSourceSequenceException;

public interface WsrmClient {
   void dispose();

   void reset();

   String getId();

   String getSequenceId();

   void setSequenceId(String var1);

   SequenceState getSequenceState();

   void requestAcknowledgement() throws WsrmException;

   SortedSet<MessageRange> getAckRanges() throws UnknownSourceSequenceException;

   long getMostRecentMessageNumber();

   SourceMessageInfo getMessageInfo(long var1) throws UnknownSourceSequenceException;

   void setFinalMessage();

   void closeSequence() throws WsrmException;

   /** @deprecated */
   void sendWsrm10EmptyLastMessage() throws WsrmException;

   void terminateSequence() throws WsrmException;
}
