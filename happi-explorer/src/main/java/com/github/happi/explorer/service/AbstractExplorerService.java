package com.github.happi.explorer.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.security.enterprise.SecurityContext;

import com.github.happi.explorer.Action;
import com.github.happi.explorer.DynamicEntry;
import com.github.happi.explorer.EntityMapper;
import com.github.happi.explorer.ExplorerManager;
import com.github.happi.explorer.Jsons;
import com.github.happi.explorer.Validations;
import com.github.happi.explorer.persistence.ExplorerDAO;
import com.github.happi.explorer.persistence.Queries;
import com.github.happi.security.HappiSecurityManager;

/**
 * Base class with basic business logic.
 * Subclass should be an injectable class.
 */
public abstract class AbstractExplorerService implements ExplorerService {

    /**
     * Security context for this module.
     */
    @Inject
    protected SecurityContext securityContext;


    /**
     * Security manager for this module.
     */
    @Inject
    protected HappiSecurityManager securityManager;

    /**
     * Manager for all entry point.
     */
    @Inject
    protected ExplorerManager explorerManager;

    /**
     * Dynamic and generic repository.
     */
    @Inject
    protected ExplorerDAO dao;

    private static final Logger LOG = Logger.getLogger(AbstractExplorerService.class.getName());

    /**
     * Default constructor.
     * Subclass should be an injectable class, don't call this constructor explicitly.
     */
    protected AbstractExplorerService() {
        // NO-OP
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> PaginationData<D> filter(
            final String name,
            final Map<String, List<String>> parameters) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.FILTER);

        var entityClass = entry.getEntityClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var queries = Queries.convertQueries(parameters);

        var entities = this.dao.find(entityClass, queries, this::onFilter);
        var size = this.dao.size(entityClass, queries, this::onFilter);
        var pageSize = Queries.getPageSize(queries);
        var pageNumber = Queries.getPageNumber(queries);
        var pageCount = Queries.getPageCount(queries, size);

        var data = mapper.fromEntity(entities);

        var paginationData = new PaginationData<D>();
        paginationData.setData(data);
        paginationData.setSize(size);
        paginationData.setPageSize(pageSize);
        paginationData.setPageNumber(pageNumber);
        paginationData.setPageCount(pageCount);
        return paginationData;

    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> Optional<D> find(
            final String name,
            final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.FIND);

        var entityClass = entry.getEntityClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var uuid = mapper.mapId(id);

        return this.dao
                .find(entityClass, uuid)
                .map(mapper::fromEntity)
                .map(this::onFind);
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>, K> K create(
            final String name,
            final JsonObject document) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.CREATE);

        var dataClass = entry.getDataClass();
        var mapper = this.explorerManager.invokeMapper(entry);

        var data = Jsons.parse(dataClass, document);
        Validations.validate(data);
        var entity = mapper.toEntity(data);

        if (this.dao.contains(entity)) {
            throw new ServiceExplorerException("Entity already exist !");
        }

        entity = this.onCreate(entity);
        entity = this.dao.save(entity);
        return this.dao.getPrimaryKey(entity);

    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> void update(
            final String name,
            final JsonObject document,
            final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.UPDATE);

        var entityClass = entry.getEntityClass();
        var dataClass = entry.getDataClass();
        var mapper = this.explorerManager.invokeMapper(entry);
        var uuid = mapper.mapId(id);

        var data = Jsons.parse(dataClass, document);
        Validations.validate(data);
        var entity = this.dao
                .find(entityClass, uuid)
                .orElseThrow(() -> new ServiceExplorerException("Entity not exist !"));

        mapper.updateEntity(data, entity);
        this.onUpdate(entity);
        //this.dao.save(entity);
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> void delete(
            final String name,
            final String id) {

        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.DELETE);

        var mapper = this.explorerManager.invokeMapper(entry);
        var entityClass = entry.getEntityClass();
        var uuid = mapper.mapId(id);
        var entity = this.dao
                .find(entityClass, uuid)
                .orElseThrow(() -> new ServiceExplorerException("Entity not exist !"));

        this.onRemove(entity);
        this.dao.remove(entity);
    }

    @Override
    public <E, D, M extends EntityMapper<E, D>> boolean exists(final String name, final String id) {
        var entry = this.explorerManager.<E, D, M, AbstractExplorerService>resolve(name);
        checkAuthorization(entry, Action.FIND);
        var mapper = this.explorerManager.invokeMapper(entry);
        var entityClass = entry.getEntityClass();
        var uuid = mapper.mapId(id);
        return this.dao.contains(entityClass, uuid);
    }

    /**
     * Check if the current user can do an action.
     *
     * @param entry  dynamic entry point
     * @param action business action
     * @throws ActionDeniedException If the user is not authenticated or has not enough authorizations
     */
    protected void checkAuthorization(
            final DynamicEntry<?, ?, ?, ?> entry,
            final Action action) {

        var principal = this.securityContext.getCallerPrincipal();
        var role = entry.getActions().getOrDefault(action, HappiSecurityManager.DENY_ALL);

        var isSecured = this.isSecured();
        var authAccess = Objects.nonNull(principal);
        var publicAccess = Objects.equals(role, HappiSecurityManager.PUBLIC);
        var permitAccess = Objects.equals(role, HappiSecurityManager.PERMIT_ALL);
        var denyAccess = Objects.equals(role, HappiSecurityManager.DENY_ALL);
        var roleAccess = this.securityContext.isCallerInRole(role);

        if (isSecured) {
            if (denyAccess && !permitAccess) {
                throw new ActionDeniedException(entry.getPath(), action, "Unauthorized operation");
            } else if (!authAccess && !publicAccess ) {
                throw new ActionDeniedException(entry.getPath(), action, "User not authenticate");
            } else if (authAccess && !permitAccess && !roleAccess) {
                throw new ActionDeniedException(entry.getPath(), action, "Insufficient authorization");
            }
        }

        // Authorization granted:
        // with role @Public
        // OR
        // authenticate user with role @PermitAll
        // OR
        // authenticate user with in a role

    }

    private boolean isSecured() {
        var secured = this.securityManager
                .getConfiguration(HappiSecurityManager.Configuration.JAKARTA_SECURITY);

        return Objects.equals(Boolean.parseBoolean(secured), Boolean.TRUE);
    }
}

