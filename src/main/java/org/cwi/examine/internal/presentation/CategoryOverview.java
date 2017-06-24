package org.cwi.examine.internal.presentation;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.cwi.examine.internal.data.DataSet;
import org.cwi.examine.internal.data.Network;
import org.cwi.examine.internal.model.Model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kdinkla on 10/8/16.
 */
public class CategoryOverview extends VBox {

    private final Model model;

    public CategoryOverview(final Model model) {
        this.model = model;

        final DataSet dataSet = model.getDataSet();
        dataSet.superNetwork.addListener((observable, oldValue, network) -> setupTables(network));
    }

    private void setupTables(final Network network) {
        final List<CategoryTable> tables =
                network.categories
                .stream()
                .map(category -> new CategoryTable(category))
                .collect(Collectors.toList());
        tables.forEach(table -> VBox.setVgrow(table, Priority.ALWAYS));
        getChildren().setAll(tables);
    }
}
