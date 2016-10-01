package org.example.server.system.upload;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("my.storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
