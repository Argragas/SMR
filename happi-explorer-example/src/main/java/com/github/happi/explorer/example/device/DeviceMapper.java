package com.github.happi.explorer.example.device;

import com.github.happi.explorer.EntityMapper;
import com.github.happi.explorer.example.persistence.GenericDAO;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class DeviceMapper implements EntityMapper<DeviceEntity, DeviceDTO> {

    @Inject
    private GenericDAO dao;

    @Override
    public DeviceEntity toEntity(final DeviceDTO data) {
        var entity = Optional
                .ofNullable(data.getId())
                .flatMap(e -> this.dao.find(DeviceEntity.class, e))
                .orElseGet(DeviceEntity::new);

        this.updateEntity(data, entity);

        return entity;
    }

    @Override
    public DeviceDTO fromEntity(final DeviceEntity entity) {
        var data = new DeviceDTO();
        data.setId(entity.getId());
        data.setName(entity.getName());
        data.setLocation(entity.getLocation());

        return data;
    }

    @Override
    public void updateEntity(final DeviceDTO source, final DeviceEntity target) {
        target.setName(source.getName());
        target.setLocation(source.getLocation());

    }
}
