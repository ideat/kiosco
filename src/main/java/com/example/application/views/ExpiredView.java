package com.example.application.views;

import com.flowingcode.vaadin.addons.wcstories.WCStories;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.router.RouterLayout;

import java.util.Arrays;
import java.util.List;

@PageTitle("Opciones")
@Route("expired")
public class ExpiredView extends VerticalLayout implements RouterLayout {

    public ExpiredView(){
//        this.getElement().getStyle().set("background","whitesmoke");
//        this.getElement().getStyle().set("background-image","url('/backgrounds/expired.png')");
        this.getElement().getStyle().set("background","whitesmoke");
        VerticalLayout layout = createMainLayout();
        layout.getElement().getStyle().set("background","whitesmoke");
        add(layout);

        this.setHorizontalComponentAlignment(Alignment.CENTER,layout);
        this.setSizeFull();
    }

    private VerticalLayout createMainLayout(){
        VerticalLayout layout = new VerticalLayout();
        layout.getElement().getStyle().set("background", "whitesmoke");
        Button btnExit = new Button(new Image("/buttons/Botones-14.png","Inicial"));
        btnExit.addClickListener(click ->   UI.getCurrent().getPage().executeJs("javascript:window.close('','_parent','');"));

        Div spaceV = new Div();
        spaceV.setHeight("20px");

        Div spaceV1 = new Div();
        spaceV1.setHeight("60px");

        Div divButton = new Div();
        divButton.add(btnExit);
//        divButton.setHeight("200px");

        List storiesPaths =
                Arrays.asList(
                        "/jpg/ahorro.jpg",
                        "/jpg/bancadigital.jpg",
                        "/jpg/creditos.jpg",
                        "/jpg/dpf.jpg",
                        "/jpg/inicial.jpg",
                        "/jpg/personal.jpg",
                        "/jpg/td.jpg");

        WCStories wcstories = new WCStories(storiesPaths);

//        wcstories.setSizeFull();
        wcstories.setHeight(800);
        wcstories.setWidth(1600);
        wcstories.setRadius(30);
//        wcstories.setWithShadow(true);

//        layout.setHeight("800px");
//        layout.setWidth("1600px");
        layout.setSizeFull();
        layout.add(spaceV1,divButton,spaceV,wcstories);
        layout.setHorizontalComponentAlignment(Alignment.CENTER,divButton,wcstories);

//        wcstories.addAttachListener(click -> UI.getCurrent().getPage().executeJs("javascript:window.close('','_parent','');") );
        return layout;
    }
}
