/*
 * Ext GWT - Ext for GWT
 * Copyright(c) 2007, 2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */
package com.extjs.gxt.samples.explorer.client.pages;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.DataLoader;
import com.extjs.gxt.ui.client.data.HttpProxy;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.XmlReader;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.viewer.ModelCellLabelProvider;
import com.extjs.gxt.ui.client.viewer.ModelContentProvider;
import com.extjs.gxt.ui.client.viewer.TableViewer;
import com.extjs.gxt.ui.client.widget.Button;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.table.Table;
import com.extjs.gxt.ui.client.widget.table.TableColumn;
import com.extjs.gxt.ui.client.widget.table.TableColumnModel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class XmlTableViewerPage extends Container implements EntryPoint {

  public void onModuleLoad() {
    RootPanel.get().add(this);
  }

  @Override
  protected void onRender(Element parent, int pos) {
    super.onRender(parent, pos);
    
    FlowLayout layout = new FlowLayout();
    layout.margin = 10;
    setLayout(layout);

    List<TableColumn> columns = new ArrayList<TableColumn>();
    columns.add(new TableColumn("Sender", .2f));
    columns.add(new TableColumn("Email", .4f));
    columns.add(new TableColumn("Phone", .2f));
    columns.add(new TableColumn("State", .1f));
    columns.add(new TableColumn("Zip", "Zip Code", .1f));

    // create the column model
    TableColumnModel cm = new TableColumnModel(columns);
    final Table table = new Table(cm);

    final TableViewer viewer = new TableViewer(table);
    viewer.setContentProvider(new ModelContentProvider());

    ModelCellLabelProvider lp = new ModelCellLabelProvider();
    viewer.getViewerColumn(0).setLabelProvider(lp);
    viewer.getViewerColumn(1).setLabelProvider(lp);
    viewer.getViewerColumn(2).setLabelProvider(lp);
    viewer.getViewerColumn(3).setLabelProvider(lp);
    viewer.getViewerColumn(4).setLabelProvider(lp);

    // defines the xml structure
    ModelType type = new ModelType();
    type.root = "records";
    type.recordName = "record";
    type.addField("Sender", "Name");
    type.addField("Email");
    type.addField("Phone");
    type.addField("State");
    type.addField("Zip");

    // use a http proxy to get the data
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "data.xml");
    HttpProxy proxy = new HttpProxy(builder);

    // need a loader, proxy, and reader
    XmlReader reader = new XmlReader(type);
    final DataLoader loader = new DataLoader(proxy, reader);
    loader.addListener(Loader.Load, new Listener<LoadEvent>() {

      public void handleEvent(LoadEvent de) {
        viewer.setInput(de.result.getData());
        table.el.unmask();
      }

    });

    ContentPanel panel = new ContentPanel();
    panel.setFrame(true);
    panel.setCollapsible(true);
    panel.setAnimCollapse(false);
    panel.setButtonAlign(HorizontalAlignment.CENTER);
    panel.setIconStyle("icon-table");
    panel.setHeading("XML TableViewer Demo");
    panel.setLayout(new FitLayout());
    panel.add(table);
    panel.setSize(575, 350);

    // add buttons
    Button load = new Button("Load XML");
    load.addSelectionListener(new SelectionListener() {
      public void componentSelected(ComponentEvent ce) {
        table.el.mask("Loading...");
        loader.load();
      }
    });
    panel.addButton(load);
    add(panel);

  }

}
