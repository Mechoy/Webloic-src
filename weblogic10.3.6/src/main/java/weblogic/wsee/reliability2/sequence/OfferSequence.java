package weblogic.wsee.reliability2.sequence;

import java.io.Serializable;

public interface OfferSequence<T extends Sequence> extends Serializable {
   T getMainSequence();
}
