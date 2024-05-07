package weblogic.wsee.message;

import javax.xml.namespace.QName;

public interface MsgHeaderFactoryIntf {
   MsgHeader createMsgHeader(QName var1) throws MsgHeaderException;
}
