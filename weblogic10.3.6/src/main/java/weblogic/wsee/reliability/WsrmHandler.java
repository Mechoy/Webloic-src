package weblogic.wsee.reliability;

import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import weblogic.wsee.reliability.headers.AckRequestedHeader;
import weblogic.wsee.reliability.headers.AcknowledgementHeader;
import weblogic.wsee.reliability.headers.SequenceHeader;
import weblogic.wsee.reliability.headers.WsrmHeader;

public class WsrmHandler extends GenericHandler {
   public QName[] getHeaders() {
      ArrayList var1 = new ArrayList();
      var1.addAll(WsrmHeader.getQNames(AcknowledgementHeader.class));
      var1.addAll(WsrmHeader.getQNames(AckRequestedHeader.class));
      var1.addAll(WsrmHeader.getQNames(SequenceHeader.class));
      return (QName[])var1.toArray(new QName[0]);
   }
}
