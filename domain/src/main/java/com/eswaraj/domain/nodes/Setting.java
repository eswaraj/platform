package com.eswaraj.domain.nodes;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.eswaraj.domain.base.BaseNode;

/**
 * Category of a complaint 
 * @author ravi
 * @date Jan 18, 2014
 *
 */
@NodeEntity
@TypeAlias("Setting")
public class Setting extends BaseNode{

    public enum SettingNames {
        ALLOW_COMPLAINT("allow.complaint", "Setting to disbale complaint creation in the system, set value as False to disable", "true"), FAKE_DELHI_POINTS("fake.location",
                "Setting to enable Fake Delhi Points", "false"), ALLOW_POLITICAL_ADMIN_SEARCH("allow.political.admin.search", "Setting to enable search political Admin in search API", "false"), ALLOW_COMPLAINT_FROM_UNKNOWN_LOCATION(
                "allow.complaint.from.unknown.location", "Setting to enable/disable Complaint Creation if location was not found, true means complaint can be created", "false"), MESSAGE_COMPLAINT_FROM_UNKNOWN_LOCATION(
                "message.complaint.from.unknown.location", "Error message shown to user if user location is not supported",
                "Currently eSwaraj is not supported in your area, it will be coming soon in your area"), 
                DAILY_MAX_COMPLAINT_PER_USER("daily.max.complaints.per.user","Maximum Number of Complaints user Can create per day", "5");

        private String name;
        private String description;
        private String defaultValue;

        SettingNames(String name, String description, String defaultValue) {
            this.name = name;
            this.description = description;
            this.defaultValue = defaultValue;
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

        public String getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
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

    @Override
    public String toString() {
        return "Setting [name=" + name + ", description=" + description + ", value=" + value + ", type=" + type + "]";
    }

}
