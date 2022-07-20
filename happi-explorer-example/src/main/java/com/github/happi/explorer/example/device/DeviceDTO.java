package com.github.happi.explorer.example.device;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.config.PropertyOrderStrategy;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;
import java.util.UUID;


@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
@XmlRootElement
public class DeviceDTO {

    @JsonbProperty("id")
    @XmlElement(name = "id")
    private String id;

    @JsonbProperty("name")
    @XmlElement(name = "name")
    private String name;

    @JsonbProperty("location")
    @XmlElement(name = "location")
    private String location;

    public DeviceDTO() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Objects.equals(getId(), deviceDTO.getId()) && Objects.equals(getName(), deviceDTO.getName()) && Objects.equals(getLocation(), deviceDTO.getLocation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getLocation());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
