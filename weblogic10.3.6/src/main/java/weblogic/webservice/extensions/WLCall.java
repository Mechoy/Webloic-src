package weblogic.webservice.extensions;

import java.util.Iterator;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;

/** @deprecated */
public interface WLCall extends Call {
   Iterator getParameterNames();

   Class getParameterJavaType(String var1);

   ParameterMode getParameterMode(String var1);
}
