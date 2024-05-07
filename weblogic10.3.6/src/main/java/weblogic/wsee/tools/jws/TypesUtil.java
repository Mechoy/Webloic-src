package weblogic.wsee.tools.jws;

import com.bea.util.jam.JClass;
import com.bea.util.jam.JParameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebMethodDecl;
import weblogic.wsee.tools.jws.decl.WebParamDecl;
import weblogic.wsee.util.JamUtil;

public class TypesUtil {
   private static List collectionTypes = new ArrayList();

   public static boolean isCollection(JClass var0) {
      return collectionTypes.contains(var0.getQualifiedName());
   }

   public static void processReturnTypes(JClass var0, WebMethodDecl var1, int var2, ProcessStrategy var3, ReportStrategy var4) throws WsBuildException {
      JClass var5 = var1.getJMethod().getReturnType();
      if (var5 != null && !var5.isVoidType()) {
         processType(var5, var1.getWebResult().getTypeClassNames(), var1, var0, var2, var3, var4);
      }

   }

   public static void processParamTypes(JClass var0, WebMethodDecl var1, int var2, ProcessStrategy var3, ReportStrategy var4) throws WsBuildException {
      Iterator var5 = var1.getWebParams();

      while(var5.hasNext()) {
         WebParamDecl var6 = (WebParamDecl)var5.next();
         JParameter var7 = var6.getJParameter();
         String[] var8 = var6.getTypeClassNames();
         processType(var7.getType(), var8, var1, var0, var2, var3, var4);
      }

   }

   private static void processType(JClass var0, String[] var1, WebMethodDecl var2, JClass var3, int var4, ProcessStrategy var5, ReportStrategy var6) throws WsBuildException {
      if (var1 != null) {
         for(int var7 = 0; var7 < var1.length; ++var7) {
            JClass var8 = JamUtil.loadJClass(var1[var7], var0.getClassLoader(), true);
            if (var8 != null) {
               if (var8.isUnresolvedType()) {
                  var6.report(new JwsLogEvent(var2.getJMethod(), "types.unresolved", new Object[]{var1[var7], var2.getMethodName()}));
               }

               if (!var0.isAssignableFrom(var8) && !var8.isAssignableFrom(var0) && !isCollection(var0)) {
                  var6.report(new JwsLogEvent(var2.getJMethod(), "types.invalid.noRelationship", new Object[]{var2.getMethodName(), var0.getQualifiedName(), var8.getQualifiedName()}));
               }

               var5.process(var3, var8, var4);
            }
         }
      }

   }

   static {
      collectionTypes.add(Collection.class.getName());
      collectionTypes.add(List.class.getName());
      collectionTypes.add(ArrayList.class.getName());
      collectionTypes.add(LinkedList.class.getName());
      collectionTypes.add(Vector.class.getName());
      collectionTypes.add(Stack.class.getName());
      collectionTypes.add(Set.class.getName());
      collectionTypes.add(TreeSet.class.getName());
      collectionTypes.add(SortedSet.class.getName());
      collectionTypes.add(HashSet.class.getName());
   }

   public interface ProcessStrategy {
      void process(JClass var1, JClass var2, int var3);
   }

   public interface ReportStrategy {
      void report(JwsLogEvent var1) throws WsBuildException;
   }
}
