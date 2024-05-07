package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.util.Enumeration;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.selectors.AndSelector;
import org.apache.tools.ant.types.selectors.ContainsRegexpSelector;
import org.apache.tools.ant.types.selectors.ContainsSelector;
import org.apache.tools.ant.types.selectors.DateSelector;
import org.apache.tools.ant.types.selectors.DependSelector;
import org.apache.tools.ant.types.selectors.DepthSelector;
import org.apache.tools.ant.types.selectors.DifferentSelector;
import org.apache.tools.ant.types.selectors.ExtendSelector;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.types.selectors.MajoritySelector;
import org.apache.tools.ant.types.selectors.NoneSelector;
import org.apache.tools.ant.types.selectors.NotSelector;
import org.apache.tools.ant.types.selectors.OrSelector;
import org.apache.tools.ant.types.selectors.PresentSelector;
import org.apache.tools.ant.types.selectors.SelectSelector;
import org.apache.tools.ant.types.selectors.SelectorContainer;
import org.apache.tools.ant.types.selectors.SizeSelector;
import org.apache.tools.ant.types.selectors.TypeSelector;
import org.apache.tools.ant.types.selectors.modifiedselector.ModifiedSelector;

/** @deprecated */
@Deprecated
public abstract class MatchingJavacTask extends DelegatingJavacTask implements SelectorContainer {
   private FileSet fileSet = new FileSet();

   public void setProject(Project var1) {
      this.fileSet.setProject(var1);
      super.setProject(var1);
   }

   public PatternSet.NameEntry createInclude() {
      return this.fileSet.createInclude();
   }

   public PatternSet.NameEntry createIncludesFile() {
      return this.fileSet.createIncludesFile();
   }

   public PatternSet.NameEntry createExclude() {
      return this.fileSet.createExclude();
   }

   public PatternSet.NameEntry createExcludesFile() {
      return this.fileSet.createExcludesFile();
   }

   public PatternSet createPatternSet() {
      return this.fileSet.createPatternSet();
   }

   public void setIncludes(String var1) {
      this.fileSet.setIncludes(var1);
   }

   public void setExcludes(String var1) {
      this.fileSet.setExcludes(var1);
   }

   public void setDefaultexcludes(boolean var1) {
      this.fileSet.setDefaultexcludes(var1);
   }

   public void setIncludesfile(File var1) {
      this.fileSet.setIncludesfile(var1);
   }

   public void setExcludesfile(File var1) {
      this.fileSet.setExcludesfile(var1);
   }

   public void setCaseSensitive(boolean var1) {
      this.fileSet.setCaseSensitive(var1);
   }

   public void setFollowSymlinks(boolean var1) {
      this.fileSet.setFollowSymlinks(var1);
   }

   public boolean hasSelectors() {
      return this.fileSet.hasSelectors();
   }

   public int selectorCount() {
      return this.fileSet.selectorCount();
   }

   public FileSelector[] getSelectors(Project var1) {
      return this.getSelectors(var1);
   }

   public Enumeration selectorElements() {
      return this.fileSet.selectorElements();
   }

   public void appendSelector(FileSelector var1) {
      this.fileSet.appendSelector(var1);
   }

   public void addSelector(SelectSelector var1) {
      this.fileSet.addSelector(var1);
   }

   public void addAnd(AndSelector var1) {
      this.fileSet.addAnd(var1);
   }

   public void addOr(OrSelector var1) {
      this.fileSet.addOr(var1);
   }

   public void addNot(NotSelector var1) {
      this.fileSet.addNot(var1);
   }

   public void addNone(NoneSelector var1) {
      this.fileSet.addNone(var1);
   }

   public void addMajority(MajoritySelector var1) {
      this.fileSet.addMajority(var1);
   }

   public void addDate(DateSelector var1) {
      this.fileSet.addDate(var1);
   }

   public void addSize(SizeSelector var1) {
      this.fileSet.addSize(var1);
   }

   public void addFilename(FilenameSelector var1) {
      this.fileSet.addFilename(var1);
   }

   public void addCustom(ExtendSelector var1) {
      this.fileSet.addCustom(var1);
   }

   public void addContains(ContainsSelector var1) {
      this.fileSet.addContains(var1);
   }

   public void addPresent(PresentSelector var1) {
      this.fileSet.addPresent(var1);
   }

   public void addDepth(DepthSelector var1) {
      this.fileSet.addDepth(var1);
   }

   public void addDepend(DependSelector var1) {
      this.fileSet.addDepend(var1);
   }

   public void addContainsRegexp(ContainsRegexpSelector var1) {
      this.fileSet.addContainsRegexp(var1);
   }

   public void addDifferent(DifferentSelector var1) {
      this.fileSet.addDifferent(var1);
   }

   public void addType(TypeSelector var1) {
      this.fileSet.addType(var1);
   }

   public void addModified(ModifiedSelector var1) {
      this.fileSet.addModified(var1);
   }

   public void add(FileSelector var1) {
      this.fileSet.add(var1);
   }

   protected FileSet getImplicitFileSet() {
      return this.fileSet;
   }
}
