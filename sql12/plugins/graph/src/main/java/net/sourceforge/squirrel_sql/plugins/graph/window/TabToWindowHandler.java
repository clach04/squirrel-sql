package net.sourceforge.squirrel_sql.plugins.graph.window;

import net.sourceforge.squirrel_sql.client.session.ISession;
import net.sourceforge.squirrel_sql.fw.gui.GUIUtils;
import net.sourceforge.squirrel_sql.plugins.graph.GraphMainPanelTab;
import net.sourceforge.squirrel_sql.plugins.graph.GraphPanelController;
import net.sourceforge.squirrel_sql.plugins.graph.GraphPlugin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TabToWindowHandler
{
   private GraphMainPanelTab _graphMainPanelTab;
   private ISession _session;
   private GraphWindowController _graphWindowController;

   public TabToWindowHandler(GraphPanelController panelController, ISession session, GraphPlugin plugin)
   {
      _session = session;
      _graphMainPanelTab = new GraphMainPanelTab(panelController, plugin);
      _graphMainPanelTab.getToWindowButton().addActionListener(new ActionListener()
      {
         @Override
         public void actionPerformed(ActionEvent e)
         {
            toWindow();
         }
      });
   }

   private void toWindow()
   {
      Dimension size = _graphMainPanelTab.getComponent().getSize();
      Point screenLoc = GUIUtils.getScreenLocationFor(_graphMainPanelTab.getComponent());

      Rectangle tabBoundsOnScreen = new Rectangle();
      tabBoundsOnScreen.x =screenLoc.x;
      tabBoundsOnScreen.y =screenLoc.y;
      tabBoundsOnScreen.width = size.width;
      tabBoundsOnScreen.height = size.height;



      final int tabIdx = _session.getSessionSheet().removeMainTab(_graphMainPanelTab);

      GraphWindowControllerListener listener = new GraphWindowControllerListener()
      {
         @Override
         public void closing(int tabIdx)
         {
            onWindowClosing(tabIdx);
         }
      };

      _graphWindowController = new GraphWindowController(_session, _graphMainPanelTab, tabIdx, tabBoundsOnScreen, listener);
   }

   private void onWindowClosing(int tabIdx)
   {
      _graphWindowController = null;

      if(tabIdx <_session.getSessionSheet().getTabCount())
      {
         _session.getSessionSheet().insertMainTab(_graphMainPanelTab, tabIdx);
      }
      else
      {
         tabIdx = _session.getSessionSheet().addMainTab(_graphMainPanelTab);
         _session.getSessionSheet().selectMainTab(tabIdx);
      }

   }

   public void showGraph()
   {
      _session.getSessionSheet().addMainTab(_graphMainPanelTab);
   }

   public void removeGraph()
   {
      if (null == _graphWindowController)
      {
         _session.getSessionSheet().removeMainTab(_graphMainPanelTab);
      }
      else
      {
         _graphWindowController.close();
         _graphWindowController = null;
      }
   }

   public void renameGraph(String newName)
   {
      _graphMainPanelTab.setTitle(newName);

      if (null == _graphWindowController)
      {
         int index = _session.getSessionSheet().removeMainTab(_graphMainPanelTab);
         _session.getSessionSheet().insertMainTab(_graphMainPanelTab, index);
      }
      else
      {
         _graphWindowController.rename(newName);
      }
   }

   public String getTitle()
   {
      return _graphMainPanelTab.getTitle();
   }

   public void setTitle(String title)
   {
      _graphMainPanelTab.setTitle(title);
   }
}
