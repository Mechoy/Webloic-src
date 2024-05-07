package weblogic.wsee.message;

import java.util.Iterator;
import org.w3c.dom.NodeList;

public interface MsgHeaders {
   MsgHeader getHeader(MsgHeaderType var1) throws MsgHeaderException;

   void addHeader(MsgHeader var1) throws MsgHeaderException;

   void addHeaders(NodeList var1) throws MsgHeaderException;

   boolean isEmpty();

   Iterator listHeaders();
}
