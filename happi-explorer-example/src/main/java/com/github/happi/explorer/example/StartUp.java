package com.github.happi.explorer.example;

import java.util.Map;

import com.github.happi.explorer.example.device.DeviceDTO;
import com.github.happi.explorer.example.device.DeviceEntity;
import com.github.happi.explorer.example.device.DeviceMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import com.github.happi.explorer.Action;
import com.github.happi.explorer.DynamicEntry;
import com.github.happi.explorer.ExplorerManager;
import com.github.happi.explorer.example.customer.CustomerDTO;
import com.github.happi.explorer.example.customer.CustomerEntity;
import com.github.happi.explorer.example.customer.CustomerMapper;
import com.github.happi.explorer.example.endpoint.WebConfiguration;
import com.github.happi.explorer.example.gender.GenderDTO;
import com.github.happi.explorer.example.gender.GenderEntity;
import com.github.happi.explorer.example.gender.GenderMapper;
import com.github.happi.explorer.example.security.Roles;
import com.github.happi.security.HappiSecurityManager;
import com.github.happi.explorer.service.BasicExplorerService;

@ApplicationScoped
public class StartUp {

    @Inject
    private HappiSecurityManager securityManager;

    @Inject
    private ExplorerManager explorerManager;

    public StartUp() {
    }

    public void start(@Observes @Initialized(ApplicationScoped.class) final Object pointless) {

        this.securityManager.scanRoleClassConfiguration(WebConfiguration.class);
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.MANAGER_ENDPOINT, "true");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.DISCOVERY_ENDPOINT, "true");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.JAKARTA_SECURITY, "true");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.SECRET, "secret");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.TOKEN_CLAIM_USERNAME, "preferred_username");
        this.securityManager.putConfiguration(HappiSecurityManager.Configuration.TOKEN_CLAIM_GROUPS, "groups");

        this.explorerManager.register(new DynamicEntry<>(
                "gender",
                Map.of(
                        Action.FILTER, HappiSecurityManager.PUBLIC,
                        Action.FIND, HappiSecurityManager.PUBLIC
                ),
                GenderEntity.class,
                GenderDTO.class,
                GenderMapper.class,
                BasicExplorerService.class
        ));

        this.explorerManager.register(new DynamicEntry<>(
                "customer",
                Map.of(
                        Action.FILTER, Roles.CUSTOMER_MANAGER,
                        Action.FIND, Roles.CUSTOMER_MANAGER,
                        Action.CREATE, Roles.CUSTOMER_MANAGER,
                        Action.UPDATE, Roles.CUSTOMER_MANAGER,
                        Action.DELETE, Roles.CUSTOMER_MANAGER
                ),
                CustomerEntity.class,
                CustomerDTO.class,
                CustomerMapper.class,
                BasicExplorerService.class
        ));

        this.explorerManager.register(new DynamicEntry<>(
                "device",
                Map.of(
                        Action.FILTER, HappiSecurityManager.PERMIT_ALL,
                        Action.FIND, HappiSecurityManager.PERMIT_ALL,
                        Action.CREATE, Roles.DEVICE_MANAGER,
                        Action.UPDATE, Roles.DEVICE_MANAGER,
                        Action.DELETE, Roles.DEVICE_MANAGER
                ),
                DeviceEntity.class,
                DeviceDTO.class,
                DeviceMapper.class,
                BasicExplorerService.class
        ));

    }
}
