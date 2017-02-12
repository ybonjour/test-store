package ch.yvu.teststore.idea.plugin;

import ch.yvu.teststore.idea.plugin.model.Model;
import ch.yvu.teststore.idea.plugin.model.TestStore;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class Main {

	private static final String KEY_TESTSTORES = "ch.yvu.teststore.idea.plugin.TestStores";

	private JPanel toolWindow;
	private TestStoreTree testStoreTree;

	public JPanel getToolWindow() {
		return toolWindow;
	}

	private void createUIComponents() {
		SimpleToolWindowPanel toolWindow = new SimpleToolWindowPanel(true, true);

		JComponent content = createContent();
		JComponent toolbar = createToolbar();
		toolWindow.setToolbar(toolbar);
		toolWindow.add(content);

		this.toolWindow = toolWindow;
	}

	private JComponent createToolbar() {
		AnAction addTestStore = new AnAction() {

			@Override
			public void actionPerformed(AnActionEvent e) {
				AddTestStoreDialog dialog = new AddTestStoreDialog(WindowFactory.currentProject);
				dialog.show();
			}
		};
		addTestStore.getTemplatePresentation().setIcon(AllIcons.ToolbarDecorator.Add);

		DefaultActionGroup actionGroup = new DefaultActionGroup();
		actionGroup.add(addTestStore);
		JPanel toolBarPanel = new JPanel(new GridLayout());
		ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, actionGroup, true);
		toolBarPanel.add(toolbar.getComponent());

		return toolBarPanel;
	}

	private JComponent createContent() {
		testStoreTree = new TestStoreTree();
		JTree tree = testStoreTree.getTree();
		tree.setCellRenderer(new TestStoreTreeRenderer());
		return new JScrollPane(tree);
	}

	private void addTestStore(String url) {
		if (testStoreTree == null)
			return;
		testStoreTree.addTestStore(new TestStore(url));
	}

	class TestStoreTreeRenderer implements TreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(
				JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

			Model model = (Model) ((DefaultMutableTreeNode) value).getUserObject();

			JLabel l = new JLabel(model.getText());

			Icon icon = model.getIcon();
			if (icon != null) {
				l.setIcon(icon);
			}

			return l;
		}
	}

	class AddTestStoreDialog extends DialogWrapper {

		private JTextField url;

		protected AddTestStoreDialog(@Nullable Project project) {
			super(project, false);
			init();
			setTitle("Test Store URL");
		}

		@Nullable
		@Override
		protected JComponent createCenterPanel() {
			url = new JTextField();
			return url;
		}

		@Nullable
		@Override
		public JComponent getPreferredFocusedComponent() {
			return url;
		}

		@NotNull
		@Override
		protected Action[] createActions() {
			return new Action[] { getOKAction(), getCancelAction() };
		}

		@Override
		protected void doOKAction() {
			addTestStore(url.getText());
			super.doOKAction();
		}
	}
}
