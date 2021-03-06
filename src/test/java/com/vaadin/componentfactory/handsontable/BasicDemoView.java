package com.vaadin.componentfactory.handsontable;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

@Route("basic")
public class BasicDemoView extends AbstractDemoView {
    private static final String SAMPLE_JSON_DATA =
            " [ \n" +
                    "{\n" +
                    "   \"name\": \"Anne\", \n" +
                    "   \"age\" : 13, \n" +
                    "   \"isMarried\" : false, \n" +
                    "   \"address\": { \n" +
                    "     \"street\": \"#1234, Main Street\", \n" +
                    "     \"zipCode\": \"123456\" \n" +
                    "   }, \n" +
                    "   \"phoneNumber\": \"011-111-1111\"\n" +
                    " }, \n" +
                    "{\n" +
                    "   \"name\": \"John\", \n" +
                    "   \"age\" : 34, \n" +
                    "   \"isMarried\" : false, \n" +
                    "   \"address\": { \n" +
                    "     \"street\": \"#1234, Kurala Street\", \n" +
                    "     \"zipCode\": \"123456\" \n" +
                    "   }, \n" +
                    "   \"phoneNumber\": \"011-222-1111\"\n" +
                    " },\n" +
                    "{\n" +
                    "   \"name\": \"Peter\", \n" +
                    "   \"age\" : 40, \n" +
                    "   \"isMarried\" : false, \n" +
                    "   \"address\": { \n" +
                    "     \"street\": \"#1234, Tor Street\", \n" +
                    "     \"zipCode\": \"123456\" \n" +
                    "   }, \n" +
                    "   \"phoneNumber\": \"011-222-1111\"\n" +
                    " },\n" +
                    "{\n" +
                    "   \"name\": \"Kate\", \n" +
                    "   \"age\" : 45, \n" +
                    "   \"isMarried\" : true, \n" +
                    "   \"address\": { \n" +
                    "     \"street\": \"#1234, Varange Street\", \n" +
                    "     \"zipCode\": \"123456\" \n" +
                    "   }, \n" +
                    "   \"phoneNumber\": \"011-333-1111\"\n" +
                    " }\n" +
                    "]";

    private TextArea textArea;
    private Handsontable handsontable;

    public BasicDemoView() {
        UI.getCurrent().setLocale(Locale.GERMANY);

        add(new H1("Basic usage"));

        textArea = new TextArea("data");
        textArea.setValue(SAMPLE_JSON_DATA);
        textArea.setHeight("500px");
        textArea.setWidth("100%");

        JsonArray data = createJsonObject();
        handsontable = new Handsontable(data);

        Settings settings = new Settings();
        settings.setLicenseKey("non-commercial-and-evaluation");
        settings.setColHeaders(true);
        handsontable.setSettings(settings);
        handsontable.setId("hot1");

        Button setDataButton = new Button("Set data", event -> {
            handsontable.setData(createJsonObject());
        });

        Button retrieveDataButton = new Button("Retrieve data", event -> {
            handsontable.retrieveData(jsonValues -> textArea.setValue(jsonValues.toString()));
        });

        Button retrieveDataAsArrayButton = new Button("Retrieve data as array", event -> {
            handsontable.retrieveDataAsArray(list -> textArea.setValue(list.stream().map(Arrays::toString).collect(Collectors.toList()).toString()));
        });

        Button setCellsMetaButton = new Button("Set cells meta", event -> {
            List<Cell> cells = new ArrayList<>();
            Cell cell;
            cell = new Cell();
            cell.setRow(1);
            cell.setCol(1);
            cell.setBold(true);
            cells.add(cell);

            cell = new Cell();
            cell.setRow(2);
            cell.setCol(1);
            cell.setStrikethrough(true);
            cells.add(cell);

            cell = new Cell();
            cell.setRow(1);
            cell.setCol(2);
            cell.setBorder(true);
            cells.add(cell);

            handsontable.setCellsMeta(cells);
        });

        Button retrieveCellsMetaButton = new Button("Retrieve Cells Meta", event -> {
            handsontable.retrieveCellsMeta(list -> textArea.setValue(list.toString()));
        });

        Button setSettingsButton = new Button("Set settings", event -> {
            Settings newSettings = new Settings();
            newSettings.setLicenseKey("non-commercial-and-evaluation");
            newSettings.setRowHeaders(true);
            newSettings.setColHeaders(new String[]{"1", "2", "3", "4", "5", "6"});
            handsontable.setSettings(newSettings);
        });

        Button changeLanguageToEnglishButton = new Button("English Language", event -> {
            Settings newSettings = new Settings();
            newSettings.setLanguage("en-FI");
            handsontable.setSettings(newSettings);
        });

        Button changeLanguageToGermanButton = new Button("German Language", event -> {
            Settings newSettings = new Settings();
            newSettings.setLanguage("de-DE");
            handsontable.setSettings(newSettings);
        });

        HorizontalLayout buttons = new HorizontalLayout(setDataButton,
                retrieveDataButton, retrieveDataAsArrayButton,
                setCellsMetaButton, retrieveCellsMetaButton, setSettingsButton,
                changeLanguageToEnglishButton, changeLanguageToGermanButton);

        Button receiveSettingsButton = new Button("Receive settings", event -> {
            handsontable.retrieveSettings(receivedSettings -> {
                try (StringWriter writer = new StringWriter()) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper.writeValue(writer, receivedSettings);
                    String stringValue = writer.toString();
                    textArea.setValue(stringValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        Button setDataAtCellButton = new Button("Set data at cell", event -> {
            handsontable.setDataAtCell(1, 1, "abcd");
        });

        Button retriveDataAtCellButton = new Button("Retrieve data at cell", event -> {
            handsontable.retrieveDataAtCell(2, 2, value -> {
                textArea.setValue("The value at the cell [2, 2] is: " + value);
            });
        });

        HorizontalLayout moreButtons = new HorizontalLayout(receiveSettingsButton, setDataAtCellButton, retriveDataAtCellButton);

        add(handsontable, textArea, buttons, moreButtons);
    }

    private JsonArray createJsonObject() {
        JsonReader reader = Json.createReader(new StringReader(textArea.getValue()));
        JsonArray jsonArray = reader.readArray();
        reader.close();
        return jsonArray;
    }
}
