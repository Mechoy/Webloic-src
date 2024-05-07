package weblogic.wsee.tools.jws.build;

import com.bea.util.jam.JClass;
import java.util.Iterator;
import weblogic.wsee.WebServiceType;
import weblogic.wsee.tools.jws.decl.port.PortDecl;
import weblogic.wsee.util.cow.CowReader;

public interface JwsInfo {
   JClass getJClass();

   Iterator<PortDecl> getPorts();

   CowReader getCowReader();

   WebServiceType getType();

   boolean isGenerateWsdl();

   boolean isGenerateDescriptors();
}
