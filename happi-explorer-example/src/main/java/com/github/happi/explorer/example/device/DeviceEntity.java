package com.github.happi.explorer.example.device;

import com.github.happi.explorer.example.persistence.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

@Entity
@Table(name = "device")
@Access(AccessType.FIELD)
public class DeviceEntity extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Size(max = 255)
    @Column(name = "location", nullable = false)
    private String location;


    public DeviceEntity() {
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
