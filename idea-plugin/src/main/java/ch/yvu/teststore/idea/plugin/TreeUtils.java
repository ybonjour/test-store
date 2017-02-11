package ch.yvu.teststore.idea.plugin;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.LinkedList;
import java.util.List;

public class TreeUtils {

	public static List<MutableTreeNode> getAllChildren(MutableTreeNode node) {
		List<MutableTreeNode> children = new LinkedList<>();
		for (int i = 0; i < node.getChildCount(); i++) {
			TreeNode child = node.getChildAt(i);
			if (child instanceof MutableTreeNode) {
				children.add((MutableTreeNode) child);
			}
		}

		return children;
	}

	public static void removeAll(DefaultTreeModel model, List<MutableTreeNode> nodes) {
		for(MutableTreeNode node : nodes) {
			model.removeNodeFromParent(node);
		}
	}
}
