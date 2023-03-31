package jiux.net.plugin.restful.navigator;


import com.intellij.ide.util.treeView.TreeState;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import com.intellij.ui.treeStructure.SimpleTree;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.tree.TreeSelectionModel;
import jiux.net.RestfulToolkitBundle;
import jiux.net.plugin.restful.common.ToolkitIcons;
import jiux.net.plugin.utils.ToolkitUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Nullable;

/**
 * @author baihua.huang
 */
@State(name = "RestServicesNavigator", storages = {@Storage(StoragePathMacros.WORKSPACE_FILE)})
public class RestServicesNavigator extends AbstractProjectComponent
        implements PersistentStateComponent<RestServicesNavigatorState>, ProjectComponent {
    public static final Logger LOG = Logger.getInstance(RestServicesNavigator.class);
    public static final String TOOL_WINDOW_ID = "RestServices";
    private static final URL SYNC_ICON_URL = RestServicesNavigator.class.getResource("/actions/refresh.png");
    protected final Project project;
    protected RestServiceStructure myStructure;
    protected RestServicesNavigatorState myState = new RestServicesNavigatorState();
    private SimpleTree myTree;
    private ToolWindowEx myToolWindow;

    public RestServicesNavigator(Project project) {
        super(project);
        this.project = project;
    }


    public static RestServicesNavigator getInstance(Project p) {
        return p.getComponent(RestServicesNavigator.class);
    }


    private void initTree() {
        myTree = new SimpleTree() {


            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                final JLabel myLabel = new JLabel(
                        RestfulToolkitBundle.message("toolkit.navigator.nothing.to.display", ToolkitUtil.formatHtmlImage(SYNC_ICON_URL)));

                if (project.isInitialized()) {
                    return;
                }
                myLabel.setFont(getFont());
                myLabel.setBackground(getBackground());
                myLabel.setForeground(getForeground());
                Rectangle bounds = getBounds();
                Dimension size = myLabel.getPreferredSize();
                myLabel.setBounds(0, 0, size.width, size.height);

                int x = (bounds.width - size.width) / 2;
                Graphics g2 = g.create(bounds.x + x, bounds.y + 20, bounds.width, bounds.height);
                try {
                    myLabel.paint(g2);
                } finally {
                    g2.dispose();
                }
            }
        };
        myTree.getEmptyText().clear();
        myTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
    }

    @Override
    public void initComponent() {
        listenForProjectsChanges();
        ToolkitUtil.runWhenInitialized(project, () -> {
            if (project.isDisposed()) {
                return;
            }
            initToolWindow();
        });
    }

    private void initToolWindow() {
        final ToolWindowManagerEx manager = ToolWindowManagerEx.getInstanceEx(project);
        myToolWindow = (ToolWindowEx) manager.getToolWindow(TOOL_WINDOW_ID);
        if (myToolWindow != null) {
            return;
        }

        initTree();

        myToolWindow = (ToolWindowEx) manager.registerToolWindow(TOOL_WINDOW_ID, false, ToolWindowAnchor.RIGHT, project, true);
        myToolWindow.setIcon(ToolkitIcons.SERVICE);

        final JPanel panel = new RestServicesNavigatorPanel(project, myTree);
        final ContentFactory contentFactory = ServiceManager.getService(ContentFactory.class);
        final Content content = contentFactory.createContent(panel, "", false);
        ContentManager contentManager = myToolWindow.getContentManager();
        contentManager.addContent(content);
        contentManager.setSelectedContent(content, false);
    }


    public void scheduleStructureUpdate() {
        scheduleStructureRequest(() -> myStructure.update());

    }

    private void scheduleStructureRequest(final Runnable r) {
        if (myToolWindow == null) {
            return;
        }
        ToolkitUtil.runWhenProjectIsReady(project, () -> {
            if (!myToolWindow.isVisible()) {
                return;
            }

            boolean shouldCreate = myStructure == null;
            if (shouldCreate) {
                initStructure();
            }

            r.run();
// fixme: compat
//            if (shouldCreate) {
//                TreeState.createFrom(myState.treeState).applyTo(myTree);
//            }
        });


    }

    private void initStructure() {
        myStructure = new RestServiceStructure(project, myTree);
    }


    private void listenForProjectsChanges() {
        //todo :
    }

    @Nullable
    @Override
    public RestServicesNavigatorState getState() {
        ApplicationManager.getApplication().assertIsDispatchThread();
        if (myStructure != null) {
            try {
                myState.treeState = new Element("root");
                TreeState.createOn(myTree).writeExternal(myState.treeState);
            } catch (WriteExternalException e) {
                LOG.warn(e);
            }
        }
        return myState;
    }

    @Override
    public void loadState(RestServicesNavigatorState state) {
        myState = state;
        scheduleStructureUpdate();
    }
}
