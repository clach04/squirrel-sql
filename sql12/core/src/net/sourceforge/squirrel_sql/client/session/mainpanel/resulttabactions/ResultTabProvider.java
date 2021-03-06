package net.sourceforge.squirrel_sql.client.session.mainpanel.resulttabactions;

import net.sourceforge.squirrel_sql.client.session.ISQLPanelAPI;
import net.sourceforge.squirrel_sql.client.session.mainpanel.IResultTab;
import net.sourceforge.squirrel_sql.client.session.mainpanel.ISQLResultExecuter;
import net.sourceforge.squirrel_sql.client.session.mainpanel.ResultTab;

public class ResultTabProvider
{
   private ISQLPanelAPI _panel;
   private ResultTab _resultTab;

   public ResultTabProvider(ResultTab resultTab)
   {
      _resultTab = resultTab;
   }

   public boolean setSQLPanelAPI(ISQLPanelAPI panel)
   {
      _panel = panel;
      return null != _panel;
   }

   public ResultTab getResultTab()
   {
      if (null != _resultTab)
      {
         return _resultTab;
      }

      if (_panel != null)
      {
         ISQLResultExecuter sqlResultExecuter = _panel.getSQLResultExecuter();
         if (sqlResultExecuter != null)
         {
            IResultTab selectedResultTab = sqlResultExecuter.getSelectedResultTab();
            if (selectedResultTab != null)
            {
               return (ResultTab) selectedResultTab;
            }
         }
      }

      return null;
   }

   public boolean hasResultTab()
   {
      return null != getResultTab();
   }
}
