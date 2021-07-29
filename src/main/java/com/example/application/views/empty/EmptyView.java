package com.example.application.views.empty;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Empty")
@Route(value = "kiosco")
@RouteAlias(value = "")
public class EmptyView extends Div {

    public EmptyView() {
        addClassName("empty-view");
        add(new Text("Content placeholder"));
    }

}
