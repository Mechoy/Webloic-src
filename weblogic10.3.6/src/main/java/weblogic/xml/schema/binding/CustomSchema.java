package weblogic.xml.schema.binding;

import java.io.Reader;
import javax.xml.namespace.QName;

public interface CustomSchema {
   Reader getSchema(QName var1, String var2);
}
