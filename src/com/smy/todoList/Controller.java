package com.smy.todoList;

import com.smy.todoList.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private ListView<TodoItem> listView;

    @FXML
    private TextArea detailView;

    @FXML
    private Label dueDate;


    public void initialize() {

        List<TodoItem> todoList = new ArrayList<>();

        todoList.add(new TodoItem("Goto Office","Goto LambdaTheta for learning",
                LocalDate.of(2020,8,13)));


        todoList.add(new TodoItem("Come back home","Come Back home through bykea",
                LocalDate.of(2020,8,13)));

        listView.getItems().setAll(todoList);

        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        detailView.editableProperty().setValue(false);

        listView.getSelectionModel().selectFirst();
        if (listView.getSelectionModel().isSelected(0))
            detailView.setText(listView.getItems().get(0).getShortDescription());

    }


    @FXML
    public void handleClickListView() {

        TodoItem item = listView.getSelectionModel().getSelectedItem();





        detailView.setText(item.getLongDescription());
        dueDate.setText(item.getDueDate().toString());

    }

}
