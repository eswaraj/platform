package com.eswaraj.domain.nodes;

import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;

/**
 * Category of a complaint 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
@NodeEntity
public class Setting extends BaseNode{

    public enum SettingNames {
        ALLOW_COMPLAINT("allow.complaint", "Setting to disbale complaint creation in the system, set value as False to disable"), FAKE_DELHI_POINTS("fake.location",
                "Seting to enable Fake Delhi Points");

        private String name;
        private String description;

        SettingNames(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
	private String name;
	private String description;
    private String value;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
