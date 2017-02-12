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
	private final DefaultTreeModel treeModel;
	private final JTree tree;
	private final DefaultMutableTreeNode root;

	public TestStoreTree() {
		root = new DefaultMutableTreeNode(LOADING);
		treeModel = new DefaultTreeModel(root);
		tree = new JTree(treeModel);
		tree.setRootVisible(false);
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

	public void addTestStore(TestStore testStore) {
		addModelToTree(testStore, root, root.getChildCount());
		TreePath path = new TreePath(root);
		tree.expandPath(path);
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
					addModelToTree(child, node, i);
					i += 1;
				}

				removeAll(treeModel, oldChildren);
			}

			@Override
			public void onError(Throwable error) {
				List<MutableTreeNode> children = getAllChildren(node);

				addModelToTree(ERROR, node, 0);

				removeAll(treeModel, children);
			}
		});

		ProgressManager.getInstance().run(loadTask);
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
		TreePath path = event.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		Model nodeModel = (Model) node.getUserObject();

		removeAll(treeModel, getAllChildren(node));

		if (nodeModel.loadChildrenTask() != null) {
			addModelToTree(LOADING, node, 0);;
		}
	}

	private void addModelToTree(Model model, MutableTreeNode parent, int idx) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(model);
		treeModel.insertNodeInto(node, parent, idx);
		if (model.loadChildrenTask() != null) {
			treeModel.insertNodeInto(new DefaultMutableTreeNode(LOADING), node, 0);
		}
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
