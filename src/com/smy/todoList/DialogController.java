package com.smy.todoList;

import com.smy.todoList.datamodel.TodoData;
import com.smy.todoList.datamodel.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {

    @FXML
    TextField shortDescription;

    @FXML
    TextArea details;

    @FXML
    DatePicker date;

    public TodoItem processResult() {

        String shortDescription = this.shortDescription.getText().trim();
        String details = this.details.getText().trim();
        LocalDate localDate = date.getValue();

        TodoItem newTodoItem = new TodoItem(shortDescription,details,localDate);
        TodoData.getInstance().addTodoItem(newTodoItem);

        return newTodoItem;

    }

    public void preEditItem(TodoItem item) {

        shortDescription.setText(item.getShortDescription());
        details.setText(item.getLongDescription());
        date.setValue(item.getDueDate());

    }




}
