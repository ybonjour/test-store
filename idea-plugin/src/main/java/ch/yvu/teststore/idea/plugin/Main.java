package ch.yvu.teststore.idea.plugin;

import ch.yvu.teststore.idea.plugin.model.Model;
import ch.yvu.teststore.idea.plugin.model.TestStore;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class Main {

	private JPanel content;

	public JPanel getContent() {
		return content;
	}

	private void createUIComponents() {
		content = new JPanel(new BorderLayout());
		TestStoreTree testStoreTree = new TestStoreTree(new TestStore());
		JTree tree = testStoreTree.getTree();
		tree.setCellRenderer(new TestStoreTreeRenderer());
		JScrollPane scroll = new JScrollPane(tree);
		content.add(scroll, BorderLayout.CENTER);

	}

	class TestStoreTreeRenderer implements TreeCellRenderer {

		@Override
		public Component getTreeCellRendererComponent(
				JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {

			Model model = (Model) ((DefaultMutableTreeNode) value).getUserObject();

			JLabel l = new JLabel(model.getText());

			Icon icon = model.getIcon();
			if(icon != null) {
				l.setIcon(icon);
			}

			return l;
		}
	}
}
