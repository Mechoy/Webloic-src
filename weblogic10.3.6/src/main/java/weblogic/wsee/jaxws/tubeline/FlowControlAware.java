package weblogic.wsee.jaxws.tubeline;

import com.sun.xml.ws.api.pipe.Tube;

public interface FlowControlAware extends Tube {
   boolean isFlowControlRequired();
}
