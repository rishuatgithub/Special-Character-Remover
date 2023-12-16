package org.pentaho.kettle.step.plugins.scr.model;

import lombok.Data;

@Data
public class SCRPojo {
    private String outputField;
    private String inputDropData;
    private String inputDropDataIndex;
    private String[] algoBoxItems;
    private String algoBoxItemsSelected;
    private String customCode;

    public void setAlgoBoxItems() {
        StringBuilder algorithms = new StringBuilder();
        algorithms.append("Remove all the Special Characters other than A-Z,a-z,0-9 including white-spaces");
        algorithms.append("Remove all the Special Characters other than A-Z,a-z,0-9 keep the white-spaces");
        algorithms.append("Remove anything outside ASCII code 0 to 255");
        algorithms.append("Remove Unicode Block");
        algorithms.append("Keep Unicode Block, remove the rest");
        algorithms.append("Keep A-Z,a-z,0-9 and ADD Exceptions");
        algorithms.append("Custom Regular Expression");

        this.algoBoxItems =
    }
}
