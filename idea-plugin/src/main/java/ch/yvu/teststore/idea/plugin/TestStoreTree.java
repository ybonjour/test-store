package ch.yvu.teststore.idea.plugin;

import static ch.yvu.teststore.idea.plugin.TreeUtils.getAllChildren;
import static ch.yvu.teststore.idea.plugin.TreeUtils.removeAll;

import ch.yvu.teststore.idea.plugin.load.LoadTask;
import ch.yvu.teststore.idea.plugin.model.Loading;
import ch.yvu.teststore.idea.plugin.model.Model;
import ch.yvu.teststore.idea.plugin.model.TestStore;
import com.intellij.openapi.progress.ProgressManager;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TestStoreTree implements TreeWillExpandListener {

	private static final Loading LOADING = new Loading();
	private static final ch.yvu.teststore.idea.plugin.model.Error ERROR = new ch.yvu.teststore.idea.plugin.model.Error();
	private final DefaultTreeModel model;
	private final JTree tree;

	public TestStoreTree(TestStore testStore) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(testStore);
		model = new DefaultTreeModel(root);
		tree = new JTree(model);
		model.insertNodeInto(loading(), root, 0);
		tree.addTreeWillExpandListener(this);
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				int selectedRow = tree.getRowForLocation(e.getX(), e.getY());
				if (selectedRow == -1)
					return;
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());

				if (SwingUtilities.isRightMouseButton(e)) {
					nodeRightClicked(path, e);
				}

				if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1) {
					nodeDoubleClicked(path);
				}
			}
		});
	}

	public JTree getTree() {
		return tree;
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
		TreePath path = event.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

		Model userModel = (Model) node.getUserObject();
		LoadTask loadTask = userModel.loadChildrenTask();
		if (loadTask == null)
			return;

		loadTask.setLoadListener(new LoadTask.LoadListener() {

			@Override
			public void onSuccess(List<Model> children) {
				List<MutableTreeNode> oldChildren = getAllChildren(node);

				int i = 0;
				for (Model child : children) {
					DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
					model.insertNodeInto(childNode, node, i);
					if (child.loadChildrenTask() != null) {
						model.insertNodeInto(loading(), childNode, 0);
					}
					i += 1;
				}

				removeAll(model, oldChildren);
			}

			@Override
			public void onError(Throwable error) {
				List<MutableTreeNode> children = getAllChildren(node);

				model.insertNodeInto(new DefaultMutableTreeNode(ERROR), node, 0);

				removeAll(model, children);
			}
		});

		ProgressManager.getInstance().run(loadTask);

	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		TreePath path = event.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Model nodeModel = (Model) node.getUserObject();

		removeAll(model, getAllChildren(node));

		if (nodeModel.loadChildrenTask() != null) {
			model.insertNodeInto(loading(), node, 0);
		}
	}

	private static DefaultMutableTreeNode loading() {
		return new DefaultMutableTreeNode(LOADING);
	}

	private void nodeDoubleClicked(TreePath path) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Model model = (Model) node.getUserObject();
		Runnable action = model.doubleClickAction();
		if (action == null)
			return;

		action.run();
	}

	private void nodeRightClicked(TreePath path, MouseEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Model model = (Model) node.getUserObject();
		Runnable action = model.rightClickAction(e);
		if (action == null) {
			return;
		}
		tree.setSelectionPath(path);
		action.run();
	}
}
