package weblogic.management.tools;

import com.bea.staxb.buildtime.Java2SchemaTask;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.types.Path;

public class JavaToSchema extends Java2SchemaTask {
   public JavaToSchema() {
      this.project = new Project();
      this.project.init();
      this.taskType = "javaToschema";
      this.taskName = "javaToschema";
      this.target = new Target();
   }

   public Path convertStringToPath(String var1) {
      Path var2 = new Path(this.project, var1);
      return var2;
   }
}
