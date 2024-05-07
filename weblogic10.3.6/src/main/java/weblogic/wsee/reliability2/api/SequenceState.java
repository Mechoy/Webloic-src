package weblogic.wsee.reliability2.api;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum SequenceState {
   NEW,
   CREATING,
   CREATED,
   /** @deprecated */
   LAST_MESSAGE_PENDING,
   /** @deprecated */
   LAST_MESSAGE,
   CLOSING,
   CLOSED,
   TERMINATING,
   TERMINATED;

   private static final Logger LOGGER = Logger.getLogger(SequenceState.class.getName());
   private List<SequenceState> _validNextStates;

   private void setValidNextStates(SequenceState[] var1) {
      this._validNextStates = Arrays.asList(var1);
   }

   public boolean isValidTransition(SequenceState var1) {
      if (!this._validNextStates.contains(var1)) {
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Sequence state transition from " + this.name() + " to " + var1.name() + " is not valid. Ignoring");
         }

         return false;
      } else {
         return true;
      }
   }

   public static boolean isTerminalState(SequenceState var0) {
      return var0 == TERMINATING || var0 == TERMINATED;
   }

   public static boolean isClosedState(SequenceState var0) {
      return var0 == CLOSING || var0 == CLOSED;
   }

   static {
      NEW.setValidNextStates(new SequenceState[]{CREATING});
      CREATING.setValidNextStates(new SequenceState[]{CREATED, NEW});
      CREATED.setValidNextStates(new SequenceState[]{LAST_MESSAGE_PENDING, CLOSING, TERMINATING});
      LAST_MESSAGE_PENDING.setValidNextStates(new SequenceState[]{LAST_MESSAGE});
      LAST_MESSAGE.setValidNextStates(new SequenceState[]{CREATED, TERMINATED});
      CLOSING.setValidNextStates(new SequenceState[]{CLOSED, CREATED, TERMINATING});
      CLOSED.setValidNextStates(new SequenceState[]{TERMINATING});
      TERMINATING.setValidNextStates(new SequenceState[]{TERMINATED, CREATED, CLOSING});
      TERMINATED.setValidNextStates(new SequenceState[0]);
   }
}
