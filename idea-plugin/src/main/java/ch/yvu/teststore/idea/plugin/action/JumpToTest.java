package ch.yvu.teststore.idea.plugin.action;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;

public class JumpToTest implements Runnable {

	private final String className;
	private final String methodName;
	private final Project project;

	public JumpToTest(Project project, String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
		this.project = project;
	}

	@Override
	public void run() {
		JavaPsiFacade facade = JavaPsiFacade.getInstance(project);
		PsiClass psiClass = facade.findClass(className, GlobalSearchScope.allScope(project));
		if (psiClass == null) {
			return;
		}
		PsiMethod[] psiMethod = psiClass.findMethodsByName(methodName, false);
		if (psiMethod.length == 0) {
			return;
		}
		psiMethod[0].navigate(true);
	}
}
