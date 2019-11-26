package ua.lyohha.window;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import ua.lyohha.page.Page;

import java.util.ArrayList;
import java.util.List;

public class Navigation {

    private List<Parent> pages = new ArrayList<>();
    private Pane pane;

    public Navigation(Pane pane) {
        this.pane = pane;
    }

    public Object navigateTo(Class c) {
        Object o = null;
        try {
            o = c.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader();

        Parent pageContent = null;
        if (o != null) {
            try {
                if (((Page) o).getPage() != null)
                    pageContent = loader.load(c.getResource(((Page) o).getPage()).openStream());
                else
                    pageContent = ((Page) o).getParent();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (pageContent != null) {
            if (((Page) o).getStyleClass() != null)
                pageContent.getStylesheets().add(c.getResource(((Page) o).getStyleClass()).toExternalForm());
            GridPane.setRowIndex(pageContent, 0);
            pages.add(pageContent);
            ((Page)loader.getController()).navigation = this;
            ((Page)loader.getController()).initializeComponent();
            updateView();
            return loader.getController();
        }
        return null;
    }

    private void updateView() {
        if (pages.size() != 0) {
            pane.getChildren().clear();
            pane.getChildren().add(pages.get(pages.size() - 1));
        }
    }

    public void navigateBack() {
        if (pages.size() > 1) {
            pages.remove(pages.size() - 1);
            updateView();
        }
    }

    public void clearNavigationStack() {
        if(pages.size() != 0)
        {
            Parent parent = pages.get(pages.size()-1);
            pages.clear();
            pages.add(parent);
        }
    }
}